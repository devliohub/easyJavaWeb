<template>
  <div class="user">
    <main>
      <breadcrumb />

      <section>
        <!-- 左侧路由 -->
        <div class="left_menu">
          <sideBar :isUser="true" />
        </div>
        <!-- 右侧内容 -->
        <div class="right_content">
          <transition name="fade-transform" mode="out-in">
            <router-view></router-view>
          </transition>
        </div>
      </section>
    </main>
  </div>
</template>
<script>
import breadcrumb from "@/components/breadCrumb.vue";
import sideBar from "@/components/sideBar";

export default {
  name: "user",
  components: { breadcrumb, sideBar },
  methods: {}
};
</script>
<style lang="scss" scope>
.user {
  width: 100%;
  & > main {
    width: 1200px;
    margin: 0 auto;
    & > section {
      width: 100%;
      display: flex;
      .left_menu {
        width: 210px;
        margin-right: 20px;
      }
      .right_content {
        flex: 1;
        min-height: calc(100vh - 230px);
        overflow: auto;
      }
    }
  }
}
</style>
<style lang="scss">
.user {
  .el-form-item {
    .el-cascader,
    .el-select,
    .el-input,
    .el-range-editor {
      width: 230px;
    }
  }
}
</style>