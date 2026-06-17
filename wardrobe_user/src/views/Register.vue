<template>
  <div style="max-width: 500px; margin: 40px auto;">
    <el-card>
      <template #header><h3 style="margin: 0;">用户注册</h3></template>
      <el-form :model="form" label-width="100px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="2-20个字符" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" placeholder="至少6位" show-password />
        </el-form-item>
        <el-form-item label="确认密码">
          <el-input v-model="form.confirmPassword" type="password" placeholder="再次输入密码" show-password />
        </el-form-item>
        <el-form-item label="手机号/学号">
          <el-input v-model="form.phone" placeholder="11位手机号或24开头的学号" />
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form.address" placeholder="收货地址" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleRegister" style="width: 100%;">注册</el-button>
        </el-form-item>
        <el-form-item>
          <el-link @click="$router.push('/login')">已有账号？去登录</el-link>
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
const form = ref({ username: '', password: '', confirmPassword: '', phone: '', address: '' })

const handleRegister = async () => {
  if (!form.value.username || !form.value.password || !form.value.phone) {
    alert('请填写完整信息')
    return
  }
  if (form.value.password !== form.value.confirmPassword) {
    alert('两次密码不一致')
    return
  }
  try {
    const { data } = await api.post('/wardrobe_back/login', new URLSearchParams({
      username: form.value.username,
      password: form.value.password,
      phone: form.value.phone,
      address: form.value.address,
      action: 'register'
    }), { headers: { 'Content-Type': 'application/x-www-form-urlencoded' } })
    if (data.code === 200) {
      alert('注册成功，请登录')
      router.push('/login')
    } else {
      alert(data.msg)
    }
  } catch (err) {
    alert('注册失败')
  }
}
</script>
