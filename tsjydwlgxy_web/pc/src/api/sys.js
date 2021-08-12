import request from '@/utils/request'

export function login(obj) {
    return request({
        url: '/sys/login',
        method: 'post',
        params: obj
    })
}

export function logout() {
    return request({
        url: '/sys/logout',
        method: 'get'
    })
}

// 修改密码
export function modifypwd(obj) {
    return request({
        url: '/sys/modifypwd',
        method: 'post',
        params: obj
    })
}

// 修改手机号
export function modifymobile(obj) {
    return request({
        url: '/sys/modifymobile',
        method: 'post',
        params: obj
    })
}

export function userRegister(obj) {
    return request({
        url: '/u/register',
        method: 'post',
        params: obj
    })
}

// 商家注册
export function salerRegister(obj) {
    return request({
        url: '/mc/register',
        method: 'post',
        params: obj
    })
}

// 发送验证码
export function sendVerify(obj) {
    return request({
        url: '/sys/sendsmsverify',
        method: 'post',
        params: obj
    })
}

// 消息获取
export function getMsg(obj) {
    return request({
        url: '/sys/messagelist',
        method: 'post',
        params: obj
    })
}

// 忘记密码
export function findPwd(obj) {
    return request({
        url: '/sys/Changepwd',
        method: 'post',
        params: obj
    })
}

// 银行支行
export function bankBranch(obj) {
    return request({
        url: '/u/Bankbranchcondition',
        method: 'post',
        params: obj
    })
}

// 银行支行
export function messageNum(obj) {
    return request({
        url: '/sys/Messageunnum',
        method: 'post',
        params: obj
    })
}