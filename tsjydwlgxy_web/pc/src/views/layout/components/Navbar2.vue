<template>
  <div class="navbar2">
      <main>
        <router-link to="/index">
          <div class="navbar2_left">
            <img class="navbar2_left_img" src="@/assets/indexLOGO.png" />
          </div>
        </router-link>
        <div class="navbar2_right">
          <i class="el-icon-search"></i>
          <input type="text" v-model.trim="search_word" @keyup.enter="globalSearch" placeholder="搜索商品" />
          <div @click="globalSearch">搜索</div>
        </div>
      </main>
  </div>
</template>

<script>
export default {
  data() {
    return {
      timer: null,
      search_word: ""
    }
  },
  watch: {
    '$route': function(v) {
      // console.log(v)
      if(v.name != 'allGoods') this.search_word = ""
    }
  },
  methods: {
    globalSearch() {
      clearTimeout(this.timer);
      this.timer = setTimeout(() => {
        this.$emit('handleSearch', this.search_word)
      }, 300);
    }
  },
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.navbar2 {
  border-bottom: 2px solid #ff5500; 
  main {
    width: 1200px; 
    margin: 0 auto;
    height: 120px;
    // line-height: 120px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    .navbar2_left {
      display: flex;
      &_img {
        width: 100%;
        // height: 75px;
        // margin-right: 10px; 
      }
    }

    .navbar2_right {
      display: flex;
      height: 36px;
      line-height: 36px;
      margin-right: 150px;
      position: relative;
      i {
        position: absolute;
        left: 10px;
        top: 9px;
        font-size: 18px;
      }
      input {
        width: 370px;
        border: 1px solid #ff5500;
        padding: 10px 0 10px 30px;
        outline: none;
        font-size: 14px;
        color: #929292;
        border-top-left-radius: 5px;
        border-bottom-left-radius: 5px;
      }
      div {
        &:hover {
          opacity: .9;
        }
        transition: .2 ease-in-out;
        cursor: pointer;
        font-size: 16px;
        text-align: center;
        background: #ff5500;
        color: #fff;
        width: 80px;
        border-top-right-radius: 5px;
        border-bottom-right-radius: 5px;
      }
    }
  }
}
</style>

