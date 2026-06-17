<template>
  <div>
    <h2 style="margin-top: 0;">用户管理</h2>
    <el-table :data="users" border v-loading="loading">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="username" label="用户名" />
      <el-table-column prop="phone" label="手机号/学号" />
      <el-table-column prop="address" label="地址" show-overflow-tooltip />
      <el-table-column label="角色" width="100">
        <template #default="{ row }">
          <el-tag :type="row.role === 1 ? 'warning' : ''">
            {{ row.role === 1 ? '管理员' : '普通用户' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="注册时间" width="180" />
      <el-table-column label="操作" width="100">
        <template #default="{ row }">
          <el-button v-if="row.role !== 1" size="small" type="danger" @click="deleteUser(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '../api/request'

const users = ref([])
const loading = ref(false)

const fetchUsers = async () => {
  loading.value = true
  try {
    const { data } = await api.get('/wardrobe_back/user', { params: { action: 'allUsers' } })
    users.value = data.data || []
  } catch (err) { console.error(err) }
  finally { loading.value = false }
}

const deleteUser = async (row) => {
  try {
    const { data } = await api.post('/wardrobe_back/user', null, {
      params: { action: 'delete', id: row.id }
    })
    if (data.code === 200) {
      alert('删除成功')
      fetchUsers()
    } else alert(data.msg)
  } catch (err) { alert('删除失败') }
}

onMounted(() => { fetchUsers() })
</script>
