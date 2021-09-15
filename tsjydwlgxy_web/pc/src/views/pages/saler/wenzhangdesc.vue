<template>
  <div class="wenzhangdesc">
    <el-dialog
      custom-class="viewDialog"
      :title="viewEntity.fileName"
      :visible.sync="dialogVisible"
      fullscreen
    >
      <iframe
        @load="handleIframeOnload"
        class="iframe_class"
        :src="viewEntity.fileUrl"
        frameborder="0"
      ></iframe>
    </el-dialog>

    <header>
      <div class="title">{{ entity.title }}</div>
      <div class="time">
        {{ dayjs(entity.create_time * 1000).format('YYYY/MM/DD HH:mm') }}
      </div>
    </header>
    <article v-html="entity.content"></article>
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
            :class="{ ishover: item.ishover }"
            class="el-icon-document-copy"
          ></i>
          <div class="midder">
            <span :class="{ ishover: item.ishover }">{{ item.fileName }}</span>
            <span :class="{ ishover: item.ishover }">{{
              item.sizeFormat
            }}</span>
          </div>
          <span
            v-if="item.ishover"
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
          iframe.clientWidth = img.width
          iframe.height = img.height

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
        this.viewEntity = JSON.parse(JSON.stringify(item))
        this.dialogVisible = true
      },
    },
  }
</script>
<style lang="scss">
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
  article {
    // text-indent: 2em;
    font-size: 14px;
    line-height: 1.5;
    padding-bottom: 20px;
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
          width: 120px;
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
          color: #888;
          &:hover {
            font-weight: bold;
            color: #c00900;
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
    min-height: 60vh;
  }
}
</style>