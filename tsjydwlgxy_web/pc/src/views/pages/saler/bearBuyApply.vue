<template>
  <div class="bearBuyApply">
    <el-alert
      style="margin-bottom:10px"
      title="请注意：添加宝贝时，平台类型和商品链接必须仔细核对后上传，否则由于商家误操作造成的损失平台配合追回，但不会做出任何赔偿!"
      type="warning"
      show-icon
    ></el-alert>
    <section v-loading="isloading">
      <el-collapse v-model="activeNames" class="wrapper">
        <el-collapse-item name="1">
          <template slot="title">
            <header>
              <i></i>活动设置
              <span>(带*的项目为必选项)</span>
            </header>
          </template>
          <main>
            <el-form
              label-position="left"
              label-width="200px"
              :model="actsForm"
              ref="actsForm"
            >
              <el-form-item label="选择活动类型">
                <el-radio v-model="actsForm.activity_type" :label="1">返利单</el-radio>
              </el-form-item>
              <el-form-item label="选择店铺类型">
                <el-radio v-model="actsForm.shop_type" :label="1">天猫店</el-radio>
                <el-radio v-model="actsForm.shop_type" :label="2">C店</el-radio>
              </el-form-item>
              <el-form-item label="买家兑换周期">
                <el-radio v-model="actsForm.c" :label="1">月不超过15单</el-radio>
                <el-radio v-model="actsForm.c" :label="1">全国IP定位</el-radio>
                <el-radio v-model="actsForm.c" :label="1">月接单统计</el-radio>
              </el-form-item>
            </el-form>
          </main>
        </el-collapse-item>
        <el-collapse-item name="2">
          <template slot="title">
            <header>
              <i></i>商品信息
              <span>(带*的项目为必选项)</span>
            </header>
          </template>
          <main>
            <el-form
              label-position="left"
              label-width="180px"
              :model="goodsForm"
              :rules="goodsRules"
              ref="goodsForm"
              size="small"
            >
              <el-form-item label="选择活动商品" prop="p_id">
                <el-select v-model="goodsForm.p_id" @change="handleChangeGoods">
                  <el-option
                    v-for="(_good, x) in goodOptions"
                    :key="x"
                    :value="_good.p_id"
                    :label="_good.title"
                  />
                </el-select>
              </el-form-item>

              <el-form-item label="所属店铺" required>
                <el-input v-model="tempGood.shop_name" placeholder disabled />&nbsp;&nbsp;&nbsp;
                <el-button
                  type="success"
                  style="width:100px"
                  @click="$router.push('/saler/addGoods')"
                >添加宝贝</el-button>
              </el-form-item>
              <el-form-item label="商品图片" required class="goods_img">
                <img
                  class="goods_img_left"
                  v-if="tempGood.img"
                  :src="tempGood.img"
                  alt
                  @error="tempGood.img=require('../../../assets/404_images/404.png')"
                />
                <img class="goods_img_left" v-else src="@/assets/404_images/404.png" alt />
                <div class="goods_img_right">
                  <el-button
                    icon="el-icon-edit"
                    type="primary"
                    style="width:100px;margin-bottom:10px"
                    @click="showEdit_inp = !showEdit_inp"
                  >更换主图</el-button>
                  <br />
                  <el-input
                    :disabled="!showEdit_inp"
                    v-model="tempGood.img"
                    @blur="showEdit_inp = false"
                  />
                </div>
              </el-form-item>

              <el-form-item label="商品标题" prop="title">
                <el-input v-model="goodsForm.title" maxlength="14" clearable />
              </el-form-item>

              <el-form-item label="商品分类" prop="pa_cid">
                <el-select v-model="goodsForm.pa_cid">
                  <el-option
                    v-for="(option, x) in classOptions"
                    :key="x"
                    :value="option.id"
                    :label="option.long_name"
                  />
                </el-select>
              </el-form-item>
            </el-form>
          </main>
        </el-collapse-item>
        <el-collapse-item name="3">
          <template slot="title">
            <header>
              <i></i>任务信息
              <span>可通过搜索优化把关键词排名做上去！</span>
            </header>
          </template>
          <main class="task_info">
            <!-- 头部按钮 单选 -->
            <div class="task_info_nav">
              <el-button type="success" size="small" @click="addKeyword">增加关键词</el-button>
              <el-button type="danger" size="small" @click="delSelets">删除选定</el-button>
              <!-- 价格表单 -->
              <el-form
                class="top_form"
                ref="priceForm"
                :inline="true"
                :model="priceForm"
                :rules="priceRules"
              >
                <el-form-item label="单品价格(元)" prop="price">
                  <el-input-number
                    size="mini"
                    :precision="2"
                    v-model="priceForm.price"
                    controls-position="right"
                    @change="handleChangePrice"
                  />
                </el-form-item>
                <el-form-item label="优惠券(元)" prop="current_price">
                  <el-input-number
                    size="mini"
                    :precision="2"
                    v-model="priceForm.current_price"
                    controls-position="right"
                    @change="handleChangePrice2"
                  />
                </el-form-item>
              </el-form>
            </div>
            <!-- 主表格 -->
            <el-form :model="tableForm" :rules="tableFormRules" ref="tableForm">
              <el-table
                :data="tableForm.tableData"
                empty-text="请新增填写行, 进行填写"
                class="task_table"
                size="mini"
                border
                stripe
                @selection-change="handleSelectionChange"
              >
                <el-table-column type="selection" width="50" align="center" />
                <el-table-column label="任务设置" width="120">
                  <template slot-scope="scope">
                    <el-form-item
                      :prop="'tableData.' + scope.$index + '.task_type'"
                      :rules="tableFormRules.task_type"
                    >
                      <el-select size="mini" v-model="scope.row.task_type" placeholder="请选择">
                        <el-option label="搜索任务" :value="1" />
                        <el-option label="淘口令任务" :value="2" />
                      </el-select>
                    </el-form-item>
                  </template>
                </el-table-column>
                <el-table-column label="关键词/淘口令" width="110">
                  <template slot-scope="scope">
                    <el-form-item
                      :prop="'tableData.' + scope.$index + '.keyword'"
                      :rules="tableFormRules.keyword"
                    >
                      <el-input size="mini" clearable v-model="scope.row.keyword" />
                    </el-form-item>
                  </template>
                </el-table-column>
                <el-table-column label="备选关键词" width="110">
                  <template slot-scope="scope">
                    <el-form-item
                      :prop="'tableData.' + scope.$index + '.keyword2'"
                      :rules="tableFormRules.keyword2"
                    >
                      <el-input size="mini" clearable v-model="scope.row.keyword2" />
                    </el-form-item>
                  </template>
                </el-table-column>

                <el-table-column label="任务数量" width="100">
                  <template slot-scope="scope">
                    <el-form-item
                      :prop="'tableData.' + scope.$index + '.task_count'"
                      :rules="tableFormRules.task_count"
                    >
                      <el-input-number
                        size="mini"
                        :precision="0"
                        v-model="scope.row.task_count"
                        controls-position="right"
                        @change="calc_service(scope.row, scope.$index)"
                      />
                    </el-form-item>
                  </template>
                </el-table-column>
                <!-- 自动计算 -->
                <el-table-column label="服务费">
                  <template slot-scope="scope">
                    <el-form-item :prop="'tableData.' + scope.$index + '.service_charge'">
                      <span>{{scope.row.service_charge}}</span>
                    </el-form-item>
                  </template>
                </el-table-column>
                <el-table-column label="预付款服务费">
                  <template slot-scope="scope">
                    <el-form-item :prop="'tableData.' + scope.$index + '.before_service_charge'">
                      <span>{{scope.row.before_service_charge}}</span>
                    </el-form-item>
                  </template>
                </el-table-column>
                <!-- /自动计算 -->
                <el-table-column label="价格区间" width="160">
                  <template slot-scope="scope">
                    <el-input-number
                      class="price_ranger"
                      size="mini"
                      :precision="2"
                      v-model="scope.row.price_start"
                      controls-position="right"
                    />&nbsp;~
                    <el-input-number
                      class="price_ranger"
                      size="mini"
                      :precision="2"
                      v-model="scope.row.price_end"
                      controls-position="right"
                    />
                  </template>
                </el-table-column>
                <el-table-column label="发货地" width="110">
                  <template slot-scope="scope">
                    <el-form-item :prop="'tableData.' + scope.$index + '.area'">
                      <el-input size="mini" clearable v-model="scope.row.area" />
                    </el-form-item>
                  </template>
                </el-table-column>
              </el-table>
            </el-form>

            <el-input type="textarea" :rows="3" placeholder="请输入备注内容" v-model="taskForm.comment" />
            <el-form
              class="task_form"
              label-position="left"
              label-width="150px"
              :model="taskForm"
              :rules="taskRules"
              ref="taskForm"
              size="small"
            >
              <el-form-item label="发布方式" prop="release_way">
                <el-radio v-model="taskForm.release_way" :label="1">正常单</el-radio>
              </el-form-item>

              <el-form-item label="活动开始日期" prop="start_time">
                <el-date-picker
                  v-model="taskForm.start_time"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  type="datetime"
                  placeholder="选择开始日期时间"
                  default-time="05:00:00"
                  :picker-options="{
                    disabledDate(time) {
                      return time.getTime() < limit_time
                    },
                    selectableRange: '05:00:00 - 23:59:59'
                  }"
                />
              </el-form-item>
              <el-form-item label="活动结束日期" prop="end_time">
                <el-date-picker
                  v-model="taskForm.end_time"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  type="datetime"
                  placeholder="选择结束日期时间"
                  default-time="23:59:59"
                  :picker-options="{
                    disabledDate(time) {
                      return time.getTime() < limit_time
                    },
                  }"
                />
              </el-form-item>
            </el-form>
          </main>
        </el-collapse-item>
      </el-collapse>
      <div class="_btn">
        <el-button v-if="isEdit" type="warning" size="small" @click="handleSubmit('update')">修改活动申请</el-button>
        <el-button v-else type="success" size="small" @click="handleSubmit('add')">提交活动申请</el-button>
      </div>
    </section>
  </div>
