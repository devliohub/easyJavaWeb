<template>
  <el-dialog
    :title="type == 'add' ? '添加菜单' : '修改菜单'"
    v-model="visible"
    width="400px"
  >
    <el-form
      :model="ruleForm"
      :rules="rules"
      ref="formRef"
      label-width="80px"
      class="good-form"
    >
      <el-form-item label="名称" prop="name">
        <el-input
          type="text"
          v-model="ruleForm.name"
          :disabled="isDingzhi"
        ></el-input>
      </el-form-item>
      <el-form-item label="类型" prop="type">
        <el-radio v-model="ruleForm.type" :label="1" :disabled="type != 'add'"
          >文章栏目</el-radio
        >
        <el-radio v-model="ruleForm.type" :label="2" :disabled="type != 'add'"
          >外部链接</el-radio
        >
        <el-radio
          v-if="ruleForm.type == 3"
          v-model="ruleForm.type"
          :label="3"
          :disabled="true"
          >定制菜单</el-radio
        >
      </el-form-item>
      <el-form-item label="样式" prop="layout" v-if="ruleForm.type == 1">
        <el-radio v-model="ruleForm.layout" :label="1">标题式</el-radio>
        <el-radio v-model="ruleForm.layout" :label="2">卡片式</el-radio>
      </el-form-item>
      <el-form-item label="链接" prop="url" v-if="ruleForm.type == 2">
        <el-input
          type="text"
          v-model="ruleForm.url"
          :disabled="isDingzhi"
        ></el-input>
      </el-form-item>
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
  import { reactive, ref, toRefs, computed } from 'vue'
  import axios from '@/utils/axios'
  import { hasEmoji } from '@/utils/index'
  import { ElMessage } from 'element-plus'

  export default {
    name: 'DialogAddCaidan',
    props: {
      reload: Function,
    },
    setup(props) {
      const formRef = ref(null)
      const isDingzhi = computed(() => {
        return state.ruleForm.is_course_menu
      })
      const state = reactive({
        type: 'add',
        visible: false,
        ruleForm: {},
        rules: {
          name: [
            { required: 'true', message: '名称不能为空', trigger: ['change'] },
          ],
          type: [{ required: 'true', message: '不能为空', trigger: ['change'] }],
          url: [{ required: 'true', message: '不能为空', trigger: ['change'] }],
          layout: [
            { required: 'true', message: '不能为空', trigger: ['change'] },
          ],
        },
        id: '',
      })
      // 开启弹窗
      const open = (obj, str) => {
        state.visible = true
        state.type = str
        if (str == 'add') {
          state.ruleForm = {
            pid: obj,
          }
        } else {
          state.ruleForm = JSON.parse(JSON.stringify(obj))
          console.log(obj)
        }
      }
      // 关闭弹窗
      const close = () => {
        state.visible = false
      }
      const submitForm = () => {
        formRef.value.validate((valid) => {
          if (valid) {
            if (
              hasEmoji(state.ruleForm.name) ||
              hasEmoji(state.ruleForm.account)
            ) {
              return ElMessage.error('禁止输入非法字符')
            }

            if (state.type == 'add') {
              axios
                .get('/api/a/menu/add', {
                  params: state.ruleForm,
                })
                .then(() => {
                  ElMessage.success('添加成功')
                  state.visible = false
                  if (props.reload) props.reload()
                })
            } else {
              axios
                .get('/api/a/menu/update', {
                  params: state.ruleForm,
                })
                .then(() => {
                  ElMessage.success('修改成功')
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
        submitForm,
        isDingzhi,
      }
    },
  }
</script>

<style scoped>
</style>