<template>
  <div class="track-management">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <h2>生产跟踪</h2>
      </div>
      <el-row :gutter="20" class="main-row">
        <el-col :span="8">
          <el-form :model="report" ref="reportForm" label-width="120px" class="report-form">
            <el-form-item label="工单" :required="true">
              <el-select v-model="report.workOrderId" placeholder="选择工单">
                <el-option
                    v-for="workOrder in startedWorkOrders"
                    :key="workOrder.id"
                    :label="`工单编号: ${workOrder.id}, 设备编号: ${workOrder.deviceId}`"
                    :value="workOrder.id"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="加工数量" :required="true">
              <el-input v-model="report.processedQuantity" type="number"></el-input>
            </el-form-item>
            <el-form-item label="合格数量" :required="true">
              <el-input v-model="report.qualifiedQuantity" type="number"></el-input>
            </el-form-item>
            <el-form-item label="加工起始时间" :required="true">
              <el-date-picker v-model="report.startTime" type="datetime" placeholder="选择起始时间"></el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-checkbox v-model="report.completeReport">完成报工</el-checkbox>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="confirmSubmitReport">提交报工</el-button>
            </el-form-item>
          </el-form>
        </el-col>
        <el-col :span="16">
          <el-table :data="paginatedWorkOrders" style="width: 100%; height: 500px; overflow: auto;">
            <el-table-column prop="id" label="工单编号"></el-table-column>
            <el-table-column prop="deviceId" label="设备编号"></el-table-column>
            <el-table-column prop="status" label="工单状态"></el-table-column>
            <el-table-column prop="processedQuantity" label="加工数量"></el-table-column>
            <el-table-column prop="qualifiedQuantity" label="合格数量"></el-table-column>
          </el-table>
          <div class="pagination">
            <el-pagination
                layout="prev, pager, next"
                :total="totalWorkOrders"
                :page-size="pageSize"
                @current-change="handlePageChange"
            ></el-pagination>
          </div>
        </el-col>
      </el-row>
    </el-card>
    <el-dialog :visible.sync="isConfirmingSubmit" title="确认提交">
      <span>确定要提交报工吗？</span>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelSubmit">取消</el-button>
        <el-button type="primary" @click="submitReport">提交</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script setup>
import { ref, computed, onMounted } from 'vue';
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
const isConfirmingSubmit = ref(false);

const pageSize = ref(5);
const currentPage = ref(1);

const paginatedWorkOrders = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  return workOrders.value.slice(start, start + pageSize.value);
});

const totalWorkOrders = computed(() => workOrders.value.length);

const handlePageChange = (page) => {
  currentPage.value = page;
};

const fetchWorkOrders = async () => {
  const workOrderData = [
    { id: 1, deviceId: 1, status: '已启动', processedQuantity: 10, qualifiedQuantity: 9 },
    { id: 2, deviceId: 2, status: '已启动', processedQuantity: 20, qualifiedQuantity: 18 }
  ];
  workOrders.value = workOrderData;
  startedWorkOrders.value = workOrderData.filter(wo => wo.status === '已启动');
};

const confirmSubmitReport = () => {
  if (!report.value.workOrderId || !report.value.processedQuantity || !report.value.qualifiedQuantity || !report.value.startTime) {
    ElMessage.error('请填写所有必填项');
    return;
  }
  isConfirmingSubmit.value = true;
};

const submitReport = async () => {
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
  isConfirmingSubmit.value = false;
};

const cancelSubmit = () => {
  isConfirmingSubmit.value = false;
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
  align-items: center;
  justify-content: center;
}

.el-card {
  width: 100%;
  height: 100%;
}

.main-row {
  height: calc(100% - 50px); /* Adjust the height to fill the container */
}

.report-form {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.el-table {
  margin-top: 20px;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.dialog-footer {
  text-align: right;
}
</style>
