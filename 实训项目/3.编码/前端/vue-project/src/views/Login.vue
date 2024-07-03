<script setup>
import { User, Lock, HomeFilled } from '@element-plus/icons-vue';
import { ref } from 'vue';
import { ElMessage } from 'element-plus';
import { useTokenStore } from '@/stores/token.js';
import { userRegisterServer, userLoginServer } from '@/api/user.js';
import { useRouter } from 'vue-router';

// 控制注册与登录表单的显示， 默认显示注册
const isRegister = ref(false);

const registerData = ref({
  username: '',
  password: '',
  role: 'factory', // 默认注册为工厂
});

const loginData = ref({
  username: '',
  password: '',
  role: 'factory', // 默认登录为客户
});

const tokenStore = useTokenStore();
const router = useRouter();

const redirectToRolePage = (role) => {
  if (role === 'factory') {
    router.push('/factory');
  } else if (role === 'customer') {
    router.push('/customer');
  } else if (role === 'platform') {
    router.push('/platform');
  }
};

const register = async () => {
  try {
    const result = await userRegisterServer(registerData.value);
    ElMessage.success(result.msg ? result.msg : '注册成功');
    tokenStore.setToken(result.data.token);
    redirectToRolePage(registerData.value.role);
  } catch (error) {
    ElMessage.error('注册失败');
  }
};

const login = async () => {
  try {
    const result = await userLoginServer(loginData.value);
    ElMessage.success(result.msg ? result.msg : '登录成功');
    tokenStore.setToken(result.data.token);
    redirectToRolePage(loginData.value.role);
  } catch (error) {
    ElMessage.error('登录失败');
  }
};

const clearRegisterData = () => {
  registerData.value = {
    username: '',
    password: '',
    role: 'factory',
  };

  loginData.value = {
    username: '',
    password: '',
    role: 'factory',
  };
};

const rules = {
  username: [
    {required: true, message: '请输入用户名', trigger: 'blur'},
    {min: 2, max: 16, message: '长度不符合要求', trigger: 'blur'},
  ],
  password: [
    {required: true, message: '请输入密码', trigger: 'blur'},
    {min: 2, max: 16, message: '长度不符合要求', trigger: 'blur'},
  ],
};
</script>

<template>
  <el-row class="login-page">
    <el-col :span="12" class="bg"></el-col>
    <el-col :span="6" :offset="3" class="form">
      <!-- 注册表单 -->
      <el-form
          ref="form"
          size="large"
          autocomplete="off"
          v-if="isRegister"
          :model="registerData"
          :rules="rules"
      >
        <el-form-item>
          <h1>注册</h1>
        </el-form-item>
        <el-form-item prop="username">
          <el-input
              :prefix-icon="User"
              placeholder="请输入用户名"
              v-model="registerData.username"
          ></el-input>
        </el-form-item>

        <el-form-item prop="password">
          <el-input
              :prefix-icon="Lock"
              type="password"
              placeholder="请输入密码"
              v-model="registerData.password"
          ></el-input>
        </el-form-item>
        <el-form-item label="选择身份" prop="role">
          <el-select v-model="registerData.role" placeholder="请选择身份">
            <el-option label="工厂" value="factory"></el-option>
            <el-option label="客户" value="customer"></el-option>
            <el-option label="平台人员" value="platform"></el-option>
          </el-select>
        </el-form-item>
        <!-- 注册按钮 -->
        <el-form-item>
          <el-button class="button" type="primary" auto-insert-space @click="register">
            注册
          </el-button>
        </el-form-item>
        <el-form-item class="flex">
          <el-link type="info" :underline="false" @click="isRegister = false; clearRegisterData()">
            ← 返回
          </el-link>
        </el-form-item>
      </el-form>
      <!-- 登录表单 -->
      <el-form ref="form" size="large" autocomplete="off" v-else :model="loginData">
        <el-form-item>
          <h1>登录</h1>
        </el-form-item>
        <el-form-item prop="username">
          <el-input :prefix-icon="User" placeholder="请输入用户名" v-model="loginData.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
              name="password"
              :prefix-icon="Lock"
              type="password"
              placeholder="请输入密码"
              v-model="loginData.password"
          ></el-input>
        </el-form-item>
        <el-form-item label="选择身份" prop="role">
          <el-select v-model="loginData.role" placeholder="请选择身份">
            <el-option label="工厂" value="factory"></el-option>
            <el-option label="客户" value="customer"></el-option>
            <el-option label="平台管理员" value="platform"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item class="flex">
          <div class="flex">
            <el-checkbox>记住我</el-checkbox>
            <el-link type="primary" :underline="false">忘记密码？</el-link>
          </div>
        </el-form-item>
        <!-- 登录按钮 -->
        <el-form-item>
          <el-button class="button" type="primary" auto-insert-space @click="login">登录</el-button>
        </el-form-item>
        <el-form-item class="flex">
          <el-link type="info" :underline="false" @click="isRegister = true; clearRegisterData()">
            没有账号？点击注册 →
          </el-link>
        </el-form-item>
      </el-form>
    </el-col>
  </el-row>
</template>

<style lang="scss" scoped>
/* 样式 */
.login-page {
  height: 100vh;
  background-color: #fff;

  .bg {
    background: url('@/assets/logo2.png') no-repeat 60% center / 500px auto,
    url('@/assets/login_bg.jpg') no-repeat center / cover;
    border-radius: 0 20px 20px 0;
  }

  .form {
    display: flex;
    flex-direction: column;
    justify-content: center;
    user-select: none;

    .title {
      margin: 0 auto;
    }

    .button {
      width: 100%;
    }

    .flex {
      width: 100%;
      display: flex;
      justify-content: space-between;
    }
  }
}
</style>
