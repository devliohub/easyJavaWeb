<template>
  <!-- 手机号弹窗 -->
  <el-dialog
    :show-close="false"
    :center="true"
    :visible.sync="dialogVisible"
    :before-close="handleBeforeClose"
    width="450px"
    custom-class="service_dialog"
  >
    <header slot="title">联系平台</header>
    <el-form size="small" label-width="100px">
      <el-form-item label="客服QQ:">3007496051</el-form-item>
      <el-form-item label="投诉QQ:">
        3007495937
      </el-form-item>
      <el-form-item label="工作时间:">
        工作日：09：00-18：00<br/>
        周六：10：00-18：00<br/>
        (法定节假日除外)
      </el-form-item>
      <el-form-item label="投诉邮箱:">
        tousu@taodaxiong.com
      </el-form-item>
    </el-form>
  </el-dialog>
</template>
<script>
export default {
  name: "serviceDialog",
  data() {
    return {
      dialogVisible: true
    };
  },
  mounted() {
  },
  methods: {
    handleBeforeClose() {
      this.$emit('cancle')
    }
  }
};
</script>
<style lang="scss">
.service_dialog {
  .el-dialog__header {
    padding: 15px 0 !important;
    font-size: 18px;
    background: #ff5500;
    color: #fff;
  }
}
</style>