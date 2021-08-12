<template>
  <!-- 售后详情 -->
  <el-dialog
    custom-class="afterDesc"
    :show-close="false"
    :center="true"
    :visible.sync="showDialog"
    width="35%"
    @closed="$emit('cancle')"
  >
    <!-- 图片放大 -->
    <img-dialog ref="imgDialog" />

    <history v-if="showHistory" :id="entity.id" @cancle="showHistory = false" />

    <applyAfter
      v-if="showApply"
      :id="isEdit ? entity.id : entity.order_Id"
      :saleType="entity.sale_type"
      @update="showApply = false;$emit('update')"
      @cancle="showApply = false"
      :isEdit="isEdit"
    />

    <header slot="title">售后详情</header>

    <article v-if="entity.sale_status == -2" style="background: #909399;">售后失败</article>
    <article v-else-if="entity.sale_status == -1" style="background: #E6A23C;">已撤销</article>
    <article v-else-if="entity.sale_status == 1" style="background: #F56C6C;">待处理</article>
    <article v-else-if="entity.sale_status == 2" style="background: #F56C6C;">待处理(已申请客服介入)</article>
    <article v-else-if="entity.sale_status == 3" style="background: #67c23a;">售后成功</article>

    <!-- <div class="afterDesc_contracts">
      <span>该售后申请已经过双方协商完成</span>
    </div>-->

    <div class="afterDesc_btns">
      <el-button
        size="small"
        v-if="(entity.sale_status == 1 || entity.sale_status == 2)  && entity.platform == '2b'"
        type="info"
        @click="handleManage(1)"
      >撤销申请</el-button>
      <el-button
        size="small"
        v-if="(entity.sale_type == 1 || entity.sale_type == 2 || entity.sale_type == 4) && entity.sale_status == 1"
        type="warning"
        @click="handleManage(2)"
      >客服介入</el-button>
      <el-button
        size="small"
        v-if="(entity.sale_status == 1 || entity.sale_status == 2) && entity.platform == '2b'"
        class="btn_export"
        @click="editAfter"
      >修改申请</el-button>
      <el-button size="small" type="success" @click="applyAfter">申请售后</el-button>
      <el-button size="small" type="primary" @click="viewHistory">协商历史</el-button>
    </div>
    <main v-loading="isloading">
      <div class="_header">
        <i v-for="(item, k) in entity.apply_img" :key="k">
          <img :src="item" alt @click="handleImgClick(item)" />
        </i>
      </div>
      <span>(点击图片放大)</span>
      <ul>
        <li>售后原因: {{ sale_type_arr[entity.sale_type - 1] }}问题</li>
        <!-- 资金问题显示 -->
        <li v-if="entity.sale_type == 1">任务金额: ¥{{ entity.price }}</li>
        <li v-if="entity.sale_type == 1">实拍金额: ¥{{ entity.reality_price }}</li>
        <li v-if="entity.sale_type == 1">差价金额: ¥{{ entity.differ_price }}</li>

        <li>售后说明: {{ entity.comment }}</li>
        <li>申请时间: {{ entity.create_time }}</li>
      </ul>
    </main>
  </el-dialog>
</template>
<script>
import history from "@/components/taskDialog/history";
import applyAfter from "@/components/taskDialog/applyAfter";
import { sale_detail, sale_apply_dispose } from "@/api/saler/freeOrderList";
import imgDialog from "@/components/imgDialog";

export default {
  name: "afterDesc",
  components: { history, applyAfter, imgDialog },
  data() {
    return {
      isloading: false,
      showDialog: true,
      showHistory: false,
      showApply: false,
      showImgs: false,
      isEdit: false, // 售后编辑状态

      entity: {},
      sale_type_arr: ["资金", "物流", "礼品", "其它"]
    };
  },
  computed: {
    isSaler() {
      return this.$store.state.user.name.platform == '2c'
    }
  },
  methods: {
    async init(row) {
      this.isloading = true;
      let res = await sale_detail({
        id: row.id,
        action: 3
      });
      if (res.error && res.error.errno == 200) this.entity = res.saleInfo;
      this.isloading = false;
    },
    viewHistory() {
      this.showHistory = true;
    },
    // 修改售后
    editAfter() {
      this.isEdit = true;
      this.showApply = true;
    },
    // 申请售后
    applyAfter() {
      this.isEdit = false;
      this.showApply = true;
    },
    async handleManage(num) {
      let res = await sale_apply_dispose({
        action: 5,
        sale_id: this.entity.id,
        dispose: num
      });
      if (res && res.error.errno == 200) {
        this.$message.success("操作成功");
        this.$emit("update");
      }
    },
    handleImgClick(val) {
      this.$nextTick(() => {
        this.$refs.imgDialog.init(val);
      });
    }
  }
};
</script>
<style lang="scss" scope>
.afterDesc {
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
  article {
    width: 100%;
    height: 50px;
    line-height: 50px;
    color: #fff;
    padding-left: 20px;
    font-size: 16px;
  }
  // .afterDesc_contracts {
  //   padding: 0 20px;
  //   span {
  //     padding: 10px 0;
  //     display: block;
  //     border-bottom: 1px solid #ccc;
  //   }
  // }
  .afterDesc_btns {
    text-align: right;
    padding: 10px 20px;
  }
  main {
    padding: 20px;
    & > span {
      font-size: 12px;
      color: #ccc;
    }
    & > ._header {
      width: 100%;
      display: flex;
      flex-wrap: wrap;
      img {
        cursor: pointer;
        border: 1px solid #ccc;
        width: 125px;
        height: 125px;
        margin-right: 20px;
      }
    }
    & > ul {
      padding: 10px 0;
      li {
        padding: 5px 0;
        font-size: 12px;
        color: #999;
      }
    }
  }
  .el-dialog__body {
    padding: 0 !important;
  }
}
</style>