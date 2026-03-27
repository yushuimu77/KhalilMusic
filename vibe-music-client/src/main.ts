import { createApp } from 'vue'
import App from './App.vue'
import router from './routers/index'
import Store from '@/stores'
import ElementPlus from 'element-plus'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
import { themeStore } from '@/stores/modules/theme'
import 'element-plus/dist/index.css'
import 'element-plus/theme-chalk/dark/css-vars.css'
import './style/index.scss'
const app = createApp(App)

// 路由
app.use(router)
// 状态管理
app.use(Store)
const theme = themeStore()
theme.initializeTheme()
// ElementPlus
app.use(ElementPlus, {
  locale: zhCn,
})
app.mount('#app')
