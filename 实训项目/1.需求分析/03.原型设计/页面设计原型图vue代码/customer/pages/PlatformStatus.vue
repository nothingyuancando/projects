<template>
  <div class="platform-status">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <h2>平台情况</h2>
      </div>
      <el-table :data="platformStatus" style="width: 100%">
        <el-table-column prop="parameter" label="参数"></el-table-column>
        <el-table-column prop="value" label="值"></el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { fetchPlatformStatusFromServer } from '@/api/customer';

const platformStatus = ref([]);

const fetchPlatformStatus = async () => {
  try {
    const response = await fetchPlatformStatusFromServer();
    platformStatus.value = response.data;
  } catch (error) {
    ElMessage.error('获取平台情况失败');
  }
};

onMounted(fetchPlatformStatus);
</script>

<style scoped>
.platform-status {
  padding: 20px;
}
</style>
