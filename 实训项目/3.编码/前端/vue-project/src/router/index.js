import { createRouter, createWebHistory } from 'vue-router'
import LoginVue from '@/views/Login.vue'

import CustomerHomeVue from '@/views/customer/CustomerHome.vue'
import OrderForm from "@/views/customer/pages/OrderForm.vue";
import OrderProgress from "@/views/customer/pages/OrderProgress.vue";
import OrderRejection from "@/views/customer/pages/OrderRejection.vue";

import PlatformHomeVue from '@/views/platform/PlatformHome.vue'
import OverView from "@/views/platform/pages/OverView.vue";
import Order from "@/views/platform/pages/Order.vue";
import Plan from "@/views/platform/pages/Plan.vue"
import Work from "@/views/platform/pages/Work.vue"

import FactoryHomeVue from "@/views/factory/FactoryHome.vue";
import FactoryEquipment from "@/views/factory/pages/Equipment.vue"
import FactoryProduct from "@/views/factory/pages/Product.vue";
import WorkOrder from "@/views/factory/pages/Workorder.vue";
import ReportWorke from "@/views/factory/pages/Reportorder.vue"
import FactoryView from "@/views/factory/pages/FactoryView.vue";
import WorkOrderGet from "@/views/factory/pages/Work.vue"
import GetReport from "@/views/factory/pages/GetReport.vue";
import ProducePlan from "@/views/factory/pages/ProducePlan.vue"


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {path:'/login',component:LoginVue},

    {path:'/platform',redirect:'/platform/overview',component:PlatformHomeVue,
    children:[
      {path:'/platform/order',component:Order},
      {path:'/platform/plan',component:Plan},
      {path:'/platform/overview',component:OverView},
      {path:'/platform/work',component:Work},

    ]
    },



    {path:'/customer',redirect:'/customer/orderform',component:CustomerHomeVue,
      children:[
        {path:'/customer/orderform',component:OrderForm},
        {path:'/customer/orderprogress',component:OrderProgress},
        {path:'/customer/orderreject',component:OrderRejection},

      ]
    },

    {path:'/factory',redirect:'/factory/factoryview',component:FactoryHomeVue,
      children:[
        {path:'/factory/factoryview',component:FactoryView},
        {path:'/factory/workorder',component:WorkOrder},
        {path:'/factory/product',component:FactoryProduct},
        {path:'/factory/equipment',component:FactoryEquipment},
        {path:'/factory/workreport',component:ReportWorke},
        {path:'/factory/work',component:WorkOrderGet},
        {path:'/factory/getreport',component:GetReport},
        {path:'/factory/produceplan',component:ProducePlan },

      ]
    },

  ]
})

export default router
