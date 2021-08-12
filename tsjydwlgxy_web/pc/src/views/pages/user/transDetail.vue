<template>
  <div class="trans" v-loading="isloading">
    <el-form :inline="true" size="mini">
      <el-form-item label="日期：">
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="~"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd"
          @change="handleTimeChange"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="资金类型：">
        <el-select
          v-model="queryForm.type"
          placeholder="请选择资金类型"
          @change="reset=true;getData()"
        >
          <el-option
            v-for="item in typeArr"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="资金状态：" v-if="queryForm.type == 4">
        <el-select
          v-model="queryForm.status"
          clearable
          placeholder="请选择资金状态"
          @change="reset=true;getData()"
        >
          <el-option
            v-for="item in statusArr"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button icon="el-icon-search" size="mini" type="primary" @click="getData">查询</el-button>
      </el-form-item>
    </el-form>

    <ul>
      <li>
        总收入:
        <span>{{topThree.income}}元</span>
      </li>
      <li>
        已提现:
        <span>{{topThree.withdraw}}元</span>
      </li>
      <li>
        余额:
        <span>{{topThree.balance}}元</span>
      </li>
    </ul>

    <el-table size="mini" border height="460" :data="tableData">
      <el-table-column type="index" width="50" label="序号" align="center" />
      <el-table-column property="time" label="日期" />
      <el-table-column property="type" label="资金类型" />
      <el-table-column property="comment" label="记录" />
      <el-table-column property="money" label="金额" />
      <el-table-column property="order_sn" label="订单号" v-if="queryForm.type != 4" />
      <el-table-column property="status" label="资金状态" />
    </el-table>
    <el-pagination
      background
      style="margin:15px 0"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="queryForm.page_no"
      :page-sizes="[10, 20, 30, 50]"
      :page-size="queryForm.page_size"
      layout="total, sizes, prev, pager, next"
      :total="total_count"
    ></el-pagination>
  </div>
</template>
<script>
import { transList, scoreThree } from "@/api/user";

export default {
  name: "transDetail",
  data() {
    return {
      isloading: false,
      typeArr: [
        { label: "任务金额", value: 1 },
        { label: "定制金额", value: 2 },
        { label: "售后金额", value: 3 },
        { label: "提现记录", value: 4 }
      ],
      statusArr: [
        // { label: "已打款", value: 1 },
        { label: "待打款", value: 2 },
        { label: "已打款", value: 3 }
      ], // 积分类型
      dateRange: [],
      queryForm: {
        type: 1,
        status: "",
        page_no: 1,
        page_size: 10
      },
      topThree: {},
      tableData: [],
      total_count: 0,
      reset: false
    };
  },
  async mounted() {
    this.getData();
    let res = await scoreThree();
    if (res && res.error.errno == 200) this.topThree = res.data;
  },
  methods: {
    async getData() {
      if (this.reset) {
        this.queryForm.page_no = 1;
        this.queryForm.page_size = 10;
      }
      this.isloading = true;
      let res = await transList(this.queryForm);
      if (res && res.error.errno == 200) {
        this.tableData = res.data;
        this.total_count = res.total_results;
      }
      this.isloading = false;
    },
    handleTimeChange(val) {
      if (val) {
        this.queryForm.start_time = val[0];
        this.queryForm.end_time = val[1];
      } else {
        this.queryForm.start_time = this.queryForm.end_time = "";
      }
      this.reset = true;
    },
    handleSizeChange(val) {
      this.queryForm.page_no = 1;
      this.queryForm.page_size = val;
      this.reset = false;
      this.getData();
    },
    handleCurrentChange(val) {
      this.reset = false;
      this.queryForm.page_no = val;
      this.getData();
    }
  }
};
</script>
<style lang="scss" scope>
.trans {
  & > ul {
    border: 1px solid #ccc;
    margin-bottom: 20px;
    display: flex;
    li {
      flex: 1;
      color: #999;
      padding: 10px 50px 10px 10px;
      border-right: 1px solid #ccc;
      & > span {
        font-weight: bold;
        color: #333;
      }
      &:last-child {
        border: none;
      }
    }
  }
  .el-form .el-form-item__content .el-input,
  .el-form .el-form-item__content .el-select {
    width: 200px;
  }
}
</style>