<template>
  <div class="salerRecord">
    <!-- 数据banner -->
    <ul>
      <li>
        流动金:
        <span>{{entity.flow_charge}}元</span>
      </li>
      <li>
        定制金额:
        <span>{{entity.make_charge}}元</span>
      </li>
      <li>
        积分:
        <span>{{entity.integral}}</span>
      </li>
      <li>
        服务费:
        <span>{{entity.service_charge}}元</span>
      </li>
      <li>
        服务费预付款:
        <span>{{entity.before_service_charge}}元</span>
      </li>
    </ul>
    <!-- 筛选条件 -->
    <el-form class="filter" :inline="true" size="mini">
      <el-form-item>
        <el-select v-model="form.type" placeholder="请选择" @change="handleTypeChange">
          <el-option
            v-for="item in types"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="日期：">
        <el-date-picker
          v-model="timeRange"
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
        <el-button
          icon="el-icon-download"
          size="mini"
          class="btn_export"
          @click="getData(true)"
        >导出数据</el-button>
      </el-form-item>
    </el-form>
    <!-- 表格数据 -->
    <div v-loading="isloading">
      <!-- 任务金额 -->
      <el-table v-if="form.type == 1" size="mini" border height="560" :data="tableData">
        <el-table-column
          v-for="(item, index) in ['日期', '金额', '订单类型', '记录说明']"
          :key="index"
          :label="item"
          :width="index < 3 ? 150: null"
        >
          <template slot-scope="scope">
            <span>{{scope.row[index]}}</span>
          </template>
        </el-table-column>
      </el-table>
      <!-- 定制金额 -->
      <el-table v-if="form.type == 2" size="mini" border height="560" :data="tableData">
        <el-table-column
          v-for="(item, index) in ['日期', '金额', '订单号', '记录说明']"
          :key="index"
          :label="item"
        >
          <template slot-scope="scope">
            <span>{{scope.row[index]}}</span>
          </template>
        </el-table-column>
      </el-table>
      <!-- 售后金额 -->
      <el-table v-if="form.type == 3" size="mini" border height="560" :data="tableData">
        <el-table-column
          v-for="(item, index) in ['日期', '申请人', '处理人', '订单号', '差价金额', '处理说明']"
          :key="index"
          :label="item"
        >
          <template slot-scope="scope">
            <span>{{scope.row[index]}}</span>
          </template>
        </el-table-column>
      </el-table>
      <!-- 资金互转记录 -->
      <el-table v-if="form.type == 4" size="mini" border height="560" :data="tableData">
        <el-table-column v-for="(item, index) in ['日期', '金额', '记录说明']" :key="index" :label="item">
          <template slot-scope="scope">
            <span>{{scope.row[index]}}</span>
          </template>
        </el-table-column>
      </el-table>
      <!-- 订单金额变更记录 -->
      <el-table v-if="form.type == 5" size="mini" border height="560" :data="tableData">
        <el-table-column
          v-for="(item, index) in ['日期', '金额','订单号', '记录说明']"
          :key="index"
          :label="item"
          :width="index < 3 ? 150: null"
        >
          <template slot-scope="scope">
            <span>{{scope.row[index]}}</span>
          </template>
        </el-table-column>
      </el-table>
      <!-- 充值明细 -->
      <el-table v-if="form.type == 6" size="mini" border height="560" :data="tableData">
        <el-table-column
          v-for="(item, index) in ['日期', '支付方式','流动金', '服务费', '总金额', '状态', '退回原因']"
          :key="index"
          :label="item"
        >
          <template slot-scope="scope">
            <span>{{scope.row[index]}}</span>
          </template>
        </el-table-column>
      </el-table>
      <!-- 后台充值记录 -->
      <el-table v-if="form.type == 7" size="mini" border height="560" :data="tableData">
        <el-table-column
          v-for="(item, index) in ['日期', '变更前流动金','变更后流动金', '变更前服务费', '变更后服务费', '变更前预付款', '变更后预付款', '变更前定制金', '变更后定制金']"
          :key="index"
          :label="item"
        >
          <template slot-scope="scope">
            <span>{{scope.row[index]}}</span>
          </template>
        </el-table-column>
      </el-table>
      <!-- 提现记录 -->
      <el-table v-if="form.type == 8" size="mini" border height="560" :data="tableData">
        <el-table-column
          v-for="(item, index) in ['日期', '记录','金额', '状态', '驳回原因']"
          :key="index"
          :label="item"
        >
          <template slot-scope="scope">
            <span>{{scope.row[index]}}</span>
          </template>
        </el-table-column>
      </el-table>
      <!-- 订单删除记录 -->
      <el-table v-if="form.type == 9" size="mini" border height="560" :data="tableData">
        <el-table-column
          v-for="(item, index) in ['创建日期', '删除日期', '金额', '订单号', '记录说明']"
          :key="index"
          :label="item"
        >
          <template slot-scope="scope">
            <span>{{scope.row[index]}}</span>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <!-- 分页 -->
    <el-pagination
      background
      style="margin:15px 0"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="form.page_no"
      :page-sizes="[20, 30, 50]"
      :page-size="form.page_size"
      layout="total, sizes, prev, pager, next"
      :total="total_count"
    ></el-pagination>
  </div>
