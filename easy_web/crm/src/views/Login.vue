<template>
  <div class="login-body">
    <div class="login-container">
      <div class="head">
        <div class="name">
          <div class="title">
            东莞理工学院通识教育中心 <br />
            后台管理系统
          </div>
        </div>
      </div>
      <el-form
        label-position="top"
        :rules="rules"
        :model="ruleForm"
        ref="loginForm"
        class="login-form"
      >
        <el-form-item label="账号" prop="username">
          <el-input
            type="text"
            v-model.trim="ruleForm.username"
            autocomplete="off"
            prefix-icon="el-icon-user"
          ></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input
            type="password"
            v-model.trim="ruleForm.password"
            autocomplete="off"
            prefix-icon="el-icon-lock"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button style="width: 100%" type="primary" @click="submitForm"
            >立即登录</el-button
          >
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
  import axios from '@/utils/axios'
  import { reactive, ref, toRefs, onMounted, onUnmounted } from 'vue'
  import { localSet } from '@/utils'
  import { ElMessage } from 'element-plus'
  import { useRouter } from 'vue-router'

  export default {
    name: 'Login',
    setup() {
      const router = useRouter()
      const loginForm = ref(null)
      const state = reactive({
        ruleForm: {
          username: '',
          password: '',
        },
        rules: {
          username: [
            { required: 'true', message: '账户不能为空', trigger: 'blur' },
          ],
          password: [
            { required: 'true', message: '密码不能为空', trigger: 'blur' },
          ],
        },
      })
      const submitForm = async () => {
        loginForm.value.validate((valid) => {
          if (valid) {
            axios
              .get('/api/a/login', {
                params: {
                  account: state.ruleForm.username || '',
                  password: state.ruleForm.password,
                },
              })
              .then((res) => {
                localSet('token', res)
                let arr = [
                  {
                    name: '文章管理',
                    url: '/article',
                  },
                  {
                    name: '轮播图管理',
                    url: '/swiper',
                  },
                  {
                    name: '菜单管理',
                    url: '/caidan',
                  },
                  {
                    name: '课程管理',
                    url: '/kecheng',
                  },
                  {
                    name: '用户管理',
                    url: '/account',
                  },
                ]
                if (res.rolemenuids[0]) {
                  router.push(arr[res.rolemenuids[0] * 1 - 1].url)
                }
              })
          } else {
            console.log('error submit!!')
            return false
          }
        })
      }
      const resetForm = () => {
        loginForm.value.resetFields()
      }
      const keyDown = (e) => {
        if (e.keyCode == 13 || e.keyCode == 13) {
          submitForm()
        }
      }
      onMounted(() => {
        window.addEventListener('keydown', keyDown)
      })
      onUnmounted(() => {
        window.removeEventListener('keydown', keyDown, false)
      })
      return {
        ...toRefs(state),
        loginForm,
        submitForm,
        resetForm,
      }
    },
  }
</script>

<style scoped>
.login-body {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  background-color: #2d3a4b;
}
.login-container {
  width: 420px;
  height: 440px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0px 21px 41px 0px rgba(0, 0, 0, 0.2);
}
.head {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 40px 0 20px 0;
}
.head .title {
  text-align: center;
  font-size: 28px;
  color: #1890ff;
  font-weight: bold;
}
.login-form {
  width: 70%;
  margin: 0 auto;
}
</style>
<style>
.el-form--label-top .el-form-item__label {
  padding: 0;
}
.login-form .el-form-item {
  margin-bottom: 20px;
}
</style>