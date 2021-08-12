import request from '@/utils/request'

// 用户首页信息
export function userIndex() {
  return request({
    url: '/u/index',
    method: 'get',
  })
}

// 用户基本信息
export function userInfo(obj) {
  if (obj) {
    return request({
      url: '/u/info',
      method: 'post',
      data: obj
    })
  } else {
    return request({
      url: '/u/info',
      method: 'get',
    })
  }
}

// 用户银行信息
export function ubankInfo(obj) {
  if (obj) {
    return request({
      url: '/u/bank',
      method: 'post',
      data: obj
    })
  } else {
    return request({
      url: '/u/bank',
      method: 'get',
    })
  }
}

// 积分明细
export function scoreList(obj) {
  return request({
    url: '/u/integraldetail',
    method: 'post',
    data: obj
  })
}
// 积分明细下方三数值
export function scoreThree() {
  return request({
    url: '/u/fundsummary',
    method: 'get'
  })
}

// 用户提现 
export function uWithdraw(obj) {
  if (obj) {
    return request({
      url: '/u/withdraw',
      method: 'post',
      data: obj
    })
  } else {
    return request({
      url: '/u/withdraw',
      method: 'get'
    })
  }
}

// 用户资金明细---金额详情
export function transList(obj) {
  return request({
    url: '/u/transactiondetail',
    method: 'post',
    params: obj
  })
}

// 粉丝端实名认证
export function verifyProve(obj) {
  if (obj) {
    return request({
      url: '/u/prove',
      method: 'post',
      data: obj
    })
  } else {
    return request({
      url: '/u/prove',
      method: 'get',
    })
  }
}
