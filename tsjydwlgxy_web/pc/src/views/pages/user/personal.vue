<template>
  <div class="personal">
    <el-dialog :visible.sync="dialogVisible" width="300px">
      <qrcode-vue style="margin-top:10px" :value="qrcode" :size="200" level="H"></qrcode-vue>
    </el-dialog>
    <!-- 修改手机号弹窗 -->
    <changeTelDialog
      class="trans"
      v-if="showChangeTel"
      :params="{
        mobile: entity.mobile,
        user: true
      }"
      @cancle="showChangeTel = false"
    />

    <div class="personal_desc" v-loading="isloading_top">
      <div class="personal_desc_left">
        <img
          v-if="entity.avatar"
          :src="entity.avatar"
          alt
          @error="entity.avatar=require('../../../assets/datouxiang.png')"
        />
        <img v-else src="@/assets/datouxiang.png" alt />

        <section>
          <span>
            {{entity.account || entity.mobile}}&nbsp;&nbsp;
            <router-link to="/user/basicInfo">个人信息</router-link>&nbsp;&nbsp;&nbsp;
            <router-link to @click.native="showChangeTel = true">修改手机号</router-link>
          </span>
          <span>邀请码: <i>{{$store.state.user.name.invite_code || '-'}}</i></span>
          <span>
            实名认证:
            <div class="verify_icon type_success" v-if="entity.verified_status == 3" @click="$router.push('/user/verify')">
              <i class="el-icon-success"></i>
              已通过实名认证
            </div>
            <div class="verify_icon type_warning" v-else-if="entity.verified_status == 2" @click="$router.push('/user/kefuInfo')">
              <i class="el-icon-warning"></i>
              实名待审核通过
            </div>
            <div class="verify_icon type_error" v-else @click="$router.push('/user/verify')">
              <i class="el-icon-error"></i>
              未通过实名认证
            </div>
          </span>
        </section>
      </div>
      <div class="personal_desc_mid">
        <span style="padding:7px 0">
          手机号码：
          <i>{{entity.mobile}}</i>
        </span>
        <span style="padding:7px 0">
          联系Q Q：
          <i>{{entity.qq}}</i>
        </span>
        <span class="line"></span>
        <span>
          账户余额：
          <i>￥{{entity.balance}}</i> &nbsp;&nbsp;
          <el-button type="success" size="mini" @click="$router.push('/user/withDraw')">提现</el-button>
        </span>
        <span style="padding:7px 0">
          待提金额：
          <i>￥{{entity.amount_raised}}</i>
        </span>
        <!-- <span style="padding:7px 0">
          我的积分：
          <i>{{entity.integral}}</i>
        </span> -->
      </div>
      <div class="personal_desc_right">
        <img src="@/assets/nodifferent.png" alt />
      </div>
    </div>
    <!-- 进行中的活动 -->
    <div class="personal_actions">
      <section>
        <div>我的活动</div>
        <div>已领取的订单（{{activity_num.received_num}}）</div>
        <div>已提交的订单（{{activity_num.submitted_num}}）</div>
        <div>待审核的订单（{{activity_num.pending_review_num}}）</div>
        <div>售后订单（{{activity_num.after_sale_num}}）</div>
        <div>已完成的订单（{{activity_num.completed_num}}）</div>
      </section>
    </div>
    <!-- 邀请记录 -->
    <div class="personal_invites">
      <div class="personal_invites_record">
        <header>邀请记录</header>
        <section>
          <div>
            <div class="title">我的粉丝</div>
            <div class="content">{{entity.fan_num}}人</div>
          </div>
          <div>
            <div class="title">累计奖励</div>
            <div class="content">-</div>
          </div>
          <div>
            <div class="title">分享好友</div>
            <div class="content">
              <el-button type="success" size="mini" @click="copyInviteUrl">复制邀请链接</el-button>
              <div id="_blank">
                <input id="_inp" v-model="copycode" type="text" />
              </div>
              <el-button type="primary" size="mini" @click="createQRcode">生成推荐二维码</el-button>
            </div>
          </div>
        </section>
      </div>
      <div class="personal_invites_record" style="margin-left:20px">
        <header>
          邀请奖励排行
          <router-link to>邀请秘籍</router-link>
        </header>
        <section>
          <div>
            <div class="title">昵称</div>
            <div class="content"></div>
          </div>
          <div>
            <div class="title">邀请人数</div>
            <div class="content"></div>
          </div>
          <div>
            <div class="title">邀请奖励</div>
            <div class="content"></div>
          </div>
        </section>
      </div>
    </div>
    <div class="personal_items">
      <header>
        <div>
          <span></span>推荐限量免单产品
        </div>
        <router-link to="/">更多推荐</router-link>
      </header>
      <ul v-loading="isloading_goods">
        <li v-for="(item, x) in goodList" :key="x" :class="{'borderLeft': x%6 == 0}">
          <img
            class="img"
            :src="item.img"
            alt
            @click="$router.push('/index/itemDesc?pid=' + item.pa_id)"
          />
          <div class="content">
            <div class="content_title">{{item.title}}</div>
            <div class="content_desc">
              <span class="price">￥{{item.price}}</span>
              <span class="amount">
                限量
                <i>{{item.task_count}}</i>份
              </span>
            </div>
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>
<script>
import changeTelDialog from "@/components/changeTelDialog";
import { userIndex } from "@/api/user/index";
import { indexSearch } from "@/api/common/common";
import QrcodeVue from "qrcode.vue";

