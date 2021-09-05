<template>
  <div class="breadcrumb">
    <span style="color: #888">当前位置：</span>
    <span>{{ levelList.name }}</span>
  </div>
</template>
<script>
  // 面包屑
  export default {
    name: 'breadcrumb',
    data() {
      return {
        levelList: {
          name: '',
        },
      }
    },
    computed: {
      Route() {
        return this.$route
      },
    },
    mounted() {
      this.getBreadcrumb()
    },
    watch: {
      $route: function () {
        this.getBreadcrumb()
      },
    },
    methods: {
      handleClick() {},
      async getBreadcrumb() {
        JSON.parse(window.sessionStorage.getItem('menuArr')).map((item) => {
          if (item.id == this.$route.query.id) this.levelList = item

          if (item.sub_menus && item.sub_menus.length > 0) {
            item.sub_menus.map((_item) => {
              if (_item.id == this.$route.query.id) this.levelList = item
            })
          }
          console.log(this.levelList)
        })
      },
    },
  }
</script>
<style lang="scss" scope>
.breadcrumb {
  width: 100%;
  padding: 0 15px;
  background: #fff;
  height: 50px;
  line-height: 50px;
  font-size: 14px;
}
</style>