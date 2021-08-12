<template>
  <!-- 定制评价 -->
  <el-dialog
    custom-class="history"
    :show-close="false"
    :center="true"
    :visible.sync="showDialog"
    width="40%"
    @closed="$emit('cancle')"
    append-to-body
  >
    <header slot="title">查看历史</header>

    <ul v-loading="isloading">
      <li>
        <!-- 首块信息展示 -->
        <div class="topper">
          <nav>
            <span v-if="entity.platform == '2c'">粉丝: {{ entity.mobile }}</span>
            <span v-else style="color:red">商家: {{ entity.account }}</span>
            <span>申请时间: {{ entity.create_time }}</span>
          </nav>
          <main>
            <div v-if="Htype == '资金问题'">任务金额: ¥{{ entity.price }}</div>
            <div v-if="Htype == '资金问题'">实拍金额: ¥{{ entity.reality_price }}</div>
            <div style="color:red" v-if="Htype == '资金问题'">差价: ¥{{ entity.differ_price }}</div>
            <div>
              凭证截图:
              <ul class="screen_imgs">
                <li v-for="(item, k) in entity.apply_img" :key="k">
                  <img :src="item" alt />
                </li>
              </ul>
            </div>
            <div>售后说明: {{ entity.comment }}</div>
            <div v-if="Htype == '物流问题'">物流单号: {{ entity.logistics_num }}</div>
          </main>
        </div>

        <!-- 物流，其他问题 -->
        <div v-if="Htype == '物流问题' || Htype == '其他问题'">
          <div
            style="border:1px solid #333;margin-bottom:15px;"
            v-for="(item, i) in entity.logitics"
            :key="i"
          >
            <nav>
              <span v-if="item.platform == '2c'">粉丝: {{ item.name }}</span>
              <span v-else style="color:red">商家: {{ item.name }}</span>
              <span>回复时间: {{ item.dispose_time }}</span>
            </nav>
            <main>{{item.info}}</main>
          </div>
        </div>

        <!-- 资金，礼品 -->
        <template v-if="entity.dispose_time">
          <div v-if="Htype == '礼品问题' || Htype == '资金问题'" class="bottomer">
          <nav>
            <span v-if="Htype == '礼品问题'">平台: 淘小熊</span>
            <span v-else-if="entity.platform == '2c'" style="color:red">商家: {{ entity.account }}</span>
            <span v-else>粉丝: {{ entity.mobile }}</span>
            <span v-if="entity.dispose_time">处理时间: {{ entity.dispose_time }}</span>
          </nav>
          <main>
            <div v-if="Htype == '资金问题'">任务金额: ¥{{ entity.price }}</div>
            <div v-if="Htype == '资金问题'">实拍金额: ¥{{ entity.reality_price }}</div>
            <div style="color:red" v-if="Htype == '资金问题'">差价: ¥{{ entity.differ_price }}</div>
            <!-- <div v-if="Htype == '礼品问题'">处理内容: 只有礼品问题才有啊</div> -->
            <div>处理说明: {{ entity.info }}</div>
          </main>
        </div>
        </template>
      </li>
    </ul>
  </el-dialog>
</template>
<script>
import { sale_history } from "@/api/saler/freeOrderList";

export default {
  name: "history",
  props: {
    id: {
      default: "",
      type: [String, Number]
    }
  },
  computed: {
    Htype() {
      return ["资金问题", "物流问题", "礼品问题", "其他问题"][
        this.entity.sale_type - 1
      ];
    }
  },
  data() {
    return {
      showImgs: false,
      isloading: false,
      showDialog: true,
      entity: {}
    };
  },
  async mounted() {
    this.isloading = true
    let res = await sale_history({
      id: this.id
    });
    if (res.error && res.error.errno == 200) this.entity = res.salehistory;
    this.isloading = false
  },
  methods: {
  }
};
</script>
<style lang="scss" scope>
.history {
  max-height: 750px;
  overflow: auto;
  header {
    width: 100%;
    height: 50px;
    line-height: 50px;
    font-weight: bold;
    font-size: 16px;
    border-bottom: 1px solid #ccc;
  }
  ul {
    & > li {
      .bottomer {
        border: 1px solid #333;
        margin-bottom: 15px;
      }
      .topper {
        border: 1px solid #333;
        margin-bottom: 15px;
        .screen_imgs {
          overflow: hidden;
          margin-top: 10px;
          li {
            float: left;
            margin-right: 5px;
          }
          img {
            width: 120px;
            height: 120px;
          }
        }
      }
      nav {
        width: 100%;
        padding: 5px;
        background: #e5e5e5;
        border-bottom: 1px solid #333;
        display: flex;
        justify-content: space-between;
      }
      main {
        width: 100%;
        padding: 5px;
        background: #ebeef5;
        & > div {
          width: 100%;
          margin: 10px 0;
        }
      }
    }
  }
}
</style>