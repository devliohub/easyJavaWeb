<template>
  <div class="footer">
    <main>
      <div class="footer_left">
        <ul>
          <li>手机网站</li>
          <li>
            <img src="@/assets/shoujiErweima.jpg" alt="" />
          </li>
        </ul>
        <ul>
          <li>微信公众号</li>
          <li>
            <img src="@/assets/erweima.jpg" alt="" />
          </li>
        </ul>
      </div>
      <div class="footer_right">
        <ul>
          <li>联系我们</li>
          <li>地址：广东省东莞市松山湖大学路1号</li>
          <li>邮编：523808</li>
          <li>电话：0769-22861099</li>
        </ul>
        <ul>
          <li>友情链接</li>
          <li>
            <el-link
              type="primary"
              target="_blank"
              href="https://www.dgut.edu.cn/"
              >东莞理工学院官网</el-link
            >
          </li>
          <li>
            <el-link
              type="primary"
              target="_blank"
              href="http://jwc.dgut.edu.cn/"
              >东莞理工学院教务处</el-link
            >
          </li>
          <li>
            <el-link type="primary" target="_blank" href="/crm"
              >管理后台入口</el-link
            >
          </li>
        </ul>
      </div>
    </main>
  </div>
</template>

<script>
  export default {
    name: 'indexfooter',
    data() {
      return {}
    },
    computed: {},
  }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.footer {
  width: 100%;
  background: #424242;
  padding: 30px 15px;
  color: #fff;
  main {
    width: 1200px;
    margin: 0 auto;
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
  &_left {
    overflow: hidden;
    ul {
      float: left;
      padding: 0;
      margin-right: 100px;
      li {
        &:first-child {
          font-weight: bold;
          padding: 0 0 15px 0;
        }
        & > img {
          display: block;
          width: 70px;
          height: 70px;
        }
      }
    }
  }
  &_right {
    display: flex;
    & > ul {
      padding: 0;
      margin-right: 100px;
      li {
        &:first-child {
          font-weight: bold;
          padding: 10px 0;
        }
        list-style: none;
        padding: 6px 0;
        a {
          color: #FFFFFF
        }
      }
    }
  }
}
</style>
