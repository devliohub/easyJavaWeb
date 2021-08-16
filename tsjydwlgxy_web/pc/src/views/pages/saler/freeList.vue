<template>
  <div class="freeList" v-loading="isloading">
    <!-- 表格内容 -->
    <ul v-if="tableData.length > 0">
      <li
        class="_li"
        :class="{ single_class: x % 2 == 0 }"
        v-for="(item, x) in tableData"
        :key="x"
      >
        <span> {{ item.title }}</span>
        <span>{{ item.task_start }}</span>
      </li>
    </ul>
    <div v-else class="no_data_div">
      <div>暂无数据</div>
    </div>
    <el-pagination
      background
      style="margin: 15px 0"
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
  import { indexSearch } from '@/api/common/common.js'

  export default {
    name: 'freeList',
    data() {
      return {
        isloading: false,

        tableData: [],
        table_total: null,
        form: {
          page_no: 1,
          page_size: 5,
        },
        reset: false,
      }
    },
    mounted() {
      this.getListData()
    },
    watch: {},
    methods: {
      async getListData() {
        if (this.reset) {
          this.form.page_no = 1
          this.form.page_size = 5
        }
        this.isloading = true
        let res = await indexSearch({
          ...this.form,
        })
        if (res && res.error.errno == 200) {
          this.tableData = res.data
          this.table_total = res.data.length
        }
        this.isloading = false
      },
      handleSizeChange(val) {
        this.form.page_no = 1
        this.form.page_size = val
        this.reset = false
        this.getListData()
      },
      handleCurrentChange(val) {
        this.reset = false
        this.form.page_no = val
        this.getListData()
      },
    },
  }
</script>
<style lang="scss" scope>
.freeList {
  border: 1px solid #efeff7;
  ._li {
    background: #fff;
    margin-bottom: 10px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 15px 30px;
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