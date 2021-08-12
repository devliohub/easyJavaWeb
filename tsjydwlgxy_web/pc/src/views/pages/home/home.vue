<template>
  <!-- 首页 -->
  <div class="index">
    <!-- 广告banner -->
    <div class="index_banner">
      <div class="index_banner_main">
        <ul class="index_banner_main_left">
          <router-link
            v-for="(item, index) in cateTitleList"
            :key="index"
            :to="'/allGoods/' + item.id"
          >
            <li>
              <span :class="item.icon"></span>
              {{ item.long_name }}
            </li>
          </router-link>
        </ul>

        <ul class="index_banner_main_right">
          <li class="index_banner_main_right_top">
            <router-link
              :to="name ? (name.platform == '2c' ? '/user/personal' : '/saler/personal') : ''"
            >
              <img
                v-if="name && name.avatar"
                :src="name.avatar"
                @error="name.avatar=require('../../../assets/datouxiang.png')"
              />
              <img v-else src="@/assets/datouxiang.png" />
            </router-link>
            <div
              v-if="isLogin"
              class="index_banner_main_right_top_tips"
            >{{ name.account || name.mobile }}</div>
          </li>
          <li class="index_banner_main_right_mid" v-if="!isLogin">
            <router-link to="/login">
              <el-button size="small" type="success">登录</el-button>
            </router-link>
            <router-link to="/registerDesc?type=user">
              <el-button size="small" type="success">注册</el-button>
            </router-link>
          </li>
          <li class="index_banner_main_right_bottom">
            <div class="gonggao">公告</div>
            <ul v-if="name && name.platform == '2b'">
              <li>
                <router-link to="/saler/tips?id=1">节假日放假通知</router-link>
              </li>
              <li>
                <router-link to="/saler/tips?id=2">商家售后教程</router-link>
              </li>
              <li>
                <router-link to="/saler/tips?id=3">商家须知及售后制度</router-link>
              </li>
              <li>
                <router-link to="/saler/tips?id=4">粉丝拉黑制度</router-link>
              </li>
            </ul>
            <ul v-else>
              <li>
                <router-link to="">节假日放假通知</router-link>
              </li>
              <li>
                <router-link to="">商家售后教程</router-link>
              </li>
              <li>
                <router-link to="">商家须知及售后制度</router-link>
              </li>
              <li>
                <router-link to="">粉丝拉黑制度</router-link>
              </li>
            </ul>
          </li>
        </ul>
      </div>
      <!-- 背景轮播 -->
      <el-carousel :interval="6000" trigger="click" height="432px" arrow="always">
        <el-carousel-item>
          <div class="bannerimg1"></div>
        </el-carousel-item>
        <el-carousel-item>
          <div class="bannerimg2"></div>
        </el-carousel-item>
      </el-carousel>
    </div>
    <!-- 两块广告 -->
    <!-- <div class="index_2_parts">
      <main>
        <router-link class="active_li" to="/index/limitFree">
          <img src="@/assets/2parts_xlmd.png" alt />
        </router-link>
        <router-link class="active_li" to="/index/bearBuy">
          <img style="margin-left:20px" src="@/assets/2parts_xqg.png" alt />
        </router-link>
      </main>
    </div> -->
    <!-- 推荐好货 最新上线 新品预告 -->
    <div class="index_3_parts">
      <el-tabs @tab-click="tabClick" v-loading="isloading">
        <el-tab-pane v-for="(label, index) in homeTabs" :key="index" :label="label">
          <ul v-if="tabList.length > 0">
            <li v-for="(item, index) in tabList" :key="index">
              <itemCard :entity="item" />
            </li>
          </ul>
          <div v-else class="no_data_div">
            <span>新品上架中 敬请期待吧</span>
            <img src="@/assets/404_images/no_data.png" alt />
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
    <!-- 主卖场 -->
    <div class="index_main_sections">
      <main v-for="(item, index) in cateDescList" :key="index">
        <img
          class="index_main_sections_left"
          :src="item.images"
          alt
          @click="handleClickImgtypes(item.id)"
        />
        <div class="index_main_sections_right">
          <ul>
            <li
              v-for="(_item, _index) in item.activity_list"
              :key="_index"
              :class="{'borderTopNone': _index > 4}"
            >
              <itemCard :entity="_item" />
            </li>
          </ul>
        </div>
      </main>
    </div>
  </div>
</template>

<script>
import { cateTitle, cateDesc, indexSearch } from "@/api/common/common.js";
import itemCard from "@/components/itemCard.vue";
import { mapGetters } from "vuex";

