<template>
  <div class="index">
    <div class="let_fixed">
      <van-icon v-if="showPop" name="cross" @click="showPop = false" />
      <van-icon v-else name="wap-nav" @click="showPop = true" />
      <img src="@/assets/logo@2x.png" alt="" />
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
        <van-collapse
          v-model="activeName"
          accordion
          v-for="(item, index) in menuList"
          :key="index"
        >
          <van-collapse-item
            :is-link="item.sub_menus.length > 0"
            :disabled="!item.sub_menus > 0"
            :title="item.name"
            :name="item.id"
            :title-class="{ is_active: $route.query.id == item.id }"
            @click.native="handleMenuClick(item)"
          >
            <template v-if="item.sub_menus.length > 0">
              <van-collapse
                accordion
                v-for="(_item, _index) in item.sub_menus"
                :key="_index"
              >
                <van-collapse-item
                  :is-link="false"
                  :title="_item.name"
                  :name="_item.id"
                  :title-class="{ is_active: $route.query.id == _item.id }"
                  @click.native="handleMenuClick(_item)"
                >
                </van-collapse-item>
              </van-collapse>
            </template>
          </van-collapse-item>
        </van-collapse>
      </div>
    </van-popup>
  </div>
</template>

<script>
  import { getMenus } from '@/api'
  export default {
    name: 'index',
    data() {
      return {
        showPop: false,
        activeName: null,
        menuList: [],
      }
    },
    async mounted() {
      let res = await getMenus()
      if (res && res.errno == 200) {
        this.menuList = res.result
      }
    },
    methods: {
      handleMenuClick(v) {
        if (v.sub_menus && v.sub_menus.length > 0) return
        if (v && v.id != 1) {
          if (v.name == '通识课程') {
            this.$router.push('/kecheng?id=' + v.id + '&pid=' + v.pid)
          } else {
            this.$router.push('/list?id=' + v.id + '&pid=' + v.pid)
          }
        } else {
          this.$router.push('/index')
        }
        this.showPop = false
      },
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

    .is_active {
      color: #c00900;
    }
  }

  .bottom_tabs {
    // display: none;
    .iconfont {
      font-size: 18px;
    }
  }

  .van-collapse-item__content {
    padding: 0 !important;
  }
  .van-collapse-item__wrapper {
    padding-left: 20px;
  }
}
</style>
