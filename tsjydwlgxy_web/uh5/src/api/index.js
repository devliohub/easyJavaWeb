import request from '@/utils/request'

export function getArticle(obj) {
    return request({
        url: '/api/article/list',
        method: 'post',
        params: obj
    })
}
export function getArticleDesc(id) {
    return request({
        url: '/api/article/info?id=' + id,
        method: 'get'
    })
}

export function getKecheng(obj) {
    return request({
        url: '/api/course/list',
        method: 'post',
        params: obj
    })
}

export function getCoursemodules() {
    return request({
        url: '/api/sys/coursemodules',
        method: 'get',
    })
}

export function getCoursetypes() {
    return request({
        url: '/api/sys/coursetypes',
        method: 'get',
    })
}

export function getMenus() {
    return request({
        url: '/api/sys/menus',
        method: 'get',
    })
}

export function getHome() {
    return request({
        url: '/api/home',
        method: 'get',
    })
}