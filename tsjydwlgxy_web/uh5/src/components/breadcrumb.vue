<template>
  <div class="breadcrumb">
    <span style="color: #888">当前位置：</span>
    <span>{{ Route.meta.title }}</span>
  </div>
</template>
<script>
  // 面包屑
  export default {
    name: 'breadcrumb',
    data() {
      return {}
    },
    computed: {
      Route() {
        return this.$route
      },
    },
    mounted() {
      console.log(this.$route)
    },
    methods: {
      handleClick() {},
    },
  }
</script>
<style lang="scss" scope>
.breadcrumb {
  width: 100%;
  padding: 0 15px;
  background: #fff;
  height: 30px;
  line-height: 30px;
  font-size: 14px;
}
</style>