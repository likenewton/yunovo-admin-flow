<template>
  <el-card class="alipay" shadow="never">
    <div slot="header" class="clearfix">
      <span>支付宝支付</span>
    </div>
    <el-form class="editor-form" v-loading="loadData" :inline="false" :model="formInline" :rules="rules" ref="ruleForm" label-width="140px" size="small" :status-icon="true">
      <el-form-item prop="alipay_partner">
        <span slot="label">签约账号：</span>
        <el-input v-model="formInline.alipay_partner"></el-input>
      </el-form-item>
      <el-form-item prop="alipay_seller_id">
        <span slot="label">收款支付宝账号：</span>
        <el-input v-model="formInline.alipay_seller_id"></el-input>
      </el-form-item>
      <el-form-item prop="alipay_key">
        <span slot="label">商户支付密钥：</span>
        <el-input v-model="formInline.alipay_key"></el-input>
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
        alipay_partner: [{
          required: true,
          message: '请输入签约账号',
          trigger: 'blur'
        }, {
          max: 500,
          message: '不能超过500个字符',
          trigger: 'blur'
        }],
        alipay_seller_id: [{
          required: true,
          message: '请输入收款支付宝账号',
          trigger: 'blur'
        }, {
          max: 500,
          message: '不能超过500个字符',
          trigger: 'blur'
        }],
        alipay_key: [{
          required: true,
          message: '请输入商户支付密钥',
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
                  this.showMsgBox({
                    type: 'success',
                    message: res.msg || '操作成功！'
                  })
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
.alipay {
  .el-card__header {
    margin: 0 15px;
  }

  .el-transfer-panel {
    width: 30%;
    min-width: 250px;
  }
}

</style>
