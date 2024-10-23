<template>
  <div id="app">
    <header>
      <h1>管线铺设辅助系统</h1>
      <el-button type="primary" @click="calculateOptimalPath">计算最优方案</el-button>
      <input type="file" @change="handleFileUpload" accept=".txt" />
    </header>

    <div v-if="state.isCalculated" class="result-section">
      <h3>最优方案：</h3>
      <pre>{{ result }}</pre>

      <div class="content-container">
        <div id="graph" class="graph"></div>
        
        <div class="info-container">
          <h3>管道铺设信息：</h3>
          <ul v-if="state.edges.length > 0">
            <li v-for="(edge, index) in state.edges" :key="index">
              从 {{ state.buildings[edge.source] }} 到 {{ state.buildings[edge.target] }} (距离: {{ edge.distance }} 米)
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue';
import * as d3 from 'd3';

// 状态管理
const state = reactive({
  adjacencyMatrix: [], // 邻接矩阵
  buildings: [],       // 建筑物名称
  result: '',         // 计算结果
  edges: [],          // 最优方案的边
  isCalculated: false  // 是否已计算最优方案
});

// 读取文件内容并解析
const handleFileUpload = (event) => {
  const file = event.target.files[0];
  if (file) {
    const reader = new FileReader();
    reader.onload = (e) => {
      const content = e.target.result;
      parseFileContent(content);
    };
    reader.readAsText(file);
  }
};

// 解析文件内容
const parseFileContent = (content) => {
  const lines = content.split('\n');
  state.buildings = [];
  state.adjacencyMatrix = []; // 确保每次解析时重置

  lines.forEach(line => {
    const parts = line.trim().split(' ');
    if (parts.length === 3) {
      const [source, target, distance] = parts;
      if (!state.buildings.includes(source)) {
        state.buildings.push(source);
      }
      if (!state.buildings.includes(target)) {
        state.buildings.push(target);
      }

      const rowIndex = state.buildings.indexOf(source);
      const colIndex = state.buildings.indexOf(target);

      // 确保邻接矩阵是正确大小
      while (state.adjacencyMatrix.length <= Math.max(rowIndex, colIndex)) {
        state.adjacencyMatrix.push(Array(state.adjacencyMatrix.length + 1).fill(0));
        state.adjacencyMatrix.forEach(row => row.push(0));
      }

      state.adjacencyMatrix[rowIndex][colIndex] = parseInt(distance, 10);
      state.adjacencyMatrix[colIndex][rowIndex] = parseInt(distance, 10); // 假设是无向图
    }
  });

  // 确保在计算最优方案时邻接矩阵和建筑物数组都已更新
  if (state.buildings.length > 0) {
    calculateOptimalPath();
  }
};

// Prim 算法计算最小生成树
const primAlgorithm = (matrix) => {
  const n = matrix.length;
  const inMST = Array(n).fill(false);
  const key = Array(n).fill(Infinity);
  const parent = Array(n).fill(-1);
  key[0] = 0;

  for (let count = 0; count < n - 1; count++) {
    let min = Infinity;
    let u = -1;

    for (let v = 0; v < n; v++) {
      if (!inMST[v] && key[v] < min) {
        min = key[v];
        u = v;
      }
    }

    inMST[u] = true;

    for (let v = 0; v < n; v++) {
      if (matrix[u][v] && !inMST[v] && matrix[u][v] < key[v]) {
        parent[v] = u;
        key[v] = matrix[u][v];
      }
    }
  }

  let result = "建筑物之间的最短路径：\n";
  for (let i = 1; i < n; i++) {
    if (parent[i] !== -1) {
      result += `${state.buildings[parent[i]]} -> ${state.buildings[i]} (距离: ${matrix[i][parent[i]]})\n`;
      state.edges.push({ source: parent[i], target: i, distance: matrix[i][parent[i]] });
    }
  }
  state.result = result;
  state.isCalculated = true;
  drawGraph();
};

// 计算最优方案
const calculateOptimalPath = () => {
  state.edges = []; // 重置边数据
  primAlgorithm(state.adjacencyMatrix);
};

// 绘制图形
const drawGraph = () => {
  const width = 750; // 图形的宽度
  const height = 650; // 图形的高度
  d3.select('#graph').selectAll('*').remove(); // 清除之前的图形

  const svg = d3.select('#graph')
    .append('svg')
    .attr('width', width)
    .attr('height', height)
    .attr('viewBox', `0 0 ${width} ${height}`)
    .attr('preserveAspectRatio', 'xMidYMid meet');

  const nodes = state.buildings.map((building, index) => ({
    id: index,
    name: building
  }));

  const links = state.edges.map(edge => ({
    source: nodes[edge.source],
    target: nodes[edge.target],
    distance: edge.distance
  }));

  const simulation = d3.forceSimulation(nodes)
    .force("link", d3.forceLink().id(d => d.id).distance(100))
    .force("charge", d3.forceManyBody().strength(-50))
    .force("center", d3.forceCenter(width / 2, height / 2))
    .on("tick", () => {
      link.attr("x1", d => d.source.x)
          .attr("y1", d => d.source.y)
          .attr("x2", d => d.target.x)
          .attr("y2", d => d.target.y);

      node.attr("cx", d => d.x)
          .attr("cy", d => d.y);
        
      svg.selectAll('text.node-label')
        .attr('x', d => d.x)
        .attr('y', d => d.y);
        
      svg.selectAll('text.edge-label')
        .attr('x', d => (d.source.x + d.target.x) / 2)
        .attr('y', d => (d.source.y + d.target.y) / 2);
    });

  const link = svg.append('g')
    .attr('class', 'links')
    .selectAll('line')
    .data(links)
    .enter().append('line')
    .attr('stroke-width', d => Math.sqrt(d.distance))
    .attr('stroke', '#999');

  const node = svg.append('g')
    .attr('class', 'nodes')
    .selectAll('circle')
    .data(nodes)
    .enter().append('circle')
    .attr('r', 5)
    .attr('fill', 'blue')
    .call(d3.drag()
      .on("start", dragstarted)
      .on("drag", dragged)
      .on("end", dragended));

  svg.selectAll('text.node-label')
    .data(nodes)
    .enter().append('text')
    .attr('class', 'node-label')
    .text(d => d.name)
    .attr('font-size', '10px')
    .attr('dy', -10);

  svg.selectAll('text.edge-label')
    .data(links)
    .enter().append('text')
    .attr('class', 'edge-label')
    .text(d => `${d.distance}米`)
    .attr('font-size', '10px')
    .attr('dy', -5);

  const totalLength = state.edges.reduce((sum, edge) => sum + edge.distance, 0);
  svg.append('text')
    .attr('x', 10)
    .attr('y', 20)
    .text(`总长度: ${totalLength}米`)
    .attr('font-size', '12px')
    .attr('fill', 'red');

  function dragstarted(event, d) {
    if (!event.active) simulation.alphaTarget(0.3).restart();
    d.fx = d.x;
    d.fy = d.y;
  }

  function dragged(event, d) {
    d.fx = event.x;
    d.fy = event.y;
  }

  function dragended(event, d) {
    if (!event.active) simulation.alphaTarget(0);
    d.fx = null;
    d.fy = null;
  }
};
</script>

<style scoped>
.header {
  line-height: 1.5;
  padding: 20px;
  text-align: center;
}

.result-section {
  margin-top: 20px;
}

.content-container {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}

.graph {
  border: 1px solid #ccc;
  height: 650px;
  width: 750px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.info-container {
  width: 300px;
  margin-left: 20px;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  margin: 5px 0;
  font-size: 14px;
}
</style>
