<template>
  <div class="search" v-loading="isloading">
    <nav>关键词：{{ $route.query.keyword }}</nav>
    <!-- 表格内容 -->
    <ul v-if="tableData.length > 0">
      <li
        class="_li"
        :class="{ single_class: x % 2 == 0 }"
        v-for="(item, x) in tableData"
        :key="x"
        @click="handleGo(item)"
      >
        <span> {{ item.title }}</span>
        <span>{{
          dayjs(item.publish_time * 1000).format('YYYY/MM/DD HH:mm')
        }}</span>
      </li>
    </ul>
    <div v-else class="no_data_div">
      <div>暂无数据</div>
    </div>
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
  </div>
</template>
<script>
  import { getArticle } from '@/api'

  export default {
    name: 'search',
    data() {
      return {
        isloading: false,

        tableData: [],
        table_total: null,
        form: {
          pageNo: 1,
          pageSize: 10,
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
          title: this.$route.query.keyword,
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
      handleGo(item) {
        this.$router.push(
          '/saler/wenzhangdesc?id=' +
            item.menu_id +
            '&name=' +
            (item.menu_name ? item.menu_name : 'undefined') +
            '&descId=' +
            item.id
        )
      },
    },
  }
</script>
<style lang="scss" scope>
.search {
  border: 1px solid #efeff7;
  nav {
    padding: 0 30px;
    height: 50px;
    line-height: 50px;
    font-weight: bold;
  }
  ._li {
    background: #fff;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 20px 30px;
    cursor: pointer;
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