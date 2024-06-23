<template>
  <div class="order-management">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <h2>订单管理</h2>
      </div>
      <div class="new-order-section">
        <el-form :model="newOrder" ref="newOrderForm" label-width="120px">
          <el-form-item label="产品名称">
            <el-input v-model="newOrder.productName"></el-input>
          </el-form-item>
          <el-form-item label="产品数量">
            <el-input v-model="newOrder.quantity" type="number"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="createOrder">新建订单</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div class="order-list-section">
        <el-table :data="orders" style="width: 100%">
          <el-table-column prop="productName" label="产品名称"></el-table-column>
          <el-table-column prop="quantity" label="产品数量"></el-table-column>
          <el-table-column prop="status" label="订单状态"></el-table-column>
          <el-table-column label="操作">
            <template #default="scope">
              <el-button v-if="scope.row.status === '未接单'" @click="acceptOrder(scope.row)" size="mini">接单</el-button>
              <el-button v-if="scope.row.status === '未接单'" @click="rejectOrder(scope.row)" size="mini" type="danger">拒单</el-button>
              <el-button v-if="scope.row.status === '已接单'" @click="convertToPlan(scope.row)" size="mini">转成计划</el-button>
              <el-button v-if="scope.row.status === '生产中'" @click="completeOrder(scope.row)" size="mini" type="success">完成订单</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>
    <el-dialog :visible.sync="isRejecting" title="拒绝订单">
      <el-form :model="rejectOrderData" ref="rejectOrderForm" label-width="120px">
        <el-form-item label="备注">
          <el-input v-model="rejectOrderData.remark"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelReject">取消</el-button>
        <el-button type="danger" @click="confirmReject">确认拒绝</el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="isCompleting" title="完成订单">
      <el-form :model="completeOrderData" ref="completeOrderForm" label-width="120px">
        <el-form-item label="备注" v-if="completeOrderData.remarkRequired">
          <el-input v-model="completeOrderData.remark"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelComplete">取消</el-button>
        <el-button type="success" @click="confirmComplete">确认完成</el-button>
      </div>
    </el-dialog>
  </div>
</template>


<script setup>
import { ref, onMounted } from 'vue';
import { ElMessageBox, ElMessage } from 'element-plus';

const newOrder = ref({ productName: '', quantity: 0 });
const orders = ref([]);
const isRejecting = ref(false);
const rejectOrderData = ref({ id: null, remark: '' });
const isCompleting = ref(false);
const completeOrderData = ref({ id: null, remark: '', remarkRequired: false });

const fetchOrders = async () => {
  // 示例数据
  const orderData = [
    { id: 1, productName: '产品A', quantity: 10, status: '未接单' },
    { id: 2, productName: '产品B', quantity: 20, status: '已接单' },
    { id: 3, productName: '产品C', quantity: 30, status: '生产中' }
  ];
  orders.value = orderData;
};

const createOrder = async () => {
  orders.value.push({
    id: orders.value.length + 1,
    ...newOrder.value,
    status: '未接单'
  });
  newOrder.value = { productName: '', quantity: 0 };
};

const acceptOrder = async (order) => {
  const availableCapacity = 100; // 示例数据，实际应从API获取
  if (availableCapacity >= order.quantity) {
    order.status = '已接单';
  } else {
    ElMessage.error('可用产能不足，无法接单');
  }
};

const rejectOrder = (order) => {
  isRejecting.value = true;
  rejectOrderData.value.id = order.id;
};

const confirmReject = async () => {
  if (rejectOrderData.value.remark) {
    const order = orders.value.find(o => o.id === rejectOrderData.value.id);
    if (order) {
      order.status = '已拒绝';
      order.remark = rejectOrderData.value.remark;
    }
    isRejecting.value = false;
    rejectOrderData.value = { id: null, remark: '' };
  } else {
    ElMessage.error('拒绝订单需填写备注');
  }
};

const cancelReject = () => {
  isRejecting.value = false;
  rejectOrderData.value = { id: null, remark: '' };
};

const convertToPlan = async (order) => {
  const { value: planDate } = await ElMessageBox.prompt('请选择计划起始日期', '转成计划', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    inputType: 'date'
  });
  if (planDate) {
    order.status = '生产中';
  }
};

const completeOrder = async (order) => {
  const completedQuantity = 10; // 示例数据，实际应从API获取
  completeOrderData.value.id = order.id;
  completeOrderData.value.remarkRequired = completedQuantity < order.quantity;
  isCompleting.value = true;
};

const confirmComplete = async () => {
  if (!completeOrderData.value.remarkRequired || completeOrderData.value.remark) {
    const order = orders.value.find(o => o.id === completeOrderData.value.id);
    if (order) {
      order.status = '已完成';
      order.remark = completeOrderData.value.remark;
    }
    isCompleting.value = false;
    completeOrderData.value = { id: null, remark: '', remarkRequired: false };
  } else {
    ElMessage.error('完成订单需填写备注');
  }
};

const cancelComplete = () => {
  isCompleting.value = false;
  completeOrderData.value = { id: null, remark: '', remarkRequired: false };
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

.new-order-section {
  margin-bottom: 20px;
}

.order-list-section {
  width: 100%;
}

.el-input {
  width: 300px;
}

.el-table {
  width: 100%;
}
</style>
