<template>
  <div class="production-plans-management">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <h2>生产计划管理</h2>
      </div>
      <div class="search-section">
        <el-input v-model="searchQuery" placeholder="输入计划ID或订单ID查询" class="input-with-select">
          <el-button slot="append" @click="searchPlans">查询</el-button>
        </el-input>
      </div>
      <div class="list-section">
        <el-table :data="paginatedPlans" style="width: 100%">
          <el-table-column prop="plan_id" label="计划ID"></el-table-column>
          <el-table-column prop="order_id" label="订单ID"></el-table-column>
          <el-table-column prop="starttime" label="开始日期"></el-table-column>
          <el-table-column prop="endtime" label="结束日期"></el-table-column>
          <el-table-column prop="plan_states" label="状态">
            <template #default="scope">
              {{ statusMap[scope.row.plan_states] }}
            </template>
          </el-table-column>
          <el-table-column label="操作">
            <template #default="scope">
              <el-button v-if="scope.row.plan_states === 1" @click="acceptPlan(scope.row)" size="mini" type="success">接收</el-button>
              <el-button v-if="scope.row.plan_states === 1" @click="rejectPlan(scope.row)" size="mini" type="danger">拒绝</el-button>
              <el-button v-if="scope.row.plan_states === 2" @click="completePlan(scope.row)" size="mini" type="primary">完成</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="pagination-container">
          <el-pagination
              background
              layout="prev, pager, next"
              :total="filteredPlans.length"
              :page-size="pageSize"
              :current-page.sync="currentPage"
              @current-change="handlePageChange">
          </el-pagination>
        </div>
      </div>
    </el-card>

    <!-- 接收计划确认弹窗 -->
    <el-dialog v-model="acceptDialogVisible" title="确认接收计划">
      <p>确定要接收计划 "{{ acceptingPlanId }}" 吗？</p>
      <div slot="footer" class="dialog-footer">
        <el-button @click="acceptPlanConfirmed">确认接收</el-button>
        <el-button @click="acceptDialogVisible = false">取消</el-button>
      </div>
    </el-dialog>

    <!-- 拒绝计划确认弹窗 -->
    <el-dialog v-model="rejectDialogVisible" title="确认拒绝计划">
      <p>确定要拒绝计划 "{{ rejectingPlanId }}" 吗？</p>
      <div slot="footer" class="dialog-footer">
        <el-button @click="rejectPlanConfirmed">确认拒绝</el-button>
        <el-button @click="rejectDialogVisible = false">取消</el-button>
      </div>
    </el-dialog>

    <!-- 完成计划确认弹窗 -->
    <el-dialog v-model="completeDialogVisible" title="确认完成计划">
      <p>确定要完成计划 "{{ completingPlanId }}" 吗？</p>
      <div slot="footer" class="dialog-footer">
        <el-button @click="completePlanConfirmed">确认完成</el-button>
        <el-button @click="completeDialogVisible = false">取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import {ref, computed, onMounted} from 'vue';
import {ElMessage} from 'element-plus';
import {
  fetchProductionPlansFromServer,
  acceptPlanOnServer,
  rejectPlanOnServer,
  completePlanOnServer,
} from '@/api/factory/productionplan.js';

const searchQuery = ref('');
const acceptDialogVisible = ref(false);
const rejectDialogVisible = ref(false);
const completeDialogVisible = ref(false);
const plans = ref([]);
const acceptingPlanId = ref(null);
const rejectingPlanId = ref(null);
const completingPlanId = ref(null);
const pageSize = ref(10);
const currentPage = ref(1);

const statusMap = {
  1: '待接收',
  2: '已接收',
  3: '已拒绝',
  4: '已完成',
};

const fetchPlans = async () => {
  try {
    const response = await fetchProductionPlansFromServer();
    if (response.code === 200 && Array.isArray(response.data)) {
      plans.value = response.data;
    } else {
      throw new Error('API response is not an array');
    }
  } catch (error) {
    ElMessage.error('获取生产计划列表失败');
  }
};

const searchPlans = async () => {
  await fetchPlans();
};

const acceptPlan = (plan) => {
  acceptingPlanId.value = plan.plan_id;
  acceptDialogVisible.value = true;
};

const acceptPlanConfirmed = async () => {
  try {
    const response = await acceptPlanOnServer({
      plan_id: acceptingPlanId.value,
      factory_id: 7,
      plan_states: 2
    });
    if (response.code === 200) {
      ElMessage.success('计划接收成功');
      acceptDialogVisible.value = false;
      fetchPlans();
    } else {
      throw new Error(response.message || '接收计划失败');
    }
  } catch (error) {
    ElMessage.error(error.message);
  }
};

const rejectPlan = (plan) => {
  rejectingPlanId.value = plan.plan_id;
  rejectDialogVisible.value = true;
};

const rejectPlanConfirmed = async () => {
  try {
    const response = await rejectPlanOnServer({
      plan_id: rejectingPlanId.value,
      factory_id: 7,
      plan_states: 3
    });
    if (response.code === 200) {
      ElMessage.success('计划拒绝成功');
      rejectDialogVisible.value = false;
      fetchPlans();
    } else {
      throw new Error(response.message || '拒绝计划失败');
    }
  } catch (error) {
    ElMessage.error(error.message);
  }
};

const completePlan = (plan) => {
  completingPlanId.value = plan.plan_id;
  completeDialogVisible.value = true;
};

const completePlanConfirmed = async () => {
  try {
    const response = await completePlanOnServer({
      plan_id: completingPlanId.value,
      factory_id: 7,
      plan_states: 4
    });
    if (response.code === 200) {
      ElMessage.success('计划完成成功');
      completeDialogVisible.value = false;
      fetchPlans();
    } else {
      throw new Error(response.message || '完成计划失败');
    }
  } catch (error) {
    ElMessage.error(error.message);
  }
};

const handlePageChange = (page) => {
  currentPage.value = page;
};

const filteredPlans = computed(() => {
  return plans.value.filter(plan =>
      plan.plan_id.toString().includes(searchQuery.value) ||
      plan.order_id.toString().includes(searchQuery.value)
  );
});

const paginatedPlans = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return filteredPlans.value.slice(start, end);
});

onMounted(fetchPlans);
</script>

<style scoped lang="scss">
.production-plans-management {
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
