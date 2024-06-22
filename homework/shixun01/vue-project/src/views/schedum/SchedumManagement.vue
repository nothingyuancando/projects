<template>
  <div class="schedum-management">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <h2>生产调度管理</h2>
      </div>
      <div class="new-work-order-section">
        <el-form :model="newWorkOrder" ref="newWorkOrderForm" label-width="120px">
          <el-form-item label="生产计划">
            <el-select v-model="newWorkOrder.planId" placeholder="选择生产计划">
              <el-option
                  v-for="plan in startedPlans"
                  :key="plan.id"
                  :label="plan.id"
                  :value="plan.id"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="设备">
            <el-select v-model="newWorkOrder.deviceId" placeholder="选择设备">
              <el-option
                  v-for="device in devices"
                  :key="device.id"
                  :label="device.name"
                  :value="device.id"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="createWorkOrder">新建工单</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div class="work-order-list-section">
        <el-table :data="workOrders" style="width: 100%">
          <el-table-column prop="planId" label="生产计划编号"></el-table-column>
          <el-table-column prop="deviceId" label="设备编号"></el-table-column>
          <el-table-column prop="status" label="工单状态"></el-table-column>
          <el-table-column label="操作">
            <template #default="scope">
              <el-button v-if="scope.row.status === '未启动'" @click="startWorkOrder(scope.row)" size="mini">启动</el-button>
              <el-button v-if="scope.row.status === '未启动'" @click="deleteWorkOrder(scope.row)" size="mini" type="danger">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ElMessageBox, ElMessage } from 'element-plus';

const newWorkOrder = ref({ planId: null, deviceId: null });
const workOrders = ref([]);
const startedPlans = ref([]);
const devices = ref([]);

const fetchWorkOrders = async () => {
  const workOrderData = await fetch('/api/work-orders').then(res => res.json());
  workOrders.value = workOrderData;
};

const fetchStartedPlans = async () => {
  const planData = await fetch('/api/plans?status=已启动').then(res => res.json());
  startedPlans.value = planData;
};

const fetchDevices = async () => {
  const deviceData = await fetch('/api/devices').then(res => res.json());
  devices.value = deviceData;
};

const createWorkOrder = async () => {
  if (!newWorkOrder.value.planId || !newWorkOrder.value.deviceId) {
    ElMessage.error('请选择生产计划和设备');
    return;
  }
  await fetch('/api/work-orders', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ ...newWorkOrder.value, status: '未启动' })
  });
  newWorkOrder.value.planId = null;
  newWorkOrder.value.deviceId = null;
  fetchWorkOrders();
};

const startWorkOrder = async (workOrder) => {
  await fetch(`/api/work-orders/${workOrder.id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ ...workOrder, status: '已启动' })
  });
  fetchWorkOrders();
};

const deleteWorkOrder = async (workOrder) => {
  const planWorkOrders = workOrders.value.filter(wo => wo.planId === workOrder.planId);
  if (planWorkOrders.length <= 1) {
    ElMessage.error('每条已启动计划中必须有一条以上工单记录');
    return;
  }
  await fetch(`/api/work-orders/${workOrder.id}`, {
    method: 'DELETE'
  });
  fetchWorkOrders();
};

onMounted(() => {
  fetchWorkOrders();
  fetchStartedPlans();
  fetchDevices();
});
</script>

<style scoped lang="scss">
.schedum-management {
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

.new-work-order-section {
  margin-bottom: 20px;
}

.work-order-list-section {
  width: 100%;
}

.el-input,
.el-select {
  width: 300px;
}

.el-table {
  width: 100%;
}
</style>
