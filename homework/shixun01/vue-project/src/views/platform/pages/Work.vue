<template>
  <div class="dispatch-form">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <h2>生产调度</h2>
      </div>
      <el-form :model="dispatchForm" ref="dispatchFormRef" label-width="120px">
        <el-form-item label="生产计划" required>
          <el-select v-model="dispatchForm.plan" placeholder="选择生产计划">
            <el-option
                v-for="plan in plans"
                :key="plan.id"
                :label="plan.name"
                :value="plan.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="工厂" required>
          <el-select v-model="dispatchForm.factory" placeholder="选择工厂">
            <el-option
                v-for="factory in factories"
                :key="factory.id"
                :label="factory.name"
                :value="factory.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="dispatchPlan">分发计划</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="box-card" style="margin-top: 20px;">
      <div slot="header" class="clearfix">
        <h2>已下发的生产调度列表</h2>
      </div>
      <el-table :data="dispatchList" style="width: 100%">
        <el-table-column prop="planName" label="计划名称" width="180"></el-table-column>
        <el-table-column prop="factoryName" label="工厂名称" width="180"></el-table-column>
        <el-table-column prop="dispatchTime" label="分发时间" width="180"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button @click="viewDetails(scope.row)" type="primary" size="mini">查看详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 弹窗: 查看计划详情 -->
    <el-dialog :visible.sync="dialogVisible.planDetails" title="计划详情">
      <div v-if="selectedPlan">
        <p><strong>计划名称:</strong> {{ selectedPlan.name }}</p>
        <p><strong>开始时间:</strong> {{ selectedPlan.starttime }}</p>
        <p><strong>结束时间:</strong> {{ selectedPlan.endtime }}</p>
        <p><strong>产品:</strong> {{ selectedPlan.product }}</p>
        <p><strong>备注:</strong> {{ selectedPlan.remark }}</p>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible.planDetails = false">关闭</el-button>
      </span>
    </el-dialog>

    <!-- 弹窗: 查看工厂详情 -->
    <el-dialog :visible.sync="dialogVisible.factoryDetails" title="工厂详情">
      <div v-if="selectedFactory">
        <p><strong>工厂名称:</strong> {{ selectedFactory.name }}</p>
        <p><strong>创建时间:</strong> {{ selectedFactory.create_time }}</p>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible.factoryDetails = false">关闭</el-button>
      </span>
    </el-dialog>

    <!-- 弹窗: 确认分发 -->
    <el-dialog :visible.sync="dialogVisible.confirmDispatch" title="确认分发">
      <div>
        <p>确定要将计划 <strong>{{ getPlanName(dispatchForm.plan) }}</strong> 分发到工厂 <strong>{{ getFactoryName(dispatchForm.factory) }}</strong> 吗？</p>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible.confirmDispatch = false">取消</el-button>
        <el-button type="primary" @click="confirmDispatch">确认</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script setup>
import {ref, reactive, onMounted} from 'vue';
import {ElMessage} from 'element-plus';
import {fetchPlans, fetchFactories, dispatchPlanToFactory, fetchDispatchList} from '@/api/platform/platform.js';

const plans = ref([]);
const factories = ref([]);
const dispatchForm = reactive({
  plan: '',
  factory: ''
});

const dispatchList = ref([]);

const dialogVisible = reactive({
  planDetails: false,
  factoryDetails: false,
  confirmDispatch: false
});

const selectedPlan = ref(null);
const selectedFactory = ref(null);

const loadPlansAndFactories = async () => {
  plans.value = await fetchPlans();
  factories.value = await fetchFactories();
};

const loadDispatchList = async () => {
  dispatchList.value = await fetchDispatchList();
};

const getPlanName = (planId) => {
  const plan = plans.value.find(plan => plan.id === planId);
  return plan ? plan.name : '';
};

const getFactoryName = (factoryId) => {
  const factory = factories.value.find(factory => factory.id === factoryId);
  return factory ? factory.name : '';
};

const dispatchPlan = () => {
  if (!dispatchForm.plan || !dispatchForm.factory) {
    ElMessage.warning('请选择生产计划和工厂');
    return;
  }
  dialogVisible.confirmDispatch = true;
};

const confirmDispatch = async () => {
  try {
    await dispatchPlanToFactory(dispatchForm.plan, dispatchForm.factory);
    ElMessage.success('计划分发成功');
    dialogVisible.confirmDispatch = false;
    resetForm();
    loadDispatchList(); // 重新加载分发列表
  } catch (error) {
    ElMessage.error('计划分发失败');
  }
};

const resetForm = () => {
  dispatchForm.plan = '';
  dispatchForm.factory = '';
};

const showPlanDetails = (planId) => {
  selectedPlan.value = plans.value.find(plan => plan.id === planId);
  dialogVisible.planDetails = true;
};

const showFactoryDetails = (factoryId) => {
  selectedFactory.value = factories.value.find(factory => factory.id === factoryId);
  dialogVisible.factoryDetails = true;
};

const viewDetails = (dispatch) => {
  showPlanDetails(dispatch.planId);
  showFactoryDetails(dispatch.factoryId);
};

onMounted(() => {
  loadPlansAndFactories();
  loadDispatchList();
});
</script>

<style scoped>
.dispatch-form {
  padding: 20px;
}

.el-input,
.el-select {
  width: 100%;
}

.el-card {
  margin-bottom: 20px;
}
</style>
