<template>
  <ul v-if="$route.name == 'wenzhang'">
    <li @click="handleGOwenzhang(entity)">{{ entity.name }}</li>
    <template>
      <li
        :class="{ isactive: $route.query.name == item.name }"
        v-for="(item, index) in entity.sub_menus"
        :key="index"
        @click="handleGOwenzhang(item, index)"
      >
        {{ item.name }}
      </li>
    </template>
  </ul>
  <ul v-else-if="$route.name == 'kecheng'">
    <li @click="$router.push('/saler/kecheng')">通识课程</li>
    <template>
      <li
        :class="{ isactive: $route.query.name == item.name }"
        v-for="(item, index) in entity"
        :key="index"
        @click="handleGOkecheng(item, index)"
      >
        {{ item.name }}
      </li>
    </template>
  </ul>
</template>

<script>
  import { getCoursemodules } from '@/api'
  export default {
    props: {},
    data() {
      return {
        entity: {},
      }
    },
    computed: {},
    watch: {
      $route: function () {
        this.getData()
      },
    },
    mounted() {
      this.getData()
    },
    methods: {
      async getData() {
        if (this.$route.name == 'kecheng') {
          let res = await getCoursemodules()
          if (res && res.errno == 200) {
            this.entity = res.result
          }
        } else if (this.$route.name == 'wenzhang') {
          JSON.parse(window.sessionStorage.getItem('menuArr')).map((item) => {
            if (item.id == this.$route.query.id) this.entity = item

            if (item.sub_menus && item.sub_menus.length > 0) {
              item.sub_menus.map((_item) => {
                if (_item.id == this.$route.query.id) this.entity = item
              })
            }
          })
        }
      },
      handleGOwenzhang(item) {
        this.$router.push(
          '/saler/wenzhang?id=' +
            item.id +
            '&pid=' +
            item.pid +
            '&name=' +
            item.name
        )
      },
      handleGOkecheng(item) {
        this.$router.push(
          '/saler/kecheng?module_id=' + item.code + '&name=' + item.name
        )
      },
    },
  }
</script>
<style lang="scss" scope>
.left_menu {
  & > ul {
    font-weight: bold;
    font-size: 15px;
    li {
      border: 1px solid #efeff7;
      border-top: none;
      height: 50px;
      line-height: 50px;
      padding-left: 25px;
      cursor: pointer;
      font-weight: normal;
      &:first-child {
        padding-left: 10px;
        color: #fff;
        background: #660000;
      }
    }
  }
  .isactive {
    font-weight: bold;
    color: #660000;
  }
}
</style>