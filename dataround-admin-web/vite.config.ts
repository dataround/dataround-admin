import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

const baseUrl = '/dataadmin';

export default defineConfig({
  base: baseUrl,
  build: {
    outDir: '../dataround-admin-svc/src/main/resources/static',
  },
  server: {
    proxy: {
      '/dataadmin/api': {
        target: 'http://localhost:5680',
        changeOrigin: true,  
        rewrite: (path) => path.replace(/^\/dataadmin\/api/, "/dataadmin/api/"),
        configure: (proxy, options) => {
          proxy.on('proxyReq', (proxyReq, req, res, options) => {
            proxyReq.setHeader('X-Forwarded-Port', '5173');
          });
        }
      }
    },
  },
  css: {
    preprocessorOptions: {
      less: {
        modifyVars: {

        },
        javascriptEnabled: true,
      },
    },
  },
  plugins: [react()],
})
