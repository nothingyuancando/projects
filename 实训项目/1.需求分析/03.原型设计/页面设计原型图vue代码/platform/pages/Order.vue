<template>
  <div class="order-management">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <h2>订单管理</h2>
      </div>
      <div class="order-list-section">
        <el-table :data="orders" style="width: 100%">
          <el-table-column prop="orderId" label="订单编号"></el-table-column>
          <el-table-column prop="customerName" label="客户姓名"></el-table-column>
          <el-table-column prop="orderStatus" label="订单状态"></el-table-column>
          <el-table-column prop="orderDate" label="订单日期"></el-table-column>
          <el-table-column label="操作">
            <template #default="scope">
              <el-button size="mini" @click="handleView(scope.row)">查看</el-button>
              <el-button size="mini" type="primary" @click="handleAccept(scope.row)">接单</el-button>
              <el-button size="mini" type="danger" @click="handleReject(scope.row)">拒单</el-button>
              <el-button size="mini" @click="handleSendInfo(scope.row)">发送信息</el-button>
              <el-button size="mini" type="success" @click="handleComplete(scope.row)" :disabled="scope.row.orderStatus !== '已接单'">完成订单</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>

    <!-- 查看订单弹窗 -->
    <el-dialog :visible.sync="viewDialogVisible" title="订单详情">
      <el-descriptions :border="true" :column="1">
        <el-descriptions-item label="订单ID">{{ currentOrder?.orderId }}</el-descriptions-item>
        <el-descriptions-item label="客户姓名">{{ currentOrder?.customerName }}</el-descriptions-item>
        <el-descriptions-item label="订单状态">{{ currentOrder?.orderStatus }}</el-descriptions-item>
        <el-descriptions-item label="订单日期">{{ currentOrder?.orderDate }}</el-descriptions-item>
        <el-descriptions-item label="订单详情">{{ currentOrder?.orderDetails }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="viewDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <!-- 接单弹窗 -->
    <el-dialog :visible.sync="acceptDialogVisible" title="接单确认">
      <span>确定要接单吗？</span>
      <template #footer>
        <el-button @click="acceptDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="acceptOrder">确定</el-button>
      </template>
    </el-dialog>

    <!-- 拒单弹窗 -->
    <el-dialog :visible.sync="rejectDialogVisible" title="拒单确认">
      <span>确定要拒单吗？</span>
      <template #footer>
        <el-button @click="rejectDialogVisible = false">取消</el-button>
        <el-button type="danger" @click="rejectOrder">确定</el-button>
      </template>
    </el-dialog>

    <!-- 发送信息弹窗 -->
    <el-dialog :visible.sync="sendInfoDialogVisible" title="发送订单信息">
      <span>确定要发送订单信息吗？</span>
      <template #footer>
        <el-button @click="sendInfoDialogVisible = false">取消</el-button>
        <el-button @click="sendOrderInfo">确定</el-button>
      </template>
    </el-dialog>

    <!-- 完成订单弹窗 -->
    <el-dialog :visible.sync="completeDialogVisible" title="完成订单">
      <span>确定要完成订单吗？</span>
      <template #footer>
        <el-button @click="completeDialogVisible = false">取消</el-button>
        <el-button type="success" @click="completeOrder">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { getOrderList, acceptOrderRequest, rejectOrderRequest, sendOrderInfoRequest, completeOrderRequest } from '@/api/order.js';

// 存储订单数据
const orders = ref([]);

// 弹窗可见性
const viewDialogVisible = ref(false);
const acceptDialogVisible = ref(false);
const rejectDialogVisible = ref(false);
const sendInfoDialogVisible = ref(false);
const completeDialogVisible = ref(false);

// 当前处理的订单
const currentOrder = ref(null);

const fetchOrders = async () => {
  try {
    const response = await getOrderList();
    orders.value = response.data;
  } catch (error) {
    ElMessage.error('获取订单列表失败');
  }
};

// 处理查看订单详情弹窗显示
const handleView = (order) => {
  currentOrder.value = order;
  viewDialogVisible.value = true;
};

// 处理接单弹窗显示
const handleAccept = (order) => {
  currentOrder.value = order;
  acceptDialogVisible.value = true;
};

// 处理拒单弹窗显示
const handleReject = (order) => {
  currentOrder.value = order;
  rejectDialogVisible.value = true;
};

// 处理发送信息弹窗显示
const handleSendInfo = (order) => {
  currentOrder.value = order;
  sendInfoDialogVisible.value = true;
};

// 处理完成订单弹窗显示
const handleComplete = (order) => {
  currentOrder.value = order;
  if (order.orderStatus === '已接单') {
    completeDialogVisible.value = true;
  } else {
    ElMessage.error('只有接单后才能完成订单');
  }
};

// 接单
const acceptOrder = async () => {
  try {
    await acceptOrderRequest(currentOrder.value.orderId);
    ElMessage.success('接单成功');
    acceptDialogVisible.value = false;
    fetchOrders(); // 刷新订单列表
  } catch (error) {
    ElMessage.error('接单失败');
  }
};

// 拒单
const rejectOrder = async () => {
  try {
    await rejectOrderRequest(currentOrder.value.orderId);
    ElMessage.success('拒单成功');
    rejectDialogVisible.value = false;
    fetchOrders(); // 刷新订单列表
  } catch (error) {
    ElMessage.error('拒单失败');
  }
};

// 发送订单信息
const sendOrderInfo = async () => {
  try {
    await sendOrderInfoRequest(currentOrder.value.orderId);
    ElMessage.success('订单信息已发送');
    sendInfoDialogVisible.value = false;
  } catch (error) {
    ElMessage.error('发送订单信息失败');
  }
};

// 完成订单
const completeOrder = async () => {
  try {
    await completeOrderRequest(currentOrder.value.orderId);
    ElMessage.success('订单已完成');
    completeDialogVisible.value = false;
    fetchOrders(); // 刷新订单列表
  } catch (error) {
    ElMessage.error('完成订单失败');
  }
};

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

.order-list-section {
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
