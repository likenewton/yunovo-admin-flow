<template>
  <el-card class="batchcreate_container" shadow="never">
    <div slot="header" class="clearfix">
      <span>微信支付</span>
    </div>
    <el-form v-loading="isLoadData" :inline="false" :model="formInline" :rules="rules" ref="ruleForm" label-width="190px" size="small" :status-icon="true">
      <el-form-item prop="pay_appid">
        <span slot="label">支付APPID：</span>
        <el-input v-model="formInline.pay_appid"></el-input>
      </el-form-item>
      <el-form-item prop="mer_no">
        <span slot="label">微信支付商户号：</span>
        <el-input v-model="formInline.mer_no"></el-input>
      </el-form-item>
      <el-form-item prop="pay_secretkey">
        <span slot="label">商户支付密钥：</span>
        <el-input v-model="formInline.pay_secretkey"></el-input>
      </el-form-item>
      <el-form-item prop="wechart_secret">
        <span slot="label">微信支付公众号SECERT：</span>
        <el-input v-model="formInline.wechart_secret"></el-input>
      </el-form-item>
      <el-form-item prop="cert_abpath">
        <span slot="label">CERT证书绝对路径：</span>
        <el-input v-model="formInline.cert_abpath"></el-input>
      </el-form-item>
      <el-form-item prop="key_abpath">
        <span slot="label">KEY证书绝对路径：</span>
        <el-input v-model="formInline.key_abpath"></el-input>
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
        pay_appid: [{
          required: true,
          message: '请输入支付APPID',
          trigger: 'blur'
        }],
        mer_no: [{
          required: true,
          message: '请输入微信支付商户号',
          trigger: 'blur'
        }],
        pay_secretkey: [{
          required: true,
          message: '请输入商户支付密钥',
          trigger: 'blur'
        }],
        wechart_secret: [{
          required: true,
          message: '请输入微信支付公众号SECERT',
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
          pay_appid: 'wxff00d74721c04ed0',
          mer_no: '1354285702',
          pay_secretkey: 'a6faa66ed34e0ebcb82fb3690eadd5c1',
          wechart_secret: 'ed6a5919505b0016d27bc24c5cc85ff9'
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
