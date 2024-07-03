<template>
  <div class="order-management">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <h2>工单管理</h2>
      </div>
      <div class="actions-section">
        <el-button @click="openNewOrderDialog">新建工单</el-button>
      </div>
      <div class="list-section">
        <el-table :data="orders" style="width: 100%">
          <el-table-column prop="workorder_id" label="工单编号"></el-table-column>
          <el-table-column prop="product_id" label="产品ID"></el-table-column>
          <el-table-column prop="equipment_id" label="设备ID"></el-table-column>
          <el-table-column prop="workorder_states" label="状态">
            <template #default="scope">
              {{ getStatusText(scope.row.workorder_states) }}
            </template>
          </el-table-column>
          <el-table-column prop="starttime" label="开始时间"></el-table-column>
          <el-table-column prop="endtime" label="结束时间"></el-table-column>
          <el-table-column label="操作">
            <template #default="scope">
              <el-button
                  @click="acceptOrder(scope.row)"
                  size="mini"
                  :disabled="scope.row.workorder_states !== 1"
              >
                接受工单
              </el-button>
<!--              <el-button-->
<!--                  @click="rejectOrder(scope.row)"-->
<!--                  size="mini"-->
<!--                  type="danger"-->
<!--                  :disabled="scope.row.workorder_states !== 1"-->
<!--              >-->
<!--                拒绝工单-->
<!--              </el-button>-->
              <el-button
                  @click="produceOrder(scope.row)"
                  size="mini"
                  type="primary"
                  :disabled="scope.row.workorder_states !== 2"
              >
                生产工单
              </el-button>
              <el-button
                  @click="completeOrder(scope.row)"
                  size="mini"
                  type="success"
                  :disabled="scope.row.workorder_states !== 3"
              >
                完成工单
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>

    <!-- 新建工单弹窗 -->
    <el-dialog v-model="newOrderDialogVisible" title="新建工单">
      <el-form :model="newOrderForm">
        <el-form-item label="设备ID">
          <el-input v-model="newOrderForm.equipmentId"></el-input>
        </el-form-item>
        <el-form-item label="产品ID">
          <el-input v-model="newOrderForm.productId"></el-input>
        </el-form-item>
        <el-form-item label="生产数量">
          <el-input v-model="newOrderForm.produceNumber"></el-input>
        </el-form-item>
        <el-form-item label="开始时间">
          <el-date-picker v-model="newOrderForm.starttime" type="datetime"></el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间">
          <el-date-picker v-model="newOrderForm.endtime" type="datetime"></el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="newOrderDialogVisible = false">取消</el-button>
        <el-button @click="createNewOrder" type="primary">确认创建</el-button>
      </div>
    </el-dialog>

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

    <!-- 完成工单确认弹窗 -->
    <el-dialog v-model="completeOrderDialogVisible" title="确认完成工单">
      <p>确定要完成工单 "{{ completingOrderName }}" 吗？</p>
      <div slot="footer" class="dialog-footer">
        <el-button @click="confirmCompleteOrder" type="success">确认完成</el-button>
        <el-button @click="completeOrderDialogVisible = false">取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { getWorkOrders, createWorkOrder, updateWorkOrderStatus, rejectWorkOrder } from '@/api/factory/workOrder.js';

const acceptOrderDialogVisible = ref(false);
const rejectOrderDialogVisible = ref(false);
const produceOrderDialogVisible = ref(false);
const completeOrderDialogVisible = ref(false);
const newOrderDialogVisible = ref(false);

const acceptingOrderId = ref(null);
const acceptingOrderName = ref('');
const rejectingOrderId = ref(null);
const rejectingOrderName = ref('');
const producingOrderId = ref(null);
const producingOrderName = ref('');
const completingOrderId = ref(null);
const completingOrderName = ref('');

const orders = ref([]);

const newOrderForm = reactive({
  equipmentId: '',
  productId: '',
  produceNumber: '',
  starttime: '',
  endtime: '',
  workorderStates: '1'
});

const fetchOrders = async () => {
  try {
    const response = await getWorkOrders();
    if (response.code === 200) {
      orders.value = response.data;
    } else {
      ElMessage.error(response.message || '获取工单列表失败');
    }
  } catch (error) {
    console.error('获取工单列表失败', error);
    ElMessage.error('获取工单列表失败');
  }
};

const openNewOrderDialog = () => {
  newOrderDialogVisible.value = true;
};

const createNewOrder = async () => {
  try {
    const response = await createWorkOrder(newOrderForm);
    if (response.code === 200) {
      ElMessage.success('工单创建成功');
      newOrderDialogVisible.value = false;
      await fetchOrders();
    } else {
      ElMessage.error(response.message || '工单创建失败');
    }
  } catch (error) {
    console.error('工单创建失败', error);
    ElMessage.error('工单创建失败');
  }
};

const acceptOrder = (order) => {
  acceptingOrderId.value = order.workorder_id;
  acceptingOrderName.value = order.name;
  acceptOrderDialogVisible.value = true;
};

const rejectOrder = (order) => {
  rejectingOrderId.value = order.workorder_id;
  rejectingOrderName.value = order.name;
  rejectOrderDialogVisible.value = true;
};

const produceOrder = (order) => {
  producingOrderId.value = order.workorder_id;
  producingOrderName.value = order.name;
  produceOrderDialogVisible.value = true;
};

const completeOrder = (order) => {
  completingOrderId.value = order.workorder_id;
  completingOrderName.value = order.name;
  completeOrderDialogVisible.value = true;
};

const confirmAcceptOrder = async () => {
  try {
    const response = await updateWorkOrderStatus(acceptingOrderId.value, 2);
    if (response.code === 200) {
      ElMessage.success('工单接受成功');
      acceptOrderDialogVisible.value = false;
      await fetchOrders();
    } else {
      ElMessage.error(response.message || '工单接受失败');
    }
  } catch (error) {
    console.error('工单接受失败', error);
    ElMessage.error('工单接受失败');
  }
};

const confirmRejectOrder = async () => {
  try {
    const response = await rejectWorkOrder(rejectingOrderId.value);
    if (response.code === 200) {
      ElMessage.success('工单拒绝成功');
      rejectOrderDialogVisible.value = false;
      await fetchOrders();
    } else {
      ElMessage.error(response.message || '工单拒绝失败');
    }
  } catch (error) {
    console.error('工单拒绝失败', error);
    ElMessage.error('工单拒绝失败');
  }
};

const confirmProduceOrder = async () => {
  try {
    const response = await updateWorkOrderStatus(producingOrderId.value, 3);
    if (response.code === 200) {
      ElMessage.success('工单生产中');
      produceOrderDialogVisible.value = false;
      await fetchOrders();
    } else {
      ElMessage.error(response.message || '工单生产失败');
    }
  } catch (error) {
    console.error('工单生产失败', error);
    ElMessage.error('工单生产失败');
  }
};

const confirmCompleteOrder = async () => {
  try {
    const response = await updateWorkOrderStatus(completingOrderId.value, 4);
    if (response.code === 200) {
      ElMessage.success('工单已完成');
      completeOrderDialogVisible.value = false;
      await fetchOrders();
    } else {
      ElMessage.error(response.message || '工单完成失败');
    }
  } catch (error) {
    console.error('工单完成失败', error);
    ElMessage.error('工单完成失败');
  }
};

const getStatusText = (status) => {
  switch (status) {
    case 1:
      return '未接单';
    case 2:
      return '已接单';
    case 3:
      return '生产中';
    case 4:
      return '已完成';
    default:
      return '未知状态';
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

.actions-section {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 20px;
}

.el-table {
  width: 100%;
}
</style>