export default {
  components: { itemCard },
  computed: {
    ...mapGetters(["name"]),
    isLogin() {
      return this.$store.state.user.token;
    }
  },
  data() {
    return {
      isloading: false,
      cateDescList: [], // 下方主活动
      cateTitleList: [], // 轮播左侧类型
      tabList: [], // tab公用栏
      homeTabs: ["推荐好货", "最新上线", "新品预告"]
    };
  },
  mounted() {
    if (
      window.localStorage.getItem("invite_code") &&
      window.localStorage.getItem("isfirst_come") == 1
    ) {
      window.localStorage.setItem("isfirst_come", 0);
      this.$router.push("/registerDesc?type=user");
    }
    this.initData();
  },
  methods: {
    async initData() {
      this.getThreeTabs(1);
      // 处理第一次进入session为空情况
      if (!JSON.parse(window.sessionStorage.getItem("tpyeArr"))) {
        let res = await cateTitle();
        this.cateTitleList = res.data;
        window.sessionStorage.setItem("tpyeArr", JSON.stringify(res.data));
      } else {
        this.cateTitleList = JSON.parse(
          window.sessionStorage.getItem("tpyeArr")
        );
      }

      let res1 = await cateDesc();
      if (res1 && res1.error.errno == 200) {
        this.cateDescList = res1.data;
      }
    },
    async getThreeTabs(num) {
      this.isloading = true;
      let res3 = await indexSearch({
        type: 1,
        w_type: num,
        module_type: 1,
        page_no: 1,
        page_size: 6
      });
      if (res3 && res3.error.errno == 200) {
        this.tabList = res3.data;
      }
      this.isloading = false;
    },
    tabClick(val, event) {
      let num = parseInt(val.index) + 1;
      this.getThreeTabs(num);
    },
    handleClickImgtypes(i) {
      this.$router.push("/allGoods/" + parseInt(i));
    }
  }
};
</script>
<style lang="scss" scoped>
.index {
  width: 100%;

  .index_banner {
    position: relative;

    .el-carousel--horizontal {
      overflow: hidden;
    }
    .bannerimg1 {
      width: 100%;
      height: 432px;
      background: url("https://taodaxiong-1259123353.cos.ap-shanghai.myqcloud.com/tdxWeb/sy-Banner.png") no-repeat center;
    }
    .bannerimg2 {
      width: 100%;
      height: 432px;
      background: url("https://taodaxiong-1259123353.cos.ap-shanghai.myqcloud.com/tdxWeb/Banner2.png") no-repeat center;
    }
    &_main {
      width: 1200px;
      margin: 0 auto;
      height: 432px;
      left: calc((100% - 1200px) / 2);
      position: absolute;
      z-index: 999;
      display: flex;
      justify-content: space-between;
      &_left {
        display: flex;
        flex-direction: column;
        width: 150px;
        background: #fff;
        font-size: 14px;
        a {
          transition: 0.2s ease-in-out;
          cursor: pointer;
          flex: 1;
          display: flex;
          align-items: center;
          justify-content: center;
          border-bottom: 1px dotted #e5e5e5;
          .iconfont {
            margin-right: 10px;
            color: #ff5500;
            background: #fff;
            font-size: 16px;
            transition: 0.2s ease-in-out;
          }
          &:hover {
            background: #ff5500;
            color: #fff;
            .iconfont {
              color: #fff;
              background: #ff5500;
            }
          }
        }
      }

      &_right {
        width: 215px;
        background: #fff;
        display: flex;
        flex-direction: column;
        & > li {
          text-align: center;
          border-bottom: 1px dotted #e5e5e5;
        }
        &_top {
          height: 140px;
          img {
            margin: 20px 0 10px 0;
            width: 80px;
            height: 80px;
          }
          &_tips {
            color: #666;
          }
        }
        &_mid {
          line-height: 76px;
          height: 76px;
          & > a {
            margin: 0 5px;
          }
        }
        &_bottom {
          padding: 0 35px;
          flex: 1;
          .gonggao {
            margin: 20px 0;
            font-size: 18px;
          }
          li {
            padding: 8px 0;
            font-size: 14px;
            a {
              font-size: 14px;
              font-weight: normal;
              color: #333;
              &:hover {
                color: #ff5500;
              }
            }
          }
        }
      }
    }
  }

  .index_2_parts {
    margin: 20px 0;
    position: relative;
    main {
      width: 1200px;
      margin: 0 auto;
      img {
        cursor: pointer;
        transition: 0.3s;
        &:hover {
          box-shadow: 0 5px 15px 0 rgba(0, 0, 0, 0.2);
        }
      }
    }
  }

  .index_3_parts {
    & > .el-tabs {
      width: 1200px;
      margin: 0 auto;
    }
    ul {
      display: flex;
      li {
        width: 200px;
        border: 1px solid #ccc;
        border-left: 0;
        &:first-child {
          border-left: 1px solid #ccc;
        }
      }
    }
  }

  .index_main_sections {
    position: relative;
    & > main {
      width: 1200px;
      margin: 0 auto;
      margin-bottom: 30px;
      display: flex;
      .index_main_sections_left {
        cursor: pointer;
        width: 200px;
      }
      .index_main_sections_right {
        ul {
          display: flex;
          flex-wrap: wrap;
          li {
            width: 200px;
            border: 1px solid #ccc;
            border-left: 0;
          }
          .borderTopNone {
            border-top: 0;
          }
        }
      }
    }
  }
}
</style>

