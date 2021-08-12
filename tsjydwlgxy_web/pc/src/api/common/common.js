import request from '@/utils/request'

// 上传图片
export function uploadImg(obj) {
    return request({
        url: '/i/upload',
        method: 'post',
        data: obj
    })
}

// 删除上传图片
export function deleteUpload(obj) {
    return request({
        url: '/i/delete',
        method: 'post',
        data: obj
    })
}

// 商品活动 分类活动列表
export function cateDesc() {
    return request({
        url: '/pa/cateactivity',
        method: 'get'
    })
}

// 商品活动分类
export function cateTitle() {
    return request({
        url: '/pa/cate',
        method: 'get'
    })
}

// 商品活动列表
export function indexSearch(obj) {
    return request({
        url: '/pa/index',
        method: 'post',
        params: obj
    })
}
// 商品活动列表导出
export function indexSearch_EX(obj) {
    return request({
        url: '/pa/index',
        method: 'post',
        responseType: 'blob',
        params: {
            result_type: 2,
            ...obj
        }
    })
}

// 商品活动详情
export function productDesc(obj) {
    return request({
        url: '/pa/detail',
        method: 'post',
        params: obj
    })
}

// 商家活动操作
export function paOptions(obj) {
    return request({
        url: '/pa/opt',
        method: 'post',
        data: obj
    })
}
