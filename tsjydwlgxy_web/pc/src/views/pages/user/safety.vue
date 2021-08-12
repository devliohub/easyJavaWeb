<template>
  <div class="safety">
    <el-alert
      style="margin-bottom:10px"
      title="请注意：绑定的账号必须与提现宝账户一致，如提交的账户不一致导致无法兑换礼品，淘小熊概不负责！"
      type="warning"
      show-icon
    ></el-alert>
    <section>
      <header>
        <span></span>设置登录密码
      </header>
      <main>
        <img v-if="name && name.avatar" :src="name.avatar" alt @error="name.avatar=require('../../../assets/datouxiang.png')" />
        <img v-else src="@/assets/datouxiang.png" alt />

        <el-form
          label-width="220px"
          :model="ruleForm"
          :rules="rules"
          ref="ruleForm"
          size="small"
        >
          <el-form-item label="原登录密码" prop="old_pwd">
            <el-input v-model.trim="ruleForm.old_pwd" />
          </el-form-item>
          <el-form-item label="新登录密码" prop="new_pwd">
            <el-input v-model.trim="ruleForm.new_pwd" />
            <span class="pw_tips">建议密码5-13位字符，包含英文、数字</span>
          </el-form-item>
          <el-form-item label="确认新密码" prop="re_pwd">
            <el-input v-model.trim="ruleForm.re_pwd" />
          </el-form-item>
        </el-form>

        <el-button type="primary" @click="submitForm">确认修改</el-button>
      </main>
    </section>
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import { modifypwd } from "@/api/sys";

export default {
  name: "safety",
  data() {
    var checkOldPwd = (rule, value, callback) => {
      if (value.length < 6 || value.length > 13) {
        callback(new Error("密码长度不能小于6位或大于13位"));
      } else if (escape(value).indexOf("%u") >= 0) {
        callback(new Error("密码不能有中文"));
      } else {
        callback();
      }
    };
    var validatePass = (rule, value, callback) => {
      if (value.length < 6 || value.length > 13) {
        callback(new Error("密码长度不能小于6位或大于13位"));
      } else if (escape(value).indexOf("%u") >= 0) {
        callback(new Error("密码不能有中文"));
      } else if(value == this.ruleForm.old_pwd) {
        callback(new Error("新密码不能与旧密码相同"));
      } else {
        if (this.ruleForm.re_pwd !== "") {
          this.$refs.ruleForm.validateField("re_pwd");
        }
        callback();
      }
    };
    var validateSamePass = (rule, value, callback) => {
      if (value.length < 6 || value.length > 13) {
        callback(new Error("密码长度不能小于6位或大于13位"));
      } else if (escape(value).indexOf("%u") >= 0) {
        callback(new Error("密码不能有中文"));
      } else if (value !== this.ruleForm.new_pwd) {
        callback(new Error("两次输入密码不一致!"));
      } else {
        callback();
      }
    };
    return {
      ruleForm: {
        new_pwd: "",
        re_pwd: "",
        old_pwd: "",
        platform: "2c"
      },
      rules: {
        new_pwd: [{ validator: validatePass, trigger: "blur" }],
        re_pwd: [{ validator: validateSamePass, trigger: "blur" }],
        old_pwd: [{ validator: checkOldPwd, trigger: "blur" }]
      }
    };
  },
  computed: {
    ...mapGetters(["name"])
  },
  methods: {
    submitForm() {
      this.$refs["ruleForm"].validate(async valid => {
        if (valid) {
          let res = await modifypwd(this.ruleForm);
          if (res && res.error.errno == 200) {
            this.$message.success("密码修改成功！请重新登录");
            this.$store.dispatch("LogOut").then(res => {
              if (res) this.$router.push({ path: "/login?type=user" });
            })
            .catch(err => {});
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
.safety {
  section {
    width: 100%;
    & > header {
      width: 100%;
      font-size: 16px;
      border-bottom: 1px solid #e5e5e5;
      padding: 12px 0;
      font-weight: bold;
      span {
        display: inline-block;
        vertical-align: middle;
        width: 4px;
        height: 18px;
        background: #ff5500;
        margin-right: 12px;
      }
    }
    & > main {
      display: flex;
      align-items: center;
      justify-content: center;
      flex-direction: column;
      & > img {
        width: 80px;
        height: 80px;
        margin: 20px 0;
      }
      .pw_tips {
        padding-left: 10px;
        font-size: 12px;
        color: #999;
      }
    }
  }

  .el-form-item {
    .el-form-item__label {
      font-weight: normal;
      color: #666;
    }
    .el-cascader,
    .el-select,
    .el-input,
    .el-range-editor {
      width: 250px;
    }
  }
}
</style>