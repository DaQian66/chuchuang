import { createRouter, createWebHashHistory } from 'vue-router'

const routes = [
  { path: '/', component: () => import('../views/Home.vue') },
  { path: '/login', component: () => import('../views/Login.vue') },
  { path: '/register', component: () => import('../views/Register.vue') },
  { path: '/details/:id', component: () => import('../views/Details.vue') },
  { path: '/cart', component: () => import('../views/Cart.vue'), meta: { requiresAuth: true } },
  { path: '/orders', component: () => import('../views/Orders.vue'), meta: { requiresAuth: true } },
  { path: '/profile', component: () => import('../views/Profile.vue'), meta: { requiresAuth: true } },
  // 管理员页面
  { path: '/admin/clothes', component: () => import('../views/AdminClothes.vue'), meta: { requiresAdmin: true } },
  { path: '/admin/orders', component: () => import('../views/AdminOrders.vue'), meta: { requiresAdmin: true } },
  { path: '/admin/users', component: () => import('../views/AdminUsers.vue'), meta: { requiresAdmin: true } }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

/**
 * 获取当前用户角色
 * @returns {number} 0=未登录, 1=管理员, 2=普通用户
 */
function getUserRole() {
  const token = localStorage.getItem('token')
  if (!token) return 0
  try {
    const user = JSON.parse(localStorage.getItem('user') || '{}')
    return user.role || 2
  } catch {
    return 0
  }
}

// 路由守卫 - 基于角色的访问控制
router.beforeEach((to, from, next) => {
  const role = getUserRole()

  // 管理员只能访问管理页面，访问非管理页面时重定向到管理首页
  if (role === 1 && !to.meta.requiresAdmin) {
    next('/admin/clothes')
    return
  }

  // 普通用户不能访问管理页面
  if (role === 2 && to.meta.requiresAdmin) {
    next('/')
    return
  }

  // 未登录访问需要登录的页面，跳转到登录页
  if (role === 0 && to.meta.requiresAuth) {
    next('/login')
    return
  }

  next()
})

export default router
