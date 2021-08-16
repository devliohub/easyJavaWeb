<template>
  <div class="desc">
    <header>
      <div class="title">这场“十四五”问计会 总书记要求处理好五大关系</div>
      <div class="time">2020/10/28</div>
    </header>
    <article>
      这是很长的内容这是很长的内容这是很长的内容这是很长的内容这是很长的内容这是很长的内容。
      这是很长的内容这是很长的内容这是很长的内容这是很长的内容这是很长的内容这是很长的内容。
      这是很长的内容这是很长的内容这是很长的内容这是很长的内容这是很长的内容这是很长的内容。
    </article>
    <div class="fujianList">
      <header>
        <span>附件</span>
      </header>
    </div>
  </div>
</template>
<script>
  export default {
    name: 'desc',
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
        this.isloading = false
      },
    },
  }
</script>
<style lang="scss">
.desc {
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