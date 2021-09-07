<template>
  <div class="header">
    <div class="left">
      <span @click="$router.push('/')" style="font-size: 20px; cursor: pointer"
        >东莞理工学院通识教育中心后台管理系统
      </span>
    </div>
    <div class="right">
      <el-popover
        placement="bottom"
        :width="200"
        trigger="click"
        popper-class="popper-user-box"
      >
        <template #reference>
          <div class="author">
            <i class="icon el-icon-s-custom" />
            {{ (userInfo && userInfo.account) || '' }}
            <i class="el-icon-caret-bottom" />
          </div>
        </template>
        <div class="nickname">
          <p>登录名：{{ (userInfo && userInfo.account) || '' }}</p>
          <el-tag size="small" class="changePwd" @click="changePwd">
            修改密码
          </el-tag>
          <el-tag size="small" effect="dark" class="logout" @click="logout"
            >退出</el-tag
          >
          <template v-if="showPwd">
            <el-form
              style="margin-top: 20px"
              :model="ruleForm"
              ref="formRef"
              class="good-form"
              size="mini"
              label-width="75px"
            >
              <el-form-item label="密码" prop="password">
                <el-input type="text" v-model="ruleForm.password"></el-input>
              </el-form-item>
              <el-form-item label="新密码" prop="newpassword">
                <el-input type="text" v-model="ruleForm.newpassword"></el-input>
              </el-form-item>
              <el-button type="primary" size="mini" @click="submitForm"
                >确 定</el-button
              >
              <el-button size="mini" @click="showPwd = false">取 消</el-button>
            </el-form>
          </template>
        </div>
      </el-popover>
    </div>
  </div>
</template>

<script>
  import { onMounted, reactive, toRefs } from 'vue'
  import { useRouter } from 'vue-router'
  import axios from '@/utils/axios'
  import { localGet, localRemove } from '@/utils'

  export default {
    name: 'Header',
    setup() {
      const router = useRouter()
      const state = reactive({
        name: 'swiper',
        userInfo: null,
        showPwd: false,
        ruleForm: {},
      })
      onMounted(() => {
        state.userInfo = localGet('token')
      })
      const logout = () => {
        axios.get('/api/a/loginout').then(() => {
          localRemove('token')
          localRemove('loRoles')
          window.location.reload()
        })
      }
      const changePwd = () => {
        state.showPwd = true
      }
      const submitForm = () => {
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
            console.log(res)
          })
      }
      const back = () => {
        router.back()
      }
      return {
        ...toRefs(state),
        logout,
        changePwd,
        back,
        submitForm,
      }
    },
  }
</script>

<style scoped>
.header {
  height: 50px;
  border-bottom: 1px solid #e9e9e9;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 15px;
  background: #f2f2f2;
}
.el-icon-back {
  border: 1px solid #e9e9e9;
  padding: 4px;
  border-radius: 50px;
  margin-right: 10px;
}
.right > div > .icon {
  font-size: 18px;
  margin-right: 6px;
}
.author {
  margin-left: 10px;
  cursor: pointer;
}
</style>
<style>
.popper-user-box {
  background: url('https://s.yezgea02.com/lingling-h5/static/account-banner-bg.png')
    50% 50% no-repeat !important;
  background-size: cover !important;
  border-radius: 0 !important;
}
.popper-user-box .nickname {
  position: relative;
  color: #ffffff;
}
.popper-user-box .nickname .logout {
  position: absolute;
  right: 0;
  top: 0;
  cursor: pointer;
}
.popper-user-box .nickname .changePwd {
  cursor: pointer;
}
</style>