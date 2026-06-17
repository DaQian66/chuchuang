<template>
  <div>
    <h2>我的购物车</h2>
    <el-table :data="cartList" border style="margin-top: 20px;" v-loading="loading">
      <el-table-column label="商品图片" width="100">
        <template #default="{ row }">
          <img :src="row.clothesImage || '/default_clothes.png'" style="width: 60px; height: 60px; object-fit: cover;" />
        </template>
      </el-table-column>
      <el-table-column prop="clothesName" label="商品名称" />
      <el-table-column prop="sizeName" label="尺码" width="80" />
      <el-table-column label="单价" width="100">
        <template #default="{ row }">¥{{ row.clothesPrice }}</template>
      </el-table-column>
      <el-table-column label="数量" width="120">
        <template #default="{ row }">
          <el-input-number v-model="row.quantity" :min="1" size="small"
            @change="updateQty(row)" />
        </template>
      </el-table-column>
      <el-table-column label="小计" width="120">
        <template #default="{ row }">
          <span style="color: #f56c6c; font-weight: bold;">¥{{ (row.clothesPrice * row.quantity).toFixed(2) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="100">
        <template #default="{ row }">
          <el-button type="danger" size="small" @click="removeItem(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div style="margin-top: 20px; text-align: right;">
      <p style="font-size: 18px;">
        合计：<span style="color: #f56c6c; font-weight: bold;">¥{{ totalFixed }}</span>
      </p>
      <el-button type="primary" size="large" @click="showCheckout = true" :disabled="cartList.length === 0">
        去结算
      </el-button>
    </div>

    <!-- 结算弹窗 -->
    <el-dialog v-model="showCheckout" title="确认订单" width="500px">
      <el-form label-width="100px">
        <el-form-item label="收货地址">
          <el-input v-model="address" type="textarea" :rows="2" placeholder="请输入收货地址" />
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="订单金额">
          <span style="color: #f56c6c; font-size: 20px; font-weight: bold;">¥{{ totalFixed }}</span>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showCheckout = false">取消</el-button>
        <el-button type="primary" @click="submitOrder">确认支付</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import api from '../api/request'

const cartList = ref([])
const loading = ref(false)
const showCheckout = ref(false)
const address = ref('')
const phone = ref('')

const totalFixed = computed(() => {
  return cartList.value.reduce((sum, item) => sum + (item.clothesPrice * item.quantity), 0).toFixed(2)
})

const fetchCart = async () => {
  loading.value = true
  try {
    const { data } = await api.get('/wardrobe_back/cart')
    cartList.value = data.data || []
  } catch (err) {
    console.error(err)
  } finally {
    loading.value = false
  }
}

const updateQty = async (item) => {
  try {
    const { data } = await api.post('/wardrobe_back/cart', null, {
      params: { action: 'updateQuantity', cartId: item.id, quantity: item.quantity }
    })
    if (data.code !== 200) alert(data.msg)
  } catch (err) { alert('更新失败') }
}

const removeItem = async (item) => {
  try {
    const { data } = await api.post('/wardrobe_back/cart', null, {
      params: { action: 'remove', cartId: item.id }
    })
    if (data.code === 200) fetchCart()
    else alert(data.msg)
  } catch (err) { alert('删除失败') }
}

const submitOrder = async () => {
  if (!address.value || !phone.value) {
    alert('请填写地址和电话')
    return
  }
  try {
    const { data } = await api.post('/wardrobe_back/order', null, {
      params: { action: 'create', address: address.value, phone: phone.value }
    })
    if (data.code === 200) {
      alert('订单创建成功！订单号：' + data.data.orderNo)
      showCheckout.value = false
      fetchCart()
    } else {
      alert(data.msg)
    }
  } catch (err) { alert('下单失败') }
}

onMounted(() => { fetchCart() })
</script>
