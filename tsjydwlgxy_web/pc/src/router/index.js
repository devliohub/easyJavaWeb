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
                children: [
                  {
                    path: 'addGoods',
                    name: 'addGoods',
                    component: (resolve) => require(['@/views/pages/saler/addGoods'], resolve),
                    meta: { title: '添加宝贝', roles: 'saler' },
                  }
                ]
              }, {
                path: '/saler',
                meta: { title: '免单活动管理', icon: 'iconhuodongguanli' },
                component: (resolve) => require(['@/views/pages/home/index'], resolve),
                children: [
                  {
                    path: 'freeList',
                    name: 'freeList',
                    component: (resolve) => require(['@/views/pages/saler/freeList'], resolve),
                    meta: { title: '免单活动列表', roles: 'saler' },
                  },
                  {
                    path: 'freeTaskList',
                    name: 'freeTaskList',
                    component: (resolve) => require(['@/views/pages/saler/freeTaskList'], resolve),
                    meta: { title: '免单任务列表', roles: 'saler' },
                  },
                  {
                    path: 'freeTaskAfterList',
                    name: 'freeTaskAfterList',
                    component: (resolve) => require(['@/views/pages/saler/freeTaskAfterList'], resolve),
                    meta: { title: '免单任务售后列表', roles: 'saler' },
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

