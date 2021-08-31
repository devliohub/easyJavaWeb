<template>
  <div class="navbar2">
    <main>
      <div class="navbar2_right">
        <!-- <el-input placeholder="关键词搜索" v-model="search_word">
          <i
            class="el-icon-search el-input__icon"
            slot="suffix"
            @click="handleIconClick"
          >
          </i>
        </el-input> -->
      </div>
    </main>

    <!-- tab栏目 -->
    <div class="index_items">
      <ul>
        <router-link
          v-for="(item, index) in cateTitleList"
          :key="index"
          :to="'/saler/wenzhang?id=' + item.id"
        >
          <li>
            <span :class="item.icon"></span>
            {{ item.name }}
          </li>
        </router-link>
      </ul>
    </div>
  </div>
</template>

<script>
  import { getMunes } from '@/api'
  export default {
    data() {
      return {
        search_word: '',
        cateTitleList: [],
      }
    },
    watch: {
      $route: function (v) {
        // console.log(v)
        if (v.name != 'allGoods') this.search_word = ''
      },
    },
    async mounted() {
      // 处理第一次进入session为空情况
      if (!JSON.parse(window.sessionStorage.getItem('menuArr'))) {
        let res = await getMunes()
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
    },
  }
</script>

<style rel="stylesheet/scss" lang="scss">
.navbar2 {
  background: url('../../../assets/home/top_bg.png');
  main {
    width: 1200px;
    margin: 0 auto;
    height: 120px;
    display: flex;
    justify-content: center;
    align-items: center;
    .navbar2_left {
      display: flex;
      margin-right: 100px;
      &_img {
        border: 1px solid red;
      }
    }

    .navbar2_right {
      display: flex;
      height: 36px;
      line-height: 36px;
      margin-right: 150px;
      position: relative;
      .el-input__inner {
        border-radius: 20px !important;
      }
    }
  }
  .index_items {
    width: 100%;
    background: #660000;
    ul {
      display: flex;
      justify-content: space-between;
      width: 1200px;
      margin: 0 auto;
      a {
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

