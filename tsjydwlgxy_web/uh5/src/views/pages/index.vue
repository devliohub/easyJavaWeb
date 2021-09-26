<template>
  <div class="home">
    <van-swipe
      class="banner"
      :initial-swipe="0"
      :autoplay="2000"
      :show-indicators="false"
      ref="swiper"
    >
      <van-swipe-item v-for="(item, index) in homeList.list0" :key="index">
        <img :src="item.img" alt @click="handleOpen(item)" />
      </van-swipe-item>
    </van-swipe>

    <!-- 7大模块 -->
    <section class="mokuaibody">
      <div v-if="isloading" class="center_loading">
        <van-loading type="spinner" />
      </div>

      <div v-else class="ul_module">
        <div class="ul_module_text">
          <img src="@/assets/home/mokuai_title.png" alt="" />
        </div>
        <ul>
          <li
            v-for="(item, index) in moduleList"
            :key="index"
            @click="goKecheng(item)"
            :class="{ margin_right_0: index % 2 == 1 }"
          >
            <div>
              <i class="iconfont" :class="`icon-` + item.iconName"></i>
            </div>
            <div>
              <span style="font-weight: bold"> {{ item.name }}</span>
              <span v-if="index != moduleList.length - 1">
                {{ item.count }}门</span
              >
            </div>
          </li>
        </ul>
      </div>
    </section>

    <!-- 通识sections -->
    <section v-for="(item, index) in homeList.list1.slice(0, 3)" :key="index">
      <header>
        <span>{{ item.menuName }}</span>
        <span @click="viewMore(item)"
          >查看更多<i class="iconfont icon-gengduo"></i
        ></span>
      </header>
      <div class="ul_list">
        <ul>
          <li
            v-for="(_item, _index) in item.list"
            :key="_index"
            @click="goDesc(_item)"
          >
            <item-card-tiny :entity="_item" />
          </li>
        </ul>
      </div>
    </section>

    <!-- 四个banner -->
    <section
      style="padding: 0 15px"
      v-for="item in homeList.list2.slice(0, 2)"
      :key="item.id"
      @click="goWengzhang(item)"
    >
      <img class="sy_banner" :src="getImgUrl(item.name)" alt="" />
    </section>

    <section v-for="item in homeList.list1.slice(3)" :key="item.id">
      <header>
        <span>{{ item.menuName }}</span>
        <span @click="viewMore(item)"
          >查看更多<i class="iconfont icon-gengduo"></i
        ></span>
      </header>
      <div class="ul_list">
        <ul>
          <li
            v-for="(_item, _index) in item.list"
            :key="_index"
            @click="goDesc(_item)"
          >
            <item-card-tiny :entity="_item" />
          </li>
        </ul>
      </div>
    </section>

    <section
      style="padding: 0 15px"
      v-for="item in homeList.list2.slice(2)"
      :key="item.id"
      @click="goWengzhang(item)"
    >
      <img class="sy_banner" :src="getImgUrl(item.name)" alt="" />
    </section>

    <!-- 学生风采 -->
    <section>
      <header>
        <span>{{ homeList.list3.menuName }}</span>
        <span @click="viewMore(homeList.list3)"
          >查看更多<i class="iconfont icon-gengduo"></i
        ></span>
      </header>
      <div class="ul_free">
        <ul>
          <li
            style="width: calc(50% - 8px); margin-right: 15px"
            :class="{ margin_right_0: index % 2 == 1 }"
            v-for="(item, index) in homeList.list3.list"
            :key="index"
            @click="goDesc(item)"
          >
            <item-card-small :entity="item" />
          </li>
        </ul>
      </div>
    </section>
    <!-- 课程展示成果 -->
    <section>
      <header>
        <span>{{ homeList.list4.menuName }}</span>
        <span @click="viewMore(homeList.list4)"
          >查看更多<i class="iconfont icon-gengduo"></i
        ></span>
      </header>
      <div class="ul_free">
        <ul>
          <li
            v-for="(item, index) in homeList.list4.list"
            :key="index"
            @click="goDesc(homeList.list4.list[0])"
          >
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
  import { getCoursemodules, getHome } from '@/api'

  export default {
    name: 'home',
    components: { itemCardTiny, itemCardSmall, itemCardMid, indexFooter },
    data() {
      return {
        isloading: false,

        queryData: {
          type: 1,
          w_type: 1,
          module_type: 1,
          page_no: 1,
          page_size: 6,
        },
        homeList: {
          list1: [],
          list2: [],
          list3: {},
          list4: {
            list: [],
          },
        },
        moduleList: [], //7大模块
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
            iconName: 'quanbu',
          })
        }
        let res2 = await getHome()
        if (res2 && res2.errno == 200) {
          this.homeList = res2.result
        }
        this.isloading = false
      },
      viewMore(item) {
        this.$router.push('/list?id=' + item.menuId + '&pid=0')
      },
      handleOpen(item) {
        if (item.url) window.open(item.url)
      },
      getImgUrl(img) {
        return require('@/assets/home/' + img + '@2x.png')
      },
      goWengzhang(item) {
        this.$router.push('/list?id=' + item.id + '&pid=' + item.pid)
      },
      goDesc(item) {
        this.$router.push(
          '/desc?id=' +
            item.menu_pid +
            '&name=' +
            item.name +
            '&descId=' +
            item.id
        )
      },
      goKecheng(item) {
        this.$router.push('/kecheng?id=6&pid=0')
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
  overflow: hidden;
  .iconfont {
    font-size: 20px;
    font-weight: normal;
  }

  // 轮播
  .banner {
    background: #fff;
    margin-top: 50px;
    width: 100%;
    text-align: center;
    img {
      height: 18.75vw;
      width: 100%;
    }
  }

  & > section {
    position: relative;
    background: #fff;
    padding: 10px 15px;
    margin-bottom: 10px;

    .sy_banner {
      width: 100%;
    }

    // 精选免单
    & > header {
      padding: 10px 0;
      display: flex;
      align-items: center;
      justify-content: space-between;
      color: #c00900;
      span {
        font-size: 18px;
        font-weight: bold;
        &:last-child {
          font-size: 12px;
          font-weight: normal;
          display: flex;
          align-items: center;
          .iconfont {
            margin-left: 4px;
            font-size: 12px;
          }
        }
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
          padding: 10px 0;
        }
      }
    }
    .ul_module {
      width: 100%;
      &_text {
        padding: 25px 15px;
        text-align: center;
        img {
          width: 100%;
        }
      }
      & > ul {
        overflow: hidden;
        li {
          float: left;
          width: calc(50% - 7.5px);
          margin: 0 15px 15px 0;
          background: rgba(192, 9, 0, 0.1);
          text-align: center;
          border: 1px solid #660000;
          border-radius: 2px;
          color: #660000;
          div {
            padding: 10px;
            font-size: 14px;
            &:first-child {
              padding-bottom: 0;
              text-align: left;
            }
            &:last-child {
              display: flex;
              justify-content: space-between;
            }
          }
        }
      }
    }
  }

  .mokuaibody {
    background: url('../../assets/home/7大能力模块背景图@2x.png') no-repeat 100%
      100%;
    background-size: cover;
  }
}
</style>