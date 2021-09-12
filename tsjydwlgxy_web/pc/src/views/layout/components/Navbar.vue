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
          @click="handleGo(item)"
          @mouseenter="handleMouseSet(item, true)"
          @mouseleave="handleMouseSet(item, false)"
          :class="{
            isactive:
              $route.query.id == item.id ||
              ($route.name == 'kecheng' && item.name == '通识课程') ||
              ($route.name == 'index' && item.id == 1),
            ishover: item.ishover,
          }"
        >
          {{ item.name }}
          <ul v-if="item.sub_menus.length > 0 && item.ishover">
            <li
              class="innerLi"
              v-for="(_item, _index) in item.sub_menus"
              :key="_index"
              @click.stop="handleGo(_item)"
              :class="{
                ishover: item.ishover,
              }"
            >
              {{ _item.name }}
            </li>
          </ul>
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
      handleMouseSet(item, flag) {
        this.cateTitleList.slice(0, 9).map((el) => {
          if (item.id == el.id) {
            this.$set(el, 'ishover', flag)
          }
        })
      },
      handleGo(item) {
        console.log(JSON.parse(JSON.stringify(item)))
        if (item.type == 2) {
          window.open(item.url)
        } else if (item.id == 1) {
          this.$router.push('/index?_t=' + Date.parse(new Date()))
        } else if (item.id == 6) {
          this.$router.push({
            path: '/saler/kecheng',
            query: {
              id: item.id,
              pid: item.pid,
              name: item.name,
              _t: Date.parse(new Date()),
            },
          })
        } else {
          this.$router.push({
            path: '/saler/wenzhang',
            query: {
              id: item.id,
              pid: item.pid,
              name: item.name,
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
    // height: 120px;
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
      top: 36px;
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
    & > ul {
      display: flex;
      justify-content: space-between;
      width: 1200px;
      margin: 0 auto;
      .ishover {
        background: #fff;
        color: #660000;
      }
      li {
        cursor: pointer;
        background: #660000;
        flex: 1;
        height: 50px;
        line-height: 50px;
        text-align: center;
        color: #fff;
        font-weight: bold;
        z-index: 999;
      }
      .innerLi {
        color: #fff;
        background: #660000;
        &:hover {
          background: #fff;
          color: #660000;
        }
      }
      .isactive {
        background: #fff;
        color: #660000;
      }
    }
  }
}
</style>

