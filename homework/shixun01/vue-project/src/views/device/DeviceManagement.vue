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
      <div class="add-section">
        <el-form :model="newDevice" ref="newDeviceForm" label-width="120px">
          <el-form-item label="设备名称">
            <el-input v-model="newDevice.name"></el-input>
          </el-form-item>
          <el-form-item label="设备序列号">
            <el-input v-model="newDevice.serialNumber"></el-input>
          </el-form-item>
          <el-form-item label="产能">
            <el-input v-model="newDevice.capacity"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="addDevice">新增</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div class="list-section">
        <el-table :data="filteredDevices" style="width: 100%">
          <el-table-column prop="name" label="设备名称"></el-table-column>
          <el-table-column prop="serialNumber" label="设备序列号"></el-table-column>
          <el-table-column prop="capacity" label="产能"></el-table-column>
          <el-table-column label="操作">
            <template #default="scope">
              <el-button @click="editDevice(scope.row)" size="mini">编辑</el-button>
              <el-button @click="viewDeviceProducts(scope.row)" size="mini">查看产品</el-button>
              <el-button @click="confirmDeleteDevice(scope.row.id)" size="mini" type="danger">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>

    <!-- 编辑设备弹窗 -->
    <el-dialog :visible.sync="isEditing" title="编辑设备">
      <el-form :model="editedDevice" ref="editDeviceForm" label-width="120px">
        <el-form-item label="设备名称">
          <el-input v-model="editedDevice.name"></el-input>
        </el-form-item>
        <el-form-item label="设备序列号">
          <el-input v-model="editedDevice.serialNumber"></el-input>
        </el-form-item>
        <el-form-item label="产能">
          <el-input v-model="editedDevice.capacity"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelEdit">取消</el-button>
        <el-button type="primary" @click="updateDevice">更新</el-button>
      </div>
    </el-dialog>

    <!-- 查看设备关联产品弹窗 -->
    <el-dialog :visible.sync="viewingProducts" title="设备关联产品">
      <el-table :data="currentDeviceProducts" style="width: 100%; margin-top: 20px;">
        <el-table-column prop="name" label="产品名称"></el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button @click="confirmDeleteProduct(scope.row.id)" size="mini" type="danger">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeProductDialog">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 删除设备确认弹窗 -->
    <el-dialog :visible.sync="deleteDeviceDialogVisible" title="确认删除设备">
      <p>确定要删除设备 "{{ deletingDeviceName }}" 吗？</p>
      <div slot="footer" class="dialog-footer">
        <el-button @click="deleteDevice(deletingDeviceId)">确认删除</el-button>
        <el-button @click="cancelDeleteDevice">取消</el-button>
      </div>
    </el-dialog>

    <!-- 删除产品确认弹窗 -->
    <el-dialog :visible.sync="deleteProductDialogVisible" title="确认删除产品">
      <p>确定要删除产品 "{{ deletingProductName }}" 吗？</p>
      <div slot="footer" class="dialog-footer">
        <el-button @click="deleteProduct(deletingProductId)">确认删除</el-button>
        <el-button @click="cancelDeleteProduct">取消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue';
import { ElMessageBox, ElMessage } from 'element-plus';

const searchQuery = ref('');
const newDevice = ref({ name: '', serialNumber: '', capacity: '' });
const devices = ref([]);
const isEditing = ref(false);
const editedDevice = ref(null);

const viewingProducts = ref(false);
const currentDeviceProducts = ref([]);
const currentDeviceId = ref(null);
const newProduct = reactive({ name: '' });

// 示例数据
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
  // 模拟API数据获取
  devices.value = exampleDevices;
};

const searchDevices = () => {
  fetchDevices();
};

const addDevice = async () => {
  if (newDevice.value.serialNumber && !devices.value.some(d => d.serialNumber === newDevice.value.serialNumber)) {
    // 模拟API新增设备
    devices.value.push({ ...newDevice.value, id: devices.value.length + 1 });
    newDevice.value = { name: '', serialNumber: '', capacity: '' };
    fetchDevices();
  } else {
    ElMessage.error('设备序列号不能为空或重复');
  }
};

const editDevice = (device) => {
  isEditing.value = true;
  editedDevice.value = { ...device };
};

const updateDevice = async () => {
  if (editedDevice.value.serialNumber && !devices.value.some(d => d.serialNumber === editedDevice.value.serialNumber && d.id !== editedDevice.value.id)) {
    // 模拟API更新设备
    const index = devices.value.findIndex(d => d.id === editedDevice.value.id);
    if (index !== -1) {
      devices.value[index] = editedDevice.value;
    }
    cancelEdit();
    fetchDevices();
  } else {
    ElMessage.error('设备序列号不能为空或重复');
  }
};

const cancelEdit = () => {
  isEditing.value = false;
  editedDevice.value = null;
};

const confirmDeleteDevice = (deviceId) => {
  const device = devices.value.find(d => d.id === deviceId);
  if (device) {
    deletingDeviceId.value = deviceId;
    deletingDeviceName.value = device.name;
    deleteDeviceDialogVisible.value = true;
  }
};

const deleteDevice = (deviceId) => {
  devices.value = devices.value.filter(d => d.id !== deviceId);
  deleteDeviceDialogVisible.value = false;
  ElMessage.success('设备删除成功');
};

const cancelDeleteDevice = () => {
  deleteDeviceDialogVisible.value = false;
};

const viewDeviceProducts = async (device) => {
  currentDeviceId.value = device.id;
  // 模拟API获取设备关联产品
  currentDeviceProducts.value = exampleProducts.filter(p => p.id % device.id === 0);
  viewingProducts.value = true;
};

const confirmDeleteProduct = (productId) => {
  const product = currentDeviceProducts.value.find(p => p.id === productId);
  if (product) {
    deletingProductId.value = productId;
    deletingProductName.value = product.name;
    deleteProductDialogVisible.value = true;
  }
};

const deleteProduct = (productId) => {
  currentDeviceProducts.value = currentDeviceProducts.value.filter(p => p.id !== productId);
  deleteProductDialogVisible.value = false;
  ElMessage.success('产品删除成功');
};

const cancelDeleteProduct = () => {
  deleteProductDialogVisible.value = false;
};

const closeProductDialog = () => {
  viewingProducts.value = false;
  currentDeviceProducts.value = [];
  currentDeviceId.value = null;
};

const filteredDevices = computed(() => {
  return devices.value.filter(device => device.name.includes(searchQuery.value) || device.serialNumber.includes(searchQuery.value));
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
