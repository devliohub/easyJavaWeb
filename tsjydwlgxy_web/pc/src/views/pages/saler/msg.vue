<template>
  <div class="msg">
    <header>
      <span></span>消息提示
    </header>
    <ul v-if="msgList.length > 0">
      <li v-for="(msg, i) in msgList" :key="i">
        <span>{{i + 1}}. {{msg.content}}</span>
        <span class="_time">{{msg.create_time}}</span>
      </li>
    </ul>
    <div class="no_data" v-else>暂无消息通知</div>
  </div>
</template>
<script>
import { getMsg } from "@/api/sys.js";

export default {
  name: "msg",
  data() {
    return {
      msgList: []
    };
  },
  computed: {
    isUser() {
      return this.$route.name == "userMsg";
    }
  },
  async mounted() {
    let res = await getMsg({
      platform: this.isUser ? "2c" : "2b",
      page_no: 1,
      page_size: 100
    });
    this.msgList = res.data;
  }
};
</script>
<style lang="scss" scoped>
.msg {
  width: 100%;
  & > header {
    width: 100%;
    font-size: 16px;
    border-bottom: 1px solid #e5e5e5;
    padding: 12px 0;
    font-weight: bold;
    span {
      display: inline-block;
      vertical-align: middle;
      width: 4px;
      height: 18px;
      background: #ff5500;
      margin-right: 12px;
    }
  }
  & > ul {
    width: 100%;
    display: flex;
    flex-direction: column;
    padding: 0 20px 0 30px;
    li {
      // width: 100%;
      padding: 15px 0;
      display: flex;
      justify-content: space-between;
      ._time {
        font-size: 12px;
        color: #999;
      }
    }
  }
  .no_data {
    text-align: center;
    padding: 25px;
    font-size: 16px;
  }
}
</style>