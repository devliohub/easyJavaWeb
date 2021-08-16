<template>
  <!-- 首页 -->
  <div class="index">
    <!-- 轮播图 -->
    <div class="index_banner">
      <!-- 背景轮播 -->
      <el-carousel :interval="6000" trigger="click" arrow="always">
        <el-carousel-item>
          <div class="bannerimg1"></div>
        </el-carousel-item>
        <!-- <el-carousel-item>
          <div class="bannerimg2"></div>
        </el-carousel-item> -->
      </el-carousel>
    </div>
    <!-- 七大能力模块 -->
    <div class="index_modules">
      <img src="../../../assets/home/ability_bg.png" alt="" />
      <!-- <ul>
        <li
          v-for="(item, index) in 8"
          :key="index"
          :class="{ margin_right_0: index % 4 == 3 }"
        >
          {{ item }}
        </li>
      </ul> -->
    </div>
    <!-- 通识sections -->
    <div class="index_tongshi">
      <main>
        <div
          v-for="(item, index) in 4"
          :key="index"
          :class="{ margin_right_0: index % 2 == 1 }"
        >
          <header>
            <span>通识新闻</span>
            <span>查看更多<i class="el-icon-d-arrow-right"></i></span>
          </header>
          <ul>
            <li v-for="(_item, _index) in test_entity" :key="_index">
              <div class="title">
                <div class="dotter"></div>
                <span>{{ _item.desc }}</span>
              </div>
              <div class="time">{{ _item.time }}</div>
            </li>
          </ul>
        </div>
      </main>
    </div>
    <!-- 学生风采 -->
    <div class="index_xuesheng">
      <main>
        <header>
          <span>学生风采</span>
          <span>查看更多<i class="el-icon-d-arrow-right"></i></span>
        </header>
        <ul>
          <li v-for="(_item, _index) in test_entity" :key="_index">
            <img src="../../../assets/2parts_xqg.png" alt="" />
            <div class="title">
              {{ _item.desc }}
            </div>
            <div class="time">{{ _item.time }}</div>
          </li>
        </ul>
      </main>
    </div>
    <!-- 课程成果 -->
    <div class="index_kecheng">
      <main>
        <header>
          <span>课程成果展示</span>
          <span>查看更多<i class="el-icon-d-arrow-right"></i></span>
        </header>
        <div>
          <div class="index_kecheng_left">
            <img src="../../../assets/2parts_xqg.png" alt="" />
            <div class="title">dsdasdadsa1231231</div>
            <div class="time">2021-09-01</div>
          </div>
          <ul class="index_kecheng_right">
            <li v-for="(_item, _index) in test_entity" :key="_index">
              <img src="../../../assets/2parts_xqg.png" alt="" />
              <div>
                <div class="title">
                  {{ _item.desc }}
                </div>
                <div class="time">{{ _item.time }}</div>
              </div>
            </li>
          </ul>
        </div>
      </main>
    </div>
  </div>
</template>

<script>
  import { cateTitle, cateDesc, indexSearch } from '@/api/common/common.js'
  import itemCard from '@/components/itemCard.vue'
  import { mapGetters } from 'vuex'

  export default {
    components: { itemCard },
    computed: {
      ...mapGetters(['name']),
      isLogin() {
        return this.$store.state.user.token
      },
    },
    data() {
      return {
        isloading: false,
        cateDescList: [], // 下方主活动

        tabList: [], // tab公用栏
        test_entity: [
          {
            desc: '活动预告 | 内容标题内容标题内容标题内容标题内容标题内容标题',
            time: '2021-07-01',
          },
          {
            desc: '文章内容标题内题文章标题内容标题文章内容标题内容标题',
            time: '2021-07-12',
          },
          {
            desc: '活动预告 | 内容标题内容标题内容标题内容标题内容标题内容标题',
            time: '2021-07-21',
          },
          {
            desc: '文章内容标题内容标题文章内容标内题内容标题文章内容标题内容标题',
            time: '2021-07-01',
          },
        ],
      }
    },
    mounted() {
      this.initData()
    },
    methods: {
      async initData() {
        this.getThreeTabs(1)

        let res1 = await cateDesc()
        if (res1 && res1.error.errno == 200) {
          this.cateDescList = res1.data
        }
      },
      async getThreeTabs(num) {
        this.isloading = true
        let res3 = await indexSearch({
          type: 1,
          w_type: num,
          module_type: 1,
          page_no: 1,
          page_size: 6,
        })
        if (res3 && res3.error.errno == 200) {
          this.tabList = res3.data
        }
        this.isloading = false
      },
      tabClick(val, event) {
        let num = parseInt(val.index) + 1
        this.getThreeTabs(num)
      },
      handleClickImgtypes(i) {
        this.$router.push('/allGoods/' + parseInt(i))
      },
    },
  }
