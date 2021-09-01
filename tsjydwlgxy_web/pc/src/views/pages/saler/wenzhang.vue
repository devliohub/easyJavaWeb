<template>
  <div class="wenzhang" v-loading="isloading">
    <ul v-if="tableData.length > 0">
      <li
        class="_li"
        :class="{ single_class: x % 2 == 0 }"
        v-for="(item, x) in tableData"
        :key="x"
        @click="handleClick(item)"
      >
        <span> {{ item.title }}</span>
        <span>
          {{ dayjs(item.create_time * 1000).format('YYYY/MM/DD HH:mm') }}
        </span>
      </li>
      <el-pagination
        background
        style="margin: 15px 0"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="form.pageNo"
        :page-sizes="[10, 20]"
        :page-size="form.pageSize"
        layout="total, sizes, prev, pager, next"
        :total="table_total"
      ></el-pagination>
    </ul>
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

        tableData: [],
        table_total: null,
        form: {
          pageNo: 1,
          pageSize: 10,
          menu_pid: '',
          menu_id: '',
        },
      }
    },
    mounted() {
      this.getListData()
    },
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
        })
        if (res && res.errno == 200) {
          this.tableData = res.result.list
          this.table_total = res.result.total
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
        this.$router.push('/saler/wenzhangdesc?id=' + item.id)
      },
    },
  }
</script>
<style lang="scss" scope>
.wenzhang {
  & > ul {
    border: 1px solid #efeff7;
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
        color: #444;
      }
      &:last-child {
        color: #888;
      }
    }
  }
  .single_class {
    background: #f5f5f5;
  }
}
</style>