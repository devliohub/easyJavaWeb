<template>
  <el-card class="caidan-container">
    <template #header>
      <div class="header">
        <el-button
          type="warning"
          size="small"
          icon="el-icon-plus"
          @click="handleAdd(0)"
          >添加菜单</el-button
        >
      </div>
    </template>
    <el-table
      v-loading="loading"
      ref="multipleTable"
      :data="tableData"
      tooltip-effect="dark"
      style="width: 100%"
      row-key="id"
      border
      default-expand-all
      :tree-props="{ children: 'sub_menus' }"
    >
      <el-table-column prop="name" label="名称" width="500"> </el-table-column>
      <el-table-column label="操作">
        <template #default="scope">
          <div v-if="scope.row.id != 1">
            <a
              v-if="scope.row.type == 1 && scope.row.sub_menus"
              style="cursor: pointer; margin-right: 10px"
              @click="handleAdd(scope.row.id)"
              >添加子菜单</a
            >
            <a
              style="cursor: pointer; margin-right: 10px"
              @click="handleEdit(scope.row)"
              >修改</a
            >
            <a
              style="cursor: pointer; margin-right: 10px"
              @click="handleMove(scope.row.id, 'prev')"
              >上移</a
            >
            <a
              style="cursor: pointer; margin-right: 10px"
              @click="handleMove(scope.row.id, 'next')"
              >下移</a
            >
            <el-popconfirm
              title="删除菜单后，对应栏目的文章不会删除。确定删除吗？"
              @confirm="handleDelete(scope.row.id)"
              confirm-button-text="是"
              cancel-button-text="否"
              :hide-icon="true"
            >
              <template #reference>
                <a style="cursor: pointer">删除</a>
              </template>
            </el-popconfirm>
          </div>
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
      const handleAdd = (id) => {
        addCaidanRef.value.open(id, 'add')
      }
      // 修改商品
      const handleEdit = (obj) => {
        addCaidanRef.value.open(obj, 'edit')
      }
      const handleMove = (id, str) => {
        axios
          .get('/api/a/menu/sort', {
            params: {
              id: id,
              sorttype: str,
            },
          })
          .then(() => {
            ElMessage.success('操作成功')
            getDataList()
          })
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
        handleMove,
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