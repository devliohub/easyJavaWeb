<template>
  <div v-if="!item.hidden && item.children">
    <el-submenu :index="item.name || item.path" v-if="item.children && item.children.length > 1" disabled>
      <!-- 头部 -->
      <template slot="title">
        <i class="fontcontainer">
          <span class="iconfont" v-if="item.meta && item.meta.icon" :class="item.meta.icon"></span>
        </i>
        <span v-if="item.meta && item.meta.title" slot="title">{{item.meta.title}}</span>
      </template>

      <template v-for="child in item.children">
        <!-- 商品大厅跳index -->
        <router-link v-if="child.name == 'shoppingMall'" :to="'/index'" :key="child.name">
          <el-menu-item :index="resolvePath(child.path)">
            <i class="fontcontainer">
              <span class="iconfont" v-if="child.meta && child.meta.icon" :class="child.meta.icon"></span>
            </i>
            <span v-if="child.meta && child.meta.title" slot="title">{{ child.meta.title }}</span>
          </el-menu-item>
        </router-link>
        <!-- 其他 -->
        <router-link v-else :to="resolvePath(child.path)" :key="child.name">
          <el-menu-item :index="resolvePath(child.path)">
            <i class="fontcontainer">
              <span class="iconfont" v-if="child.meta && child.meta.icon" :class="child.meta.icon"></span>
            </i>
            <span v-if="child.meta && child.meta.title" slot="title">{{ child.meta.title }}</span>
          </el-menu-item>
        </router-link>
      </template>
    </el-submenu>

    <!-- 商家中心/添加宝贝 -->
    <router-link v-else :to="{name: item.children[0].name}">
      <el-menu-item :class="{'style_fixer': item.children[0].name == 'addGoods'}" class="oneChild" :index="item.children[0].name || item.children[0].path">
        <span
          v-if="item.children[0].meta && item.children[0].meta.title"
          slot="title"
        >{{item.children[0].meta.title}}</span>
      </el-menu-item>
    </router-link>
  </div>
</template>

<script>
import path from "path";

export default {
  name: "SidebarItem",
  props: {
    item: {
      type: Object,
      required: true
    },
    basePath: {
      type: String,
      default: ""
    }
  },
  data() {
    return {};
  },
  methods: {
    hasOneShowingChild(children) {
      const showingChildren = children.filter(item => {
        if (item.hidden) {
          return false;
        } else {
          return true;
        }
      });
      if (showingChildren.length === 1) {
        return true;
      }
      return false;
    },
    resolvePath(...paths) {
      return path.resolve(this.basePath, ...paths);
    },
  }
};
</script>

<style lang="scss" scoped>
.fontcontainer {
  vertical-align: top;
  padding-right: 10px;
  .iconfont {
    font-size: 18px !important;
  }
}
.style_fixer {
  margin-bottom: 10px;
  border-top: 0 !important;  
}

.oneChild {
  border: 1px solid #ccc;
  text-align: center;
  font-size: 16px;
}
</style>