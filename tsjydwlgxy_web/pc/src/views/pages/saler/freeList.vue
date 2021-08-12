<template>
  <div class="freeList" v-loading="isloading">
    <!-- 完成情况弹窗 -->
    <completeForm ref="complete_ref" v-if="showComplete" @close="showComplete = false" />

    <el-form :inline="true" size="mini">
      <!-- <el-form-item label="商家旺旺或商品编号：">
        <el-input v-model="form.keywords" placeholder="请输入商家旺旺或商品编号" clearable />
      </el-form-item> -->

      <el-form-item>
        <el-input class="shorter" clearable placeholder="请输入" v-model="form.condition">
          <el-select v-model="form.keywords" slot="prepend" placeholder="请选择">
            <el-option label="商家旺旺" value="1"></el-option>
            <el-option label="商品编号" value="2"></el-option>
            <el-option label="淘宝ID" value="3"></el-option>
          </el-select>
        </el-input>
      </el-form-item>

      <el-form-item label="任务提交时间：">
        <el-date-picker
          v-model="taskRange"
          type="daterange"
          value-format="yyyy-MM-dd"
          @change="handleTimeChange"
          range-separator="~"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button icon="el-icon-search" size="mini" type="primary" @click="getListData()">查询</el-button>
        <el-button
          icon="el-icon-download"
          size="mini"
          class="btn_export"
          @click="getListData(true)"
        >导出数据</el-button>
      </el-form-item>
    </el-form>

    <el-tabs @tab-click="tabClick" v-model="form.status">
      <el-tab-pane>
        <span slot="label">所有活动</span>
      </el-tab-pane>
      <el-tab-pane>
        <span slot="label">审核退回</span>
      </el-tab-pane>
      <el-tab-pane>
        <span slot="label">待审核</span>
      </el-tab-pane>
      <el-tab-pane>
        <span slot="label">已审核</span>
      </el-tab-pane>
      <el-tab-pane>
        <span slot="label">已完成</span>
      </el-tab-pane>
      <el-tab-pane>
        <span slot="label">未抢完活动</span>
      </el-tab-pane>
    </el-tabs>

    <!-- 表格内容 -->
    <ul v-if="tableData.length > 0">
      <li class="_li" v-for="(item, x) in tableData" :key="x">
        <div class="table_title">
          <span
            class="table_title_left"
          >商家用户名：{{name.account}}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;提交日期：{{item.create_time}}</span>
          <span class="table_title_right">活动日期：{{item.start_time}} ~ {{item.end_time}}</span>
        </div>
        <div class="table_main">
          <div style="width:280px">
            <header>商品信息</header>
            <main class="good_info">
              <img :src="item.img" alt @click="handleGoProducturl(item)" />
              <div class="good_info_content">
                <span>
                  宝贝标题: <i>{{item.title}}</i>
                </span>
                <span>
                  宝贝编号: <i>{{item.number}}</i>
                </span>
                <span>
                  掌柜旺旺: <i>{{item.shop_ww}}</i>
                </span>
              </div>
            </main>
          </div>
          <div style="width:220px">
            <header>赠品信息</header>
            <main class="gift_info">
              <div class="gift_info_left">
                <img :src="item.sipping_url" alt />
              </div>
              <div class="liner"></div>
              <div class="gift_info_right">
                <ul>
                  <li>礼品分类: {{item.cate_name || '全部商品'}}</li>
                  <li>活动类型: {{activitytypeArr[item.activity_type - 1]}}</li>
                </ul>
              </div>
            </main>
          </div>
          <div style="width:150px">
            <header>任务费用</header>
            <main class="task_cost">
              <ul>
                <li>
                  总金额
                  <i>¥{{(item.guarantee_fee + item.service_fee).toFixed(2)}}</i>
                </li>
                <li>
                  担保金额
                  <i>¥{{item.guarantee_fee}}</i>
                </li>
                <li>
                  服务费
                  <i>¥{{item.service_fee}}</i>
                </li>
              </ul>
            </main>
          </div>
          <div style="width:100px">
            <header>相关数量</header>
            <main class="relate_num">
              <span>{{item.task_total}}件</span>
              <br />待抢数量:
              <br />
              <br />
              <span>{{item.remain_total}}件</span>
            </main>
          </div>
          <div style="width:100px">
            <header>任务状态</header>
            <main class="task_status">{{item.status}}</main>
          </div>
          <div style="flex:1">
            <header>操作</header>
            <main :class="{'btn_4': item.status == '待审核', 'btn_2': item.status != '待审核'}">
              <el-button size="mini" type="success" @click="rehandleTask(item)">重复任务</el-button>
              <el-button size="mini" class="btn_export" @click="openComplete(item)">完成情况</el-button>
              <el-button
                size="mini"
                v-if="item.status == '待审核'"
                type="warning"
                @click="rehandleTask(item, true)"
              >修改任务</el-button>
              <el-button
                size="mini"
                v-if="item.status == '待审核'"
                type="danger"
                @click="delTask(item)"
              >作废任务</el-button>
            </main>
          </div>
        </div>
      </li>
    </ul>
    <div v-else class="no_data_div">
      <div>暂无数据</div>
    </div>
    <el-pagination
      background
      style="margin:15px 0"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="form.page_no"
      :page-sizes="[5, 10, 20]"
      :page-size="form.page_size"
      layout="total, sizes, prev, pager, next"
      :total="table_total"
    ></el-pagination>
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import { indexSearch, indexSearch_EX, paOptions } from "@/api/common/common.js";
import completeForm from "@/components/completeForm";
import dayjs from "dayjs";

