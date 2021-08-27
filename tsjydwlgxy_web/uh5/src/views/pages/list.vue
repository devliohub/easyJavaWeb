<template>
  <div class="list">
    <breadcrumb />

    <div v-if="isloading" class="center_loading">
      <van-loading type="spinner" />
    </div>
    <ul v-else-if="paList.length > 0">
      <li
        v-for="(item, x) in paList"
        :key="x"
        :class="{ signle_class: x % 2 == 0 }"
        @click="handlegoDesc(item)"
      >
        <div class="title">{{ item.title }}</div>
        <div class="time">
          发布时间：{{
            dateFormater(item.create_time * 1000, 'YYYY-MM-DD HH:mm')
          }}
        </div>
      </li>
    </ul>

    <van-empty v-else description="暂无数据" />
  </div>
</template> 
<script>
  import { getArticle } from '@/api'
  import breadcrumb from '@/components/breadcrumb'
  export default {
    name: 'listPage',
    components: { breadcrumb },
    data() {
      return {
        isloading: false,
        queryObj: {
          menu_pid: this.$route.query.pid,
          menu_id: this.$route.query.id,
          title: '', //（搜索文章关键词）
          pageNo: 1,
          pageSize: 10,
        },
        paList: [],
      }
    },
    mounted() {
      this.getData()
    },
    watch: {
      '$route.fullPath': function (newv, oldv) {
        if (newv != oldv) {
          this.getData()
          this.queryObj.menu_pid = this.$route.query.pid
          this.queryObj.menu_id = this.$route.query.id
        }
      },
    },
    methods: {
      async getData() {
        this.isloading = true
        let res = await getArticle(this.queryObj)
        if (res) {
          this.paList = res.result.list
        }
        this.isloading = false
      },
      handlegoDesc(item) {
        this.$router.push('/desc?id=' + item.id)
      },
    },
  }
</script>
<style lang="scss" scope>
.list {
  position: relative;
  width: 100%;
  margin-top: 50px;
  & > ul {
    display: flex;
    flex-direction: column;
    li {
      padding: 15px;
      background: #fff;
      width: 100%;
      margin-bottom: 10px;
      line-height: 1.2;
      .title {
        font-size: 14px;
        font-weight: bold;
        color: #444;
        margin-bottom: 10px;
      }
      .time {
        font-size: 12px;
        color: #888;
      }
    }
  }
  .center_loading {
    display: block;
    position: absolute;
    margin: 0 auto;
    top: 100px;
    left: 0;
    right: 0;
    bottom: 0;
  }
  .signle_class {
    background: #f8f8f8;
  }
}
</style>