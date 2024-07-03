<template>
  <div class="work-report-management">
    <!-- 报工汇总部分（左侧） -->
    <el-card class="box-card work-report-summary__card">
      <div slot="header" class="clearfix">
        <h2>报工汇总</h2>
      </div>
      <div ref="workOrderChart" style="width: 100%; height: 400px;"></div>
      <div class="chart-description">
        <p>本图表显示了不同月份的工单处理进度，包括最近几个工厂的新工单、进行中工单和已完成工单的数量。</p>
      </div>
    </el-card>

    <!-- 报工列表部分（右侧） -->
    <el-card class="box-card work-report-management__card">
      <div slot="header" class="clearfix">
        <h2>报工管理</h2>
      </div>

      <div class="work-report-list-section">
        <el-table :data="workReports" style="width: 100%">
          <el-table-column prop="workorder_id" label="工单编号"></el-table-column>
          <el-table-column
              prop="producing_states"
              label="状态"
              :formatter="formatState"
          ></el-table-column>
          <el-table-column prop="workreport_time" label="报工时间"></el-table-column>
          <el-table-column prop="processing_number" label="处理数量"></el-table-column>
          <el-table-column prop="qualify_number" label="合格数量"></el-table-column>
          <el-table-column prop="work_hour" label="工作时长"></el-table-column>
        </el-table>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import {ref, onMounted, nextTick} from 'vue';
import {ElMessage} from 'element-plus';
import {getWorkReportList} from '@/api/factory/getreport.js';
import * as echarts from 'echarts';

// 存储报工记录数据
const workReports = ref([]);

// 引用图表 DOM 元素
const workOrderChart = ref(null);

// 获取报工记录数据
const fetchWorkReports = async () => {
  try {
    const response = await getWorkReportList();
    if (response.code === 200) {
      workReports.value = response.data;
      updateChart();
    } else {
      ElMessage.error('获取报工记录失败');
    }
  } catch (error) {
    ElMessage.error('获取报工记录失败');
  }
};

// 更新图表数据
const updateChart = () => {
  const months = ['1月', '2月', '3月', '4月', '5月', '6月'];
  const newOrders = [10, 20, 15, 30, 25, 40];
  const ongoingOrders = [5, 15, 10, 25, 20, 35];
  const completedOrders = [3, 8, 7, 12, 10, 18];

  // 确保 DOM 更新完成后初始化 ECharts 实例
  nextTick(() => {
    const chartInstance = echarts.init(workOrderChart.value);
    const option = {
      title: {text: '工单处理进度'},
      tooltip: {},
      legend: {data: ['新工单', '进行中工单', '已完成工单']},
      xAxis: {data: months},
      yAxis: {},
      series: [
        {name: '新工单', type: 'bar', data: newOrders},
        {name: '进行中工单', type: 'bar', data: ongoingOrders},
        {name: '已完成工单', type: 'bar', data: completedOrders}
      ]
    };
    chartInstance.setOption(option);
  });
};

// 状态格式化函数
const formatState = (state) => {
  switch (state) {
    case 3:
      return '生产中';
    case 4:
      return '已完成';
    default:
      return '未知状态';
  }
};

onMounted(() => {
  fetchWorkReports();
});
</script>

<style scoped lang="scss">
.work-report-management {
  padding: 20px;
  background: #f5f5f5;
  height: 100%;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}

.work-report-summary__card {
  width: 48%;
}

.work-report-management__card {
  width: 48%;
}

.work-report-list-section {
  width: 100%;
}

.el-input,
.el-select {
  width: 300px;
}

.el-table {
  width: 100%;
}

.dialog-footer {
  text-align: right;
}

.work-report-summary {
  padding: 20px;
}

.chart-description {
  margin-top: 10px;
}
</style>
