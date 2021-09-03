<template>
  <!-- 首页 -->
  <div class="index">
    <!-- 轮播图 -->
    <div class="index_banner">
      <!-- 背景轮播 -->
      <el-carousel
        :interval="6000"
        trigger="click"
        arrow="always"
        :height="'18.75vw'"
      >
        <el-carousel-item v-for="(item, index) in homeList.list0" :key="index">
          <img class="bannerimg1" :src="item.img" alt="" />
        </el-carousel-item>
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
          v-for="(item, index) in homeList.list1"
          :key="index"
          :class="{ margin_right_0: index % 2 == 1 }"
        >
          <header>
            <span>{{ item.menuName }}</span>
            <span @click="viewMore(item)"
              >查看更多<i class="el-icon-d-arrow-right"></i
            ></span>
          </header>
          <ul>
            <li
              v-for="(_item, _index) in item.list"
              :key="_index"
              @click="goDesc(_item)"
            >
              <div class="title">
                <div class="dotter"></div>
                <span>{{ _item.title }}</span>
              </div>
              <div class="time">
                {{ dayjs(_item.create_time * 1000).format('YYYY-MM-DD') }}
              </div>
            </li>
          </ul>
        </div>
      </main>
    </div>
    <!-- 四个banner -->
    <div class="index_luntan">
      <ul>
        <li
          v-for="(item, index) in homeList.list2"
          :key="index"
          :class="{ margin_right_0: index == 3 }"
          @click="goWengzhang(item)"
        >
          {{ item.name }}
        </li>
      </ul>
    </div>
    <!-- 学生风采 -->
    <div class="index_xuesheng">
      <main>
        <header>
          <span>{{ homeList.list3.menuName }}</span>
          <span @click="viewMore(homeList.list3)"
            >查看更多<i class="el-icon-d-arrow-right"></i
          ></span>
        </header>
        <ul>
          <li
            v-for="(_item, _index) in homeList.list3.list"
            :key="_index"
            @click="goDesc(_item)"
          >
            <img :src="_item.cover" alt="" />
            <div class="title">
              {{ _item.title }}
            </div>
            <div class="time">
              {{ dayjs(_item.create_time * 1000).format('YYYY-MM-DD') }}
            </div>
          </li>
        </ul>
      </main>
    </div>
    <!-- 课程成果 -->
    <div class="index_kecheng">
      <main>
        <header>
          <span>{{ homeList.list4.menuName }}</span>
          <span @click="viewMore(homeList.list4)"
            >查看更多<i class="el-icon-d-arrow-right"></i
          ></span>
        </header>
        <div v-if="homeList.list4.list.length > 0">
          <div
            class="index_kecheng_left"
            @click="goDesc(homeList.list4.list[0])"
          >
            <img :src="homeList.list4.list[0].cover" alt="" />
            <div class="title">
              {{ homeList.list4.list[0].title }}
            </div>
            <div class="time">
              {{
                dayjs(homeList.list4.list[0].create_time * 1000).format(
                  'YYYY-MM-DD'
                )
              }}
            </div>
          </div>
          <ul class="index_kecheng_right">
            <li
              v-for="(_item, _index) in homeList.list4.list"
              :key="_index"
              @click="goDesc(_item)"
            >
              <img :src="_item.cover" alt="" />
              <div>
                <div class="title">
                  {{ _item.title }}
                </div>
                <div class="time">
                  {{ dayjs(_item.create_time * 1000).format('YYYY-MM-DD') }}
                </div>
              </div>
            </li>
          </ul>
        </div>
      </main>
    </div>
  </div>
</template>

<script>
  import { getHome } from '@/api'

  export default {
    components: {},
    computed: {},
    data() {
      return {
        isloading: false,
        tabList: [], // tab公用栏
        homeList: {
          list1: [],
          list2: [],
          list3: {},
          list4: {
            list: [],
          },
        },
      }
    },
    mounted() {
      this.initData()
    },
    methods: {
      async initData() {
        let res = await getHome()
        if (res && res.errno == 200) {
          this.homeList = res.result
        }
      },
      goDesc(item) {
        this.$router.push(
          '/saler/wenzhangdesc?id=' +
            item.menu_pid +
            '&name=' +
            item.name +
            '&descId=' +
            item.id
        )
      },
      goWengzhang(item) {
        this.$router.push('/saler/wenzhang?id=' + item.id + '&pid=' + item.pid)
      },
      viewMore(item) {
        this.$router.push('/saler/wenzhang?id=' + item.menuId + '&pid=0')
      },
    },
  }
</script>
<style lang="scss" scoped>
.index {
  width: 100%;

  .index_banner {
    .bannerimg1 {
      width: 100%;
      // background: url('../../../assets/home/banner1.png') no-repeat 100% 100%;
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
            cursor: pointer;
            &:hover {
              background: #eee;
            }
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

  .index_luntan {
    width: 100%;
    margin-bottom: 25px;
    ul {
      width: 1200px;
      margin: 0 auto;
      display: flex;
      text-align: center;
      li {
        cursor: pointer;
        background: url('../../../assets/home/word_bg.jpg') 100% 100% no-repeat;
        flex: 1;
        font-family: 'Courier New', Courier, monospace;
        font-weight: bold;
        color: #660000;
        margin-right: 20px;
        padding: 15px 0;
        letter-spacing: 5px;
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
    margin-top: 25px;
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
        cursor: pointer;
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

