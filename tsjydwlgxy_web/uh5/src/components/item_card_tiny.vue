<template>
  <div class="item_card_tiny" @click="handleClick">
    <main>
      <div class="title">
        <div class="dotter"></div>
        <span>{{ entity.title }}</span>
      </div>
      <div class="time">{{ entity.time }}</div>
    </main>
  </div>
</template>
<script>
  export default {
    name: 'item_card_tiny',
    props: {
      entity: {
        default: {},
        type: Object,
      },
    },
    data() {
      return {}
    },
    mounted() {},
    computed: {},
    methods: {
      handleClick() {
        this.$router.push('./desc')
      },
    },
  }
</script>
<style lang="scss" scope>
.item_card_tiny {
  width: 100%;
  & > main {
    display: flex;
    justify-content: space-between;
    .title {
      font-weight: bold;
      color: #444;
      display: flex;
      align-items: center;
      .dotter {
        margin-right: 8px;
        width: 6px;
        height: 6px;
        border-radius: 50%;
        background: #d9ae73;
      }
    }
    .time {
      color: #969696;
    }
  }
}
</style>