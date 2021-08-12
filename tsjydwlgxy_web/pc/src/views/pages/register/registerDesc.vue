<template>
  <div class="registerDesc">
    <main>
      <template v-if="isUser">
        <header>
          <span style="border-bottom:2px solid #ff464f">用户注册</span>
        </header>
        <section>
          <el-form
            :model="userForm"
            :rules="userRules"
            ref="userForm"
            label-width="120px"
            size="small"
          >
            <el-form-item label="用户名" prop="account">
              <el-input v-model.trim="userForm.account" clearable />
            </el-form-item>
            <el-form-item label="设置登录密码" prop="pwd">
              <el-input type="text" v-model.trim="userForm.pwd" clearable />
            </el-form-item>
            <el-form-item label="重复登录密码" prop="re_password">
              <el-input type="text" v-model.trim="userForm.re_password" clearable />
            </el-form-item>
            <el-form-item label="手机号" prop="mobile">
              <el-input v-model.trim="userForm.mobile" clearable />
            </el-form-item>
            <el-form-item label="验证码" prop="sms_code">
              <div class="identy_code">
                <el-input v-model.trim="userForm.sms_code" clearable />&nbsp;&nbsp;&nbsp;
                <el-button v-if="showCount" type="primary" @click="sendVerify">发送验证码</el-button>
                <el-button v-else type="primary">重新发送({{countNum}}s)</el-button>
              </div>
            </el-form-item>
            <el-form-item label="QQ号" prop="qq">
              <el-input v-model.trim="userForm.qq" clearable />
            </el-form-item>
            <el-form-item label="微信号" prop="wx">
              <el-input v-model.trim="userForm.wx" clearable />
            </el-form-item>
            <el-form-item label="邀请码" prop="invite_code">
              <el-input v-model.trim="userForm.invite_code" clearable />
            </el-form-item>
            <div class="insure_text">
              <el-checkbox v-model="is_accept"></el-checkbox>
              <span>
                我已仔细阅读并接受
                <router-link to="/agreement">《淘小熊服务协议》</router-link>
              </span>
            </div>
            <el-form-item>
              <el-button type="primary" @click="submitForm('userForm')" :disabled="!is_accept">提 交</el-button>
              <router-link to="/login?type=user" style="margin-left:35px">
                我已注册，现在就
                <el-button size="mini" type="primary" plain>登录</el-button>
              </router-link>
            </el-form-item>
          </el-form>
        </section>
      </template>

      <template v-else>
        <header>
          <span style="border-bottom:2px solid #ff5500">商家注册</span>
        </header>
        <section>
          <el-form
            :model="saleForm"
            :rules="saleRules"
            ref="saleForm"
            label-width="120px"
            size="small"
          >
            <el-form-item label="用户名" prop="account">
              <el-input v-model.trim="saleForm.account" clearable />
            </el-form-item>
            <el-form-item label="设置登录密码" prop="password">
              <el-input v-model.trim="saleForm.password" clearable />
            </el-form-item>
            <el-form-item label="重复登录密码" prop="re_password">
              <el-input v-model.trim="saleForm.re_password" clearable />
            </el-form-item>
            <el-form-item label="手机号码" prop="mobile">
              <el-input v-model.trim="saleForm.mobile" clearable />
            </el-form-item>
            <el-form-item label="验证码" prop="code">
              <div class="identy_code">
                <el-input v-model.trim="saleForm.code" clearable />&nbsp;&nbsp;&nbsp;
                <el-button v-if="showCount" type="primary" @click="sendVerify">发送验证码</el-button>
                <el-button v-else type="primary">重新发送({{countNum}}s)</el-button>
              </div>
            </el-form-item>
            <el-form-item label="QQ号" prop="qq">
              <el-input v-model.trim="saleForm.qq" clearable />
            </el-form-item>
            <el-form-item label="微信号" prop="wx">
              <el-input v-model.trim="saleForm.wx" clearable />
            </el-form-item>
            <el-form-item label="对接销售" prop="sell_user">
              <el-input v-model.trim="saleForm.sell_user" clearable />
            </el-form-item>
            <el-form-item label="邀请码" prop="invite_code">
              <el-input v-model.trim="saleForm.invite_code" clearable />
            </el-form-item>
            <div class="insure_text">
              <el-checkbox v-model="is_accept"></el-checkbox>
              <span>
                我已仔细阅读并接受
                <router-link to="/agreement">《淘小熊服务协议》</router-link>
              </span>
            </div>
            <el-form-item>
              <el-button type="primary" @click="submitForm('saleForm')" :disabled="!is_accept">提 交</el-button>
              <router-link to="/login?type=saler" style="margin-left:35px">
                我已注册，现在就
                <el-button size="mini" type="primary" plain>登录</el-button>
              </router-link>
            </el-form-item>
          </el-form>
        </section>
      </template>
    </main>
  </div>
</template>

<script>
import { userRegister, salerRegister, sendVerify } from "@/api/sys";
import { validateMobile, validateAccount } from "@/utils/validate.js";

