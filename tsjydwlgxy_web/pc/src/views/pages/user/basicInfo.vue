<template>
  <div class="basicInfo">
    <el-alert
      style="margin-bottom:10px"
      title="请注意：提现涉及到资金安全问题，请务必核实一下信息，确认无误后申请提现，否则，淘小熊概不负责！"
      type="warning"
      show-icon
    ></el-alert>
    <section>
      <header>
        <span></span>用户基本信息
      </header>
      <main v-loading="isloading">
        <img v-if="entity.avatar" :src="entity.avatar" alt @error="entity.avatar=require('../../../assets/datouxiang.png')" />
        <img v-else src="@/assets/datouxiang.png" alt="">

        <el-form size="small" label-width="100px">
          <el-form-item label="注册时间">
            <el-date-picker
              value-format="yyyy-MM-dd"
              type="date"
              placeholder="选择日期"
              v-model="entity.create_time"
              disabled
            ></el-date-picker>
          </el-form-item>
          <el-form-item label="性别">
            <el-radio-group v-model="entity.gender">
              <el-radio label="男" value="男"></el-radio>
              <el-radio label="女" value="女"></el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="年龄">
            <el-input v-model="entity.age" clearable />
          </el-form-item>
          <el-form-item label="QQ">
            <el-input v-model="entity.qq" clearable />
          </el-form-item>
          <!-- <el-form-item label="Email">
            <el-input v-model="entity.email" clearable />
          </el-form-item> -->
          <el-form-item label="婚姻状况">
            <el-radio-group v-model="entity.marriage">
              <el-radio label="未婚" value="未婚"></el-radio>
              <el-radio label="已婚" value="已婚"></el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="教育程度">
            <el-select v-model="entity.education" placeholder="请选择教育程度">
              <el-option label="高中（中专）以下" value="高中（中专）以下"></el-option>
              <el-option label="中专" value="中专"></el-option>
              <el-option label="大专" value="大专"></el-option>
              <el-option label="本科" value="本科"></el-option>
              <el-option label="研究生" value="研究生"></el-option>
              <el-option label="博士" value="博士"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="所属人群">
            <el-select v-model="entity.crowd" placeholder="请选择所属人群">
              <el-option label="上班" value="上班"></el-option>
              <el-option label="待业" value="待业"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="所属地区">
            <el-cascader v-model="bindCity" :options="citys" @change="handleCityChange" clearable />
          </el-form-item>
        </el-form>

        <el-button type="primary" class="submit_btn" size="small" @click="handleSubmit">确认提交</el-button>
      </main>
    </section>
  </div>
</template>
<script>
import citys from "@/assets/citys.js";
import { userInfo } from "@/api/user";

export default {
  name: "basicInfo",
  data() {
    return {
      isloading: false,
      entity: {},
      bindCity: [] // 省市级联
    };
  },
  computed: {
    citys() {
      return citys || [];
    }
  },
  mounted() {
    this.getData();
  },
  methods: {
    async getData() {
      this.isloading = true
      let res = await userInfo();
      if (res && res.error.errno == 200) {
        this.entity = res.data;
        this.bindCity = [res.data.province, res.data.city];
      }
      this.isloading = false
    },
    // 省市级联变化
    handleCityChange(val) {
      console.log(val);
      if (val && val.length > 0) {
        this.entity.province = val[0];
        this.entity.city = val[1] ? val[1] : val[0];
      } else {
        this.entity.province = "";
        this.entity.city = "";
      }
    },
    // 表单提交
    async handleSubmit() {
      let res = await userInfo(this.entity)
      if(res && res.error.errno == 200) {
        this.$message.success('更新成功')
        this.getData();
      }
    }
  }
};
</script>
<style lang="scss" scope>
.basicInfo {
  section {
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
      & > img {
        margin: 20px 0;
        width: 100px;
        height: 100px;
      }
    }
  }

  .el-form-item {
    padding-right: 100px;
    .el-form-item__label {
      font-weight: normal;
      color: #666;
    }
    .el-cascader,
    .el-select,
    .el-input,
    .el-range-editor {
      width: 250px;
    }
  }
}
</style>