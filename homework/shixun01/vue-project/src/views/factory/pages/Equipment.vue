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
        <el-button plain @click="openLinkProductDialog">设备关联产品</el-button>
      </div>
      <div class="list-section">
        <el-table :data="paginatedDevices" style="width: 100%">
          <el-table-column prop="equipment_name" label="设备名称"></el-table-column>
          <el-table-column prop="serial_number" label="设备序列号"></el-table-column>
          <el-table-column prop="factoryId" label="工厂ID"></el-table-column>
          <el-table-column prop="equipment_states" label="设备状态"></el-table-column>
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
          <el-input v-model="form.equipment_name"></el-input>
        </el-form-item>
        <el-form-item label="设备序列号">
          <el-input v-model="form.serial_number"></el-input>
        </el-form-item>
        <el-form-item label="工厂ID">
          <el-input v-model="form.factoryId"></el-input>
        </el-form-item>
        <el-form-item label="设备状态">
          <el-input v-model="form.equipment_states"></el-input>
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

    <!-- 设备关联产品弹窗 -->
    <el-dialog v-model="linkProductDialogVisible" title="设备关联产品" width="500">
      <el-form :model="linkProductForm" label-width="120px">
        <el-form-item label="设备ID">
          <el-input v-model="linkProductForm.equipmentId"></el-input>
        </el-form-item>
        <el-form-item label="产品ID">
          <el-input v-model="linkProductForm.productId"></el-input>
        </el-form-item>
        <el-form-item label="产量">
          <el-input v-model="linkProductForm.yield"></el-input>
        </el-form-item>
        <el-form-item label="是否生产">
          <el-select v-model="linkProductForm.canproduce">
            <el-option label="是" value="1"></el-option>
            <el-option label="否" value="2"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="linkProductDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="linkProductToDevice">确认</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>
<script setup>

import {ref, reactive, computed, onMounted} from 'vue';
import {ElMessage, ElMessageBox} from 'element-plus';
import {
  fetchDevicesFromServer,
  addDeviceToServer,
  updateDeviceOnServer,
  deleteDeviceFromServer,
  fetchDeviceProductsFromServer,
  addProductToDeviceOnServer,
  deleteProductFromDeviceOnServer,
  linkProductToDeviceOnServer,
} from '@/api/factory/equipment.js';

const searchQuery = ref('');
const dialogFormVisible = ref(false);
const isEditing = ref(false);
const viewingProducts = ref(false);
const deleteDeviceDialogVisible = ref(false);
const deleteProductDialogVisible = ref(false);
const linkProductDialogVisible = ref(false);
const form = reactive({id: null, equipment_name: '', serial_number: '', factoryId: '', equipment_states: ''});
const devices = ref([]);
const currentDeviceProducts = ref([]);
const deletingDeviceId = ref(null);
const deletingDeviceName = ref('');
const deletingProductId = ref(null);
const deletingProductName = ref('');
const pageSize = ref(10);
const currentPage = ref(1);

const newProduct = reactive({name: ''});
const linkProductForm = reactive({equipmentId: '', productId: '', canproduce: '1',yield:'1'});

