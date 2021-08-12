<template>
  <div class="navbar">
    <main>
      <ul class="navbar_left">
        <li>
          <span v-if="isLogin" class="avatar">
            <img v-if="name.avatar" :src="name.avatar" alt="" />
            <span>{{name.account || ''}}&nbsp;&nbsp;&nbsp;{{isFans ? '粉丝' : '商家'}}: {{ name.mobile }}</span>
          </span>
          <span v-else>游 客</span>
        </li>
        <li v-if="isLogin">
          <a :class="{'has_msg': my_num > 0}" class="active_li" @click="handleClickMsg">
            消息通知
            <span v-if="my_num > 0">({{my_num}})</span>
          </a>
        </li>
        <!-- <li>微信登录</li> -->
        <li v-if="!isLogin">
          <a class="active_li" @click="handleClickUserlogin">用户登录</a>
        </li>
        <li v-if="!isLogin">
          <router-link class="active_li" to="/login">商家登录</router-link>
        </li>
        <li v-if="!isLogin">
          <router-link class="active_li" to="/register">免费注册</router-link>
        </li>
      </ul>

      <ul class="navbar_right">
        <li>
          <router-link class="active_li" to="/index">
            <i class="el-icon-s-home"></i> 淘小熊首页
          </router-link>
        </li>
        <li v-if="isLogin && isFans">
          <router-link class="active_li" to="/user/personal">
            <i class="el-icon-user-solid"></i> 用户中心
          </router-link>
        </li>
        <li v-if="isLogin && !isFans">
          <router-link class="active_li" to="/saler/personal">
            <i class="el-icon-s-custom"></i> 商家中心
          </router-link>
        </li>
        <li v-if="isLogin">
          <a class="active_li" @click="handleLogout" style="color:#ff5500">
            <i class="el-icon-switch-button"></i> 退出登录
          </a>
        </li>
      </ul>
    </main>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { messageNum } from "@/api/sys.js";

export default {
  components: {},
  data() {
    return {
      my_num: 0
    };
  },
  mounted() {
    this.getMsgNum()
  },
  watch: {
    '$route.path': function (newVal, oldVal) {
      this.getMsgNum()
    }
  },
  computed: {
    ...mapGetters(["name"]),
    isProd() {
      return process.env.NODE_ENV == "development" ? "开发" : "生产";
    },
    isFans() {
      if (this.isLogin) return this.name.platform == "2c";
    },
    isLogin() {
      return this.name;
    }
  },
  methods: {
    // 2b商家 2c粉丝
    async getMsgNum() {
      // console.log(this.$store.state.user.name.platform)
      console.log(this.$store.state.user.name)
      if(this.$store.state.user.name) {
        let res = await messageNum({
          platform: this.$store.state.user.name.platform
        })
        if (res && res.error.errno == 200) {
          if(res.un_msg_num > 0) {
            this.my_num = res.un_msg_num
          }
        }
      }
      
    },
    handleClickUserlogin() {
      this.$emit("showLogin");
    },
    handleClickMsg() {
      let url = this.isFans ? "/user/msg" : "/saler/msg";
      this.$router.push(url);
    },
    handleLogout() {
      this.$confirm("是否确认退出登录?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          this.$store
            .dispatch("LogOut")
            .then(res => {
              if (res) {
                this.$router.push({ path: "/" });
                this.$message.success("登出成功！");
              }
            })
            .catch(err => {});
        })
        .catch(() => {});
    }
  }
};
</script>

<style lang="scss" scoped>
.navbar {
  background-color: #333;
  font-size: 12px;
  .has_msg {
    color: #ff5500;
    font-weight: bold;
  }
  main {
    width: 1200px;
    margin: 0 auto;
    color: #fff;
    display: flex;
    height: 40px;
    justify-content: space-between;

    .active_li {
      transition: 0.2s;
      &:hover {
        color: #ff5500;
      }
    }

    .navbar_left {
      display: flex;
      align-items: center;
      li {
        &:first-child {
          padding-left: 0;
        }
        height: 20px;
        line-height: 20px;
        padding: 0 15px;
        border-right: 1px solid #fff;
        &:last-child {
          border: none;
        }
        .avatar {
          display: flex;
          align-items: center;
          & > img {
            margin-right: 10px;
            width: 20px;
            height: 20px;
          }
        }
      }
    }

    .navbar_right {
      display: flex;
      align-items: center;
      li {
        &:last-child {
          padding-left: 30px;
        }
        display: flex;
        height: 20px;
        line-height: 20px;
        padding: 0 15px;
        align-items: center;
        i {
          font-size: 18px;
        }
      }
    }
  }
}
</style>

