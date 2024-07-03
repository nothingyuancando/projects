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
        <el-col :span="24">
          <el-card shadow="hover">
            <div slot="header" class="clearfix">
              <span>工厂概览信息</span>
            </div>
            <div class="overview-text">
              <p>工厂概览展示了当前设备的运行状态、生产效益和订单统计情况。</p>
              <p>通过这些信息，您可以更好地了解工厂的整体运作情况，及时进行调整和优化。</p>
              <p>定期刷新设备运行情况，切换生产效益图表类型以查看不同的展示效果。</p>
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
  setInterval(refreshDeviceChart, 300000000000000); // 每分钟刷新一次设备运行情况图表
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

.overview-text {
  padding: 20px;
  font-size: 16px;
  line-height: 1.5;

  p {
    margin: 10px 0;
  }
}
</style>

