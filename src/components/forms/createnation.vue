<template>
  <el-card class="batchcreate_container" shadow="never">
    <div slot="header" class="clearfix">
      <span>国家区域</span>
    </div>
    <el-form v-loading="isLoadData" :inline="false" :model="formInline" :rules="rules" ref="ruleForm" label-width="140px" size="small" :status-icon="true">
      <el-form-item prop="area_name">
        <span slot="label">区域名称：</span>
        <el-input v-model="formInline.area_name"></el-input>
      </el-form-item>
      <el-form-item prop="postal_code">
        <span slot="label">区域邮政：</span>
        <el-input v-model="formInline.postal_code"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="$router.back()">返回</el-button>
        <el-button type="primary" @click="submitForm('ruleForm')">保存</el-button>
        <el-button v-if="isShowReset" type="warning" @click="resetForm('ruleForm')">重置</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>
<script>
import Api from 'assets/js/api.js'

export default {
  data() {
    return {
      isLoadData: true,
      isShowReset: true,
      formInline: {},
      rules: {
        area_name: [{
          required: true,
          message: '请输入区域名称',
          trigger: 'blur'
        }],
        postal_code: [{
          required: true,
          message: '请输入区域编号',
          trigger: 'blur'
        }]
      }
    }
  },
  mounted() {
    let isUpdate = this.$route.query.type === 'update'
    if (isUpdate) {
      this.getData()
      this.isShowReset = false
    } else {
      this.isLoadData = false
    }
  },
  methods: {
    // 获取数据
    getData() {
      this.isLoadData = true
      setTimeout(() => {
        this.formInline = {
          area_name: '蒸湘区',
          postal_code: '430408',
        }
        this.isLoadData = false
      }, 1000)
    },
    // 提交表单
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          // 验证通过
          console.log(this.formInline)
        } else {
          console.log('valid')
          return false;
        }
      });
    },
    // 重置表单
    resetForm(formName) {
      // resetFields 只能重置需要验证的值
      this.$refs[formName].resetFields()
    },
  }
}

</script>
<style lang="scss">
.batchcreate_container {
  .el-card__header {
    margin: 0 15px;
  }

  .el-transfer-panel {
    width: 30%;
    min-width: 250px;
  }
}

</style>
