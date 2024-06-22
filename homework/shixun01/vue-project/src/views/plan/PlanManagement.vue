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
                  :label="order.id"
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
              <el-button v-if="scope.row.status === '未启动'" @click="deletePlan(scope.row)" size="mini" type="danger">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ElMessageBox, ElMessage } from 'element-plus';

const newPlan = ref({ orderId: null });
const plans = ref([]);
const productionOrders = ref([]);

const fetchPlans = async () => {
  const planData = await fetch('/api/plans').then(res => res.json());
  plans.value = planData;
};

const fetchProductionOrders = async () => {
  const orderData = await fetch('/api/orders?status=生产中').then(res => res.json());
  productionOrders.value = orderData;
};

const createPlan = async () => {
  if (!newPlan.value.orderId) {
    ElMessage.error('请选择订单编号');
    return;
  }
  await fetch('/api/plans', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ orderId: newPlan.value.orderId, status: '未启动', startDate: new Date().toISOString().split('T')[0] })
  });
  newPlan.value.orderId = null;
  fetchPlans();
};

const startPlan = async (plan) => {
  await fetch(`/api/plans/${plan.id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ ...plan, status: '已启动' })
  });
  await fetch('/api/work-orders', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ planId: plan.id, status: '未开始', deviceId: null })
  });
  fetchPlans();
};

const deletePlan = async (plan) => {
  const orderPlans = plans.value.filter(p => p.orderId === plan.orderId);
  if (orderPlans.length <= 1) {
    ElMessage.error('每条生产中订单必须有一条以上生产计划');
    return;
  }
  await fetch(`/api/plans/${plan.id}`, {
    method: 'DELETE'
  });
  fetchPlans();
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
</style>
