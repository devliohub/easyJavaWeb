import axios from 'axios'
import { Message } from 'element-ui'
import store from '../store'

// 创建axios实例
const service = axios.create({
    timeout: 1000000, // 请求超时时间
})

if (process.env.NODE_ENV != 'development') service.baseURL = "http://www.taoxiaoxiong.cn"

// request拦截器
service.interceptors.request.use(
    config => {
        // if(config.data) {
        //     config.data = {
        //         ...config.data,
        //         _sign: 123
        //     }
        // } else {
        //     config.params = {
        //         ...config.params,
        //         _sign: 123
        //     }
        // }
        // if (store.getters.token) {
        // config.headers.Authorization = '';
        // }
        return config
    }, error => {
        Message({
            message: error.msg,
            type: 'error'
        })
        return Promise.reject(error)
    })

// respone拦截器
service.interceptors.response.use(
    response => {
        const res = response.data
        // 导出方法不做数据回调处理
        if (response.config.responseType == 'blob') {
            return res
        }
        if (res.error.errno == 431) {
            Message({
                message: '登录异常，请重新登录',
                type: 'error',
            })
            store.dispatch("LogOut").then(res => {
                if (res) location.href = "/#/login"
            })
        } else if (res.error.errno != 200) {
            Message({
                message: res.error.usermsg,
                type: 'error',
            })
        }
        return res
    },
    error => {
        return Promise.reject(error)
    }
)

export default service
