import request from '@/utils/request'

export function getArticle(obj) {
    return request({
        url: '/article/list',
        method: 'post',
        params: obj
    })
}
export function getArticleDesc(id) {
    return request({
        url: '/article/info?id=' + id,
        method: 'get'
    })
}

export function getKecheng(obj) {
    return request({
        url: '/course/list',
        method: 'post',
        params: obj
    })
}

export function getCoursemodules() {
    return request({
        url: '/sys/coursemodules',
        method: 'get',
    })
}

export function getCoursetypes() {
    return request({
        url: '/sys/coursetypes',
        method: 'get',
    })
}

export function getMenus() {
    return request({
        url: '/sys/menus',
        method: 'get',
    })
}