<template>
  <div class="footer">
    <main>
      <div class="footer_left">
        <ul>
          <li>手机网站</li>
          <li>
            <span>二维码</span>
          </li>
        </ul>
        <ul>
          <li>微信公众号</li>
          <li>
            <span>微信公众号二维码</span>
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
          <li>东莞理工学院官网</li>
          <li>东莞理工学院教务处</li>
          <li>123456789</li>
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
          padding: 10px 0;
        }
        & > span {
          display: block;
          width: 65px;
          height: 65px;
          background: #fff;
        }
      }
    }
  }
  &_right {
    display: flex;
    & > ul {
      flex: 1;
      padding: 0;
      margin-right: 100px;
      li {
        &:first-child {
          font-weight: bold;
          padding: 10px 0;
        }
        list-style: none;
        padding: 6px 0;
      }
    }
  }
}
</style>
