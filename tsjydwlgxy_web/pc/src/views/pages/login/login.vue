<template>
  <div class="login">
    <section>
      <div v-if="activeName == 'user'">
        <h3>品质精选</h3>
        <h3>全民疯抢</h3>
        <span>购物更划算 就来淘小熊</span>
      </div>
      <div v-else>
        <h3>精准买家资源</h3>
        <h3>快速打造爆款</h3>
      </div>
    </section>

    <main>
      <span class="qr_code">
        <img src="../../../assets/components/qr_code.png" alt />
      </span>

      <el-tabs v-model="activeName">
        <el-tab-pane label="用户登录" name="user">
          <el-form
            class="login-form"
            autocomplete="on"
            :model="user_form"
            :rules="user_rules"
            ref="user_form"
            label-position="left"
          >
            <!-- 登陆姓名 -->
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
            <!-- 登陆密码 -->
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
            <!-- 登录按钮 -->
            <el-form-item>
              <el-button
                type="primary"
                style="width:100%"
                :loading="loading"
                @click.native.prevent="handleLogin('user_form')"
              >
                <span style="font-weight:bold;font-size:20px">登 录</span>
              </el-button>
            </el-form-item>
            
            <div class="tips">
              <div class="tips_main">
                <router-link to="/registerDesc?type=user">
                  <img src="../../../assets/components/right_arrow.png" alt />&nbsp;
                  <span>没有账号 免费注册</span>
                </router-link>
              </div>
              <a class="forget" @click="$router.push('/findPwd?type=user')">忘记密码？</a>
              <!-- <div class="tips_others">
                <img src="../../../assets/components/weixin_icon.png" alt />
              </div> -->
            </div>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="商家登录" name="saler">
          <el-form
            class="login-form"
            autocomplete="on"
            :model="saler_form"
            :rules="saler_rules"
            ref="saler_form"
            label-position="left"
          >
            <!-- 登陆姓名 -->
            <el-form-item prop="username">
              <el-input
                placeholder="请输入用户名"
                type="text"
                v-model="saler_form.username"
                autocomplete="on"
                clearable
              >
                <template slot="prepend">
                  <i class="el-icon-user-solid _icon"></i>
                </template>
              </el-input>
            </el-form-item>
            <!-- 登陆密码 -->
            <el-form-item prop="password">
              <el-input
                placeholder="请输入密码"
                type="text"
                v-model="saler_form.password"
                autocomplete="on"
                @keyup.enter.native="handleLogin('saler_form')"
                show-password
              >
                <template slot="prepend">
                  <i class="el-icon-lock _icon"></i>
                </template>
              </el-input>
            </el-form-item>
            
            <!-- 登录按钮 -->
            <el-form-item>
              <el-button
                type="primary"
                style="width:100%"
                :loading="loading"
                @click.native.prevent="handleLogin('saler_form')"
              >
                <span style="font-weight:bold;font-size:20px">登 录</span>
              </el-button>
            </el-form-item>
            <div class="tips">
              <div class="tips_main">
                <router-link to="/registerDesc?type=saler">
                  <img src="../../../assets/components/right_arrow.png" alt />&nbsp;
                  <span>没有账号 免费注册</span>
                </router-link>
              </div>
              <a class="forget" @click="$router.push('/findPwd')">忘记密码？</a>
            </div>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </main>
  </div>
</template>

<script>
import { validatePass } from "@/utils/validate.js";

export default {
  name: "login",
  data() {
    return {
      activeName: "saler",
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
      // 商家
      saler_form: {
        username: "", //pipi
        password: "", //1234567
        platform: '2b'
      },
      saler_rules: {
        username: [{ required: true, trigger: ["blur", "change"], message: "请输入用户名" }],
        password: [{ required: true, trigger: ["blur", "change"], validator: validatePass }]
      },
      loading: false
    };
  },
  mounted() {
    this.activeName = this.$route.query.type || "saler";
  },
  methods: {
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
              }
              this.loading = false;
            })
            .catch(err => {
              this.loading = false;
            });
        } else {
          return false;
        }
      });
    }
  }
};
</script>
<style lang="scss">
.login {
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
</style>
<style lang="scss" scoped>
.login {
  position: relative;
  height: calc(100vh - 160px);
  width: 100%;
  background: url("https://taodaxiong-1259123353.cos.ap-shanghai.myqcloud.com/tdxWeb/sjdl_imgbeijing.png") no-repeat;
  background-size: cover;
  // 右侧
  & > main {
    position: absolute;
    right: 350px;
    top: 100px;
    background: #fff;
    padding: 33px;
    border-radius: 3px;
    // 二维码
    .qr_code {
      position: absolute;
      width: 50px;
      height: 50px;
      top: 10px;
      right: 10px;
    }
    .login-form {
      width: 300px;
      margin-top: 10px;
    }
    ._icon {
      font-size: 22px;
      color: #dadada;
    }
    // 下方提示
    .tips {
      font-size: 13px;
      display: flex;
      align-items: center;
      justify-content: space-between;
      .forget {
        &:hover {
          color: #ff5500;
        }
      }
      &_main {
        & > a {
          display: flex;
          align-items: center;
          justify-content: center;
          span {
            color: #fc3935;
            cursor: pointer;
          }
        }
      }
      // &_others {
      //   padding: 20px 5px;
      //   img {
      //     cursor: pointer;
      //     transition: 0.3s ease-in-out;
      //     &:hover {
      //       transform: scale(1.1);
      //       -webkit-transform: scale(1.1);
      //       -moz-transform: scale(1.1);
      //     }
      //   }
      // }
    }
  }
  // 左侧
  & > section {
    position: absolute;
    top: 210px;
    left: 350px;
    h3 {
      font-size: 46px;
      color: #fff;
      letter-spacing: 2px;
      font-weight: bold;
      padding-bottom: 25px;
    }
    span {
      display: block;
      background-color: rgba(255, 255, 255, 0.2);
      border-radius: 20px;
      font-size: 28px;
      color: #fff;
      padding: 20px 55px 20px 20px;
    }
  }
}
</style>