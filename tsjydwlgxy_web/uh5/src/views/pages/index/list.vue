<template>
  <div class="list">
    <header>
      <div style="color: #888">当前位置：</div>
      <div style="color: #444">通识中心</div>
    </header>
    <div v-if="isloading" class="center_loading">
      <van-loading type="spinner" />
    </div>
    <ul v-else>
      <li
        v-for="(item, x) in paList"
        :key="x"
        :class="{ signle_class: x % 2 == 0 }"
      >
        <div class="title">{{ item.title }}</div>
        <div class="time">发布时间：2010-02-04</div>
      </li>
    </ul>
  </div>
</template> 
<script>
  import itemCardLarge from '@/components/item_card_large'
  export default {
    name: 'list',
    components: { itemCardLarge },
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
  header {
    background: #fff;
    margin-top: 50px;
    padding: 15px;
    font-size: 14px;
    display: flex;
  }
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