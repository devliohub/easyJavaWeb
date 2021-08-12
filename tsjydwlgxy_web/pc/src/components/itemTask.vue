<template>
  <div class="item_task">
    <div class="item_task_main">
      <div class="item_task_main_title">{{title}}</div>
      <div class="item_task_main_4_keys">
        <div style="margin-bottom:12px">
          <span>
            商品价值：
            <i>{{entity.price*entity.order_number}}</i>元
          </span>
          <span>
            剩余份数：
            <i>{{entity.remain_count}}</i>份
          </span>
        </div>
        <div>
          <span>
            任务金额：
            <i>{{entity.price*entity.order_number}}</i>元
          </span>
          <span>
            返还金额：
            <i>{{(entity.price - entity.current_price)*entity.order_number}}</i>元
          </span>
        </div>
      </div>
      <div class="item_task_main_time">
        截止日期：{{ entity.task_end }}
      </div>
      <div class="item_task_main_insure">
        <img src="@/assets/components/iconshangjia.png" alt="" />
        商家已存入保证金{{entity.order_number*entity.price}}元平台担保返款
      </div>
    </div>
    <div class="item_task_btn">
      <el-popover placement="right" width="220" trigger="click">
        <div class="phone_canvas">
          <span>微信扫一扫</span>
          <span style="color:red">注：手机端账号和pc端账号须一致(否则，重新登录)</span>
          <div class="phone_canvas_code">
            <qrcode-vue style="margin-top:10px" :value="qr.value" :size="qr.size" level="H"></qrcode-vue>
          </div>
        </div>
        <el-button slot="reference" type="primary">手机抢</el-button>
      </el-popover>
    </div>
  </div>
</template>
<script>
import QrcodeVue from "qrcode.vue";

export default {
  name: "item_task",
  components: { QrcodeVue },
  props: {
    entity: {
      default: function() {
        return {};
      },
      type: Object
    },
    title: {
      default: "暂无",
      type: String
    }
  },
  data() {
    return {
      qr: {
        value: "http://www.taoxiaoxiong.cn/uh5/#/purchase?fromPc=1&t_id=" + this.entity.t_id,
        size: 160
      }
    };
  },
  mounted() {
    console.log(JSON.parse(JSON.stringify(this.entity)));
  }
};
</script>

<style lang="scss">
.item_task {
  width: 100%;
  padding-bottom: 18px;
  display: flex;
  display: -webkit-box;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px dashed #e5e5e5;
  // 熊抢购
  &_xqg {
    border: 1px solid #ccc;
    &_title {
      margin: 5px 0;
      font-weight: bold;
      font-size: 18px;
    }
    &_context {
      color: #666;
      &_num {
        font-size: 12px;
        margin-bottom: 10px;
        i {
          font-weight: bold;
          font-size: 16px;
        }
      }
      &_price {
        display: flex;
        align-items: center;
        font-size: 14px;
        & > div {
          margin-left: 20px;
          border: 1px solid #ff5500;
          & > span {
            display: inline-block;
            padding: 4px 10px;
          }
          & > span:first-child {
            background: #ff5500;
            color: #fff;
          }
          & > span:last-child {
            background: #fff;
            color: #ff5500;
          }
        }
      }
      &_desc {
        margin-top: 8px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        & > span {
          font-size: 14px;
          i {
            color: #ff5500;
            font-weight: bold;
            font-size: 18px;
          }
        }
      }
    }
    &_time {
      font-size: 12px;
      color: #999;
      margin: 10px 0;
    }
    &_insure {
      color: #999;
      font-size: 12px;
    }
  }
  &_main {
    &_title {
      margin: 10px 0 15px 0;
      font-weight: bold;
      font-size: 18px;
    }
    &_4_keys {
      & > div > span {
        margin-right: 25px;
        i {
          color: #ff5500;
          font-weight: bold;
        }
      }
    }
    &_time {
      display: inline-block;
      padding: 10px;
      margin: 10px 0;
      border: 1px solid #ff5500;
      border-radius: 5px;
    }
    &_insure {
      display: flex;
      align-items: center;
      color: #999;
      font-size: 12px;
      img {
        width: 12px;
        margin-right:5px;
      }
    }
  }
  &_btn {
    .el-button {
      padding: 12px 38px;
    }
  }
}
.phone_canvas {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  & > span {
    text-align: center;
  }
}
</style>
