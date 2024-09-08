import {build, defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  optimizeDeps: {
    include: ['jquery']
  },
  server: {
    proxy: {
      // with options: http://localhost:5174/api/test-> http://localhost:8080/test
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ''),
      },
      '/ws': {
        target: 'http://localhost:8080',
        ws: true,  // WebSocket 프록시 설정
        changeOrigin: true,
      },
    },
  },
  build: {
    outDir: '../src/main/resources/static',
    assetsDir: 'assets'
  }
})