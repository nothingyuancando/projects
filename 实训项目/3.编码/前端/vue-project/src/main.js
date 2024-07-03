import './assets/main.scss'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import {createPersistedState} from "pinia-persistedstate-plugin";
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'


import App from './App.vue'
import router from './router'

const persist = createPersistedState();

const app = createApp(App)
const pinia = createPinia();
pinia.use(persist)
app.use(pinia)
app.use(router)
app.use(ElementPlus)
app.mount('#app')
