<template>
  <div class="item_card_mid" @click="handleClick">
    <img :src="entity.img" alt />
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
    methods: {
      handleClick() {},
    },
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
    .title {
      font-size: 14px;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
      padding: 10px 0;
      font-weight: bold;
      color: #444;
    }
    .time {
      padding: 5px 0;
      font-size: 12px;
      color: #969696;
    }
  }
}
</style>