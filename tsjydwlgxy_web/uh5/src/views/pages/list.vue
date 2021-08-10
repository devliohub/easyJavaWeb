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
        @click="
          () => {
            $router.push('/desc')
          }
        "
      >
        <div class="title">{{ item.title }}</div>
        <div class="time">发布时间：2010-02-04</div>
      </li>
    </ul>

    <van-empty v-else description="暂无数据" />
  </div>
</template> 
<script>
  import breadcrumb from '@/components/breadcrumb'
  export default {
    name: 'list',
    components: { breadcrumb },
    data() {
      return {
        isloading: false,
        paList: [
          {
            title: '这是大标题最多显示两行超出省略这是大标题最多显示两行超出省略',
          },
          {
            title: '23234dedegded',
          },
          {
            title:
              '这是大标题最多显示两行超出省232略这是大标题最多显示两行超出省略',
          },
          {
            title:
              '这是大标题最多显示两行超出省略这是大标题最多显示两行超出省44略',
          },
        ],
      }
    },
    mounted() {
      this.getData()
    },
    methods: {
      async getData() {
        this.isloading = true
        setTimeout(() => {
          this.isloading = false
        }, 1000)
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