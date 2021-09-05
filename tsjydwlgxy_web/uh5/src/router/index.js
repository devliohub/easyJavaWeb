export default [
  {
    path: '/',
    component: () => import('@/views/index'),
    redirect: '/index',
    children: [
      {
        path: '/index',
        name: 'index',
        component: () => import('@/views/pages/index'),
        meta: { title: '首页' }
      },
      {
        path: '/list',
        name: 'list',
        component: () => import('@/views/pages/list'),
        meta: { title: '列表' }
      },
      {
        path: '/search',
        name: 'search',
        component: () => import('@/views/pages/search'),
        meta: { title: '首页 > 搜索' }
      },
      {
        path: '/kecheng',
        name: 'kecheng',
        component: () => import('@/views/pages/kecheng'),
        meta: { title: '通识课程' }
      },
      {
        path: '/desc',
        name: 'desc',
        component: () => import('@/views/pages/desc'),
        meta: { title: '详情' }
      },
    ]
  },
  {
    path: '/404',
    component: () => import('@/views/404')
  },
  { path: '*', redirect: '/404', hidden: true }
];
