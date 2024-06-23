<template>
  <div class="plan-management">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <h2>生产计划管理</h2>
      </div>
      <div class="new-plan-section">
        <el-form :model="newPlan" ref="newPlanForm" label-width="120px">
          <el-form-item label="订单编号">
            <el-select v-model="newPlan.orderId" placeholder="选择订单">
              <el-option
                  v-for="order in productionOrders"
                  :key="order.id"
                  :label="`订单编号: ${order.id}, 产品: ${order.productName}`"
                  :value="order.id"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="createPlan">新建生产计划</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div class="plan-list-section">
        <el-table :data="plans" style="width: 100%">
          <el-table-column prop="orderId" label="订单编号"></el-table-column>
          <el-table-column prop="startDate" label="计划开始日期"></el-table-column>
          <el-table-column prop="status" label="计划状态"></el-table-column>
          <el-table-column label="操作">
            <template #default="scope">
              <el-button v-if="scope.row.status === '未启动'" @click="startPlan(scope.row)" size="mini">启动</el-button>
              <el-button v-if="scope.row.status === '未启动'" @click="confirmDeletePlan(scope.row)" size="mini" type="danger">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>
    <el-dialog :visible.sync="isConfirmingDelete" title="确认删除">
      <span>确定要删除该生产计划吗？</span>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelDelete">取消</el-button>
        <el-button type="danger" @click="deletePlan">删除</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ElMessageBox, ElMessage } from 'element-plus';

const newPlan = ref({ orderId: null });
const plans = ref([]);
const productionOrders = ref([]);
const isConfirmingDelete = ref(false);
const planToDelete = ref(null);

const fetchPlans = async () => {
  // 示例数据，实际应从API获取
  const planData = [
    { id: 1, orderId: 1, startDate: '2024-06-01', status: '未启动' },
    { id: 2, orderId: 2, startDate: '2024-06-02', status: '未启动' }
  ];
  plans.value = planData;
};

const fetchProductionOrders = async () => {
  // 示例数据，实际应从API获取
  const orderData = [
    { id: 1, productName: '产品A', status: '生产中' },
    { id: 2, productName: '产品B', status: '生产中' }
  ];
  productionOrders.value = orderData;
};

const createPlan = async () => {
  if (!newPlan.value.orderId) {
    ElMessage.error('请选择订单编号');
    return;
  }
  plans.value.push({
    id: plans.value.length + 1,
    orderId: newPlan.value.orderId,
    startDate: new Date().toISOString().split('T')[0],
    status: '未启动'
  });
  newPlan.value.orderId = null;
};

const startPlan = async (plan) => {
  plan.status = '已启动';
  ElMessage.success('生产计划已启动');
};

const confirmDeletePlan = (plan) => {
  planToDelete.value = plan;
  isConfirmingDelete.value = true;
};

const deletePlan = async () => {
  if (planToDelete.value) {
    const index = plans.value.findIndex(p => p.id === planToDelete.value.id);
    if (index !== -1) {
      plans.value.splice(index, 1);
      ElMessage.success('生产计划已删除');
    }
  }
  isConfirmingDelete.value = false;
  planToDelete.value = null;
};

const cancelDelete = () => {
  isConfirmingDelete.value = false;
  planToDelete.value = null;
};

onMounted(() => {
  fetchPlans();
  fetchProductionOrders();
});
</script>


<style scoped lang="scss">
.plan-management {
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

.new-plan-section {
  margin-bottom: 20px;
}

.plan-list-section {
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

