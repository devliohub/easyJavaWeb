import Vue from "vue";
import VueRouter from 'vue-router';
import App from "./App.vue";
import routes from "./router/index";
import store from "./store";
import VueClipboard from 'vue-clipboard2'

// 解决移动端click事件300毫秒延迟方法
import FastClick from 'fastclick'
FastClick.attach(document.body);

// 移动端适配
import 'lib-flexible/flexible.js'

// vant
import Vant from 'vant';
import 'vant/lib/index.css';

// 样式
import "@/assets/iconfont/iconfont.css"
import '@/styles/index.scss'

VueClipboard.config.autoSetContainer = true // add this line

Vue.use(VueClipboard)
Vue.use(VueRouter)
Vue.use(Vant);

const router = new VueRouter({
  routes
})

Vue.prototype.dateFormater = function (t, formater) {
  if (t) {
    let date = new Date(t),
      Y = date.getFullYear() + '',
      M = date.getMonth() + 1,
      D = date.getDate(),
      H = date.getHours(),
      m = date.getMinutes(),
      s = date.getSeconds();
    return formater.replace(/YYYY|yyyy/g, Y)
      .replace(/YY|yy/g, Y.substr(2, 2))
      .replace(/MM/g, (M < 10 ? '0' : '') + M)
      .replace(/DD/g, (D < 10 ? '0' : '') + D)
      .replace(/HH|hh/g, (H < 10 ? '0' : '') + H)
      .replace(/mm/g, (m < 10 ? '0' : '') + m)
      .replace(/ss/g, (s < 10 ? '0' : '') + s)
  } else {
    return ''
  }
};

router.beforeEach((to, from, next) => {
  /* 路由发生变化修改页面title, 回到顶部 */
  console.log(to)
  if (JSON.parse(window.sessionStorage.getItem('menuArr'))) {
    JSON.parse(window.sessionStorage.getItem('menuArr')).map((item) => {
      // 一级
      if (item.id == to.query.id) {
        document.title = item.name
        to.meta.title = item.name
      }
      // 二级
      if (item.sub_menus && item.sub_menus.length > 0) {
        item.sub_menus.map((_item) => {
          if (_item.id == to.query.id) {
            to.meta.title = item.name + ' > ' + _item.name
            document.title = _item.name
          }
        })
      }

    })
  }

  window.scrollTo(0, 0);
  next()
});


new Vue({
  router,
  store,
  render: h => h(App)
}).$mount("#app");
