<template>
  <el-card class="caidan-container">
    <template #header>
      <div class="header">
        <el-button
          type="warning"
          size="small"
          icon="el-icon-plus"
          @click="handleAdd()"
          >添加用户</el-button
        >
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
            @confirm="handleDelete(scope.row.id)"
          >
            <template #reference>
              <a style="cursor: pointer">删除</a>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <DialogAddCaidan ref="addCaidanRef" :reload="() => getDataList()" />
  </el-card>
</template>

<script>
  import DialogAddCaidan from '@/components/DialogAddCaidan.vue'
  import { ElMessage } from 'element-plus'
  import { onMounted, ref, reactive, toRefs } from 'vue'
  import axios from '@/utils/axios'
  export default {
    name: 'caidan',
    components: {
      DialogAddCaidan,
    },
    setup() {
      const addCaidanRef = ref(null)
      const state = reactive({
        loading: false,
        tableData: [],
      })
      onMounted(() => {
        getDataList()
      })
      // 获取列表
      const getDataList = () => {
        state.loading = true
        axios.get('/api/a/menu/list').then((res) => {
          state.tableData = res
          state.loading = false
        })
      }
      // 添加商品
      const handleAdd = () => {
        addCaidanRef.value.open()
      }
      // 修改商品
      const handleEdit = (id) => {
        addCaidanRef.value.open(id)
      }
      const handleDelete = (id) => {
        axios
          .get('/api/a/menu/delete', {
            params: {
              id: id,
            },
          })
          .then(() => {
            ElMessage.success('删除成功')
            getDataList()
          })
      }

      return {
        ...toRefs(state),
        getDataList,
        handleAdd,
        handleEdit,
        addCaidanRef,
        handleDelete,
      }
    },
  }
</script>

<style scoped>
.caidan-container {
  min-height: 99%;
}
.caidan-container .header {
  display: flex;
  justify-content: space-between;
}
.el-card.is-always-shadow {
  min-height: 99% !important;
}
</style>