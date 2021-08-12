import request from '@/utils/request'

// 熊抢购
export function rush_order_list(obj) {
    return request({
        url: '/mc/rushorderlist',
        method: 'post',
        data: obj
    })
}
// 熊抢购（导出）
export function rush_order_list_EX(obj) {
    return request({
        url: '/mc/rushorderlist',
        method: 'post',
        responseType: 'blob',
        params: {
            result_type: 2,
            ...obj
        }
    })
}

export function rush_order_action(obj) {
    return request({
        url: '/mc/rushorderaction',
        method: 'post',
        data: obj
    })
}

// 商家--免单售后列表
export function rush_sale_list(obj) {
    return request({
        url: '/mc/rushsalelist',
        method: 'post',
        data: obj
    })
}
// 商家--免单售后列表（导出）
export function rush_sale_list_EX(obj) {
    return request({
        url: '/mc/rushsalelist',
        method: 'post',
        responseType: 'blob',
        params: {
            result_type: 2,
            ...obj
        }
    })
}
