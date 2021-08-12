<template>
  <div class="findPwd">
    <main>
      <template v-if="isUser">
        <header>
          <span style="border-bottom:2px solid #ff464f">用户找回密码</span>
        </header>
        <section>
          <el-form
            :model="userForm"
            :rules="userRules"
            ref="userForm"
            label-width="120px"
            size="small"
          >
            <el-form-item label="用户名/手机号" prop="mobile">
              <el-input v-model.trim="userForm.mobile" clearable />
            </el-form-item>
            <el-form-item label="验证码" prop="sms_code">
              <div class="identy_code">
                <el-input v-model.trim="userForm.sms_code" clearable />&nbsp;&nbsp;&nbsp;
                <el-button v-if="showCount" type="primary" @click="handleSendVerify">发送验证码</el-button>
                <el-button v-else type="primary">重新发送({{countNum}}s)</el-button>
              </div>
            </el-form-item>
            <el-form-item label="设置登录密码" prop="pwd">
              <el-input v-model.trim="userForm.pwd" clearable />
            </el-form-item>
            <el-form-item label="重复登录密码" prop="re_password">
              <el-input v-model.trim="userForm.re_password" clearable />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size="medium" @click="submitForm('userForm')">找回密码</el-button>
            </el-form-item>
          </el-form>
        </section>
      </template>

      <template v-else>
        <header>
          <span style="border-bottom:2px solid #ff5500">商家找回密码</span>
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
            <el-form-item label="手机号码" prop="mobile">
              <el-input v-model.trim="saleForm.mobile" clearable />
            </el-form-item>
            <el-form-item label="验证码" prop="sms_code">
              <div class="identy_code">
                <el-input v-model.trim="saleForm.sms_code" clearable />&nbsp;&nbsp;&nbsp;
                <el-button v-if="showCount" type="primary" @click="handleSendVerify">发送验证码</el-button>
                <el-button v-else type="primary">重新发送({{countNum}}s)</el-button>
              </div>
            </el-form-item>
            <el-form-item label="设置登录密码" prop="pwd">
              <el-input v-model.trim="saleForm.pwd" clearable />
            </el-form-item>
            <el-form-item label="重复登录密码" prop="re_password">
              <el-input v-model.trim="saleForm.re_password" clearable />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size="medium" @click="submitForm('saleForm')">找回密码</el-button>
            </el-form-item>
          </el-form>
        </section>
      </template>
    </main>
  </div>
</template>

<script>
import { findPwd, sendVerify } from "@/api/sys";
import { validateMobile } from "@/utils/validate.js";

export default {
  name: "findPwd",
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
          if (value !== this.saleForm.pwd) {
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
        mobile: "",
        sms_code: "",
        pwd: "",
        re_password: ""
      },
      saleForm: {
        account: "",
        pwd: "",
        re_password: "",
        mobile: "",
        sms_code: ""
      },
      userRules: {
        mobile: { required: true, validator: validateMobile, trigger: "blur" },
        sms_code: [{ required: true, trigger: "blur", message: "请填写六位验证码" }, { min: 6, max: 6, message: '请填写六位验证码', trigger: 'blur' }],
        pwd: { required: true, validator: validatePass, trigger: "blur" },
        re_password: {
          required: true,
          validator: validateSamePass,
          trigger: "blur"
        }
      },
      saleRules: {
        account: { required: true, message: "请填写用户名", trigger: "blur" },
        pwd: { required: true, validator: validatePass, trigger: "blur" },
        re_password: {
          required: true,
          validator: validateSamePass,
          trigger: "blur"
        },
        mobile: { required: true, validator: validateMobile, trigger: "blur" },
        sms_code: [{ required: true, trigger: "blur", message: "请填写六位验证码" }, { min: 6, max: 6, message: '请填写六位验证码', trigger: 'blur' }]
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
    async handleSendVerify() {
      let res;
      if (this.isUser) {
        this.$refs.userForm.validateField("mobile");
        if (this.userForm.mobile) {
          let queryObj = {
            mobile: this.userForm.mobile,
            platform: "2c",
            is_repeat: 'false',
            type: 3
          };
          res = await sendVerify(queryObj);
        }
      } else {
        this.$refs.saleForm.validateField("mobile");
        if (this.saleForm.mobile) {
          let queryObj = {
            mobile: this.saleForm.mobile,
            platform: "2b",
            is_repeat: 'false',
            type: 3
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
          let res;
          if (this.isUser) {
            res = await findPwd({
              platform: "2c",
              ...this.userForm
            });
            if (res && res.error.errno == 200) {
              this.$message.success("成功找回密码！");
              setTimeout(() => {
                this.$router.push("/login?type=user");
              }, 500);
            }
          } else {
            res = await findPwd({
              platform: "2b",
              ...this.saleForm
            });
            if (res && res.error.errno == 200) {
              this.$message.success("成功找回密码！");
              setTimeout(() => {
                this.$router.push("/login");
              }, 500);
            }
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
.findPwd {
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
