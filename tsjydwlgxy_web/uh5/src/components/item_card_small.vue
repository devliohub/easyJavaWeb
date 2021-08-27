<template>
  <div class="item_card_small" @click="handleClick">
    <img :src="entity.cover" alt="" />
    <main>
      <div class="title">{{ entity.name }}</div>
      <div class="time" v-if="isIndexPage">
        {{ dateFormater(entity.create_time * 1000, 'YYYY-MM-DD') }}
      </div>
    </main>
  </div>
</template>
<script>
  // 首页推荐小卡片
  export default {
    name: 'item_card_small',
    props: {
      entity: {
        default: {},
        type: Object,
      },
    },
    data() {
      return {}
    },
    computed: {
      isIndexPage() {
        return true
        // return this.$route.name == 'index'
      },
    },
    methods: {
      handleClick() {
        this.$router.push('./desc')
      },
    },
  }
</script>
<style lang="scss" scope>
.item_card_small {
  width: 100%;
  text-align: left;
  & > img {
    width: 100%;
    height: 60px;
  }
  & > main {
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