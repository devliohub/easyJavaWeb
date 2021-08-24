<template>
  <el-dialog
    :title="type == 'add' ? '添加课程' : '修改课程'"
    v-model="visible"
    width="450px"
  >
    <el-form
      :model="ruleForm"
      :rules="rules"
      ref="formRef"
      label-width="120px"
      class="good-form"
    >
      <el-form-item label="课程名称：" prop="name">
        <el-input type="text" v-model="ruleForm.name"></el-input>
      </el-form-item>
      <el-form-item label="链接地址：" prop="url">
        <el-input type="text" v-model="ruleForm.url"></el-input>
      </el-form-item>
      <el-form-item label="所属模块：" prop="module_id">
        <el-select
          size="small"
          v-model="ruleForm.module_id"
          placeholder="请选择所属模块"
        >
          <el-option
            v-for="item in moduleOptions"
            :key="item.code"
            :label="item.name"
            :value="item.code"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="课程类型：" prop="type_id">
        <el-select
          size="small"
          v-model="ruleForm.type_id"
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
      </el-form-item>
      <el-form-item label="封面图片：" prop="cover">
        <el-input type="text" v-model="ruleForm.cover"></el-input>
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
  import { reactive, ref, toRefs } from 'vue'
  import axios from '@/utils/axios'
  import { hasEmoji } from '@/utils/index'
  import { ElMessage } from 'element-plus'
  import { localGet } from '@/utils'

  export default {
    name: 'DialogAddKecheng',
    props: {
      reload: Function,
    },
    setup(props) {
      const formRef = ref(null)
      const moduleOptions = localGet('loModules')
      const typeOptions = localGet('loTypes')

      const state = reactive({
        type: 'add',
        visible: false,
        ruleForm: {
          module_id: 1,
          type_id: 1,
          name: '',
          cover: '',
        },
        rules: {
          name: [
            { required: 'true', message: '名称不能为空', trigger: ['change'] },
          ],
        },
        id: '',
      })
      // 获取详情
      const getDetail = (id) => {
        axios.get('/api/a/course/info?id=' + `${id}`).then((res) => {
          state.ruleForm = res
        })
      }
      // 开启弹窗
      const open = (id) => {
        state.visible = true
        if (id) {
          state.type = 'edit'
          state.id = id
          getDetail(id)
        } else {
          state.type = 'add'
          state.ruleForm = { module_id: 1, type_id: 1, name: '', cover: '' }
        }
      }
      // 关闭弹窗
      const close = () => {
        state.visible = false
      }
      const submitForm = () => {
        formRef.value.validate((valid) => {
          if (valid) {
            if (hasEmoji(state.ruleForm.name)) {
              return ElMessage.error('禁止输入非法字符')
            }
            if (state.type == 'add') {
              axios
                .get('/api/a/course/add', {
                  params: state.ruleForm,
                })
                .then(() => {
                  ElMessage.success('添加成功')
                  state.visible = false
                  if (props.reload) props.reload()
                })
            } else {
              axios
                .get('/api/a/course/update', {
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
        submitForm,
        moduleOptions,
        typeOptions,
      }
    },
  }
</script>

<style scoped>
</style>