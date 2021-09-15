<template>
  <div class="wenzhang" v-loading="isloading">
    <template v-if="tableData.length > 0">
      <ul class="card_ul" v-if="isCard">
        <li
          v-for="(entity, x) in tableData"
          :key="x"
          :class="{ margin_right_0: x % 4 == 3 }"
          @click="handleClick(entity)"
        >
          <el-image :src="entity.cover" alt="">
            <div slot="error" class="image-slot">
              <img src="@/assets/wenzhangMR.jpg" alt="" />
            </div>
          </el-image>
          <div class="title">
            <span v-if="entity.is_top"> [置顶] </span>
            {{ entity.title }}
          </div>
          <div class="timer">
            {{ dayjs(entity.create_time * 1000).format('YYYY-MM-DD') }}
          </div>
        </li>
        <br />
      </ul>
      <ul class="list_ul" v-else>
        <li
          class="_li"
          :class="{ single_class: x % 2 == 0 }"
          v-for="(item, x) in tableData"
          :key="x"
          @click="handleClick(item)"
        >
          <span>
            <b v-if="item.is_top"> [置顶] </b>
            {{ item.title }}
          </span>
          <span>
            {{ dayjs(item.create_time * 1000).format('YYYY/MM/DD HH:mm') }}
          </span>
        </li>
      </ul>
      <el-pagination
        background
        :hide-on-single-page="true"
        style="margin: 15px 0"
        :pager-count="5"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="form.pageNo"
        :page-size="form.pageSize"
        layout="prev, pager, next, jumper"
        :total="table_total"
      ></el-pagination>
    </template>
    <div v-else class="no_data_div">
      <div>暂无数据</div>
    </div>
  </div>
</template>
<script>
  import { getArticle } from '@/api'

  export default {
    name: 'wenzhang',
    data() {
      return {
        isloading: false,
        isCard: false,

        tableData: [],
        table_total: null,
        form: {
          pageNo: 1,
          pageSize: 12,
        },
      }
    },
    mounted() {
      this.getListData()
    },
    computed: {},
    watch: {
      $route: function (val) {
        if (val) this.getListData()
      },
    },
    methods: {
      async getListData() {
        this.isloading = true
        let res = await getArticle({
          ...this.form,
          name: this.$route.query.keyword,
          menu_id: this.$route.query.id,
          menu_pid: this.$route.query.pid,
        })
        if (res && res.errno == 200) {
          this.tableData = res.result.list
          this.isCard = res.result.layout == 2
          this.table_total = res.result.total
          // 单篇跳转详情
          if (this.table_total == 1) {
            this.handleClick(this.tableData[0])
          }
        }
        this.isloading = false
      },
      handleSizeChange(val) {
        this.form.pageNo = 1
        this.form.pageSize = val
        this.getListData()
      },
      handleCurrentChange(val) {
        this.form.pageNo = val
        this.getListData()
      },
      handleClick(item) {
        this.$router.push(
          '/saler/wenzhangdesc?id=' +
            this.$route.query.id +
            '&name=' +
            this.$route.query.name +
            '&descId=' +
            item.id
        )
      },
    },
  }
</script>
<style lang="scss" scope>
.wenzhang {
  .card_ul {
    font-size: 14px;
    display: flex;
    flex-wrap: wrap;
    li {
      cursor: pointer;
      width: calc(25% - 18px);
      margin: 0 24px 24px 0;
      &:hover {
        .title {
          color: #c00900;
        }
      }
      img {
        width: 100%;
        height: 124px;
      }
      .title {
        max-width: 220px;
        font-weight: 400;
        line-height: 1.5;
        margin: 10px 0;
        font-size: 14px;
        color: #444;
        display: -webkit-box;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 2;
        overflow: hidden;
        span {
          color: #dfa665;
        }
      }
      .timer {
        font-size: 12px;
        color: #969696;
      }
    }
  }

  .list_ul {
    border: 1px solid #efeff7;
    .single_class {
      background: #f5f5f5 !important;
    }
    ._li {
      cursor: pointer;
      background: #fff;
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 15px 30px;
      &:hover {
        background: #efeff7;
      }
      span {
        &:first-child {
          max-width: 85%;
          overflow: hidden;
          white-space: nowrap;
          text-overflow: ellipsis;
        }

        b {
          color: #c00900;
        }
        &:first-child {
          color: #444;
        }
        &:last-child {
          color: #888;
        }
      }
    }
  }
}
</style>