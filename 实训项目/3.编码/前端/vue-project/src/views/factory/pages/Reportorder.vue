<template>
  <div class="track-management">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <h2>生产跟踪</h2>
      </div>
      <el-row :gutter="20" class="main-row">
        <el-col :span="24">
          <el-form :model="report" ref="reportForm" label-width="120px" class="report-form">
            <el-form-item label="工单" :required="true">
              <el-select v-model="report.workorderId" placeholder="选择工单">
                <el-option
                    v-for="workOrder in startedWorkOrders"
                    :key="workOrder.workorder_id"
                    :label="`工单编号: ${workOrder.workorder_id}`"
                    :value="workOrder.workorder_id"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="加工数量" :required="true">
              <el-input v-model="report.processingNumber" type="number"></el-input>
            </el-form-item>
            <el-form-item label="合格数量" :required="true">
              <el-input v-model="report.qualifiyNumber" type="number"></el-input>
            </el-form-item>
            <el-form-item label="加工起始时间" :required="true">
              <el-date-picker v-model="report.workreportTime" type="datetime" placeholder="选择起始时间"></el-date-picker>
            </el-form-item>
            <el-form-item label="工时">
              <el-input v-model="report.workHour" type="number"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="confirmSubmitReport">提交报工</el-button>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
    </el-card>
    <el-dialog v-model="isConfirmingSubmit" title="确认提交">
      <span>确定要提交报工吗？</span>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelSubmit">取消</el-button>
        <el-button type="primary" @click="submitReport">提交</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { getWorkOrders, submitReportData } from '@/api/factory/workReport.js';

const report = ref({
  workorderId: null,
  producingStates: '1', // 默认生产状态为未生产
  workreportTime: null,
  workHour: null,
  processingNumber: null,
  qualifiyNumber: null,
});

const workOrders = ref([]);
const startedWorkOrders = ref([]);
const isConfirmingSubmit = ref(false);

const fetchWorkOrders = async () => {
  try {
    const response = await getWorkOrders();
    if (response.code === 200) {
      workOrders.value = response.data;
      startedWorkOrders.value = response.data.filter((wo) => wo.workorder_states === 3);
    } else {
      ElMessage.error(response.message || '获取工单列表失败');
    }
  } catch (error) {
    console.error('获取工单列表失败', error);
    ElMessage.error('获取工单列表失败');
  }
};

const confirmSubmitReport = () => {
  if (!report.value.workorderId || !report.value.processingNumber || !report.value.qualifiyNumber || !report.value.workreportTime) {
    ElMessage.error('请填写所有必填项');
    return;
  }
  isConfirmingSubmit.value = true;
};

const submitReport = async () => {
  try {
    const payload = {
      workorderId: report.value.workorderId,
      producingStates: report.value.producingStates,
      workreportTime: report.value.workreportTime,
      workHour: report.value.workHour,
      processingNumber: report.value.processingNumber,
      qualifiyNumber: report.value.qualifiyNumber,
    };
    await submitReportData(payload);
    ElMessage.success('报工提交成功');
    resetReportForm();
    fetchWorkOrders();
  } catch (error) {
    console.error('报工提交失败', error);
    ElMessage.error('报工提交失败');
  } finally {
    isConfirmingSubmit.value = false;
  }
};

const resetReportForm = () => {
  report.value = {
    workorderId: null,
    producingStates: '1',
    workreportTime: null,
    workHour: null,
    processingNumber: null,
    qualifiyNumber: null,
  };
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
  height: calc(100% - 50px);
}

.report-form {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.dialog-footer {
  text-align: right;
}
</style>
