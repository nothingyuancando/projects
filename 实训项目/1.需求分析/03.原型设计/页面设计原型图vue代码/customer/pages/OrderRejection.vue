<template>
  <div class="order-rejection">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <h2>处理平台拒单</h2>
      </div>
      <div class="search-section">
        <el-input v-model="searchQuery" placeholder="输入订单编号或产品名称查询" class="input-with-select">
          <el-button slot="append" @click="searchRejectedOrders">查询</el-button>
        </el-input>
      </div>
      <el-table :data="paginatedRejectedOrders" style="width: 100%">
        <el-table-column prop="orderId" label="订单编号"></el-table-column>
        <el-table-column prop="productName" label="产品名称"></el-table-column>
        <el-table-column prop="reason" label="拒单原因"></el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button @click="openEditOrderDialog(scope.row)" size="mini">修改订单</el-button>
            <el-button @click="confirmCancelOrder(scope.row)" size="mini" type="danger">取消订单</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination-container">
        <el-pagination
            background
            layout="prev, pager, next"
            :total="filteredRejectedOrders.length"
            :page-size="pageSize"
            :current-page.sync="currentPage"
            @current-change="handlePageChange">
        </el-pagination>
      </div>
    </el-card>

    <!-- 编辑订单弹窗 -->
    <el-dialog v-model="editOrderDialogVisible" title="修改订单">
      <el-form :model="editOrderForm" ref="editOrderFormRef" label-width="120px">
        <el-form-item label="产品名称" required>
          <el-input v-model="editOrderForm.productName"></el-input>
        </el-form-item>
        <el-form-item label="数量" required>
          <el-input-number v-model="editOrderForm.quantity" :min="1"></el-input-number>
        </el-form-item>
        <el-form-item label="交货日期" required>
          <el-date-picker v-model="editOrderForm.deliveryDate" type="date" placeholder="选择日期"></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitEditOrder">提交修改</el-button>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="editOrderDialogVisible = false">取消</el-button>
      </div>
    </el-dialog>

    <!-- 取消订单确认弹窗 -->
    <el-dialog v-model="cancelOrderDialogVisible" title="确认取消订单">
      <p>确定要取消订单 "{{ cancelOrderForm.orderId }}" 吗？</p>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelOrder" type="danger">确认取消</el-button>
        <el-button @click="cancelOrderDialogVisible = false">取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import {
  fetchRejectedOrdersFromServer,
  updateOrderOnServer,
  cancelOrderFromServer,
} from '@/api/customer';

// 示例数据
const rejectedOrders = ref([
  {
    orderId: '12345',
    productName: '产品A',
    reason: '库存不足',
  },
  {
    orderId: '12346',
    productName: '产品B',
    reason: '生产线故障',
  },
  {
    orderId: '12347',
    productName: '产品C',
    reason: '原材料缺货',
  },
  {
    orderId: '12348',
    productName: '产品D',
    reason: '质量问题',
  },
  {
    orderId: '12349',
    productName: '产品E',
    reason: '生产计划变更',
  },
]);

const editOrderDialogVisible = ref(false);
const cancelOrderDialogVisible = ref(false);

const editOrderForm = reactive({
  orderId: '',
  productName: '',
  quantity: 1,
  deliveryDate: '',
});

const cancelOrderForm = reactive({
  orderId: '',
});

const searchQuery = ref('');
const pageSize = ref(5);
const currentPage = ref(1);

const fetchRejectedOrders = async () => {
  try {
    const response = await fetchRejectedOrdersFromServer();
    rejectedOrders.value = response.data;
  } catch (error) {
    ElMessage.error('获取拒单列表失败');
  }
};

const searchRejectedOrders = () => {
  fetchRejectedOrders();
};

const openEditOrderDialog = (order) => {
  Object.assign(editOrderForm, order);
  editOrderDialogVisible.value = true;
};

const submitEditOrder = async () => {
  try {
    await updateOrderOnServer(editOrderForm);
    ElMessage.success('订单修改成功');
    editOrderDialogVisible.value = false;
    await fetchRejectedOrders();
  } catch (error) {
    ElMessage.error('订单修改失败');
  }
};

const confirmCancelOrder = (order) => {
  cancelOrderForm.orderId = order.orderId;
  cancelOrderDialogVisible.value = true;
};

const cancelOrder = async () => {
  try {
    await cancelOrderFromServer(cancelOrderForm.orderId);
    ElMessage.success('订单取消成功');
    cancelOrderDialogVisible.value = false;
    await fetchRejectedOrders();
  } catch (error) {
    ElMessage.error('订单取消失败');
  }
};

const handlePageChange = (page) => {
  currentPage.value = page;
};

const filteredRejectedOrders = computed(() => {
  return rejectedOrders.value.filter(order =>
      order.orderId.includes(searchQuery.value) || order.productName.includes(searchQuery.value));
});

const paginatedRejectedOrders = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return filteredRejectedOrders.value.slice(start, end);
});

onMounted(fetchRejectedOrders);
</script>

<style scoped>
.order-rejection {
  padding: 20px;
}

.el-table .el-button {
  margin-right: 5px;
}

.dialog-footer {
  text-align: right;
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
