import { createRouter, createWebHashHistory } from 'vue-router'

const routes = [
  { path: '/login', component: () => import('../views/Login.vue') },
  { path: '/', component: () => import('../views/ClothesManage.vue'), meta: { requiresAdmin: true } },
  { path: '/orders', component: () => import('../views/OrderManage.vue'), meta: { requiresAdmin: true } },
  { path: '/users', component: () => import('../views/UserManage.vue'), meta: { requiresAdmin: true } }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.meta?.requiresAdmin && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router
