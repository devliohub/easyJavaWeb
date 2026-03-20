import axios from 'axios'
// import Message from 'element-ui';

// 创建axios实例
const service = axios.create({
    timeout: 10000 // 请求超时时间
})

// request拦截器
service.interceptors.request.use(
    config => {
        return config
    }, error => {
        return Promise.reject(error)
    })

// respone拦截器
service.interceptors.response.use(
    response => {
        const res = response.data
        // if (res.errno != 200) {
        //     Message.fail(res.usermsg);
        // }
        return res
    },
    error => {
        return Promise.reject(error)
    }
)

export default service
