
import request from '@/utils/request'

// 用户（PC活动列表）
export function order_list(obj) {
  return request({
      url: '/o/orderlist',
      method: 'post',
      data: obj
  })
}

// 用户（PC活动列表）
export function pc_make_order_list(obj) {
  return request({
      url: '/o/makeorderlist',
      method: 'post',
      data: obj
  })
}

// 用户（PC活动列表）
export function order_action(obj) {
  return request({
      url: '/o/orderaction',
      method: 'post',
      data: obj
  })
}


// 用户订单活动数量
export function order_num(obj) {
  return request({
      url: '/o/ordernum',
      method: 'post',
      data: obj
  })
}