<template>
  <el-card class="batchcreate_container" shadow="never">
    <div slot="header" class="clearfix">
      <span>支付宝支付</span>
    </div>
    <el-form v-loading="isLoadData" :inline="false" :model="formInline" :rules="rules" ref="ruleForm" label-width="140px" size="small" :status-icon="true">
      <el-form-item prop="sign_account">
        <span slot="label">签约账号：</span>
        <el-input v-model="formInline.sign_account"></el-input>
      </el-form-item>
      <el-form-item prop="shroff_account">
        <span slot="label">收款支付宝账号：</span>
        <el-input v-model="formInline.shroff_account"></el-input>
      </el-form-item>
      <el-form-item prop="pay_secretkey">
        <span slot="label">商户支付密钥：</span>
        <el-input v-model="formInline.pay_secretkey"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="$router.back()">返回</el-button>
        <el-button type="primary" @click="submitForm('ruleForm')">保存</el-button>
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
      formInline: {},
      rules: {
        sign_account: [{
          required: true,
          message: '请输入签约账号',
          trigger: 'blur'
        }],
        shroff_account: [{
          required: true,
          message: '请输入收款支付宝账号',
          trigger: 'blur'
        }],        
        pay_secretkey: [{
          required: true,
          message: '请输入商户支付密钥',
          trigger: 'blur'
        }]
      }
    }
  },
  mounted() {
    this.getData()
  },
  methods: {
    // 获取数据
    getData() {
      this.isLoadData = true
      setTimeout(() => {
        this.formInline = {
          sign_account: '2088421210458395',
          shroff_account: 'liujinwei@yunovo.cn',
          pay_secretkey: '67murmgj5taqvwljcmakypby3iama5fd'
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
    }
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
