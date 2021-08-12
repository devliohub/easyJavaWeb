<template>
  <div class="salerCharge">
    <!-- 图片放大 -->
    <img-dialog ref="imgDialog" />

    <main v-loading="isloading">
      <el-form label-width="150px" :model="form" :rules="formRules" ref="ruleForm" size="small">
        <el-form-item label="支付方式">
          <el-radio v-model="form.pay_way" label="1">银行卡</el-radio>
          <!-- <el-radio v-model="form.pay_way" label="2">支付宝</el-radio> -->
        </el-form-item>
        <el-form-item>
          <span style="color:#FF5500">商家充值提示： 重要提示：为了各位商友的店铺安全，建议直接使用银行app转帐，还可以避免转帐手续费哦~</span>
        </el-form-item>
        <el-form-item label="流动金(元):" prop="flow_charge">
          <el-input v-model.trim="form.flow_charge" clearable />
        </el-form-item>
        <el-form-item label="服务费(元):" prop="service_charge">
          <el-input v-model.trim="form.service_charge" clearable />
        </el-form-item>
        <el-form-item label="合计(元):">
          <el-input
            :disabled="true"
            :value="parseFloat(form.service_charge || 0) + parseFloat(form.flow_charge || 0) || 0"
          />
        </el-form-item>
        <!-- 银行卡 -->
        <el-form-item v-if="form.pay_way == 1" label="收款人姓名:">{{form.name}}</el-form-item>
        <el-form-item v-if="form.pay_way == 1" label="收款人所属行:">{{form.bank}}</el-form-item>
        <el-form-item v-if="form.pay_way == 1" label="收款人银行卡号:">{{form.bank_card}}</el-form-item>
        <el-form-item v-if="form.pay_way == 1" label="付款截图:">
          <el-upload
            class="upload-demo"
            ref="imgsRef"
            action="#"
            :http-request="myUpload"
            accept="image/jpeg, image/gif, image/png"
            list-type="picture"
            :on-remove="handleRemove"
            :on-preview="handleImgClick"
            :file-list="fileList"
            :limit="1"
          >
            <el-button size="small" type="primary">点击上传</el-button>
          </el-upload>
        </el-form-item>
        <!-- 支付宝 -->
        <!-- <el-form-item v-if="form.pay_way == 2" label="支付宝名称">{{form.zfb_name}}</el-form-item>
        <el-form-item v-if="form.pay_way == 2" label="支付宝账号">{{form.zfb_account}}</el-form-item>
        <el-form-item v-if="form.pay_way == 2" label="支付宝二维码">
          <qrcode-vue style="margin-top:10px" :value="form.zfb_img" :size="140" level="H"></qrcode-vue>
          <el-button class="btn_export">
            打开支付宝
            <span style="font-weight:bold">[扫一扫]</span>
          </el-button>
        </el-form-item>-->
      </el-form>
      <div>
        <el-button type="primary" size="small" @click="submitForm">确认</el-button>&nbsp;&nbsp;
        <router-link to="/saler/personal">
          <el-button type="success" size="small">取消</el-button>
        </router-link>
      </div>
    </main>
  </div>
</template>
<script>
import { recharge } from "@/api/saler/index.js";
import QrcodeVue from "qrcode.vue";
import imgDialog from "@/components/imgDialog";
import { uploadImg, deleteUpload } from "@/api/common/common";

export default {
  name: "salerCharge",
  components: { QrcodeVue, imgDialog },
  data() {
    var validateLDJ = (rule, value, callback) => {
      if (value == "" && this.form.service_charge == "") {
        callback(new Error("请填写流动金"));
      } else {
        callback();
      }
    };
    var validateFWF = (rule, value, callback) => {
      if (value == "" && this.form.flow_charge == "") {
        callback(new Error("请填写服务费"));
      } else {
        callback();
      }
    };
    return {
      isloading: false,
      fileList: [],
      form: {},
      formRules: {
        flow_charge: {
          required: false,
          trigger: "blur",
          validator: validateLDJ
        },
        service_charge: {
          required: false,
          trigger: "blur",
          validator: validateFWF
        }
      }
    };
  },
  async mounted() {
    this.isloading = true;
    let res = await recharge();
    if (res && res.error.errno == 200) {
      this.form = {
        // pay_way: res.data.is_zfb ? "2" : "1",
        pay_way: "1",
        flow_charge: "",
        service_charge: "",
        recharge_img: "",
        bank: res.data.bank,
        bank_card: res.data.bank_card,
        name: res.data.name,
        zfb_account: res.data.zfb_account,
        zfb_img: res.data.zfb_img,
        zfb_name: res.data.zfb_name
      };
    }
    this.isloading = false;
  },
  methods: {
    async myUpload(content) {
      this.isloading = true;
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
      this.isloading = false;
    },
    handleRemove(a) {
      this.isloading = true;
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
          this.isloading = false;
        }
      });
    },
    handleImgClick(val) {
      this.$nextTick(() => {
        this.$refs.imgDialog.init(val.url);
      });
    },
    submitForm() {
      let arr = [];
      // 处理图片
      if (this.$refs["imgsRef"]) {
        this.$refs["imgsRef"].fileList.map(el => {
          arr.push(el.url);
        });
      }

      this.$refs["ruleForm"].validate(async valid => {
        if (valid) {
          // 支付宝必传图片
          if (this.form.pay_way == 1 && arr.length == 0) {
            return this.$message.error("请上传付款图片");
          }
          let queryObj = {
            flow_charge: this.form.flow_charge,
            service_charge: this.form.service_charge,
            recharge_img: arr.join(","),
            pay_way: this.form.pay_way
          };
          let res = await recharge(queryObj);
          if (res && res.error.errno == 200) {
            this.$message.success("充值成功,等待后台确认审核");
            setTimeout(() => {
              this.$router.push("/saler/personal");
            }, 500);
          }
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    }
  }
};
</script>
<style lang="scss" scope>
.salerCharge {
  width: 100%;
  main {
    display: flex;
    align-items: center;
    justify-content: center;
    flex-direction: column;
    .el-upload,
    .el-upload--text,
    .uploadBtn {
      width: calc(100% - 10px);
    }
    .el-upload {
      text-align: left;
    }
    .el-form-item__content > span {
      font-size: 12px;
      color: #999;
      padding-left: 5px;
    }
    .el-form-item {
      padding-right: 130px;
      .el-form-item__label {
        font-weight: normal;
        color: #666;
      }
    }
  }
}
</style>