export default {
  name: "registerDesc",
  components: {},
  data() {
    var validatePass = (rule, value, callback) => {
      if (value.length < 6 || value.length > 13) {
        callback(new Error("密码长度不能小于6位或大于13位"));
      } else if (escape(value).indexOf("%u") >= 0) {
        callback(new Error("密码不能有中文"));
      } else {
        if (this.isUser) {
          if (this.userForm.re_password !== "") {
            this.$refs.userForm.validateField("re_password");
          }
          callback();
        } else {
          if (this.saleForm.re_password !== "") {
            this.$refs.saleForm.validateField("re_password");
          }
          callback();
        }
      }
    };
    var validateSamePass = (rule, value, callback) => {
      if (value.length < 6 || value.length > 13) {
        callback(new Error("密码长度不能小于6位或大于13位"));
      } else if (escape(value).indexOf("%u") >= 0) {
        callback(new Error("密码不能有中文"));
      } else {
        if (this.isUser) {
          if (value !== this.userForm.pwd) {
            callback(new Error("两次输入密码不一致!"));
          } else {
            callback();
          }
        } else {
          if (value !== this.saleForm.password) {
            callback(new Error("两次输入密码不一致!"));
          } else {
            callback();
          }
        }
      }
    };
    return {
      timer: null,
      countNum: '', // 倒计时
      showCount: true,

      userForm: {
        account: "",
        mobile: "",
        pwd: "",
        re_password: "",
        sms_code: "",
        qq: "",
        wx: "",
        invite_code: window.localStorage.getItem('invite_code')
      },
      saleForm: {
        account: "",
        password: "",
        re_password: "",
        mobile: "",
        code: "",
        qq: "",
        wx: "",
        sell_user: "",
        invite_code: ""
      },
      is_accept: true, // 是否接受条令
      userRules: {
        account: { required: true, validator: validateAccount, trigger: "blur" },
        mobile: { required: true, validator: validateMobile, trigger: "blur" },
        pwd: { required: true, validator: validatePass, trigger: "blur" },
        re_password: {
          required: true,
          validator: validateSamePass,
          trigger: "blur"
        },
        sms_code: [{ required: true, trigger: "blur", message: "请填写六位验证码" }, { min: 6, max: 6, message: '请填写六位验证码', trigger: 'blur' }],
        qq: { required: true, message: "请输入QQ号", trigger: "blur" },
        wx: { required: true, message: "请输入微信号", trigger: "blur" },
        invite_code: {
          required: true,
          message: "请输入邀请码",
          trigger: "blur"
        }
      },
      saleRules: {
        account: { required: true, validator: validateAccount, trigger: "blur" },
        password: { required: true, validator: validatePass, trigger: "blur" },
        re_password: {
          required: true,
          validator: validateSamePass,
          trigger: "blur"
        },
        mobile: { required: true, validator: validateMobile, trigger: "blur" },
        code: [{ required: true, trigger: "blur", message: "请填写六位验证码" }, { min: 6, max: 6, message: '请填写六位验证码', trigger: 'blur' }],
        qq: { required: true, message: "请输入QQ号", trigger: "blur" },
        wx: { required: true, message: "请输入微信号", trigger: "blur" }
      }
    };
  },
  computed: {
    isUser() {
      return this.$route.query.type == "user";
    }
  },
  mounted() {},
  methods: {
    // 倒计时计算
    countVerifyNum() {
      if(!this.timer) {
        this.countNum = 60
        this.showCount = false
        this.timer = setInterval(() => {
          if(this.countNum > 1 && this.countNum <= 60) {
            this.countNum--;
          } else {
            this.showCount = true;
            clearInterval(this.timer)
            this.timer = null
          }
        }, 1000);
      }
    },
    async sendVerify() {
      let res;
      if (this.isUser) {
        this.$refs.userForm.validateField("mobile");
        if (this.userForm.mobile) {
          let queryObj = {
            mobile: this.userForm.mobile,
            platform: "2c",
            type: 2
          };
          res = await sendVerify(queryObj);
        }
      } else {
        this.$refs.saleForm.validateField("mobile");
        if (this.saleForm.mobile) {
          let queryObj = {
            mobile: this.saleForm.mobile,
            platform: "2b",
            type: 2
          };
          res = await sendVerify(queryObj);
        }
      }
      if (res && res.error.errno == 200) {
        this.countVerifyNum()
        this.$message.success("验证码发送成功！请在手机上查看");
      }
    },
    submitForm(formName) {
      this.$refs[formName].validate(async valid => {
        if (valid) {
          if (this.is_accept) {
            let res;
            if (this.isUser) {
              res = await userRegister(this.userForm);
              if (res && res.error.errno == 200) {
                this.$message.success("用户注册成功！");
                setTimeout(() => {
                  window.localStorage.removeItem('invite_code')
                  this.$router.push("/login?type=user");
                }, 500);
              }
            } else {
              res = await salerRegister(this.saleForm);
              if (res && res.error.errno == 200) {
                this.$message.success("商家注册成功！");
                setTimeout(() => {
                  this.$router.push("/login");
                }, 500);
              }
            }
          } else {
            return this.$message.warning("仔细阅读并接受《用户协议》");
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
.registerDesc {
  width: 100%;
  main {
    width: 1140px;
    margin: 30px auto;
    padding: 30px;
    box-shadow: 0px 0px 35px 11px rgba(255, 70, 78, 0.06);
    & > header {
      padding-bottom: 12px;
      border-bottom: 1px solid #e5e5e5;
      span {
        font-size: 16px;
        font-weight: bold;
        padding-bottom: 10px;
      }
    }

    & > section {
      width: 500px;
      margin-top: 30px;
      // 用户协议
      .insure_text {
        margin: 0 0 20px 115px;
        display: flex;
        align-items: center;
        span {
          font-size: 13px;
          padding-left: 5px;
        }
        a {
          color: #409eff;
          &:hover {
            text-decoration: underline;
          }
        }
      }
      // 验证码
      .identy_code {
        display: flex;
      }
    }
  }
  .el-form-item__label {
    font-weight: normal;
    font-size: 14px;
    color: #666;
  }
}
</style>
