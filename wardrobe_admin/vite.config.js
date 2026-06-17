import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  server: {
    port: 7071,
    proxy: {
      '/wardrobe_back': {
        target: 'http://localhost:8080/8_war_exploded',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/wardrobe_back/, '')
      }
    }
  }
})
