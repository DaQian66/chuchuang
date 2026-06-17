<template>
  <div>
    <h2 style="margin-top: 0;">订单管理</h2>
    <el-table :data="orderList" border v-loading="loading">
      <el-table-column prop="orderNo" label="订单号" width="200" />
      <el-table-column prop="username" label="用户" width="100" />
      <el-table-column prop="clothesDetail" label="商品详情" show-overflow-tooltip />
      <el-table-column label="金额" width="100">
        <template #default="{ row }">¥{{ row.totalPrice }}</template>
      </el-table-column>
      <el-table-column prop="statusText" label="状态" width="100" />
      <el-table-column prop="address" label="收货地址" show-overflow-tooltip />
      <el-table-column prop="phone" label="电话" width="120" />
      <el-table-column prop="createTime" label="下单时间" width="180" />
      <el-table-column label="操作" width="100">
        <template #default="{ row }">
          <el-button v-if="row.status === 0" type="success" size="small" @click="shipOrder(row)">发货</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '../api/request'

const orderList = ref([])
const loading = ref(false)

const fetchOrders = async () => {
  loading.value = true
  try {
    const { data } = await api.get('/wardrobe_back/order', { params: { action: 'all' } })
    orderList.value = (data.data || []).map(o => {
      o.statusText = { 0: '待发货', 1: '已发货', 2: '已完成', 3: '已取消' }[o.status]
      return o
    })
  } catch (err) { console.error(err) }
  finally { loading.value = false }
}

const shipOrder = async (row) => {
  try {
    const { data } = await api.post('/wardrobe_back/order', null, {
      params: { action: 'ship', id: row.id }
    })
    if (data.code === 200) {
      alert('发货成功')
      fetchOrders()
    } else alert(data.msg)
  } catch (err) { alert('发货失败') }
}

onMounted(() => { fetchOrders() })
</script>
