<template>
  <div v-loading="loading">
    <template v-if="clothes">
      <el-card>
        <el-row :gutter="30">
          <el-col :span="10">
            <img :src="clothes.image || '/default_clothes.png'" style="width: 100%; height: 400px; object-fit: cover;" />
          </el-col>
          <el-col :span="14">
            <h2>{{ clothes.name }}</h2>
            <p style="color: #999;">{{ clothes.typeName }} | {{ clothes.style }}</p>
            <p style="color: #f56c6c; font-size: 28px; font-weight: bold;">
              ¥{{ clothes.price }}
            </p>
            <p style="color: #666;">库存: {{ clothes.stock }}</p>
            <p style="line-height: 1.8;">{{ clothes.description }}</p>

            <el-divider />

            <div style="margin-top: 20px;">
              <p>选择尺码：</p>
              <el-radio-group v-model="selectedSize" style="display: flex; flex-wrap: wrap; gap: 10px;">
                <el-radio v-for="size in sizes" :key="size.id" :value="size.id">
                  {{ size.sizeName }}
                </el-radio>
              </el-radio-group>
            </div>

            <div style="margin-top: 20px; display: flex; align-items: center; gap: 10px;">
              <p>数量：</p>
              <el-input-number v-model="quantity" :min="1" :max="clothes.stock" />
            </div>

            <div style="margin-top: 30px;">
              <el-button type="primary" size="large" @click="addToCart" style="margin-right: 10px;">
                <el-icon><CartFilled /></el-icon> 加入购物车
              </el-button>
              <el-button type="success" size="large" @click="buyNow">
                <el-icon><ShoppingCart /></el-icon> 立即购买
              </el-button>
            </div>
          </el-col>
        </el-row>
      </el-card>
    </template>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import api from '../api/request'

const route = useRoute()
const router = useRouter()
const clothes = ref(null)
const sizes = ref([])
const selectedSize = ref(null)
const quantity = ref(1)
const loading = ref(false)

const fetchDetails = async () => {
  loading.value = true
  try {
    const { data } = await api.get('/wardrobe_back/allClothes', {
      params: { action: 'details', id: route.params.id }
    })
    if (data.code === 200) {
      clothes.value = data.data
    }
    // 获取尺码
    const { data: sizeData } = await api.get('/wardrobe_back/size', {
      params: { typeId: data.data?.typeId }
    })
    if (sizeData.code === 200) sizes.value = sizeData.data
  } catch (err) {
    console.error(err)
  } finally {
    loading.value = false
  }
}

const addToCart = () => {
  if (!selectedSize.value) { alert('请选择尺码'); return }
  if (!localStorage.getItem('token')) {
    alert('请先登录')
    router.push('/login')
    return
  }
  api.post('/wardrobe_back/cart', null, {
    params: {
      action: 'addToCart',
      clothesId: clothes.value.id,
      sizeId: selectedSize.value,
      quantity: quantity.value
    }
  }).then(res => {
    if (res.data.code === 200) {
      alert(res.data.msg)
    } else {
      alert(res.data.msg)
    }
  }).catch(() => alert('添加失败'))
}

const buyNow = () => {
  if (!localStorage.getItem('token')) {
    alert('请先登录')
    router.push('/login')
    return
  }
  // 先加入购物车，再跳转
  addToCart()
}

onMounted(() => { fetchDetails() })
</script>
