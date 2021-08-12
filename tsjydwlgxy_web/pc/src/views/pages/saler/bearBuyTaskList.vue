<template>
  <div class="bearBuyTaskList" v-loading="isLoading">
    <el-alert
      style="margin-bottom:10px"
      title="请注意：如不按时审核，出现的资金问题由商家自行承担。"
      type="warning"
      show-icon
    ></el-alert>
    <!-- 备注 -->
    <remark ref="remark_ref" v-if="showRemark" :isbear="true" @cancle="showRemark = false" />
    <!-- 退回原因 -->
    <reback ref="reback" v-if="showReturn" :isbear="true" @cancle="showReturn = false" @update="showReturn = false;getData()" />
    <!-- 售后申请 -->
    <applyAfter :id="prop_id" v-if="showApply" @cancle="showApply = false" @update="showApply = false;getData()"/>
    <!-- 好评截图 -->
    <viewImgs ref="viewImg_ref" v-if="showImgs" :isbear="true" @cancle="showImgs = false" />

    <el-form :inline="true" size="mini">
      <el-form-item label="买家旺旺或订单编号：">
        <el-input v-model="formData.condition" placeholder="请输入买家旺旺或订单编号" clearable />
      </el-form-item>
      <el-form-item label="任务日期：">
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
      <el-form-item>
        <el-button icon="el-icon-search" size="mini" type="primary" @click="getData()">查询</el-button>
        <el-button icon="el-icon-download" size="mini" class="btn_export" @click="getData(true)">导出数据</el-button>
      </el-form-item>
    </el-form>

    <!-- tab栏 -->
    <el-tabs @tab-click="handleTabClick">
      <el-tab-pane>
        <span slot="label">所有活动</span>
      </el-tab-pane>
      <el-tab-pane>
        <span slot="label">已领取</span>
      </el-tab-pane>
      <el-tab-pane>
        <span slot="label">已下单</span>
      </el-tab-pane>
      <el-tab-pane>
        <span slot="label">待返款</span>
      </el-tab-pane>
      <el-tab-pane>
        <span slot="label">已完成</span>
      </el-tab-pane>
    </el-tabs>

    <!-- 表格内容 -->
    <el-table size="mini" border height="460" :data="tableData">
      <el-table-column label="任务日期">
        <template slot-scope="scope">
          <div>{{scope.row.create_time}}</div>
        </template>
      </el-table-column>
      <el-table-column property="user_ww" label="买家旺旺" />
      <el-table-column property="order_sn" label="订单编号" min-width="70" />
      <el-table-column property="price" label="商品原价" min-width="70" />
      <el-table-column property="current_price" label="活动价" min-width="70" />
      <el-table-column property="integral" label="兑换积分数" min-width="70" />

      <el-table-column property="user_comment" show-overflow-tooltip label="买手备注" min-width="70" />
      <el-table-column property="shop_ww" label="商家旺旺" min-width="70" />
      <el-table-column label="任务状态" min-width="70">
        <template slot-scope="scope">
          <div v-if="scope.row.status == 1">已领取</div>
          <div v-else-if="scope.row.status == 2">已下单</div>
          <div v-else-if="scope.row.status == 3">待返款</div>
          <div v-else-if="scope.row.status == 4">已完成</div>
          <!-- <div v-else-if="scope.row.status == 5">定制评价</div> -->
          <div v-else>未知状态</div>
        </template>
      </el-table-column>
      <el-table-column label="操作" min-width="100">
        <template slot-scope="scope">
          <div class="operates">
            <el-button size="mini" type="success" @click="applyAfter(scope.row)">申请售后</el-button>
            <el-button
              size="mini"
              v-if="scope.row.status == 3"
              class="btn_export"
              @click="ensureRefund(scope.row)"
            >确认返款</el-button>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="好评截图" min-width="100">
        <template slot-scope="scope">
          <div class="operates">
            <el-button
              size="mini"
              v-if="scope.row.status > 2"
              type="primary"
              @click="handleViewImg(scope.row)"
            >好评截图</el-button>
            <el-button
              size="mini"
              v-if="scope.row.status == 3"
              class="btn_export"
              @click="handleReback(scope.row)"
            >任务退回</el-button>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="备注" min-width="100">
        <template slot-scope="scope">
          <div class="operates">
            <el-button
              size="mini"
              v-if="scope.row.is_sipping == -1"
              type="primary"
              @click="sendGoods(scope.row)"
            >发 货</el-button>
            <el-button
              size="mini"
              v-else
              disabled
            >已发货</el-button>
            <el-button size="mini" class="btn_export" @click="handleRemark(scope.row)">备 注</el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      background
      style="margin:15px 0"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="formData.page"
      :page-sizes="[10, 20, 30, 50]"
      :page-size="formData.page_num"
      layout="total, sizes, prev, pager, next"
      :total="total_count"
    />
  </div>
