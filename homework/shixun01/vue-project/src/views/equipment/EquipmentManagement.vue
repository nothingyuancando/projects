<template>
  <div class="device-management">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <h2>设备管理</h2>
      </div>
      <div class="search-section">
        <el-input v-model="searchQuery" placeholder="输入产品名称或设备名称查询" class="input-with-select">
          <el-button slot="append" @click="searchDevices">查询</el-button>
        </el-input>
      </div>
      <div style="display: flex; justify-content: flex-end; margin-bottom: 20px;">
        <el-button plain @click="openAddDialog">新增设备</el-button>
      </div>
      <div class="list-section">
        <el-table :data="paginatedDevices" style="width: 100%">
          <el-table-column prop="name" label="设备名称"></el-table-column>
          <el-table-column prop="serialNumber" label="设备序列号"></el-table-column>
          <el-table-column prop="capacity" label="产能"></el-table-column>
          <el-table-column label="操作">
            <template #default="scope">
              <el-button @click="openEditDialog(scope.row)" size="mini">编辑</el-button>
              <el-button @click="viewDeviceProducts(scope.row)" size="mini">查看产品</el-button>
              <el-button @click="confirmDeleteDevice(scope.row)" size="mini" type="danger">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="pagination-container">
          <el-pagination
              background
              layout="prev, pager, next"
              :total="filteredDevices.length"
              :page-size="pageSize"
              :current-page.sync="currentPage"
              @current-change="handlePageChange">
          </el-pagination>
        </div>
      </div>
    </el-card>

    <!-- 新增/编辑设备弹窗 -->
    <el-dialog v-model="dialogFormVisible" :title="isEditing ? '编辑设备' : '新增设备'" width="500">
      <el-form :model="form" ref="formRef" label-width="120px">
        <el-form-item label="设备名称">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="设备序列号">
          <el-input v-model="form.serialNumber"></el-input>
        </el-form-item>
        <el-form-item label="产能">
          <el-input v-model="form.capacity"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取消</el-button>
          <el-button type="primary" @click="isEditing ? updateDevice() : addDevice()">确认</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 查看设备关联产品弹窗 -->
    <el-dialog v-model="viewingProducts" title="设备关联产品" width="600">
      <div style="margin-bottom: 20px;">
        <el-form :model="newProduct" label-width="120px">
          <el-form-item label="产品名称">
            <el-input v-model="newProduct.name"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="addProductToDevice">添加产品</el-button>
          </el-form-item>
        </el-form>
      </div>
      <el-table :data="currentDeviceProducts" style="width: 100%; margin-top: 20px;">
        <el-table-column prop="name" label="产品名称"></el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button @click="confirmDeleteProduct(scope.row)" size="mini" type="danger">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="viewingProducts = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 删除设备确认弹窗 -->
    <el-dialog v-model="deleteDeviceDialogVisible" title="确认删除设备">
      <p>确定要删除设备 "{{ deletingDeviceName }}" 吗？</p>
      <div slot="footer" class="dialog-footer">
        <el-button @click="deleteDevice(deletingDeviceId)">确认删除</el-button>
        <el-button @click="deleteDeviceDialogVisible = false">取消</el-button>
      </div>
    </el-dialog>

    <!-- 删除产品确认弹窗 -->
    <el-dialog v-model="deleteProductDialogVisible" title="确认删除产品">
      <p>确定要删除产品 "{{ deletingProductName }}" 吗？</p>
      <div slot="footer" class="dialog-footer">
        <el-button @click="deleteProduct(deletingProductId)">确认删除</el-button>
        <el-button @click="deleteProductDialogVisible = false">取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';

const searchQuery = ref('');
const dialogFormVisible = ref(false);
const isEditing = ref(false);
const viewingProducts = ref(false);
const deleteDeviceDialogVisible = ref(false);
const deleteProductDialogVisible = ref(false);
const form = reactive({ id: null, name: '', serialNumber: '', capacity: '' });
const devices = ref([]);
const currentDeviceProducts = ref([]);
const deletingDeviceId = ref(null);
const deletingDeviceName = ref('');
const deletingProductId = ref(null);
const deletingProductName = ref('');
const pageSize = ref(10);
const currentPage = ref(1);

const newProduct = reactive({ name: '' });

const exampleDevices = [
  { id: 1, name: '设备1', serialNumber: 'SN001', capacity: '100' },
  { id: 2, name: '设备2', serialNumber: 'SN002', capacity: '200' },
  { id: 3, name: '设备3', serialNumber: 'SN003', capacity: '300' },
];

const exampleProducts = [
  { id: 1, name: '产品A' },
  { id: 2, name: '产品B' },
  { id: 3, name: '产品C' },
];

const fetchDevices = async () => {
  devices.value = exampleDevices;
};

const searchDevices = () => {
  fetchDevices();
};

const openAddDialog = () => {
  resetForm();
  isEditing.value = false;
  dialogFormVisible.value = true;
};

const openEditDialog = (device) => {
  Object.assign(form, device);
  isEditing.value = true;
  dialogFormVisible.value = true;
};

const addDevice = async () => {
  if (form.serialNumber && !devices.value.some(d => d.serialNumber === form.serialNumber)) {
    form.id = Date.now();
    devices.value.push({ ...form });
    ElMessage.success('设备添加成功');
    dialogFormVisible.value = false;
    resetForm();
  } else {
    ElMessage.error('设备序列号不能为空或重复');
  }
};

const updateDevice = async () => {
  const index = devices.value.findIndex(d => d.id === form.id);
  if (index !== -1) {
    devices.value[index] = { ...form };
    ElMessage.success('设备更新成功');
    dialogFormVisible.value = false;
    resetForm();
  } else {
    ElMessage.error('设备序列号不能为空或重复');
  }
};

const confirmDeleteDevice = (device) => {
  deletingDeviceId.value = device.id;
  deletingDeviceName.value = device.name;
  deleteDeviceDialogVisible.value = true;
};

const deleteDevice = (deviceId) => {
  devices.value = devices.value.filter(d => d.id !== deviceId);
  ElMessage.success('设备删除成功');
  deleteDeviceDialogVisible.value = false;
};

const viewDeviceProducts = (device) => {
  currentDeviceProducts.value = exampleProducts.filter(p => p.id % device.id === 0);
  viewingProducts.value = true;
};

const addProductToDevice = () => {
  if (newProduct.name) {
    currentDeviceProducts.value.push({ id: Date.now(), name: newProduct.name });
    ElMessage.success('产品添加成功');
    newProduct.name = '';
  } else {
    ElMessage.error('产品名称不能为空');
  }
};

const confirmDeleteProduct = (product) => {
  deletingProductId.value = product.id;
  deletingProductName.value = product.name;
  deleteProductDialogVisible.value = true;
};

const deleteProduct = (productId) => {
  currentDeviceProducts.value = currentDeviceProducts.value.filter(p => p.id !== productId);
  ElMessage.success('产品删除成功');
  deleteProductDialogVisible.value = false;
};

const resetForm = () => {
  form.id = null;
  form.name = '';
  form.serialNumber = '';
  form.capacity = '';
};

const handlePageChange = (page) => {
  currentPage.value = page;
};

const filteredDevices = computed(() => {
  return devices.value.filter(device => device.name.includes(searchQuery.value) || device.serialNumber.includes(searchQuery.value));
});

const paginatedDevices = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return filteredDevices.value.slice(start, end);
});

onMounted(fetchDevices);
</script>

<style scoped lang="scss">
.device-management {
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
