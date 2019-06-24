<template>
  <el-card class="batchcreate_container" shadow="never">
    <div slot="header" class="clearfix">
      <span>微信支付</span>
    </div>
    <el-form v-loading="loadData" :inline="false" :model="formInline" :rules="rules" ref="ruleForm" label-width="190px" size="small" :status-icon="true">
      <el-form-item prop="wxpay_appid">
        <span slot="label">支付APPID：</span>
        <el-input v-model="formInline.wxpay_appid"></el-input>
      </el-form-item>
      <el-form-item prop="wxpay_mchid">
        <span slot="label">微信支付商户号：</span>
        <el-input v-model="formInline.wxpay_mchid"></el-input>
      </el-form-item>
      <el-form-item prop="wxpay_mchkey">
        <span slot="label">商户支付密钥：</span>
        <el-input v-model="formInline.wxpay_mchkey"></el-input>
      </el-form-item>
      <el-form-item prop="wxpay_appsecret">
        <span slot="label">微信支付公众号SECERT：</span>
        <el-input v-model="formInline.wxpay_appsecret"></el-input>
      </el-form-item>
      <el-form-item prop="wxpay_sslcert_path">
        <span slot="label">CERT证书绝对路径：</span>
        <el-input v-model="formInline.wxpay_sslcert_path"></el-input>
      </el-form-item>
      <el-form-item prop="wxpay_sslkey_path">
        <span slot="label">KEY证书绝对路径：</span>
        <el-input v-model="formInline.wxpay_sslkey_path"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="$router.back()">返回</el-button>
        <el-button type="primary" @click="submitForm('ruleForm')">保存</el-button>
        <el-button type="warning" @click="resetForm('ruleForm')">重置</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>
<script>
import Api from 'assets/js/api.js'

export default {
  data() {
    return {
      loadData: true,
      isUpdate: false,
      formInline: {
        type: Api.UNITS.getQuery('pay')
      },
      rules: {
        wxpay_appid: [{
          required: true,
          message: '请输入支付APPID',
          trigger: 'blur'
        }, {
          max: 500,
          message: '不能超过500个字符',
          trigger: 'blur'
        }],
        wxpay_mchid: [{
          required: true,
          message: '请输入微信支付商户号',
          trigger: 'blur'
        }, {
          max: 500,
          message: '不能超过500个字符',
          trigger: 'blur'
        }],
        wxpay_mchkey: [{
          required: true,
          message: '请输入商户支付密钥',
          trigger: 'blur'
        }, {
          max: 500,
          message: '不能超过500个字符',
          trigger: 'blur'
        }],
        wxpay_appsecret: [{
          required: true,
          message: '请输入微信支付公众号SECERT',
          trigger: 'blur'
        }, {
          max: 500,
          message: '不能超过500个字符',
          trigger: 'blur'
        }]
      }
    }
  },
  mounted() {
    this.isUpdate = Api.UNITS.getQuery('type') === 'update'
    if (this.isUpdate) {
      this.getData()
    } else {
      this.loadData = false
    }
  },
  methods: {
    // 获取数据
    getData() {
      this.loadData = true
      _axios.send({
        method: 'get',
        url: _axios.ajaxAd.getPayDetail,
        params: { type: Api.UNITS.getQuery('pay') },
        done: ((res) => {
          this.formInline = res.data
          this.loadData = false
        })
      })
    },
    // 提交表单
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          // 验证通过
          if (this.isUpdate) {
            _axios.send({
              method: 'post',
              url: _axios.ajaxAd.updatePay,
              data: Object.assign({ type: Api.UNITS.getQuery('pay') }, this.formInline),
              done: ((res) => {
                this.$router.push({ name: 'paySet' })
                setTimeout(() => {
                  this.$message.success(res.msg || '操作成功')
                }, 150)
              })
            })
          }
        } else {
          Api.UNITS.showMsgBox()
          return false;
        }
      })
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
