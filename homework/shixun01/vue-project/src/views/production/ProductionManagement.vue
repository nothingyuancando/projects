<template>
  <div class="product-management">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <h2>产品管理</h2>
      </div>

      <el-input v-model="searchQuery" placeholder="输入产品名称查询" class="input-with-select">
        <!-- 注意：这里不使用 slot="append"，因为我们将在外部添加按钮 -->
      </el-input>
      <div style="display: flex; justify-content: flex-end;">
        <el-button plain @click="dialogFormVisible = true">
          新增产品
        </el-button>
      </div>


      <el-dialog v-model="dialogFormVisible" title="添加产品" width="500">
          <el-form :model="form">
            <el-form-item label="产品名称" :label-width="formLabelWidth">
              <el-input v-model="form.name" autocomplete="off" />
            </el-form-item>
            <el-form-item label="耗能" :label-width="formLabelWidth">
              <el-input v-model="form.name" autocomplete="off" />
            </el-form-item>
          </el-form>
          <template #footer>
            <div class="dialog-footer">
              <el-button @click="dialogFormVisible = false">Cancel</el-button>
              <el-button type="primary" @click="dialogFormVisible = false">
                Confirm
              </el-button>
            </div>
          </template>
        </el-dialog>

<hr/>
      <div class="list-section">
        <el-table :data="filteredProducts" style="width: 100%">
          <el-table-column prop="name" label="产品名称"></el-table-column>
          <el-table-column label="操作">
            <template #default="scope">
              <el-button @click="editProduct(scope.row)" size="mini">编辑</el-button>
              <el-button @click="deleteProduct(scope.row.id)" size="mini" type="danger">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>
    <el-dialog :visible.sync="isEditing" title="编辑产品">
      <el-form :model="editedProduct" ref="editProductForm" label-width="120px">
        <el-form-item label="编辑产品名称">
          <el-input v-model="editedProduct.name"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelEdit">取消</el-button>
        <el-button type="primary" @click="updateProduct">更新</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="js">
// import {ref, computed, onMounted} from 'vue';
// import {ElMessageBox, ElMessage} from 'element-plus';
//
// const searchQuery = ref('');
// const newProduct = ref({name: ''});
// const products = ref([]);
// const isEditing = ref(false);
// const editedProduct = ref(null);
//
// const fetchProducts = async () => {
//   const productData = await fetch('/api/products').then(res => res.json());
//   products.value = productData;
// };
//
// const searchProducts = () => {
//   fetchProducts();
// };
//
// const addProduct = async () => {
//   if (newProduct.value.name && !products.value.some(p => p.name === newProduct.value.name)) {
//     await fetch('/api/products', {
//       method: 'POST',
//       headers: {'Content-Type': 'application/json'},
//       body: JSON.stringify(newProduct.value)
//     });
//     newProduct.value.name = '';
//     fetchProducts();
//   } else {
//     ElMessage.error('产品名称不能为空或重复');
//   }
// };
//
// const editProduct = (product) => {
//   isEditing.value = true;
//   editedProduct.value = {...product};
// };
//
// const updateProduct = async () => {
//   if (editedProduct.value.name && !products.value.some(p => p.name === editedProduct.value.name && p.id !== editedProduct.value.id)) {
//     await fetch(`/api/products/${editedProduct.value.id}`, {
//       method: 'PUT',
//       headers: {'Content-Type': 'application/json'},
//       body: JSON.stringify(editedProduct.value)
//     });
//     cancelEdit();
//     fetchProducts();
//   } else {
//     ElMessage.error('产品名称不能为空或重复');
//   }
// };
//
// const cancelEdit = () => {
//   isEditing.value = false;
//   editedProduct.value = null;
// };
//
// const deleteProduct = async (id) => {
//   const hasOrders = await fetch(`/api/orders?productId=${id}`).then(res => res.json()).then(data => data.length > 0);
//   if (!hasOrders) {
//     ElMessageBox.confirm('此操作将永久删除该产品, 是否继续?', '提示', {
//       confirmButtonText: '确定',
//       cancelButtonText: '取消',
//       type: 'warning',
//     }).then(async () => {
//       await fetch(`/api/products/${id}`, {method: 'DELETE'});
//       fetchProducts();
//     }).catch(() => {
//       ElMessage.info('已取消删除');
//     });
//   } else {
//     ElMessage.error('该产品存在关联订单，不能删除');
//   }
// };
//
// const filteredProducts = computed(() => {
//   return products.value.filter(product => product.name.includes(searchQuery.value));
// });
//
// onMounted(fetchProducts);



import { reactive, ref } from 'vue'

const dialogFormVisible = ref(false)
const formLabelWidth = '140px'

const form = reactive({
  name: '',
  region: '',
  date1: '',
  date2: '',
  delivery: false,
  type: [],
  resource: '',
  desc: '',
})



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

.add-section {
  margin-bottom: 20px;
}

.el-input {
  width: 300px;
}

.el-table {
  width: 100%;
}
</style>
