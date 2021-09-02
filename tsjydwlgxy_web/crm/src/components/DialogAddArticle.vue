<template>
  <el-dialog
    :title="type == 'add' ? '发布文章' : '修改文章'"
    v-model="visible"
    width="800px"
    top="5vh"
  >
    <el-form
      :model="ruleForm"
      :rules="rules"
      ref="formRef"
      label-width="120px"
      class="good-form"
    >
      <el-form-item label="文章标题：" prop="title">
        <el-input type="text" v-model="ruleForm.title"></el-input>
      </el-form-item>
      <el-form-item label="详细内容：">
        <div ref="editor"></div>
      </el-form-item>
      <el-row>
        <el-col :span="12">
          <el-form-item label="所属栏目：" prop="menu_pid">
            <el-select
              size="small"
              v-model="ruleForm.menu_pid"
              placeholder="请选择一级菜单"
              @change="handleMenuchange"
            >
              <el-option
                v-for="item in menuOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="所属栏目：" prop="menu_id">
            <el-select
              size="small"
              v-model="ruleForm.menu_id"
              placeholder="请选择二级菜单"
            >
              <el-option
                v-for="item in submenuOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="是否置顶：" prop="is_top">
            <el-checkbox v-model="ruleForm.is_top"></el-checkbox>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="封面图片：" prop="cover">
            <el-input type="text" v-model="ruleForm.cover"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="visible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script>
  import { reactive, ref, toRefs, watch } from 'vue'
  import axios from '@/utils/axios'
  import { hasEmoji } from '@/utils/index'
  import { ElMessage } from 'element-plus'
  import WangEditor from 'wangeditor'

  export default {
    name: 'DialogAddArticle',
    props: {
      reload: Function,
    },
    setup(props) {
      const formRef = ref(null)
      const editor = ref(null)
      const state = reactive({
        type: 'add',
        visible: false,
        instance: null,
        ruleForm: {},
        menuOptions: [],
        submenuOptions: [],
        rules: {
          title: [{ required: 'true', message: '不能为空', trigger: ['change'] }],
        },
        id: '',
      })

      let instance
      watch(editor, (a) => {
        if (a) {
          instance = new WangEditor(editor.value)
          instance.create()
        }
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
      const handleMenuchange = (val) => {
        if (val) {
          getOptions(val)
          state.ruleForm.menu_id = ''
        } else {
          state.submenuOptions = []
        }
      }
      // 获取详情
      const getDetail = (id) => {
        axios.get('/api/a/article/info?id=' + `${id}`).then((res) => {
          state.ruleForm = res
          instance.txt.html(res.content)
        })
      }
      // 开启弹窗
      const open = (id) => {
        state.visible = true
        getOptions(0)
        if (id) {
          state.type = 'edit'
          state.id = id
          getDetail(id)
        } else {
          state.type = 'add'
          state.ruleForm = {
            is_top: false,
            cover: '',
            menu_id: '',
            menu_pid: '',
          }
        }
      }
      // 关闭弹窗
      const close = () => {
        instance.destroy()
        instance = null
        state.visible = false
      }
      const submitForm = () => {
        formRef.value.validate((valid) => {
          if (valid) {
            if (hasEmoji(state.ruleForm.name)) {
              return ElMessage.error('禁止输入非法字符')
            }

            state.ruleForm.content = instance.txt.html()

            if (state.type == 'add') {
              axios
                .get('/api/a/article/add', {
                  params: state.ruleForm,
                })
                .then(() => {
                  ElMessage.success('添加成功')
                  state.visible = false
                  if (props.reload) props.reload()
                })
            } else {
              axios
                .get('/api/a/article/update', {
                  params: state.ruleForm,
                })
                .then(() => {
                  ElMessage.success('添加成功')
                  state.visible = false
                  if (props.reload) props.reload()
                })
            }
          }
        })
      }
      return {
        ...toRefs(state),
        open,
        close,
        formRef,
        editor,
        submitForm,
        handleMenuchange,
      }
    },
  }
</script>

<style scoped>
</style>