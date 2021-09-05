<template>
  <div id="app">
    <transition name="fade-transform" mode="out-in">
      <router-view />
    </transition>
  </div>
</template>

<script>
  import { getMenus } from '@/api'
  export default {
    name: 'App',
    data() {
      return {}
    },
    async mounted() {
      if (!JSON.parse(window.sessionStorage.getItem('menuArr'))) {
        let res = await getMenus()
        window.sessionStorage.setItem('menuArr', JSON.stringify(res.result))
      }
    },
    watch: {},
  }
</script>
<style lang="scss">
</style>
