<template>
  <div class="kecheng">
    <section v-loading="isloading">
      <main>
        <div class="add_section">
          <header>全部课程</header>
          <el-input
            size="small"
            v-model="queryObj.name"
            style="width: 200px"
            placeholder="关键词搜索"
          >
            <i
              class="el-icon-search el-input__icon"
              slot="suffix"
              @click="getData"
            >
            </i>
          </el-input>
        </div>
        <div class="query_section">
          <nav>
            <el-radio-group
              v-model="queryObj.type_id"
              size="small"
              @change="() => getData()"
              :max="1"
            >
              <el-radio
                v-for="(item, index) in courseArr"
                :label="item.code"
                :key="index"
                border
                >{{ item.name }}({{ item.count }})</el-radio
              >
            </el-radio-group>
          </nav>

          <ul>
            <li
              v-for="(entity, x) in entitys"
              :key="x"
              :class="{ margin_right_0: x % 4 == 3 }"
              @click="goUrl(entity)"
            >
              <div class="wrapper">
                <el-image :src="entity.cover" alt="">
                  <div slot="error" class="image-slot">
                    <img src="@/assets/kechengMR.jpg" alt="" />
                  </div>
                </el-image>
                <span
                  :class="{
                    _orange: entity.typeName == '公共必修课',
                    _blue: entity.typeName == '公共选修课',
                    _green: entity.typeName == '核心限选课',
                  }"
                  >{{ entity.typeName }}</span
                >
              </div>

              <div class="namer">{{ entity.name }}</div>
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
  import { getKecheng, getCoursetypes } from '@/api'
  export default {
    name: 'kecheng',
    data() {
      return {
        isloading: false,
        courseArr: [],
        queryObj: {
          type_id: [],
          name: '',
          pageNo: 1,
          pageSize: 8,
        },
        total_count: 0,

        entitys: [],
      }
    },
    watch: {
      $route() {
        this.getData()
      },
    },
    mounted() {
      this.getData()
      this.getCourses()
    },
    methods: {
      async getData() {
        this.isloading = true
        let res = await getKecheng({
          ...this.queryObj,
          module_id: this.$route.query.module_id,
        })
        if (res && res.errno == 200) {
          this.entitys = res.result.list
          this.total_count = res.result.total
        }
        this.isloading = false
      },
      async getCourses() {
        let res = await getCoursetypes()
        if (res && res.errno == 200) {
          this.courseArr = JSON.parse(JSON.stringify(res.result))

          let _t = 0
          res.result.map((el) => {
            _t += el.count
          })

          this.courseArr.unshift({
            code: 0,
            count: _t,
            name: '全部',
          })
        }
      },
      handleRadioClick(v) {
        // this.$set(this.queryObj, 'type_id', v == this.queryObj.type_id ? '' : v)
        // this.getData()
      },
      handleSizeChange(val) {
        this.queryObj.pageNo = 1
        this.queryObj.pageSize = val
        this.getData()
      },
      handleCurrentChange(val) {
        this.queryObj.pageNo = val
        this.getData()
      },
      goUrl(item) {
        window.open(item.url)
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
          background: #f1f1f1;
        }
      }

      .query_section {
        & > nav {
          display: flex;
          align-items: center;
          margin: 18px 0;
          // span {
          //   cursor: pointer;
          //   display: inline-block;
          //   padding: 5px 15px;
          //   border: 1px solid #e4e4e4;
          //   margin-right: 20px;
          //   border-radius: 20px;
          // }
        }

        & > ul {
          font-size: 14px;
          display: flex;
          flex-wrap: wrap;
          li {
            width: calc(25% - 18px);
            margin: 0 24px 24px 0;
            cursor: pointer;
            .wrapper {
              width: 100%;
              position: relative;
              span {
                position: absolute;
                left: 10px;
                bottom: 10px;
                z-index: 999;
                padding: 5px 15px;
                color: #fff;
              }
              img {
                width: 100%;
                height: 9vw; //16:9
              }
              ._orange {
                background: #ffaf00;
              }
              ._blue {
                background: #0041c1;
              }
              ._green {
                background: #00af09;
              }
            }

            .namer {
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