const fetchDevices = async () => {
  try {
    const response = await fetchDevicesFromServer();
    if (response.code === 200 && Array.isArray(response.data)) {
      devices.value = response.data;
    } else {
      throw new Error('API response is not an array');
    }
  } catch (error) {
    ElMessage.error('获取设备列表失败');
  }
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

const openLinkProductDialog = () => {
  resetLinkProductForm();
  linkProductDialogVisible.value = true;
};

const addDevice = async () => {
  if (form.serial_number && !devices.value.some(d => d.serialNumber === form.serial_number)) {
    try {
      const response = await addDeviceToServer({
        equipmentName: form.equipment_name,
        factoryId: form.factoryId,
        equipmentStates: form.equipment_states,
        serialNumber: form.serial_number,
      });
      if (response.code === 200) {
        ElMessage.success('设备添加成功');
        dialogFormVisible.value = false;
        resetForm();
        fetchDevices();
      } else {
        throw new Error(response.message || '添加设备失败');
      }
    } catch (error) {
      ElMessage.error(error.message);
    }
  } else {
    ElMessage.error('设备序列号不能为空或重复');
  }
};

const updateDevice = async () => {
  try {
    const response = await updateDeviceOnServer({
      equipmentId: form.equipment_id,
      equipmentName: form.equipment_name,
      factoryId: form.factoryId,
      equipmentStates: form.equipment_states,
      serialNumber: form.serial_number,
    });
    if (response.code === 200) {
      ElMessage.success('设备更新成功');
      dialogFormVisible.value = false;
      resetForm();
      fetchDevices();
    } else {
      throw new Error(response.message || '更新设备失败');
    }
  } catch (error) {
    ElMessage.error(error.message);
  }
};

const confirmDeleteDevice = (device) => {
  deletingDeviceId.value = device.equipment_id;
  deletingDeviceName.value = device.equipment_name;
  deleteDeviceDialogVisible.value = true;
};

const deleteDevice = async (deviceId) => {
  try {
    const response = await deleteDeviceFromServer(deviceId);
    if (response.code === 200) {
      ElMessage.success('设备删除成功');
      deleteDeviceDialogVisible.value = false;
      fetchDevices();
    } else {
      throw new Error(response.message || '删除设备失败');
    }
  } catch (error) {
    ElMessage.error(error.message);
  }
};

const viewDeviceProducts = async (device) => {
  try {
    const response = await fetchDeviceProductsFromServer(device.id);
    if (response.code === 200) {
      currentDeviceProducts.value = response.data;
      viewingProducts.value = true;
    } else {
      throw new Error(response.message || '获取设备产品失败');
    }
  } catch (error) {
    ElMessage.error(error.message);
  }
};

const addProductToDevice = async () => {
  if (newProduct.name) {
    try {
      const response = await addProductToDeviceOnServer({deviceId: deletingDeviceId.value, ...newProduct});
      if (response.code === 200) {
        ElMessage.success('产品添加成功');
        newProduct.name = '';
        viewDeviceProducts({id: deletingDeviceId.value});
      } else {
        throw new Error(response.data.message || '添加产品失败');
      }
    } catch (error) {
      ElMessage.error(error.message);
    }
  } else {
    ElMessage.error('产品名称不能为空');
  }
};

const linkProductToDevice = async () => {
  try {
    const response = await linkProductToDeviceOnServer({
      equipmentId: linkProductForm.equipmentId,
      productId: linkProductForm.productId,
      canproduce: linkProductForm.canproduce,
      yield: linkProductForm.yield,
    });
    if (response.code === 200) {
      ElMessage.success('产品关联成功');
      linkProductDialogVisible.value = false;
      resetLinkProductForm();
    } else {
      throw new Error(response.message || '关联产品失败');
    }
  } catch (error) {
    ElMessage.error(error.message);
  }
};

const confirmDeleteProduct = (product) => {
  deletingProductId.value = product.id;
  deletingProductName.value = product.name;
  deleteProductDialogVisible.value = true;
};

const deleteProduct = async (productId) => {
  try {
    const response = await deleteProductFromDeviceOnServer(productId);
    if (response.code === 200) {
      ElMessage.success('产品删除成功');
      deleteProductDialogVisible.value = false;
      viewDeviceProducts({id: deletingDeviceId.value});
    } else {
      throw new Error(response.data.message || '删除产品失败');
    }
  } catch (error) {
    ElMessage.error(error.message);
  }
};

const resetForm = () => {
  form.equipment_id = null;
  form.equipment_name = '';
  form.serial_number = '';
  form.factoryId = '';
  form.equipment_states = '';
};

const resetLinkProductForm = () => {
  linkProductForm.equipmentId = '';
  linkProductForm.productId = '';
  linkProductForm.canproduce = '1';
};

const handlePageChange = (page) => {
  currentPage.value = page;
};

const filteredDevices = computed(() => {
  return devices.value.filter(device => device.equipment_name.includes(searchQuery.value) || device.serial_number.includes(searchQuery.value));
});

const paginatedDevices = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return filteredDevices.value.slice(start, end);
});

onMounted(fetchDevices);

</script>

<style  scoped lang="scss">
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