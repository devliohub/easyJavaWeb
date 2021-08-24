<template>
  <el-dialog
    :title="type == 'add' ? '添加用户' : '修改用户'"
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
      <el-form-item label="姓名" prop="name">
        <el-input type="text" v-model="ruleForm.name"></el-input>
      </el-form-item>
      <el-form-item label="账号" prop="account">
        <el-input type="text" v-model="ruleForm.account"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input type="text" v-model="ruleForm.password"></el-input>
      </el-form-item>
      <!-- <el-form-item label="排序值" prop="rolemenuids">
        <el-input type="number" v-model="ruleForm.rolemenuids"></el-input>
      </el-form-item> -->
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
  export default {
    name: 'DialogAddUser',
    props: {
      reload: Function,
    },
    setup(props) {
      const formRef = ref(null)
      const state = reactive({
        type: 'add',
        visible: false,
        ruleForm: {
          name: '',
          account: '',
          password: '',
          rolemenuids: '1,2,3,4,5',
        },
        rules: {
          name: [
            { required: 'true', message: '名称不能为空', trigger: ['change'] },
          ],
          password: [
            { required: 'true', message: '编号不能为空', trigger: ['change'] },
          ],
          account: [
            { required: 'true', message: '排序不能为空', trigger: ['change'] },
          ],
        },
        id: '',
      })
      // 获取详情
      const getDetail = (id) => {
        axios.get('/api/a/user/info?id=' + `${id}`).then((res) => {
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
          state.ruleForm = {
            name: '',
            password: '',
            account: '',
            rolemenuids: '1,2,3,4,5',
          }
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
                .get('/api/a/user/add', {
                  params: state.ruleForm,
                })
                .then(() => {
                  ElMessage.success('添加成功')
                  state.visible = false
                  if (props.reload) props.reload()
                })
            } else {
              axios
                .get('/api/a/user/update', {
                  params: {
                    id: state.ruleForm.id,
                    name: state.ruleForm.name,
                    password: state.ruleForm.name,
                    rolemenuids: '1,2,3,4,5',
                    account: state.ruleForm.account,
                  },
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
      }
    },
  }
</script>

<style scoped>
</style>