</template>
<script>
import { salerTransaction, salerTransaction_EX, salerTransaction_ST } from "@/api/saler/index";

export default {
  name: "salerRecord",
  data() {
    return {
      isloading: false,
      timeRange: [],
      entity: {},
      form: {
        type: 1,
        start_time: "",
        end_time: "",
        page_no: 1,
        page_size: 20
      },
      reset: false,
      types: [
        {
          label: "任务金额",
          value: 1
        },
        {
          label: "定制金额",
          value: 2
        },
        {
          label: "售后金额",
          value: 3
        },
        {
          label: "资金互转记录",
          value: 4
        },
        {
          label: "订单金额变更记录",
          value: 5
        },
        {
          label: "充值明细",
          value: 6
        },
        {
          label: "后台充值记录",
          value: 7
        },
        {
          label: "提现记录",
          value: 8
        },
        {
          label: "订单删除记录",
          value: 9
        }
      ],
      tableData: [],
      total_count: 0
    };
  },
  mounted() {
    this.getBasic()
    this.getData();
  },
  methods: {
    // 获取顶部静态资源
    async getBasic() {
      let res = await salerTransaction_ST()
      this.entity = res.data
    },
    // flag为export
    async getData(flag) {
      if(this.reset) {
        this.form.page_no = 1;
        this.form.page_size = 20;
      }
      if (flag) {
        let res = await salerTransaction_EX(this.form);
        let downloadElement = document.createElement("a");
        let href = window.URL.createObjectURL(res); // 创建下载的链接
        downloadElement.href = href;
        downloadElement.download = "交易记录-" + this.types[this.form.type - 1].label + ".xls";
        document.body.appendChild(downloadElement);
        downloadElement.click(); // 触发点击
        document.body.removeChild(downloadElement); //下载完成移除元素
        window.URL.revokeObjectURL(href); //释放掉blob对象
      } else {
        this.isloading = true;
        let res = await salerTransaction(this.form);
        this.isloading = false;
        this.tableData = res.body_data;
        this.total_count = res.total_results
      }
    },
    handleTypeChange(val) {
      this.reset = true
      if (val) this.getData();
    },
    handleTimeChange(val) {
      if (val) {
        this.form.start_time = val[0];
        this.form.end_time = val[1];
      } else {
        this.form.start_time = this.form.end_time = "";
      }
      this.reset = true
    },
    handleSizeChange(val) {
      this.form.page_no = 1;
      this.form.page_size = val;
      this.reset = false;
      this.getData();
    },
    handleCurrentChange(val) {
      this.form.page_no = val;
      this.reset = false;
      this.getData();
    }
  }
};
</script>
<style lang="scss" scope>
.salerRecord {
  & > ul {
    border: 1px solid #ebeef5;
    border-bottom: 0;
    display: flex;
    li {
      flex: 1;
      color: #999;
      padding: 10px 0 10px 10px;
      border-right: 1px solid #ebeef5;
      & > span {
        font-weight: bold;
        color: #333;
      }
      &:last-child {
        border: none;
      }
    }
  }
  .filter {
    border: 1px solid #ebeef5;
    border-bottom: 0;
    .el-form-item {
      margin-bottom: 0;
      padding: 10px;
    }
  }
}
</style>