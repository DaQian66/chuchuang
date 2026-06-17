<script setup>
</script>

<template>
  <el-container>
    <el-header style="height: 60px; background: #2c3e50; display: flex; align-items: center; justify-content: space-between; padding: 0 20px;">
      <div style="display: flex; align-items: center; gap: 10px;">
        <el-icon style="font-size: 28px; color: #fff;"><ShoppingBag /></el-icon>
        <span style="font-size: 20px; color: #fff; font-weight: bold;">网上衣橱</span>
      </div>
      <div style="display: flex; align-items: center; gap: 15px;">
        <el-link type="info" @click="$router.push('/')" style="color: #ddd; font-size: 14px;">首页</el-link>
        <template v-if="token">
          <el-link type="primary" @click="$router.push('/cart')" style="color: #ddd; font-size: 14px;">
            <el-icon><CartFilled /></el-icon> 购物车
          </el-link>
          <el-link type="success" @click="$router.push('/orders')" style="color: #ddd; font-size: 14px;">
            <el-icon><List /></el-icon> 我的订单
          </el-link>
          <el-dropdown>
            <span style="color: #ddd; font-size: 14px; cursor: pointer;">
              <el-icon><User /></el-icon> {{ userName }}
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="$router.push('/profile')">个人中心</el-dropdown-item>
                <el-dropdown-item @click="logout" style="color: #f56c6c;">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
        <template v-else>
          <el-button type="primary" size="small" @click="$router.push('/login')">登录</el-button>
          <el-button size="small" @click="$router.push('/register')">注册</el-button>
        </template>
      </div>
    </el-header>
    <el-main style="min-height: 600px; padding: 20px;">
      <router-view />
    </el-main>
    <el-footer style="text-align: center; color: #999; font-size: 12px; padding: 20px;">
      © 2026 网上衣橱系统 - 实验项目
    </el-footer>
  </el-container>
</template>

<style>
body {
  margin: 0;
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Microsoft YaHei', sans-serif;
}
</style>

<script>
export default {
  computed: {
    token() { return localStorage.getItem('token') },
    userName() {
      const user = JSON.parse(localStorage.getItem('user') || '{}')
      return user.username || ''
    }
  },
  methods: {
    logout() {
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      this.$router.push('/login')
      this.$message.success('已退出登录')
    }
  }
}
</script>
