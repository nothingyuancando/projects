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
                  :label="`计划编号: ${plan.id}, 开始日期: ${plan.startDate}`"
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
        <el-table :data="paginatedWorkOrders" style="width: 100%">
          <el-table-column prop="planId" label="生产计划编号"></el-table-column>
          <el-table-column prop="deviceId" label="设备编号"></el-table-column>
          <el-table-column prop="status" label="工单状态"></el-table-column>
          <el-table-column label="操作">
            <template #default="scope">
              <el-button v-if="scope.row.status === '未启动'" @click="startWorkOrder(scope.row)" size="mini">启动</el-button>
              <el-button v-if="scope.row.status === '未启动'" @click="confirmDeleteWorkOrder(scope.row)" size="mini" type="danger">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
            layout="prev, pager, next"
            :total="totalWorkOrders"
            :page-size="pageSize"
            @current-change="handlePageChange"
        ></el-pagination>
      </div>
    </el-card>
    <el-dialog :visible.sync="isConfirmingDelete" title="确认删除">
      <span>确定要删除该工单吗？</span>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelDelete">取消</el-button>
        <el-button type="danger" @click="deleteWorkOrder">删除</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { ElMessageBox, ElMessage } from 'element-plus';

const newWorkOrder = ref({ planId: null, deviceId: null });
const workOrders = ref([]);
const startedPlans = ref([]);
const devices = ref([]);
const isConfirmingDelete = ref(false);
const workOrderToDelete = ref(null);

const pageSize = ref(5);
const currentPage = ref(1);

const paginatedWorkOrders = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return workOrders.value.slice(start, end);
});

const totalWorkOrders = computed(() => workOrders.value.length);

const handlePageChange = (page) => {
  currentPage.value = page;
};

const fetchWorkOrders = async () => {
  // 示例数据，实际应从API获取
  const workOrderData = [
    { id: 1, planId: 1, deviceId: 1, status: '未启动' },
    { id: 2, planId: 2, deviceId: 2, status: '未启动' }
  ];
  workOrders.value = workOrderData;
};

const fetchStartedPlans = async () => {
  // 示例数据，实际应从API获取
  const planData = [
    { id: 1, startDate: '2024-06-01', status: '已启动' },
    { id: 2, startDate: '2024-06-02', status: '已启动' }
  ];
  startedPlans.value = planData;
};

const fetchDevices = async () => {
  // 示例数据，实际应从API获取
  const deviceData = [
    { id: 1, name: '设备A' },
    { id: 2, name: '设备B' }
  ];
  devices.value = deviceData;
};

const createWorkOrder = async () => {
  if (!newWorkOrder.value.planId || !newWorkOrder.value.deviceId) {
    ElMessage.error('请选择生产计划和设备');
    return;
  }
  workOrders.value.push({
    id: workOrders.value.length + 1,
    planId: newWorkOrder.value.planId,
    deviceId: newWorkOrder.value.deviceId,
    status: '未启动'
  });
  newWorkOrder.value.planId = null;
  newWorkOrder.value.deviceId = null;
  fetchWorkOrders();
};

const startWorkOrder = async (workOrder) => {
  workOrder.status = '已启动';
  ElMessage.success('工单已启动');
};

const confirmDeleteWorkOrder = (workOrder) => {
  workOrderToDelete.value = workOrder;
  isConfirmingDelete.value = true;
};

const deleteWorkOrder = async () => {
  if (workOrderToDelete.value) {
    const index = workOrders.value.findIndex(wo => wo.id === workOrderToDelete.value.id);
    if (index !== -1) {
      workOrders.value.splice(index, 1);
      ElMessage.success('工单已删除');
    }
  }
  isConfirmingDelete.value = false;
  workOrderToDelete.value = null;
};

const cancelDelete = () => {
  isConfirmingDelete.value = false;
  workOrderToDelete.value = null;
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

.dialog-footer {
  text-align: right;
}
</style>
