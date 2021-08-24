<template>
  <div class="salerTips">
    <template v-if="tipType == 1"> 暂无节假日公告 </template>
    <template v-else-if="tipType == 2">
      <div class="title">粉丝向商家发起售后</div>
      <div>
        1.粉丝端发起售后之后，商家在免单任务售后列表中查看售后详情，粉丝发起售后商家36小时内不处理，平台会自动介入处理
      </div>
    </template>
    <template v-else-if="tipType == 3">
      <img
        src="https://taodaxiong-1259123353.cos.ap-shanghai.myqcloud.com/images/zysx.png"
        alt=""
      />
      <!-- <img src="http://www.taodaxiong.cn/Content/Web/Business/as/img/1.jpg" alt="" /> -->
    </template>
    <template v-else-if="tipType == 4">
      <img
        src="http://www.taodaxiong.cn/Content/Web/Business/as/img/2.png"
        alt=""
      />
    </template>
  </div>
</template>
<script>
  export default {
    name: 'salerTips',
    data() {
      return {}
    },
    computed: {
      tipType: function () {
        return this.$route.query.id
      },
    },
    async mounted() {},
  }
</script>
<style lang="scss" scope>
.salerTips {
  width: 100%;
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  padding-bottom: 50px;
  .title {
    font-weight: bold;
    font-size: 16px;
  }
  div {
    padding: 10px 0;
    width: 100%;
  }
  img {
    padding: 5px 0;
    width: 90%;
  }
}
</style>