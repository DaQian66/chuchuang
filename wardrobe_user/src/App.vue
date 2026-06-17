<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

function loadToken() { return localStorage.getItem('token') }
function loadUser() {
  try { return JSON.parse(localStorage.getItem('user') || '{}') } catch { return {} }
}

const token = ref(loadToken())
const user = ref(loadUser())

watch(() => route.path, () => {
  token.value = loadToken()
  user.value = loadUser()
})

const userName = computed(() => user.value.username || '')
const userRole = computed(() => user.value.role || 0)
const isLoggedIn = computed(() => !!token.value)
const isAdmin = computed(() => isLoggedIn.value && userRole.value === 1)

function logout() {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  token.value = null
  user.value = {}
  router.push('/login')
}

onMounted(() => {
  token.value = loadToken()
  user.value = loadUser()
})
</script>

<template>
  <el-container style="min-height: 100vh;">
    <!-- ======== 管理员侧边栏 ======== -->
    <template v-if="isAdmin">
      <el-aside width="200px" style="background: #001529;">
        <div style="height: 60px; line-height: 60px; text-align: center; color: #fff; font-size: 18px; font-weight: bold; border-bottom: 1px solid rgba(255,255,255,0.1);">
          <el-icon style="vertical-align: middle;"><Monitor /></el-icon> 后台管理
        </div>
        <el-menu
          :default-active="route.path"
          router
          background-color="#001529"
          text-color="rgba(255,255,255,0.65)"
          active-text-color="#409eff"
          style="border: none;"
        >
          <el-menu-item index="/admin/clothes">
            <el-icon><DataAnalysis /></el-icon> 服装管理
          </el-menu-item>
          <el-menu-item index="/admin/orders">
            <el-icon><List /></el-icon> 订单管理
          </el-menu-item>
          <el-menu-item index="/admin/users">
            <el-icon><UserFilled /></el-icon> 用户管理
          </el-menu-item>
        </el-menu>
        <div style="position: absolute; bottom: 20px; width: 200px; text-align: center;">
          <el-button text size="small" style="color: rgba(255,255,255,0.65);" @click="logout">
            <el-icon><SwitchButton /></el-icon> 退出登录（{{ userName }}）
          </el-button>
        </div>
      </el-aside>
      <el-main style="padding: 20px;">
        <router-view />
      </el-main>
    </template>

    <!-- ======== 用户端侧边栏 ======== -->
    <template v-else>
      <el-container style="min-height: 100vh;">
        <!-- 顶部栏 -->
        <el-header style="height: 50px; background: #2c3e50; display: flex; align-items: center; justify-content: space-between; padding: 0 20px;">
          <span style="color: #fff; font-size: 16px; font-weight: bold; cursor: pointer;" @click="$router.push('/')">
            <el-icon style="vertical-align: middle; margin-right: 6px;"><ShoppingBag /></el-icon>
            网上衣橱
          </span>
          <div>
            <template v-if="isLoggedIn">
              <el-dropdown>
                <span style="color: #ddd; font-size: 13px; cursor: pointer;">
                  <el-icon><User /></el-icon> {{ userName }}
                </span>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item @click="logout" style="color: #f56c6c;">退出登录</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </template>
            <template v-else>
              <el-button type="primary" size="small" @click="$router.push('/login')">登录</el-button>
            </template>
          </div>
        </el-header>
        <!-- 主体：侧边栏 + 内容 -->
        <el-container style="flex:1;">
          <el-aside width="180px" style="background: #fff; border-right: 1px solid #e6e6e6;">
            <el-menu
              :default-active="route.path"
              router
              style="border: none;"
            >
              <el-menu-item index="/">
                <el-icon><HomeFilled /></el-icon> 首页
              </el-menu-item>
              <template v-if="isLoggedIn">
                <el-menu-item index="/cart">
                  <el-icon><CartFilled /></el-icon> 购物车
                </el-menu-item>
                <el-menu-item index="/orders">
                  <el-icon><List /></el-icon> 我的订单
                </el-menu-item>
                <el-menu-item index="/profile">
                  <el-icon><User /></el-icon> 个人中心
                </el-menu-item>
              </template>
            </el-menu>
          </el-aside>
          <el-main style="padding: 20px; background: #f5f7fa;">
            <router-view />
          </el-main>
        </el-container>
      </el-container>
    </template>
  </el-container>
</template>

<style>
body {
  margin: 0;
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Microsoft YaHei', sans-serif;
}
</style>
