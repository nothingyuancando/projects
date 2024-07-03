<template>
  <el-container class="layout-container">
    <!-- 左侧菜单 -->
    <el-aside width="300px">
      <div class="el-aside__logo"></div>
      <el-menu active-text-color="#ffd04b" background-color="#232323" text-color="#fff" router>
        <el-menu-item index="/factory/factoryview">
          <el-icon><Tools /></el-icon>
          <span>工厂概览</span>
        </el-menu-item>

        <el-menu-item index="/factory/product">
          <el-icon><Management /></el-icon>
          <span>产品管理</span>
        </el-menu-item>
        <el-menu-item index="/factory/equipment">
          <el-icon><Cpu /></el-icon>
          <span>设备管理</span>
        </el-menu-item>

        <el-menu-item index="/factory/workorder">
          <el-icon><ScaleToOriginal /></el-icon>
          <span>工单管理</span>
        </el-menu-item>
        <el-menu-item index="/factory/workreport">
          <el-icon><Promotion /></el-icon>
          <span>报工</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <!-- 右侧主区域 -->
    <el-container>
      <!-- 头部区域 -->
      <el-header>
        <div class="header-left">东软智能制造云平台用户：<strong>{{ username }}</strong></div>
        <div class="header-right">
          <el-button type="primary" @click="goToProfile">个人中心</el-button>
          <el-button type="danger" @click="logout">退出登录</el-button>
        </div>
      </el-header>
      <!-- 中间区域 -->
      <el-main>
        <div class="main-content">
          <router-view></router-view>
        </div>
      </el-main>
      <!-- 底部区域 -->
      <el-footer>东软智能制造云平台2024实训小组coco蜜雪</el-footer>
    </el-container>
  </el-container>
</template>


<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import {
  Management,
  Promotion,
  UserFilled,
  User,
  Crop,
  EditPen,
  SwitchButton,
  CaretBottom,
  Tools,
  List,
  Cpu,
  Calendar,
  ScaleToOriginal,
  Avatar,
} from '@element-plus/icons-vue';

const username = ref('john_doe');  // 示例用户名，应从实际用户数据中获取
const router = useRouter();

const goToProfile = () => {
  router.push('/user');
};

const logout = async () => {
  await fetch('/api/logout', { method: 'POST' });
  ElMessage.success('已成功退出登录');
  router.push('/login');
};
</script>

<style lang="scss" scoped>
.layout-container {
  height: 100vh;

  .el-aside {
    background-color: #232323;

    &__logo {
      height: 120px;
      background: url('@/assets/logo2.png') no-repeat center / 120px auto;
    }

    .el-menu {
      border-right: none;
    }
  }

  .el-header {
    background-color: #fff;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 20px;

    .header-left {
      font-size: 16px;
      font-weight: bold;
    }

    .header-right {
      display: flex;
      gap: 10px;
    }

    .el-button {
      margin-left: 10px;
    }
  }

  .el-main {
    padding: 20px;
    background-color: #f5f5f5;
  }

  .el-footer {
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 14px;
    color: #666;
  }

  .main-content {
    width: 1350px;
    height: 750px;
  }
}
</style>


