<template>
  <div class="app-main">
    <transition name="fade-transform" mode="out-in">
      <router-view></router-view>
    </transition>
  </div>
</template>

<script>
export default {
  name: "AppMain",
};
</script>
<style scoped lang="scss">
.app-main {
  margin: 0;
  padding: 0;
  position: relative;
}
</style>
<style lang="scss">
.index_3_parts {
  // 加粗title
  margin-bottom: 30px;
  .el-tabs__item {
    font-size: 21px;
    font-weight: bold;
    padding: 0 15px;
  }
  .el-tabs__nav-wrap::after {
    background-color: #fff;
  }
}
</style>
