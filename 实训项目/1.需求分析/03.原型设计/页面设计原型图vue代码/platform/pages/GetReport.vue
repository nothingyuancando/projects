<template>
  <div class="work-report-management">
    <!-- 报工汇总部分（左侧） -->
    <el-card class="box-card work-report-summary__card">
      <div slot="header" class="clearfix">
        <h2>报工汇总</h2>
      </div>      <div ref="workOrderChart" style="width: 100%; height: 400px;"></div>
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
          <el-table-column prop="reportId" label="报工编号"></el-table-column>
          <el-table-column prop="factoryName" label="工厂名称"></el-table-column>
          <el-table-column prop="productName" label="产品名称"></el-table-column>
          <el-table-column prop="quantity" label="报工数量"></el-table-column>
          <el-table-column prop="reportDate" label="报工日期"></el-table-column>
          <el-table-column prop="status" label="状态"></el-table-column>
          <el-table-column label="操作">
            <template #default="scope">
              <el-button size="mini" @click="handleView(scope.row)">查看</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>

    <!-- 查看报工弹窗 -->
    <el-dialog :visible.sync="viewDialogVisible" title="报工详情">
      <el-descriptions :border="true" :column="1">
        <el-descriptions-item label="报工编号">{{ currentReport?.reportId }}</el-descriptions-item>
        <el-descriptions-item label="工厂名称">{{ currentReport?.factoryName }}</el-descriptions-item>
        <el-descriptions-item label="产品名称">{{ currentReport?.productName }}</el-descriptions-item>
        <el-descriptions-item label="报工数量">{{ currentReport?.quantity }}</el-descriptions-item>
        <el-descriptions-item label="报工日期">{{ currentReport?.reportDate }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ currentReport?.status }}</el-descriptions-item>
        <el-descriptions-item label="报工详情">{{ currentReport?.reportDetails }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="viewDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>


  </div>
</template>

<script setup>


import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { getWorkReportList } from '@/api/workReport.js';

// 存储报工记录数据
const workReports = ref([]);

// 弹窗可见性
const viewDialogVisible = ref(false);


// 当前处理的报工记录
const currentReport = ref(null);

const fetchWorkReports = async () => {
  try {
    const response = await getWorkReportList();
    workReports.value = response.data;
  } catch (error) {
    ElMessage.error('获取报工记录失败');
  }
};









onMounted(fetchWorkReports);


import * as echarts from 'echarts';

const workOrderChart = ref(null); // 创建一个引用来保存工单汇总图表对象

onMounted(() => {
  // 假设这里有获取工单数据的逻辑，此处为示例直接写死数据
  const months = ['工厂1', '工厂2', '工厂3', '工厂4', '工厂5', '工厂6'];
  const newOrders = [20, 30, 40, 35, 50, 60]; // 新订单数据
  const ongoingOrders = [15, 25, 35, 30, 45, 55]; // 进行中订单数据
  const completedOrders = [5, 10, 20, 25, 30, 40]; // 已完成订单数据

  // 创建工单汇总图表
  const chartInstance = echarts.init(workOrderChart.value);
  const option = {
    title: { text: '工单处理进度' },
    tooltip: {},
    legend: { data: ['新工单', '进行中工单', '已完成工单'] },
    xAxis: { data: months },
    yAxis: {},
    series: [
      {
        name: '新工单',
        type: 'bar',
        data: newOrders
      },
      {
        name: '进行中工单',
        type: 'bar',
        data: ongoingOrders
      },
      {
        name: '已完成工单',
        type: 'bar',
        data: completedOrders
      }
    ]
  };
  chartInstance.setOption(option);
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
