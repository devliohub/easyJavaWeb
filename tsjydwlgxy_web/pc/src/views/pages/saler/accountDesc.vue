<template>
  <div class="accountDesc">
    <el-form :inline="true" size="mini">
      <el-form-item>
        <el-input class="inputSelect" v-model.trim="queryObj.inquire_content" clearable placeholder="请输入内容">
          <el-select v-model="queryObj.inquire_type" slot="prepend">
            <el-option
              v-for="item in shopInfoOptions"
              :key="item.item"
              :label="item.value"
              :value="item.item"
            />
          </el-select>
        </el-input>
      </el-form-item>

      <el-form-item label="任务日期：">
        <el-date-picker
          v-model="taskRange"
          type="daterange"
          value-format="yyyy-MM-dd"
          @change="handleTimeChange"
          range-separator="~"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>

      <el-form-item>
        <el-button icon="el-icon-search" size="mini" type="primary" @click="getData()">查询</el-button>
        <el-button icon="el-icon-download" size="mini" class="btn_export" @click="getData(true)">导出数据</el-button>
      </el-form-item>
    </el-form>

    <!-- 表格内容 -->
    <div class="table_title">
      <el-form :inline="true" size="mini">
        <el-form-item label="是否存在售后款：">
          <el-select v-model="queryObj.is_after_sale" placeholder="请选择" @change="reset=true;getData()">
            <el-option :value="1" label="是"></el-option>
            <el-option :value="2" label="否"></el-option>
          </el-select>
        </el-form-item>&nbsp;&nbsp;&nbsp;
        <el-form-item label="是否有消耗定制评价：">
          <el-select v-model="queryObj.is_customized_evaluation" placeholder="请选择" @change="reset=true;getData()">
            <el-option :value="1" label="是"></el-option>
            <el-option :value="2" label="否"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
    </div>

    <el-table size="mini" border height="550" :data="tableData" v-loading="isloading">
      <el-table-column label="任务日期" min-width="90">
        <template slot-scope="scope">
          <span>{{scope.row.task_start}}</span>
        </template>
      </el-table-column>
      <el-table-column property="user_ww" label="买家旺旺" />
      <el-table-column property="order_sn" label="订单编号" min-width="90" />
      <el-table-column property="price" label="任务金额" />
      <el-table-column property="shop_ww" label="商家旺旺" show-overflow-tooltip />

      <el-table-column property="is_money_sale" label="售后款" />
      <el-table-column property="flow_charge" label="消耗流动金" min-width="75" />
      <el-table-column property="service_charge" label="消耗服务费" min-width="75" />
      <el-table-column property="integral" label="积分消耗" />
      <el-table-column property="make_charge" label="消耗定制评价金额" min-width="100"/>
    </el-table>

    <el-pagination
      background
      style="margin:15px 0"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="queryObj.page_no"
      :page-sizes="[15, 30, 50]"
      :page-size="queryObj.page_size"
      layout="total, sizes, prev, pager, next"
      :total="total_count"
    ></el-pagination>
  </div>
</template>
<script>
import {salerOrderDesc, salerOrderDesc_EX} from "@/api/saler/index"

export default {
  name: "accountDesc",
  data() {
    return {
      isloading: false,
      // 店铺信息选项
      shopInfoOptions: [
        {
          item: 1,
          value: "订单编号"
        },
        {
          item: 2,
          value: "买家旺旺号"
        },
        {
          item: 3,
          value: "商家旺旺号"
        }
      ],
      taskRange: [],
      tableData: [],
      total_count: 0,
      queryObj: {
        inquire_type: 1,
        inquire_content: '',
        start_time: '',
        end_time: '',
        is_after_sale: 2,
        is_customized_evaluation: 2,
        page_no: 1,
        page_size: 15
      },
      reset: false
    };
  },
  mounted() {
    this.getData()
  },
  watch: {
    'queryObj.inquire_type': function() {
      this.reset = true
    },
    'queryObj.inquire_content': function() {
      this.reset = true
    }
  },
  methods: {
    async getData(flag) {
      if(this.reset) {
        this.queryObj.page_no = 1;
        this.queryObj.page_size = 15;
      }
      if(flag) {
        let res = await salerOrderDesc_EX(this.queryObj);
        let downloadElement = document.createElement("a");
        let href = window.URL.createObjectURL(res); // 创建下载的链接
        downloadElement.href = href;
        downloadElement.download = "账户明细.xls";
        document.body.appendChild(downloadElement);
        downloadElement.click(); // 触发点击
        document.body.removeChild(downloadElement); //下载完成移除元素
        window.URL.revokeObjectURL(href); //释放掉blob对象
      } else {
        this.isloading = true
        let res = await salerOrderDesc(this.queryObj)
        this.tableData = res.order_detail
        this.total_count = res.total_results || 0
        this.isloading = false
      }
    },
    handleTimeChange(val) {
      if (val) {
        this.queryObj.start_time = val[0];
        this.queryObj.end_time = val[1];
      } else {
        this.queryObj.start_time = this.queryObj.end_time = "";
      }
      this.reset = true
    },
    handleSizeChange(val) {
      this.queryObj.page_no = 1;
      this.queryObj.page_size = val;
      this.reset = false
      this.getData();
    },
    handleCurrentChange(val) {
      this.queryObj.page_no = val;
      this.reset = false
      this.getData();
    }
  },
};
</script>
<style lang="scss" scope>
.accountDesc {
  .table_title {
    width: 100%;
    padding: 10px;
    border: 1px solid #ebeef5;
    border-bottom: none;
    display: flex;
    justify-content: space-between;
    align-items: center;
    .el-form-item {
      margin-bottom: 0;
      .el-select,
      .el-input {
        width: 100px;
      }
    }
  }
  .inputSelect {
    width: 240px !important;
    .el-select, .el-input {
      width: 110px !important;
    }
  }
}
</style>