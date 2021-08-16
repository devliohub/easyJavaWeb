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
            path: 'itemDesc',
            name: 'itemDesc',
            component: (resolve) => require(['@/views/pages/home/itemDesc'], resolve),
            meta: { title: '商品详情' }
          }, {
            path: '/allGoods/:type',
            name: 'allGoods',
            component: (resolve) => require(['@/views/pages/home/allGoods'], resolve),
          }, {
            path: '/allGoods/:type/itemDesc',
            name: 'allGoods_itemDesc',
            component: (resolve) => require(['@/views/pages/home/itemDesc'], resolve),
            meta: { title: '商品详情' }
          }, {
            path: 'limitFree',
            name: 'limitFree',
            component: (resolve) => require(['@/views/pages/home/index'], resolve),
            meta: { title: '限量免单' },
            children: [{
              path: '',
              component: (resolve) => require(['@/views/pages/home/allGoods'], resolve),
            }, {
              path: 'itemDesc',
              name: 'limitFree_itemDesc',
              component: (resolve) => require(['@/views/pages/home/itemDesc'], resolve),
              meta: { title: '商品详情' }
            }]
          }, {
            path: 'bearBuy',
            name: 'bearBuy',
            component: (resolve) => require(['@/views/pages/home/index'], resolve),
            meta: { title: '熊抢购' },
            children: [{
              path: '',
              component: (resolve) => require(['@/views/pages/home/allGoods'], resolve),
            }, {
              path: 'itemDesc',
              name: 'bearBuy_itemDesc',
              component: (resolve) => require(['@/views/pages/home/itemDesc'], resolve),
              meta: { title: '商品详情' }
            }]
          }, {
            path: 'saler',
            name: 'saler',
            redirect: '/saler/personal',
            component: (resolve) => require(['@/views/pages/saler/index'], resolve),
            meta: { title: '管理中心' },
            children: [
              {
                hidden: true,
                path: '/saler',
                redirect: '/saler/personal',
                component: (resolve) => require(['@/views/pages/home/index'], resolve),
                children: [
                  {
                    path: 'tips',
                    name: 'salerTips',
                    component: (resolve) => require(['@/views/pages/saler/tips'], resolve),
                    meta: { title: '系统公告', roles: 'saler' },
                  }
                ]
              },
              {
                hidden: true,
                path: '/saler',
                redirect: '/saler/personal',
                component: (resolve) => require(['@/views/pages/home/index'], resolve),
                children: [
                  {
                    path: 'charge',
                    name: 'salerCharge',
                    component: (resolve) => require(['@/views/pages/saler/salerCharge'], resolve),
                    meta: { title: '充值', roles: 'saler' },
                  }
                ]
              },
              {
                hidden: true,
                path: '/saler',
                redirect: '/saler/personal',
                component: (resolve) => require(['@/views/pages/home/index'], resolve),
                children: [
                  {
                    path: 'msg',
                    name: 'salerMsg',
                    component: (resolve) => require(['@/views/pages/saler/msg'], resolve),
                    meta: { title: '消息通知', roles: 'saler' },
                  }
                ]
              },
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
                    path: 'freeApply',
                    name: 'freeApply',
                    component: (resolve) => require(['@/views/pages/saler/freeApply'], resolve),
                    meta: { title: '免单活动申请', roles: 'saler' },
                  },
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
              {
                path: '/saler',
                meta: { title: '我的账户', icon: 'iconwodezhanghu' },
                component: (resolve) => require(['@/views/pages/home/index'], resolve),
                children: [
                  {
                    path: 'accountDesc',
                    name: 'accountDesc',
                    component: (resolve) => require(['@/views/pages/saler/accountDesc'], resolve),
                    meta: { title: '账户明细', roles: 'saler' }
                  },
                  {
                    path: 'salerRecord',
                    name: 'salerRecord',
                    component: (resolve) => require(['@/views/pages/saler/salerRecord'], resolve),
                    meta: { title: '交易记录', roles: 'saler' },
                  }
                ]
              },
            ]
          }
        ]
      },
      {
        path: '/register',
        name: 'register',
        component: (resolve) => require(['@/views/pages/register/register'], resolve),
        meta: { title: '注册', islogin: false }
      },
      {
        path: '/registerDesc',
        name: 'registerDesc',
        component: (resolve) => require(['@/views/pages/register/registerDesc'], resolve),
        meta: { title: '注册详情', islogin: false }
      },
      {
        path: '/login',
        name: 'login',
        component: (resolve) => require(['@/views/pages/login/login'], resolve),
        meta: { title: '登录', islogin: false }
      },
      {
        path: '/findPwd',
        name: 'findPwd',
        component: (resolve) => require(['@/views/pages/login/findPwd'], resolve),
        meta: { title: '忘记密码', islogin: false }
      },
    ]
  },
  {
    path: '/404',
    component: (resolve) => require(['@/views/404'], resolve),
  },
  { path: '*', redirect: '/404', hidden: true }
];

