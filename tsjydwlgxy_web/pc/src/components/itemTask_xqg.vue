<template>
  <div class="item_task_xqg">
    <div class="item_task_xqg_left">
      <div class="item_task_xqg_title">{{title}}</div>
      <div class="item_task_xqg_context">
        <div class="item_task_xqg_context_num">
          剩余
          <i style="color: #ff5500">{{entity.remain_count}}</i>
          份/
          限量
          <i>{{entity.task_count}}</i> 份
        </div>
        <div class="item_task_xqg_context_price">
          任务金额:&nbsp;&nbsp;¥ {{entity.price}}
          <div>
            <span>返利</span>
            <span>¥{{(entity.price - entity.current_price).toFixed(2)}}</span>
          </div>
        </div>
        <div class="item_task_xqg_context_desc">
          <span>
            最低价:&nbsp;
            <i>¥ {{entity.current_price}}</i>
          </span>
        </div>
      </div>
      <div class="item_task_xqg_time">
        <img src="@/assets/components/iconhuodong.png" alt="" />
        截止日期：{{ entity.task_end }}
      </div>
      <div class="item_task_xqg_insure">
        <img src="@/assets/components/iconshangjia.png" alt="" />
        商家已存入保证金{{(entity.price - entity.current_price).toFixed(2)}}元平台担保返款</div>
    </div>

    <div class="item_task_xqg_right">
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
  name: "item_task_xqg",
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
.item_task_xqg {
  width: 100%;
  display: flex;
  justify-content: space-between;
  padding: 10px;
  background: #f6f6f6;
  margin-bottom: 10px;
  &_left {
    .item_task_xqg_title {
      margin: 0 0 10px 0;
      font-weight: bold;
      font-size: 18px;
    }
    .item_task_xqg_context {
      color: #666;
      &_num {
        font-size: 12px;
        margin-bottom: 10px;
        i {
          font-weight: bold;
          font-size: 16px;
        }
      }
      .item_task_xqg_price {
        display: flex;
        align-items: center;
        font-size: 14px;
        & > div {
          margin-left: 20px;
          border: 1px solid #ff5500;
          & > span {
            display: inline-block;
            padding: 4px 12px;
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
      .item_task_xqg_desc {
        margin-top: 8px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        & > span {
          font-size: 14px;
          i {
            color: #ff5500;
            font-weight: bold;
            font-size: 20px;
          }
        }
      }
    }
    .item_task_xqg_time, .item_task_xqg_insure {
      display: flex;
      align-items: center;
      img {
        width: 12px;
        margin-right: 8px;
      }
    }
  }
  &_right {
    .el-button {
      padding: 10px 30px;
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
