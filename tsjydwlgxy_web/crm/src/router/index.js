import { createRouter, createWebHashHistory } from 'vue-router'

const router = createRouter({
  history: createWebHashHistory(), // hash模式：createWebHashHistory，history模式：createWebHistory
  routes: [
    {
      path: '/',
      redirect: '/hot'
    },
    {
      path: '/login',
      name: 'login',
      component: () => import(/* webpackChunkName: "login" */ '../views/Login.vue')
    },
    {
      path: '/add',
      name: 'add',
      component: () => import(/* webpackChunkName: "add" */ '../views/AddGood.vue')
    },
    {
      path: '/swiper',
      name: 'swiper',
      component: () => import(/* webpackChunkName: "swiper" */ '../views/Swiper.vue')
    },
    {
      path: '/hot',
      name: 'hot',
      component: () => import(/* webpackChunkName: "hot" */ '../views/IndexConfig.vue')
    },
    {
      path: '/recommend',
      name: 'recommend',
      component: () => import(/* webpackChunkName: "recommend" */ '../views/IndexConfig.vue')
    },
    {
      path: '/kecheng',
      name: 'kecheng',
      component: () => import(/* webpackChunkName: "kecheng" */ '../views/kecheng.vue'),
    },
    {
      path: '/good',
      name: 'good',
      component: () => import(/* webpackChunkName: "new" */ '../views/Good.vue')
    },
    {
      path: '/order',
      name: 'order',
      component: () => import(/* webpackChunkName: "order" */ '../views/Order.vue')
    },
    {
      path: '/order_detail',
      name: 'order_detail',
      component: () => import(/* webpackChunkName: "order_detail" */ '../views/OrderDetail.vue')
    },
    {
      path: '/account',
      name: 'account',
      component: () => import(/* webpackChunkName: "account" */ '../views/Account.vue')
    }
  ]
})

export default router