<script setup>
</script>

<template>
  <el-container style="min-height: 100vh;">
    <el-aside width="200px" style="background: #001529;">
      <div style="height: 60px; line-height: 60px; text-align: center; color: #fff; font-size: 18px; font-weight: bold; border-bottom: 1px solid rgba(255,255,255,0.1);">
        <el-icon style="vertical-align: middle;"><Monitor /></el-icon> 后台管理
      </div>
      <el-menu
        :default-active="$route.path"
        router
        background-color="#001529"
        text-color="#rgba(255,255,255,0.65)"
        active-text-color="#409eff"
        style="border: none;"
      >
        <el-menu-item index="/">
          <el-icon><DataAnalysis /></el-icon> 服装管理
        </el-menu-item>
        <el-menu-item index="/orders">
          <el-icon><List /></el-icon> 订单管理
        </el-menu-item>
        <el-menu-item index="/users">
          <el-icon><UserFilled /></el-icon> 用户管理
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-main style="padding: 20px;">
      <router-view />
    </el-main>
  </el-container>
</template>

<script>
export default {}
</script>
