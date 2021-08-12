<template>
  <div class="layout-wrapper">
    <!-- 登录弹窗 -->
    <loginDialog
      v-if="showLoginDialog"
      @close="showLoginDialog = false"
      @goFindpwd="showLoginDialog = false;$router.push('/findPwd?type=user')"
    />
    <!-- 客服弹窗 -->
    <serviceDialog v-if="showService" @cancle="showService = false" />

    <navbar @showLogin="showLoginDialog = true" />
    <navbar2 @handleSearch="handleSearch" />
    <app-main ref="app_ref" />
    <div v-show="isSalerCenter" class="service" @click="showService = true">
      <img src="@/assets/components/imgkefu.png" alt="" />
    </div>
    <el-backtop target=".layout-wrapper"></el-backtop>
  </div>
</template>

<script>
import { Navbar, Navbar2, AppMain } from "./components";
import loginDialog from "@/components/loginDialog";
import serviceDialog from "@/components/serviceDialog";

export default {
  name: "layout",
  components: {
    Navbar,
    Navbar2,
    AppMain,
    loginDialog,
    serviceDialog
  },
  data() {
    return {
      showLoginDialog: false,
      showService: false
    };
  },
  computed: {
    // 只在商家中心显示
    isSalerCenter() {
      return this.$route.path == "/saler/personal";
    }
  },
  methods: {
    handleSearch(val) {
      if (this.$route.name != "allGoods")
        this.$router.push("/allGoods/0?keyword=" + val);
      else this.$refs["app_ref"].$children[0].$children[0].initData(val);
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.layout-wrapper {
  position: relative;
  width: 100%;
  overflow: auto;
  height: 100%;
  .service {
    transition: .3s;
    position: fixed;
    z-index: 999;
    text-align: center;
    top: calc(50vh - 30px);
    right: 120px;
    font-size: 15px;
    color: #fff;
    border-radius: 3px;
    &:hover {
      transform: scale(1.05);
      cursor: pointer;
    }
  }
}
</style>
