<template>
  <el-container class="dashboard-container">
    <el-header>
      <h2>工厂概览</h2>
    </el-header>
    <el-main>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-card shadow="hover">
            <div slot="header" class="clearfix">
              <span>设备运行情况</span>
              <el-button @click="refreshDeviceChart" type="primary" size="mini" style="float: right;">刷新</el-button>
            </div>
            <div ref="deviceChart" class="chart"></div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="hover">
            <div slot="header" class="clearfix">
              <span>工厂生产效益情况</span>
              <el-button @click="switchEfficiencyChartType" type="primary" size="mini" style="float: right;">切换图表</el-button>
            </div>
            <div ref="efficiencyChart" class="chart"></div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="hover">
            <div slot="header" class="clearfix">
              <span>订单统计情况</span>
            </div>
            <div ref="orderChart" class="chart"></div>
          </el-card>
        </el-col>
      </el-row>
      <el-row :gutter="20" style="margin-top: 20px;">
        <el-col :span="8">
          <el-card shadow="hover">
            <div slot="header" class="clearfix">
              <span>工厂能耗情况</span>
            </div>
            <div ref="energyChart" class="chart"></div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="hover">
            <div slot="header" class="clearfix">
              <span>员工绩效情况</span>
            </div>
            <div ref="performanceChart" class="chart"></div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="hover">
            <div slot="header" class="clearfix">
              <span>原材料库存情况</span>
            </div>
            <div ref="inventoryChart" class="chart"></div>
          </el-card>
        </el-col>
      </el-row>
    </el-main>
  </el-container>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import * as echarts from 'echarts';

const deviceChart = ref(null);
const efficiencyChart = ref(null);
const orderChart = ref(null);
const energyChart = ref(null);
const performanceChart = ref(null);
const inventoryChart = ref(null);
let efficiencyChartType = ref('line');

onMounted(() => {
  initCharts();
  setInterval(refreshDeviceChart, 60000); // 每分钟刷新一次设备运行情况图表
});

const initCharts = () => {
  // Initialize Device Running Status Chart
  const deviceChartInstance = echarts.init(deviceChart.value);
  const deviceChartOption = {
    title: { text: '设备运行情况' },
    tooltip: {},
    legend: { data: ['设备状态'] },
    xAxis: { data: ['设备1', '设备2', '设备3', '设备4', '设备5'] },
    yAxis: {},
    series: [{
      name: '设备状态',
      type: 'bar',
      data: [5, 20, 36, 10, 10]
    }]
  };
  deviceChartInstance.setOption(deviceChartOption);

  // Initialize Factory Efficiency Chart
  const efficiencyChartInstance = echarts.init(efficiencyChart.value);
  const efficiencyChartOption = {
    title: { text: '工厂生产效益情况' },
    tooltip: {},
    legend: { data: ['生产效益'] },
    xAxis: { data: ['1月', '2月', '3月', '4月', '5月', '6月'] },
    yAxis: {},
    series: [{
      name: '生产效益',
      type: efficiencyChartType.value,
      data: [150, 230, 224, 218, 135, 147]
    }]
  };
  efficiencyChartInstance.setOption(efficiencyChartOption);

  // Initialize Order Statistics Chart
  const orderChartInstance = echarts.init(orderChart.value);
  const orderChartOption = {
    title: { text: '订单统计情况' },
    tooltip: {},
    legend: { data: ['订单数量'] },
    xAxis: { data: ['1月', '2月', '3月', '4月', '5月', '6月'] },
    yAxis: {},
    series: [{
      name: '订单数量',
      type: 'bar',
      data: [60, 80, 70, 90, 100, 120]
    }]
  };
  orderChartInstance.setOption(orderChartOption);

  // Initialize Energy Consumption Chart
  const energyChartInstance = echarts.init(energyChart.value);
  const energyChartOption = {
    title: { text: '工厂能耗情况' },
    tooltip: {},
    legend: { data: ['能耗'] },
    xAxis: { data: ['1月', '2月', '3月', '4月', '5月', '6月'] },
    yAxis: {},
    series: [{
      name: '能耗',
      type: 'line',
      data: [200, 180, 240, 210, 230, 250]
    }]
  };
  energyChartInstance.setOption(energyChartOption);

  // Initialize Employee Performance Chart
  const performanceChartInstance = echarts.init(performanceChart.value);
  const performanceChartOption = {
    title: { text: '员工绩效情况' },
    tooltip: {},
    legend: { data: ['绩效'] },
    xAxis: { data: ['1月', '2月', '3月', '4月', '5月', '6月'] },
    yAxis: {},
    series: [{
      name: '绩效',
      type: 'bar',
      data: [80, 90, 85, 88, 92, 95]
    }]
  };
  performanceChartInstance.setOption(performanceChartOption);

  // Initialize Inventory Statistics Chart
  const inventoryChartInstance = echarts.init(inventoryChart.value);
  const inventoryChartOption = {
    title: { text: '原材料库存情况' },
    tooltip: {},
    legend: { data: ['库存'] },
    xAxis: { data: ['1月', '2月', '3月', '4月', '5月', '6月'] },
    yAxis: {},
    series: [{
      name: '库存',
      type: 'line',
      data: [300, 320, 310, 305, 330, 340]
    }]
  };
  inventoryChartInstance.setOption(inventoryChartOption);
};

const refreshDeviceChart = () => {
  // Simulate data refresh
  const deviceChartInstance = echarts.init(deviceChart.value);
  const newData = Array.from({ length: 5 }, () => Math.floor(Math.random() * 50));
  deviceChartInstance.setOption({
    series: [{
      data: newData
    }]
  });
};

const switchEfficiencyChartType = () => {
  efficiencyChartType.value = efficiencyChartType.value === 'line' ? 'bar' : 'line';
  initCharts();
};
</script>

<style scoped lang="scss">
.dashboard-container {
  width: 1300px;
  height: 800px;
  margin: 0 auto;
}

.chart {
  width: 100%;
  height: 400px;
}
</style>
