<template>
  <div class="freeApply">
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
              :rules="actsRules"
              ref="actsForm"
            >
              <el-form-item label="选择活动类型" prop="activity_type">
                <!-- <el-radio v-model="actsForm.activity_type" :label="1">AA单</el-radio> -->
                <el-radio :disabled="!canTakeAB" v-model="actsForm.activity_type" :label="2">AB单</el-radio>
              </el-form-item>
              <el-form-item label="选择店铺类型" prop="shop_type">
                <el-radio v-model="actsForm.shop_type" :label="1">天猫店</el-radio>
                <el-radio v-model="actsForm.shop_type" :label="2">C店</el-radio>
              </el-form-item>
              <el-form-item label="买家兑换周期" prop="c">
                <el-radio v-model="actsForm.c" :label="1">月不超过15单</el-radio>
                <el-radio v-model="actsForm.c" :label="1">全国IP定位</el-radio>
                <el-radio v-model="actsForm.c" :label="1">月接单统计</el-radio>
              </el-form-item>

              <!-- <el-row v-if="actsForm.activity_type == 1">
                <el-col :span="9">
                  <el-form-item label="是否奖励积分" prop="d">
                    <el-radio v-model="actsForm.d" :label="1">是</el-radio>
                    <el-radio v-model="actsForm.d" :label="2">否</el-radio>
                  </el-form-item>
                </el-col>
                <el-col :span="7" class="score_inp">
                  <el-form-item v-if="actsForm.d == 1" prop="integral">
                    <el-input-number
                      v-model="actsForm.integral"
                      :min="0"
                      size="mini"
                      :precision="0"
                      controls-position="right"
                    />
                    <span>积分</span>
                  </el-form-item>
                </el-col>
              </el-row>-->

              <!-- <el-form-item label="是否需要晒图" prop="is_img" v-if="actsForm.activity_type == 1">
                <el-radio v-model="actsForm.is_img" :label="1">是</el-radio>
                <el-radio v-model="actsForm.is_img" :label="-1">否</el-radio>
              </el-form-item>-->
              <el-form-item label="偏远地区不发货" prop="is_remote">
                <el-radio v-model="actsForm.is_remote" :label="1">是</el-radio>
                <el-radio v-model="actsForm.is_remote" :label="-1">否</el-radio>
              </el-form-item>
            </el-form>
          </main>
        </el-collapse-item>
        <el-collapse-item name="2">
          <template slot="title">
            <header>
              <i></i>礼品信息
              <span>(带*的项目为必选项)</span>
            </header>
          </template>
          <main>
            <el-form
              label-position="left"
              label-width="180px"
              :model="giftsForm"
              :rules="giftsRules"
              ref="giftsForm"
              size="small"
            >
              <el-form-item :label="actsForm.activity_type == 1 ? `商品标题` : `礼品标题`" prop="title">
                <el-input v-model="giftsForm.title" maxlength="14" placeholder="请填写标题" clearable />
              </el-form-item>
              <el-form-item :label="actsForm.activity_type == 1 ? `商品分类` : `礼品分类`" prop="pa_cid">
                <el-select v-model="giftsForm.pa_cid">
                  <el-option
                    v-for="(option, x) in classOptions"
                    :key="x"
                    :value="option.id"
                    :label="option.long_name"
                  />
                </el-select>
              </el-form-item>
              <el-form-item
                :label="actsForm.activity_type == 1 ? `商品图片链接` : `礼品图片链接`"
                prop="sipping_url"
              >
                <el-input v-model.trim="giftsForm.sipping_url" placeholder="请填写图片链接" clearable />&nbsp;&nbsp;&nbsp;
                <el-button type="success" style="width:100px" @click="goToImg">跳转图片空间</el-button>
                <el-image
                  class="gifts_img"
                  :src="giftsForm.sipping_url"
                  v-if="giftsForm.sipping_url"
                  @error="imgError"
                  @load="imgLoad"
                />
              </el-form-item>
            </el-form>
          </main>
        </el-collapse-item>
        <el-collapse-item name="3">
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
              <el-form-item label="选择活动商品" prop="number">
                <el-select v-model="goodsForm.number" @change="handleChangeGoods">
                  <el-option v-for="(_good, x) in goodOptions" :key="x" :value="_good.number">
                    <!-- :label="_good.title" -->
                    <span style="font-size:12px;color: #8492a6;">【{{ _good.number }}】</span>
                    <span>{{ _good.title }}</span>
                  </el-option>
                </el-select>
              </el-form-item>

              <el-form-item label="所属店铺" required>
                <el-input v-model="tempGood.shop_name" placeholder disabled />&nbsp;&nbsp;&nbsp;
                <el-button
                  icon="el-icon-plus"
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
            </el-form>
          </main>
        </el-collapse-item>
        <el-collapse-item name="4">
          <template slot="title">
            <header>
              <i></i>设置任务信息
              <span>可通过搜索优化把关键词排名做上去！</span>
            </header>
          </template>
          <main class="task_info">
            <!-- 头部按钮 单选 -->
            <div class="task_info_nav">
              <el-button type="success" size="small" @click="addKeyword">增加关键词</el-button>
              <el-button type="danger" size="small" @click="delSelets">删除选定</el-button>
              <span>
                <i>*</i> 发布方式：
              </span>
              <el-radio v-model="taskForm.release_way" :label="1">正常单</el-radio>
              <el-radio v-model="taskForm.release_way" :label="2" :disabled="overThreeClock">
                临时单
                <span class="tips">每天15:00后不可发布临时单</span>
              </el-radio>
            </div>
            <!-- 主表格 -->
            <el-form :model="tableForm" :rules="tableFormRules" ref="tableForm">
              <el-table
                ref="table_ref"
                :data="tableForm.tableData"
                empty-text="请新增填写行, 进行填写"
                class="task_table"
                size="mini"
                border
                stripe
                @selection-change="handleSelectionChange"
              >
                <el-table-column type="selection" width="50" align="center" />
                <el-table-column label="开始时间" width="180">
                  <template slot-scope="scope">
                    <el-form-item
                      :prop="'tableData.' + scope.$index + '.task_start'"
                      :rules="tableFormRules.task_start"
                    >
                      <el-date-picker
                        value-format="yyyy-MM-dd HH:mm:ss"
                        class="time_ranger"
                        size="mini"
                        v-model="scope.row.task_start"
                        type="datetime"
                        placeholder="开始时间"
                        default-time="05:00:00"
                        :picker-options="{
                          disabledDate(time) {
                            return time.getTime() < limit_time
                          },
                          selectableRange: select_time_range
                        }"
                      />
                    </el-form-item>
                  </template>
                </el-table-column>
                <el-table-column label="结束时间" width="180">
                  <template slot-scope="scope">
                    <el-form-item
                      :prop="'tableData.' + scope.$index + '.task_end'"
                      :rules="tableFormRules.task_end"
                    >
                      <el-date-picker
                        value-format="yyyy-MM-dd HH:mm:ss"
                        class="time_ranger"
                        size="mini"
                        v-model="scope.row.task_end"
                        type="datetime"
                        placeholder="结束时间"
                        default-time="23:59:59"
                        :picker-options="{
                          disabledDate(time) {
                            return time.getTime() < limit_time
                          }
                        }"
                      />
                    </el-form-item>
                  </template>
                </el-table-column>
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
                <el-table-column label="关键词/淘口令" width="80">
                  <template slot-scope="scope">
                    <el-form-item
                      :prop="'tableData.' + scope.$index + '.keyword'"
                      :rules="tableFormRules.keyword"
                    >
                      <el-input size="mini" clearable v-model="scope.row.keyword" />
                    </el-form-item>
                  </template>
                </el-table-column>
                <el-table-column label="备选关键词" width="80">
                  <template slot-scope="scope">
                    <el-form-item
                      :prop="'tableData.' + scope.$index + '.keyword2'"
                      :rules="tableFormRules.keyword2"
                    >
                      <el-input size="mini" clearable v-model="scope.row.keyword2" />
                    </el-form-item>
                  </template>
                </el-table-column>
                <el-table-column label="下单数量" width="70">
                  <template slot-scope="scope">
                    <el-form-item
                      :prop="'tableData.' + scope.$index + '.order_number'"
                      :rules="tableFormRules.order_number"
                    >
                      <el-input-number
                        size="mini"
                        :precision="0"
                        v-model="scope.row.order_number"
                        controls-position="right"
                        @change="calc_service(scope)"
                      />
                    </el-form-item>
                  </template>
                </el-table-column>
                <el-table-column label="单品价格" width="70">
                  <template slot-scope="scope">
                    <el-form-item
                      :prop="'tableData.' + scope.$index + '.price'"
                      :rules="tableFormRules.price"
                    >
                      <el-input-number
                        size="mini"
                        :precision="2"
                        v-model="scope.row.price"
                        controls-position="right"
                        @change="calc_service(scope)"
                      />
                    </el-form-item>
                  </template>
                </el-table-column>
                <el-table-column label="任务数量" width="70">
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
                        @change="calc_service(scope)"
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
                <el-table-column label="发货地" width="100">
                  <template slot-scope="scope">
                    <el-form-item :prop="'tableData.' + scope.$index + '.area'">
                      <el-input size="mini" clearable v-model="scope.row.area" />
                    </el-form-item>
                  </template>
                </el-table-column>
              </el-table>
            </el-form>
            <el-input type="textarea" :rows="3" placeholder="请输入备注内容" v-model="taskForm.comment" />
          </main>
        </el-collapse-item>
      </el-collapse>

      <div class="_btn">
        <el-button v-if="isEdit" type="warning" size="small" @click="handleSubmit('update')">修改活动申请</el-button>
        <el-button
          :loading="btnLoading"
          v-else
          type="success"
          size="small"
          @click="handleSubmit('add')"
        >提交活动申请</el-button>
      </div>
    </section>
  </div>
