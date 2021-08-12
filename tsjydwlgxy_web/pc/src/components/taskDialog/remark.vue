<template>
  <!-- 定制评价 -->
  <el-dialog
    custom-class="remark"
    :show-close="false"
    :center="true"
    :visible.sync="showDialog"
    width="35%"
    @closed="$emit('cancle')"
  >
    <header slot="title">备 注</header>
    <el-form v-loading="isloading" :model="querObj" ref="remarkForm" size="small" label-width="80px">
      <el-form-item label="备注:" prop="desc">
        <el-input type="textarea" :rows="4" placeholder="请输入内容" v-model="querObj.point_comment" />
      </el-form-item>
    </el-form>
    <span slot="footer">
      <el-button size="small" type="primary" @click="handleSave">保 存</el-button>
      <el-button size="small" @click="$emit('cancle')">取 消</el-button>
    </span>
  </el-dialog>
</template>
<script>
import { rush_order_action } from "@/api/saler/rushOrderList";
import { free_order_action } from "@/api/saler/freeOrderList";

export default {
  name: "remark",
  props: {
    isbear: {
      default: false,
      type: Boolean
    }
  },
  data() {
    return {
      showDialog: true,
      isloading: false,

      querObj: {
        id: "",
        point_comment: "",
        type: null
      }
    };
  },
  methods: {
    async init(row) {
      this.querObj.id = row.id
      let res
      this.isloading = true
      if(this.isbear) {
        this.querObj.type = 5;
        res = await rush_order_action(this.querObj);
        this.querObj.point_comment = res.point_comment
      } else {
        this.querObj.type = 7;
        res = await free_order_action(this.querObj);
        this.querObj.point_comment = res.point_comment
      }
      this.isloading = false
    },
    async handleSave() {
      let res;
      if (this.isbear) {
        this.querObj.type = 5;
        res = await rush_order_action(this.querObj);
      } else {
        this.querObj.type = 7;
        res = await free_order_action(this.querObj);
      }
      if (res.error && res.error.errno == 200) {
        this.$message.success("操作成功");
        this.$emit("cancle");
      }
    },
    handlePreview() {},
    handleRemove() {}
  }
};
</script>
<style lang="scss" scope>
.remark {
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
}
</style>