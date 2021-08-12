<template>
  <div class="assessTask" v-loading="isLoading">
    <!-- 查看商家定制评价 -->
    <evaluation
      ref="evaluation"
      v-if="showEvaluation"
      @cancle="showEvaluation = false"
      @update="showEvaluation = false;getData()"
    />
    <!-- 查看好评截图 -->
    <viewImgs :prop_id="prop_id" v-if="showImgs" @cancle="showImgs = false" :fromUser="true" />
    <!-- 上传好评截图 -->
    <uploadImgs
      :prop_id="prop_id"
      v-if="showUploadImgs"
      @cancle="showUploadImgs = false"
      @update="showUploadImgs = false;getData()"
    />

    <el-alert
      style="margin-bottom:10px"
      title="请注意：成功领取礼品任务后请在两小时内提交订单号，否则过时后系统自动取消，回收该任务！"
      type="warning"
      show-icon
    ></el-alert>
    <el-form :inline="true" size="mini">
      <el-form-item label="查询抢购日期：">
        <el-date-picker
          v-model="myRange"
          type="daterange"
          range-separator="~"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          format="yyyy-MM-dd"
          value-format="yyyy-MM-dd"
          @change="handleTimeChange"
        ></el-date-picker>
      </el-form-item>
      <!-- <el-form-item label="任务类型">
        <el-select v-model="formData.order_type" placeholder="请选择任务类型" disabled>
          <el-option label="免单任务" value="1"></el-option>
        </el-select>
      </el-form-item>-->
      <el-form-item>
        <el-button icon="el-icon-search" size="mini" type="primary" @click="getData()">查询</el-button>
      </el-form-item>
    </el-form>

    <el-tabs @tab-click="handleTabClick">
      <el-tab-pane>
        <span slot="label">全部订单({{ status_counts[0] || 0 }})</span>
      </el-tab-pane>
      <el-tab-pane>
        <span slot="label">待提交({{ status_counts[1] || 0 }})</span>
      </el-tab-pane>
      <el-tab-pane>
        <span slot="label">待审核({{ status_counts[2] || 0 }})</span>
      </el-tab-pane>
      <el-tab-pane>
        <span slot="label">已完成({{ status_counts[3] || 0 }})</span>
      </el-tab-pane>
    </el-tabs>
    <!-- 表格内容 -->
    <ul v-if="tableData.length > 0">
      <li class="_li" v-for="(entity, k) in tableData" :key="k">
        <div class="table_title">
          <span class="table_title_left">淘宝商家名：{{ entity.shop_ww }}</span>
          <span class="table_title_right">抢购日期：{{ entity.create_time }}&nbsp;&nbsp;完成日期： {{ entity.finish_time ? entity.finish_time : '未完成'}}</span>
        </div>
        <div class="table_main">
          <div style="min-width:280px">
            <header>商品信息</header>
            <main class="good_info">
              <img :src="entity.img" alt />
              <div class="good_info_content">
                <div class="good_info_content_title">任务标题:</div>
                <div class="good_info_content_body">{{ entity.title }}</div>
              </div>
            </main>
          </div>
          <div style="min-width:200px">
            <header>赠品信息</header>
            <main class="gift_info">
              <div class="gift_info_left">
                <img :src="entity.sipping_url" alt />
              </div>
              <div class="liner"></div>
              <div class="gift_info_right">
                <ul>
                  <li>原价: ¥{{ entity.price }}</li>
                  <li>应返: ¥{{ entity.price }}</li>
                </ul>
              </div>
            </main>
          </div>
          <div style="min-width:140px">
            <header>任务信息</header>
            <main class="task_cost">
              <ul>
                <li>
                  买手账号
                  <i style="color:#FF5500">{{entity.mobile}}</i>
                </li>
                <li>
                  订单信息
                  <i style="color:#409EFF">{{entity.order_sn}}</i>
                </li>
              </ul>
            </main>
          </div>
          <div style="min-width:80px">
            <header>商品价格</header>
            <main class="relate_num">
              <span>¥{{entity.price}}</span>
            </main>
          </div>
          <div style="min-width:80px">
            <header>关键词</header>
            <main class="relate_num">
              <span>{{entity.keyword}}</span>
              <span v-if="entity.keyword2">,{{entity.keyword2}}</span>
            </main>
          </div>
          <div style="min-width:80px">
            <header>任务状态</header>
            <main class="task_status">
              <span v-if="entity.make_status == -1">驳回</span>
              <span v-else-if="entity.make_status == 1">待提交</span>
              <span v-else-if="entity.make_status == 2">待审核</span>
              <span v-else-if="entity.make_status == 3">已完成</span>
            </main>
          </div>
          <div style="flex:1">
            <header>操作</header>
            <main class="operates">
              <el-button
                v-if="entity.make_status != 0"
                size="mini"
                class="btn_export"
                @click="openEvaluate(entity, 0)"
              >评价信息详情</el-button>
              <el-button
                v-if="entity.make_status == 2 || entity.make_status == 1"
                size="mini"
                type="primary"
                @click="clickUploadImg(entity)"
              >上传好评截图</el-button>
              <el-button
                v-if="entity.make_status != 0 && entity.make_status != 1"
                size="mini"
                type="success"
                @click="clickViewImg(entity)"
              >查看好评截图</el-button>
            </main>
          </div>
        </div>
      </li>
    </ul>
    <div class="no_data_div" v-else>
      <div>暂无数据</div>
    </div>
    <el-pagination
      background
      style="margin:15px 0"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="formData.page"
      :page-sizes="[5, 10, 20, 50]"
      :page-size="formData.page_num"
      layout="total, sizes, prev, pager, next"
      :total="total_count"
    />
  </div>
