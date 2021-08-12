import request from '@/utils/request'

// 商家具体信息
export function mcIndex() {
  return request({
    url: '/mc/index',
    method: 'get'
  })
}

// 商家资料接口get 商家资料更新接口post
export function mcInfo(obj) {
  if (obj) {
    return request({
      url: '/mc/info',
      method: 'post',
      params: obj
    })
  } else {
    return request({
      url: '/mc/info',
      method: 'get'
    })
  }
}

// 商家提现管理
export function mcWithdraw(obj) {
  if (obj) {
    return request({
      url: '/mc/withdraw',
      method: 'post',
      params: obj
    })
  } else {
    return request({
      url: '/mc/withdraw',
      method: 'get'
    })
  }
}

// 商家商品列表查询
export function salerProducts(obj) {
  return request({
    url: '/p/index',
    method: 'post',
    data: obj
  })
}

// 商家商品操作
export function salerOption(obj) {
  return request({
    url: '/p/opt',
    method: 'post',
    params: obj
  })
}

// 商家交易记录
export function salerTransaction(obj) {
  return request({
    url: '/mc/transactionrecord',
    method: 'post',
    params: {
      result_type: 1,
      ...obj
    }
  })
}
export function salerTransaction_EX(obj) {
  return request({
    url: '/mc/transactionrecord',
    method: 'post',
    responseType: 'blob',
    params: {
      result_type: 2,
      ...obj
    }
  })
}
// 商家交易记录静态
export function salerTransaction_ST() {
  return request({
    url: '/mc/Assetinfo',
    method: 'get',
  })
}

// 商家订单明细
export function salerOrderDesc(obj) {
  return request({
    url: '/mc/orderdetail',
    method: 'post',
    params: {
      result_type: 1,
      ...obj
    }
  })
}
export function salerOrderDesc_EX(obj) {
  return request({
    url: '/mc/orderdetail',
    method: 'post',
    responseType: 'blob',
    params: {
      result_type: 2,
      ...obj
    }
  })
}

// 商家资金互转
export function moneyTransfer(obj) {
  return request({
    url: '/mc/moneytransfer',
    method: 'post',
    params: obj
  })
}

// 商家充值
export function recharge(obj) {
  if (obj) {
    return request({
      url: '/mc/recharge',
      method: 'post',
      params: obj
    })
  } else {
    return request({
      url: '/mc/recharge',
      method: 'get',
    })
  }
}

export function serviceFee(obj) {
  return request({
    url: '/mc/Servicefee',
    method: 'post',
    params: obj
  })
}