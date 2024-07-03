<template>
  <div class="order-progress">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <h2>订单进度</h2>
      </div>
      <div class="search-section">
        <el-input v-model="searchQuery" placeholder="输入订单编号或产品名称查询" class="input-with-select">
          <el-button slot="append" @click="searchOrders">查询</el-button>
        </el-input>
      </div>
      <el-table :data="paginatedOrders" style="width: 100%">
        <el-table-column prop="orderId" label="订单编号"></el-table-column>
        <el-table-column prop="productName" label="产品名称"></el-table-column>
        <el-table-column prop="quantity" label="数量"></el-table-column>
        <el-table-column prop="platformStatus" label="平台接单状态">
          <template #default="scope">
            <el-tag :type="getTagType(scope.row.platformStatus)">
              {{ scope.row.platformStatus }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="订单状态"></el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button @click="viewOrderDetails(scope.row)" size="mini">查看详情</el-button>
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
    </el-card>

    <el-dialog v-model="orderDetailsVisible" title="订单详情">
      <el-table :data="orderDetails" style="width: 100%">
        <el-table-column prop="stage" label="生产阶段"></el-table-column>
        <el-table-column prop="progress" label="进度">
          <template #default="scope">
            <el-progress :percentage="scope.row.progress" :status="getProgressStatus(scope.row.progress)"></el-progress>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="orderDetailsVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { fetchOrdersFromServer, fetchOrderDetailsFromServer } from '@/api/customer';

// 示例数据
const orders = ref([
  {
    orderId: '12345',
    productName: '产品A',
    quantity: 10,
    platformStatus: '已接单',
    status: '生产中',
  },
  {
    orderId: '12346',
    productName: '产品B',
    quantity: 20,
    platformStatus: '未接单',
    status: '待生产',
  },
  {
    orderId: '12347',
    productName: '产品C',
    quantity: 15,
    platformStatus: '已接单',
    status: '已完成',
  },
  {
    orderId: '12348',
    productName: '产品D',
    quantity: 25,
    platformStatus: '已接单',
    status: '待生产',
  },
  {
    orderId: '12349',
    productName: '产品E',
    quantity: 30,
    platformStatus: '未接单',
    status: '待生产',
  }
]);

const orderDetails = ref([]);
const orderDetailsVisible = ref(false);
const searchQuery = ref('');
const pageSize = ref(5);
const currentPage = ref(1);

const fetchOrders = async () => {
  try {
    const response = await fetchOrdersFromServer();
    orders.value = response.data;
  } catch (error) {
    ElMessage.error('获取订单列表失败');
  }
};

const searchOrders = () => {
  fetchOrders();
};

const viewOrderDetails = async (order) => {
  try {
    const response = await fetchOrderDetailsFromServer(order.orderId);
    orderDetails.value = response.data;
    orderDetailsVisible.value = true;
  } catch (error) {
    ElMessage.error('获取订单详情失败');
  }
};

const getTagType = (status) => {
  switch (status) {
    case '已接单':
      return 'success';
    case '未接单':
      return 'warning';
    default:
      return '';
  }
};

const getProgressStatus = (progress) => {
  if (progress < 50) return 'exception';
  if (progress < 100) return 'active';
  return 'success';
};

const handlePageChange = (page) => {
  currentPage.value = page;
};

const filteredOrders = computed(() => {
  return orders.value.filter(order =>
      order.orderId.includes(searchQuery.value) || order.productName.includes(searchQuery.value));
});

const paginatedOrders = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return filteredOrders.value.slice(start, end);
});

onMounted(fetchOrders);
</script>

<style scoped>
.order-progress {
  padding: 20px;
}

.el-table .el-tag {
  margin-right: 5px;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.search-section {
  margin-bottom: 20px;
}

.el-input {
  width: 300px;
}
</style>
