<template>
  <div class="wenzhangdesc">
    <el-dialog
      custom-class="viewDialog"
      :title="viewEntity.fileName"
      :visible.sync="dialogVisible"
      fullscreen
    >
      <iframe
        v-if="isViewType"
        @load="handleIframeOnload"
        class="iframe_class"
        :src="viewEntity.fileUrl"
        frameborder="0"
      ></iframe>
      <div class="iframe_class" v-else>
        <div class="cantView_class">
          <span>{{ viewEntity.fileName }}</span>
          该附件不支持在线预览，请 <a :href="viewEntity.fileUrl">下载</a> 查看
        </div>
      </div>
    </el-dialog>

    <header>
      <div class="title">{{ entity.title }}</div>
      <div class="time">
        {{ dayjs(entity.publish_time * 1000).format('YYYY/MM/DD HH:mm') }}
      </div>
    </header>
    <article class="w-e-text" v-html="entity.content"></article>
    <div class="fujianList" v-if="entity.attachmentArr.length > 0">
      <header>
        <span>附件</span>
      </header>
      <ul>
        <li
          v-for="(item, index) in entity.attachmentArr"
          :key="index"
          @mouseenter="handleMouseSet(item, true)"
          @mouseleave="handleMouseSet(item, false)"
          @click="viewFunc(item)"
        >
          <i
            class="iconfont icon-xiazai1"
            :class="{ ishover: item.ishover }"
          ></i>

          <div class="midder">
            <span :class="{ ishover: item.ishover }">{{ item.fileName }}</span>
            <span :class="{ ishover: item.ishover }">{{
              item.sizeFormat
            }}</span>
          </div>
          <span
            v-if="item.ishover && entity.is_attachment_down"
            class="downclass"
            @click="downloadFunc(item)"
            >下 载</span
          >
        </li>
      </ul>
    </div>
  </div>
</template>
<script>
  import { getArticleDesc } from '@/api'
  export default {
    name: 'wenzhangdesc',
    data() {
      return {
        dialogVisible: false,
        viewEntity: {},
        isViewType: true,

        isloading: false,
        entity: {
          attachmentArr: [],
        },
      }
    },
    computed: {},
    mounted() {
      this.getData()
    },
    methods: {
      handleIframeOnload() {
        let iframe = document.querySelectorAll('.iframe_class')[0]
        let idoc = iframe.contentWindow.document
        let img = idoc.querySelectorAll('img')[0]
        if (img) {
          img.width = iframe.clientWidth
          let top = (iframe.clientHeight - img.height) / 2
          img.style.marginTop = top + 'px'
          // if (img.width >= img.height) {
          //   img.width = iframe.clientWidth
          //   let top = (iframe.clientHeight - img.height) / 2
          //   img.style.marginTop = top + 'px'
          // } else {
          //   img.height = iframe.clientHeight
          //   let left = (iframe.clientWidth - img.width) / 2
          //   img.style.marginLeft = left + 'px'
          // }
        }
      },
      async getData() {
        this.isloading = true
        let res = await getArticleDesc(this.$route.query.descId)
        if (res && res.errno == 200) {
          this.entity = res.result
          this.entity.attachmentArr.map((el) => {
            this.$set(el, 'ishover', false)
          })
        }
        this.isloading = false
      },
      handleMouseSet(item, flag) {
        this.entity.attachmentArr.map((el) => {
          if (item.fileName == el.fileName) {
            this.$set(el, 'ishover', flag)
          }
        })
      },
      downloadFunc(item) {
        window.open(item.fileUrl)
      },
      viewFunc(item) {
        // wps预览模式区分
        const sufix = item.fileName.split('.')[1] || ''
        if (['doc', 'docx', 'ppt', 'pptx', 'xls', 'xlsx'].includes(sufix)) {
          this.viewEntity = {
            ...JSON.parse(JSON.stringify(item)),
            fileUrl:
              `https://view.officeapps.live.com/op/view.aspx?src=` + item.fileUrl,
          }
        } else if (['zip', 'xmind'].includes(sufix)) {
          this.isViewType = false
          this.viewEntity = JSON.parse(JSON.stringify(item))
        } else {
          this.isViewType = true
          this.viewEntity = JSON.parse(JSON.stringify(item))
        }
        this.dialogVisible = true
      },
    },
  }
</script>

