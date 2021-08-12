<template>
  <div class="salerPersonal">
    <!-- 假日通知弹窗 -->
    <el-dialog
      title="通 知"
      custom-class="infoTips"
      :show-close="false"
      :center="true"
      :visible.sync="showInfoTips"
      width="700px"
    >
      <div style="margin: 0">公告：</div>
      <div>
        因近期商家售后渐长，为了提高平台整体工作效率把重心更多放在出单和粉丝管理上面，提高商家单量完成率，现平台拟定商家售后制度方案如下（详情请见商家中心-淘大熊商家注意事项）：
      </div>
      <!-- <div><img src="@/assets/tip_images/datetips.png" alt="" /></div> -->
      
      <div>每周六统计一次商家礼品售后（每周的礼品售后次数/每周的出单量=占比）</div>
      <div>占比最高的暂停任务三天</div>
      <div>如一个月出现2次最高，暂停任务一周</div>
      <div>如一个月出现3次最高，暂停任务一个月</div>

      <div>还望各位商家积极配合，平台的效率提高也才能有更多的精力在出单和粉丝管理上面，该商家售后制度于2020年4月20日开始执行，如有疑问可以联系商服或销售，不便之处请谅解，谢谢~</div>
      <div style="text-align:right">淘小熊 2020.4.14</div>

      <span slot="footer">
        <el-button size="small" @click="showInfoTips = false">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 资金管理弹窗 -->
    <el-dialog
      custom-class="trans"
      :show-close="false"
      :center="true"
      :visible.sync="showTrans"
      width="600px"
    >
      <header slot="title">资金管理</header>
      <el-form v-if="showTrans" :model="tansForm" :rules="transRules" ref="TransForm" size="small">
        <el-row>
          <el-col :span="12">
            <el-form-item label="从" prop="out_money_type">
              <el-select v-model="tansForm.out_money_type" placeholder="请选择">
                <el-option label="流动金额" :value="1" />
                <el-option label="定制金额" :value="2" />
                <!-- <el-option label="积分" :value="3" /> -->
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="转入" prop="in_money_type">
              <el-select v-model="tansForm.in_money_type" placeholder="请选择">
                <el-option label="流动金额" :value="1" />
                <el-option label="定制金额" :value="2" />
                <!-- <el-option label="积分" :value="3" /> -->
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="转移金额(元):" prop="money">
          <el-input-number
            v-model="tansForm.money"
            controls-position="right"
            :min="0"
            :precision="2"
            clearable
          />
        </el-form-item>
      </el-form>

      <span slot="footer">
        <el-button size="small" type="primary" @click="handleTransform()">确 定</el-button>
        <el-button size="small" @click="showTrans = false">取 消</el-button>
      </span>
    </el-dialog>

    <!-- 修改手机号弹窗 -->
    <changeTelDialog
      class="trans"
      v-if="showChangeTel"
      :params="{
        mobile: roleEntity.mobile,
        user: false
      }"
      @cancle="showChangeTel = false"
    />

    <!-- 密码弹窗 -->
    <el-dialog
      custom-class="trans"
      :show-close="false"
      :center="true"
      :visible.sync="showChangePsw"
      width="500px"
    >
      <header slot="title">修改密码</header>
      <el-form
        v-if="showChangePsw"
        :model="PswForm"
        :rules="saleRules"
        ref="PswForm"
        size="small"
        label-width="100px"
      >
        <el-form-item label="原始密码:" prop="old_pwd">
          <el-input v-model="PswForm.old_pwd" clearable />
        </el-form-item>
        <el-form-item label="新登录密码:" prop="new_pwd">
          <el-input v-model="PswForm.new_pwd" clearable />
        </el-form-item>
        <el-form-item label="确认新密码:" prop="re_pwd">
          <el-input v-model="PswForm.re_pwd" clearable />
        </el-form-item>
      </el-form>

      <span slot="footer">
        <el-button size="small" type="primary" @click="handleChangePSW()">确 定</el-button>
        <el-button size="small" @click="showChangePsw = false">取 消</el-button>
      </span>
    </el-dialog>

    <div class="salerPersonal_desc">
      <div class="salerPersonal_desc_left">
        <img
          v-if="roleEntity.avatar"
          :src="roleEntity.avatar"
          alt
          @error="roleEntity.avatar=require('../../../assets/datouxiang.png')"
        />
        <img v-else src="@/assets/datouxiang.png" alt />
        <section>
          <!-- 内容上层 -->
          <ul class="contents" v-loading="isloading">
            <li>
              <span style="font-size:16px;font-weight:bold">{{ roleEntity.account || "-" }}</span>
              <i @click="$router.push('/saler/bindCard')">完善资料</i>
              <i @click="showChangeTel = true">修改手机号码</i>
              <i @click="showChangePsw = true">修改密码</i>
            </li>
            <li>
              <span>流动金：¥ {{ roleEntity.flow_charge }}</span>
              <span>定制金额：¥ {{ roleEntity.make_charge }}</span>
              <!-- <span>积分：{{ roleEntity.integral }}</span> -->
            </li>
            <li>
              <span>服务费：¥ {{ roleEntity.service_charge }}</span>
              <span>待提现金额：¥ {{ roleEntity.wait_money }}</span>
            </li>
            <li>
              <span>服务费预付款：¥ {{ roleEntity.before_service_charge }}</span>
            </li>
          </ul>
          <!-- 按钮下层 -->
          <div class="btns">
            <router-link to="/saler/withDraw">
              <el-button type="primary" size="small">提 现</el-button>
            </router-link>
            <router-link to="/saler/charge">
              <el-button type="success" size="small">充 值</el-button>
            </router-link>
            <el-button class="btn_export" @click="showTrans = true" size="small">资金管理</el-button>
          </div>
        </section>
      </div>

      <div class="salerPersonal_desc_right">
        <header>系统公告</header>
        <ul>
          <li>
            <router-link to="/saler/tips?id=1">节假日放假通知</router-link>
          </li>
          <li>
            <router-link to="/saler/tips?id=2">商家售后教程</router-link>
          </li>
          <li>
            <router-link to="/saler/tips?id=3">商家须知及售后制度</router-link>
          </li>
          <li>
            <router-link to="/saler/tips?id=4">粉丝拉黑制度</router-link>
          </li>
        </ul>
      </div>
    </div>
    <div class="salerPersonal_actions">
      <section>
        <div>我的活动</div>
        <div>已领取的订单（{{ orderNum.received_num }}）</div>
        <div>已下单的订单（{{ orderNum.submitted_num }}）</div>
        <div>待返款的订单（{{ orderNum.pending_review_num }}）</div>
        <div>售后订单（{{ orderNum.after_sale_num }}）</div>
        <div>已完成的订单（{{ orderNum.completed_num }}）</div>
      </section>
    </div>
    <!-- 淘宝头条 -->
    <div class="salerPersonal_invites">
      <div>
        <header>淘宝头条</header>
        <ul>
          <li>
            <a target="_blank" href="https://zhuanlan.zhihu.com/p/59897889">2019全年淘宝活动时间表</a>
          </li>
          <li>
            <a target="_blank" href="http://bbs.paidai.com/topic/1749700">淘宝最新流量爆发规则机制---论推荐式流量</a>
          </li>
          <li>
            <a
              target="_blank"
              href="https://dacu.bbs.taobao.com/detail.html?spm=a2152.7715603.0.0.24a8446a2wgdhR&postId=9370727"
            >2020年淘宝年货节活动招商规则</a>
          </li>
        </ul>
      </div>
      <!-- 店铺运营技巧 -->
      <div style="margin-left:20px">
        <header>店铺运营技巧</header>
        <ul>
          <li>
            <a
              target="_blank"
              href="https://maijia.bbs.taobao.com/detail.html?spm=a210m.7699124.0.0.64421ba2f0W3sC&postId=6594206"
            >新手卖家到底能参加哪些推广活动？</a>
          </li>
          <li>
            <a
              target="_blank"
              href="https://helpcenter.taobao.com/learn/knowledge?version=old&id=1119285&referer=https%3A%2F%2Fservice.taobao.com%2Fsupport%2Fseller%2F1333018051.htm%3Fspm%3Da215a.7391958.1998013685.18.Ro0HjP%26_pvf%3DsellerQuestion"
            >店铺动态评分如何计分的？</a>
          </li>
          <li>
            <a target="_blank" href="http://bbs.paidai.com/topic/1719528">你“被”虚假交易了吗？</a>
          </li>
          <!-- <li>
            <a target="_blank" href="http://bbs.paidai.com/topic/1649548">4. 新手如何去开直通车</a>
          </li>
          <li>
            <a target="_blank" href="http://bbs.paidai.com/topic/1740078">5. 2019年黑搜的秘密都在这</a>
          </li>
          <li>
            <a
              target="_blank"
              href="https://rule.bbs.taobao.com/detail.html?spm=a210m.8146692.0.0.kaX1e6&postId=7235522"
            >6. 宝贝不是你想换，想换就能换</a>
          </li> -->
        </ul>
      </div>
    </div>
    <!-- 平台相关信息 -->
    <div class="salerPersonal_plate_info">
      <header>平台相关信息</header>
      <section>
        <ul style="border-right:1px solid #ccc">
          <li>平台充值时间</li>
          <li>① 周一至周五：早上9:00——下午18:00</li>
          <li>② 周六：早上10:00——下午18:00</li>
          <li>注：周日不审核，如需安排任务，可提前充值安排</li>
        </ul>
        <ul>
          <li>平台审核时间</li>
          <li>① 临时单：早上9:00——下午15:00</li>
          <li>② 计划单：早上9:00——晚上21:00</li>
          <li>注：周日不审核，如需安排，可提前上传任务</li>
        </ul>
      </section>
    </div>
  </div>
