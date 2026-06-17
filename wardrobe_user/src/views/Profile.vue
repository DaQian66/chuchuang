<template>
  <div style="max-width: 500px;">
    <h2>个人中心</h2>
    <el-card v-loading="loading">
      <el-form label-width="100px">
        <el-form-item label="用户名">
          <el-input :value="user.username" disabled />
        </el-form-item>
        <el-form-item label="手机号/学号">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form.address" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="updateUser">保存修改</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '../api/request'

const user = ref({})
const form = ref({ phone: '', address: '' })
const loading = ref(false)

const fetchInfo = async () => {
  loading.value = true
  try {
    const { data } = await api.get('/wardrobe_back/user', { params: { action: 'info' } })
    if (data.code === 200) {
      user.value = data.data
      form.value.phone = data.data.phone || ''
      form.value.address = data.data.address || ''
    }
  } catch (err) { console.error(err) }
  finally { loading.value = false }
}

const updateUser = async () => {
  try {
    const { data } = await api.post('/wardrobe_back/user', null, {
      params: { action: 'update', phone: form.value.phone, address: form.value.address }
    })
    if (data.code === 200) {
      alert('更新成功')
      fetchInfo()
    } else alert(data.msg)
  } catch (err) { alert('更新失败') }
}

onMounted(() => { fetchInfo() })
</script>
