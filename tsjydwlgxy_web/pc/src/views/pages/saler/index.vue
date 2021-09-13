<template>
  <div class="saler">
    <main>
      <section>
        <!-- 左侧路由 -->
        <div class="left_menu" v-if="$route.name != 'search'">
          <sideBar />
        </div>
        <!-- 右侧内容 -->
        <div class="right_content">
          <breadcrumb
            v-if="['wenzhang', 'wenzhangdesc', 'search'].includes($route.name)"
          />

          <transition name="fade-transform" mode="out-in">
            <router-view></router-view>
          </transition>
        </div>
      </section>
    </main>
  </div>
</template>
<script>
  import breadcrumb from '@/components/breadCrumb.vue'
  import sideBar from '@/components/siderBar.vue'

  export default {
    name: 'saler',
    components: { breadcrumb, sideBar },
    methods: {},
  }
</script>
<style lang="scss" scope>
.saler {
  width: 100%;
  background: url('../../../assets/home/次级页面背景.png') no-repeat bottom left;
  background-size: cover;
  & > main {
    width: 1200px;
    margin: 0 auto;
    & > section {
      width: 100%;
      display: flex;
      padding-top: 20px;
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