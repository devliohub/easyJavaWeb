<template>
  <el-card class="kecheng-container">
    <template #header>
      <div class="header">
        <div>
          <el-select
            size="small"
            v-model="queryObj.module_id"
            placeholder="请选择课程模块"
            style="margin-right: 20px"
          >
            <el-option
              v-for="item in moduleOptions"
              :key="item.code"
              :label="item.name"
              :value="item.code"
            >
            </el-option>
          </el-select>
          <el-select
            size="small"
            v-model="queryObj.type_id"
            placeholder="请选择课程类型"
          >
            <el-option
              v-for="item in typeOptions"
              :key="item.code"
              :label="item.name"
              :value="item.code"
            >
            </el-option>
          </el-select>
          <el-input
            size="small"
            style="width: 250px; margin: 0 20px"
            type="text"
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
            >添加课程</el-button
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
      <el-table-column prop="name" label="课程名称"> </el-table-column>
      <el-table-column prop="module_id" label="模块">
        <template #default="scope">
          <span>{{ moduleOptions[scope.row.module_id].name }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="type_id" label="类型">
        <template #default="scope">
          <span>{{ typeOptions[scope.row.type_id].name }}</span>
        </template>
      </el-table-column>
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
      <el-table-column prop="create_uname" label="创建人姓名">
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
            confirm-button-text="是"
            cancel-button-text="否"
            :hide-icon="true"
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

    <DialogKecheng ref="addKechengRef" :reload="() => getDataList()" />
  </el-card>
</template>

<script>
  import DialogKecheng from '@/components/DialogAddKecheng.vue'
  import { ElMessage } from 'element-plus'
  import { onMounted, ref, reactive, toRefs, computed } from 'vue'
  import axios from '@/utils/axios'
  import { localGet } from '@/utils'

  export default {
    name: 'kecheng',
    components: {
      DialogKecheng,
    },
    setup() {
      const moduleOptions = computed(() => {
        return [{ code: 0, name: '全部模块' }].concat(localGet('loModules'))
      })
      const typeOptions = computed(() => {
        return [{ code: 0, name: '全部类型' }].concat(localGet('loTypes'))
      })
      const addKechengRef = ref(null)
      const state = reactive({
        loading: false,
        tableData: [], // 数据列表
        total: 0, // 总条数

        queryObj: {
          module_id: 0,
          type_id: 0,
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
          .get('/api/a/course/list', {
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
        addKechengRef.value.open()
      }
      // 修改商品
      const handleEdit = (id) => {
        addKechengRef.value.open(id)
      }
      const handleDelete = (id) => {
        axios
          .get('/api/a/course/delete', {
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
        addKechengRef,
        handleDelete,
        moduleOptions,
        typeOptions,
      }
    },
  }
</script>

<style scoped>
.kecheng-container {
  min-height: 100%;
}
.kecheng-container .header {
  display: flex;
  justify-content: space-between;
}
.el-card.is-always-shadow {
  min-height: 99% !important;
}
</style>