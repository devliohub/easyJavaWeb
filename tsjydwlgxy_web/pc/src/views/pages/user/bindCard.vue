<template>
  <div class="bindCard">
    <el-alert
      style="margin-bottom:10px"
      title="请注意：提现涉及到资金安全问题，请务必核实一下信息，确认无误后保存账户信息，否则，淘小熊概不负责！"
      type="warning"
      show-icon
    ></el-alert>
    <section>
      <header>
        <span></span>用户银行卡信息
      </header>
      <main v-loading="isloading">
        <img
          v-if="entity.avatar"
          :src="entity.avatar"
          alt
          @error="entity.avatar=require('../../../assets/datouxiang.png')"
        />
        <img v-else src="@/assets/datouxiang.png" alt />

        <el-form size="small" label-width="120px" :model="entity" :rules="rules" ref="ref_bindcard">
          <el-form-item label="用户账号">
            <el-input v-model="entity.account" disabled />
          </el-form-item>

          <el-form-item label="联系号码">
            <el-input v-model="entity.mobile" disabled />
          </el-form-item>
          <el-form-item label="真实姓名" v-if="!isSaler">
            <el-input v-model="entity.real_name" disabled />
          </el-form-item>

          <el-form-item label="QQ" prop="qq">
            <el-input v-model="entity.qq" :disabled="!isReadonly && !isSaler" />
          </el-form-item>
          <el-form-item v-if="isSaler" label="微信号" prop="wx">
            <el-input v-model="entity.wx" :disabled="!isReadonly && !isSaler" />
          </el-form-item>
          <el-form-item label="银行卡类型" prop="bank">
            <el-select
              v-model="entity.bank"
              placeholder="请选择银行卡类型"
              :disabled="!isReadonly && !isSaler"
              @change="getBankBranch"
            >
              <el-option
                :label="bank.label"
                :value="bank.value"
                v-for="(bank, i) in bankArr"
                :key="i"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item v-if="isSaler" label="真实姓名" prop="real_name">
            <el-input v-model="entity.real_name" :disabled="!isReadonly && !isSaler" />
          </el-form-item>
          <el-form-item label="开户地区">
            <el-cascader
              v-model="bindCity"
              :options="citys"
              @change="handleCityChange"
              clearable
              :disabled="!isReadonly && !isSaler"
            />
          </el-form-item>
          <el-form-item v-if="!isSaler" label="开户支行" prop="sub_branch_name">
            <el-select
              filterable
              :disabled="!isReadonly"
              v-model="entity.sub_branch_name"
              placeholder="请选择银行卡类型"
              @blur="searchBlur"
            >
              <el-option
                :label="bank"
                :value="bank"
                v-for="(bank, i) in bankBranched"
                :key="i"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="银行卡号" prop="bank_card">
            <el-input v-model="entity.bank_card" :disabled="!isReadonly && !isSaler" />
          </el-form-item>
          <el-form-item label="验证码" prop="sms_code" v-if="(isReadonly && !isSaler) || isSaler">
            <div style="display:flex">
              <el-input style="width:140px;margin-right:10px" v-model="entity.sms_code" />
              <el-button v-if="showCount" type="primary" @click="handleSendVerify">发送验证码</el-button>
              <el-button v-else type="primary">重新发送({{countNum}}s)</el-button>
            </div>
          </el-form-item>
        </el-form>

        <el-button
          v-if="(isReadonly && !isSaler) || isSaler"
          type="primary"
          class="submit_btn"
          size="small"
          @click="handleSubmit"
        >确认提交</el-button>
      </main>
    </section>
  </div>
</template>
<script>
import { ubankInfo } from "@/api/user/index";
import { mcInfo } from "@/api/saler/index";
import { sendVerify, bankBranch } from "@/api/sys";
import citys from "@/assets/citys.js";

