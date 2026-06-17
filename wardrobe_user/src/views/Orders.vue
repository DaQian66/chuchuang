<template>
  <div>
    <h2>我的订单</h2>
    <el-table :data="orderList" border style="margin-top: 20px;" v-loading="loading">
      <el-table-column prop="orderNo" label="订单号" width="200" />
      <el-table-column prop="clothesDetail" label="商品详情" show-overflow-tooltip />
      <el-table-column label="金额" width="100">
        <template #default="{ row }">
          <span style="color: #f56c6c; font-weight: bold;">¥{{ row.totalPrice }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="statusText" label="状态" width="100" />
      <el-table-column prop="phone" label="联系电话" width="120" />
      <el-table-column prop="createTime" label="下单时间" width="180" />
      <el-table-column label="操作" width="120">
        <template #default="{ row }">
          <el-button v-if="row.status === 0" type="danger" size="small" @click="cancelOrder(row)">
            取消
          </el-button>
          <el-tag v-if="row.status === 1" type="success">已发货</el-tag>
          <el-tag v-if="row.status === 2" type="">已完成</el-tag>
        </template>
      </el-table-column>
    </el-table>
    <el-empty v-if="!loading && orderList.length === 0" description="暂无订单" />
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
    const { data } = await api.get('/wardrobe_back/order')
    orderList.value = (data.data || []).map(o => {
      o.statusText = getStatusText(o.status)
      return o
    })
  } catch (err) {
    console.error(err)
  } finally {
    loading.value = false
  }
}

const getStatusText = (status) => {
  const map = { 0: '待发货', 1: '已发货', 2: '已完成', 3: '已取消' }
  return map[status] || '未知'
}

const cancelOrder = async (row) => {
  try {
    const { data } = await api.post('/wardrobe_back/order', null, {
      params: { action: 'cancel', id: row.id }
    })
    if (data.code === 200) {
      alert('订单已取消')
      fetchOrders()
    } else alert(data.msg)
  } catch (err) { alert('取消失败') }
}

onMounted(() => { fetchOrders() })
</script>
