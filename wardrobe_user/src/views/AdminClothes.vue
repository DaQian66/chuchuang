<template>
  <div>
    <h2 style="margin-top: 0;">服装管理</h2>

    <!-- 查询条件 -->
    <el-card style="margin-bottom: 20px;">
      <el-form :inline="true">
        <el-form-item label="类型">
          <el-input v-model="filters.typeName" placeholder="服装类型" />
        </el-form-item>
        <el-form-item label="风格">
          <el-input v-model="filters.style" placeholder="风格" />
        </el-form-item>
        <el-form-item label="名称">
          <el-input v-model="filters.name" placeholder="服装名称" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="fetchClothes">查询</el-button>
          <el-button type="success" @click="showAddDialog = true">添加服装</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 服装列表 -->
    <el-table :data="clothesList" border v-loading="loading">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column label="图片" width="80">
        <template #default="{ row }">
          <img :src="row.image || '/default_clothes.png'" style="width: 50px; height: 50px; object-fit: cover;" />
        </template>
      </el-table-column>
      <el-table-column prop="name" label="名称" />
      <el-table-column prop="typeName" label="类型" width="80" />
      <el-table-column prop="style" label="风格" width="80" />
      <el-table-column label="价格" width="80">
        <template #default="{ row }">¥{{ row.price }}</template>
      </el-table-column>
      <el-table-column prop="stock" label="库存" width="60" />
      <el-table-column label="操作" width="150">
        <template #default="{ row }">
          <el-button size="small" @click="editClothes(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="deleteClothes(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加/编辑对话框 -->
    <el-dialog v-model="showAddDialog" :title="editingId ? '编辑服装' : '添加服装'" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="form.typeId" placeholder="选择类型">
            <el-option v-for="t in types" :key="t.id" :label="t.typeName" :value="t.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="风格">
          <el-input v-model="form.style" />
        </el-form-item>
        <el-form-item label="价格">
          <el-input-number v-model="form.price" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="库存">
          <el-input-number v-model="form.stock" :min="0" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="图片">
          <el-upload
            :action="'/wardrobe_back/upload'"
            name="image"
            :headers="uploadHeaders"
            :on-success="onUploadSuccess"
            :on-error="onUploadError"
            :limit="1"
            accept=".jpg,.jpeg,.png"
          >
            <el-button type="primary">上传图片</el-button>
          </el-upload>
          <span v-if="form.image" style="margin-left: 10px;">{{ form.image }}</span>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddDialog = false">取消</el-button>
        <el-button type="primary" @click="saveClothes">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessageBox, ElMessage } from 'element-plus'
import api from '../api/request'

const clothesList = ref([])
const types = ref([])
const loading = ref(false)
const showAddDialog = ref(false)
const editingId = ref(null)

const filters = ref({ typeName: '', style: '', name: '' })
const form = ref({ name: '', typeId: null, style: '', price: 0, stock: 0, description: '', image: '' })

const token = computed(() => localStorage.getItem('token') || '')
const uploadHeaders = computed(() => ({ token: token.value }))

const fetchClothes = async () => {
  loading.value = true
  try {
    const params = { ...filters.value }
    const { data } = await api.get('/wardrobe_back/allClothes', { params })
    clothesList.value = data.data || []
  } catch (err) { console.error(err) }
  finally { loading.value = false }
}

const fetchTypes = async () => {
  try {
    const { data } = await api.get('/wardrobe_back/type')
    if (data.data) types.value = data.data
  } catch (err) {}
}

const onUploadSuccess = (response) => {
  if (response.code === 200) {
    form.value.image = response.data || response.msg || ''
  }
}

const onUploadError = (err) => {
  console.error('Upload error:', err)
  alert('图片上传失败')
}

const editClothes = (row) => {
  editingId.value = row.id
  Object.assign(form.value, { ...row, typeId: row.typeId })
  showAddDialog.value = true
}

const saveClothes = async () => {
  const params = { ...form.value }
  if (editingId.value) {
    params.action = 'update'
  } else {
    params.action = 'add'
  }
  try {
    const { data } = await api.post('/wardrobe_back/allClothes', null, { params })
    if (data.code === 200) {
      alert(editingId.value ? '更新成功' : '添加成功')
      showAddDialog.value = false
      editingId.value = null
      Object.keys(form.value).forEach(k => form.value[k] = k === 'stock' ? 0 : k === 'price' ? 0 : '')
      fetchClothes()
    } else alert(data.msg)
  } catch (err) { alert('保存失败') }
}

const deleteClothes = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除"${row.name}"吗？此操作不可恢复。`,
      '删除确认',
      {
        confirmButtonText: '确定删除',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    const { data } = await api.post('/wardrobe_back/allClothes', null, {
      params: { action: 'delete', id: row.id }
    })
    if (data.code === 200) {
      ElMessage.success('删除成功')
      fetchClothes()
    } else {
      ElMessage.error(data.msg)
    }
  } catch {
    // 用户取消则不操作
  }
}

onMounted(() => { fetchClothes(); fetchTypes() })
</script>
