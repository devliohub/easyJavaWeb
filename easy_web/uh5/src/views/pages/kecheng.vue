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
          <li
            v-for="(item, index) in dataList"
            :key="index"
            @click="kechengDesc(item)"
            :class="{ margin_right_0: index % 2 == 1 }"
          >
            <item-card-small :entity="item" />
          </li>
        </ul>

        <van-pagination
          v-model="queryData.pageNo"
          :total-items="totalPage"
          :items-per-page="queryData.pageSize"
          mode="simple"
          @change="() => getData()"
        />
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
          pageSize: 10,
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
      kechengDesc(item) {
        window.open(item.url)
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
      .margin_right_0 {
        padding-right: 0 !important;
      }
      li {
        width: 50%;
        padding: 0 15px 10px 0;
      }
    }
    .van-pagination {
      margin: 14px 0 30px 0;
      .van-pagination__page-desc {
        margin: 0 15px;
      }
      .van-pagination__item {
        border: 1px solid #c5c5c5;
        color: #462b2b;
        border-radius: 4px;
        height: 40px;
      }
      .van-pagination__next,
      .van-pagination__prev {
        padding: 0;
      }
    }
  }
}
</style>