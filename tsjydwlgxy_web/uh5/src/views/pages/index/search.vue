<template>
  <div class="search">
    <van-search
      v-model="search_value"
      show-action
      placeholder="请输入搜索关键词"
      @search="onSearch"
      @cancel="onCancel"
    />
    <section>
      <van-dropdown-menu>
        <van-dropdown-item v-model="value1" :options="option1" />
      </van-dropdown-menu>
    </section>
    <div class="ul_wrapper">
      <ul>
        <li v-for="(item, index) in dataList" :key="index">
          <item-card-small :entity="item" />
        </li>
      </ul>

      <div class="pagin">
        <span>上一页</span>
        <span class="num">{{ queryData.page_no }}/{{ totalPage }}</span>
        <span>下一页</span>
      </div>
    </div>
  </div>
</template>
<script>
  import itemCardSmall from '@/components/item_card_small'
  import { indexSearch } from '@/api'

  export default {
    name: 'search',
    components: { itemCardSmall },
    data() {
      return {
        search_value: '',
        dataList: [],
        queryData: {
          type: 1,
          w_type: 1,
          module_type: 1,
          page_no: 1,
          page_size: 50,
        },
        totalPage: 0,

        value1: 0,
        option1: [
          { text: '全部(10)', value: 0 },
          { text: '公共必修课', value: 1 },
          { text: '公共选修课', value: 2 },
          { text: '核心限选课', value: 3 },
        ],
      }
    },
    mounted() {
      this.getData()
    },
    methods: {
      async getData() {
        this.isloading = true
        let res = await indexSearch(this.queryData)
        if (res && res.error.errno == 200) {
          this.dataList = res.data
          this.totalPage = parseInt(res.data.length / 10)
        }
        this.isloading = false
      },
      onSearch(val) {
        console.log(val)
      },
      onCancel() {
        this.$router.go(-1)
      },
    },
  }
</script>
<style lang="scss" scope>
.search {
  position: relative;
  margin-top: 50px;
  section {
    width: 100%;
    display: block;
    background: #fff;
    font-size: 12px;
    .van-dropdown-menu__bar {
      box-shadow: none;
    }
    .van-dropdown-menu {
      width: 100px;
    }
  }

  // 子项目item
  .ul_wrapper {
    width: 100%;
    text-align: center;
    padding: 0 15px;
    background: #fff;
    & > ul {
      display: flex;
      flex-wrap: wrap;
      li {
        width: 50%;
        padding: 10px 5px;
      }
    }
  }

  .pagin {
    padding: 20px 0;
    display: flex;
    justify-content: space-between;
    font-size: 14px;
    .num {
      background: #c00900;
      color: #fff;
    }
    & > span {
      padding: 8px 30px;
      border: 1px solid #c5c5c5;
      color: #444;
      border-radius: 4px;
    }
  }
}
</style>