</template>
<script>
import { pc_make_order_list } from "@/api/user/orderList";
import viewImgs from "@/components/taskDialog/viewImgs";
import uploadImgs from "@/components/taskDialog/uploadImgs";
import evaluation from "@/components/taskDialog/evaluation";

export default {
  name: "assessTask",
  components: { viewImgs, uploadImgs, evaluation },
  data() {
    return {
      isLoading: false,
      showImgs: false,
      showUploadImgs: false,
      showEvaluation: false,
      prop_id: null,
      myRange: [], // 时间
      formData: {
        uid: "",
        task_start: "",
        task_end: "",
        page_num: 5,
        page: 1,
        order_type: "1",
        make_status: "0"
      },
      total_count: 0,
      status_counts: [],
      tableData: [],
      reset: false
    };
  },
  mounted() {
    this.getData();
  },
  methods: {
    async getData() {
      if (this.reset) {
        this.formData.page = 1;
        this.formData.page_num = 5;
      }
      this.isLoading = true;
      let res = await pc_make_order_list(this.formData);
      this.tableData = res.orderList || [];
      this.total_count = res.count || 0;
      this.status_counts = res.orderNum;
      this.isLoading = false;
    },
    handleTimeChange(val) {
      if (val) {
        this.formData.task_start = val[0];
        this.formData.task_end = val[1];
      } else {
        this.formData.task_start = this.formData.task_end = "";
      }
      this.reset = true;
    },
    handleTabClick(val) {
      this.formData.make_status = val.index;
      this.reset = true;
      this.getData();
    },
    handleSizeChange(val) {
      this.formData.page = 1;
      this.formData.page_num = val;
      this.reset = false;
      this.getData();
    },
    handleCurrentChange(val) {
      this.reset = false;
      this.formData.page = val;
      this.getData();
    },
    clickUploadImg(val) {
      this.prop_id = val.id;
      this.showUploadImgs = true;
    },
    clickViewImg(val) {
      this.prop_id = val.id;
      this.showImgs = true;
    },
    openEvaluate(row, val) {
      this.showEvaluation = true;
      this.$nextTick(() => {
        this.$refs["evaluation"].init(row, val);
      });
    }
  }
};
</script>
<style lang="scss" scope>
.assessTask {
  ._li {
    margin-bottom: 10px;
  }
  .table_title {
    width: 100%;
    padding: 10px;
    border: 1px solid #ccc;
    border-bottom: none;
    display: flex;
    justify-content: space-between;
    align-items: center;
    background: #fdf6ec;
    &_left {
      color: #dc8c00;
      font-size: 14px;
    }
    &_right {
      font-size: 12px;
      color: #999;
    }
  }

  .table_main {
    width: 100%;
    border: 1px solid #ccc;
    display: flex;
    font-size: 14px;
    & > div {
      border-left: 1px solid #ccc;
      &:first-child {
        border-left: 0;
      }

      & > header {
        text-align: center;
        height: 35px;
        line-height: 35px;
        border-bottom: 1px solid #ccc;
        color: #666;
      }
      & > main {
        height: calc(100% - 35px);
      }
      // 商品信息
      .good_info {
        display: flex;
        padding: 5px;
        & > img {
          width: 120px;
          height: 120px;
          background: #ff5500;
          margin-right: 10px;
        }
        &_content {
          width: 130px;
          &_title {
            width: 100%;
            font-weight: bold;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            margin: 10px 0;
          }
          &_body {
            color: #999;
          }
        }
      }
      // 赠品信息
      .gift_info {
        display: flex;
        font-size: 14px;
        &_left {
          display: flex;
          padding: 5px;
          align-items: center;
          img {
            width: 75px;
            height: 75px;
          }
          span {
            flex: 1;
            width: 100%;
            padding-top: 5px;
            font-weight: bold;
            // overflow: hidden;
            i {
              color: #999;
              font-weight: normal;
            }
          }
        }
        .liner {
          width: 2px;
          height: 100%;
          background: #ccc;
        }
        &_right {
          width: 100%;
          padding: 5px;
          & > ul {
            font-weight: bold;
            height: 100%;
            display: flex;
            flex-direction: column;
            li {
              display: flex;
              align-items: center;
              justify-content: center;
              flex: 1;
            }
          }
        }
      }
      // 任务费用
      .task_cost {
        font-size: 13px;
        & > ul {
          display: flex;
          flex-direction: column;
          height: 100%;
          padding: 10px;
          li {
            word-break: break-word;
            flex: 1;
            font-weight: bold;
            span {
              color: #999;
              font-weight: normal;
            }
            i {
              display: block;
              margin-top: 5px;
            }
          }
        }
      }
      // 相关数量
      .relate_num {
        display: flex;
        justify-content: center;
        align-items: center;
        flex-direction: column;
        & > span {
          font-weight: bold;
        }
      }
      // 任务状态
      .task_status {
        display: flex;
        justify-content: center;
        align-items: center;
        font-weight: bold;
      }
      // 操作
      .operates {
        padding: 10px;
        .el-button {
          padding: 5px 7px;
          margin-bottom: 10px;
        }
        .el-button + .el-button {
          margin-left: 0;
        }
      }
    }
  }
}
</style>