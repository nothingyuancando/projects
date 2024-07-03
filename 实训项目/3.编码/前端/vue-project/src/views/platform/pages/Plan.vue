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
                  :key="order.order_id"
                  :label="`订单编号: ${order.order_id}`"
                  :value="order.order_id"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="开始日期">
            <el-date-picker v-model="newPlan.starttime" type="date" placeholder="选择日期"></el-date-picker>
          </el-form-item>
          <el-form-item label="结束日期">
            <el-date-picker v-model="newPlan.endtime" type="date" placeholder="选择日期"></el-date-picker>
          </el-form-item>
          <el-form-item label="工厂编号">
            <el-input v-model="newPlan.factoryId" placeholder="输入工厂编号"></el-input>
          </el-form-item>
          <el-form-item label="产品编号">
            <el-input v-model="newPlan.productId" placeholder="输入产品编号"></el-input>
          </el-form-item>
          <el-form-item label="备注">
            <el-input v-model="newPlan.remark" placeholder="输入备注"></el-input>
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
              <el-button v-if="scope.row.status === 1" @click="updatePlanStatus(scope.row, 2)" size="mini">启动</el-button>
              <el-button v-if="scope.row.status === 1" @click="confirmDeletePlan(scope.row)" size="mini" type="danger">删除</el-button>
              <el-button v-if="scope.row.status === 2" @click="updatePlanStatus(scope.row, 3)" size="mini" type="success">完成</el-button>
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
import { ElMessage } from 'element-plus';
import { getProductionPlans, getProductionOrders, createProductionPlan, updateProductionPlanStatus, deleteProductionPlan } from '@/api/platform/plan.js';

const newPlan = ref({
  orderId: null,
  starttime: '',
  endtime: '',
  factoryId: '',
  productId: '',
  remark: ''
});
const plans = ref([]);
const productionOrders = ref([]);
const isConfirmingDelete = ref(false);
const planToDelete = ref(null);

const fetchPlans = async () => {
  try {
    const response = await getProductionPlans();
    if (response.code === 200) {
      plans.value = response.data;
    } else {
      ElMessage.error(response.message || '获取生产计划列表失败');
    }
  } catch (error) {
    ElMessage.error('获取生产计划列表失败');
  }
};

const fetchProductionOrders = async () => {
  try {
    const response = await getProductionOrders();
    if (response.code === 200) {
      productionOrders.value = response.data;
    } else {
      ElMessage.error(response.message || '获取订单列表失败');
    }
  } catch (error) {
    ElMessage.error('获取订单列表失败');
  }
};

const createPlan = async () => {
  try {
    const response = await createProductionPlan(newPlan.value);
    if (response.code === 200) {
      ElMessage.success('生产计划创建成功');
      fetchPlans(); // 刷新生产计划列表
    } else {
      ElMessage.error(response.message || '生产计划创建失败');
    }
  } catch (error) {
    ElMessage.error('生产计划创建失败');
  }
};

const updatePlanStatus = async (plan, status) => {
  try {
    const response = await updateProductionPlanStatus({ planId: plan.id, planStates: status });
    if (response.code === 200) {
      ElMessage.success('生产计划状态更新成功');
      fetchPlans(); // 刷新生产计划列表
    } else {
      ElMessage.error(response.message || '生产计划状态更新失败');
    }
  } catch (error) {
    ElMessage.error('生产计划状态更新失败');
  }
};

const confirmDeletePlan = (plan) => {
  planToDelete.value = plan;
  isConfirmingDelete.value = true;
};

const deletePlan = async () => {
  try {
    const response = await deleteProductionPlan(planToDelete.value.id);
    if (response.code === 200) {
      ElMessage.success('生产计划已删除');
      fetchPlans(); // 刷新生产计划列表
    } else {
      ElMessage.error(response.message || '生产计划删除失败');
    }
  } catch (error) {
    ElMessage.error('生产计划删除失败');
  } finally {
    isConfirmingDelete.value = false;
    planToDelete.value = null;
  }
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
.el-select,
.el-date-picker {
  width: 300px;
}

.el-table {
  width: 100%;
}

.dialog-footer {
  text-align: right;
}
</style>
