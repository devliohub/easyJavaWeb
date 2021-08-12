<template>
  <div class="addGoods">
    <el-alert
      style="margin-bottom:10px"
      title="请注意：添加宝贝时，平台类型和商品链接必须仔细核对后上传，否则由于商家误操作造成的损失平台配合追回，但不会做出任何赔偿。"
      type="warning"
      show-icon
    ></el-alert>
    <section v-loading="isloading">
      <main>
        <div class="add_section">
          <header>添加活动商品</header>
          <el-form :inline="true" size="mini" class="add_section_content">
            <el-form-item label="平台类型：">
              <el-input style="width:100px" value="天猫/淘宝" disabled />
            </el-form-item>
            <el-form-item label="宝贝链接">
              <el-input v-model="addObj.product_url" style="width:250px" clearable />
            </el-form-item>
            <el-form-item>
              <el-button type="success" @click="handleClickAdd">保存</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div class="query_section">
          <nav>
            <span>查询条件</span>
            <el-input
              placeholder="请输入内容"
              v-model="queryObj.condition_value"
              class="input-with-select"
              size="small"
              clearable
            >
              <el-select
                v-model="queryObj.condition"
                slot="prepend"
                placeholder="请选择"
                @change="reset=true"
              >
                <el-option label="掌柜旺旺" :value="2"></el-option>
                <el-option label="商品编号" :value="1"></el-option>
                <el-option label="标题" :value="3"></el-option>
              </el-select>
              <el-button slot="append" @click="getGoods">查询</el-button>
            </el-input>
          </nav>

          <ul>
            <li
              v-for="(entity, x) in entitys"
              :key="x"
              :class="{'borderLeft': x%4 == 0, 'marginTop': x>3}"
            >
              <itemCards
                :entity="entity"
                @showApply="emitApply"
                @clickUpdate="emitUpdate"
                @showDelete="emitShowDel"
              />
            </li>
          </ul>
          <el-pagination
            background
            style="margin:15px 0"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="queryObj.page_no"
            :page-sizes="[8, 16, 32, 50]"
            :page-size="queryObj.page_size"
            layout="total, sizes, prev, pager, next"
            :total="total_count"
          ></el-pagination>
        </div>
      </main>
    </section>
    <!-- 活动申请弹窗 -->
    <el-dialog :show-close="false" :center="true" :visible.sync="showApply" width="30%">
      <section class="dialog_body">
        <el-radio v-model="radio" :label="1">免单活动</el-radio>
        <el-radio v-model="radio" :label="2">熊抢购活动</el-radio>
      </section>
      <span slot="footer">
        <el-button size="small" type="primary" @click="goToApply">确 定</el-button>
        <el-button size="small" @click="showApply = false">取 消</el-button>
      </span>
    </el-dialog>
    <!-- 删除弹窗 -->
    <el-dialog :show-close="false" :center="true" :visible.sync="showDelete" width="30%">
      <section class="dialog_body" style="font-weight:bold">确认删除宝贝？</section>
      <span slot="footer">
        <el-button size="small" type="primary" @click="handleDel">确 定</el-button>
        <el-button size="small" @click="showDelete = false">取 消</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import itemCards from "@/components/itemCard_saler";
import { salerProducts, salerOption } from "@/api/saler/index";

export default {
  name: "addGoods",
  components: { itemCards },
  data() {
    return {
      reset: false,
      isloading: false,
      addObj: {
        opt: "add",
        product_url: ""
      },
      queryObj: {
        condition: "",
        condition_value: "",
        page_no: 1,
        page_size: 8
      },
      total_count: 0,

      entitys: [],
      target_pid: "",

      input3: "",
      select: "",
      showApply: false,
      showDelete: false,
      radio: ""
    };
  },
  watch: {
    showApply(val) {
      if (val) this.radio = "";
    }
  },
  mounted() {
    this.getGoods();
  },
  methods: {
    async getGoods() {
      if (this.reset) {
        this.queryObj.page_no = 1;
        this.queryObj.page_size = 8;
      }
      this.isloading = true;
      let res = await salerProducts(this.queryObj);
      if (res && res.error.errno == 200) {
        this.entitys = res.data;
        this.total_count = res.total_results;
      }
      this.isloading = false;
    },
    // 新增商品
    async handleClickAdd() {
      if (!this.addObj.product_url) {
        return this.$message.warning("请填写宝贝链接");
      } else {
        this.isloading = true;
        let res = await salerOption(this.addObj);
        if (res && res.error.errno == 200) {
          this.$message.success("添加成功！");
          this.reset = true;
          this.getGoods();
        }
        this.isloading = false;
      }
    },
    // 子组件新增
    emitApply(v, n) {
      this.$router.push("/saler/freeApply?p_id=" + v + '&number=' + n)
      // this.showApply = true;
      // this.target_pid = v;
    },
    // 子组件更新
    async emitUpdate(v) {
      this.isloading = true;
      let res = await salerOption({
        p_id: v,
        opt: "update"
      });
      if (res && res.error.errno == 200) {
        this.$message.success("更新成功！");
        this.getGoods();
      }
      this.isloading = false;
    },
    // 子组件删除
    emitShowDel(v) {
      this.showDelete = true;
      this.target_pid = v;
    },
    // 删除商品
    async handleDel() {
      this.isloading = true;
      let res = await salerOption({
        p_id: this.target_pid,
        opt: "del"
      });
      if (res && res.error.errno == 200) {
        this.$message.success("删除成功！");
        this.showDelete = false;
        this.getGoods();
      }
      this.isloading = false;
    },
    goToApply() {
      if (this.radio == "") {
        this.$message.warning("请先选择申请类型");
      } else {
        this.showApply = false;
        this.radio == 1
          ? this.$router.push("/saler/freeApply?p_id=" + this.target_pid)
          : this.$router.push("/saler/bearBuyApply?p_id=" + this.target_pid);
      }
    },
    handleSizeChange(val) {
      this.queryObj.page_no = 1;
      this.queryObj.page_size = val;
      this.reset = false;
      this.getGoods();
    },
    handleCurrentChange(val) {
      this.queryObj.page_no = val;
      this.reset = false;
      this.getGoods();
    }
  }
};
</script>
<style lang="scss" scope>
.addGoods {
  section {
    width: 100%;
    & > main {
      width: 100%;
      .add_section {
        width: 100%;
        border: 1px solid #ccc;
        & > header {
          font-size: 16px;
          font-weight: bold;
          padding: 10px;
        }
        &_content {
          border-top: 1px solid #ccc;
          padding: 15px 10px;
        }
        .el-form-item {
          margin-bottom: 0;
          .el-select,
          .el-input {
            width: 120px;
          }
        }
      }

      .query_section {
        & > nav {
          display: flex;
          align-items: center;
          margin: 20px 0;
          & > span {
            font-size: 16px;
            margin-right: 10px;
          }
          .input-with-select {
            width: 500px;
          }
          .el-select {
            width: 100px;
          }
          .el-input-group__append {
            color: #fff;
            background-color: #61bf57;
            border: 1px solid #61bf57;
          }
        }

        & > ul {
          font-size: 14px;
          display: flex;
          flex-wrap: wrap;
          // 每行第一个
          .borderLeft {
            border-left: 1px solid #ccc;
          }
          .marginTop {
            margin-top: 20px;
          }
          li {
            width: 242px;
            border: 1px solid #ccc;
            border-left: 0;
          }
        }
      }
    }
  }

  .dialog_body {
    font-size: 16px;
    padding-top: 30px;
    text-align: center;
  }
}
</style>