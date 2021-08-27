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
        <img src="@/assets/bannerH5.png" alt @click="$router.push('/search')" />
      </van-swipe-item>
    </van-swipe>

    <!-- 7大模块 -->
    <section>
      <div v-if="isloading" class="center_loading">
        <van-loading type="spinner" />
      </div>

      <div v-else class="ul_module">
        <div class="ul_module_text">
          <span>{{ moduleList.length - 1 }}</span>
          大能力模块，构建通识教育课程体系
        </div>
        <ul>
          <li v-for="(item, index) in moduleList" :key="index">
            <div><van-icon :name="iconArr[index]" /></div>
            <div>
              <span style="font-weight: bold"> {{ item.name }}</span>
              <span> 80门</span>
            </div>
          </li>
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

    <indexFooter />
  </div>
</template>
<script>
  import itemCardTiny from '@/components/item_card_tiny'
  import itemCardSmall from '@/components/item_card_small'
  import itemCardMid from '@/components/item_card_mid'
  import indexFooter from './footer.vue'
  import { getCoursemodules } from '@/api'

  export default {
    name: 'home',
    components: { itemCardTiny, itemCardSmall, itemCardMid, indexFooter },
    data() {
      return {
        isloading: false,

        iconArr: [
          'location-o',
          'like-o',
          'star-o',
          'phone-o',
          'setting-o',
          'fire-o',
          'coupon-o',
          'cart-o',
        ], // 模块icon
        queryData: {
          type: 1,
          w_type: 1,
          module_type: 1,
          page_no: 1,
          page_size: 6,
        },
        moduleList: [], //7大模块
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
        let res = await getCoursemodules(this.queryData)
        if (res && res.errno == 200) {
          this.moduleList = res.result
          this.moduleList.push({
            code: 0,
            name: '全部课程',
          })
        }
        this.isloading = false
      },
    },
    async mounted() {
      this.$nextTick(() => {
        window.addEventListener('scroll', this.handleScroll)
      })
      this.getData()
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
    margin-top: 50px;
    width: 100%;
    text-align: center;
    img {
      width: 100%;
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
      &_text {
        padding: 25px 0;
        font-size: 18px;
        text-align: center;
        color: #c00900;
        span {
          font-size: 24px;
          font-family: Impact, Haettenschweiler, 'Arial Narrow Bold', sans-serif;
        }
      }
      & > ul {
        display: flex;
        flex-wrap: wrap;
        li {
          width: 40%;
          margin: 10px 5%;
          background: rgba(192, 9, 0, 0.1);
          text-align: center;
          border: 1px solid #660000;
          border-radius: 2px;
          color: #660000;
          div {
            padding: 10px;
            &:first-child {
              padding-bottom: 0;
              font-size: 20px;
              text-align: left;
            }
            &:last-child {
              font-size: 14px;
              display: flex;
              justify-content: space-between;
            }
          }
        }
      }
    }
  }
}
</style>