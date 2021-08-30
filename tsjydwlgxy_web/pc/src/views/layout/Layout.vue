<template>
  <div class="layout-wrapper">
    <navbar @handleSearch="handleSearch" />
    <app-main ref="app_ref" />
    <homeFooter />
    <el-backtop target=".layout-wrapper"></el-backtop>
  </div>
</template>

<script>
  import { Navbar, AppMain } from './components'
  import homeFooter from '../pages/home/footer'

  export default {
    name: 'layout',
    components: {
      Navbar,
      AppMain,
      homeFooter,
    },
    data() {
      return {
        showLoginDialog: false,
        showService: false,
      }
    },
    computed: {},
    methods: {
      handleSearch(val) {
        if (this.$route.name != 'allGoods')
          this.$router.push('/allGoods/0?keyword=' + val)
        else this.$refs['app_ref'].$children[0].$children[0].initData(val)
      },
    },
  }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.layout-wrapper {
  position: relative;
  width: 100%;
  overflow: auto;
  height: 100%;
  .service {
    transition: 0.3s;
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