</template>
<script>
import dayjs from "dayjs";
import { cateTitle, paOptions, productDesc } from "@/api/common/common";
import { salerProducts, serviceFee } from "@/api/saler/index.js";

export default {
  name: "bearBuyApply",
  data() {
    var validate_price = (rule, value, callback) => {
      if (!value || value < 0) {
        callback(new Error("请填写规范单品价"));
      } else {
        if (this.priceForm.current_price !== "") {
          this.$refs.priceForm.validateField("current_price");
        }
        callback();
      }
    };
    var validate_current_price = (rule, value, callback) => {
      if (!value || value < 0) {
        callback(new Error("请填写规范活动价格"));
      } else if (value > this.priceForm.price) {
        callback(new Error("优惠券不能高于单品价"));
      } else {
        callback();
      }
    };
    return {
      isloading: false,
      classOptions: [], // 礼品分类下拉
      goodOptions: [], // 商品分类下拉
      tempGood: {}, // 存储商品
      isEdit: false, // 是否可编辑
      activeNames: ["1", "2", "3"],

      showEdit_inp: false, // 显示活动商品图片修改input
      actsForm: {
        activity_type: 1,
        shop_type: 1,
        c: 1 // 周期
      },
      goodsForm: {
        p_id: "",
        title: "",
        pa_cid: ""
      },
      // 次日早上6点~后天零点
      taskForm: {
        release_way: 1,
        start_time: dayjs().add(1, 'day').set('hour', 6).set('minute', 0).set('second', 0).format('YYYY-MM-DD HH:mm:ss'),
        end_time: dayjs().add(2, 'day').set('hour', 0).set('minute', 0).set('second', 0).format('YYYY-MM-DD HH:mm:ss'),
      },
      priceForm: {
        price: null,
        current_price: null
      },
      priceRules: {
        price: { required: true, trigger: "blur", validator: validate_price },
        current_price: {
          required: true,
          trigger: "blur",
          validator: validate_current_price
        }
      },
      goodsRules: {
        p_id: { required: true, trigger: "blur", message: "必填项" },
        title: { required: true, trigger: "blur", message: "必填项" },
        pa_cid: { required: true, trigger: "blur", message: "必填项" }
      },
      taskRules: {
        release_way: { required: true, trigger: "blur", message: "必填项" },
        start_time: { required: true, trigger: "blur", message: "必填项" },
        end_time: { required: true, trigger: "blur", message: "必填项" }
      },
      multipleSelection: [],

      // 表格里表单
      tableForm: {
        tableData: [
          {
            before_service_charge: null,
            service_charge: null,
            uid: "u_1"
          }
        ]
      },
      tableFormRules: {
        task_type: { required: true, trigger: "blur" },
        keyword: { required: true, trigger: "blur" },
        keyword2: { required: true, trigger: "blur" },
        price: { required: true, trigger: "blur" },
        task_count: { required: true, trigger: "blur" }
      }
    };
  },
  computed: {
    limit_time() {
      return dayjs()
        .subtract(1, "day")
        .valueOf();
    }
  },
  mounted() {
    this.getData();
  },
  methods: {
    async getData() {
      this.isloading = true;
      let res = await cateTitle();
      this.classOptions = res.data;
      let res1 = await salerProducts({
        page_no: 1,
        page_size: 999
      });
      this.goodOptions = res1.data;
      this.setData();
      this.isloading = false;
    },
    async setData() {
      if (this.$route.query.e) {
        this.isEdit = true;
      }
      if (this.$route.query.pa_id) {
        let res = await productDesc({
          pa_id: this.$route.query.pa_id
        });
        let _data = res.data;
        this.actsForm = {
          pa_id: this.$route.query.pa_id,
          activity_type: _data.activity_type,
          shop_type: parseInt(_data.shop_type),
          c: 1 // 周期
        };
        this.goodsForm = {
          p_id: _data.p_id,
          pa_cid: _data.pa_cid,
          title: _data.title
        };
        this.handleChangeGoods(_data.p_id);
        this.taskForm = {
          release_way: _data.release_way,
          comment: _data.comment,
          start_time: _data.start_time,
          end_time: _data.end_time
        };
        this.tableForm.tableData = _data.task_list;
        this.priceForm = {
          price: _data.task_list[0].price,
          current_price:
            _data.task_list[0].price - _data.task_list[0].current_price // 优惠券=价格-当前价
        };
      }
      // p_id只设置活动商品
      if (this.$route.query.p_id) {
        this.goodsForm = {
          p_id: parseInt(this.$route.query.p_id)
        };
        this.handleChangeGoods(this.$route.query.p_id);
      }
    },
    handleChangePrice() {
      this.tableForm.tableData.map((el, index) => {
        this.$set(el, "price", this.priceForm.price);
        this.calc_service(el, index);
      });
    },
    // 通过活动价计算服务费
    handleChangePrice2(val) {
      this.tableForm.tableData.map((el, index) => {
        this.calc_service(el, index);
      });
    },
    /**
     * 计算服务费
     * row: 列表行数据
     * index: 第几行
     * 1~10 (3); 10~100 (5); 100~150 (8); 150~200 (10); 200~500 (15); 500~1000 (20); 1000 < (1000)
     */
    async calc_service(row, index) {
      let _r = this.priceForm.price - this.priceForm.current_price;
      let c = row.task_count;
      if (_r >= 0 && c) {
        let res = await serviceFee({
          price: _r,
          module_type: "xqg"
        });
        if (res && res.error.errno == 200) {
          this.$set(
            this.tableForm.tableData[index],
            "before_service_charge",
            res.before_service_charge * c
          );
          this.$set(
            this.tableForm.tableData[index],
            "service_charge",
            res.before_service_charge * c
          );
          this.$set(this.tableForm.tableData[index], "current_price", _r);
          this.$set(
            this.tableForm.tableData[index],
            "price",
            this.priceForm.price
          );
        }
      } else {
        return null;
      }
    },
    handleChangeGoods(val) {
      this.tempGood = this.goodOptions.filter(el => el.p_id == val)[0];
      if (!this.tempGood) {
        this.$message.error("活动商品暂已下架");
        setTimeout(() => {
          this.$router.go(-1);
        }, 500);
      }
    },
    // 添加关键词
    addKeyword() {
      let _n = this.tableForm.tableData.length + 1;
      this.tableForm.tableData.push({
        uid: "u_" + _n
      });
    },
    // 删除选定
    delSelets() {
      let ids = []; //选中准备删除的任务id数组
      let s = []; // 排除之后剩下的任务数组

      this.multipleSelection.map(el => {
        if (el.uid) ids.push(el.uid);
        if (el.t_id) ids.push(el.t_id);
      });
      this.tableForm.tableData.map(el => {
        if (ids.indexOf(el.uid) == -1 && ids.indexOf(el.t_id) == -1) s.push(el);
      });
      this.tableForm.tableData = s;
    },
    // 选择勾选改变
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    handleSubmit(opt) {
      console.log(this.tableForm.tableData);
      this.$refs["goodsForm"].validate(valid1 => {
        this.$refs["taskForm"].validate(valid2 => {
          this.$refs["priceForm"].validate(valid3 => {
            this.$refs["tableForm"].validate(async valid4 => {
              if (valid1 && valid2 && valid3 && valid4) {
                // 整理表格数据
                if (
                  dayjs(this.taskForm.start_time) >
                  dayjs(this.taskForm.end_time)
                ) {
                  return this.$message.error("任务结束时间不能小于开始时间");
                }
                // 请求体
                let queryObj = {
                  module_type: "xqg",
                  opt: opt,
                  pa_id: opt == "add" ? "" : this.actsForm.pa_id,
                  activity_type: this.actsForm.activity_type,
                  shop_type: this.actsForm.shop_type,
                  img: this.tempGood.img,
                  ...this.taskForm,
                  ...this.goodsForm,
                  task_list: this.tableForm.tableData
                };
                let res = await paOptions(queryObj);
                if (res && res.error.errno == 200) {
                  this.$message.success('操作成功');
                  this.$router.push("/saler/bearBuyList");
                } else if (
                  res.error.errno == 1000 &&
                  res.error.usermsg == "余额不足,请充值"
                ) {
                  this.$confirm("余额不足,是否前往充值?", "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                  }).then(() => {
                    this.$router.push("/saler/charge");
                  });
                }
              } else {
                this.$message.warning("请完善表单填写");
                return false;
              }
            });
          });
        });
      });
    }
  }
};
</script>
<style lang="scss" scope>
.bearBuyApply {
  .goods_img {
    .el-form-item__content {
      display: flex;
      display: -webkit-box;
    }
    &_left {
      width: 250px;
    }
    &_right {
      margin-left: 15px;
    }
  }
  section {
    width: 100%;
    .wrapper {
      border: 1px solid #e5e5e5;
      margin-bottom: 20px;
      // 设置任务信息
      .task_info {
        margin-left: 0;
        padding: 20px;
        &_nav {
          display: flex;
          align-items: center;
          // 发布方式
          & > span {
            margin: 0 20px 0 60px;
            font-size: 15px;
            i {
              color: red;
            }
          }
          .top_form {
            margin-left: 25px;
            .el-input {
              width: 80px;
            }
            .el-input__inner {
              padding: 0 5px;
            }
            .el-input-number__decrease,
            .el-input-number__increase {
              display: none;
            }
          }
        }
        .task_table {
          margin: 20px 0;
          .el-form-item__error {
            display: none;
          }
          .el-select > .el-input,
          .el-input-number > .el-input {
            width: 100%;
          }
          .el-input,
          .el-select,
          .el-input-number {
            width: 85%;
            .el-input__inner {
              padding: 0 5px;
            }
            .el-input-number__decrease,
            .el-input-number__increase {
              display: none;
            }
          }
          .price_ranger {
            width: 60px;
          }
        }
        .task_form {
          display: flex;
          flex-direction: column;
          justify-content: center;
          align-items: center;
          .el-radio {
            width: 250px;
          }
        }
      }
      .el-collapse-item__header {
        background: #eee;
      }
      header {
        width: 100%;
        font-size: 15px;
        padding-left: 20px;
        font-weight: bold;
        & > i {
          display: inline-block;
          vertical-align: middle;
          width: 4px;
          height: 18px;
          background: #ff5500;
          margin-right: 12px;
        }
        & > span {
          font-size: 12px;
          color: #ff464e;
        }
      }
      main {
        margin: 10px 0 0 100px;
      }
    }
    ._btn {
      text-align: center;
      margin: 30px 0;
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