import { build, defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  server: {
    proxy: {
      // with options: http://localhost:5174/api/test-> http://localhost:8080/test
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ''),
      }
    },
  },
  build: {
    outDir: '../src/main/resources/static',
    assetsDir: './'
  }
})