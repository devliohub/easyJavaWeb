<template>
  <div class="scoreDetail">
    <el-form :inline="true" size="mini">
      <el-form-item label="日期：">
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="~"
          value-format="yyyy-MM-dd"
          @change="handleTimeChange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="积分状态：">
        <el-select
          v-model="queryForm.status"
          clearable
          placeholder="请选择积分状态"
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
        积分总额:
        <span>{{topThree.integral_total}}积分</span>
      </li>
      <li>
        已消耗积分:
        <span>{{topThree.integral_use}}积分</span>
      </li>
      <li>
        积分余额:
        <span>{{topThree.integral}}积分</span>
      </li>
    </ul>

    <el-table size="mini" border height="460" :data="tableData">
      <el-table-column type="index" width="50" label="序号" align="center" />
      <el-table-column property="create_time" label="日期" />
      <el-table-column property="order_sn" label="订单号" />
      <el-table-column property="comment" label="记录" />
      <el-table-column property="type" label="积分类型" />
      <el-table-column property="integral_num" label="积分" />
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
import { scoreList, scoreThree } from "@/api/user";

export default {
  name: "scoreDetail",
  data() {
    return {
      statusArr: [
        { label: "消耗积分", value: 1 },
        { label: "奖励积分", value: 2 }
      ], // 积分类型
      total_count: 0,
      dateRange: [],
      queryForm: {
        status: "",
        page_no: 1,
        page_size: 10
      },
      topThree: {},
      tableData: [],
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
      let res = await scoreList(this.queryForm);
      if (res && res.error.errno == 200) {
        this.tableData = res.data;
        this.total_count = res.total_results || 0;
      }
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
      this.queryForm.page_no = val;
      this.reset = false;
      this.getData();
    }
  }
};
</script>
<style lang="scss" scope>
.scoreDetail {
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
}
</style>