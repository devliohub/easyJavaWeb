<template>
  <!-- 手机号弹窗 -->
  <el-dialog
    :show-close="false"
    :center="true"
    :visible.sync="dialogVisible"
    :before-close="handleBeforeClose"
    width="500px"
  >
    <header slot="title">修改手机号</header>
    <el-form :model="TelForm" :rules="telRules" ref="TelForm" size="small" label-width="100px">
      <el-form-item label="手机号码:">{{ params.mobile }}</el-form-item>
      <el-form-item label="新手机号:" prop="mobile">
        <el-input v-model="TelForm.mobile" clearable />&nbsp;&nbsp;
        <el-button v-if="showCount" type="primary" @click="handleSendVerify">发送验证码</el-button>
        <el-button v-else type="primary">重新发送({{countNum}}s)</el-button>
      </el-form-item>
      <el-form-item label="验证码:" prop="sms_code">
        <el-input v-model="TelForm.sms_code" clearable />
      </el-form-item>
      <!-- sms_code -->
    </el-form>

    <span slot="footer">
      <el-button size="small" type="primary" @click="handleChangeMobile()">确 定</el-button>
      <el-button size="small" @click="$emit('cancle')">取 消</el-button>
    </span>
  </el-dialog>
</template>
<script>
import { modifymobile, sendVerify } from "@/api/sys";

export default {
  name: "changeTelDialog",
  props: {
    params: {
      default: {
        user: false,
        mobile: ""
      },
      type: Object
    }
  },
  data() {
    var validateNewMobile = (rule, value, callback) => {
      if (!/^1[3456789]\d{9}$/.test(value)) {
        callback(new Error("请输入11位手机号码"));
      } else if (value == this.params.mobile) {
        callback(new Error("不能与旧电话号码相同"));
      } else {
        callback();
      }
    };
    return {
      timer: null,
      countNum: "", // 倒计时
      showCount: true,

      dialogVisible: true,
      telRules: {
        mobile: {
          required: true,
          trigger: "blur",
          validator: validateNewMobile
        },
        sms_code: [
          { required: true, trigger: "blur", message: "请填写六位验证码" },
          { min: 6, max: 6, message: "请填写六位验证码", trigger: "blur" }
        ]
      },
      TelForm: {
        mobile: "",
        platform: "",
        sms_code: ""
      }
    };
  },
  mounted() {
    this.TelForm.platform = this.params.user ? "2c" : "2b";
  },
  methods: {
    // 倒计时计算
    countVerifyNum() {
      if (!this.timer) {
        this.countNum = 60;
        this.showCount = false;
        this.timer = setInterval(() => {
          if (this.countNum > 1 && this.countNum <= 60) {
            this.countNum--;
          } else {
            this.showCount = true;
            clearInterval(this.timer);
            this.timer = null;
          }
        }, 1000);
      }
    },
    handleBeforeClose() {
      this.$emit("cancle");
    },
    // 发送验证码
    async handleSendVerify() {
      this.$refs.TelForm.validateField("mobile");
      if (this.TelForm.mobile) {
        let queryObj = {
          mobile: this.TelForm.mobile,
          platform: this.params.user ? "2c" : "2b",
          type: 1
        };
        let res = await sendVerify(queryObj);
        if (res && res.error.errno == 200) {
          this.$message.success("验证码发送成功！请在手机上查看");
          this.countVerifyNum();
        }
      }
    },
    async handleChangeMobile() {
      this.$refs["TelForm"].validate(async valid => {
        if (valid) {
          let res = await modifymobile(this.TelForm);
          if (res && res.error.errno == 200) {
            this.$message.success("手机号码修改成功！");
            this.$emit("cancle");
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
<style lang="scss">
</style>