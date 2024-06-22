<script setup>
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
} from '@element-plus/icons-vue'
import avatar from '@/assets/default.png'
</script>

<template>
  <el-container class="layout-container">
    <!-- 左侧菜单 -->
    <el-aside width="300px">
      <div class="el-aside__logo"></div>
      <el-menu active-text-color="#ffd04b" background-color="#232323"  text-color="#fff"
               router>

        <el-menu-item index="/overview">
          <el-icon>
            <Tools />
          </el-icon>

          <span>工厂概览</span>
        </el-menu-item>
        <el-menu-item index="/employee">
          <el-icon><Avatar /></el-icon>
          <span>员工管理</span>
        </el-menu-item>
        <el-menu-item index="/production">
          <el-icon>
            <Management />
          </el-icon>
          <span>产品管理</span>
        </el-menu-item>

        <el-menu-item index="/device">
          <el-icon><Cpu /></el-icon>
          <span>设备管理</span>
        </el-menu-item>

        <el-menu-item index="/order">
          <el-icon><List /></el-icon>
          <span>订单管理</span>
        </el-menu-item>


        <el-menu-item index="/plan">
          <el-icon><Calendar /></el-icon>
          <span>生产计划管理</span>
        </el-menu-item>

        <el-menu-item index="/schedum">
          <el-icon><ScaleToOriginal /></el-icon>
          <span>生产调度管理</span>
        </el-menu-item>

        <el-menu-item index="/track">
          <el-icon>
            <Promotion />
          </el-icon>
          <span>生产跟踪</span>
        </el-menu-item>



      </el-menu>
    </el-aside>
    <!-- 右侧主区域 -->
    <el-container>
      <!-- 头部区域 -->
      <el-header>
        <div>东软智能制造云平台用户：<strong></strong></div>
        <el-dropdown placement="bottom-end">
                    <span class="el-dropdown__box">
                        <el-avatar :src="avatar" />
                        <el-icon>
                            <CaretBottom />
                        </el-icon>
                    </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="profile" :icon="User">基本资料</el-dropdown-item>
              <el-dropdown-item command="avatar" :icon="Crop">更换头像</el-dropdown-item>
              <el-dropdown-item command="password" :icon="EditPen">重置密码</el-dropdown-item>
              <el-dropdown-item command="logout" :icon="SwitchButton">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-header>
      <!-- 中间区域 -->
      <el-main>
        <div style="width: 1300px; height: 600px;">
          <router-view></router-view>
        </div>
      </el-main>
      <!-- 底部区域 -->
      <el-footer>东软智能制造云平台2024实训小组coco蜜雪</el-footer>
    </el-container>
  </el-container>
</template>

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

    .el-dropdown__box {
      display: flex;
      align-items: center;

      .el-icon {
        color: #999;
        margin-left: 10px;
      }

      &:active,
      &:focus {
        outline: none;
      }
    }
  }

  .el-footer {
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 14px;
    color: #666;
  }
}
</style>