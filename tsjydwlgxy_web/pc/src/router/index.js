import Layout from '../views/layout/Layout'

export default [
  {
    path: '/',
    component: Layout,
    redirect: '/index',
    children: [
      {
        path: '/index',
        name: 'index',
        component: (resolve) => require(['@/views/pages/home/index'], resolve),
        meta: { title: '首页' },
      },
      {
        path: 'saler',
        name: 'saler',
        component: (resolve) => require(['@/views/pages/saler/index'], resolve),
        children: [
          {
            path: 'kecheng',
            name: 'kecheng',
            component: (resolve) => require(['@/views/pages/saler/kecheng'], resolve),
            meta: { title: '通识课程' },
          },
          {
            path: 'wenzhang',
            name: 'wenzhang',
            component: (resolve) => require(['@/views/pages/saler/wenzhang'], resolve),
            meta: { title: '文章列表' },
          },
          {
            hidden: true,
            path: 'wenzhangdesc',
            name: 'wenzhangdesc',
            component: (resolve) => require(['@/views/pages/saler/wenzhangdesc'], resolve),
            meta: { title: '文章详情' },
          },
          {
            path: '/search',
            name: 'search',
            component: (resolve) => require(['@/views/pages/saler/search'], resolve),
            meta: { title: '搜索结果' },
          }
        ]
      }
    ]
  },
  {
    path: '/404',
    component: (resolve) => require(['@/views/404'], resolve),
  },
  { path: '*', redirect: '/404', hidden: true }
];

