<template>
  <div class="user-profile-management">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <h2>用户信息</h2>
      </div>
      <div class="profile-section">
        <el-form :model="userProfile" label-width="120px">
          <el-form-item label="用户名">
            <el-input v-model="userProfile.username" disabled></el-input>
          </el-form-item>

        </el-form>
      </div>
      <div class="password-reset-section">
        <el-form :model="resetPassword" ref="resetPasswordForm" label-width="120px">
          <el-form-item label="旧密码" :required="true">
            <el-input v-model="resetPassword.oldPassword" type="password"></el-input>
          </el-form-item>
          <el-form-item label="新密码" :required="true">
            <el-input v-model="resetPassword.newPassword" type="password"></el-input>
          </el-form-item>
          <el-form-item label="确认新密码" :required="true">
            <el-input v-model="resetPassword.confirmNewPassword" type="password"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="confirmResetPassword">重置密码</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
    <el-dialog :visible.sync="isConfirmingReset" title="确认重置密码">
      <span>确定要重置密码吗？</span>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelReset">取消</el-button>
        <el-button type="primary" @click="resetPasswordSubmit">重置</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';

const userProfile = ref({
  username: 'john_doe',
  email: 'john@example.com',
  phoneNumber: '123-456-7890'
});

const resetPassword = ref({
  oldPassword: '',
  newPassword: '',
  confirmNewPassword: ''
});

const isConfirmingReset = ref(false);

const fetchUserProfile = async () => {
  // 示例数据，实际应从API获取
  const userData = {
    username: 'john_doe',
    email: 'john@example.com',
    phoneNumber: '123-456-7890'
  };
  userProfile.value = userData;
};

const confirmResetPassword = () => {
  if (!resetPassword.value.oldPassword || !resetPassword.value.newPassword || !resetPassword.value.confirmNewPassword) {
    ElMessage.error('请填写所有必填项');
    return;
  }
  if (resetPassword.value.newPassword !== resetPassword.value.confirmNewPassword) {
    ElMessage.error('新密码和确认新密码不一致');
    return;
  }
  isConfirmingReset.value = true;
};

const resetPasswordSubmit = async () => {
  // 发送重置密码请求
  await fetch('/api/user/reset-password', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ ...resetPassword.value })
  });

  // 重置表单
  resetPassword.value.oldPassword = '';
  resetPassword.value.newPassword = '';
  resetPassword.value.confirmNewPassword = '';

  ElMessage.success('密码已成功重置');
  isConfirmingReset.value = false;
};

const cancelReset = () => {
  isConfirmingReset.value = false;
};

onMounted(() => {
  fetchUserProfile();
});
</script>
<style scoped lang="scss">
.user-profile-management {
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

.profile-section,
.password-reset-section {
  margin-bottom: 20px;
}

.el-input {
  width: 300px;
}

.dialog-footer {
  text-align: right;
}
</style>