</template>
<script>
import remark from "@/components/taskDialog/remark";
import reback from "@/components/taskDialog/reback";
import applyAfter from "@/components/taskDialog/applyAfter";
import viewImgs from "@/components/taskDialog/viewImgs";
import { rush_order_list, rush_order_list_EX, rush_order_action } from "@/api/saler/rushOrderList";

export default {
  name: "bearBuyTaskList",
  components: { remark, reback, applyAfter, viewImgs },
  data() {
    return {
      prop_id: null,
      showRemark: false,
      showReturn: false,
      showApply: false,
      showImgs: false,
      isLoading: false,

      myRange: [], // 时间
      total_count: 0,
      tableData: [],
      formData: {
        mid: "",
        condition: "",
        task_start: "",
        task_end: "",
        page: 1,
        page_num: 10,
        status: "0"
      },
      reset: false
    };
  },
  mounted() {
    this.getData();
  },
  watch: {
    'formData.condition': function() {
      this.reset = true
    }
  },
  methods: {
    async getData(flag) {
      if(this.reset) {
        this.formData.page = 1;
        this.formData.page_num = 10;
      }
      if(flag) {
        let res = await rush_order_list_EX(this.formData);
        let downloadElement = document.createElement("a");
        let href = window.URL.createObjectURL(res); // 创建下载的链接
        downloadElement.href = href;
        downloadElement.download = "熊抢购任务列表.xls";
        document.body.appendChild(downloadElement);
        downloadElement.click(); // 触发点击
        document.body.removeChild(downloadElement); //下载完成移除元素
        window.URL.revokeObjectURL(href); //释放掉blob对象
      } else {
      this.isLoading = true;
      let res = await rush_order_list(this.formData);
      if (res && res.error.errno == 200) {
        this.tableData = res.freeOrderList;
        this.total_count = res.count;
      }
      this.isLoading = false;
      }
    },
    /**
     * 表格操作方法
     * num: type类型
     */
    actionPost(num, id) {
      let querObj = {
        id: id || "",
        dismiss_reason: "", // type4
        mc_comment: "", // type5
        type: num
      };
      return rush_order_action(querObj);
    },
    handleTimeChange(val) {
      if (val) {
        this.formData.task_start = val[0];
        this.formData.task_end = val[1];
      } else {
        this.formData.task_start = this.formData.task_end = "";
      }
      this.reset = true
    },
    handleTabClick(val) {
      this.reset = true
      this.formData.status = val.index;
      this.getData();
    },
    // 发货
    async sendGoods(row) {
      let res = await this.actionPost(2, row.id);
      if (res && res.error.errno == 200) {
        this.$message.success("发货成功");
        this.getData();
      }
    },
    // 确认返款
    async ensureRefund(row) {
      this.$confirm("此操作将确认返款, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(async () => {
          let res = await this.actionPost(3, row.id);
          if (res && res.error.errno == 200) {
            this.$message.success("确认返款成功");
            this.getData();
          }
        })
        .catch();
    },
    handleViewImg(row) {
      this.showImgs = true;
      this.$nextTick(() => {
        this.$refs["viewImg_ref"].init(row);
      });
    },
    handleRemark(row) {
      this.showRemark = true;
      this.$nextTick(() => {
        this.$refs["remark_ref"].init(row);
      });
    },
    // 任务退回
    handleReback(row) {
      this.showReturn = true;
      this.$nextTick(() => {
        this.$refs["reback"].init(row);
      });
    },
    applyAfter(row) {
      this.prop_id = row.id
      this.showApply = true;
    },
    handleSizeChange(val) {
      this.formData.page = 1;
      this.formData.page_num = val;
      this.reset = false
      this.getData();
    },
    handleCurrentChange(val) {
      this.formData.page = val;
      this.reset = false
      this.getData();
    }
  }
};
</script>
<style lang="scss" scope>
.bearBuyTaskList {
  // 操作
  .operates {
    .el-button + .el-button {
      margin-left: 0;
    }
    .el-button {
      margin: 2px 0;
      width: 80px;
    }
  }
}
</style>