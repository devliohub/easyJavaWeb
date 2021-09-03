<template>
  <div class="navbar2">
    <img src="../../../assets/home/toper_bg.jpg" alt="" />
    <main>
      <div class="navbar2_right">
        <el-input placeholder="关键词搜索" v-model="search_word">
          <i
            class="el-icon-search el-input__icon"
            slot="suffix"
            @click="handleIconClick"
          >
          </i>
        </el-input>
      </div>
    </main>

    <!-- tab栏目 -->
    <div class="index_items">
      <ul>
        <li
          v-for="(item, index) in cateTitleList.slice(0, 9)"
          :key="index"
          @click="handleGo(item, index)"
          :class="{
            isactive:
              $route.query.id == item.id ||
              ($route.name == 'kecheng' && item.name == '通识课程') ||
              ($route.name == 'index' && item.id == 1),
          }"
        >
          {{ item.name }}
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
  import { getMenus } from '@/api'
  export default {
    data() {
      return {
        search_word: '',
        cateTitleList: [],
      }
    },
    watch: {},
    async mounted() {
      if (!JSON.parse(window.sessionStorage.getItem('menuArr'))) {
        let res = await getMenus()
        this.cateTitleList = res.result
        window.sessionStorage.setItem('menuArr', JSON.stringify(res.result))
      } else {
        this.cateTitleList = JSON.parse(window.sessionStorage.getItem('menuArr'))
      }
    },
    methods: {
      handleIconClick() {
        this.$emit('handleSearch', this.search_word)
      },
      handleGo(item, index) {
        console.log(item)
        if (index == 0) {
          this.$router.push('/index')
        } else if (index == 2) {
          this.$router.push('/saler/kecheng?_t=' + Date.parse(new Date()))
        } else {
          this.$router.push({
            path: '/saler/wenzhang',
            query: {
              id: item.id,
              pid: item.pid,
              _t: Date.parse(new Date()),
            },
          })
        }
      },
    },
  }
</script>

<style rel="stylesheet/scss" lang="scss">
.navbar2 {
  width: 100%;
  & > img {
    width: 100%;
    margin-bottom: -2px;
  }
  main {
    top: 0;
    width: 1200px;
    height: 120px;
    position: absolute;
    transform: translateX(-50%);
    left: 50%;
    display: flex;
    align-items: center;
    z-index: 0;
    .navbar2_right {
      height: 36px;
      line-height: 36px;
      position: absolute;
      width: 220px;
      border: none;
      right: 0;
      .el-input__inner {
        background: #fff;
        color: #660000;
        opacity: 0.5;
        border-radius: 20px !important;
      }
    }
  }
  .index_items {
    width: 100%;
    background: #660000;
    .isactive {
      background: #fff;
      color: #660000;
    }
    ul {
      display: flex;
      justify-content: space-between;
      width: 1200px;
      margin: 0 auto;
      li {
        cursor: pointer;
        flex: 1;
        height: 50px;
        line-height: 50px;
        text-align: center;
        color: #fff;
        font-weight: bold;
        &:hover {
          background: #fff;
          color: #660000;
        }
      }
    }
  }
}
</style>