<style lang="scss" scoped>
.wenzhangdesc {
  position: relative;
  color: #444;
  overflow-wrap: break-word;
  .ishover {
    color: #c00900 !important;
  }
  header {
    .title {
      font-size: 20px;
      line-height: 1.5;
      font-weight: bold;
    }
    .time {
      padding: 10px 0 20px 0;
      color: #888;
      font-size: 12px;
    }
  }
  .fujianList {
    border-top: 1px solid #eee;
    padding: 20px 0;
    & > header {
      padding: 10px 0;
      span {
        font-size: 16px;
        padding: 0 8px;
        font-weight: bold;
        border-left: 3px solid #660000;
      }
    }
    & > ul {
      overflow: hidden;
      li {
        cursor: pointer;
        padding: 10px 0;
        display: flex;
        align-content: center;
        &:hover {
          background: #eee;
        }
        i {
          margin-right: 10px;
          font-size: 32px;
          color: #929292;
        }
        & > .midder {
          width: 400px;
          display: flex;
          flex-direction: column;
          span {
            &:first-child {
              margin-bottom: 5px;
              color: #444;
            }
            &:last-child {
              color: #929292;
            }
          }
        }
        & > .downclass {
          cursor: pointer;
          line-height: 32px;
          color: #c00900;
          &:hover {
            font-weight: bold;
          }
          &:click {
            color: #660000;
          }
        }
      }
    }
  }

  .iframe_class {
    width: 100%;
    position: relative;
    min-height: 85vh;
    .cantView_class {
      position: absolute;
      top: 50%;
      left: 50%;
      margin-top: -260px;
      margin-left: -400px;
      background: #fff;
      width: 800px;
      height: 520px;
      line-height: 520px;
      text-align: center;
      a {
        color: #409eff;
        &:hover {
          color: #66b1ff;
        }
      }
      & > span {
        font-size: 18px;
        height: 50px;
        line-height: 50px;
        color: #444;
        display: block;
        width: 100%;
      }
    }
  }
}
</style>

<style lang="scss">
article {
  font-size: 14px;
  line-height: 1.6;
  padding-bottom: 20px;
  a {
    color: #409eff !important;
  }
  ul {
    list-style-type: disc;
    list-style-position: inside;
  }
  ol {
    list-style-type: decimal;
    list-style-position: inside;
  }
  blockquote {
    display: block;
    border-left: 8px solid #d0e5f2;
    padding: 5px 10px;
    margin: 10px 0;
    background-color: #f1f1f1;
  }
}
</style>
<style>
.w-e-text-container {
  position: relative;
  height: 100%;
}
.w-e-text-container .w-e-progress {
  position: absolute;
  background-color: #1e88e5;
  top: 0;
  left: 0;
  height: 1px;
}
.w-e-text-container .placeholder {
  color: #D4D4D4;
  position: absolute;
  font-size: 11pt;
  line-height: 22px;
  left: 10px;
  top: 10px;
  -webkit-user-select: none;
     -moz-user-select: none;
      -ms-user-select: none;
          user-select: none;
  z-index: -1;
}
.w-e-text {
  padding: 0 10px;
  overflow-y: auto;
}
.w-e-text p,
.w-e-text h1,
.w-e-text h2,
.w-e-text h3,
.w-e-text h4,
.w-e-text h5,
.w-e-text table,
.w-e-text pre {
  margin: 10px 0;
  line-height: 1.5;
}
.w-e-text ul,
.w-e-text ol {
  margin: 10px 0 10px 20px;
}
.w-e-text blockquote {
  display: block;
  border-left: 8px solid #d0e5f2;
  padding: 5px 10px;
  margin: 10px 0;
  line-height: 1.4;
  font-size: 100%;
  background-color: #f1f1f1;
}
.w-e-text code {
  display: inline-block;
  background-color: #f1f1f1;
  border-radius: 3px;
  padding: 3px 5px;
  margin: 0 3px;
}
.w-e-text pre code {
  display: block;
}
.w-e-text table {
  border-top: 1px solid #ccc;
  border-left: 1px solid #ccc;
}
.w-e-text table td,
.w-e-text table th {
  border-bottom: 1px solid #ccc;
  border-right: 1px solid #ccc;
  padding: 3px 5px;
  min-height: 30px;
  height: 30px;
}
.w-e-text table th {
  border-bottom: 2px solid #ccc;
  text-align: center;
  background-color: #f1f1f1;
}
.w-e-text:focus {
  outline: none;
}
.w-e-text img {
  cursor: pointer;
}
.w-e-text img:hover {
  box-shadow: 0 0 5px #333;
}
.w-e-text .w-e-todo {
  margin: 0 0 0 20px;
}
.w-e-text .w-e-todo li {
  list-style: none;
  font-size: 1em;
}
.w-e-text .w-e-todo li span:nth-child(1) {
  position: relative;
  left: -18px;
}
.w-e-text .w-e-todo li span:nth-child(1) input {
  position: absolute;
  margin-right: 3px;
}
.w-e-text .w-e-todo li span:nth-child(1) input[type=checkbox] {
  top: 50%;
  margin-top: -6px;
}
.w-e-tooltip {
  position: absolute;
  display: flex;
  color: #f1f1f1;
  background-color: rgba(0, 0, 0, 0.75);
  box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.15);
  border-radius: 4px;
  padding: 4px 5px 6px;
  justify-content: center;
  align-items: center;
}
.w-e-tooltip-up::after {
  content: "";
  position: absolute;
  top: 100%;
  left: 50%;
  margin-left: -5px;
  border: 5px solid rgba(0, 0, 0, 0);
  border-top-color: rgba(0, 0, 0, 0.73);
}
.w-e-tooltip-down::after {
  content: "";
  position: absolute;
  bottom: 100%;
  left: 50%;
  margin-left: -5px;
  border: 5px solid rgba(0, 0, 0, 0);
  border-bottom-color: rgba(0, 0, 0, 0.73);
}
.w-e-tooltip-item-wrapper {
  cursor: pointer;
  font-size: 14px;
  margin: 0 5px;
}
.w-e-tooltip-item-wrapper:hover {
  color: #ccc;
  text-decoration: underline;
}
strong {
  font-weight: bold !important;
}
</style>