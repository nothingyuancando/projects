<template>
  <div class="order-form">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <h2>下单</h2>
      </div>
      <el-form :model="orderForm" ref="orderFormRef" label-width="120px">
        <el-button type="primary" icon="el-icon-plus" @click="addProduct" style="margin-top: 10px;">添加产品</el-button>
        <el-form-item label="订单产品" required>
          <div v-for="(product, index) in orderForm.order_details" :key="index" class="product-item">
            <el-input v-model="product.product_id" placeholder="产品ID" style="width: 40%; margin-right: 10px;"></el-input>
            <el-input-number v-model="product.amount" :min="1" placeholder="数量" style="width: 20%; margin-right: 10px;"></el-input-number>
            <el-button type="danger" icon="el-icon-minus" @click="removeProduct(index)">删除产品</el-button>
          </div>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitOrder">提交订单</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue';
import { ElMessage } from 'element-plus';
import { submitOrderToServer } from '@/api/customer/customer.js';

const orderForm = reactive({
  order_states: 1,
  order_details: [
    { product_id: '', amount: 1 },
  ],
});

const addProduct = () => {
  orderForm.order_details.push({ product_id: '', amount: 1 });
};

const removeProduct = (index) => {
  if (orderForm.order_details.length > 1) {
    orderForm.order_details.splice(index, 1);
  } else {
    ElMessage.warning('订单至少包含一个产品');
  }
};

const submitOrder = async () => {
  try {
    await submitOrderToServer(orderForm);
    ElMessage.success('订单提交成功');
    resetForm();
  } catch (error) {
    ElMessage.error('订单提交失败');
  }
};

const resetForm = () => {
  orderForm.order_details = [{ product_id: '', amount: 1 }];
};
</script>

<style scoped>
.order-form {
  padding: 20px;
}

.product-item {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.el-input,
.el-input-number {
  width: 100%;
}
</style>
