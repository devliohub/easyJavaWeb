<template>
  <div class="item_card">
    <img v-if="entity.img" class="item_card_img" :src="entity.img" @click.once="goToItemDesc" />
    <img v-else class="item_card_img" src="@/assets/404_images/404.png" alt />

    <div class="item_card_main">
      <div class="item_card_main_title">{{ entity.title || "暂无" }}</div>
      <!-- 熊抢购 -->
      <div v-if="entity.module_type == 'xqg'" class="item_card_main_context2">
        <div class="item_card_main_context2_price">
          <span>¥ {{entity.price}}</span>
          <div>
            <span>返利</span>
            <span>¥{{(entity.price - entity.current_price).toFixed(2)}}</span>
          </div>
        </div>
        <div class="item_card_main_context2_desc">
          <span>
            最低价
            <i>{{entity.current_price}}</i>元
          </span>
          <img v-if="entity.shop_type == 1" src="@/assets/tianmao.png" alt />
          <img v-else src="@/assets/taobao.png" alt />
        </div>
      </div>

      <!-- 限量免单 -->
      <div v-else class="item_card_main_context">
        <span class="item_card_main_context_price">￥{{ entity.price }}</span>
        <div class="item_card_main_context_desc">
          <el-progress
            :percentage="Percentage"
            :text-inside="true"
            :show-text="false"
            :color="`#f2cecd`"
          ></el-progress>
          <span class="desc_context">已抢{{entity.order_count || 0}}件共{{entity.task_count || 0}}件</span>
        </div>
      </div>

      <div class="item_card_main_btn" @click.once="goToItemDesc">马上抢</div>
    </div>
  </div>
</template>
<script>
export default {
  name: "item_card",
  props: {
    entity: {
      default: function() {
        return {
          img: "",
          shop_name: "店铺名称123",
          price: "9.9",
          order_count: 10,
          task_count: 10
        };
      },
      type: Object
    }
  },
  computed: {
    Percentage() {
      let _o = this.entity.order_count || 0;
      let _t = this.entity.task_count || 1;
      if (100 * (1 - _o / _t) > 100) {
        return 100;
      } else {
        return 100 * (1 - _o / _t);
      }
    }
  },
  data() {
    return {};
  },
  methods: {
    goToItemDesc() {
      let p = this.$route.fullPath;
      let pid = this.entity.pa_id;
      if (p.lastIndexOf("?") > 0) p = p.substr(0, p.lastIndexOf("?"));
      this.$router.push(p + "/itemDesc?pid=" + pid);
    }
  }
};
</script>

<style lang="scss" scope>
.item_card {
  width: 100%;
  padding: 20px;
  cursor: pointer;
  transition: 0.2s ease-in-out;
  &:hover {
    opacity: 0.8;
  }
  &_img {
    width: 160px;
    height: 160px;
  }
  &_main {
    &_title {
      padding: 12px 0;
      max-width: 160px;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
      color: #555;
      font-size: 12px;
      font-weight: bold;
    }

    // 限量免单
    &_context {
      display: flex;
      justify-content: space-between;
      padding-bottom: 15px;
      &_price {
        font-size: 22px;
        color: #ff5500;
        text-decoration: line-through;
      }
      &_desc {
        display: flex;
        flex-direction: column;
        .el-progress-bar__outer {
          height: 7px !important;
        }
        .desc_context {
          flex: 1;
          line-height: 24px;
          font-size: 12px;
          color: #999;
          max-width: 90px;
          overflow: hidden;
          white-space: nowrap;
          text-overflow: ellipsis;
        }
      }
    }

    // 熊抢购
    &_context2 {
      padding-bottom: 15px;
      &_price {
        display: flex;
        align-items: center;
        font-size: 12px;
        & > span {
          font-size: 16px;
          color: #ff464f;
          text-decoration: line-through;
        }
        & > div {
          margin-left: 10px;
          border: 1px solid #ff464f;
          & > span {
            display: inline-block;
            padding: 2px 5px;
          }
          & > span:first-child {
            background: #ff464f;
            color: #fff;
          }
          & > span:last-child {
            background: #fff;
            color: #ff464f;
          }
        }
      }
      &_desc {
        margin-top: 8px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        & > span:first-child {
          color: #fff;
          background: #ff464f;
          border-radius: 0 15px 15px 0;
          padding: 4px 10px;
          font-size: 10px;
          i {
            font-size: 14px;
          }
        }
        & > img {
          width: 24px;
          color: #ff464f;
        }
      }
    }
    &_btn {
      // margin-bottom: 20px;
      height: 32px;
      line-height: 32px;
      text-align: center;
      border: 1px solid #ff5500;
      color: #ff5500;
      border-radius: 5px;
      transition: 0.2s ease-in-out;
      cursor: pointer;
      &:hover {
        color: #fff;
        background: #ff5500;
      }
    }
  }
}
</style>
