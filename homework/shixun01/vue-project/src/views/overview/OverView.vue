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
            </div>
            <div ref="deviceChart" class="chart"></div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="hover">
            <div slot="header" class="clearfix">
              <span>工厂生产效益情况</span>
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
    </el-main>
  </el-container>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import * as echarts from 'echarts';

const deviceChart = ref(null);
const efficiencyChart = ref(null);
const orderChart = ref(null);

onMounted(() => {
  initCharts();
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
      type: 'line',
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
</script>

<style scoped lang="scss">
.dashboard-container {
  height: 100vh;
}

.chart {
  width: 100%;
  height: 400px;
}
</style>
