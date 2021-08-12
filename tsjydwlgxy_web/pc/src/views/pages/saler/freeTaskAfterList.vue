<template>
  <div class="freeTaskAfterList" v-loading="isLoading">
    <el-alert
      style="margin-bottom:10px"
      title="超时将会自动审核通过，请各位商家及时处理，如有争议可申请平台介入，谢谢配合~"
      type="warning"
      show-icon
    ></el-alert>
    <!-- 图片放大 -->
    <img-dialog ref="imgDialog" />
    <!-- 售后处理 -->
    <handleAfter
      :id="prop_id"
      v-if="showHandleAfter"
      @cancle="showHandleAfter = false"
      @update="showHandleAfter = false;getData()"
    />
    <!-- 售后详情 -->
    <afterDesc
      ref="afterDesc"
      v-if="showAfterDesc"
      @update="showAfterDesc = false;getData()"
      @cancle="showAfterDesc = false"
    />

    <el-form :inline="true" size="mini">
      <!-- <el-form-item class="shorter" label="买家旺旺或订单号：">
        <el-input v-model="formData.condition" placeholder="请输入" clearable />
      </el-form-item> -->

      <el-form-item label="买家旺旺或订单号：">
        <el-input class="shorter" clearable placeholder="请输入" v-model="formData.condition">
          <el-select v-model="formData.keywords" slot="prepend" placeholder="请选择">
            <el-option label="买家旺旺" value="1"></el-option>
            <el-option label="订单号" value="2"></el-option>
          </el-select>
        </el-input>
      </el-form-item>

      <el-form-item class="shorter" label="售后状态：">
        <el-select v-model="formData.status" placeholder="请选择" clearable>
          <el-option
            v-for="item in statusArr"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
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
        <el-button
          icon="el-icon-download"
          size="mini"
          class="btn_export"
          @click="getData(true)"
        >导出数据</el-button>
      </el-form-item>
    </el-form>

    <el-table size="mini" border height="500" :data="tableData">
      <el-table-column label="任务日期">
        <template slot-scope="scope">
          <div>{{scope.row.create_time}}</div>
        </template>
      </el-table-column>
      <el-table-column property="user_ww" label="买家旺旺" />
      <el-table-column property="title" label="赠品" min-width="70" />
      <el-table-column property="order_sn" label="订单编号" min-width="70" />
      <el-table-column label="赠品图片">
        <template slot-scope="scope">
          <div class="table_imgs">
            <img @click="handleImgClick(scope.row.sipping_url)" :src="scope.row.sipping_url" alt />
          </div>
        </template>
      </el-table-column>

      <el-table-column property="integral" label="积分数量" min-width="60" />
      <el-table-column property="reality_price" label="实拍金额" min-width="60" />
      <el-table-column property="user_comment" label="买手备注" min-width="70" />
      <el-table-column property="shop_ww" label="商家旺旺" />

      <el-table-column label="任务状态" min-width="70">
        <template slot-scope="scope">
          <div v-if="scope.row.order_status == 1">已领取</div>
          <div v-else-if="scope.row.order_status == 2">已下单</div>
          <div v-else-if="scope.row.order_status == 3">待返款</div>
          <div v-else-if="scope.row.order_status == 4">已完成</div>
          <div v-else>未知状态</div>
        </template>
      </el-table-column>
      <el-table-column property="sale_type" label="售后类型" min-width="70">
        <template slot-scope="scope">
          <div>{{sale_type_arr[scope.row.sale_type - 1]}}问题</div>
        </template>
      </el-table-column>
      <el-table-column property="sale_status" label="售后状态" min-width="70">
        <template slot-scope="scope">
          <span v-if="scope.row.sale_status == -2">售后失败</span>
          <span v-else-if="scope.row.sale_status == -1">已撤销</span>
          <span v-else-if="scope.row.sale_status == 1">
            <!-- 礼品直接待平台处理 -->
            <span v-if="scope.row.sale_type == 3">待平台处理</span>  
            <span v-else-if="scope.row.platform == '2b'">待粉丝处理</span>
            <span v-else>待处理</span>
          </span>
          <span v-else-if="scope.row.sale_status == 2">待平台处理</span>
          <span v-else-if="scope.row.sale_status == 3">售后成功</span>
        </template>
      </el-table-column>
      <el-table-column property="discount_name" label="操作" min-width="90">
        <template slot-scope="scope">
          <div class="operates">
            <!-- (scope.row.platform == '2c' && scope.row.sale_status == 2) -->
            <el-button
              v-if="scope.row.platform == '2c' && scope.row.sale_status == 1 && scope.row.sale_type != 3"
              size="mini"
              type="primary"
              @click="handler(scope.row)"
            >处 理</el-button>
            <el-button size="mini" type="success" @click="handlerDesc(scope.row)">售后详情</el-button>
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
    ></el-pagination>
  </div>
