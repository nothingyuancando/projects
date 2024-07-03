<template>
  <div class="order-management">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <h2>工单管理</h2>
      </div>
      <div class="search-section">
        <el-input v-model="searchQuery" placeholder="输入工单名称或编号查询" class="input-with-select">
          <el-button slot="append" @click="searchOrders">查询</el-button>
        </el-input>
      </div>
      <div class="list-section">
        <el-table :data="paginatedOrders" style="width: 100%">
          <el-table-column prop="name" label="工单名称"></el-table-column>
          <el-table-column prop="orderNumber" label="工单编号"></el-table-column>
          <el-table-column prop="status" label="状态"></el-table-column>
          <el-table-column label="操作">
            <template #default="scope">
              <el-button @click="acceptOrder(scope.row)" size="mini" :disabled="scope.row.status !== '待接受'">接受工单</el-button>
              <el-button @click="rejectOrder(scope.row)" size="mini" type="danger" :disabled="scope.row.status !== '待接受'">拒绝工单</el-button>
              <el-button @click="produceOrder(scope.row)" size="mini" type="primary" :disabled="scope.row.status !== '已接受'">生产工单</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="pagination-container">
          <el-pagination
              background
              layout="prev, pager, next"
              :total="filteredOrders.length"
              :page-size="pageSize"
              :current-page.sync="currentPage"
              @current-change="handlePageChange">
          </el-pagination>
        </div>
      </div>
    </el-card>

    <!-- 接受工单确认弹窗 -->
    <el-dialog v-model="acceptOrderDialogVisible" title="确认接受工单">
      <p>确定要接受工单 "{{ acceptingOrderName }}" 吗？</p>
      <div slot="footer" class="dialog-footer">
        <el-button @click="confirmAcceptOrder">确认接受</el-button>
        <el-button @click="acceptOrderDialogVisible = false">取消</el-button>
      </div>
    </el-dialog>

    <!-- 拒绝工单确认弹窗 -->
    <el-dialog v-model="rejectOrderDialogVisible" title="确认拒绝工单">
      <p>确定要拒绝工单 "{{ rejectingOrderName }}" 吗？</p>
      <div slot="footer" class="dialog-footer">
        <el-button @click="confirmRejectOrder" type="danger">确认拒绝</el-button>
        <el-button @click="rejectOrderDialogVisible = false">取消</el-button>
      </div>
    </el-dialog>

    <!-- 生产工单确认弹窗 -->
    <el-dialog v-model="produceOrderDialogVisible" title="确认生产工单">
      <p>确定要生产工单 "{{ producingOrderName }}" 吗？</p>
      <div slot="footer" class="dialog-footer">
        <el-button @click="confirmProduceOrder" type="primary">确认生产</el-button>
        <el-button @click="produceOrderDialogVisible = false">取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue';
import { ElMessage } from 'element-plus';

const searchQuery = ref('');
const acceptOrderDialogVisible = ref(false);
const rejectOrderDialogVisible = ref(false);
const produceOrderDialogVisible = ref(false);

const acceptingOrderId = ref(null);
const acceptingOrderName = ref('');
const rejectingOrderId = ref(null);
const rejectingOrderName = ref('');
const producingOrderId = ref(null);
const producingOrderName = ref('');

const orders = ref([]);
const pageSize = ref(10);
const currentPage = ref(1);

// 示例数据
const fetchOrders = async () => {
  orders.value = [
    { id: 1, name: '工单A', orderNumber: '0001', status: '待接受' },
    { id: 2, name: '工单B', orderNumber: '0002', status: '待接受' },
    { id: 3, name: '工单C', orderNumber: '0003', status: '已接受' },
    { id: 4, name: '工单D', orderNumber: '0004', status: '生产中' }
  ];
};

const searchOrders = async () => {
  await fetchOrders();
};

const acceptOrder = (order) => {
  acceptingOrderId.value = order.id;
  acceptingOrderName.value = order.name;
  acceptOrderDialogVisible.value = true;
};

const rejectOrder = (order) => {
  rejectingOrderId.value = order.id;
  rejectingOrderName.value = order.name;
  rejectOrderDialogVisible.value = true;
};

const produceOrder = (order) => {
  producingOrderId.value = order.id;
  producingOrderName.value = order.name;
  produceOrderDialogVisible.value = true;
};

const confirmAcceptOrder = async () => {
  try {
    const order = orders.value.find(o => o.id === acceptingOrderId.value);
    if (order) {
      order.status = '已接受';
    }
    ElMessage.success('工单接受成功');
    acceptOrderDialogVisible.value = false;
  } catch (error) {
    ElMessage.error('工单接受失败');
  }
};

const confirmRejectOrder = async () => {
  try {
    const index = orders.value.findIndex(o => o.id === rejectingOrderId.value);
    if (index !== -1) {
      orders.value.splice(index, 1);
    }
    ElMessage.success('工单拒绝成功');
    rejectOrderDialogVisible.value = false;
  } catch (error) {
    ElMessage.error('工单拒绝失败');
  }
};

const confirmProduceOrder = async () => {
  try {
    const order = orders.value.find(o => o.id === producingOrderId.value);
    if (order) {
      order.status = '生产中';
    }
    ElMessage.success('工单生产中');
    produceOrderDialogVisible.value = false;
  } catch (error) {
    ElMessage.error('工单生产失败');
  }
};

const handlePageChange = (page) => {
  currentPage.value = page;
};

const filteredOrders = computed(() => {
  return orders.value.filter(order => order.name.includes(searchQuery.value) || order.orderNumber.includes(searchQuery.value));
});

const paginatedOrders = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return filteredOrders.value.slice(start, end);
});

onMounted(fetchOrders);
</script>

<style scoped lang="scss">
.order-management {
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

.search-section {
  margin-bottom: 20px;
}

.actions-section {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 20px;
}

.el-input {
  width: 300px;
}

.el-table {
  width: 100%;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>
