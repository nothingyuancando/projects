<template>
  <div class="schedum-management">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <h2>生产调度管理</h2>
      </div>
      <div class="order-list-section">
        <el-table :data="orders" style="width: 100%">
          <el-table-column prop="id" label="订单编号"></el-table-column>
          <el-table-column prop="productName" label="产品名称"></el-table-column>
          <el-table-column prop="quantity" label="总数量"></el-table-column>
          <el-table-column label="操作">
            <template #default="scope">
              <el-button @click="splitOrder(scope.row)" size="mini">拆分订单</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div class="work-order-list-section" style="margin-top: 20px;">
        <el-table :data="paginatedWorkOrders" style="width: 100%">
          <el-table-column prop="orderId" label="工单编号"></el-table-column>
          <el-table-column prop="productName" label="产品名称"></el-table-column>
          <el-table-column prop="quantity" label="数量"></el-table-column>
          <el-table-column prop="factoryId" label="工厂编号"></el-table-column>
          <el-table-column prop="deviceId" label="设备编号"></el-table-column>
          <el-table-column prop="status" label="工单状态"></el-table-column>
          <el-table-column label="操作">
            <template #default="scope">
              <el-button v-if="scope.row.status !== '已取消'" @click="cancelWorkOrder(scope.row)" size="mini">取消</el-button>
              <el-button @click="modifyWorkOrder(scope.row)" size="mini">修改</el-button>
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
    <el-dialog :visible.sync="isConfirmingReject" title="确认拒绝">
      <span>确定要拒绝该工单吗？</span>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelReject">取消</el-button>
        <el-button type="danger" @click="rejectWorkOrder">拒绝</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { ElMessageBox, ElMessage } from 'element-plus';

const orders = ref([
  { id: 1, productName: '产品A', quantity: 500 },
  { id: 2, productName: '产品B', quantity: 300 }
]);

const workOrders = ref([]);
const devices = ref([]);
const isConfirmingReject = ref(false);
const workOrderToReject = ref(null);

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

const fetchDevices = async () => {
  // 示例数据，实际应从API获取
  const deviceData = [
    { id: 1, name: '设备A' },
    { id: 2, name: '设备B' }
  ];
  devices.value = deviceData;
};

const splitOrder = (order) => {
  const orderQuantity = order.quantity;
  const devicesCount = devices.value.length;
  const quantityPerDevice = Math.ceil(orderQuantity / devicesCount);

  for (let i = 0; i < devicesCount; i++) {
    const quantity = (i === devicesCount - 1) ? (orderQuantity - quantityPerDevice * i) : quantityPerDevice;
    workOrders.value.push({
      id: workOrders.value.length + 1,
      orderId: order.id,
      productName: order.productName,
      quantity,
      deviceId: devices.value[i].id,
      status: '待确认'
    });
  }
};

const acceptWorkOrder = async (workOrder) => {
  workOrder.status = '已接受';
  ElMessage.success('工单已接受');
};

const confirmRejectWorkOrder = (workOrder) => {
  workOrderToReject.value = workOrder;
  isConfirmingReject.value = true;
};

const rejectWorkOrder = async () => {
  if (workOrderToReject.value) {
    workOrderToReject.value.status = '已拒绝';
    ElMessage.success('工单已拒绝');
  }
  isConfirmingReject.value = false;
  workOrderToReject.value = null;
};

const cancelReject = () => {
  isConfirmingReject.value = false;
  workOrderToReject.value = null;
};

const cancelWorkOrder = (workOrder) => {
  workOrder.status = '已取消';
  ElMessage.success('工单已取消');
};

const modifyWorkOrder = (workOrder) => {
  ElMessageBox.prompt('请输入新的数量', '修改工单', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    inputPattern: /^[1-9]\d*$/,
    inputErrorMessage: '请输入正整数'
  }).then(({ value }) => {
    workOrder.quantity = parseInt(value, 10);
    ElMessage.success('工单已修改');
  }).catch(() => {
    ElMessage.info('已取消修改');
  });
};

onMounted(() => {
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

.order-list-section {
  margin-bottom: 20px;
  width: 100%;
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
