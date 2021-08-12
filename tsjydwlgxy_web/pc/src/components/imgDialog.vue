<template>
  <el-dialog
    class="imgDialog"
    append-to-body
    :visible.sync="dialogVisible"
    :width="width"
    :top="top"
    :show-close="false"
  >
    <div v-if="dialogVisible">
      <!-- 多图片 -->
      <div v-if="isMultiple" class="img_outer" style="overflow: hidden">
        <ul v-for="(item, index) in imgSrcs" :key="index">
          <li class="multi_img" v-loading="item.isloading">
            <img :src="item.src" @load="item.isloading = false" />
          </li>
        </ul>
      </div>
      <!-- 单图片 -->
      <div v-else v-loading="loading" class="img_outer">
        <el-image :src="imgSrc" @load="onLoad">
          <div slot="error" class="image-slot">
            <i class="el-icon-picture-outline"></i>
          </div>
        </el-image>
      </div>
    </div>
  </el-dialog>
</template>
<script>
export default {
  name: "imgDialog",
  data() {
    return {
      isMultiple: false, // 是否多张图
      loading: null, // loading层
      imgSrcs: [], // 多选数组
      imgSrc: "", // 单选链接
      width: "", // 弹窗宽度自适应
      top: "", // 弹窗高度自适应
      dialogVisible: false
    };
  },
  mounted() {},
  methods: {
    resetData() {
      this.imgSrcs = [];
      this.dialogVisible = true;
      this.loading = true;
      this.top = "20vh";
    },
    init(val) {
      // console.log(JSON.parse(JSON.stringify(val)))
      if (val) {
        this.resetData();
        if (typeof val == "object" && val.length > 1) {
          // 多图片计算
          this.imgSrc = "";
          this.isMultiple = true;
          // 多图赋值
          val.map(el => {
            let _obj = {
              src: el,
              isloading: true
            };
            this.imgSrcs.push(_obj);
          });
          this.calcMultiLayout(val.length);
        } else if (val.length == 1) {
          // 单图片
          this.isMultiple = false;
          this.imgSrc = val[0];
        } else {
          // 单图片
          this.isMultiple = false;
          this.imgSrc = val;
        }
      }
    },
    // 单图片计算
    onLoad(e) {
      const img = e.target;
      let _width = 0;
      let _height = 0;
      if (img.fileSize > 0 || (img.width > 1 && img.height > 1)) {
        _width = img.naturalWidth + 20;
        _height = img.naturalHeight + 20;
      }
      // 设置宽度,top度 不超过80%
      if (_width > window.innerWidth * 0.8) {
        this.width = "80%";
        if (_height * 0.8 < window.innerHeight) {
          this.top = (window.innerHeight - _height * 0.8) / 2 + "px";
        } else {
          this.top = "0";
        }
      } else {
        this.width = _width + "px";
        if (_height < window.innerHeight) {
          this.top = (window.innerHeight - _height) / 2 + "px";
        } else {
          this.top = "0";
        }
      }
      this.loading = false;
    },
    // 多图片计算（单个图600*300） 一行2张
    calcMultiLayout(img_num) {
      // 宽度
      this.width = img_num > 1 ? "1220px" : 600 * img_num + 20 + "px";
      // 高度
      let _height;
      if (img_num > 1) {
        _height = Math.ceil(img_num / 2) * 300 + 20;
      } else {
        _height = 320;
      }
      if (_height > window.innerHeight) {
        this.top = "0";
      } else {
        this.top = (window.innerHeight - _height) / 2 + "px";
      }
    }
  }
};
</script>
<style lang="scss">
.imgDialog {
  // .img_outer {
    // min-width: 100px;
    // min-height: 100px;
    // overflow: hidden;
  // }
  .el-dialog__header {
    padding: 0;
  }
  .el-dialog__body {
    background: #f5f5f5;
    overflow: auto;
    padding: 10px !important;
  }
  .multi_img {
    float: left;
    padding: 5px;
    width: 600px;
    height: 300px;
    img {
      width: 100%;
      height: 100%;
    }
  }
}
</style>
