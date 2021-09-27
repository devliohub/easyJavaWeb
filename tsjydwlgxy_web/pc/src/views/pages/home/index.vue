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
        :height="'18.7vw'"
      >
        <el-carousel-item v-for="(item, index) in homeList.list0" :key="index">
          <img
            @click="handleOpen(item)"
            class="bannerimg1"
            :src="item.img"
            alt=""
          />
        </el-carousel-item>
      </el-carousel>
    </div>
    <!-- 七大能力模块 -->
    <div class="index_modules">
      <main>
        <img src="../../../assets/home/mokuai_title.png" alt="" />
      </main>
      <ul>
        <li
          v-for="(item, index) in moduleList"
          :key="index"
          :class="{ margin_right_0: index % 4 == 3 }"
          @click="goKecheng(item)"
        >
          <div class="li_div">
            <i
              v-if="index != moduleList.length - 1"
              class="iconfont"
              :class="`icon-` + item.iconName"
            ></i>
            &nbsp;&nbsp;
            <span> {{ item.name }}</span>
            &nbsp;&nbsp;&nbsp;
            <span v-if="index != moduleList.length - 1">
              {{ item.count }} 门</span
            >
            <i
              v-if="index == moduleList.length - 1"
              class="iconfont"
              :class="`icon-` + item.iconName"
            ></i>
          </div>
        </li>
      </ul>
    </div>
    <!-- 通识sections -->
    <div class="index_tongshi">
      <main>
        <div
          v-for="(item, index) in homeList.list1"
          :key="index"
          :class="{
            margin_right_0: index % 2 == 1,
            margin_bottom_0: index > homeList.list1.length - 3,
          }"
        >
          <header>
            <span>{{ item.menuName }}</span>
            <span @click="viewMore(item)"
              >查看更多<i class="iconfont icon-gengduo"></i
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
                {{ dayjs(_item.publish_time * 1000).format('YYYY-MM-DD') }}
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
          <img :src="getImgUrl(item.name)" alt="" />
        </li>
      </ul>
    </div>
    <!-- 学生风采 -->
    <div class="index_xuesheng">
      <main>
        <header>
          <span>{{ homeList.list3.menuName }}</span>
          <span @click="viewMore(homeList.list3)"
            >查看更多<i class="iconfont icon-gengduo"></i
          ></span>
        </header>
        <ul>
          <li
            v-for="(_item, _index) in homeList.list3.list"
            :key="_index"
            @click="goDesc(_item)"
            :class="{ margin_right_0: _index % 4 == 3 }"
          >
            <el-image :src="_item.cover" alt="">
              <div slot="error" class="image-slot">
                <img src="@/assets/wenzhangMR.jpg" alt="" />
              </div>
            </el-image>
            <div class="title">
              {{ _item.title }}
            </div>
            <div class="time">
              {{ dayjs(_item.publish_time * 1000).format('YYYY-MM-DD') }}
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
            >查看更多<i class="iconfont icon-gengduo"></i
          ></span>
        </header>
        <div v-if="homeList.list4.list.length > 0">
          <div
            class="index_kecheng_left"
            @click="goDesc(homeList.list4.list[0])"
          >
            <el-image :src="homeList.list4.list[0].cover" alt="">
              <div slot="error" class="image-slot">
                <img src="@/assets/kechengMR.jpg" alt="" />
              </div>
            </el-image>
            <div class="title">
              {{ homeList.list4.list[0].title }}
            </div>
            <div class="time">
              {{
                dayjs(homeList.list4.list[0].publish_time * 1000).format(
                  'YYYY-MM-DD'
                )
              }}
            </div>
          </div>
          <ul class="index_kecheng_right">
            <li
              v-for="(_item, _index) in homeList.list4.list.slice(1)"
              :key="_index"
              @click="goDesc(_item)"
              :class="{ padding_top10: _index != 0 }"
            >
              <el-image :src="_item.cover" alt="">
                <div slot="error" class="image-slot">
                  <img src="@/assets/kechengMR.jpg" alt="" />
                </div>
              </el-image>
              <div>
                <div class="title">
                  {{ _item.title }}
                </div>
                <div class="time">
                  {{ dayjs(_item.publish_time * 1000).format('YYYY-MM-DD') }}
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
  import { getHome, getCoursemodules } from '@/api'

  export default {
    components: {},
    computed: {},
    data() {
      return {
        isloading: false,
        moduleList: [], //7大模块
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
        let res = await getCoursemodules()
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
      },
      getImgUrl(img) {
        return require('@/assets/home/' + img + '@2x.png')
      },
      handleOpen(item) {
        if (item.url) window.open(item.url)
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
        this.$router.push(
          '/saler/wenzhang?id=' +
            item.id +
            '&pid=' +
            item.pid +
            '&name=' +
            item.name
        )
      },
      viewMore(item) {
        this.$router.push(
          '/saler/wenzhang?id=' + item.menuId + '&pid=' + item.menuPId
        )
      },
      goKecheng(item) {
        this.$router.push(
          '/saler/kecheng?module_id=' + item.code + '&name=' + item.name
        )
      },
    },
  }
