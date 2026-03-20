<template>
  <el-dialog
    :title="type == 'add' ? '添加轮播图' : '修改轮播图'"
    v-model="visible"
    width="800px"
  >
    <el-form
      :model="ruleForm"
      :rules="rules"
      ref="formRef"
      label-width="100px"
      class="good-form"
    >
      <el-form-item label="图片" prop="img">
        <img style="width: 600px" :src="ruleForm.img" alt="" />
      </el-form-item>
      <el-form-item label="链接地址" prop="url">
        <el-input type="text" v-model="ruleForm.url"></el-input>
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
  import { hasEmoji } from '@/utils'
  import { ElMessage } from 'element-plus'

  export default {
    name: 'DialogAddSwiper',
    props: {
      type: String,
      reload: Function,
    },
    setup(props) {
      const formRef = ref(null)
      const state = reactive({
        visible: false,
        ruleForm: {},
        rules: {},
        id: '',
      })
      // 开启弹窗
      const open = (obj) => {
        state.visible = true
        if (obj) {
          state.ruleForm = JSON.parse(JSON.stringify(obj))
        }
      }
      // 关闭弹窗
      const close = () => {
        state.visible = false
      }
      const submitForm = () => {
        formRef.value.validate((valid) => {
          if (valid) {
            if (hasEmoji(state.ruleForm.link)) {
              return ElMessage.error('不要输入表情包，再输入就打死你个龟孙儿~')
            }
            axios
              .get('/api/a/banner/update', {
                params: state.ruleForm,
              })
              .then(() => {
                ElMessage.success('修改成功')
                state.visible = false
                if (props.reload) props.reload()
              })
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
.avatar-uploader {
  width: 100px;
  height: 100px;
  color: #ddd;
  font-size: 30px;
}
.avatar-uploader-icon {
  display: block;
  width: 100%;
  height: 100%;
  border: 1px solid #e9e9e9;
  padding: 32px 17px;
}
</style>