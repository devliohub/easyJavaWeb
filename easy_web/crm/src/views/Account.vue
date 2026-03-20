<template>
  <el-card class="account-container">
    <template #header>
      <div class="header">
        <div>
          <el-input
            style="width: 250px; margin-right: 30px"
            type="text"
            size="small"
            clearable
            v-model="queryObj.name"
          ></el-input>
          <el-button
            type="primary"
            size="small"
            icon="el-icon-search"
            @click="getDataList()"
            >搜 索</el-button
          >
        </div>
        <div>
          <el-button
            type="warning"
            size="small"
            icon="el-icon-plus"
            @click="handleAdd()"
            >添加用户</el-button
          >
        </div>
      </div>
    </template>
    <el-table
      v-loading="loading"
      ref="multipleTable"
      :data="tableData"
      tooltip-effect="dark"
      style="width: 100%"
    >
      <el-table-column prop="name" label="姓名"> </el-table-column>
      <el-table-column prop="account" label="账号"> </el-table-column>
      <el-table-column prop="create_time" label="创建时间">
        <template #default="scope">
          <span>{{
            $filters.dateFormater(
              scope.row.create_time * 1000,
              'YYYY-MM-DD HH:mm'
            )
          }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="100">
        <template #default="scope">
          <a
            style="cursor: pointer; margin-right: 10px"
            @click="handleEdit(scope.row.id)"
            >修改</a
          >
          <el-popconfirm
            title="确定删除吗？"
            confirm-button-text="是"
            cancel-button-text="否"
            :hide-icon="true"
            @confirm="handleDelete(scope.row.id)"
          >
            <template #reference>
              <a style="cursor: pointer">删除</a>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <!--总数超过一页，再展示分页器-->
    <el-pagination
      background
      layout="prev, pager, next"
      :total="total"
      :page-size="queryObj.pageSize"
      :current-page="queryObj.pageNo"
      @current-change="changePage"
    />

    <DialogAddUser ref="addUserRef" :reload="() => getDataList()" />
  </el-card>
</template>

<script>
  import DialogAddUser from '@/components/DialogAddUser.vue'
  import { ElMessage } from 'element-plus'
  import { onMounted, ref, reactive, toRefs } from 'vue'
  import axios from '@/utils/axios'
  export default {
    name: 'Account',
    components: {
      DialogAddUser,
    },
    setup() {
      const addUserRef = ref(null)
      const state = reactive({
        loading: false,
        tableData: [], // 数据列表
        total: 0, // 总条数

        queryObj: {
          name: '', // 名称
          pageNo: 1, // 当前页
          pageSize: 10, // 分页大小
        },
      })
      onMounted(() => {
        getDataList()
      })
      // 获取列表
      const getDataList = () => {
        state.loading = true
        axios
          .get('/api/a/user/list', {
            params: state.queryObj,
          })
          .then((res) => {
            state.tableData = res.list
            state.total = res.total
            state.loading = false
          })
      }
      // 添加商品
      const handleAdd = () => {
        addUserRef.value.open()
      }
      // 修改商品
      const handleEdit = (id) => {
        addUserRef.value.open(id)
      }
      const handleDelete = (id) => {
        axios
          .get('/api/a/user/delete', {
            params: {
              id: id,
            },
          })
          .then(() => {
            ElMessage.success('删除成功')
            getDataList()
          })
      }
      const changePage = (val) => {
        state.queryObj.pageNo = val
        getDataList()
      }
      return {
        ...toRefs(state),
        getDataList,
        changePage,
        handleAdd,
        handleEdit,
        addUserRef,
        handleDelete,
      }
    },
  }
</script>

<style scoped>
.account-container {
  min-height: 99%;
}
.account-container .header {
  display: flex;
  justify-content: space-between;
}
.el-card.is-always-shadow {
  min-height: 99% !important;
}
</style>