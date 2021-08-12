import Vue from "vue";
import VueRouter from 'vue-router'
import App from "./App.vue";
import routes from "./router/index";
import store from "./store";
// import ElementUI from "element-ui";
import NProgress from 'nprogress' // Progress 进度条
import { getToken } from '@/utils/auth' // 验权

import "element-ui/lib/theme-chalk/index.css"; // 主题样式
import 'nprogress/nprogress.css'// Progress 进度条样式
import "normalize.css/normalize.css";
import '@/styles/index.scss'
import "@/assets/iconfont/iconfont.css"

import { cateTitle } from "@/api/common/common.js"

Vue.use(VueRouter)
import {
  Pagination,
  Dialog,
  Menu,
  Progress,
  Submenu,
  MenuItem,
  MenuItemGroup,
  Input,
  InputNumber,
  Radio,
  RadioGroup,
  RadioButton,
  Checkbox,
  CheckboxButton,
  CheckboxGroup,
  Select,
  Option,
  OptionGroup,
  Button,
  ButtonGroup,
  Table,
  TableColumn,
  DatePicker,
  Popover,
  Tooltip,
  Breadcrumb,
  BreadcrumbItem,
  Form,
  FormItem,
  Tabs,
  TabPane,
  Tag,
  Alert,
  Icon,
  Row,
  Col,
  Upload,
  Card,
  Carousel,
  CarouselItem,
  Collapse,
  CollapseItem,
  Cascader,
  Header,
  Aside,
  Main,
  Footer,
  Link,
  Image,
  Calendar,
  Backtop,
  PageHeader,
  CascaderPanel,
  Loading,
  MessageBox,
  Message,
  Notification
} from 'element-ui';

Vue.use(Pagination);
Vue.use(Dialog);
Vue.use(Menu);
Vue.use(Progress);
Vue.use(Submenu);
Vue.use(MenuItem);
Vue.use(MenuItemGroup);
Vue.use(Input);
Vue.use(InputNumber);
Vue.use(Radio);
Vue.use(RadioGroup);
Vue.use(RadioButton);
Vue.use(Checkbox);
Vue.use(CheckboxButton);
Vue.use(CheckboxGroup);
Vue.use(Select);
Vue.use(Option);
Vue.use(OptionGroup);
Vue.use(Button);
Vue.use(ButtonGroup);
Vue.use(Table);
Vue.use(TableColumn);
Vue.use(DatePicker);
Vue.use(Popover);
Vue.use(Tooltip);
Vue.use(Breadcrumb);
Vue.use(BreadcrumbItem);
Vue.use(Form);
Vue.use(FormItem);
Vue.use(Tabs);
Vue.use(TabPane);
Vue.use(Tag);
Vue.use(Alert);
Vue.use(Icon);
Vue.use(Row);
Vue.use(Col);
Vue.use(Upload);
Vue.use(Card);
Vue.use(Carousel);
Vue.use(CarouselItem);
Vue.use(Collapse);
Vue.use(CollapseItem);
Vue.use(Cascader);
Vue.use(Header);
Vue.use(Aside);
Vue.use(Main);
Vue.use(Footer);
Vue.use(Link);
Vue.use(Image);
Vue.use(Calendar);
Vue.use(Backtop);
Vue.use(PageHeader);
Vue.use(CascaderPanel);

Vue.use(Loading.directive);

Vue.prototype.$loading = Loading.service;
Vue.prototype.$alert = MessageBox.alert;
Vue.prototype.$confirm = MessageBox.confirm;
Vue.prototype.$message = Message;
Vue.prototype.$notify = Notification;

const router = new VueRouter({
  routes
})

// 全局获取分类
if (!JSON.parse(window.sessionStorage.getItem('tpyeArr'))) {
  cateTitle().then(res => {
    window.sessionStorage.setItem('tpyeArr', JSON.stringify(res.data))
  })
}

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount("#app");


router.beforeEach((to, from, next) => {
  NProgress.start()
  if (getToken()) { // 登录状态
    if (to.meta.islogin == false) { // 登录状态禁止的页面
      next('/404')
    } else { // 登录状态 粉丝2c, 商家2b
      if (to.meta.roles && store.getters.name) {
        if (to.meta.roles == 'fans' && store.getters.name.platform != '2c') {
          next('/404')
        } else if (to.meta.roles == 'saler' && store.getters.name.platform != '2b') {
          next('/404')
        } else {
          next()
        }
      } else { // 特殊处理商品类型下情况
        next()
      }
    }
    NProgress.done()
  } else { // 未登录状态
    to.meta.roles ? next('/login') : next()
    NProgress.done()
  }
})

router.afterEach(() => {
  NProgress.done() // 结束Progress
})