</script>
<style lang="scss" scoped>
.index {
  width: 100%;

  .index_banner {
    // position: relative;

    .el-carousel--horizontal {
      overflow: hidden;
    }
    .bannerimg1 {
      width: 1920px;
      height: 360px;
      background: url('../../../assets/home/banner1.png') no-repeat 100% 100%;
    }
  }

  .index_modules {
    width: 100%;
    margin-bottom: 50px;
    & > img {
      width: 100%;
    }
    ul {
      width: 1200px;
      margin: 0 auto;
      position: relative;
      display: flex;
      flex-wrap: wrap;
      li {
        background: rgb(238, 218, 216);
        margin-right: 20px;
        margin-bottom: 30px;
        border: 1px solid #660000;
        color: #660000;
        flex: calc(25% - 15px);
        height: 65px;
        line-height: 65px;
        text-align: center;
      }
    }
  }

  .index_tongshi {
    width: 100%;
    main {
      width: 1200px;
      margin: 0 auto;
      overflow: hidden;
      & > div {
        width: 580px;
        float: left;
        margin: 0 40px 40px 0;
        ul {
          li {
            display: flex;
            justify-content: space-between;
            padding: 10px 0;
            .title {
              font-weight: bold;
              color: #444;
              display: flex;
              align-items: center;
              .dotter {
                margin-right: 8px;
                width: 6px;
                height: 6px;
                border-radius: 50%;
                background: #d9ae73;
              }
            }
            .time {
              color: #969696;
            }
          }
        }
      }
    }
  }

  .index_xuesheng {
    width: 100%;
    main {
      width: 1200px;
      margin: 0 auto;
      & > ul {
        overflow: hidden;
        li {
          float: left;
          width: 22%;
          margin-right: 3%;
          img {
            width: 100%;
          }
          .title {
            padding: 10px 0;
            color: #444;
            font-weight: bold;
          }
          .time {
            color: #969696;
          }
        }
      }
    }
  }

  .index_kecheng {
    width: 100%;
    main {
      width: 1200px;
      margin: 0 auto;
      & > div {
        display: flex;
        padding: 30px 20px;
        .index_kecheng_left {
          flex: 3;
          margin-right: 50px;
          img {
            width: 100%;
          }
          .title {
            padding: 20px 0;
            font-size: 18px;
            font-weight: bold;
          }
          .time {
            text-align: right;
            color: #969696;
          }
        }
        .index_kecheng_right {
          flex: 2;
          overflow: hidden;
          // padding: 20px;
          li {
            display: flex;
            align-items: center;
            margin-bottom: 15px;
            & > img {
              width: 180px;
              margin-right: 24px;
            }
            & > div {
              flex: 1;
              .title {
                padding: 10px 0;
                color: #444;
                font-weight: bold;
              }
              .time {
                color: #969696;
              }
            }
          }
        }
      }
    }
  }

  main header {
    display: flex;
    align-items: center;
    color: #c00900;
    margin-bottom: 20px;
    span {
      padding: 10px 0;
      &:first-child {
        font-size: 18px;
        font-weight: bold;
        border-bottom: 3px solid #c00900;
      }
      &:last-child {
        margin-left: 5px;
        flex: 1;
        text-align: right;
        font-size: 12px;
        border-bottom: 1px solid #c00900;
      }
    }
  }
}
</style>

