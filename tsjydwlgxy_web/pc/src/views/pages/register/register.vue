<template>
  <div class="register">
    <main>
      <!-- 商家注册 -->
      <div class="register_user" style="margin-right:20px">
        <div class="header">
          <span>用户注册</span>
        </div>
        <div class="body">
          <router-link :to="{name: 'registerDesc', query:{type: 'user'}}">
            <div class="body_btn btn_user">用户注册</div>
          </router-link>
          <router-link to="/login?type=user">
            我已注册，现在就 <el-button size="mini" type="primary" plain>登录</el-button>
          </router-link>
        </div>
      </div>
      <!-- 商家注册 -->
      <div class="register_saler">
        <div class="header">
          <span>商家注册</span>
        </div>
        <div class="body">
          <router-link :to="{name: 'registerDesc', query:{type: 'saler'}}">
            <div class="body_btn btn_saler">商家注册</div>
          </router-link>
          <router-link to="/login?type=saler">
            我已注册，现在就 <el-button size="mini" type="primary" plain>登录</el-button>
          </router-link>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
export default {
  name: "register",
  components: {},
  data() {
    return {};
  },
  mounted() {},
  methods: {}
};
</script>
<style lang="scss" scope>
.register {
  width: 100%;
  main {
    width: 1200px;
    margin: 0 auto;
    display: flex;
    align-items: center;
    justify-content: center;
    height: calc(100vh - 160px);
    & > div {
      background: #fcfcfc;
      width: 470px;
      border: solid 1px #e5e5e5;
      padding: 40px 30px;
      .header {
        font-weight: bold;
        font-size: 22px;
        border-bottom: 2px solid #f1eeeb;
        padding-bottom: 12px;
        & > span {
          padding-bottom: 12px;
          border-bottom: 2px solid #ff5500;
        }
      }
      .body {
        display: flex;
        align-items: center;
        justify-content: center;
        padding: 100px;
        flex-direction: column;
        & > span {
          color: #666;
          display: block;
        }
        &_btn {
          width: 200px;
          height: 50px;
          line-height: 50px;
          text-align: center;
          box-shadow: -3px 6px 35px 11px rgba(255, 70, 78, 0.17);
          border-radius: 26px;
          margin-bottom: 20px;
          font-size: 20px;
          color: #fff;
          cursor: pointer;
          transition: .2s ease-in-out;
        }
        .btn_user {
          background:#ff464f;
          border: 1px solid #ff464f;
          &:hover {
            color: #ff464f;
            background: #fff
          }
        }
        .btn_saler {
          background:#ff5500;
          border: 1px solid #ff5500;
          &:hover {
            color: #ff5500;
            background: #fff
          }
        }
      }
    }
  }
}
</style>
