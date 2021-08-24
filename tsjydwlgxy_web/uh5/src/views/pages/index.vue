<template>
  <div class="home">
    <van-swipe
      class="banner"
      :initial-swipe="0"
      :autoplay="2000"
      :show-indicators="false"
      ref="swiper"
    >
      <van-swipe-item>
        <img src="@/assets/banner1.png" alt @click="$router.push('/search')" />
      </van-swipe-item>
      <van-swipe-item>
        <img src="@/assets/banner2.png" alt @click="$router.push('/search')" />
      </van-swipe-item>
    </van-swipe>

    <!-- 7大模块 -->
    <section>
      <div class="ul_module">
        <ul>
          <li v-for="(item, index) in 8" :key="index"></li>
        </ul>
      </div>
    </section>

    <!-- 精选免单 -->
    <section>
      <header>
        <span>课程成果展示</span>
      </header>
      <div class="ul_list">
        <ul>
          <li v-for="(item, index) in tinyList" :key="index">
            <item-card-tiny :entity="item" />
          </li>
        </ul>
      </div>
    </section>

    <!-- 种类推荐 -->
    <section v-for="(cate, x) in cateList" :key="x">
      <div class="sy_banner">
        <img :src="cate.images_h5" @click="$router.push('/list')" />
      </div>
      <div class="ul_wrapper">
        <ul v-if="cate.activity_list.length > 0">
          <li v-for="(item, y) in cate.activity_list" :key="y">
            <item-card-small :entity="item" />
          </li>
        </ul>
        <div class="no_data" v-else>暂无数据</div>
      </div>
    </section>

    <!-- 精选免单 -->
    <section>
      <header>
        <span>课程成果展示</span>
      </header>
      <div class="ul_free">
        <ul>
          <li v-for="(item, index) in topList" :key="index">
            <item-card-mid :entity="item" />
          </li>
        </ul>
      </div>
    </section>

    <indexfooter />
  </div>
</template>
<script>
  import itemCardTiny from '@/components/item_card_tiny'
  import itemCardSmall from '@/components/item_card_small'
  import itemCardMid from '@/components/item_card_mid'
  import indexfooter from './footer.vue'
  import { indexSearch, cateSearch, cateTitle } from '@/api'

  export default {
    name: 'home',
    components: { itemCardTiny, itemCardSmall, itemCardMid, indexfooter },
    data() {
      return {
        isloading: false,

        classicTabs: [],
        queryData: {
          type: 1,
          w_type: 1,
          module_type: 1,
          page_no: 1,
          page_size: 6,
        },
        topList: [], //3个大块
        cateList: [], //分类列表
        tinyList: [
          { title: '活动预告 | 内容标题内容标题', time: '2021-07-01' },
          { title: '文章内容标题内容标题', time: '2021-07-02' },
          { title: '1文章内容标题内容标题2', time: '2021-07-03' },
        ],
      }
    },
    methods: {
      async getData() {
        this.isloading = true
        let res = await indexSearch(this.queryData)
        if (res && res.error.errno == 200) this.topList = res.data
        this.isloading = false
      },
      async getCateData() {
        let res = await cateSearch()
        if (res && res.error.errno == 200) {
          this.cateList = res.data
        }
      },
      handleTabClick(index, name) {
        let _target = this.classicTabs.find((el) => el.short_name == name)
        setTimeout(() => {
          this.$router.push('/limitFree?cid=' + _target.id)
        }, 300)
      },
    },
    async mounted() {
      this.$nextTick(() => {
        window.addEventListener('scroll', this.handleScroll)
      })
      // 处理第一次进入session为空情况
      if (!JSON.parse(window.sessionStorage.getItem('tpyeArr'))) {
        let res = await cateTitle({
          account: 'test1',
          password: '123456',
        })
        this.classicTabs = res.data
        window.sessionStorage.setItem('tpyeArr', JSON.stringify(res.data))
      } else {
        this.classicTabs = JSON.parse(window.sessionStorage.getItem('tpyeArr'))
      }
      this.getData()
      this.getCateData()
    },
  }
</script>
<style lang="scss" scope>
.home {
  width: 100%;
  overflow: auto;

  // 轮播
  .banner {
    background: #fff;
    border: 1px solid red;
    margin-left: 15px;
    margin-top: 100px;
    width: 345px;
    text-align: center;
    border-radius: 5px;
    img {
      width: 100%;
      height: 142px;
    }
  }

  & > section {
    position: relative;
    background: #fff;
    padding: 10px 15px;
    margin-bottom: 15px;

    .sy_banner {
      img {
        width: 345px;
        text-align: center;
        border-radius: 2px;
        height: 105px;
        margin-bottom: 10px;
      }
    }

    // 子项目item
    .ul_wrapper {
      width: 100%;
      text-align: center;
      & > ul {
        display: flex;
        flex-wrap: wrap;
        li {
          border: 1px solid red;
          width: 50%;
          padding: 10px 5px;
        }
      }
    }

    // 精选免单
    & > header {
      padding: 10px 0;
      span {
        font-size: 16px;
        padding: 0 8px;
        font-weight: bold;
        border-left: 3px solid #ff5500;
      }
    }
    .ul_list {
      width: 100%;
      ul {
        width: 100%;
        display: flex;
        flex-wrap: wrap;
        li {
          font-size: 14px;
          width: 100%;
          padding: 10px 0;
        }
      }
    }
    .ul_free {
      width: 100%;
      ul {
        width: 100%;
        display: flex;
        flex-wrap: wrap;
        li {
          width: 100%;
          padding: 20px 0;
        }
      }
    }
    .ul_module {
      width: 100%;
      & > ul {
        display: flex;
        flex-wrap: wrap;
        li {
          width: 40%;
          padding: 30px 5px;
          margin: 10px 5%;
          background: #c00900 10%;
          color: #660000;
        }
      }
    }
  }
}
</style>