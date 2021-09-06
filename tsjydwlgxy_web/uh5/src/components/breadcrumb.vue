<template>
  <div class="breadcrumb">
    <span style="color: #888">当前位置：</span>
    <span v-if="$route.meta.title"> {{ $route.meta.title }}</span>
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
    mounted() {},
    watch: {},
    methods: {},
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