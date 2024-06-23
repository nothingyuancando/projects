import { createRouter, createWebHistory } from 'vue-router'
import LoginVue from '@/views/Login.vue'
import HomeVue from '@/views/Home.vue'
import Order from '@/views/order/OrderManagement.vue'
import Plan from '@/views/plan/PlanManagement.vue'
import Production from '@/views/production/ProductionManagement.vue'
import Track from '@/views/track/TrackManagement.vue'
import Schedum from '@/views/schedum/SchedumManagement.vue'
import OverView from '@/views/overview/OverView.vue'
import Employee from '@/views/employee/EmployeeManagement.vue'
import Equipment from "@/views/equipment/EquipmentManagement.vue";
import User from "@/views/user/UserManagement.vue"
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {path:'/login',component:LoginVue},
    {path:'/',redirect:'/overview',component:HomeVue,
    children:[
      {path:'/order',component:Order},
      {path:'/plan',component:Plan},
        {path:'/production',component:Production},
        {path:'/track',component:Track},
      {path:'/schedum',component:Schedum},
      {path:'/overview',component:OverView},
      {path:'/employee',component:Employee},
      {path:'/equipment',component:Equipment},
      {path:'/user',component:User},

    ]

    },


  ]
})

export default router
