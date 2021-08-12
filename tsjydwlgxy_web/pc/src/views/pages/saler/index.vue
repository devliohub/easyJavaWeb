<template>
  <div class="saler">
    <main>
      <breadcrumb />

      <section>
        <!-- 左侧路由 -->
        <div class="left_menu">
          <sideBar :isUser="false" />
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
  name: "saler",
  components: { breadcrumb, sideBar },
  methods: {}
};
</script>
<style lang="scss" scope>
.saler {
  width: 100%;
  & > main {
    width: 1200px;
    margin: 0 auto;
    & > section {
      width: 100%;
      display: flex;
      .left_menu {
        width: 210px;
        margin-right: 15px;
        .router-link-active {
          .el-menu-item {
            font-weight: bold;
            color: #ff5500;
          }
        }
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
.saler {
  .el-form-item {
    .el-cascader,
    .el-select,
    .el-input,
    .el-range-editor {
      width: 210px;
    }
  }
}
</style>