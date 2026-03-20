import request from '@/utils/request'

export function mcIndex(obj) {
    return request({
        url: '/mc/index',
        method: 'post',
        data: obj
    })
}
