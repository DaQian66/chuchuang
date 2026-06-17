<template>
  <div>
    <el-row :gutter="20" style="margin-bottom: 20px;">
      <el-col :span="6">
        <el-input v-model="filters.typeName" placeholder="服装类型" clearable @clear="search" />
      </el-col>
      <el-col :span="6">
        <el-input v-model="filters.style" placeholder="风格" clearable @clear="search" />
      </el-col>
      <el-col :span="6">
        <el-input v-model="filters.name" placeholder="搜索名称" clearable @clear="search" />
      </el-col>
      <el-col :span="6">
        <el-button type="primary" @click="search" style="width: 100%;">查询</el-button>
      </el-col>
    </el-row>

    <el-row :gutter="20" justify="start">
      <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="item in clothesList" :key="item.id" style="margin-bottom: 20px;">
        <el-card :body-style="{ padding: '0px' }" shadow="hover">
          <img :src="item.image || '/default_clothes.png'" style="width: 100%; height: 250px; object-fit: cover;"
               @click="goDetails(item.id)" />
          <div style="padding: 14px;">
            <h4 style="margin: 0 0 8px; font-size: 15px;">{{ item.name }}</h4>
            <p style="color: #999; font-size: 12px; margin: 4px 0;">
              {{ item.typeName }} | {{ item.style }}
            </p>
            <div style="display: flex; justify-content: space-between; align-items: center; margin-top: 10px;">
              <span style="color: #f56c6c; font-size: 18px; font-weight: bold;">
                ¥{{ item.price }}
              </span>
              <el-button size="small" @click="goDetails(item.id)">查看详情</el-button>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-empty v-if="!loading && clothesList.length === 0" description="暂无服装数据" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import api from '../api/request'

const router = useRouter()
const clothesList = ref([])
const loading = ref(false)
const filters = ref({ typeName: '', style: '', name: '' })

const fetchClothes = async () => {
  loading.value = true
  try {
    const params = {}
    if (filters.value.typeName) params.typeName = filters.value.typeName
    if (filters.value.style) params.style = filters.value.style
    if (filters.value.name) params.name = filters.value.name
    const { data } = await api.get('/wardrobe_back/allClothes', { params })
    clothesList.value = data.data || []
  } catch (err) {
    console.error(err)
  } finally {
    loading.value = false
  }
}

const search = () => { fetchClothes() }

const goDetails = (id) => { router.push(`/details/${id}`) }

onMounted(() => { fetchClothes() })
</script>
