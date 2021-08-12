<template>
  <div class="kefuInfo">
    <header>
      <span></span>客服信息
    </header>
    <span>
      实名认证已提交，微信扫码找客服“
      <template v-if="$store.state.user.name.prove_group == 'A'">嘟嘟</template>
      <template v-else-if="$store.state.user.name.prove_group == 'B'">蹦蹦</template>”，发手机号审核。否则无法领取任务，谢谢！
    </span>
    <img v-if="$store.state.user.name.prove_group == 'A'" src="@/assets/components/kefu2.png" alt />
    <img
      v-else-if="$store.state.user.name.prove_group == 'B'"
      src="@/assets/components/kefu1.png"
      alt
    />
  </div>
</template>
<script>
export default {
  name: "kefuInfo",
  data() {
    return {};
  },
  mounted() {},
  methods: {}
};
</script>
<style lang="scss" scope>
.kefuInfo {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  width: 100%;
  & > header {
    width: 100%;
    font-size: 16px;
    border-bottom: 1px solid #e5e5e5;
    padding: 12px 0;
    font-weight: bold;
    span {
      display: inline-block;
      vertical-align: middle;
      width: 4px;
      height: 18px;
      background: #ff5500;
      margin-right: 12px;
    }
  }
  & > span {
    color: red;
    font-size: 16px;
    padding: 25px;
  }
}
</style>