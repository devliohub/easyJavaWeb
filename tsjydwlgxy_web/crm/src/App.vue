<template>
  <div class="layout">
    <Header v-if="state.showMenu" />
    <el-container v-if="state.showMenu" class="container">
      <el-aside class="aside">
        <el-menu
          :default-openeds="state.defaultOpen"
          background-color="#222832"
          text-color="#fff"
          :router="true"
          :default-active="state.currentPath"
        >
          <el-submenu index="1">
            <template #title>
              <span>网站管理</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="/article"
                ><i class="el-icon-star-on" />文章管理</el-menu-item
              >
              <el-menu-item index="/swiper"
                ><i class="el-icon-picture" />轮播图管理</el-menu-item
              >
              <el-menu-item index="/caidan"
                ><i class="el-icon-menu" />菜单管理</el-menu-item
              >
            </el-menu-item-group>
          </el-submenu>
          <el-submenu index="2">
            <template #title>
              <span>课程管理</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="/kecheng"
                ><i class="el-icon-edit" />课程管理</el-menu-item
              >
            </el-menu-item-group>
          </el-submenu>
          <el-submenu index="3">
            <template #title>
              <span>系统管理</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="/account"
                ><i class="el-icon-user" />用户管理</el-menu-item
              >
            </el-menu-item-group>
          </el-submenu>
        </el-menu>
      </el-aside>
      <el-container class="content">
        <div class="main">
          <router-view />
        </div>
      </el-container>
    </el-container>
    <el-container v-else class="container2">
      <router-view />
    </el-container>
  </div>
</template>

<script>
  import { onUnmounted, reactive } from 'vue'
  import Header from '@/components/Header.vue'
  import { useRouter } from 'vue-router'
  import { pathMap, localGet, localSet } from '@/utils'
  import axios from '@/utils/axios'

  export default {
    name: 'App',
    components: {
      Header,
    },
    setup() {
      const noMenu = ['/login']
      const router = useRouter()

      const state = reactive({
        defaultOpen: ['1', '2', '3', '4'],
        showMenu: true,
        currentPath: '/account',
      })
      // 字典字段缓存
      if (!localGet('loRoles')) {
        axios.get('/api/sys/rolemenus').then((res) => {
          localSet('loRoles', res)
        })
      }
      if (!localGet('loModules')) {
        axios.get('/api/sys/coursemodules').then((res) => {
          localSet('loModules', res)
        })
      }
      if (!localGet('loTypes')) {
        axios.get('/api/sys/coursetypes').then((res) => {
          localSet('loTypes', res)
        })
      }
      if (!localGet('loMenus')) {
        axios.get('/api/sys/menus').then((res) => {
          localSet('loMenus', res)
        })
      }

      const unwatch = router.beforeEach((to, from, next) => {
        if (to.path == '/login') {
          next()
        } else {
          // 如果不是 /login，判断是否有 token
          if (!localGet('token')) {
            // 如果没有，则跳至登录页面
            next({ path: '/login' })
          } else {
            next()
          }
        }
        state.showMenu = !noMenu.includes(to.path)
        state.currentPath = to.path
        document.title = pathMap[to.name]
      })

      onUnmounted(() => {
        unwatch()
      })

      return {
        state,
      }
    },
  }
</script>

<style scoped>
.layout {
  min-height: 100vh;
  background-color: #ffffff;
}
.layout-header {
  height: 50px;
  line-height: 50px;
  padding-left: 15px;
  background: #f2f2f2;
  font-size: 20px;
}
.container {
  height: calc(100vh - 51px);
}
.container2 {
  height: 100vh;
}
.aside {
  width: 200px !important;
  background-color: #222832;
  overflow: hidden;
  overflow-y: auto;
  -ms-overflow-style: none;
  overflow: -moz-scrollbars-none;
}
.aside::-webkit-scrollbar {
  display: none;
}
.head {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 50px;
  padding: 10px;
}
.head > div {
  display: flex;
  align-items: center;
}

.head img {
  width: 50px;
  height: 50px;
  margin-right: 10px;
}
.head span {
  font-size: 20px;
  color: #ffffff;
}
.content {
  display: flex;
  flex-direction: column;
  /* max-height: 100vh; */
  overflow: hidden;
}
.main {
  height: calc(100vh - 130px);
  overflow: auto;
  padding: 10px;
}
</style>
<style>
body {
  padding: 0;
  margin: 0;
  box-sizing: border-box;
}
.el-menu {
  border-right: none !important;
}
.el-submenu {
  border-top: 1px solid hsla(0, 0%, 100%, 0.05);
  border-bottom: 1px solid rgba(0, 0, 0, 0.2);
}
.el-submenu:first-child {
  border-top: none;
}
.el-submenu [class^='el-icon-'] {
  vertical-align: -1px !important;
}
a {
  color: #409eff;
  text-decoration: none;
}
.el-pagination {
  text-align: center;
  margin-top: 20px;
}
.el-popper__arrow {
  display: none;
}
</style>
