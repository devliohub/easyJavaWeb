<template>
  <el-dialog
    :title="type == 'add' ? '添加课程' : '修改课程'"
    v-model="visible"
    width="550px"
  >
    <el-form
      :model="ruleForm"
      :rules="rules"
      ref="formRef"
      label-width="120px"
      class="good-form"
      v-loading="isloading"
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
      <el-form-item label="封面图片：" prop="attachment">
        <el-upload
          class="imgsRef"
          ref="imgsRef"
          action="#"
          :http-request="myUpload2"
          :file-list="fileListCover"
          :show-file-list="false"
          :limit="1"
          :on-exceed="onExceed"
          accept="image/jpeg, image/jpg, image/png"
        >
          <el-button size="small" type="warning">点击上传</el-button>
        </el-upload>
        <ul class="attach_ul">
          <li v-for="(item, index) in fileListCover" :key="index">
            <img :src="item.url" alt="" />
            <span>{{ item.url }}</span>
          </li>
        </ul>
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
        isloading: false,

        fileListCover: [],
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
      const onExceed = () => {
        return ElMessage.error('只能上传一张封面')
      }
      // 获取详情
      const getDetail = (id) => {
        state.isloading = true
        axios.get('/api/a/course/info?id=' + `${id}`).then((res) => {
          state.ruleForm = res

          state.fileListCover = []
          state.ruleForm.cover.split(',').map((el) => {
            if (el) {
              state.fileListCover.push({
                url: el,
                name: el,
              })
            }
          })
          state.isloading = false
        })
      }
      // 图片上传
      const myUpload2 = async (content) => {
        let form = new FormData()
        const sufix = content.file.name.split('.')[1] || ''
        if (!['jpg', 'jpeg', 'png'].includes(sufix)) {
          ElMessage.error('请上传 jpg、jpeg、png 格式的图片')
          return false
        }
        form.append('img', content.file)
        state.isloading = true
        axios.post('/api/sys/upload', form).then((res) => {
          state.fileListCover.push({
            url: res,
            uid: content.file.uid,
            name: content.file.name,
          })
          state.isloading = false
          return ElMessage.success('上传成功')
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
          state.fileListCover = []
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

            // cover转换
            let arr_file = []
            if (state.fileListCover) {
              state.fileListCover.map((el) => {
                arr_file.push(el.url)
              })
              state.ruleForm.cover = arr_file.join(',')
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
        myUpload2,
        onExceed,
      }
    },
  }
</script>

<style scoped>
.attach_ul {
  padding: 5px 0;
}
.attach_ul li {
  border: 1px solid #eee;
  display: flex;
  align-items: center;
  justify-content: space-between;
  cursor: pointer;
  margin-bottom: 10px;
}
.attach_ul li img {
  width: 75px;
  height: 75px;
  margin-right: 25px;
}
.attach_ul li span {
  flex: 1;
}
</style>
