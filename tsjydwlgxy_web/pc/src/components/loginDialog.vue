<template>
  <el-dialog
    append-to-body
    :visible.sync="dialogVisible"
    :show-close="false"
    :close-on-press-escape="false"
    :before-close="handleBeforeClose"
    width="420px"
    custom-class="login_dialog"
  >
    <div slot="title" class="dialog-title">登录 / 注册</div>
    <!-- fullscreen -->
    <el-radio-group v-model="loginType" @change="handleChange">
      <el-radio-button label="1">
        <i class="el-icon-user"></i> 用户登录
      </el-radio-button>
      <el-radio-button label="2">
        <i class="el-icon-circle-plus-outline"></i> 用户注册
      </el-radio-button>
    </el-radio-group>

    <!-- 用户登录 -->
    <el-form
      v-if="loginType == '1'"
      class="login-form"
      autocomplete="on"
      :model="user_form"
      :rules="user_rules"
      ref="user_form"
      label-position="left"
    >
      <el-form-item prop="username">
        <el-input
          placeholder="请输入用户名"
          type="text"
          v-model="user_form.username"
          autocomplete="on"
          clearable
        >
          <template slot="prepend">
            <i class="el-icon-user-solid _icon"></i>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          placeholder="请输入密码"
          type="text"
          v-model="user_form.password"
          autocomplete="on"
          @keyup.enter.native="handleLogin('user_form')"
          show-password
        >
          <template slot="prepend">
            <i class="el-icon-lock _icon"></i>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          style="width:100%"
          :loading="loading"
          @click.native.prevent="handleLogin('user_form')"
        >
          <span style="font-weight:bold;font-size:20px">立即登录</span>
        </el-button>
      </el-form-item>
      <div class="tips">
        <div class="tips_others">
          <div>
            <!-- <span>使用其他账号登录</span>
            <img src="@/assets/components/weixin_icon.png" alt /> -->
          </div>
          <a class="forget" @click="$emit('goFindpwd')">忘记密码？</a>
        </div>
      </div>
    </el-form>
  </el-dialog>
</template>
<script>
import { validatePass } from "@/utils/validate.js";

export default {
  name: "loginDialog",
  data() {
    return {
      loginType: "1",
      loading: false,
      dialogVisible: true,
      activeName: "1",
      // 用户
      user_form: {
        username: "", //18827035411
        password: "", //123456
        platform: '2c'
      },
      user_rules: {
        username: [{ required: true, trigger: "blur", message: "请输入用户名" }],
        password: [{ required: true, trigger: "blur", validator: validatePass }]
      },
      saler_rules: {
        username: [{ required: true, trigger: "blur", message: "请输入用户名" }],
        password: [{ required: true, trigger: "blur", validator: validatePass }]
      }
    };
  },
  methods: {
    handleBeforeClose() {
      this.$emit("close");
    },
    // 点击新增用户跳转
    handleChange(val) {
      if(val == '2') {
        this.loginType = '1'
        window.open(window.location.origin + "/#/register");
      }
    },
    handleLogin(str) {
      this.$refs[str].validate(valid => {
        if (valid) {
          this.loading = true;
          this.$store
            .dispatch("Login", this[str])
            .then(res => {
              if (res) {
                this.$router.push({ path: "/" });
                this.$message.success("登录成功！");
                this.$emit("close");
              }
              this.loading = false;
            })
            .catch(err => {
              this.loading = false;
              this.$emit("close");
            });
        } else {
          return false;
        }
      });
    }
  }
};
</script>
<style lang="scss" scope>
.dialog-title {
  background: #ff5500;
  color: #fff;
  font-size: 18px;
  padding: 10px 0;
  font-weight: bold;
  text-align: center;
}
.login_dialog {
  .el-input-group__prepend {
    padding: 0 15px;
    font-size: 16px;
  }
}
.tips_others {
  display: flex;
  justify-content: space-between;
  align-items: center;
  & > div {
    display: flex;
    flex-direction: column;
    img {
      width: 36px;
      height: 36px;
      cursor: pointer;
      transition: 0.3s ease-in-out;
      &:hover {
        transform: scale(1.1);
        -webkit-transform: scale(1.1);
        -moz-transform: scale(1.1);
      }
    }
    span {
      padding-bottom: 10px;
      color: #999;
    }
  }
}
.forget:hover {
  color: #ff5500;
}
</style>
<style lang="scss">
.login_dialog {
  .el-tabs__item {
    font-size: 18px;
    font-weight: bold;
    margin: 5px 0;
  }
  .el-input-group__append,
  .el-input-group__prepend {
    padding: 0 10px;
  }
  .el-form-item__content {
    margin-bottom: 20px;
  }
}
.el-dialog__header {
  padding: 0 !important;
}
.el-dialog__body {
  padding: 20px 50px !important;
  .el-radio-group {
    width: 100%;
    padding-bottom: 15px;
    .el-radio-button {
      width: 50%;
      .el-radio-button__inner {
        width: 100%;
      }
    }
  }
}
</style>