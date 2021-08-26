import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vitePluginImport from 'vite-plugin-babel-import';
import path from 'path'

// https://vitejs.dev/config/
export default ({ mode }) => defineConfig({
  base: './',
  build: {
    outDir: 'crm',
  },
  plugins: [
    vue(),
    vitePluginImport([
      {
        libraryName: 'element-plus',
        libraryDirectory: 'es',
        style(name) {
          return `element-plus/lib/theme-chalk/${name}.css`;
        },
      }
    ])
  ],
  resolve: {
    alias: {
      '~': path.resolve(__dirname, './'),
      '@': path.resolve(__dirname, 'src')
    }
  },
  server: {
    host: '0.0.0.0',
    port: 9000,
    https: false,
    open: false,
    proxy: {
      '/api': {
        target: 'http://121.36.22.149:2000',
        changeOrigin: true,
      }
    }
  }
})
