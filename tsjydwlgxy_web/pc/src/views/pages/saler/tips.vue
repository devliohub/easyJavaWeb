<template>
  <div class="salerTips">
    <template v-if="tipType == 1">
      暂无节假日公告
    </template>
    <template v-else-if="tipType == 2">
      <div class="title">粉丝向商家发起售后</div>
      <div>1.粉丝端发起售后之后，商家在免单任务售后列表中查看售后详情，粉丝发起售后商家36小时内不处理，平台会自动介入处理</div>
      <img src="@/assets/tip_images/图片 1.png" alt="" />
      <img src="@/assets/tip_images/图片 2.png" alt="" />
      <div>2.点击处理按钮处理售后，根据实际情况填写处理说明并同意或者驳回</div>
      <img src="@/assets/tip_images/图片 3.png" alt="" />
      <img src="@/assets/tip_images/图片 4.png" alt="" />
      <div>3.处理完成后，可以查看协商历史</div>
      <img src="@/assets/tip_images/图片 5.png" alt="" />
      <img src="@/assets/tip_images/图片 6.png" alt="" />

      <div class="title">商家向粉丝发起售后</div>
      <div>1.在免单任务列表中找到对应订单，点击申请售后</div>
      <img src="@/assets/tip_images/图片 7.png" alt="" />
      <div>2.选择售后原因（资金问题），填写实付金额、申请说明、上传售后凭证，点击提交。</div>
      <img src="@/assets/tip_images/图片 8.png" alt="" />
      <div>3.点击协商历史，可查看与粉丝的协商历史</div>
      <img src="@/assets/tip_images/图片 9.png" alt="" />
      <div>4.提交售后申请后售后状态变为待粉丝处理 商家发起售后粉丝36小时内不处理，平台会自动介入，在售中（资金）的订单无法返款成功</div>
      <img src="@/assets/tip_images/图片 9_2.png" alt="" />
    </template>
    <template v-else-if="tipType == 3">
      <img src="https://taodaxiong-1259123353.cos.ap-shanghai.myqcloud.com/images/zysx.png" alt="" />
      <!-- <img src="http://www.taodaxiong.cn/Content/Web/Business/as/img/1.jpg" alt="" /> -->
    </template>
    <template v-else-if="tipType == 4">
      <img src="http://www.taodaxiong.cn/Content/Web/Business/as/img/2.png" alt="" />
    </template>
  </div>
</template>
<script>
export default {
  name: "salerTips",
  data() {
    return {
    };
  },
  computed: {
    tipType: function() {
      return this.$route.query.id
    }
  },
  async mounted() {
  },
};
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