</template>
<script>
import dayjs from "dayjs";
import { cateTitle, paOptions, productDesc } from "@/api/common/common";
import { salerProducts, serviceFee } from "@/api/saler/index";

export default {
  name: "freeApply",
  data() {
    return {
      btnLoading: false,
      isloading: false,
      classOptions: [], // 礼品分类下拉
      goodOptions: [], // 商品分类下拉
      tempGood: {}, // 存储商品
      isEdit: false, // 是否可编辑
      activeNames: ["1", "2", "3", "4"],

      img_load_sucess: null, // 图片是否加载完成

      showEdit_inp: false, // 显示活动商品图片修改input
      actsForm: {
        pa_id: null,
        activity_type: null,
        shop_type: 1,
        is_img: -1,
        is_remote: -1,
        d: 2, // 积分
        c: 1 // 兑换周期默认
      },
      giftsForm: {
        title: "",
        pa_cid: "",
        sipping_url: ""
      },
      goodsForm: {
        p_id: "",
        number: ""
      },
      taskForm: {
        release_way: 1,
        comment: ""
      },
      actsRules: {
        activity_type: [{ required: true, trigger: "blur", message: "必填项" }],
        shop_type: [{ required: true, trigger: "blur", message: "必填项" }],
        is_img: [{ required: true, trigger: "blur", message: "必填项" }],
        is_remote: [{ required: true, trigger: "blur", message: "必填项" }],
        c: [{ required: true, trigger: "blur", message: "必填项" }],
        d: [{ required: true, trigger: "blur", message: "必填项" }],
        integral: [{ required: true, trigger: "blur", message: "积分必填" }]
      },
      giftsRules: {
        title: [{ required: true, trigger: "blur", message: "必填项" }],
        pa_cid: [{ required: true, trigger: "blur", message: "必填项" }],
        sipping_url: [{ required: true, trigger: "blur", message: "必填项" }]
      },
      goodsRules: {
        number: [{ required: true, trigger: "blur", message: "必填项" }]
      },
      multipleSelection: [],

      // 表格里表单 初始化正常单时间
      tableForm: {
        tableData: [
          {
            before_service_charge: null,
            service_charge: null,
            uid: "u_1",
            task_start: dayjs()
              .add(1, "day")
              .set("hour", 6)
              .set("minute", 0)
              .set("second", 0)
              .format("YYYY-MM-DD HH:mm:ss"),
            task_end: dayjs()
              .add(2, "day")
              .set("hour", 0)
              .set("minute", 0)
              .set("second", 0)
              .format("YYYY-MM-DD HH:mm:ss")
          }
        ]
      },
      tableFormRules: {
        task_type: { required: true, trigger: "blur" },
        keyword: { required: true, trigger: "blur" },
        keyword2: { required: true, trigger: "blur" },
        order_number: { required: true, trigger: "blur" },
        price: { required: true, trigger: "blur" },
        task_count: { required: true, trigger: "blur" },
        task_start: { required: true, trigger: "blur" },
        task_end: { required: true, trigger: "blur" }
      }
    };
  },
  mounted() {
    this.initData();
  },
  computed: {
    // 'taskForm.release_way': function() {
    //   return 1
    // },
    // 是否有权限下ab单
    canTakeAB() {
      return this.$store.state.user.name.is_ab > 0;
    },
    // 临时单超过下午3点失效
    overThreeClock() {
      return dayjs().isAfter(dayjs().format("YYYY-MM-DD 15:00:00"));
    },
    // 开始时间大于当日之前, 结束时间大于开始时间
    limit_time() {
      return dayjs()
        .subtract(1, "day")
        .valueOf();
    },
    // '05:00:00 - 23:59:59'
    select_time_range() {
      return "06:00:00 - 23:59:59";
    }
  },
  watch: {
    "taskForm.release_way": function(val) {
      // 1正常单 2临时单
      if (val == 1) {
        this.tableForm.tableData.map((el, index) => {
          this.$set(
            el,
            "task_start",
            dayjs()
              .add(1, "day")
              .set("hour", 6)
              .set("minute", 0)
              .set("second", 0)
              .format("YYYY-MM-DD HH:mm:ss")
          );
          this.$set(
            el,
            "task_end",
            dayjs()
              .add(2, "day")
              .set("hour", 0)
              .set("minute", 0)
              .set("second", 0)
              .format("YYYY-MM-DD HH:mm:ss")
          );
        });
      } else if (val == 2) {
        this.tableForm.tableData.map((el, index) => {
          // 判断是否在当天凌晨6点以前
          if (dayjs().isBefore(dayjs().format("YYYY-MM-DD 06:00:00"))) {
            this.$set(
              el,
              "task_start",
              dayjs()
                .set("hour", 6)
                .set("minute", 0)
                .set("second", 0)
                .format("YYYY-MM-DD HH:mm:ss")
            );
          } else {
            this.$set(el, "task_start", dayjs().format("YYYY-MM-DD HH:mm:ss"));
          }

          this.$set(
            el,
            "task_end",
            dayjs()
              .add(1, "day")
              .set("hour", 0)
              .set("minute", 0)
              .set("second", 0)
              .format("YYYY-MM-DD HH:mm:ss")
          );
        });
      }
    }
  },
  methods: {
    imgError(e) {
      this.img_load_sucess = false;
      console.log(e);
    },
    imgLoad(e) {
      this.img_load_sucess = true;
      console.log(1111, e);
    },
    // 初始化下拉框数据
    async initData() {
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
      // pa_id设置编辑状态数据
      if (this.$route.query.pa_id) {
        let res = await productDesc({
          pa_id: this.$route.query.pa_id,
          task_all: 1
        });
        let _data = res.data;
        this.actsForm = {
          pa_id: this.$route.query.pa_id,
          activity_type: this.canTakeAB ? _data.activity_type : 1,
          shop_type: parseInt(_data.shop_type),
          is_img: _data.is_img,
          is_remote: _data.is_remote,
          integral: _data.integral,
          c: 1, // 周期
          d: _data.integral > 0 ? 1 : 2 // 积分
        };
        this.giftsForm = {
          title: _data.title,
          pa_cid: _data.pa_cid,
          sipping_url: _data.sipping_url
        };
        this.goodsForm = {
          p_id: _data.p_id,
          number: _data.number
        };
        this.handleChangeGoods(_data.number);
        //  _data.release_way
        this.taskForm = {
          release_way: this.overThreeClock ? 1 : 2,
          comment: _data.comment
        };
        this.tableForm.tableData = _data.task_list;
        if (this.overThreeClock) {
          this.tableForm.tableData.map((el, index) => {
            this.$set(
              el,
              "task_start",
              dayjs()
                .add(1, "day")
                .set("hour", 6)
                .set("minute", 0)
                .set("second", 0)
                .format("YYYY-MM-DD HH:mm:ss")
            );
            this.$set(
              el,
              "task_end",
              dayjs()
                .add(2, "day")
                .set("hour", 0)
                .set("minute", 0)
                .set("second", 0)
                .format("YYYY-MM-DD HH:mm:ss")
            );
          });
        } else {
          this.tableForm.tableData.map((el, index) => {
            this.$set(el, "task_start", dayjs().format("YYYY-MM-DD HH:mm:ss"));
            this.$set(
              el,
              "task_end",
              dayjs()
                .add(1, "day")
                .set("hour", 0)
                .set("minute", 0)
                .set("second", 0)
                .format("YYYY-MM-DD HH:mm:ss")
            );
          });
        }
      }
      // p_id只设置活动商品
      if (this.$route.query.p_id) {
        this.goodsForm = {
          p_id: parseInt(this.$route.query.p_id),
          number: parseInt(this.$route.query.number)
        };
        this.handleChangeGoods(this.$route.query.number);
      }
    },
    /**
     * 计算服务费
     * 1~10 (3); 10~100 (5); 100~150 (8); 150~200 (10); 200~500 (15); 500~1000 (20); 1000 < (1000)
     */
    async calc_service(scope) {
      let a = scope.row.order_number;
      let b = scope.row.price;
      let c = scope.row.task_count;
      let _r = a * b;
      if (a && b && c) {
        let res = await serviceFee({
          price: _r,
          module_type: "free"
        });
        if (res && res.error.errno == 200) {
          this.$set(
            this.tableForm.tableData[scope.$index],
            "before_service_charge",
            res.before_service_charge * c
          );
          this.$set(
            this.tableForm.tableData[scope.$index],
            "service_charge",
            res.before_service_charge * c
          );
        }
      }
    },
    handleChangeGoods(val) {
      this.tempGood = this.goodOptions.filter(el => el.number == val)[0];
      console.log(JSON.parse(JSON.stringify(this.tempGood)));
      if (!this.tempGood) {
        this.$message.error("活动商品暂已下架");
        setTimeout(() => {
          this.$router.go(-1);
        }, 500);
      }
    },
    // 打开图片链接
    goToImg() {
      window.open("http://tadget.taobao.com/redaction/manager.htm");
    },
    // 添加关键词
    addKeyword() {
      let _n = this.tableForm.tableData.length + 1;
      this.tableForm.tableData.push({
        uid: "u_" + _n,
        task_start:
          this.taskForm.release_way == 1
            ? dayjs()
                .add(1, "day")
                .set("hour", 6)
                .set("minute", 0)
                .set("second", 0)
                .format("YYYY-MM-DD HH:mm:ss")
            : dayjs().format("YYYY-MM-DD HH:mm:ss"),
        task_end:
          this.taskForm.release_way == 1
            ? dayjs()
                .add(2, "day")
                .set("hour", 0)
                .set("minute", 0)
                .set("second", 0)
                .format("YYYY-MM-DD HH:mm:ss")
            : dayjs()
                .add(1, "day")
                .set("hour", 0)
                .set("minute", 0)
                .set("second", 0)
                .format("YYYY-MM-DD HH:mm:ss")
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
    // 提交方法
    async handleSubmit(opt) {
      let allow = true;
      this.$refs["giftsForm"].validate(valid1 => {
        this.$refs["goodsForm"].validate(valid2 => {
          this.$refs["tableForm"].validate(async valid3 => {
            if (valid1 && valid2 && valid3) {
              // 整理表格数据
              this.tableForm.tableData.map(el => {
                if (dayjs(el.task_start) < dayjs().subtract(1, "hour")) {
                  allow = false;
                  return this.$message.error("任务开始时间过早");
                }
                if (dayjs(el.task_start) > dayjs(el.task_end)) {
                  allow = false;
                  return this.$message.error("任务结束时间应该大于开始时间");
                }
                if (
                  this.overThreeClock &&
                  dayjs(el.task_start) <
                    dayjs()
                      .add(1, "day")
                      .set("hour", 0)
                      .set("minute", 0)
                      .set("second", 0)
                ) {
                  allow = false;
                  return this.$message.error(
                    "三点之后发布的任务开始时间不能早于次日0点"
                  );
                }
              });
              // 图片校验
              if (!this.img_load_sucess)
                return this.$message.error("图片链接地址错误");

              // 请求体
              let queryObj = {
                module_type: "free",
                opt: opt,
                pa_id: opt == "add" ? "" : this.actsForm.pa_id,
                activity_type: this.actsForm.activity_type,
                shop_type: this.actsForm.shop_type,
                integral: this.actsForm.integral,
                is_img: this.actsForm.is_img,
                is_remote: this.actsForm.is_remote,
                img: this.tempGood.img,
                p_id: this.tempGood.p_id,
                ...this.giftsForm,
                ...this.taskForm,
                task_list: this.tableForm.tableData
              };
              if (allow) {
                this.btnLoading = true;
                let res = await paOptions(queryObj);
                this.btnLoading = false;
                if (res && res.error.errno == 200) {
                  this.$message.success("操作成功");
                  this.$router.push("/saler/freeList");
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
              }
            } else {
              this.$message.warning("请完善表单填写");
              return false;
            }
          });
        });
      });
    }
  }
};
</script>
<style lang="scss" scope>
.freeApply {
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
              vertical-align: middle;
              color: red;
            }
          }
          .tips {
            color: red;
            margin: 0 10px;
            font-size: 12px;
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
          .time_ranger .el-input__prefix {
            display: none;
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
      .el-button {
        width: 200px;
      }
    }
    .gifts_img {
      width: 60%;
      // height: 10%;
      padding: 10px 0;
    }

    .score_inp {
      .el-form-item__content {
        margin-left: 0 !important;
        .el-input-number--mini {
          width: 100px;
        }
        .el-input {
          width: 80px;
        }
      }
      .el-input-number__decrease,
      .el-input-number__increase {
        display: none;
      }
      .el-input-number.is-controls-right .el-input__inner {
        padding: 0 !important;
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