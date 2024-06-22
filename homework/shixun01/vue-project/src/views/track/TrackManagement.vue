<template>
  <div class="track-management">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <h2>生产跟踪</h2>
      </div>
      <div class="report-section">
        <el-form :model="report" ref="reportForm" label-width="120px">
          <el-form-item label="工单">
            <el-select v-model="report.workOrderId" placeholder="选择工单">
              <el-option
                  v-for="workOrder in startedWorkOrders"
                  :key="workOrder.id"
                  :label="workOrder.id"
                  :value="workOrder.id"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="加工数量">
            <el-input v-model="report.processedQuantity"></el-input>
          </el-form-item>
          <el-form-item label="合格数量">
            <el-input v-model="report.qualifiedQuantity"></el-input>
          </el-form-item>
          <el-form-item label="加工起始时间">
            <el-date-picker v-model="report.startTime" type="datetime" placeholder="选择起始时间"></el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-checkbox v-model="report.completeReport">完成报工</el-checkbox>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submitReport">提交报工</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div class="work-order-list-section">
        <el-table :data="workOrders" style="width: 100%">
          <el-table-column prop="id" label="工单编号"></el-table-column>
          <el-table-column prop="deviceId" label="设备编号"></el-table-column>
          <el-table-column prop="status" label="工单状态"></el-table-column>
          <el-table-column prop="processedQuantity" label="加工数量"></el-table-column>
          <el-table-column prop="qualifiedQuantity" label="合格数量"></el-table-column>
        </el-table>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';

const report = ref({
  workOrderId: null,
  processedQuantity: null,
  qualifiedQuantity: null,
  startTime: null,
  completeReport: false
});
const workOrders = ref([]);
const startedWorkOrders = ref([]);

const fetchWorkOrders = async () => {
  const workOrderData = await fetch('/api/work-orders').then(res => res.json());
  workOrders.value = workOrderData;
  startedWorkOrders.value = workOrderData.filter(wo => wo.status === '已启动');
};

const submitReport = async () => {
  if (!report.value.workOrderId || !report.value.processedQuantity || !report.value.qualifiedQuantity || !report.value.startTime) {
    ElMessage.error('请填写所有必填项');
    return;
  }

  await fetch(`/api/work-orders/${report.value.workOrderId}/report`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ ...report.value })
  });

  if (report.value.completeReport) {
    await fetch(`/api/work-orders/${report.value.workOrderId}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ status: '已完成' })
    });
  }

  report.value.workOrderId = null;
  report.value.processedQuantity = null;
  report.value.qualifiedQuantity = null;
  report.value.startTime = null;
  report.value.completeReport = false;

  fetchWorkOrders();
};

onMounted(() => {
  fetchWorkOrders();
});
</script>

<style scoped lang="scss">
.track-management {
  padding: 20px;
  background: #f5f5f5;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.el-card {
  width: 100%;
  height: 100%;
}

.report-section {
  margin-bottom: 20px;
}

.work-order-list-section {
  width: 100%;
}

.el-input,
.el-select,
.el-date-picker {
  width: 300px;
}

.el-table {
  width: 100%;
}
</style>
