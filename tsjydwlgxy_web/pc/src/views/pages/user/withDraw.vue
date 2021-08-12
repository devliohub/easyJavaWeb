<template>
  <div class="withDraw">
    <section>
      <header>
        <span></span>用户提现相关说明：
      </header>
      <main>
        <div class="tips">
          <ul>
            <li>1、每天提现次数最多1次，单日上限50000元。</li>
            <li>2、平台暂时只支持提现到银行卡账户。</li>
            <li>3、在国家法定假日和双休日期间，用户可以申请提现，但会在假期后第一个工作日予以处理。由此造成的不便，请多多谅解!</li>
            <li>4、提现后需要2-3个工作日安排提现打款</li>
            <li>5、提现金额不得低于1元，提现的所有手续费均由平台承担。</li>
            <li>6、如有问题，请联系平台客服咨询！</li>
          </ul>
        </div>

        <el-form
          v-loading="isloading"
          ref="refWidthdraw"
          :model="entity"
          :rules="rules"
          status-icon
          size="small"
          label-width="100px"
        >
          <el-form-item label="可提现金额">
            <el-input v-model="canWithdraw" disabled />
            <span class="unit"> 元(人民币)</span>
          </el-form-item>
          <el-form-item label="提现方式">
            <el-radio-group v-model="withDrawType">
              <el-radio label="银行卡"></el-radio>
            </el-radio-group>
          </el-form-item>

          <el-form-item v-if="!isSaler" label="真实姓名">
            <el-input v-model="entity.real_name" disabled />
          </el-form-item>
          <el-form-item v-if="!isSaler" label="开户银行">
            <el-input v-model="bankName" disabled />
          </el-form-item>
          <el-form-item v-if="!isSaler" label="银行卡号">
            <el-input v-model="entity.bank_card" disabled />
          </el-form-item>

          <el-form-item v-if="isSaler" label="提现金额" prop="money">
            <el-input-number
              v-model="entity.money"
              controls-position="right"
              :precision="2"
              :step="1"
              :min="0"
            />
            <span class="unit"> 元(人民币)</span>
          </el-form-item>
          <el-form-item v-if="isSaler" label="提现标注" prop="remark">
            <el-input v-model="entity.remark" placeholder="请填写提现标注" type="textarea" :rows="2" />
          </el-form-item>
        </el-form>

        <el-button
          :loading="isloading"
          type="primary"
          class="submit_btn"
          size="small"
          @click="handleSubmit"
        >确认提现</el-button>
      </main>
    </section>
  </div>
</template>
<script>
import { mcWithdraw } from "@/api/saler/index";
import { uWithdraw } from "@/api/user/index";

export default {
  name: "withDraw",
  data() {
    var validateWithDraw = (rule, value, callback) => {
      if (value == '') {
        callback(new Error("请填写提现金额"));
      } else if (value > this.canWithdraw) {
        callback(new Error("提现金额大于可提现金额"));
      } else {
        callback();
      }
    }
    return {
      isloading: false,
      entity: {}, // 用户提现单用
      canWithdraw: 0, // 可提现金额
      withDrawType: "银行卡",
      bankName: "暂无",
      bankArr: [
        { value: "中国建设银行", label: "中国建设银行" },
        { value: "中国农业银行", label: "中国农业银行" },
        { value: "中国工商银行", label: "中国工商银行" },
        { value: "中国银行", label: "中国银行" },
        { value: "交通银行", label: "交通银行" },
        { value: "招商银行", label: "招商银行" }
      ],
      rules: {
        money: { required: true, validator: validateWithDraw, trigger: "blur" },
        remark: { required: true, message: "请填写提现标注", trigger: "blur" }
      }
    };
  },
  computed: {
    isSaler() {
      return this.$route.name == "salerWithDraw";
    }
  },
  mounted() {
    this.initData();
  },
  methods: {
    async initData() {
      this.entity = {};
      this.isloading = true;
      let res = this.isSaler ? await mcWithdraw() : await uWithdraw();
      if (res && res.error.errno == 200) {
        if (this.isSaler) {
          this.canWithdraw = res.withdraw_money;
        } else {
          this.canWithdraw = res.data.balance;
          this.entity = res.data;
          this.bankName = this.bankArr.filter(el => res.data.bank == el.value)[0].label || '暂无'
        }
      } else if (res.error.errno == 432) {
        setTimeout(() => {
          // 未绑定银行卡
          this.$confirm("是否立即绑定银行卡?", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(() => {
            this.isSaler
              ? this.$router.push("/saler/bindCard")
              : this.$router.push("/user/bindCard");
          });
        }, 1000);
      }
      this.isloading = false;
    },
    handleSubmit() {
      this.$refs["refWidthdraw"].validate(async valid => {
        if (valid) {
          this.isloading = true;
          let res = this.isSaler
            ? await mcWithdraw(this.entity)
            : await uWithdraw(this.entity);
          if (res && res.error.errno == 200)
            this.$message.success("提现成功！");
          this.isloading = false;
          this.initData();
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
.withDraw {
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
      .tips {
        width: 100%;
        margin: 20px 0;
        background-color: #fff7f0;
        padding: 12px;
        list-style-type: order;
        color: #666;
        span {
          display: block;
          font-weight: bold;
          padding-bottom: 20px;
        }
        li {
          padding: 6px 0;
        }
      }
    }
    .unit {
      font-size: 12px;
      color: #999;
    }
  }

  .el-form-item {
    padding-right: 40px;
    .el-form-item__label {
      font-weight: normal;
      color: #666;
    }
    .el-cascader,
    .el-select,
    .el-input,
    .el-textarea,
    .el-range-editor {
      width: 250px;
    }
    .el-input-number--small {
      width: 250px;
    }
    .el-input-number.is-controls-right .el-input__inner {
      text-align: left;
    }
  }
}
</style>