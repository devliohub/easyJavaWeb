<template>
  <el-dialog title="修改密码" v-model="visible" width="400px" append-to-body>
    <el-form
      :model="ruleForm"
      :rules="rules"
      ref="formRef"
      size="small"
      label-width="100px"
    >
      <el-form-item label="旧密码" prop="password">
        <el-input
          type="password"
          placeholder="请输入旧密码"
          v-model="ruleForm.password"
        ></el-input>
      </el-form-item>
      <el-form-item label="新密码" prop="newpassword">
        <el-input
          type="password"
          placeholder="请输入新密码"
          v-model="ruleForm.newpassword"
        ></el-input>
      </el-form-item>
      <el-form-item label="新密码" prop="newpassword2">
        <el-input
          type="password"
          placeholder="再次输入新密码"
          v-model="ruleForm.newpassword2"
        ></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="visible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm()">确 定</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script>
  import { reactive, ref, toRefs } from 'vue'
  import axios from '@/utils/axios'
  import { ElMessage } from 'element-plus'
  import { localRemove } from '@/utils'

  export default {
    name: 'changepwdRef',
    setup() {
      var validatePass = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('密码应为6-16位字母或数字'))
        } else {
          if (state.ruleForm.newpassword2 !== '') {
            formRef.value.validateField('newpassword2')
          }
          callback()
        }
      }
      var validatePass2 = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请再次输入密码'))
        } else if (value !== state.ruleForm.newpassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      }

      const formRef = ref(null)
      const state = reactive({
        visible: false,
        ruleForm: {
          password: '',
          newpassword: '',
          newpassword2: '',
        },
        rules: {
          password: [
            { required: 'true', message: '旧密码不能为空', trigger: ['change'] },
          ],
          newpassword: [{ validator: validatePass, trigger: ['change'] }],
          newpassword2: [{ validator: validatePass2, trigger: ['change'] }],
        },
      })
      // 提交
      const submitForm = () => {
        formRef.value.validate((valid) => {
          if (valid) {
            axios
              .get('/api/a/user/password', {
                params: state.ruleForm,
              })
              .then((res) => {
                console.log(res)
                if (res && res.errno == 200) {
                  ElMessage.error('密码修改成功，请重新登陆')
                  localRemove('token')
                  window.location.reload()
                }
              })
          }
        })
      }

      // 开启弹窗
      const open = () => {
        state.ruleForm = {
          password: '',
          newpassword: '',
          newpassword2: '',
        }
        state.visible = true
      }
      // 关闭弹窗
      const close = () => {
        state.visible = false
      }
      return {
        ...toRefs(state),
        open,
        close,
        submitForm,
        formRef,
      }
    },
  }
</script>

<style scoped>
</style>