export default {
  name: "personal",
  components: { QrcodeVue, changeTelDialog },
  data() {
    return {
      dialogVisible: false,
      isloading_top: false,
      isloading_goods: false,
      showChangeTel: false,

      entity: {},
      activity_num: {},
      goodList: []
    };
  },
  mounted() {
    this.getInfo();
    this.getGoods();
  },
  computed: {
    qrcode() {
      let _code = this.$store.state.user.name.invite_code;
      if (_code) return "http://www.taoxiaoxiong.cn/uh5/#/register?code=" + _code;
    },
    copycode() {
      let _code = this.$store.state.user.name.invite_code;
      return "http://www.taoxiaoxiong.cn/bmc/#/registerDesc?code=" + _code;
    }
  },
  methods: {
    // 上方信息
    async getInfo() {
      this.isloading_top = true;
      let res = await userIndex();
      if (res && res.error.errno == 200) {
        this.entity = res.data;
        this.activity_num = {
          after_sale_num:
            res.data.order_num.free.after_sale_num +
            res.data.order_num.xqg.after_sale_num,
          completed_num:
            res.data.order_num.free.completed_num +
            res.data.order_num.xqg.completed_num,
          pending_review_num:
            res.data.order_num.free.pending_review_num +
            res.data.order_num.xqg.pending_review_num,
          received_num:
            res.data.order_num.free.received_num +
            res.data.order_num.xqg.received_num,
          submitted_num:
            res.data.order_num.free.submitted_num +
            res.data.order_num.xqg.submitted_num
        };
      }
      this.isloading_top = false;
    },
    // 下方商品列表
    async getGoods() {
      this.isloading_goods = true
      let res = await indexSearch({
        type: 2,
        module_type: 1,
        page_no: 1,
        page_size: 12
      });
      if (res && res.error.errno == 200) this.goodList = res.data;
      this.isloading_goods = false
    },
    // 复制邀请链接码
    copyInviteUrl() {
      let _inp = document.querySelector("#_inp");
      _inp.select();
      _inp.focus();
      document.execCommand("copy", false, null);
      this.$message.success("成功复制链接到剪贴板！");
    },
    // 生成推荐二维码
    createQRcode() {
      this.dialogVisible = true;
    }
  }
};
</script>
<style lang="scss" scope>
.personal {
  &_desc {
    display: flex;
    padding-bottom: 10px;
    border-bottom: 1px solid #e5e5e5;

    &_left {
      display: flex;
      flex: 3;
      img {
        width: 80px;
        height: 80px;
        margin-right: 20px;
      }
      section {
        & > span {
          font-size: 16px;
          font-weight: bold;
          display: block;
          height: 30px;
          line-height: 30px;
          padding: 10px 0;
          & > a {
            font-size: 14px;
            font-weight: normal;
            color: #3bb4f2;
            &:hover {
              color: #ff5500;
            }
          }
          & > i {
            font-size: 14px;
          }
          .verify_icon {
            display: inline-block;
            padding: 0 5px;
            font-size: 12px;
            margin-left: 5px;
            cursor: pointer;
          }
          .type_success {
            border: solid 1px #61bf57;
            color: #24a617;
          }
          .type_warning {
            border: solid 1px #E6A23C;
            color: #E6A23C;
          }
          .type_error {
            border: solid 1px red;
            color: red;
          }
        }
      }
    }
    &_mid {
      display: flex;
      flex-direction: column;
      font-size: 14px;
      flex: 2;
      .line {
        width: 100%;
        height: 1px;
        margin: 6px 0;
        background: #e5e5e5;
      }
    }
    &_right {
      width: 250px;
      display: flex;
      margin-left: 30px;
      img {
        width: 220px;
        height: 70px;
      }
    }
  }

  &_actions {
    margin-top: 20px;
    border: 1px solid #ccc;
    section {
      display: flex;
      & > div {
        cursor: pointer;
        padding: 15px 0;
        flex: 1;
        text-align: center;
        border-right: 1px solid #ccc;
        &:first-child {
          font-weight: bold;
          font-size: 16px;
        }
        &:last-child {
          border-right: none;
        }
        span {
          display: inline-block;
          font-size: 12px;
          color: #999;
          padding-top: 5px;
        }
      }
    }
  }

  &_invites {
    margin-top: 20px;
    display: flex;
    & > div {
      border: 1px solid #ccc;
      header {
        font-size: 16px;
        font-weight: bold;
        padding: 10px;
        display: flex;
        justify-content: space-between;
        & > a {
          font-size: 14px;
          font-weight: normal;
          color: #3bb4f2;
          &:hover {
            color: #ff5500;
          }
        }
      }
      section {
        font-size: 14px;
      }
    }

    &_record {
      flex: 1;
      section {
        display: flex;
        & > div {
          flex: 1;
          border-right: 1px solid #ccc;
          border-top: 1px solid #ccc;
          text-align: center;
          display: flex;
          flex-direction: column;
          .title {
            height: 30px;
            line-height: 30px;
            border-bottom: 1px solid #ccc;
          }
          .content {
            height: 80px;
            line-height: 80px;
            color: #999;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            #_blank {
              position: relative;
              background: #fff;
              width: 116px;
              height: 10px;
            }
            #_inp {
              position: absolute;
              z-index: -1;
              width: 10px;
              height: 1px;
            }
            & > .el-button {
              width: 116px;
            }
          }
          &:last-child {
            border-right: none;
          }
        }
      }
    }
  }

  &_items {
    margin: 20px 0;
    & > header {
      width: 100%;
      font-size: 16px;
      padding: 12px 0;
      font-weight: bold;
      display: flex;
      justify-content: space-between;
      & > div > span {
        display: inline-block;
        vertical-align: middle;
        width: 4px;
        height: 18px;
        background: #ff5500;
        margin-right: 12px;
      }
      & > a {
        font-size: 14px;
        font-weight: normal;
        color: #3bb4f2;
        &:hover {
          color: #ff5500;
        }
      }
    }
    & > ul {
      overflow: hidden;
      // 每行第一个
      .borderLeft {
        border-left: 1px solid #ccc;
      }
      li {
        float: left;
        border: 1px solid #ccc;
        border-left: none;
        padding: 20px;
        margin-bottom: 20px;
        .img {
          &:hover {
            opacity: 0.8;
          }
          cursor: pointer;
          width: 120px;
          height: 120px;
        }
        .content {
          &_title {
            padding: 10px 0;
            max-width: 120px;
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
            font-size: 14px;
          }
          &_desc {
            display: flex;
            justify-content: space-between;
            align-items: center;
            .price {
              font-size: 16px;
              color: #ff5500;
            }
            .amount {
              font-size: 12px;
              color: #666;
              i {
                color: #ff5500;
                font-weight: bold;
              }
            }
          }
        }
      }
    }
  }
  .trans {
    header {
      width: 100%;
      height: 50px;
      line-height: 50px;
      font-weight: bold;
      font-size: 16px;
      border-bottom: 1px solid #ccc;
    }
    .el-form-item {
      .el-select,
      .el-input {
        width: 130px;
      }
    }
  }
}
</style>