</script>
<style lang="scss" scoped>
.index {
  width: 100%;
  .iconfont {
    font-size: 20px;
    font-weight: normal;
  }
  .index_banner {
    .bannerimg1 {
      width: 100%;
      cursor: pointer;
      // background: url('../../../assets/home/banner1.png') no-repeat 100% 100%;
    }
  }

  .index_modules {
    width: 100%;
    padding-bottom: 20px;
    background: url('../../../assets/home/mokuai_body.png') no-repeat 100% 100%;
    background-size: cover;
    main {
      width: 1200px;
      margin: 0 auto;
      overflow: hidden;
      text-align: center;
      display: flex;
      flex-direction: column;
      align-items: center;
      & > img {
        width: 412px;
        margin: 30px 0 40px 0;
      }
    }

    ul {
      width: 1200px;
      margin: 0 auto;
      position: relative;
      display: flex;
      flex-wrap: wrap;
      li {
        cursor: pointer;
        background: rgba(238, 218, 216, 0.5);
        margin-right: 20px;
        margin-bottom: 30px;
        font-size: 16px;
        border: 1px solid #660000;
        color: #660000;
        flex: calc(25% - 15px);
        height: 65px;
        line-height: 65px;
        text-align: center;
        &:hover {
          color: goldenrod;
          background: #660000;
          border: 1px solid goldenrod;
        }
        .li_div {
          font-weight: bold;
          display: flex;
          display: -webkit-flex;
          align-items: center;
          -webkit-align-items: center;
          justify-content: center;
          -webkit-justify-content: center;
        }
      }
    }
  }

  .index_tongshi {
    width: 100%;
    padding-top: 50px;
    main {
      width: 1200px;
      margin: 0 auto;
      overflow: hidden;
      & > div {
        width: 580px;
        float: left;
        margin: 0 40px 50px 0;
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
              span {
                overflow: hidden;
                white-space: nowrap;
                text-overflow: ellipsis;
                max-width: 460px;
              }
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
    margin-bottom: 50px;
    ul {
      width: 1200px;
      margin: 0 auto;
      display: flex;
      text-align: center;
      li {
        cursor: pointer;
        flex: 1;
        margin: 20px 20px 0 0;
        img {
          width: 100%;
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
        margin-top: 25px;
        overflow: hidden;
        li {
          float: left;
          width: 280px;
          margin-right: 26px;
          cursor: pointer;
          img,
          .el-image {
            height: 160px;
            width: 100%;
          }
          .title {
            margin: 10px 0;
            color: #444;
            font-weight: bold;
            overflow: hidden;
            text-overflow: ellipsis;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            max-width: 100%;
            line-height: 1.5;
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
    background: url('../../../assets/home/首页底部背景图.png') no-repeat top
      left;
    background-size: cover;
    main {
      padding-top: 50px;
      width: 1200px;
      margin: 0 auto;
      padding-bottom: 100px;
      & > div {
        margin-top: 50px;
        display: flex;
        padding: 30px 20px;
        background: url('../../../assets/home/课程成果展示背景.png') no-repeat
          top left;
        background-size: cover;

        .index_kecheng_left {
          width: 669px;
          cursor: pointer;
          .el-image {
            border: 20px solid #fff;
            width: 630px;
            height: 360px;
            .image-slot img {
              width: 590px;
              height: 320px;
            }
          }
          .title {
            margin: 20px 0;
            font-size: 18px;
            font-weight: bold;
            color: #fff;
            width: 95%;
            line-height: 1.5;
            overflow: hidden;
            text-overflow: ellipsis;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
          }
          .time {
            margin-right: 40px;
            text-align: right;
            color: #969696;
          }
        }
        .index_kecheng_right {
          flex: 1;
          overflow: hidden;
          .padding_top10 {
            padding-top: 0 !important;
          }
          li {
            cursor: pointer;
            overflow: hidden;
            background: #fff;
            padding: 20px;
            .el-image {
              float: left;
              width: 160px;
              height: 90px;
              .image-slot img {
                width: 160px;
                height: 90px;
              }
            }
            & > div {
              float: left;
              width: calc(100% - 220px);
              margin-right: 24px;
              display: flex;
              flex-direction: column;
              justify-content: space-between;
              height: 90px;

              .title {
                overflow: hidden;
                text-overflow: ellipsis;
                display: -webkit-box;
                -webkit-line-clamp: 2;
                -webkit-box-orient: vertical;
                line-height: 1.5;
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
    margin-bottom: 8px;
    position: relative;
    span {
      position: relative;
      &:first-child {
        padding: 5px 0;
        font-size: 24px;
        font-weight: bold;
        border-bottom: 5px solid #c00900;
        margin-right: 10px;
        &::before {
          position: absolute;
          content: '';
          width: 0;
          height: 0;
          border-top: 5px solid #c00900;
          border-right: 5px solid transparent;
          right: -5px;
          bottom: -5px;
        }
      }
      &:last-child {
        padding: 10px 0;
        cursor: pointer;
        flex: 1;
        text-align: right;
        font-size: 14px;
        border-bottom: 2px solid #c00900;
        i {
          margin-left: 4px;
          font-size: 12px;
        }
        &::before {
          position: absolute;
          content: '';
          width: 2px;
          height: 6px;
          transform: rotate(45deg);
          background: #c00900;
          left: -2px;
          bottom: -6px;
        }
      }
    }
  }
}
</style>

