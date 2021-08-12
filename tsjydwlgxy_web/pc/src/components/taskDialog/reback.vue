<template>
  <!-- 定制评价 -->
  <el-dialog
    custom-class="reback"
    :show-close="false"
    :center="true"
    :visible.sync="showDialog"
    width="35%"
    @closed="$emit('cancle')"
  >
    <header slot="title">任务退回</header>
    <el-form
      :model="querObj"
      ref="rebackForm"
      size="small"
      label-width="80px"
    >
      <el-form-item label="退回原因:" prop="desc">
        <el-input type="textarea" :rows="4" placeholder="请输入内容" v-model="querObj.dismiss_reason" />
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
import { free_order_action } from "@/api/saler/freeOrderList"

export default {
  name: "reback",
  props: {
    isbear: {
      default: false,
      type: Boolean
    }
  },
  data() {
    return {
      showDialog: true,
      querObj: {
        id: "",
        dismiss_reason: '',
        type: null
      }
    };
  },
  methods: {
    init(row) {
      this.querObj.id = row.id
    },
    async handleSave() {
      let res
      if(this.isbear) {
        this.querObj.type = 4
        res = await rush_order_action(this.querObj);
      } else {
        this.querObj.type = 6
        res = await free_order_action(this.querObj);
      }
      if(res.error && res.error.errno == 200) {
        this.$message.success('操作成功')
        this.$emit('update')
      }
    },
    handlePreview() {},
    handleRemove() {}
  }
};
</script>
<style lang="scss" scope>
.reback {
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