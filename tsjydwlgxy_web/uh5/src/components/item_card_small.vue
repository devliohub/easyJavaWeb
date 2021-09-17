<template>
  <div class="item_card_small">
    <!-- 课程/文章卡片 默认失败图 -->
    <van-image :src="entity.cover" v-if="isKechengPage">
      <template v-slot:error>
        <img src="@/assets/kechengMR.jpg" alt="" />
      </template>
    </van-image>
    <van-image :src="entity.cover" v-else>
      <template v-slot:error>
        <img src="@/assets/wenzhangMR.jpg" alt="" />
      </template>
    </van-image>

    <span
      :class="{
        _orange: entity.typeName == '公共必修课',
        _blue: entity.typeName == '公共选修课',
        _green: entity.typeName == '核心限选课',
      }"
      >{{ entity.typeName }}</span
    >
    <main>
      <div class="title">
        {{ isKechengPage ? entity.name : entity.title }}
      </div>
      <div class="time">
        {{ dateFormater(entity.create_time * 1000, 'YYYY-MM-DD') }}
      </div>
    </main>
  </div>
</template>
<script>
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
      isKechengPage() {
        return this.$route.name == 'kecheng'
      },
    },
    methods: {},
  }
</script>
<style lang="scss" scope>
.item_card_small {
  width: 100%;
  text-align: left;
  position: relative;
  span {
    position: absolute;
    left: 6px;
    top: 65px;
    z-index: 1;
    padding: 5px 10px;
    border-radius: 3px;
    color: #fff;
  }
  & > .van-image {
    width: 100%;
    height: 93px;
    .van-image__error img {
      width: 100%;
      height: 93px;
    }
  }
  ._orange {
    background: #ffaf00;
  }
  ._blue {
    background: #0041c1;
  }
  ._green {
    background: #00af09;
  }
  & > main {
    .title {
      font-size: 14px;
      display: -webkit-box;
      -webkit-box-orient: vertical;
      -webkit-line-clamp: 2;
      overflow: hidden;
      margin: 10px 0;
      font-weight: 500;
      line-height: 1.2;
      color: #444;
    }
    .time {
      padding: 0 0 15px 0;
      font-size: 12px;
      color: #969696;
    }
  }
}
</style>