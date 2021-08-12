<template>
  <div class="allGoods">
    <main>
      <breadcrumb />
      <!-- 推荐好货 最新上线 新品预告 -->
      <div class="index_3_parts" v-if="$route.name != 'allGoods'">
        <el-tabs @tab-click="tabClick" v-loading="isToploading">
          <el-tab-pane v-for="(label, index) in homeTabs" :key="index" :label="label">
            <ul v-if="tabList.length > 0">
              <li v-for="(item, index) in tabList" :key="index">
                <itemCard :entity="item" />
              </li>
            </ul>
            <div v-else class="no_data_div">
              <span>新品上架中 敬请期待吧</span>
              <img src="@/assets/404_images/no_data.png" alt="" />
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
      <!-- 限时免单数据 -->
      <div class="section">
        <!-- 精选内容 -->
        <div class="section_body" v-loading="isloading">
          <el-tabs v-model="activeTab" @tab-click="handleClick">
            <el-tab-pane :pid="tab.id" :name="i + ''" :label="tab.short_name" v-for="(tab, i) in classicTabs" :key="i">
              <ul v-if="cardList.length > 0">
                <li v-for="(card, x) in cardList" :key="x" :class="{'borderLeft': x%6 == 0}">
                  <itemCard :entity="card" />
                </li>
              </ul>
              <div v-else class="no_data_div">
                <span>新品上架中 敬请期待吧</span>
                <img src="@/assets/404_images/no_data.png" alt="" />
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>
        <!-- 分页 -->
        <div class="section_footer">
          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="queryForm.page_no"
            :page-sizes="[12, 24, 36, 50]"
            :page-size="queryForm.page_size"
            background
            layout="total, sizes, prev, pager, next"
            :total="total_acount"
          ></el-pagination>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import { indexSearch, cateTitle } from "@/api/common/common.js";
import itemCard from "@/components/itemCard.vue";
import breadcrumb from "@/components/breadCrumb.vue";

export default {
  name: "limitFree",
  components: { itemCard, breadcrumb },
  data() {
    return {
      isToploading: false,
      isloading: false,
      activeTab: "0",
      queryForm: {
        keywords: "",
        cid: "",
        type: 2,
        page_no: 1,
        page_size: 12
      },
      cardList: [], // 下方分类数据
      tabList: [], // tab公用栏
      total_acount: 0, 
      homeTabs: ["推荐好货", "最新上线", "新品预告"],
      classicTabs: [],
    };
  },
  mounted() {
    this.initData();
    if(this.$route.params.type) {
      this.classicTabs.map((el, i) => {
        if(el.id == this.$route.params.type) this.activeTab = i + ""
      })
    }
  },
  computed: {
    module_type() {
      if(this.$route.path == "/index/bearBuy") {
        return 2
      } else {
        return 1
      }
    }
  },
  methods: {
    async initData(val) {
      if(this.$route.query.keyword) this.queryForm.keywords = this.$route.query.keyword
      if(val || val == "") this.queryForm.keywords = val

      if (this.$route.name != "allGoods") this.getThreeTabs(1);
      // 获取tab栏标签
      this.$nextTick(() => {
        this.classicTabs = JSON.parse(window.sessionStorage.getItem('tpyeArr'))
      })
      this.getModuleCards();
    },
    // 分类点击事件
    handleClick(tab, event) {
      let pid = tab.$attrs.pid
      if (this.$route.name == "allGoods") {
        this.$router.push({ name: "allGoods", params: { type: pid } });
      }
      this.getModuleCards(pid);
    },
    // 主体卡片
    async getModuleCards(pid) {
      if(pid) {
        this.queryForm.cid = pid
      } else {
        this.queryForm.cid = this.$route.params.type || 0
      }
      this.queryForm.module_type = this.module_type
      this.isloading = true
      let res = await indexSearch(this.queryForm);
      if (res && res.error.errno == 200) {
        this.cardList = res.data;
        this.total_acount = res.total_results || 0
      }
      this.isloading = false
    },
    async getThreeTabs(num) {
      this.isToploading = true
      let res = await indexSearch({
        type: 1,
        w_type: num,
        module_type: this.module_type,
        page_no: 1,
        page_size: 12 //一行最多6个
      });
      if (res && res.error.errno == 200) this.tabList = res.data;
      this.isToploading = false
    },
    // 三推荐店点击事件
    tabClick(val, event) {
      let num = parseInt(val.index) + 1;
      this.getThreeTabs(num);
    },
    handleSizeChange(val) {
      this.queryForm.page_no = 1;
      this.queryForm.page_size = val
      this.getModuleCards();
    },
    handleCurrentChange(val) {
      this.queryForm.page_no = val
      this.getModuleCards();
    }
  }
};
</script>
<style lang="scss" scope>
.allGoods {
  width: 100%;
  main {
    width: 1200px;
    margin: 0 auto;
    .index_3_parts {
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

    .section {
      padding-bottom: 30px;
      &_search {
        display: flex;
        height: 36px;
        line-height: 36px;
        position: relative;
        i {
          position: absolute;
          left: 10px;
          top: 9px;
          font-size: 18px;
        }
        & > span {
          font-size: 16px;
          padding-right: 10px;
        }
        & > input {
          width: 370px;
          border: 1px solid #ff5500;
          padding: 10px 0 10px 30px;
          outline: none;
          font-size: 14px;
          border-top-left-radius: 5px;
          border-bottom-left-radius: 5px;
        }
        & > div {
          &:hover {
            opacity: 0.9;
          }
          transition: 0.2 ease-in-out;
          cursor: pointer;
          font-size: 16px;
          text-align: center;
          background: #ff464f;
          color: #fff;
          width: 80px;
          border-top-right-radius: 5px;
          border-bottom-right-radius: 5px;
        }
      }

      &_body {
        border-top: 1px solid #e5e5e5;
        margin-top: 20px;
        ul {
          overflow: hidden;
          // 每行第一个
          .borderLeft {
            border-left: 1px solid #ccc;
          }
          li {
            float: left;
            width: 200px;
            border: 1px solid #ccc;
            border-left: 0;
            margin-bottom: 20px;
          }
        }
      }

      &_footer {
        text-align: right;
        margin-top: 30px;
      }
    }
  }
}
</style>
<style lang="scss">
.allGoods {
  .el-tabs__nav-wrap::after {
    height: 1px;
  }
}
</style>
