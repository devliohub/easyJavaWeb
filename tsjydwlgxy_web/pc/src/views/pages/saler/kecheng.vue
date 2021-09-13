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
              :class="{
                margin_right_0: x % 4 == 3,
              }"
              @mouseenter="handleMouseSet(entity, true)"
              @mouseleave="handleMouseSet(entity, false)"
            >
              <div class="wrapper">
                <el-image :src="entity.cover" alt="">
                  <div slot="error" class="image-slot">
                    <img src="@/assets/kechengMR.jpg" alt="" />
                  </div>
                </el-image>
                <b class="descBtn" v-if="entity.ishover" @click="goUrl(entity)"
                  >查看详情</b
                >
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
            :hide-on-single-page="true"
            style="margin: 15px 0"
            :pager-count="5"
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
          pageSize: 12,
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
          this.entitys.map((el) => {
            this.$set(el, 'ishover', false)
          })
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
      handleMouseSet(item, flag) {
        this.entitys.map((el) => {
          if (item.id == el.id) {
            this.$set(el, 'ishover', flag)
          }
        })
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
            flex: 1;
            margin: 0 20px 20px 0;
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
              .descBtn {
                position: absolute;
                padding: 8px 20px;
                color: #fff;
                cursor: pointer;
                font-weight: 400;
                background: #c00900;
                border-radius: 4px;
                left: 50%;
                top: 50%;
                transform: translate(-50%, -50%);
                &:hover {
                  background: #de3b32;
                }
                &:active {
                  background: #660000;
                }
              }
              img {
                width: 218px;
                height: 123px;
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
              max-width: 218px;
              font-weight: bold;
              line-height: 1.5;
              margin-top: 10px;
              font-size: 14px;
              color: #444;
              display: -webkit-box;
              -webkit-box-orient: vertical;
              -webkit-line-clamp: 2;
              overflow: hidden;
            }
          }
        }
      }
    }
  }
}
</style>