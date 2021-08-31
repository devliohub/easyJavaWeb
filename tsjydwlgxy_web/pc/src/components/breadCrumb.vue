<template>
  <el-breadcrumb class="app-breadcrumb" separator=">">
    <transition-group name="breadcrumb">
      <el-breadcrumb-item
        v-for="item in levelList"
        :key="item.path"
        :to="resolvePath(item)"
      >
        <template v-if="item.meta.title">
          <span>{{ item.meta.title }}</span>
        </template>
      </el-breadcrumb-item>
    </transition-group>
  </el-breadcrumb>
</template>

<script>
  export default {
    created() {
      this.getBreadcrumb()
    },
    data() {
      return {
        levelList: null,
        metaArr: [
          '全部商品',
          '女装',
          '男装',
          '内衣',
          '母婴',
          '彩妆',
          '居家',
          '鞋包',
          '美食',
          '文体车品',
          '数码家电',
          '其他商品',
        ],
      }
    },
    watch: {
      $route() {
        this.getBreadcrumb()
      },
    },
    methods: {
      resolvePath(item) {
        return { path: item.path }
      },
      getBreadcrumb() {
        let matched = this.$route.matched.filter((item) => item.name)
        this.levelList = matched
      },
    },
  }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.app-breadcrumb {
  display: inline-block;
  font-size: 14px;
  line-height: 50px;
  span {
    cursor: pointer;
    font-weight: bold;
  }
}
</style>