</template>
<script>
import changeTelDialog from "@/components/changeTelDialog";
import { mcIndex, moneyTransfer } from "@/api/saler/index";
import { modifypwd } from "@/api/sys";

export default {
  name: "salerPersonal",
  components: { changeTelDialog },
  data() {
    var checkOldPwd = (rule, value, callback) => {
      if (value.length < 6 || value.length > 13) {
        callback(new Error("密码长度不能小于6位或大于13位"));
      } else if (escape(value).indexOf("%u") >= 0) {
        callback(new Error("密码不能有中文"));
      } else {
        callback();
      }
    };
    var validatePass = (rule, value, callback) => {
      if (value.length < 6 || value.length > 13) {
        callback(new Error("密码长度不能小于6位或大于13位"));
      } else if (escape(value).indexOf("%u") >= 0) {
        callback(new Error("密码不能有中文"));
      } else if (value == this.PswForm.old_pwd) {
        callback(new Error("新密码不能与旧密码相同"));
      } else {
        if (this.PswForm.re_pwd !== "") {
          this.$refs.PswForm.validateField("re_pwd");
        }
        callback();
      }
    };
    var validateSamePass = (rule, value, callback) => {
      if (value.length < 6 || value.length > 13) {
        callback(new Error("密码长度不能小于6位或大于13位"));
      } else if (escape(value).indexOf("%u") >= 0) {
        callback(new Error("密码不能有中文"));
      } else if (value !== this.PswForm.new_pwd) {
        callback(new Error("两次输入密码不一致!"));
      } else {
        callback();
      }
    };
    return {
      isloading: false,
      roleEntity: {}, // 角色信息对象
      showInfoTips: true,

      orderNum: {
        received_num: "",
        submitted_num: "",
        pending_review_num: "",
        after_sale_num: "",
        completed_num: ""
      }, // 活动数据

      tansForm: {
        out_money_type: null,
        in_money_type: null,
        money: 0
      },
      PswForm: {
        old_pwd: "",
        new_pwd: "",
        re_pwd: "",
        platform: "2b"
      },
      transRules: {
        money: { required: true, trigger: "change", message: "必填项" },
        out_money_type: {
          required: true,
          trigger: "change",
          message: "必填项"
        },
        in_money_type: { required: true, trigger: "change", message: "必填项" }
      },
      saleRules: {
        old_pwd: { required: true, validator: checkOldPwd, trigger: "blur" },
        new_pwd: { required: true, validator: validatePass, trigger: "blur" },
        re_pwd: {
          required: true,
          validator: validateSamePass,
          trigger: "blur"
        }
      },
      isAllow: true,
      showTrans: false, // 资金管理
      showChangeTel: false, // 电话号码
      showChangePsw: false // 密码
    };
  },
  watch: {
    showTrans(val) {
      if (val)
        this.tansForm = {
          out_money_type: null,
          in_money_type: null,
          money: 0
        };
    },
    showChangePsw(val) {
      if (val)
        this.PswForm = {
          old_pwd: "",
          new_pwd: "",
          re_pwd: "",
          platform: "2b"
        };
    }
  },
  mounted() {
    // this.getInfoData();
    if (this.$store.state.user.name.status == 2) {
      this.getInfoData();
    } else {
      this.$message.error('您的账户权限未开通，请联系您的销售经理或客服开通操作权限')
      this.$router.go(-1)
    }
  },
  methods: {
    async getInfoData() {
      this.isloading = true;
      let res = await mcIndex();
      if (res && res.error.errno == 200) {
        this.roleEntity = res.data;
        this.orderNum = res.data.order_num;
      }
      this.isloading = false;
    },
    async handleChangePSW() {
      this.$refs["PswForm"].validate(async valid => {
        if (valid) {
          let res = await modifypwd(this.PswForm);
          if (res && res.error.errno == 200) {
            this.$message.success("密码修改成功！请重新登录");
            this.$store
              .dispatch("LogOut")
              .then(res => {
                if (res) this.$router.push({ path: "/login" });
              })
              .catch(err => {});
          }
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    handleTransform() {
      this.$refs["TransForm"].validate(async valid => {
        if (valid) {
          if (this.tansForm.money == 0)
            return this.$message.error("请输入转移金额");
          let res = await moneyTransfer(this.tansForm);
          if (res && res.error.errno == 200) {
            this.$message.success('操作成功');
            this.showTrans = false;
            this.getInfoData();
          }
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    }
  }
};
</script>
<style lang="scss" scope>
.salerPersonal {
  position: relative;
  .infoTips {
    .el-dialog__header {
      padding: 20px 20px 10px !important;
    }
    .el-dialog__body > div {
      // padding: 10px 0;
      line-height: 2.5;
      margin-left: 30px;
    }
  }

  &_desc {
    display: flex;
    border-bottom: 1px solid #e5e5e5;
    padding-bottom: 10px;

    &_left {
      display: flex;
      flex: 3;
      border-right: 1px solid #ccc;
      img {
        width: 80px;
        height: 80px;
        margin-right: 20px;
      }
      section {
        width: 100%;
        padding-right: 30px;
        .contents {
          font-size: 14px;
          & > li:first-child {
            display: block;
          }
          & > li {
            display: block;
            margin-bottom: 20px;
            display: flex;
            & > span {
              flex: 1;
            }
            & > i {
              cursor: pointer;
              color: #3bb4f2;
              padding-left: 15px;
              &:hover {
                color: #ff5500;
              }
            }
          }
        }
        .btns {
          display: flex;
          padding-top: 10px;
          border-top: 1px solid #e5e5e5;
          .el-button {
            font-size: 14px;
            margin-right: 30px;
            width: 90px;
          }
        }
      }
    }

    &_right {
      display: flex;
      flex-direction: column;
      font-size: 14px;
      flex: 2;
      padding: 0 30px;
      header {
        font-size: 16px;
        font-weight: bold;
        padding-bottom: 20px;
      }
      & > ul {
        flex: 1;
        display: flex;
        flex-direction: column;
        margin-bottom: 40px;
        & > li {
          flex: 1;
          display: flex;
          justify-content: space-between;
          a {
            font-size: 14px;
            font-weight: normal;
            color: #333;
            &:hover {
              color: #ff5500;
            }
          }
        }
      }
    }
  }

  &_actions {
    margin-top: 30px;
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
    margin-top: 30px;
    display: flex;
    & > div {
      flex: 1;
      border: 1px solid #ccc;
      header {
        font-size: 16px;
        font-weight: bold;
        padding: 10px;
        display: flex;
        justify-content: center;
        border-bottom: 1px solid #ccc;
        & > a {
          font-size: 14px;
          font-weight: normal;
          color: #3bb4f2;
          &:hover {
            color: #ff5500;
          }
        }
      }
      ul {
        width: 100%;
        display: flex;
        flex-direction: column;
        padding: 0 20px;
        li {
          padding: 10px 0;
          border-bottom: 1px dashed #e5e5e5;
          display: flex;
          justify-content: space-between;
          a {
            font-size: 14px;
            font-weight: normal;
            color: #333;
            &:hover {
              color: #ff5500;
            }
          }
          & > span {
            font-size: 12px;
            color: #999;
          }
          &:last-child {
            border-bottom: 0;
          }
        }
      }
    }
  }

  &_plate_info {
    margin: 30px 0;
    border: 1px solid #ccc;
    & > header {
      width: 100%;
      font-size: 16px;
      font-weight: bold;
      padding: 10px;
      display: flex;
      justify-content: center;
      border-bottom: 1px solid #ccc;
    }
    & > section {
      padding: 10px 0;
      display: flex;
      & > ul {
        padding: 0 20px;
        flex: 1;
      }
      & > ul > li {
        padding: 10px 0;
        &:first-child {
          border-bottom: 1px solid #e5e5e5;
          font-weight: bold;
          color: #ff5500;
        }
      }
    }
  }
  // 弹窗样式
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
        width: 140px;
      }
    }
    .el-input-number__decrease,
    .el-input-number__increase {
      display: none;
    }
  }
}
</style>