<template>
  <div class="kecheng">
    <section v-loading="isloading">
      <main>
        <div class="add_section">
          <header>全部课程</header>
          <el-input
            size="small"
            v-model="addObj.product_url"
            style="width: 200px"
            placeholder="关键词搜索"
            clearable
          >
            <i
              class="el-icon-search el-input__icon"
              slot="suffix"
              @click="handleClickAdd"
            >
            </i>
          </el-input>
        </div>
        <div class="query_section">
          <nav>
            <span v-for="(item, index) in 4" :key="index">{{ item }}</span>
          </nav>

          <ul>
            <li
              v-for="(entity, x) in entitys"
              :key="x"
              :class="{ margin_right_0: x % 4 == 3 }"
            >
              <el-image :src="entity.cover" alt="" />
              <div>{{ entity.name }}</div>
            </li>
          </ul>

          <el-pagination
            background
            style="margin: 15px 0"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="queryObj.pageNo"
            :page-size="queryObj.pageSize"
            layout="prev, pager, next"
            :total="total_count"
          ></el-pagination>
        </div>
      </main>
    </section>
  </div>
</template>
<script>
  import { getKecheng } from '@/api'
  export default {
    name: 'kecheng',
    data() {
      return {
        isloading: false,
        addObj: {
          opt: 'add',
          product_url: '',
        },
        queryObj: {
          condition: '',
          condition_value: '',
          pageNo: 1,
          pageSize: 8,
        },
        total_count: 0,

        entitys: [],
        target_pid: '',
      }
    },
    watch: {},
    mounted() {
      this.getGoods()
    },
    methods: {
      async getGoods() {
        this.isloading = true
        let res = await getKecheng(this.queryObj)
        if (res && res.errno == 200) {
          this.entitys = res.result.list
          this.total_count = res.result.total
        }
        this.isloading = false
      },
      // 新增商品
      async handleClickAdd() {
        if (!this.addObj.product_url) {
          return this.$message.warning('请填写宝贝链接')
        } else {
          this.isloading = true
          let res = await salerOption(this.addObj)
          if (res && res.error.errno == 200) {
            this.$message.success('添加成功！')
            this.getGoods()
          }
          this.isloading = false
        }
      },
      handleSizeChange(val) {
        this.queryObj.pageNo = 1
        this.queryObj.pageSize = val
        this.getGoods()
      },
      handleCurrentChange(val) {
        this.queryObj.pageNo = val
        this.getGoods()
      },
    },
  }
</script>
<style lang="scss" scope>
.kecheng {
  section {
    width: 100%;
    & > main {
      width: 100%;
      border: 1px solid #efeff7;
      padding: 15px 20px;
      .add_section {
        width: 100%;
        display: flex;
        align-items: center;
        justify-content: space-between;
        & > header {
          font-size: 18px;
          font-weight: bold;
          padding: 10px;
        }
        input {
          border-radius: 50px;
          background: #eee;
        }
      }

      .query_section {
        & > nav {
          display: flex;
          align-items: center;
          margin: 18px 0;
          span {
            display: inline-block;
            padding: 5px 15px;
            border: 1px solid #e4e4e4;
            margin-right: 20px;
            border-radius: 20px;
          }
        }

        & > ul {
          font-size: 14px;
          display: flex;
          flex-wrap: wrap;
          li {
            border: 1px solid red;
            width: calc(25% - 18px);
            margin-right: 24px;
            img {
              width: 100%;
            }
            div {
              font-weight: bold;
              line-height: 1.5;
              padding: 10px 0;
              font-size: 14px;
              color: #444;
            }
          }
        }
      }
    }
  }
}
</style>