export default {
  name: "bindCard",
  data() {
    var validateBank = (rule, value, callback) => {
      if (value == "") {
        callback(new Error("请输入银行卡号"));
      } else if (value.length != 16 && value.length != 19) {
        callback(new Error("请输入正确银行卡号格式"));
      } else {
        callback();
      }
    };
    return {
      timer: null,
      countNum: "", // 倒计时
      showCount: true,

      entity: {},
      bindCity: [], // 省市级联
      isloading: false,
      bankBranched: [], // 支行数据
      bankArr: [
        { value: "中国建设银行", label: "中国建设银行" },
        { value: "中国农业银行", label: "中国农业银行" },
        { value: "中国工商银行", label: "中国工商银行" },
        { value: "中国银行", label: "中国银行" },
        { value: "交通银行", label: "交通银行" },
        { value: "招商银行", label: "招商银行" }
      ],
      rules: {
        bank: { required: true, message: "请输入银行卡类型", trigger: "blur" },
        bank_card: {
          required: true,
          trigger: ["blur", "change"],
          validator: validateBank
        },
        sms_code: [
          { required: true, trigger: "blur", message: "请填写六位验证码" },
          { min: 6, max: 6, message: "请填写六位验证码", trigger: "blur" }
        ]
      }
    };
  },
  computed: {
    isSaler() {
      return this.$route.name == "salerBindCard";
    },
    citys() {
      return citys || [];
    },
    // 已经提交的状态只读
    isReadonly() {
      return this.entity.is_submit;
    }
  },
  mounted() {
    this.initData();
  },
  watch: {
    entity: {
      handler: async function(newval, oldval) {
        if (newval.bank && newval.regist_city && newval.regist_province) {
        }
      },
      deep: true
    }
  },
  methods: {
    // 倒计时计算
    countVerifyNum() {
      if (!this.timer) {
        this.countNum = 60;
        this.showCount = false;
        this.timer = setInterval(() => {
          if (this.countNum > 1 && this.countNum <= 60) {
            this.countNum--;
          } else {
            this.showCount = true;
            clearInterval(this.timer);
            this.timer = null;
          }
        }, 1000);
      }
    },
    async initData() {
      let res;
      this.isloading = true;
      if (this.isSaler) {
        res = await mcInfo();
        if (res && res.error.errno == 200) {
          this.entity = res.data;
          this.bindCity = [res.data.regist_province, res.data.regist_city];
        }
      } else {
        res = await ubankInfo();
        if (res && res.error.errno == 200) {
          this.entity = res.data;
          this.bindCity = [res.data.regist_province, res.data.regist_city];
          if (!this.entity.is_submit) {
            this.$notify.warning({
              message:
                "您的账户已绑定银行卡，如需更改银行卡信息，请联系客服处理！",
              position: "bottom-right",
              duration: 3000,
              offset: 150
            });
          }
        }
      }
      this.isloading = false;
    },
    // 省市级联变化
    handleCityChange(val) {
      if (val && val.length > 0) {
        this.entity.regist_province = val[0];
        this.entity.regist_city = val[1] ? val[1] : val[0];
      } else {
        this.entity.regist_province = "";
        this.entity.regist_city = "";
      }
      this.getBankBranch()
    },
    // 通过省市 + 银行卡 获取支行
    async getBankBranch() {
      if(this.entity.regist_province && this.entity.regist_city && this.entity.bank) {
        let res = await bankBranch({
          province: this.entity.regist_province,
          city: this.entity.regist_city,
          bank_name: this.entity.bank
        });
        if(res && res.error.errno == 200) this.bankBranched = res.data
      }
    },
    // el-select可输入
    searchBlur(e) {
      this.entity.sub_branch_name = e.target.value
    },
    // 发送验证码功能
    async handleSendVerify() {
      if (this.entity.mobile) {
        let queryObj = {
          mobile: this.entity.mobile,
          platform: this.isSaler ? "2b" : "2c",
          is_repeat: "false",
          type: 4
        };
        let res = await sendVerify(queryObj);
        if (res && res.error.errno == 200) {
          this.$message.success("验证码发送成功！请在手机上查看");
          this.countVerifyNum();
        }
      }
    },
    // 提交按钮
    async handleSubmit() {
      this.$refs["ref_bindcard"].validate(async valid => {
        if (valid) {
          this.isloading = true;
          let res;
          if (this.isSaler) {
            res = await mcInfo(this.entity);
            if (res && res.error.errno == 200) {
              this.$message.success("更新成功！");
              this.initData();
            }
          } else {
            res = await ubankInfo(this.entity);
            if (res && res.error.errno == 200) {
              this.$message.success("更新成功！");
              this.initData();
            }
          }
          this.isloading = false;
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
.bindCard {
  section {
    width: 100%;
    & > header {
      width: 100%;
      font-size: 16px;
      border-bottom: 1px solid #e5e5e5;
      padding: 12px 0;
      font-weight: bold;
      span {
        display: inline-block;
        vertical-align: middle;
        width: 4px;
        height: 18px;
        background: #ff5500;
        margin-right: 12px;
      }
    }
    & > main {
      display: flex;
      align-items: center;
      justify-content: center;
      flex-direction: column;
      & > img {
        margin: 20px 0;
        width: 80px;
        height: 80px;
      }
    }
  }

  .el-form-item {
    padding-right: 120px;
    .el-form-item__label {
      font-weight: normal;
      color: #666;
    }
  }
}
</style>