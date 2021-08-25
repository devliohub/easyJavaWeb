import { createApp } from 'vue'
import { ElRow, ElCol, ElButton, ElContainer, ElAside, ElHeader, ElMain, ElFooter, ElMenu, ElSubmenu, ElMenuItemGroup, ElMenuItem, ElForm, ElFormItem, ElInput, ElPopover, ElTag, ElCard, ElTable, ElTableColumn, ElPagination, ElDialog, ElPopconfirm, ElUpload, ElLoading, ElSelect, ElOption, ElRadioGroup, ElRadio, ElCascader, ElCheckbox, ElCheckboxGroup, ElInputNumber } from 'element-plus'
import App from './App.vue'
import router from './router/index'
// import 'element-plus/lib/theme-chalk/index.css'

// 修改后的主题样式必须放在最后面
import '../theme/index.css'

const orderStatus = {
  0: '待支付',
  1: '已支付',
  2: '配货完成',
  3: '出库成功',
  4: '交易成功',
  '-1': '手动关闭',
  '-2': '超时关闭',
  '-3': '商家关闭'
}

const app = createApp(App)
// 全局过滤器
app.config.globalProperties.$filters = {
  orderMap(status) {
    return orderStatus[status] || '未知状态'
  },
  dateFormater(t, formater) {
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
  },
  prefix(url) {
    if (url && url.startsWith('http')) {
      return url
    } else {
      url = `http://backend-api-02.newbee.ltd${url}`
      return url
    }
  },
  resetImgUrl(imgObj, imgSrc, maxErrorNum) {
    if (maxErrorNum > 0) {
      imgObj.onerror = function () {
        resetImgUrl(imgObj, imgSrc, maxErrorNum - 1)
      }
      setTimeout(function () {
        imgObj.src = imgSrc
      }, 500)
    } else {
      imgObj.onerror = null
      imgObj.src = imgSrc
    }
  }
}

app.use(router)

app.use(ElRow)
  .use(ElCol)
  .use(ElButton)
  .use(ElContainer)
  .use(ElAside)
  .use(ElHeader)
  .use(ElMain)
  .use(ElFooter)
  .use(ElMenu)
  .use(ElSubmenu)
  .use(ElMenuItemGroup)
  .use(ElMenuItem)
  .use(ElForm)
  .use(ElFormItem)
  .use(ElInput)
  .use(ElPopover)
  .use(ElTag)
  .use(ElCard)
  .use(ElTable)
  .use(ElTableColumn)
  .use(ElPagination)
  .use(ElDialog)
  .use(ElPopconfirm)
  .use(ElUpload)
  .use(ElLoading)
  .use(ElSelect)
  .use(ElOption)
  .use(ElRadioGroup)
  .use(ElRadio)
  .use(ElCascader)
  .use(ElCheckbox)
  .use(ElCheckboxGroup)
  .use(ElInputNumber)

app.mount('#app')