</template>
<script>
import imgDialog from "@/components/imgDialog";
import handleAfter from "@/components/taskDialog/handleAfter";
import afterDesc from "@/components/taskDialog/afterDesc";
import { free_sale_list, free_sale_list_EX } from "@/api/saler/freeOrderList";
import dayjs from "dayjs";

export default {
  name: "freeTaskAfterList",
  components: { imgDialog, handleAfter, afterDesc },
  data() {
    return {
      showHandleAfter: false,
      showAfterDesc: false,
      isLoading: false,
      prop_id: null,

      sale_type_arr: ["资金", "物流", "礼品", "其它"],
      statusArr: [
        { label: '售后失败',value: -2},
        { label: '已撤销',value: -1},
        { label: '待处理',value: 1},
        { label: '待平台处理',value: 2},
        { label: '售后成功',value: 3},
      ],
      myRange: [
        dayjs().subtract(7, 'day')
        .format("YYYY-MM-DD"),
        dayjs().format("YYYY-MM-DD")
      ], // 时间
      formData: {
        condition: "",
        task_start: dayjs().subtract(7, 'day').format("YYYY-MM-DD"),
        task_end: dayjs().format("YYYY-MM-DD"),
        page: 1,
        page_num: 10
      },
      tableData: [],
      total_count: 0,
      reset: false
    };
  },
  mounted() {
    this.getData();
  },
  watch: {
    'formData.condition': function() {
      this.reset = true
    },
    "formData.status": function() {
      this.reset = true;
      this.getData()
    }
  },
  methods: {
    async getData(flag) {
      if(this.reset) {
        this.formData.page = 1;
        this.formData.page_num = 10;
      }
      if (flag) {
        let res = await free_sale_list_EX(this.formData);
        let downloadElement = document.createElement("a");
        let href = window.URL.createObjectURL(res); // 创建下载的链接
        downloadElement.href = href;
        downloadElement.download = "免单任务售后列表.xls";
        document.body.appendChild(downloadElement);
        downloadElement.click(); // 触发点击
        document.body.removeChild(downloadElement); //下载完成移除元素
        window.URL.revokeObjectURL(href); //释放掉blob对象
      } else {
        this.isLoading = true;
        let res = await free_sale_list(this.formData);
        if (res && res.error.errno == 200) {
          this.tableData = res.freeSaleList;
          this.total_count = res.count;
        }
        this.isLoading = false;
      }
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
    },
    handler(row) {
      this.prop_id = row.id;
      this.showHandleAfter = true;
    },
    handleImgClick(val) {
      this.$nextTick(() => {
        this.$refs.imgDialog.init(val);
      });
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
    handlerDesc(row) {
      this.showAfterDesc = true;
      this.$nextTick(() => {
        this.$refs["afterDesc"].init(row);
      });
    }
  }
};
</script>
<style lang="scss" scope>
.freeTaskAfterList {
  .table_imgs > img {
    cursor: pointer;
    width: 60px;
    height: 60px;
  }
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
<style lang="scss">
.freeTaskAfterList {
  .shorter {
    .el-cascader,
    .el-select,
    .el-input,
    .el-range-editor {
      width: 100px;
    }
  }
}
</style>