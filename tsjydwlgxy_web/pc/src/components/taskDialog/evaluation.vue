<template>
  <!-- 定制评价 -->
  <el-dialog
    custom-class="evaluation"
    :show-close="false"
    :center="true"
    :visible.sync="showDialog"
    width="35%"
    @closed="$emit('cancle')"
  >
    <header slot="title">定制评价</header>
    <el-form :model="evaluationForm" ref="evaluationForm" size="small" label-width="80px">
      <el-form-item v-if="isSaler" label="评价类型:" prop="type">
        <el-radio v-model="evaluationForm.type" label="1">固定评价</el-radio>
        <div class="tips">由商家给予评价信息为评价标准，文字内容不得有误差即为固定评价。</div>
      </el-form-item>
      <el-form-item label="评价说明:" prop="desc">
        <el-input
          :disabled="!canEdit"
          type="textarea"
          :rows="3"
          placeholder="请输入内容"
          v-model="evaluationForm.desc"
        />
      </el-form-item>
      <el-form-item label="好评截图:" prop="imgs" :class="{'disableUpload': !canEdit}">
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
          <el-button size="small" type="primary" v-if="canEdit">点击上传</el-button>
        </el-upload>
      </el-form-item>
    </el-form>
    <span slot="footer">
      <el-button v-if="canEdit" size="small" type="primary" @click="handleSave">保 存</el-button>
      <el-button size="small" @click="$emit('cancle')">取 消</el-button>
    </span>
  </el-dialog>
</template>
<script>
import { free_order_action } from "@/api/saler/freeOrderList";
import { uploadImg, deleteUpload } from "@/api/common/common";
import { order_action } from "@/api/user/orderList";

export default {
  name: "evaluation",
  data() {
    return {
      canEdit: false,
      showDialog: true,
      evaluationForm: {
        type: "1",
        desc: ""
      },
      queryObj: {
        id: ""
      },
      fileList: []
    };
  },
  computed: {
    isSaler() {
      return this.$route.meta.roles == "saler";
    }
  },
  methods: {
    async init(row, val) {
      this.queryObj.id = row.id;
      this.canEdit = val == 1;
      if (row.make_status == 0) {
        console.log("第一次进入");
      } else {
        let res;
        if (this.isSaler) {
          res = await free_order_action({
            id: row.id,
            type: 1
          });
        } else {
          res = await order_action({
            id: row.id,
            type: 4
          });
        }
        if (res.img) {
          let arr = [];
          res.img.map((el, i) => {
            arr.push({
              name: i,
              url: el
            });
          });
          this.fileList = arr;
        }
        this.evaluationForm.desc = res.m_eva_explain || "";
      }
    },
    async myUpload(content) {
      let form = new FormData();
      form.append("img", content.file);
      let res = await uploadImg(form);
      if (res && res.error.errno == 200) {
        this.fileList.push({
          url: res.url,
          uid: content.file.uid,
          name: content.file.name
        });
        this.$message.success('上传成功');
      }
    },
    handleRemove(a) {
      this.fileList.map(async (el, i) => {
        if (el.uid == a.uid) {
          let res = await deleteUpload({
            url: el.url
          });
          if(res && res.error.errno == 200) {
            this.fileList.splice(i, 1);
            this.$message.success('图片删除成功');
          } else {
            this.$message.success(res.error.usermsg);
          }
        }
      });
    },
    async handleSave() {
      let arr = [];
      this.$refs["imgsRef"].fileList.map(el => {
        arr.push(el.url);
      });
      let res = await free_order_action({
        ...this.queryObj,
        type: 5,
        m_eva_explain: this.evaluationForm.desc,
        m_evaluate_img: arr
      });
      if (res && res.error.errno == 200) {
        this.$message.success('操作成功');
        this.$emit("update");
      }
    }
  }
};
</script>
<style lang="scss" scope>
.evaluation {
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
  .tips {
    font-size: 12px;
    color: red;
  }
  .disableUpload {
    .el-upload-list__item:hover .el-icon-close {
      display: none !important;
    }
  }
}
</style>