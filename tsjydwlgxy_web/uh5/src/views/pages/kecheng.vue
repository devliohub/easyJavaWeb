<template>
  <div class="kecheng">
    <breadcrumb />

    <section>
      <van-dropdown-menu>
        <van-dropdown-item
          v-model="queryData.type_id"
          :options="dropOptions"
          @change="() => getData()"
        />
      </van-dropdown-menu>
    </section>

    <div class="ul_wrapper">
      <div v-if="isloading" class="center_loading">
        <van-loading type="spinner" />
      </div>

      <div v-else-if="dataList.length > 0">
        <ul class="ul">
          <li v-for="(item, index) in dataList" :key="index">
            <item-card-small :entity="item" />
          </li>
        </ul>

        <div class="pagin">
          <span>上一页</span>
          <span class="num">{{ queryData.pageNo }}/{{ totalPage }}</span>
          <span>下一页</span>
        </div>
      </div>

      <van-empty v-else description="暂无数据" />
    </div>
  </div>
</template>
<script>
  import breadcrumb from '@/components/breadcrumb'
  import itemCardSmall from '@/components/item_card_small'
  import { getKecheng, getCoursetypes } from '@/api'

  export default {
    name: 'kecheng',
    components: { itemCardSmall, breadcrumb },
    data() {
      return {
        isloading: false,
        dataList: [],
        queryData: {
          type_id: 0,
          module_id: '',
          name: '',
          pageNo: 1,
          pageSize: 50,
        },
        totalPage: 0,

        value1: 0,
        dropOptions: [{ text: '全部', value: 0 }],
      }
    },
    async mounted() {
      await this.getData(true)
      this.getOptions()
    },
    methods: {
      async getOptions() {
        let res = await getCoursetypes()
        if (res && res.errno == 200) {
          res.result.map((el) => {
            this.dropOptions.push({
              text: el.name,
              value: el.code,
            })
          })
        }
      },
      async getData(flag) {
        this.isloading = true
        let res = await getKecheng(this.queryData)
        if (res && res.errno == 200) {
          this.dataList = res.result.list
          this.totalPage = res.result.total
          if (flag)
            this.$set(
              this.dropOptions[0],
              'text',
              '全部(' + res.result.total + ')'
            )
        }
        this.isloading = false
      },
    },
  }
</script>
<style lang="scss" scope>
.kecheng {
  position: relative;
  margin-top: 50px;

  section {
    width: 100%;
    display: block;
    font-size: 12px;
    .van-dropdown-menu__bar {
      box-shadow: none;
    }
    .van-dropdown-menu {
      width: 100px;
    }
  }

  // 子项目item
  .ul_wrapper {
    width: 100%;
    text-align: center;
    padding: 0 15px;
    background: #fff;
    .ul {
      display: flex;
      flex-wrap: wrap;
      li {
        width: 50%;
        padding: 10px 5px;
      }
    }
  }

  .pagin {
    padding: 20px 0;
    display: flex;
    justify-content: space-between;
    font-size: 14px;
    .num {
      background: #c00900;
      color: #fff;
      border: 1px solid #c00900;
    }
    & > span {
      padding: 8px 30px;
      border: 1px solid #c5c5c5;
      color: #444;
      border-radius: 4px;
    }
  }
}
</style>