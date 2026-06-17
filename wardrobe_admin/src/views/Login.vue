<template>
  <div style="max-width: 400px; margin: 80px auto;">
    <el-card>
      <template #header><h3 style="margin: 0;">管理员登录</h3></template>
      <el-form :model="form" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin" style="width: 100%;">登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import api from '../api/request'

const router = useRouter()
const form = ref({ username: '', password: '' })

const handleLogin = async () => {
  if (!form.value.username || !form.value.password) return
  try {
    const { data } = await api.post('/wardrobe_back/login', new URLSearchParams({
      username: form.value.username,
      password: form.value.password,
      action: 'adminLogin'
    }), { headers: { 'Content-Type': 'application/x-www-form-urlencoded' } })
    if (data.code === 200) {
      localStorage.setItem('token', data.data.token)
      localStorage.setItem('user', JSON.stringify(data.data.user))
      router.push('/')
    } else {
      alert(data.msg)
    }
  } catch (err) { alert('登录失败') }
}
</script>
