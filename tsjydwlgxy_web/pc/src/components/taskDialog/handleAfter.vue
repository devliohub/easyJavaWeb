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
    <header slot="title">处理售后</header>
    <section>
      <img v-for="(img, index) in entity.apply_img" :key="index" :src="img" alt />
    </section>
    <el-form :model="entity" :rules="applyRules" ref="applyForm" size="small" label-width="120px">
      <el-form-item label="商品标题">{{entity.title}}</el-form-item>
      <el-form-item label="售后原因:">
        <el-select :disabled="true" v-model="entity.sale_type" placeholder="请选择">
          <el-option
            v-for="item in afterReasons"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="粉丝实付金额:" v-if="entity.sale_type == 1">
        <el-input :disabled="true" v-model="entity.reality_price" />
        <span
          class="money_tips"
        >(任务金额：¥{{entity.price}}&nbsp;&nbsp;&nbsp;差价金额：¥{{entity.differ_price}})</span>
      </el-form-item>

      <el-form-item label="申请说明:" prop="comment">
        <el-input
          :disabled="true"
          type="textarea"
          :rows="2"
          placeholder="请输入内容"
          v-model="entity.comment"
        />
      </el-form-item>

      <el-form-item v-if="entity.sale_type == 2" label="物流运单号:">
        <el-input :disabled="canEditNum" placeholder="请输入单号" v-model="entity.logistics_num" />
      </el-form-item>

      <!-- <el-form-item label="物流运单号:" prop="number" v-if="applyForm.reason == '物流问题'">
        <el-input placeholder="请输入运单号" v-model="applyForm.number" />
      </el-form-item>-->

      <el-form-item label="处理说明:" prop="info">
        <el-input :disabled="entity.sale_type == 3" type="textarea" :rows="2" v-model="entity.info" />
      </el-form-item>
    </el-form>
    <span slot="footer" v-if="entity.sale_type == 1">
      <el-button size="small" type="success" @click="handleSubmit(1)">通 过</el-button>
      <el-button size="small" type="danger" @click="handleSubmit(-1)">驳 回</el-button>
    </span>
    <span slot="footer" v-if="entity.sale_type == 2 || entity.sale_type == 4">
      <el-button size="small" class="btn_export" @click="handleSubmit(null)">发 送</el-button>
    </span>
  </el-dialog>
</template>
<script>
import { sale_apply_dispose } from "@/api/saler/freeOrderList";

export default {
  name: "apply",
  props: {
    id: {
      default: null,
      type: [Number, String]
    }
  },
  data() {
    return {
      isloading: false,
      showDialog: true,
      showHistory: false,
      canEditNum: false,
      entity: {},

      applyForm: {
        sale_type: 1,
        comment: "",
        apply_img: []
      },
      applyRules: {
        info: { required: true, trigger: "blur", message: "必填项" }
      },
      afterReasons: [
        {
          label: "资金问题",
          value: 1
        },
        {
          label: "物流问题",
          value: 2
        },
        {
          label: "礼品问题",
          value: 3
        },
        {
          label: "其他问题",
          value: 4
        }
      ]
    };
  },
  async mounted() {
    let res = await sale_apply_dispose({
      id: this.id,
      action: 3 // 获取处理订单详细信息
    });
    if (res && res.error.errno == 200) {
      this.entity = res.saleInfo;
      this.canEditNum = this.entity.logistics_num != ''
    }
  },
  methods: {
    async handleSubmit(num) {
      this.$refs["applyForm"].validate(async valid => {
        if (valid) {
          let res = await sale_apply_dispose({
            id: this.id,
            dispose: num,
            action: 4,
            info: this.entity.info,
            logistics_num: this.entity.logistics_num
          })
          if(res && res.error.errno == 200) {
            this.$message.success('操作成功');
            this.$emit('update')
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