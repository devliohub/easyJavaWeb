<template>
  <div class="item_card_mid">
    <img :src="entity.cover" alt />
    <main>
      <div class="title">
        <span>{{ entity.title }}</span>
      </div>
      <div class="time">发布时间：2021-08-09</div>
    </main>
  </div>
</template>
<script>
  // 首页精选免单中卡片
  export default {
    name: 'item_card_mid',
    props: {
      entity: {
        type: Object,
        default: {},
      },
    },
    data() {
      return {}
    },
    methods: {},
  }
</script>
<style lang="scss" scope>
.item_card_mid {
  width: 100%;
  display: flex;
  & > img {
    width: 100px;
    margin-right: 20px;
    height: 60px;
  }
  & > main {
    flex: 1;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    .title {
      font-size: 14px;
      margin: 0 0 10px 0;
      font-weight: bold;
      color: #444;
      display: -webkit-box;
      -webkit-box-orient: vertical;
      -webkit-line-clamp: 2;
      overflow: hidden;
    }
    .time {
      padding: 5px 0;
      font-size: 12px;
      color: #969696;
    }
  }
}
</style>