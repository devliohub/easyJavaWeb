<template>
  <el-breadcrumb class="app-breadcrumb" separator=">">
    <transition-group name="breadcrumb">
      <el-breadcrumb-item :key="1">{{ levelList.name }}</el-breadcrumb-item>
      <el-breadcrumb-item :key="2" v-if="$route.query.name">{{
        $route.query.name
      }}</el-breadcrumb-item>
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
        levelList: {
          name: '',
        },
      }
    },
    watch: {
      $route() {
        this.getBreadcrumb()
      },
    },
    methods: {
      async getBreadcrumb() {
        if (this.$route.name == 'wenzhang') {
          JSON.parse(window.sessionStorage.getItem('menuArr')).map((item) => {
            if (item.id == this.$route.query.id) this.levelList = item

            if (item.sub_menus && item.sub_menus.length > 0) {
              item.sub_menus.map((_item) => {
                if (_item.id == this.$route.query.id) this.levelList = item
              })
            }
          })
        }
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
    font-weight: bold;
  }
}
</style>
