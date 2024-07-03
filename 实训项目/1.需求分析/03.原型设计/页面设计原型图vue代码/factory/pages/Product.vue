<template>
  <div class="product-management">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <h2>产品管理</h2>
      </div>

      <div class="search-section">
        <el-input v-model="searchQuery" placeholder="输入产品名称查询" class="input-with-select"></el-input>
      </div>
      <div style="display: flex; justify-content: flex-end;">
        <el-button plain @click="openAddDialog">新增产品</el-button>
      </div>

      <el-dialog v-model="dialogFormVisible" :title="isEditing ? '编辑产品' : '添加产品'" width="500">
        <el-form :model="form" ref="formRef" label-width="120px">
          <el-form-item label="产品名称" :label-width="formLabelWidth" prop="name">
            <el-input v-model="form.name" autocomplete="off" />
          </el-form-item>
          <el-form-item label="耗能" :label-width="formLabelWidth" prop="energyConsumption">
            <el-input v-model="form.energyConsumption" autocomplete="off" />
          </el-form-item>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="dialogFormVisible = false">取消</el-button>
            <el-button type="primary" @click="isEditing ? updateProduct() : addProduct()">
              确认
            </el-button>
          </div>
        </template>
      </el-dialog>

      <hr/>

      <div class="list-section">
        <el-table :data="paginatedProducts" style="width: 100%">
          <el-table-column prop="name" label="产品名称"></el-table-column>
          <el-table-column prop="energyConsumption" label="耗能"></el-table-column>
          <el-table-column label="操作">
            <template #default="scope">
              <el-button @click="openEditDialog(scope.row)" size="mini">编辑</el-button>
              <el-button @click="deleteProduct(scope.row.id)" size="mini" type="danger">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="pagination-container">
          <el-pagination
              background
              layout="prev, pager, next"
              :total="filteredProducts.length"
              :page-size="pageSize"
              :current-page.sync="currentPage"
              @current-change="handlePageChange">
          </el-pagination>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref, computed, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import {
  fetchProductsFromServer,
  addProductToServer,
  updateProductOnServer,
  deleteProductFromServer
} from '@/api/product.js';

const searchQuery = ref('');
const dialogFormVisible = ref(false);
const isEditing = ref(false);
const formLabelWidth = '140px';

const form = reactive({
  id: null,
  name: '',
  energyConsumption: ''
});

const products = ref([]);

const fetchProducts = async () => {
  try {
    const response = await fetchProductsFromServer();
    if (Array.isArray(response.data)) {
      products.value = response.data;
    } else {
      throw new Error('API response is not an array');
    }
  } catch (error) {
    ElMessage.error('获取产品列表失败');
  }
};

const filteredProducts = computed(() => {
  return products.value.filter(product => product.name.includes(searchQuery.value));
});

const pageSize = ref(10); // 每页显示的产品数
const currentPage = ref(1);

const paginatedProducts = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return filteredProducts.value.slice(start, end);
});

const handlePageChange = (page) => {
  currentPage.value = page;
};

const openAddDialog = () => {
  resetForm();
  isEditing.value = false;
  dialogFormVisible.value = true;
};

const openEditDialog = (product) => {
  Object.assign(form, product);
  isEditing.value = true;
  dialogFormVisible.value = true;
};

const addProduct = async () => {
  try {
    await addProductToServer(form);
    ElMessage.success('产品添加成功');
    dialogFormVisible.value = false;
    await fetchProducts();
    resetForm();
  } catch (error) {
    ElMessage.error('产品添加失败');
  }
};

const updateProduct = async () => {
  try {
    await updateProductOnServer(form);
    ElMessage.success('产品更新成功');
    dialogFormVisible.value = false;
    await fetchProducts();
    resetForm();
  } catch (error) {
    ElMessage.error('产品更新失败');
  }
};

const deleteProduct = async (id) => {
  ElMessageBox.confirm('此操作将永久删除该产品, 是否继续?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(async () => {
    try {
      await deleteProductFromServer(id);
      ElMessage.success('产品删除成功');
      await fetchProducts();
    } catch (error) {
      ElMessage.error('产品删除失败');
    }
  }).catch(() => {
    ElMessage.info('已取消删除');
  });
};

const resetForm = () => {
  form.id = null;
  form.name = '';
  form.energyConsumption = '';
};

onMounted(fetchProducts);
</script>

<style scoped lang="scss">
.product-management {
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
