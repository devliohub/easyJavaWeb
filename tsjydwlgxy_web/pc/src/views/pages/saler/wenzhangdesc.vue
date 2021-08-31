<template>
  <div class="wenzhangdesc">
    <header>
      <div class="title">{{ entity.title }}</div>
      <div class="time">
        {{ dayjs(entity.create_time * 1000).format('YYYY/MM/DD HH:mm') }}
      </div>
    </header>
    <article v-html="entity.content"></article>
    <div class="fujianList">
      <header>
        <span>附件</span>
      </header>
    </div>
  </div>
</template>
<script>
  import { getArticleDesc } from '@/api'
  export default {
    name: 'wenzhangdesc',
    data() {
      return {
        isloading: false,
        entity: {},
      }
    },
    computed: {},
    mounted() {
      this.getData()
    },
    methods: {
      async getData() {
        this.isloading = true
        let res = await getArticleDesc(this.$route.query.id)
        if (res && res.errno == 200) {
          this.entity = res.result
        }
        this.isloading = false
      },
    },
  }
</script>
<style lang="scss">
.wenzhangdesc {
  position: relative;
  color: #444;
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
    text-indent: 2em;
    font-size: 14px;
    line-height: 1.5;
    border-bottom: 1px solid #eee;
    padding-bottom: 20px;
  }
  .fujianList {
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
  }
}
</style>