<template>
  <div class="login-page">
    <div class="login-card">
      <!-- 顶部 -->
      <div class="login-header">
        <div class="header-icon">
          <el-icon :size="40"><ShoppingBag /></el-icon>
        </div>
        <h2 class="header-title">网上衣橱</h2>
        <p class="header-desc">{{ showAdmin ? '管理员登录 · 后台管理' : '用户登录 · 时尚购物' }}</p>
      </div>

      <!-- 模式切换标签 -->
      <div class="mode-tabs">
        <div
          class="mode-tab"
          :class="{ active: !showAdmin }"
          @click="showAdmin = false"
        >
          <el-icon><User /></el-icon> 用户
        </div>
        <div
          class="mode-tab"
          :class="{ active: showAdmin }"
          @click="showAdmin = true"
        >
          <el-icon><Monitor /></el-icon> 管理
        </div>
      </div>

      <!-- 表单 -->
      <el-form :model="form" class="login-form" @keyup.enter="handleLogin">
        <el-form-item>
          <el-input
            v-model="form.username"
            placeholder="请输入用户名"
            :prefix-icon="UserIcon"
            size="large"
          />
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="form.password"
            type="password"
            placeholder="请输入密码"
            show-password
            :prefix-icon="Lock"
            size="large"
          />
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            size="large"
            class="login-btn"
            @click="handleLogin"
            :loading="loading"
            round
          >
            {{ loading ? '登录中...' : '登 录' }}
          </el-button>
        </el-form-item>
      </el-form>

      <!-- 注册链接 -->
      <div v-if="!showAdmin" class="register-link">
        还没有账号？
        <span class="link" @click="$router.push('/register')">立即注册</span>
      </div>

      <!-- 演示账号 -->
      <div class="demo-section">
        <span class="demo-label">演示账号快速登录</span>
        <div class="demo-buttons">
          <el-button size="small" @click="fillAccount('admin', 'admin123')">
            <el-tag size="small" type="warning" style="margin-right:4px;">管理</el-tag>
            admin
          </el-button>
          <el-button size="small" @click="fillAccount('123456', '123456')">
            <el-tag size="small" type="primary" style="margin-right:4px;">用户</el-tag>
            123456
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User as UserIcon, Lock } from '@element-plus/icons-vue'
import api from '../api/request'

const router = useRouter()
const form = ref({ username: '', password: '' })
const showAdmin = ref(false)
const loading = ref(false)

const handleLogin = async () => {
  if (!form.value.username || !form.value.password) return
  loading.value = true
  try {
    const { data } = await api.post('/wardrobe_back/login', new URLSearchParams({
      username: form.value.username,
      password: form.value.password,
      action: showAdmin.value ? 'adminLogin' : 'login'
    }), { headers: { 'Content-Type': 'application/x-www-form-urlencoded' } })
    if (data.code === 200) {
      localStorage.setItem('token', data.data.token)
      localStorage.setItem('user', JSON.stringify(data.data.user))
      router.push(data.data.user.role === 1 ? '/admin/clothes' : '/')
    } else {
      ElMessage.error(data.msg)
    }
  } catch {
    ElMessage.error('登录失败')
  } finally {
    loading.value = false
  }
}

function fillAccount(username, password) {
  form.value.username = username
  form.value.password = password
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f7fa;
  padding: 20px;
}

.login-card {
  width: 420px;
  max-width: 100%;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 24px rgba(0, 0, 0, 0.08);
  padding: 40px 36px 32px;
}

/* ====== 头部 ====== */
.login-header {
  text-align: center;
  margin-bottom: 28px;
}

.header-icon {
  width: 72px;
  height: 72px;
  margin: 0 auto 16px;
  background: linear-gradient(135deg, #2c3e50, #3498db);
  border-radius: 18px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
}

.header-title {
  font-size: 22px;
  font-weight: 700;
  color: #1a1a1a;
  margin: 0 0 6px;
}

.header-desc {
  font-size: 13px;
  color: #999;
  margin: 0;
}

/* ====== 模式切换 ====== */
.mode-tabs {
  display: flex;
  background: #f0f2f5;
  border-radius: 8px;
  padding: 3px;
  margin-bottom: 28px;
}

.mode-tab {
  flex: 1;
  text-align: center;
  padding: 8px 0;
  font-size: 13px;
  color: #666;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.25s;
}

.mode-tab .el-icon {
  vertical-align: middle;
  margin-right: 4px;
}

.mode-tab.active {
  background: #fff;
  color: #409eff;
  font-weight: 600;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

/* ====== 表单 ====== */
.login-form {
  margin-bottom: 16px;
}

.login-form :deep(.el-input__wrapper) {
  border-radius: 8px;
  padding: 4px 12px;
}

.login-btn {
  width: 100%;
  height: 42px;
  font-size: 15px;
  letter-spacing: 3px;
}

/* ====== 注册链接 ====== */
.register-link {
  text-align: center;
  font-size: 13px;
  color: #999;
  margin-bottom: 24px;
}

.register-link .link {
  color: #409eff;
  cursor: pointer;
}

.register-link .link:hover {
  text-decoration: underline;
}

/* ====== 演示账号 ====== */
.demo-section {
  background: #f8f9fa;
  border-radius: 10px;
  padding: 16px;
  text-align: center;
}

.demo-label {
  font-size: 12px;
  color: #bbb;
  display: block;
  margin-bottom: 12px;
}

.demo-buttons {
  display: flex;
  gap: 10px;
  justify-content: center;
}

.demo-buttons .el-button {
  font-size: 12px;
}
</style>
