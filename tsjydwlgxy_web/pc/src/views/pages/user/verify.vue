<template>
  <div class="verify">
    <!-- 图片放大 -->
    <img-dialog ref="imgDialog" />

    <header>
      <span></span>实名认证
    </header>
    <main>
      <el-form size="small" label-width="100px" v-loading="isloading">
        <el-form-item label="真实姓名">
          <el-input :disabled="!entity.is_submit" v-model.trim="entity.real_name" clearable />
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group :disabled="!entity.is_submit" v-model="entity.gender">
            <el-radio label="男" value="男"></el-radio>
            <el-radio label="女" value="女"></el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="年龄">
          <el-input :disabled="!entity.is_submit" v-model="entity.age" type="number" clearable />
        </el-form-item>
        <el-form-item label="淘宝会员名">
          <el-input :disabled="!entity.is_submit" v-model.trim="entity.ww_name" clearable />
        </el-form-item>
        <el-form-item label="支付宝截图">
          <el-upload
            :disabled="!entity.is_submit"
            class="upload-demo"
            ref="imgsRef"
            action="#"
            :http-request="myUpload"
            accept="image/jpeg, image/gif, image/png"
            list-type="picture"
            :on-remove="handleRemove"
            :on-preview="handlePreview"
            :file-list="fileList"
            :on-exceed="onExceed"
            :limit="1"
          >
            <el-button size="small" type="primary" :disabled="!entity.is_submit">点击上传</el-button>
          </el-upload>
        </el-form-item>
      </el-form>

      <el-button v-if="entity.is_submit" type="primary" class="submit_btn" size="small" @click="handleSubmit">确认提交</el-button>
    </main>
  </div>
</template>
<script>
import imgDialog from "@/components/imgDialog";
import { verifyProve } from "@/api/user";
import { uploadImg, deleteUpload } from "@/api/common/common";

export default {
  name: "verify",
  data() {
    return {
      isloading: false,
      entity: {},
      fileList: []
    };
  },
  components: {
    imgDialog
  },
  async mounted() {
    this.isloading = true
    let res = await verifyProve();
    if (res && res.error.errno == 200) {
      this.entity = res.data;
      if (this.entity.prove_img)
        this.fileList.push({ url: this.entity.prove_img, name: '支付宝截图' });
    }
    this.isloading = false
  },
  methods: {
    async myUpload(content) {
      let form = new FormData();
      form.append("img", content.file);
      let res = await uploadImg(form);
      if (res && res.error.errno == 200) {
        this.fileList.push({
          url: res.url,
          uid: content.file.uid,
          name: content.file.name
        });
        this.$message.success('操作成功');
      }
    },
    onExceed() {
      this.$message.warning('最多上传一张截图')
    },
    handlePreview(val) {
      this.$nextTick(() => {
        this.$refs.imgDialog.init(val.url);
      });
    },
    handleRemove(a) {
      this.fileList.map(async (el, i) => {
        if (el.uid == a.uid) {
          let res = await deleteUpload({
            url: el.url
          });
          if (res && res.error.errno == 200) {
            this.fileList.splice(i, 1);
            this.$message.success("图片删除成功");
          } else {
            this.$message.success(res.error.usermsg);
          }
        }
      });
    },
    async handleSubmit() {
      if (this.fileList.length == 0) {
        this.$message.error("请上传支付宝截图");
      } else {
        this.isloading = true
        this.entity.prove_img = this.fileList[0].url;
        let res = await verifyProve(this.entity);
        if (res && res.error.errno == 200) {
          this.$message.success("认证申请成功！");
          setTimeout(() => {
            this.$router.push("/user/kefuInfo");
          }, 500);
        }
        this.isloading = false
      }
    }
  }
};
</script>
<style lang="scss" scoped>
.verify {
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

  & > main {
    display: flex;
    align-items: center;
    justify-content: center;
    flex-direction: column;
    margin-top: 50px;
  }
  .el-form-item {
    padding-right: 100px;
  }
}
</style>