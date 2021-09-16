<template>
  <el-card class="swiper-container" v-loading="loading">
    <el-upload
      ref="imgsRef"
      action="#"
      :http-request="myUpload"
      :before-upload="beforeAvatarUpload"
      accept="image/jpeg, image/jpg, image/png"
      :file-list="fileList"
      :show-file-list="false"
    >
      <el-button size="small" type="warning">点击上传</el-button>
    </el-upload>
    <div style="font-size: 14px; margin-top: 10px; color: #929292">
      支持jpg、jpeg、png格式，建议尺寸1920*360，大小不超过2M；最多添加10张图片
    </div>

    <ul>
      <li v-for="(item, index) in fileList" :key="index">
        <img :src="item.img" alt="" />
        <div>
          <span style="font-size: 14px"
            >上传时间：{{
              $filters.dateFormater(item.create_time * 1000, 'YYYY-MM-DD HH:mm')
            }}</span
          >
          <span>
            <el-button plain type="success" @click="handleEdit(item)"
              >编 辑</el-button
            >
            <el-popconfirm
              title="确定删除吗？"
              @confirm="handleDelete(item.id)"
              confirm-button-text="是"
              cancel-button-text="否"
              :hide-icon="true"
            >
              <template #reference>
                <el-button plain type="danger">删 除</el-button>
              </template>
            </el-popconfirm>

            <el-button
              plain
              type="primary"
              @click="handleMove(item.id, 'prev')"
              :disabled="index == 0"
              >上 移</el-button
            >
            <el-button
              plain
              type="primary"
              @click="handleMove(item.id, 'next')"
              :disabled="index == fileList.length - 1"
              >下 移</el-button
            >
          </span>
        </div>
      </li>
    </ul>
    <DialogAddSwiper ref="addSwiper" :reload="() => getCarousels()" />
  </el-card>
</template>

<script>
  import { onMounted, reactive, ref, toRefs } from 'vue'
  import { ElMessage } from 'element-plus'
  import DialogAddSwiper from '@/components/DialogAddSwiper.vue'
  import axios from '@/utils/axios'
  export default {
    name: 'Swiper',
    components: {
      DialogAddSwiper,
    },
    setup() {
      const addSwiper = ref(null)
      const state = reactive({
        loading: false,
        fileList: [],
      })
      onMounted(() => {
        getCarousels()
      })
      // 获取轮播图列表
      const getCarousels = () => {
        state.loading = true
        axios.get('/api/a/banner/list').then((res) => {
          state.fileList = res
          state.loading = false
        })
      }
      const beforeAvatarUpload = (file) => {
        const isLt2M = file.size / 1024 / 1024 < 2
        const isLt10P = state.fileList.length < 10

        if (!isLt2M) {
          ElMessage.error('上传大小不能超过 2MB')
        }
        if (!isLt10P) {
          ElMessage.error('总共不能超过10张')
        }
        return isLt10P && isLt2M
      }
      const myUpload = async (content) => {
        let form = new FormData()
        const sufix = content.file.name.split('.')[1] || ''
        if (!['jpg', 'jpeg', 'png'].includes(sufix)) {
          ElMessage.error('请上传 jpg、jpeg、png 格式的图片')
          return false
        }
        form.append('img', content.file)
        state.loading = true
        axios.post('/api/sys/upload', form).then((res) => {
          state.fileList.push({
            url: res,
            uid: content.file.uid,
            name: content.file.name,
          })
          axios
            .get('/api/a/banner/add', {
              params: {
                img: res,
              },
            })
            .then(() => {
              getCarousels()
              ElMessage.success('上传成功')
            })
        })
      }
      // 修改轮播图
      const handleEdit = (item) => {
        addSwiper.value.open(item)
      }
      // 单个删除
      const handleDelete = (id) => {
        axios.get('/api/a/banner/delete?id=' + id).then(() => {
          ElMessage.success('删除成功')
          getCarousels()
        })
      }
      const handleMove = (id, str) => {
        axios
          .get('/api/a/banner/sort', {
            params: {
              id: id,
              sorttype: str,
            },
          })
          .then(() => {
            ElMessage.success('操作成功')
            getCarousels()
          })
      }

      return {
        ...toRefs(state),
        addSwiper,
        handleEdit,
        handleDelete,
        getCarousels,
        myUpload,
        handleMove,
        beforeAvatarUpload,
      }
    },
  }
</script>

<style scoped>
.swiper-container {
  min-height: 100%;
}
.el-card.is-always-shadow {
  min-height: 99% !important;
}
.swiper-container ul {
  padding: 0;
  margin: 20px 0;
}
.swiper-container ul li {
  list-style: none;
  background: #eee;
  border-radius: 15px;
  padding: 20px;
  display: flex;
  display: -webkit-flex;
  margin-bottom: 50px;
}
.swiper-container ul li img {
  width: 200px;
  margin-right: 100px;
}
.swiper-container ul li div {
  text-align: right;
  flex: 1;
  display: flex;
  display: -webkit-flex;
  align-items: center;
  -webkit-align-items: center;
  justify-content: space-between;
  -webkit-justify-content: space-between;
}
</style>