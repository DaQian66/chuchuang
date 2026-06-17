import axios from 'axios'

const api = axios.create({
  baseURL: '',
  timeout: 10000
})

// 请求拦截器 - 自动添加 token
api.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.token = token
  }
  return config
})

// 响应拦截器 - 处理 401 和 403
api.interceptors.response.use(
  response => {
    const result = response.data
    if (result.code === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      alert('登录已过期，请重新登录')
      window.location.href = '/#/login'
    } else if (result.code === 403) {
      alert(result.msg || '无权限访问')
    }
    return response
  },
  error => {
    return Promise.reject(error)
  }
)

export default api
