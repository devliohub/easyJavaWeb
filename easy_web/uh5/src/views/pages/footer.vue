<template>
  <div class="footer">
    <div class="footer_bottom">
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
  &_top {
    // height: 150px;
    overflow: hidden;
    margin-bottom: 30px;
    ul {
      float: left;
      padding: 0;
      margin-right: 30px;
      li {
        &:first-child {
          font-weight: bold;
          padding: 10px 0;
        }
        list-style: none;
        & > span {
          display: block;
          width: 65px;
          height: 65px;
          background: #fff;
        }
      }
    }
  }
  &_bottom {
    display: flex;
    & > ul {
      flex: 1;
      padding: 0;
      margin: 0;
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
