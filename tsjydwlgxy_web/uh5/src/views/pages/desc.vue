<template>
  <div class="desc">
    <breadcrumb />
    <div v-if="isloading" class="center_loading">
      <van-loading type="spinner" />
    </div>

    <section v-else>
      <header>
        <div class="title">{{ entity.title }}</div>
        <div class="time">
          {{ dateFormater(entity.publish_time * 1000, 'YYYY/MM/DD') }}
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
            @click="downloadFunc(item)"
          >
            <i class="el-icon-document-copy"></i>
            <div class="midder">
              <span>{{ item.fileName }}</span>
              <span>{{ item.sizeFormat }}</span>
            </div>
          </li>
        </ul>
      </div>
    </section>
  </div>
</template>
<script>
  import breadcrumb from '@/components/breadcrumb'
  import { getArticleDesc } from '@/api'
  export default {
    name: 'listDesc',
    components: { breadcrumb },
    data() {
      return {
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
      async getData() {
        this.isloading = true
        let res = await getArticleDesc(this.$route.query.descId)
        if (res && res.errno == 200) {
          this.entity = res.result
        }
        this.isloading = false
      },
      handleMouseSet(item, flag) {
        this.entity.attachmentArr.map((el) => {
          if (item.fileName == el.fileName) {
            console.log(el)
            this.$set(el, 'ishover', flag)
          }
        })
      },
      downloadFunc(item) {
        window.open(item.fileUrl)
      },
    },
  }
</script>
<style lang="scss">
.desc {
  position: relative;
  margin-top: 50px;
  section {
    padding: 15px;
    color: #444;
    header {
      .title {
        font-size: 18px;
        line-height: 1.5;
        overflow-wrap: break-word;
      }
      .time {
        padding: 10px 0 20px 0;
        color: #888;
        font-size: 12px;
      }
    }
    article {
      overflow: auto;
      text-indent: 2em;
      font-size: 14px;
      line-height: 1.5;
      padding-bottom: 20px;
      overflow-wrap: break-word;
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
          font-size: 14px;
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
        }
      }
    }
  }
}
</style>