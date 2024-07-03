<template>
  <div class="order-progress">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <h2>订单进度</h2>
      </div>
      <div class="search-section">
        <el-input v-model="searchQuery" placeholder="输入订单编号或客户ID查询" class="input-with-select">
          <el-button slot="append" @click="searchOrders">查询</el-button>
        </el-input>
      </div>
      <el-table :data="paginatedOrders" style="width: 100%">
        <el-table-column prop="order_id" label="订单编号"></el-table-column>
        <el-table-column prop="order_time" label="订单时间"></el-table-column>
        <el-table-column prop="customer_id" label="客户ID"></el-table-column>
        <el-table-column prop="order_states" label="订单状态">
          <template #default="scope">
            <el-tag :type="getTagType(scope.row.order_states)">
              {{ getOrderStateText(scope.row.order_states) }}
            </el-tag>
          </template>
        </el-table-column>
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
      <el-table :data="[orderDetails]" style="width: 100%">
        <el-table-column prop="order_id" label="订单编号"></el-table-column>
        <el-table-column prop="create_time" label="创建时间"></el-table-column>
        <el-table-column prop="customer_id" label="客户ID"></el-table-column>
        <el-table-column prop="order_states" label="订单状态">
          <template #default="scope">
            <el-tag :type="getTagType(scope.row.order_states)">
              {{ getOrderStateText(scope.row.order_states) }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
      <el-table :data="orderDetails.order_detail" style="width: 100%" v-if="orderDetails.order_detail.length">
        <el-table-column prop="product_id" label="产品名称"></el-table-column>
        <el-table-column prop="amount" label="数量"></el-table-column>
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
import { fetchOrdersFromServer, fetchOrderDetailsFromServer } from '@/api/customer/customer.js';

const orders = ref([]);
const orderDetails = ref({});
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
    const response = await fetchOrderDetailsFromServer(order.order_id);
    orderDetails.value = response.data[0];
    orderDetailsVisible.value = true;
  } catch (error) {
    ElMessage.error('获取订单详情失败');
  }
};

const getOrderStateText = (state) => {
  switch (state) {
    case 0:
      return '未接单';
    case 1:
      return '已接单';
    case 2:
      return '已完成';
    default:
      return '';
  }
};

const getTagType = (state) => {
  switch (state) {
    case 0:
      return 'warning';
    case 1:
      return 'success';
    case 2:
      return 'info';
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
      order.order_id.toString().includes(searchQuery.value) ||
      order.customer_id.toString().includes(searchQuery.value)
  );
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
