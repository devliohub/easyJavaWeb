<template>
  <div class="item_desc">
    <main>
      <breadcrumb />
      <!-- 商品详情(xqg) -->
      <div v-if="entity.module_type == 'xqg'" class="item_desc_main" v-loading="isloading">
        <!-- 多个任务 -->
        <span class="_down_arrow" v-if="entity.task_number > 2" @click="is_hide = !is_hide">
          <i class="el-icon-d-arrow-left" :class="{'rotate90': is_hide}"></i>
        </span>
        <img class="item_desc_main_left" :src="entity.img" alt />
        <!-- 单一任务 -->
        <div class="item_desc_main_mid_xqg" v-if="entity.task_number == 1">
          <div class="single_xqg">
            <div class="single_xqg_title">{{entity.title}}</div>
            <div class="single_xqg_num">
              <span>
                剩余
                <i style="color:#ff5500">{{entity.task_list[0].remain_count}}</i> 份 / 限量
                <i>{{entity.task_list[0].task_count}}</i> 份
              </span>
              <span style="color:#999">浏览次数 {{randomTimes}} 次</span>
            </div>
            <div class="single_xqg_desc">
              <div class="_top">
                <span>
                  最低价：&nbsp;&nbsp;
                  <i style="color:#ff5500">¥ {{entity.task_list[0].current_price}}</i>
                  <i style="text-decoration:line-through">¥ {{entity.task_list[0].price}}</i>
                </span>
                <el-popover placement="right" width="250" trigger="click">
                  <div class="phone_canvas">
                    <span>微信扫一扫</span>
                    <span style="color:red">注：手机端账号和pc端账号须一致(否则，重新登录)</span>
                    <div class="phone_canvas_code">
                      <qrcode-vue
                        style="margin-top:10px"
                        :value="'http://www.taoxiaoxiong.cn/uh5/#/purchase?fromPc=1&t_id=' + entity.task_list[0].t_id"
                        :size="160"
                        level="H"
                      ></qrcode-vue>
                    </div>
                  </div>
                  <el-button slot="reference" type="primary">手机抢</el-button>
                </el-popover>
              </div>
              <div class="_bottom">
                <span>
                  返利&nbsp;
                  <i>¥ {{entity.task_list[0].price - entity.task_list[0].current_price}}</i>
                </span>
                <span>
                  任务金额
                  <i>¥ {{entity.task_list[0].price}}</i>
                </span>
              </div>
            </div>
            <div class="single_xqg_insure">
              <img src="@/assets/components/iconshangjia.png" alt="" />
              商家已存入保证金{{entity.task_list[0].price - entity.task_list[0].current_price}}元，平台担保返款，请放心购买！
            </div>
            <div class="single_xqg_time">
              <img src="@/assets/components/iconhuodong.png" alt="" />
              活动结束时间：{{entity.end_time}}
            </div>
          </div>
        </div>
        <div class="item_desc_main_mid_xqg" v-else :class="{'limit_height': !is_hide}">
          <itemTaskXqg
            v-for="(_entity, x) in entity.task_list"
            :key="x"
            :entity="_entity"
            :title="entity.title"
          />
        </div>
        <div class="item_desc_main_right_xqg">
          <div class="timer" v-if="entity.task_number == 1">{{countDownList}}</div>
          <div class="center">
            <span>微信扫码关注公众号</span>
            <span>
              <qrcode-vue :value="qr.value" :size="qr.size" level="H"></qrcode-vue>
            </span>
            <span>
              关注淘小熊公众号！
              <br />
              <br />开始抢单吧~
            </span>
          </div>
          <div class="insurant">
            <img src="@/assets/components/iconpingtai.png" alt="" />&nbsp;
            <span>平台返款保障</span>
          </div>
        </div>
      </div>
      <!-- 商品详情(free) -->
      <div v-else class="item_desc_main" v-loading="isloading">
        <!-- 多个任务 -->
        <span class="_down_arrow" v-if="entity.task_number > 2" @click="is_hide = !is_hide">
          <i class="el-icon-d-arrow-left" :class="{'rotate90': is_hide}"></i>
        </span>
        <img class="item_desc_main_left" :src="entity.img" alt />
        <div class="item_desc_main_right" :class="{'limit_height': !is_hide}">
          <itemTask
            v-for="(_entity, x) in entity.task_list"
            :key="x"
            :class="{'is_last': ((!is_hide && x == 1) || (is_hide && x == entity.task_number - 1))  }"
            :entity="_entity"
            :title="entity.title"
          />
        </div>
      </div>
      <!-- 温馨提示 -->
      <div class="item_desc_tips">
        <span class="_title">温馨提示</span>
        <div class="item_desc_tips_body">
          <span>
            <i></i>注意事项：
          </span>
          <ol>
            <li>1、与商家旺旺聊天时禁止提及“淘小熊、试客、试单、刷单"等信息，否则取消平台任务合作机会!</li>
            <li>2、禁止使用信用卡、花呗、淘金币、优惠券、红包、天猫积分等淘宝商家使用的官方优惠渠道</li>
            <li>3、禁止通过淘客网、返利网、一淘等返现返利链接下单!</li>
          </ol>* 以上由于买家违规下单所产生的费用，由买家承担。淘小熊有权冻结其帐号，限制提现操作，IP列入黑名单。
        </div>

        <div class="item_desc_tips_body">
          <span>
            <i></i>小提示：
          </span>
          <ol>
            <li>1、用户获取免单资格后，请根据时间提示及时下单并提交订单号。</li>
            <li>2、若因未按时提交任务信息而被系统取消任务资格，平台概不负责!</li>
          </ol>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import breadcrumb from "@/components/breadCrumb.vue";
