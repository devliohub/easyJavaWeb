<template>
  <el-menu
    mode="vertical"
    :show-timeout="200"
    :default-active="$route.path"
    :default-openeds="['/saler', '/user']"
    active-text-color="#ff5500"
    :unique-opened="false"
    :collapse-transition="false"
  >
    <sidebar-item v-for="route in routes" :key="route.name" :item="route" :base-path="route.path"></sidebar-item>
  </el-menu>
</template>

<script>
import { mapGetters } from "vuex";
import SidebarItem from "./SidebarItem";

export default {
  components: { SidebarItem },
  props: {
    isUser: {
      type: Boolean,
      default: true
    }
  },
  computed: {
    ...mapGetters(["sidebar"]),
    routes() {
      return this.isUser
        ? this.$router.options.routes[0].children[0].children[6].children
        : this.$router.options.routes[0].children[0].children[7].children;
    }
  },
  mounted() {
    console.log(this.$route.path);
  },
  methods: {}
};
</script>
<style lang="scss" scope>
.el-submenu__title {
  & > span {
    font-weight: bold;
    font-size: 15px;
  }
}
</style>