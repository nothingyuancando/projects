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
          <el-form-item label="可生产产品">
            <el-input v-model="newDevice.product"></el-input>
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
          <el-table-column prop="product" label="可生产产品"></el-table-column>
          <el-table-column prop="capacity" label="产能"></el-table-column>
          <el-table-column label="操作">
            <template #default="scope">
              <el-button @click="editDevice(scope.row)" size="mini">编辑</el-button>
              <el-button @click="deleteDevice(scope.row.id)" size="mini" type="danger">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>
    <el-dialog :visible.sync="isEditing" title="编辑设备">
      <el-form :model="editedDevice" ref="editDeviceForm" label-width="120px">
        <el-form-item label="设备名称">
          <el-input v-model="editedDevice.name"></el-input>
        </el-form-item>
        <el-form-item label="设备序列号">
          <el-input v-model="editedDevice.serialNumber"></el-input>
        </el-form-item>
        <el-form-item label="可生产产品">
          <el-input v-model="editedDevice.product"></el-input>
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
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { ElMessageBox, ElMessage } from 'element-plus';

const searchQuery = ref('');
const newDevice = ref({ name: '', serialNumber: '', product: '', capacity: '' });
const devices = ref([]);
const isEditing = ref(false);
const editedDevice = ref(null);

const fetchDevices = async () => {
  const deviceData = await fetch('/api/devices').then(res => res.json());
  devices.value = deviceData;
};

const searchDevices = () => {
  fetchDevices();
};

const addDevice = async () => {
  if (newDevice.value.serialNumber && !devices.value.some(d => d.serialNumber === newDevice.value.serialNumber)) {
    await fetch('/api/devices', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(newDevice.value)
    });
    newDevice.value = { name: '', serialNumber: '', product: '', capacity: '' };
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
    await fetch(`/api/devices/${editedDevice.value.id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(editedDevice.value)
    });
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

const deleteDevice = async (id) => {
  const hasOrders = await fetch(`/api/orders?deviceId=${id}`).then(res => res.json()).then(data => data.length > 0);
  if (!hasOrders) {
    ElMessageBox.confirm('此操作将永久删除该设备, 是否继续?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }).then(async () => {
      await fetch(`/api/devices/${id}`, { method: 'DELETE' });
      fetchDevices();
    }).catch(() => {
      ElMessage.info('已取消删除');
    });
  } else {
    ElMessage.error('该设备已关联启动工单，不能删除');
  }
};

const filteredDevices = computed(() => {
  return devices.value.filter(device => device.name.includes(searchQuery.value) || device.product.includes(searchQuery.value));
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