import itemTask from "@/components/itemTask.vue";
import itemTaskXqg from "@/components/itemTask_xqg.vue";
import { productDesc } from "@/api/common/common.js";
import QrcodeVue from "qrcode.vue";

export default {
  components: { breadcrumb, itemTask, itemTaskXqg, QrcodeVue },
  computed: {
    // 随机生成浏览次数
    randomTimes() {
      return Math.floor(Math.random() * (500 - 100)) + 100;
    }
  },
  data() {
    return {
      isloading: false,
      entity: {},
      is_hide: false,
      countDownList: "活动倒计时：00天 00时 00分 00秒",
      qr: {
        value: "http://www.taoxiaoxiong.cn/uh5/#/index",
        size: 120
      }
    };
  },
  mounted() {
    this.initData();
  },
  methods: {
    async initData() {
      this.isloading = true;
      this.countDown();
      let res = await productDesc({
        pa_id: this.$route.query.pid
      });
      if (res && res.error.errno == 200) {
        this.entity = res.data;
      } else if (res && res.error.errno == 1000) {
        setTimeout(() => {
          this.$router.push("/");
        }, 500);
      }
      this.isloading = false;
    },
    timeFormat(param) {
      return param < 10 ? "0" + param : param;
    },
    countDown() {
      clearInterval(interval);
      var interval = setInterval(() => {
        let obj = null; // 如果活动未结束，对时间进行处理
        // 获取当前时间，同时得到活动结束时间数组
        let newTime = new Date().getTime(); // 对结束时间进行处理渲染到页面
        let endTime = new Date(this.entity.end_time).getTime();
        if (endTime - newTime > 0) {
          let time = (endTime - newTime) / 1000; // 获取天、时、分、秒
          let day = parseInt(time / (60 * 60 * 24));
          let hou = parseInt((time % (60 * 60 * 24)) / 3600);
          let min = parseInt(((time % (60 * 60 * 24)) % 3600) / 60);
          let sec = parseInt(((time % (60 * 60 * 24)) % 3600) % 60);
          obj = {
            day: this.timeFormat(day),
            hou: this.timeFormat(hou),
            min: this.timeFormat(min),
            sec: this.timeFormat(sec)
          };
        } else {
          clearInterval(interval);
          return (this.countDownList = "已过期");
        }
        this.countDownList =
          "活动倒计时：" +
          obj.day +
          "天 " +
          obj.hou +
          "时 " +
          obj.min +
          "分 " +
          obj.sec +
          "秒";
      }, 1000);
    }
  }
};
</script>
<style lang="scss" scope>
.single_xqg {
  padding: 10px 35px;
  & > div {
    margin-bottom: 20px;
  }
  &_title {
    font-size: 16px;
    font-weight: bold;
  }
  &_num {
    color: #666;
    display: flex;
    justify-content: space-between;
    align-items: center;
    i {
      font-weight: bold;
    }
  }
  &_desc {
    border: 1px solid #ccc;
    background: #f6f6f6;
    padding: 0 5px;
    ._top {
      border-bottom: 1px solid #ccc;
      display: flex;
      padding: 15px 5px;
      align-items: center;
      justify-content: space-between;
      & > span {
        font-size: 15px;
        i:first-child {
          font-weight: bold;
          font-size: 20px;
          padding-right: 20px;
        }
      }
      .el-button {
        padding: 10px 30px;
      }
    }
    ._bottom {
      padding: 20px 0;
      display: flex;
      i {
        color: #ff5500;
      }
      & > span {
        padding: 10px 20px;
        flex: 1;
        text-align: center;
      }
      & > span:first-child {
        border-right: 1px solid #ccc;
      }
    }
  }
  &_insure,
  &_time {
    display: flex;
    align-items: center;
    color: #999;
    img {
      width: 15px;
      margin-right: 8px;
    }
  }
}
</style>
<style lang="scss" scoped>
.item_desc {
  width: 100%;
  position: relative;
  main {
    width: 1200px;
    margin: 0 auto;
    .item_desc_main {
      display: flex;
      position: relative;
      &_left {
        width: 360px;
        height: 360px;
      }
      &_mid_xqg {
        padding: 0 30px;
        flex: 1;
        position: relative;
      }
      &_right_xqg {
        width: 300px;
        height: 360px;
        border: 1px solid #ccc;
        display: flex;
        flex-direction: column;
        .timer {
          height: 50px;
          line-height: 50px;
          text-align: center;
          border-bottom: 1px solid #ccc;
          font-weight: bold;
        }
        .center {
          height: 260px;
          display: flex;
          flex-direction: column;
          justify-content: center;
          & > span {
            padding: 10px 0;
            text-align: center;
          }
        }
        .insurant {
          flex: 1;
          display: flex;
          align-items: center;
          justify-content: center;
          border-top: 1px solid #ccc;
          font-size: 16px;
        }
      }
      &_right {
        margin-left: 40px;
        flex: 1;
        display: flex;
        position: relative;
        flex-direction: column;
        .is_last {
          margin-bottom: 15px;
        }
      }
    }
    .item_desc_tips {
      margin: 60px 0;
      padding: 30px;
      border: solid 1px #e5e5e5;
      position: relative;
      // 温馨提示title
      & > ._title {
        position: absolute;
        display: inline-block;
        background: #fff;
        font-size: 18px;
        top: -10px;
        left: 50%;
        width: 90px;
        height: 20px;
        line-height: 20px;
        margin-left: -45px;
        text-align: center;
      }
      &_body {
        margin-bottom: 30px;
        & > span {
          font-weight: bold;
          font-size: 16px;
          margin-bottom: 20px;
          display: inline-block;
          i {
            display: inline-block;
            margin-right: 10px;
            width: 4px;
            height: 14px;
            background: #ff5500;
          }
        }
        & > ol {
          padding-left: 10px;
          li {
            margin-bottom: 15px;
            color: #666;
          }
        }
      }
    }
  }

  .limit_height {
    height: 360px;
    overflow: hidden;
  }
  ._down_arrow {
    border: 1px solid #ff5500;
    position: absolute;
    left: 50%;
    margin-left: -60px;
    bottom: -42px;
    width: 120px;
    height: 40px;
    border-radius: 5px;
    text-align: center;
    background: #ff5500;
    color: #fff;
    transition: 0.2s ease-in-out;
    cursor: pointer;
    z-index: 9999;
    // 旋转类
    .rotate90 {
      transform: rotate(90deg);
    }
    &:hover {
      background: #fff;
      i {
        color: #ff5500;
      }
    }
    i {
      margin-top: 5px;
      font-size: 30px;
      transform: rotate(-90deg);
    }
  }
}
</style>

