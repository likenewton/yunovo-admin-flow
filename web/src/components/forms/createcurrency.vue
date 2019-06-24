<template>
  <el-card class="batchcreate_container" shadow="never">
    <div slot="header" class="clearfix">
      <span>货币设置</span>
    </div>
    <el-form v-loading="loadData" :inline="false" :model="formInline" :rules="rules" ref="ruleForm" label-width="140px" size="small" :status-icon="true">
      <el-form-item prop="title">
        <span slot="label">货币名称：</span>
        <el-input v-model="formInline.title" placeholder="请输入货币名称"></el-input>
      </el-form-item>
      <el-form-item prop="code">
        <span slot="label">代码：</span>
        <el-input v-model="formInline.code" placeholder="请输入ISO代码"></el-input>
        <div class="annotation">如果这是您的默认货币请不要改变(必须是有效的 <a class="text_primary" href="http://www.xe.com/iso4217.php" target="blank">ISO代码</a>)</div>
      </el-form-item>
      <el-form-item prop="symbol_left">
        <span slot="label">左符号：</span>
        <el-input v-model="formInline.symbol_left" placeholder="请输入货币左边符号"></el-input>
      </el-form-item>
      <el-form-item prop="symbol_right">
        <span slot="label">右符号：</span>
        <el-input v-model="formInline.symbol_right" placeholder="请输入货币右边符号"></el-input>
      </el-form-item>
      <el-form-item prop="decimal_place">
        <span slot="label">小数位：</span>
        <el-input v-model="formInline.decimal_place" @input="formInline.decimal_place = limitNumber(formInline.decimal_place, 1, 0)" placeholder="请输入货币小数位"></el-input>
      </el-form-item>
      <el-form-item prop="value">
        <span slot="label">汇率：</span>
        <el-input v-model="formInline.value" @input="formInline.value = limitNumber(formInline.value, 4, 4)" placeholder='请输入汇率'></el-input>
        <div class="annotation">如果这是您的默认货币，请将它设置为1.0</div>
      </el-form-item>
      <el-form-item prop="status">
        <span slot="label">状态：</span>
        <el-select v-model="formInline.status" placeholder="请选择">
          <el-option v-for="(item, index) in statusSelect" :key="index" :label="item.label" :value="item.value"></el-option>
        </el-select>
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
import { mapState } from 'vuex'

export default {
  data() {
    return {
      loadData: true,
      isUpdate: true,
      formInline: {
        currency_id: Api.UNITS.getQuery('currency_id')
      },
      rules: {
        title: [{
          required: true,
          message: '请输入货币名称',
          trigger: 'blur'
        }],
        code: [{
          required: true,
          message: '请输入代码',
          trigger: 'blur'
        }, {
          min: 3,
          max: 3,
          message: '货币代码必须是3个字符',
          trigger: 'blur'
        }],
        status: [{
          required: true,
          message: '请选择启用状态',
          trigger: 'change'
        }],
        decimal_place: [{
          required: true,
          message: '请输入货币小数位',
          trigger: 'blur'
        }]
      }
    }
  },
  mounted() {
    this.isUpdate = this.$route.query.type === 'update'
    if (this.isUpdate) {
      this.getData()
    } else {
      this.loadData = false
    }
  },
  methods: {
    getData() {
      this.loadData = true
      _axios.send({
        method: 'get',
        url: _axios.ajaxAd.getCurrencyDetail,
        params: {
          currency_id: Api.UNITS.getQuery('currency_id')
        },
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
              url: _axios.ajaxAd.updateCurrency,
              data: this.formInline,
              done: ((res) => {
                this.$router.push({ name: 'currencyset' })
                setTimeout(() => {
                  this.$message.success(res.msg || '操作成功')
                }, 150)
              })
            })
          } else {
            _axios.send({
              method: 'post',
              url: _axios.ajaxAd.addCurrency,
              data: this.formInline,
              done: ((res) => {
                this.$router.push({ name: 'currencyset' })
                setTimeout(() => {
                  this.$message.success(res.msg || '操作成功')
                }, 150)
              })
            })
          }
        } else {
          Api.UNITS.showMsgBox()
          return false
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
