<template>
  <!-- 资金管理弹窗 -->
  <el-dialog
    custom-class="complete_form"
    :show-close="false"
    :center="true"
    :visible.sync="isShow"
    :close-on-press-escape="false"
    :before-close="handleClose"
    width="1200px"
  >
    <el-alert title="请注意：在完成情况里面如果有返款要求的，请及时审核，否则系统36小时内自动审核通过!" type="warning" show-icon></el-alert>
    
    <!-- 图片放大 -->
    <img-dialog ref="imgDialog" />

    <!-- 中间 -->
    <header>
      <span>任务商品</span>
      <i
        v-if="topTableData.length > 0"
      >活动日期：{{ topTableData[0].start_time }}~{{ topTableData[0].end_time }}</i>
    </header>
    <el-table :data="topTableData" border>
      <el-table-column label="商品信息">
        <template slot-scope="scope">
          <img class="toptable_img" :src="scope.row.img" @click="handleImgClick(scope.row.img)" alt />
        </template>
      </el-table-column>
      <el-table-column label="总金额">
        <template slot-scope="scope">
          <span>{{scope.row.service_fee + scope.row.guarantee_fee }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="task_total" label="总数量"></el-table-column>
      <el-table-column label="已完成">
        <template slot-scope="scope">
          <span>{{scope.row.task_total - scope.row.remain_total }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="任务状态"></el-table-column>
    </el-table>

    <!-- 下部分 -->
    <el-tabs style="margin: 20px 0" @tab-click="handleTabClick">
      <el-tab-pane
        v-for="(item, index) in editableTabs"
        :key="index"
        :label="item"
      />
    </el-tabs>

    <el-table v-loading="isloading" :data="bottomTableData" border size="mini">
      <el-table-column label="任务日期" width="150">
        <template slot-scope="scope">
          <span>{{scope.row.create_time}}</span>
        </template>
      </el-table-column>
      <el-table-column prop="user_ww" label="买家旺旺" width="100"></el-table-column>
      <el-table-column prop="order_sn" label="订单编号"></el-table-column>
      <el-table-column prop="price" label="任务金额" width="100"></el-table-column>
      <el-table-column prop="reality_price" label="实拍金额" width="100"></el-table-column>
      <el-table-column prop="user_comment" label="买手备注"></el-table-column>
      <el-table-column prop="shop_ww" label="商家旺旺"></el-table-column>
      <el-table-column prop="status" label="任务状态" width="100">
        <template slot-scope="scope">
          <div>
            {{editableTabs[scope.row.status]}}
          </div>
        </template>
      </el-table-column>
      <el-table-column label="好评截图">
        <template slot-scope="scope">
          <img class="toptable_img" :src="scope.row.u_evaluate_img" @click="handleImgClick(scope.row.u_evaluate_img)" alt />
        </template>
      </el-table-column>
    </el-table>
  </el-dialog>
</template>
<script>
import { free_order_list } from "@/api/saler/freeOrderList";
import imgDialog from "@/components/imgDialog";

export default {
  name: "completeForm",
  components: {imgDialog},
  data() {
    return {
      isloading: false,
      isShow: true,
      tansForm: {},
      // 动态标签
      bottomTableData: [],
      editableTabs: [
        "所有活动",
        "进行中",
        "已下单",
        "待返款",
        "已完成",
        "售后中"
      ],
      // 表格数据
      topTableData: [],
      entity: {}
    };
  },
  methods: {
    async init(item) {
      this.entity = item
      if (item) {
        console.log(item)
        this.topTableData.push(item);
        this.getList(0)
      }
    },
    handleImgClick(val) {
      this.$nextTick(() => {
        this.$refs.imgDialog.init(val);
      });
    },
    async getList(status) {
      this.isloading = true
      // if(status == 5) status = 6
      let res = await free_order_list({
        pa_id: this.entity.pa_id,
        status: status
      });
      if (res && res.error.errno == 200) this.bottomTableData = res.freeOrderList;
      this.isloading = false
    },
    handleTabClick(tab) {
      let status = tab.index
      this.getList(status)
    },
    handleClose(done) {
      this.$emit("close");
      done();
    }
  }
};
</script>
<style lang="scss" scope>
.complete_form {
  header {
    display: flex;
    padding: 12px;
    justify-content: space-between;
    border: solid 1px #e5e5e5;
    border-bottom: 0;
    margin-top: 10px;
    background: #fdf6ec;
    span {
      color: #dc8c00;
    }
    i {
      color: #999;
      font-size: 12px;
    }
  }

  .toptable_img {
    cursor: pointer;
    width: 100px;
  }
}
</style>