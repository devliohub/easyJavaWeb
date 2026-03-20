import { createRouter, createWebHashHistory } from 'vue-router'

const router = createRouter({
  history: createWebHashHistory(), // hash模式：createWebHashHistory，history模式：createWebHistory
  routes: [
    {
      path: '/',
      redirect: '/account'
    },
    {
      path: '/login',
      name: 'login',
      component: () => import(/* webpackChunkName: "login" */ '../views/Login.vue')
    },
    {
      path: '/caidan',
      name: 'caidan',
      component: () => import(/* webpackChunkName: "caidan" */ '../views/Caidan.vue')
    },
    {
      path: '/swiper',
      name: 'swiper',
      component: () => import(/* webpackChunkName: "swiper" */ '../views/Swiper.vue')
    },
    {
      path: '/article',
      name: 'article',
      component: () => import(/* webpackChunkName: "article" */ '../views/Article.vue')
    },
    {
      path: '/kecheng',
      name: 'kecheng',
      component: () => import(/* webpackChunkName: "kecheng" */ '../views/kecheng.vue'),
    },
    {
      path: '/account',
      name: 'account',
      component: () => import(/* webpackChunkName: "account" */ '../views/Account.vue')
    }
  ]
})

export default router