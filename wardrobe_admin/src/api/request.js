import axios from 'axios'

const api = axios.create({ baseURL: '', timeout: 10000 })

api.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) config.headers.token = token
  return config
})

api.interceptors.response.use(
  response => response,
  error => Promise.reject(error)
)

export default api
