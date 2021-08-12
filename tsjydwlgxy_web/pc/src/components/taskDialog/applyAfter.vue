<template>
  <!-- 售后处理 -->
  <el-dialog
    custom-class="apply"
    :show-close="false"
    :center="true"
    :visible.sync="showDialog"
    width="35%"
    @closed="$emit('cancle')"
    append-to-body
  >
    <header slot="title">{{isEdit ? '修改申请' : '申请售后'}}</header>
    <section>
      <img :src="applyForm.img" alt />
      <span>{{ applyForm.title }}</span>
    </section>
    <el-form
      :model="applyForm"
      :rules="applyRules"
      ref="applyForm"
      size="small"
      label-width="120px"
    >
      <el-form-item label="售后原因:" prop="sale_type">
        <el-select
          :disabled="typeof saleType == 'number'"
          v-model="applyForm.sale_type"
          placeholder="请选择"
        >
          <el-option
            v-for="item in afterReasons"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="粉丝实付金额:" prop="reality_price" v-if="applyForm.sale_type == 1">
        <el-input-number
          v-model="applyForm.reality_price"
          controls-position="right"
          :min="0"
          :precision="2"
        />
        <span
          class="money_tips"
        >(任务金额：¥{{applyForm.price}}&nbsp;&nbsp;&nbsp;差价金额：¥{{(applyForm.price - parseInt(applyForm.reality_price) || 0)}})</span>
      </el-form-item>

      <el-form-item label="申请说明:" prop="comment">
        <el-input type="textarea" :rows="2" placeholder="请输入内容" v-model="applyForm.comment" />
      </el-form-item>

      <el-form-item v-if="applyForm.sale_type == 2" label="物流运单号:" prop="logistics_num">
        <el-input placeholder="请输入内容" v-model="applyForm.logistics_num" />
      </el-form-item>

      <el-form-item label="售后凭证:">
        <span style="font-size:12px;color:red">请提供淘宝付款截图: 包含店铺名，付款金额及订单号</span>
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

      <!-- <el-form-item label="物流运单号:" prop="number" v-if="applyForm.reason == '物流问题'">
        <el-input placeholder="请输入运单号" v-model="applyForm.number" />
      </el-form-item>-->

      <!-- <el-form-item label="处理说明:" prop="money">
        <el-input type="textarea" :rows="2" placeholder="请输入内容" v-model="applyForm.desc" />
      </el-form-item>-->
    </el-form>
    <span slot="footer">
      <el-button v-if="isEdit" size="small" type="warning" @click="handleSubmit('edit')">修 改</el-button>
      <el-button v-else size="small" type="success" @click="handleSubmit('create')">提 交</el-button>
    </span>
  </el-dialog>
</template>
<script>
import { sale_apply_dispose } from "@/api/saler/freeOrderList";
import { uploadImg, deleteUpload } from "@/api/common/common";

export default {
  name: "apply",
  props: {
    id: {
      default: 0,
      type: [Number, String]
    },
    saleType: {
      default: null,
      type: [Number, String]
    },
    isEdit: {
      default: false,
      type: Boolean
    }
  },
  data() {
    var validatePrice = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入粉丝实付金额"));
      } else if (value == this.applyForm.price) {
        callback(new Error("不能等于任务金额"));
      } else {
        callback();
      }
    };
    return {
      isloading: false,
      showDialog: true,
      showHistory: false,
      entity: {},
      fileList: [],

      applyForm: {
        sale_type: 1,
        comment: ""
        // reality_price: 0,
      },
      applyRules: {
        reality_price: {
          required: true,
          trigger: "blur",
          validator: validatePrice
        },
        logistics_num: { required: true, trigger: "blur", message: "必填项" }
      },
      afterReasons: [
        {
          label: "资金问题",
          value: 1
        },
        // {
        //   label: "物流问题",
        //   value: 2
        // },
        // {
        //   label: "礼品问题",
        //   value: 3
        // },
        // {
        //   label: "其他问题",
        //   value: 4
        // }
      ]
    };
  },
  async mounted() {
    if (this.isEdit) {
      let res = await sale_apply_dispose({
        sale_id: this.id,
        action: 6 // 修改售后申请前，获取上次申请的售后信息
      });
      if (res && res.error.errno == 200) {
        this.applyForm = res.saleInfo;
        // 设置图片
        let arr = [];
        if (res.saleInfo.apply_img) {
          let arr = [];
          res.saleInfo.apply_img.map((el, i) => {
            arr.push({
              name: i,
              url: el
            });
          });
          this.fileList = arr;
        }
      }
    } else {
      let res = await sale_apply_dispose({
        id: this.id,
        action: 1 // 获取订单详细信息
      });
      if (res && res.error.errno == 200) this.applyForm = res.data;
      if (this.saleType) this.applyForm.sale_type = this.saleType;
    }
  },
  methods: {
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
          if (res && res.error.errno == 200) {
            this.fileList.splice(i, 1);
            this.$message.success("图片删除成功");
          } else {
            this.$message.success(res.error.usermsg);
          }
        }
      });
    },
    async handleSubmit(str) {
      let arr = [];
      this.fileList.map(el => {
        arr.push(el.url);
      });
      this.$refs["applyForm"].validate(async valid => {
        if (valid) {
          if (arr.length == 0) {
            return this.$message.error("请上传售后凭证");
          }
          let res = await sale_apply_dispose({
            ...this.applyForm,
            id: this.id,
            action: str == 'create' ? 2 : 7, // 申请信息
            apply_img: arr
          });
          if (res && res.error.errno == 200) {
            this.$message.success("已提交，等待粉丝处理");
            this.$emit("update");
          }
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    viewHistory() {
      this.showHistory = true;
    }
  }
};
</script>
<style lang="scss" scope>
.apply {
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
  section {
    display: flex;
    padding-bottom: 20px;
    & > img {
      width: 120px;
      height: 120px;
      margin-right: 20px;
    }
    & > span {
      font-size: 16px;
      text-align: center;
    }
  }

  .el-upload--picture-card {
    width: 110px;
    height: 110px;
    line-height: 113px;
  }
  .money_tips {
    display: block;
    font-size: 12px;
    color: #929292;
  }
}
</style>