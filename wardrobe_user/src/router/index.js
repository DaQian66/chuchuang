import { createRouter, createWebHashHistory } from 'vue-router'

const routes = [
  { path: '/', component: () => import('../views/Home.vue') },
  { path: '/login', component: () => import('../views/Login.vue') },
  { path: '/register', component: () => import('../views/Register.vue') },
  { path: '/details/:id', component: () => import('../views/Details.vue') },
  { path: '/cart', component: () => import('../views/Cart.vue'), meta: { requiresAuth: true } },
  { path: '/orders', component: () => import('../views/Orders.vue'), meta: { requiresAuth: true } },
  { path: '/profile', component: () => import('../views/Profile.vue'), meta: { requiresAuth: true } }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

// 路由守卫 - 检查登录状态
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.meta.requiresAuth && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router
