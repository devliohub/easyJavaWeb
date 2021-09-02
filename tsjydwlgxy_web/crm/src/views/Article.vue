<template>
  <el-card class="article-container">
    <template #header>
      <div class="header">
        <div>
          <el-select
            size="small"
            v-model="queryObj.menu_pid"
            placeholder="请选择一级菜单"
            clearable
            @change="handleMenuchange"
          >
            <el-option
              v-for="item in menuOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
          <el-select
            size="small"
            v-model="queryObj.menu_id"
            placeholder="请选择二级菜单"
            clearable
          >
            <el-option
              v-for="item in submenuOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
          <el-input
            size="small"
            style="width: 250px; margin: 0 30px"
            type="text"
            clearable
            v-model="queryObj.title"
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
            >添加文章</el-button
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
      <el-table-column prop="title" label="标题"> </el-table-column>
      <el-table-column prop="module_id" label="所属栏目">
        <template #default="scope">
          <span>{{ scope.row.menu_name }} / {{ scope.row.menu_pname }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="create_uname" label="发布人"></el-table-column>
      <el-table-column prop="create_time" label="发布时间">
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
    <!--总数超过一页，再展示分页器-->
    <el-pagination
      background
      layout="prev, pager, next"
      :total="total"
      :page-size="queryObj.pageSize"
      :current-page="queryObj.pageNo"
      @current-change="changePage"
    />

    <DialogArticle ref="addArticleRef" :reload="() => getDataList()" />
  </el-card>
</template>

<script>
  import DialogArticle from '@/components/DialogAddArticle.vue'
  import { ElMessage } from 'element-plus'
  import { onMounted, ref, reactive, toRefs } from 'vue'
  import axios from '@/utils/axios'

  export default {
    name: 'wenzhang',
    components: {
      DialogArticle,
    },
    setup() {
      const addArticleRef = ref(null)
      const state = reactive({
        loading: false,
        tableData: [], // 数据列表
        total: 0, // 总条数
        menuOptions: [],
        submenuOptions: [],

        queryObj: {
          menu_pid: '',
          menu_id: '',
          title: '', // 标题
          pageNo: 1, // 当前页
          pageSize: 10, // 分页大小
        },
      })
      onMounted(() => {
        getDataList()
        getOptions(0)
      })
      const getOptions = (num) => {
        axios.get('/api/sys/menus?pid=' + num).then((res) => {
          if (num) {
            state.submenuOptions = res
          } else {
            state.menuOptions = res
          }
        })
      }
      // 获取列表
      const getDataList = () => {
        state.loading = true
        axios
          .get('/api/a/article/list', {
            params: state.queryObj,
          })
          .then((res) => {
            state.tableData = res.list
            state.total = res.total
            state.loading = false
          })
      }
      const handleMenuchange = (val) => {
        if (val) {
          getOptions(val)
          state.ruleForm.menu_id = ''
        } else {
          state.submenuOptions = []
        }
      }
      // 添加商品
      const handleAdd = () => {
        addArticleRef.value.open()
      }
      // 修改商品
      const handleEdit = (id) => {
        addArticleRef.value.open(id)
      }
      const handleDelete = (id) => {
        axios
          .get('/api/a/article/delete', {
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
        addArticleRef,
        handleDelete,
        handleMenuchange,
      }
    },
  }
</script>

<style scoped>
.article-container {
  min-height: 100%;
}
.article-container .header {
  display: flex;
  justify-content: space-between;
}
.el-card.is-always-shadow {
  min-height: 99% !important;
}
</style>