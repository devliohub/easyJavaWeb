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
        children: [
          {
            path: '',
            component: (resolve) => require(['@/views/pages/home/home'], resolve),
          }, {
            path: 'saler',
            name: 'saler',
            redirect: '/saler/personal',
            component: (resolve) => require(['@/views/pages/saler/index'], resolve),
            meta: { title: '管理中心' },
            children: [
              {
                path: '/saler',
                redirect: '/saler/personal',
                component: (resolve) => require(['@/views/pages/home/index'], resolve),
                children: [
                  {
                    path: 'personal',
                    name: 'salerPersonal',
                    component: (resolve) => require(['@/views/pages/saler/salerPersonal'], resolve),
                    meta: { title: '商家中心', roles: 'saler' },
                  }
                ]
              }, {
                path: '/saler',
                component: (resolve) => require(['@/views/pages/home/index'], resolve),

              }, {
                path: '/saler',
                meta: { title: '免单活动管理', icon: 'iconhuodongguanli' },
                component: (resolve) => require(['@/views/pages/home/index'], resolve),
                children: [
                  {
                    path: 'kecheng',
                    name: 'kecheng',
                    component: (resolve) => require(['@/views/pages/saler/kecheng'], resolve),
                    meta: { title: '课程列表' },
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
                    path: 'search',
                    name: 'search',
                    component: (resolve) => require(['@/views/pages/saler/search'], resolve),
                    meta: { title: '搜索结果' },
                  }
                ]
              },
            ]
          }
        ]
      },
    ]
  },
  {
    path: '/404',
    component: (resolve) => require(['@/views/404'], resolve),
  },
  { path: '*', redirect: '/404', hidden: true }
];