export default {
  name: "freeList",
  data() {
    return {
      showComplete: false,
      isloading: false,

      activitytypeArr: ["AA单", "AB单", "熊抢购"],
      tableData: [],
      table_total: null,
      form: {
        condition: "",
        module_type: 1,
        type: 3, //商家后台展示
        start_time: dayjs().subtract(7, 'day').format("YYYY-MM-DD"),
        end_time: dayjs().format("YYYY-MM-DD"),
        keywords: "",
        status: 0,
        page_no: 1,
        page_size: 5
      },
      reset: false,
      taskRange: [
        dayjs().subtract(7, 'day')
        .format("YYYY-MM-DD"),
        dayjs().format("YYYY-MM-DD")
      ]
    };
  },
  components: { completeForm },
  computed: {
    ...mapGetters(["name"])
  },
  mounted() {
    this.getListData();
  },
  watch: {
    'form.keywords': function(val) {
      this.reset = true
    }
  },
  methods: {
    async getListData(flag) {
      if(this.reset) {
        this.form.page_no = 1;
        this.form.page_size = 5;
      }
      if (flag) {
        let res = await indexSearch_EX(this.form);
        let downloadElement = document.createElement("a");
        let href = window.URL.createObjectURL(res); // 创建下载的链接
        downloadElement.href = href;
        downloadElement.download = "免单活动列表.xls";
        document.body.appendChild(downloadElement);
        downloadElement.click(); // 触发点击
        document.body.removeChild(downloadElement); //下载完成移除元素
        window.URL.revokeObjectURL(href); //释放掉blob对象
      } else {
        this.isloading = true;
        let res = await indexSearch({
          ...this.form
        });
        if (res && res.error.errno == 200) {
          this.tableData = res.data;
          this.table_total = res.total_results || 0;
        }
        this.isloading = false;
      }
    },
    // 重复任务 bol为编辑
    rehandleTask(item, bol) {
      if (item) {
        if (bol) {
          this.$router.push("/saler/freeApply?pa_id=" + item.pa_id + "&e=1");
        } else {
          this.$router.push("/saler/freeApply?pa_id=" + item.pa_id);
        }
      }
    },
    handleTimeChange(val) {
      if (val) {
        this.form.start_time = val[0];
        this.form.end_time = val[1];
      } else {
        this.form.start_time = this.form.end_time = "";
      }
      this.reset = true
    },
    handleGoProducturl(val) {
      if(val && val.product_url) window.open(val.product_url)
    },
    // 完成情况
    openComplete(item) {
      this.showComplete = true;
      this.$nextTick(() => {
        this.$refs["complete_ref"].init(item);
      });
    },
    tabClick(tab) {
      this.reset = true
      this.getListData();
    },
    // 删除任务
    delTask(item) {
      this.$confirm("此操作将删除该任务, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(async () => {
          let res = await paOptions({
            pa_id: item.pa_id,
            opt: "del"
          });
          if (res && res.error.errno == 200) {
            this.$message.success('操作成功');
            this.getListData();
          }
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除"
          });
        });
    },
    handleSizeChange(val) {
      this.form.page_no = 1;
      this.form.page_size = val;
      this.reset = false
      this.getListData();
    },
    handleCurrentChange(val) {
      this.reset = false
      this.form.page_no = val;
      this.getListData();
    }
  }
};
</script>
<style lang="scss" scope>
.freeList {
  .shorter {
    .el-cascader,
    .el-select,
    .el-input,
    .el-range-editor {
      width: 100px;
    }
  }

  ._li {
    margin-bottom: 10px;
  }
  .table_title {
    width: 100%;
    padding: 8px 10px;
    border: 1px solid #ccc;
    border-bottom: none;
    display: flex;
    justify-content: space-between;
    align-items: center;
    background: #fdf6ec;
    &_left {
      color: #dc8c00;
      font-size: 14px;
    }
    &_right {
      font-size: 12px;
      color: #999;
    }
  }

  .table_main {
    width: 100%;
    border: 1px solid #ccc;
    display: flex;
    font-size: 14px;
    & > div {
      border-left: 1px solid #ccc;
      &:first-child {
        border-left: 0;
      }

      & > header {
        text-align: center;
        height: 30px;
        line-height: 30px;
        border-bottom: 1px solid #ccc;
        color: #666;
      }
      & > main {
        height: calc(100% - 30px);
      }
      // 商品信息
      .good_info {
        display: flex;
        padding: 5px;
        & > img {
          width: 120px;
          height: 120px;
          margin-right: 10px;
          cursor: pointer;
        }
        &_content {
          // flex: 1;
          max-width: 140px;
          display: flex;
          flex-direction: column;
          & > span {
            flex: 1;
            width: 100%;
            font-weight: bold;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            i {
              margin-top: 5px;
              display: block;
              color: #999;
              font-weight: normal;
              font-size: 12px;
            }
          }
        }
      }
      // 赠品信息
      .gift_info {
        display: flex;
        font-size: 12px;
        &_left {
          display: flex;
          padding: 5px;
          align-items: center;
          img {
            line-height: 1;
            width: 70px;
            height: 70px;
          }
          span {
            flex: 1;
            width: 100%;
            padding-top: 5px;
            font-weight: bold;
            // overflow: hidden;
            i {
              color: #999;
              font-weight: normal;
            }
          }
        }
        .liner {
          width: 2px;
          height: 100%;
          background: #ccc;
        }
        &_right {
          width: 100%;
          padding: 5px;
          & > ul {
            height: 100%;
            display: flex;
            flex-direction: column;
            li {
              flex: 1;
              display: flex;
              justify-content: center;
              align-items: center;
            }
          }
        }
      }
      // 任务费用
      .task_cost {
        font-size: 14px;
        & > ul {
          display: flex;
          flex-direction: column;
          height: 100%;
          padding: 10px;
          li {
            display: flex;
            align-items: center;
            flex: 1;
            justify-content: center;
            font-weight: bold;
            span {
              color: #999;
              font-weight: normal;
            }
            i {
              margin-left: 5px;
            }
          }
        }
      }
      // 相关数量
      .relate_num {
        display: flex;
        justify-content: center;
        align-items: center;
        flex-direction: column;
        & > span {
          font-weight: bold;
        }
      }
      // 任务状态
      .task_status {
        display: flex;
        justify-content: center;
        align-items: center;
      }
      // 操作
      .btn_4 {
        display: flex;
        flex-direction: column;
        padding: 10px 20px;
        .el-button {
          padding: 5px 8px;
          margin-bottom: 5px;
        }
        .el-button + .el-button {
          margin-left: 0;
        }
      }
      .btn_2 {
        display: flex;
        flex-direction: column;
        padding: 30px 20px;
        .el-button {
          padding: 5px 8px;
          margin-bottom: 10px;
        }
        .el-button + .el-button {
          margin-left: 0;
        }
      }
    }
  }
}
</style>