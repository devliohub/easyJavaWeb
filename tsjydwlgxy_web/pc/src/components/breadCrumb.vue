<template>
  <el-breadcrumb class="app-breadcrumb" separator=">">
    <transition-group name="breadcrumb">
      <el-breadcrumb-item v-for="item in levelList" :key="item.path" :to="resolvePath(item)">
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
    this.getBreadcrumb();
  },
  data() {
    return {
      levelList: null,
      metaArr: [
        "全部商品",
        "女装",
        "男装",
        "内衣",
        "母婴",
        "彩妆",
        "居家",
        "鞋包",
        "美食",
        "文体车品",
        "数码家电",
        "其他商品"
      ]
    };
  },
  watch: {
    $route(val) {
      this.getBreadcrumb();
    }
  },
  methods: {
    // 修复allgood下path为 /:type异常, 反解析一下
    resolvePath(item) {
      if (item.name == "allGoods") {
        // let n = this.metaArr.indexOf(item.meta.title);
        // if (n > -1) return { path: "/allGoods/" + n };
      } else if (item.name == "allGoods_itemDesc") {
        return { path: "" };
      } else {
        return { path: item.path };
      }
    },
    getBreadcrumb() {
      let matched = this.$route.matched.filter(item => item.name);
      this.levelList = matched;
      // 处理特殊情况路由
      let _t = this.$route.params.type;
      let _title;
      
      if (_t || _t == 0) {
        JSON.parse(window.sessionStorage.getItem("tpyeArr")).map(el => {
          if (el.id == _t) _title = el.short_name;
        });
        let obj = {
          path: "/allGoods/" + _t,
          meta: { title: _title }
        };
        this.levelList.splice(1, 1, obj);
      }
    }
  }
};
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
