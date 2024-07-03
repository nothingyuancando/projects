<template>
  <el-container class="dashboard-container">
    <el-header>
      <h2>智能制造云平台概览</h2>
    </el-header>
    <el-main>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-card shadow="hover">
            <div slot="header" class="clearfix">
              <span>设备运行状态</span>
              <el-button @click="refreshDeviceChart" type="primary" size="mini" style="float: right;">刷新</el-button>
            </div>
            <div ref="deviceChart" class="chart"></div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="hover">
            <div slot="header" class="clearfix">
              <span>生产效益分析</span>
              <el-button @click="switchEfficiencyChartType" type="primary" size="mini" style="float: right;">切换图表</el-button>
            </div>
            <div ref="efficiencyChart" class="chart"></div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="hover">
            <div slot="header" class="clearfix">
              <span>订单处理进度</span>
            </div>
            <div ref="orderChart" class="chart"></div>
          </el-card>
        </el-col>
      </el-row>
      <el-row :gutter="20" style="margin-top: 20px;">
        <el-col :span="24">
          <el-card shadow="hover">
            <div slot="header" class="clearfix">
              <span>平台概览信息</span>
            </div>
            <div class="overview-text">
              <p>平台概览展示了当前设备的运行状态、生产效益分析和订单处理进度。</p>
              <p>通过这些信息，您可以更好地了解智能制造平台的整体运作情况，及时进行调整和优化。</p>
              <p>定期刷新设备运行状态，切换生产效益图表类型以查看不同的展示效果。</p>
            </div>
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
let efficiencyChartType = ref('line');

onMounted(() => {
  initCharts();
  setInterval(refreshDeviceChart, 300000); // 每5分钟刷新一次设备运行状态图表
});

const initCharts = () => {
  // Initialize Device Running Status Chart
  const deviceChartInstance = echarts.init(deviceChart.value);
  const deviceChartOption = {
    title: { text: '设备运行状态' },
    tooltip: {},
    legend: { data: ['运行时间', '利用率'] },
    xAxis: { data: ['设备1', '设备2', '设备3', '设备4', '设备5'] },
    yAxis: {},
    series: [
      {
        name: '运行时间',
        type: 'bar',
        data: [5, 20, 36, 10, 10]
      },
      {
        name: '利用率',
        type: 'line',
        data: [70, 80, 90, 60, 50]
      }
    ]
  };
  deviceChartInstance.setOption(deviceChartOption);

  // Initialize Production Efficiency Chart
  const efficiencyChartInstance = echarts.init(efficiencyChart.value);
  const efficiencyChartOption = {
    title: { text: '生产效益分析' },
    tooltip: {},
    legend: { data: ['生产效率', '产能利用率', '生产成本'] },
    xAxis: { data: ['1月', '2月', '3月', '4月', '5月', '6月'] },
    yAxis: {},
    series: [
      {
        name: '生产效率',
        type: efficiencyChartType.value,
        data: [150, 230, 224, 218, 135, 147]
      },
      {
        name: '产能利用率',
        type: 'line',
        data: [60, 80, 75, 85, 90, 95]
      },
      {
        name: '生产成本',
        type: 'line',
        data: [3000, 2500, 2700, 2900, 3100, 2800]
      }
    ]
  };
  efficiencyChartInstance.setOption(efficiencyChartOption);

  // Initialize Order Processing Progress Chart
  const orderChartInstance = echarts.init(orderChart.value);
  const orderChartOption = {
    title: { text: '订单处理进度' },
    tooltip: {},
    legend: { data: ['新订单', '进行中订单', '已完成订单'] },
    xAxis: { data: ['1月', '2月', '3月', '4月', '5月', '6月'] },
    yAxis: {},
    series: [
      {
        name: '新订单',
        type: 'bar',
        data: [20, 30, 40, 35, 50, 60]
      },
      {
        name: '进行中订单',
        type: 'bar',
        data: [15, 25, 35, 30, 45, 55]
      },
      {
        name: '已完成订单',
        type: 'bar',
        data: [5, 10, 20, 25, 30, 40]
      }
    ]
  };
  orderChartInstance.setOption(orderChartOption);
};

const refreshDeviceChart = () => {
  // Simulate data refresh
  const deviceChartInstance = echarts.init(deviceChart.value);
  const newData = Array.from({ length: 5 }, () => Math.floor(Math.random() * 50));
  deviceChartInstance.setOption({
    series: [
      {
        data: newData
      }
    ]
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

.overview-text {
  padding: 20px;
  font-size: 16px;
  line-height: 1.5;

  p {
    margin: 10px 0;
  }
}
</style>
