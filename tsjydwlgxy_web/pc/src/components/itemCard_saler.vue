<template>
  <div class="salerCard">
    <!-- 图片放大 -->
    <img-dialog ref="imgDialog" />

    <img class="salerCard_img" @click="handleImgClick(entity.img)" :src="entity.img" alt="" />

    <div class="salerCard_main">
      <div class="salerCard_main_col">商品编号：{{entity.number}}</div>
      <div class="salerCard_main_col linker" @click="handleGoProducturl">标题：{{ entity.title }}</div>
      <div class="salerCard_main_col">掌柜旺旺：{{ entity.shop_ww }}</div>
      <div class="salerCard_main_col btns_col">
        <span style="background:#409EFF" @click="$emit('showApply', entity.p_id, entity.number)">活动申请</span>
        <span style="background:#67C23A" @click="$emit('clickUpdate', entity.p_id)">更新宝贝</span>
        <span style="background:#F56C6C" @click="$emit('showDelete', entity.p_id)">删除宝贝</span>
      </div>
    </div>
  </div>
</template>
<script>
import imgDialog from "@/components/imgDialog";

export default {
  name: "itemCard_saler",
  components: { imgDialog },
  props: {
    entity: {
      default: {},
      type: Object
    }
  },
  data() {
    return {};
  },
  methods: {
    handleImgClick(val) {
      this.$nextTick(() => {
        this.$refs.imgDialog.init(val);
      });
    },
    handleGoProducturl() {
      window.open(this.entity.product_url)
    }
  }
};
</script>

<style lang="scss" scope>
.salerCard {
  width: 100%;
  padding: 20px;
  .linker {
    cursor: pointer;
    color: #409eff;
  }
  &_img {
    width: 200px;
    height: 200px;
    cursor: pointer;
    display: block;
  }
  &_main {
    margin-top: 20px; 
    &_col {
      padding: 8px 0;
      max-width: 200px;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
    }
    .btns_col {
      width: 100%;
      display: flex;
      justify-content: space-between;
      margin-bottom: 0;
      & > span {
        width: 60px;
        padding: 7px 0;
        color: #fff;
        font-size: 12px;
        text-align: center;
        border-radius: 5px;
        cursor: pointer;
      }
    }
  }
}
</style>
