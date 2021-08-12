import request from '@/utils/request'

// 免单任务
export function free_order_list(obj) {
    return request({
        url: '/mc/freeorderlist',
        method: 'post',
        data: obj
    })
}
// 免单任务导出
export function free_order_list_EX(obj) {
    return request({
        url: '/mc/freeorderlist',
        method: 'post',
        responseType: 'blob',
        params: {
            result_type: 2,
            ...obj
        }
    })
}

export function free_order_action(obj) {
  return request({
      url: '/mc/freeorderaction',
      method: 'post',
      data: obj
  })
}

// 商家售后详情接口（免单/熊抢购）
export function sale_detail(obj) {
    return request({
        url: '/mc/saleapplydispose',
        method: 'post',
        data: obj
    })
}

// 商家售后历史（免单/熊抢购）
export function sale_history(obj) {
    return request({
        url: '/mc/salehistory',
        method: 'post',
        data: obj
    })
}

// 商家--售后申请处理
export function sale_apply_dispose(obj) {
    return request({
        url: '/mc/saleapplydispose',
        method: 'post',
        data: obj
    })
}

// 商家--免单售后列表
export function free_sale_list(obj) {
    return request({
        url: '/mc/freesalelist',
        method: 'post',
        data: obj
    })
}
// 商家--免单售后列表（导出）
export function free_sale_list_EX(obj) {
    return request({
        url: '/mc/freesalelist',
        method: 'post',
        responseType: 'blob',
        params: {
            result_type: 2,
            ...obj
        }
    })
}