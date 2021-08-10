<template>
  <div class="index">
    <!-- 回到顶部 -->
    <nut-backtop :duration="1000" :bottom="60"></nut-backtop>

    <div class="let_fixed">
      <van-icon v-if="showPop" name="cross" @click="showPop = false" />
      <van-icon v-else name="wap-nav" @click="showPop = true" />
      <div>东莞理工学院通识教育中心</div>
    </div>

    <transition name="fade-transform" mode="out-in">
      <router-view />
    </transition>

    <van-popup
      v-model="showPop"
      position="left"
      :style="{ height: '100%', width: '70%' }"
    >
      <div class="home_poper_ul">
        <van-collapse v-model="activeName" accordion>
          <van-collapse-item title="标题1" name="1">
            <div
              @click="
                () => {
                  $router.push('/search')
                  showPop = false
                }
              "
            >
              内容内容通识教育
            </div>
          </van-collapse-item>
          <van-collapse-item
            title="标题2"
            name="2"
            :is-link="false"
            :disabled="true"
            >内容</van-collapse-item
          >
          <van-collapse-item title="标题3" name="3">内容</van-collapse-item>
        </van-collapse>
      </div>
    </van-popup>
  </div>
</template>

<script>
  export default {
    name: 'index',
    data() {
      return {
        showPop: false,
        activeName: null,
      }
    },
  }
</script>

<style rel="stylesheet/scss" lang="scss">
.index {
  position: relative;
  width: 100%;
  overflow: auto;
  min-height: 100vh;
  background: #fff;
  .home_poper_ul {
    font-size: 16px;
    color: #444;
    margin-top: 50px;
    padding: 0;
  }

  .bottom_tabs {
    // display: none;
    .iconfont {
      font-size: 18px;
    }
  }
}
</style>
