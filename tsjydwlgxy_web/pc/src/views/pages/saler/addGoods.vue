<template>
  <div class="addGoods">
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
              <img :src="entity.img" alt="" />
              <div>{{ entity.title }}</div>
            </li>
          </ul>

          <el-pagination
            background
            style="margin: 15px 0"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="queryObj.page_no"
            :page-size="queryObj.page_size"
            layout="prev, pager, next"
            :total="total_count"
          ></el-pagination>
        </div>
      </main>
    </section>
  </div>
</template>
<script>
  import { salerProducts, salerOption } from '@/api/saler/index'

  export default {
    name: 'addGoods',
    data() {
      return {
        reset: false,
        isloading: false,
        addObj: {
          opt: 'add',
          product_url: '',
        },
        queryObj: {
          condition: '',
          condition_value: '',
          page_no: 1,
          page_size: 8,
        },
        total_count: 0,

        entitys: [],
        target_pid: '',

        input3: '',
        select: '',
        showApply: false,
        showDelete: false,
        radio: '',
      }
    },
    watch: {
      showApply(val) {
        if (val) this.radio = ''
      },
    },
    mounted() {
      this.getGoods()
    },
    methods: {
      async getGoods() {
        if (this.reset) {
          this.queryObj.page_no = 1
          this.queryObj.page_size = 8
        }
        this.isloading = true
        let res = await salerProducts(this.queryObj)
        if (res && res.error.errno == 200) {
          this.entitys = res.data
          this.total_count = res.total_results
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
            this.reset = true
            this.getGoods()
          }
          this.isloading = false
        }
      },
      // 删除商品
      async handleDel() {
        this.isloading = true
        let res = await salerOption({
          p_id: this.target_pid,
          opt: 'del',
        })
        if (res && res.error.errno == 200) {
          this.$message.success('删除成功！')
          this.showDelete = false
          this.getGoods()
        }
        this.isloading = false
      },
      goToApply() {
        if (this.radio == '') {
          this.$message.warning('请先选择申请类型')
        } else {
          this.showApply = false
          this.radio == 1
            ? this.$router.push('/saler/freeApply?p_id=' + this.target_pid)
            : this.$router.push('/saler/bearBuyApply?p_id=' + this.target_pid)
        }
      },
      handleSizeChange(val) {
        this.queryObj.page_no = 1
        this.queryObj.page_size = val
        this.reset = false
        this.getGoods()
      },
      handleCurrentChange(val) {
        this.queryObj.page_no = val
        this.reset = false
        this.getGoods()
      },
    },
  }
</script>
<style lang="scss" scope>
.addGoods {
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