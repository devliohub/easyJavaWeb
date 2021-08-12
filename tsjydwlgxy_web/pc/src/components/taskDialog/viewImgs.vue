<template>
  <!-- 定制评价 -->
  <el-dialog
    custom-class="viewImgs"
    :center="true"
    :visible.sync="showDialog"
    width="35%"
    @closed="$emit('cancle')"
  >
    <header slot="title">{{isAudit ? '定制评价审核' : "好评截图"}}</header>
    <ul>
      <li v-for="item in imgs" :key="item">
        <img :src="item" alt />
      </li>
    </ul>
    <footer slot="footer" v-if="isAudit">
      <el-button @click="handleSubmit(3)" type="success">通过</el-button>
      <el-button @click="handleSubmit(-1)" type="danger">驳回</el-button>
      <el-button v-if="showYanchi" @click="handleSubmit(1)" type="warning">延时</el-button>
    </footer>
  </el-dialog>
</template>
<script>
import { rush_order_action } from "@/api/saler/rushOrderList";
import { free_order_action } from "@/api/saler/freeOrderList";
import { order_action } from "@/api/user/orderList";

export default {
  name: "viewImgs",
  props: {
    fromUser: {
      default: false,
      type: Boolean
    },
    isbear: {
      default: false,
      type: Boolean
    },
    prop_id: {
      default: null,
      type: [Number, String]
    }
  },
  data() {
    return {
      showDialog: true,
      showYanchi: false,
      querObj: {
        id: "",
        type: null
      },
      isAudit: false,
      imgs: []
    };
  },
  async mounted() {
    let res;
    if (this.fromUser) {
      res = await order_action({
        type: 3,
        id: this.prop_id
      });
    } else if (this.isbear) {
      this.querObj.type = 1;
      res = await rush_order_action(this.querObj);
    } else {
      this.querObj.type = 2;
      res = await free_order_action(this.querObj);
    }
    this.imgs = res.img;
  },
  methods: {
    init(row, flag) {
      if (flag) this.isAudit = true;
      if (row) {
        this.querObj.id = row.id;
        this.showYanchi = row.is_delay == -1;
      }
    },
    async handleSubmit(num) {
      let obj = {
        ...this.querObj,
        action: num,
        type: 8,
      }
      let res = await free_order_action(obj);
      this.$message.success('操作成功');
      this.$emit("update")
    }
  }
};
</script>
<style lang="scss" scope>
.viewImgs {
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
  ul {
    overflow: hidden;
    width: 100%;
    li {
      width: 100%;
      float: left;
      margin-bottom: 10px;
      &:last-child {
        margin-bottom: 0;
      }
      img {
        width: 100%;
      }
    }
  }
}
</style>