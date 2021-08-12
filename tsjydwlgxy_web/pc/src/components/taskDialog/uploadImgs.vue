<template>
  <!-- 定制评价 -->
  <el-dialog
    custom-class="uploads"
    :show-close="false"
    :center="true"
    :visible.sync="showDialog"
    width="35%"
    @closed="$emit('cancle')"
  >
    <header slot="title">上传好评截图</header>
    <el-form v-loading="isLoading" :model="uploadsForm" ref="uploadsForm" size="small" label-width="80px">
      <el-form-item label="买家旺旺:" prop="user_ww">{{ uploadsForm.user_ww }}</el-form-item>
      <el-form-item label="订单号:" prop="order_sn">{{ uploadsForm.order_sn }}</el-form-item>
      <el-form-item label="好评截图:" prop="imgs">
        <el-upload
          class="upload-demo"
          ref="imgsRef"
          action="#"
          :http-request="myUpload"
          accept="image/jpeg, image/gif, image/png"
          list-type="picture"
          :on-remove="handleRemove"
          :file-list="fileList"
        >
          <el-button size="small" type="primary">点击上传</el-button>
        </el-upload>
      </el-form-item>
    </el-form>
    <span slot="footer">
      <el-button size="small" :loading="isLoading" type="primary" @click="handleSave">保 存</el-button>
      <el-button size="small" @click="$emit('cancle')">取 消</el-button>
    </span>
  </el-dialog>
</template>
<script>
import { order_action } from "@/api/user/orderList";
import { uploadImg, deleteUpload } from "@/api/common/common";

export default {
  name: "uploads",
  props: {
    prop_id: {
      default: null,
      type: [Number, String]
    }
  },
  data() {
    return {
      showDialog: true,
      uploadsForm: {},
      fileList: [],
      isLoading: false
    };
  },
  async mounted() {
    this.isLoading = true;
    let res = await order_action({
      id: this.prop_id,
      type: 7
    });
    if (res && res.error.errno == 200)
      this.uploadsForm = JSON.parse(JSON.stringify(res));
    this.isLoading = false;
  },
  methods: {
    async myUpload(content) {
      let form = new FormData();
      form.append("img", content.file);
      let res = await uploadImg(form);
      if(res && res.error.errno == 200) {
        this.fileList.push({
          url: res.url,
          uid: content.file.uid,
          name: content.file.name
        })
        this.$message.success('操作成功');
      }
    },
    handleRemove(a) {
      this.fileList.map(async (el, i) => {
        if(el.uid == a.uid) {
          let res = await deleteUpload({
            url: el.url
          })
          if(res && res.error.errno == 200) {
            this.fileList.splice(i, 1);
            this.$message.success('图片删除成功');
          } else {
            this.$message.success(res.error.usermsg);
          }
        }
      })
    },
    async handleSave() {
      if(this.$refs["imgsRef"].fileList.length == 0) {
        return this.$message.error("请上传好评截图")
      }
      let arr = []
      this.$refs["imgsRef"].fileList.map(el => {
        arr.push(el.url)
      })
      let res = await order_action({
        id: this.prop_id,
        type: 2,
        img: arr
      })
      if(res && res.error.errno == 200) {
        this.$message.success('操作成功');
        this.$emit('update')
      }
    }
  }
};
</script>
<style lang="scss" scope>
.uploads {
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