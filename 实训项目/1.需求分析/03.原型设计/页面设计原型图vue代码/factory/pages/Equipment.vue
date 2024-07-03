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
      <div class="actions-section">
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
      <div class="product-add-section">
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
import {
  fetchDevicesFromServer,
  addDeviceToServer,
  updateDeviceOnServer,
  deleteDeviceFromServer,
  fetchDeviceProductsFromServer,
  addProductToDeviceOnServer,
  deleteProductFromDeviceOnServer,
} from '@/api/equipment.js';

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

const fetchDevices = async () => {
  // 示例数据，实际应从API获取
  devices.value = [
    { id: 1, name: '设备A', serialNumber: '123456', capacity: '100' },
    { id: 2, name: '设备B', serialNumber: '789012', capacity: '200' }
  ];
};

const searchDevices = async () => {
  await fetchDevices();
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
    try {
      // 模拟添加设备，实际应调用API
      devices.value.push({ ...form, id: devices.value.length + 1 });
      ElMessage.success('设备添加成功');
      dialogFormVisible.value = false;
      resetForm();
    } catch (error) {
      ElMessage.error('设备添加失败');
    }
  } else {
    ElMessage.error('设备序列号不能为空或重复');
  }
};

const updateDevice = async () => {
  try {
    // 模拟更新设备，实际应调用API
    const index = devices.value.findIndex(d => d.id === form.id);
    if (index !== -1) {
      devices.value[index] = { ...form };
    }
    ElMessage.success('设备更新成功');
    dialogFormVisible.value = false;
    resetForm();
  } catch (error) {
    ElMessage.error('设备更新失败');
  }
};

const confirmDeleteDevice = (device) => {
  deletingDeviceId.value = device.id;
  deletingDeviceName.value = device.name;
  deleteDeviceDialogVisible.value = true;
};

const deleteDevice = async (deviceId) => {
  try {
    // 模拟删除设备，实际应调用API
    const index = devices.value.findIndex(d => d.id === deviceId);
    if (index !== -1) {
      devices.value.splice(index, 1);
    }
    ElMessage.success('设备删除成功');
    deleteDeviceDialogVisible.value = false;
  } catch (error) {
    ElMessage.error('设备删除失败');
  }
};

const viewDeviceProducts = async (device) => {
  try {
    // 示例数据，实际应从API获取
    currentDeviceProducts.value = [
      { id: 1, name: '产品A' },
      { id: 2, name: '产品B' }
    ];
    viewingProducts.value = true;
  } catch (error) {
    ElMessage.error('获取设备产品失败');
  }
};

const addProductToDevice = async () => {
  if (newProduct.name) {
    try {
      // 模拟添加产品到设备，实际应调用API
      currentDeviceProducts.value.push({ id: currentDeviceProducts.value.length + 1, name: newProduct.name });
      ElMessage.success('产品添加成功');
      newProduct.name = '';
    } catch (error) {
      ElMessage.error('产品添加失败');
    }
  } else {
    ElMessage.error('产品名称不能为空');
  }
};

const confirmDeleteProduct = (product) => {
  deletingProductId.value = product.id;
  deletingProductName.value = product.name;
  deleteProductDialogVisible.value = true;
};

const deleteProduct = async (productId) => {
  try {
    // 模拟删除产品，实际应调用API
    const index = currentDeviceProducts.value.findIndex(p => p.id === productId);
    if (index !== -1) {
      currentDeviceProducts.value.splice(index, 1);
    }
    ElMessage.success('产品删除成功');
    deleteProductDialogVisible.value = false;
  } catch (error) {
    ElMessage.error('产品删除失败');
  }
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
