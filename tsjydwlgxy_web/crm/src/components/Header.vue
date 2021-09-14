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
        :width="100"
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
          <el-tag size="small" effect="dark" class="logout" @click="logout"
            >退出登陆</el-tag
          >
          <br />
          <el-tag size="small" class="changePwd" @click="changePwd">
            修改密码
          </el-tag>
        </div>
      </el-popover>
    </div>

    <DialogChangePwd ref="changepwdRef" />
  </div>
</template>

<script>
  import DialogChangePwd from '@/components/DialogChangePwd.vue'
  import { onMounted, ref, reactive, toRefs } from 'vue'
  import { useRouter } from 'vue-router'
  import axios from '@/utils/axios'
  import { localGet, localRemove } from '@/utils'

  export default {
    name: 'Header',
    components: {
      DialogChangePwd,
    },
    setup() {
      const changepwdRef = ref(null)
      const router = useRouter()
      const state = reactive({
        name: 'header',
        userInfo: null,
      })
      onMounted(() => {
        state.userInfo = localGet('token')
      })
      const changePwd = () => {
        changepwdRef.value.open()
      }
      const logout = () => {
        axios.get('/api/a/loginout').then(() => {
          localRemove('token')
          localRemove('loRoles')
          window.location.reload()
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
        changepwdRef,
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
  text-align: center;
  color: #333;
}
.popper-user-box .nickname .logout {
  cursor: pointer;
  margin-bottom: 10px;
}
.popper-user-box .nickname .changePwd {
  cursor: pointer;
}
</style>