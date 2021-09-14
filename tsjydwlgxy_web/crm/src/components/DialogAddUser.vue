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
        <el-input
          type="password"
          v-model.trim="ruleForm.password"
          autocomplete="off"
        ></el-input>
      </el-form-item>
      <el-form-item label="用户权限" prop="checkList">
        <el-checkbox-group v-model="ruleForm.checkList">
          <el-checkbox
            v-for="item in roleArr"
            :label="item.code"
            :value="item.code"
            :key="item.code"
            >{{ item.name }}</el-checkbox
          >
        </el-checkbox-group>
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
    name: 'DialogAddUser',
    props: {
      reload: Function,
    },
    setup(props) {
      const formRef = ref(null)
      const roleArr = localGet('loRoles')
      const state = reactive({
        type: 'add',
        visible: false,
        ruleForm: {
          name: '',
          account: '',
          password: '',
          rolemenuids: '1,2,3,4,5',
          checkList: [],
        },
        rules: {
          name: [
            { required: 'true', message: '姓名不能为空', trigger: ['change'] },
          ],
          password: [
            { required: 'true', message: '密码不能为空', trigger: ['change'] },
          ],
          account: [
            { required: 'true', message: '账号不能为空', trigger: ['change'] },
          ],
          checkList: [
            { required: 'true', message: '权限不能为空', trigger: ['change'] },
          ],
        },
        id: '',
      })
      // 获取详情
      const getDetail = (id) => {
        axios.get('/api/a/user/info?id=' + `${id}`).then((res) => {
          state.ruleForm = res
          state.ruleForm.checkList = res.rolemenuids.map((el) => el * 1)
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
            account: '',
            password: '',
            rolemenuids: '1,2,3,4,5',
            checkList: [],
          }
        }
      }
      // 关闭弹窗
      const close = () => {
        state.visible = false
      }
      const submitForm = () => {
        console.log(state.ruleForm)
        formRef.value.validate((valid) => {
          if (valid) {
            if (
              hasEmoji(state.ruleForm.name) ||
              hasEmoji(state.ruleForm.account)
            ) {
              return ElMessage.error('禁止输入非法字符')
            }
            if (state.ruleForm.checkList.length == 0) {
              return ElMessage.error('请输入用户权限')
            }

            state.ruleForm.rolemenuids = state.ruleForm.checkList.join(',')

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
                    password: state.ruleForm.password,
                    rolemenuids: state.ruleForm.rolemenuids,
                    account: state.ruleForm.account,
                  },
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
        roleArr,
      }
    },
  }
</script>

<style scoped>
</style>