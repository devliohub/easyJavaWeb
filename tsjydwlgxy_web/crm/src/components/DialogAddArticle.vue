<template>
  <el-dialog
    :title="type == 'add' ? '发布文章' : '修改文章'"
    v-model="visible"
    width="900px"
    top="5vh"
    custom-class="wenzahngClass"
    :show-close="false"
    :close-on-click-modal="false"
  >
    <el-dialog
      :title="canView ? '预 览' : '下 载'"
      v-model="dialogShow"
      append-to-body
      width="1000px"
      top="5vh"
      :before-close="() => (dialogShow = false)"
    >
      <iframe
        v-if="canView"
        style="width: 960px; height: 70vh"
        :src="viewUrl"
        frameborder="0"
      ></iframe>
      <div v-else>
        暂不支持预览，点击
        <el-link target="_blank" type="primary" :href="viewUrl">下 载</el-link>
      </div>
    </el-dialog>

    <el-form
      v-loading="isloading"
      :model="ruleForm"
      :rules="rules"
      ref="formRef"
      label-width="120px"
      class="good-form"
    >
      <el-form-item label="文章标题：" prop="title">
        <el-input type="text" v-model="ruleForm.title"></el-input>
      </el-form-item>
      <el-form-item style="margin-top: 20px" label="详细内容：">
        <div ref="editor"></div>
      </el-form-item>
      <el-row>
        <el-col :span="24">
          <el-form-item label="附 件：" prop="attachment">
            <el-upload
              class="imgsRef"
              ref="imgsRef"
              action="#"
              :http-request="myUpload"
              :file-list="ruleForm.attachmentArr"
              :before-upload="beforeAvatarUpload"
              :show-file-list="false"
              :limit="5"
              :on-exceed="onExceed"
            >
              <el-button size="small" type="warning" v-loading="isloading"
                >点击上传</el-button
              >
              <span style="font-size: 14px; margin-left: 20px; color: #929292"
                >最多上传5个附件，单个文件大小不超过500M</span
              >
            </el-upload>
            <ul class="attach_ul">
              <li
                v-for="(item, index) in ruleForm.attachmentArr"
                :key="index"
                @click="viewIframe(item)"
                @mouseenter="handleMouseSet(item, true, index)"
                @mouseleave="handleMouseSet(item, false, index)"
              >
                <!-- <img :src="item.fileUrl" alt="" /> -->
                <span
                  >&nbsp;&nbsp;&nbsp;<b>{{ index + 1 }}. </b
                  >{{ item.fileName }}</span
                >

                <i
                  v-if="item.ishover"
                  @click.stop="handleDelete(item, index)"
                  class="el-icon-circle-close"
                ></i>
              </li>
            </ul>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="所属栏目：" prop="menu_pid">
            <el-select
              size="small"
              v-model="ruleForm.menu_pid"
              placeholder="请选择一级菜单"
              @change="handleMenuchange"
            >
              <el-option
                v-for="item in menuOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
            &nbsp; &nbsp; &nbsp;
            <el-select
              size="small"
              v-model="ruleForm.menu_id"
              placeholder="请选择二级菜单"
            >
              <el-option
                v-for="item in submenuOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="是否置顶：" prop="is_top">
            <el-checkbox v-model="ruleForm.is_top"></el-checkbox>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <!-- <el-form-item label="" prop="cover">
            <el-input type="text" v-model="ruleForm.cover"></el-input>
          </el-form-item> -->
          <el-form-item label="封面图片：" prop="attachment">
            <el-upload
              class="imgsRef"
              ref="imgsRef"
              action="#"
              :before-upload="beforeAvatarUpload2"
              :http-request="myUpload2"
              :file-list="fileListCover"
              :limit="1"
              :on-exceed="onExceed"
              :on-remove="handleRemove"
              list-type="picture"
              accept="image/jpeg, image/jpg, image/png"
            >
              <el-button size="small" type="warning">点击上传</el-button>
              <span style="font-size: 14px; margin-left: 20px; color: #929292"
                >支持jpg、jpeg、png格式，大小不超过2M</span
              >
            </el-upload>
            <!-- <ul class="attach_ul">
              <li v-for="(item, index) in fileListCover" :key="index">
                <img :src="item.url" alt="" />
                <span>{{ item.url }}</span>
              </li>
            </ul> -->
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发布时间：" prop="publish_time">
            <el-date-picker
              v-model="ruleForm.publish_time"
              type="datetime"
              placeholder="选择日期时间"
            >
            </el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <span class="dialog-footer" v-loading="isloading">
        <el-button @click="visible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script>
  import { reactive, ref, toRefs, watch } from 'vue'
  import axios from '@/utils/axios'
  import { hasEmoji } from '@/utils/index'
  import { ElMessage } from 'element-plus'
  import WangEditor from 'wangeditor'
  import dayjs from 'dayjs'

  export default {
    name: 'DialogAddArticle',
    props: {
      reload: Function,
    },
    setup(props) {
      const formRef = ref(null)
      const editor = ref(null)
      const state = reactive({
        type: 'add',
        visible: false,
        instance: null,
        isloading: false,

        dialogShow: false, // 附件预览
        canView: false, // 是否支持预览

        viewUrl: '',
        menuOptions: [],
        submenuOptions: [],

        ruleForm: {
          attachmentArr: [],
        },
        fileListCover: [],
        rules: {
          title: [{ required: 'true', message: '不能为空', trigger: ['change'] }],
          menu_pid: [
            { required: 'true', message: '不能为空', trigger: ['change'] },
          ],
          cover: [{ required: 'true', message: '不能为空', trigger: ['change'] }],
          publish_time: [
            { required: 'true', message: '不能为空', trigger: ['change'] },
          ],
        },
        id: '',
      })

      let instance
      watch(editor, (a) => {
        if (a) {
          instance = new WangEditor(editor.value)

          instance.config.fontSizes = {
            'x-small': { name: '12px', value: '1' },
            small: { name: '14px', value: '2' },
            normal: { name: '16px', value: '3' },
            large: { name: '18px', value: '4' },
            'x-large': { name: '24px', value: '5' },
            'xx-large': { name: '32px', value: '6' },
            'xxx-large': { name: '48px', value: '7' },
          }
          instance.config.showLinkImg = false // 隐藏外链上传
          instance.config.customUploadImg = function (content, insert) {
            let form = new FormData()
            form.append('img', content[0])
            axios.post('/api/sys/upload', form).then((res) => {
              insert(res)
            })
          }

          instance.create()
        }
      })
      const getOptions = (num) => {
        axios
          .get('/api/sys/menus?is_article_type=true&pid=' + num)
          .then((res) => {
            if (num) {
              state.submenuOptions = res
              state.submenuOptions.unshift({
                id: 0,
                name: '--',
              })
            } else {
              state.menuOptions = res
            }
          })
      }
      const handleMenuchange = (val) => {
        if (val) {
          getOptions(val)
          state.ruleForm.menu_id = ''
        } else {
          state.submenuOptions = []
        }
      }
      const handleMouseSet = (item, flag, index) => {
        state.ruleForm.attachmentArr.map((el, _i) => {
          if (index == _i) {
            el['ishover'] = flag
          }
        })
      }
      const handleDelete = (item, index) => {
        state.ruleForm.attachmentArr.splice(index, 1)
      }
      // 附件预览
      const viewIframe = (item) => {
        state.viewUrl = ''
        const sufix = item.fileName.split('.')[1] || ''
        if (['doc', 'docx', 'ppt', 'pptx'].includes(sufix)) {
          ElMessage.error('该类型暂不支持预览')
          state.canView = false
        } else {
          state.canView = true
        }
        state.viewUrl = item.fileUrl
        state.dialogShow = true
      }
      // 获取详情
      const getDetail = (id) => {
        axios.get('/api/a/article/info?id=' + `${id}`).then((res) => {
          state.ruleForm = {
            ...res,
            publish_time: res.publish_time * 1000,
          }
          state.ruleForm.cover.split(',').map((el) => {
            if (el) {
              state.fileListCover.push({
                url: el,
                name: el,
              })
            }
          })
          instance.txt.html(res.content)

          if (state.ruleForm.menu_pid) {
            getOptions(state.ruleForm.menu_pid)
          }
        })
      }
      const onExceed = () => {
        return ElMessage.error('超出个数限制')
      }
      const handleRemove = (file, fileList) => {
        state.fileListCover = []
      }
      const beforeAvatarUpload = (file) => {
        const isLt2M = file.size / 1024 / 1024 < 500
        if (!isLt2M) {
          ElMessage.error('上传大小不能超过 500MB')
        }
        return isLt2M
      }
      // 附件上传
      const myUpload = async (content) => {
        let form = new FormData()
        form.append('file', content.file)
        state.isloading = true
        let res = await axios.post('/api/sys/upload', form)
        if (res) {
          state.ruleForm.attachmentArr.push({
            fileUrl: res,
            fileName: content.file.name,
            size: content.file.size,
            ishover: false,
          })
        }
        state.isloading = false
        return ElMessage.success('上传成功')
      }
      const beforeAvatarUpload2 = (file) => {
        const isLt2M = file.size / 1024 / 1024 < 2
        if (!isLt2M) {
          ElMessage.error('上传大小不能超过 2MB')
        }
        return isLt2M
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
        axios.post('/api/sys/upload', form).then((res) => {
          state.fileListCover.push({
            url: res,
            uid: content.file.uid,
            name: content.file.name,
          })
          return ElMessage.success('上传成功')
        })
      }
      // 开启弹窗
      const open = (id) => {
        state.visible = true
        getOptions(0)
        state.fileListCover = []

        if (instance) instance.txt.html('<p><br></p>') // 清空上一次内容

        if (id) {
          state.type = 'edit'
          state.id = id
          getDetail(id)
        } else {
          state.type = 'add'
          state.ruleForm = {
            is_top: false,
            cover: '',
            menu_id: '',
            menu_pid: '',
            publish_time: dayjs().valueOf(),
            attachmentArr: [],
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
            if (hasEmoji(state.ruleForm.name)) {
              return ElMessage.error('禁止输入非法字符')
            }

            state.ruleForm.content = instance.txt.html()

            // cover转换
            let arr_file = []
            if (state.fileListCover) {
              state.fileListCover.map((el) => {
                arr_file.push(el.url)
              })
              state.ruleForm.cover = arr_file.join(',')
            }
            // 附件json化
            state.ruleForm.attachment = JSON.stringify(
              state.ruleForm.attachmentArr
            )

            if (state.type == 'add') {
              axios
                .get('/api/a/article/add', {
                  params: {
                    ...state.ruleForm,
                    publish_time: state.ruleForm.publish_time / 1000,
                  },
                })
                .then(() => {
                  ElMessage.success('添加成功')
                  state.visible = false
                  if (props.reload) props.reload()
                })
            } else {
              axios
                .get('/api/a/article/update', {
                  params: {
                    ...state.ruleForm,
                    publish_time: state.ruleForm.publish_time / 1000,
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
        beforeAvatarUpload,
        beforeAvatarUpload2,
        handleRemove,
        myUpload,
        myUpload2,
        formRef,
        editor,
        submitForm,
        handleMenuchange,
        onExceed,
        viewIframe,
        handleMouseSet,
        handleDelete,
      }
    },
  }
</script>

<style scoped>
.attach_ul {
  padding: 0;
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
.attach_ul li i {
  font-size: 18px;
  margin-right: 20px;
  font-weight: 400;
  color: #de3b32;
  cursor: pointer;
}
.attach_ul li i:hover {
  color: #c00900;
}
.wenzahngClass .el-form-item {
  margin-bottom: 10px;
}

font[size='1'] {
  font-size: 12px;
}
font[size='2'] {
  font-size: 14px;
}
</style>