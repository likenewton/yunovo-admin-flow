<template>
  <el-card class="batchcreate_container" shadow="never">
    <div slot="header" class="clearfix">
      <span>货币设置</span>
    </div>
    <el-form v-loading="isLoadData" :inline="false" :model="formInline" :rules="rules" ref="ruleForm" label-width="140px" size="small" :status-icon="true">
      <el-form-item prop="currency_name">
        <span slot="label">货币名称：</span>
        <el-input v-model="formInline.currency_name" placeholder="请输入货币名称"></el-input>
      </el-form-item>      
      <el-form-item prop="code">
        <span slot="label">代码：</span>
        <el-input v-model="formInline.code" placeholder="请输入ISO代码"></el-input>
        <div class="annotation">如果这是您的默认货币请不要改变(必须是有效的<el-button type="text" @click="iso">ISO代码</el-button>)</div>
      </el-form-item>
      <el-form-item prop="left_symbel">
        <span slot="label">左符号：</span>
        <el-input v-model="formInline.left_symbel" placeholder="请输入货币左边符号"></el-input>
      </el-form-item>
      <el-form-item prop="right_symbel">
        <span slot="label">右符号：</span>
        <el-input v-model="formInline.right_symbel" placeholder="请输入货币右边符号"></el-input>
      </el-form-item>
      <el-form-item prop="fixed_count">
        <span slot="label">小数位：</span>
        <el-input v-model.number="formInline.fixed_count" placeholder="请输入货币小数位"></el-input>
      </el-form-item>
      <el-form-item prop="rate">
        <span slot="label">汇率：</span>
        <el-input v-model="formInline.rate" placeholder='请输入汇率'></el-input>
        <div class="annotation">如果这是您的默认货币，请将它设置为1.0</div>
      </el-form-item>
      <el-form-item prop="status">
        <span slot="label">状态：</span>
        <el-select v-model="formInline.status" placeholder="请选择">
          <el-option label="启用" :value="0"></el-option>
          <el-option label="停用" :value="1"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="$router.back()">返回</el-button>
        <el-button type="primary" @click="submitForm('ruleForm')">保存</el-button>
        <el-button v-if="isCreate" type="warning" @click="resetForm('ruleForm')">重置</el-button>
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
      isCreate: true,
      formInline: {
        fixed_count: 2,
        status: 0
      },
      rules: {
        currency_name: [{
          required: true,
          message: '请输入货币名称',
          trigger: 'blur'
        }],
        code: [{
          required: true,
          message: '请输入代码',
          trigger: 'blur'
        }],
        status: [{
          required: true,
          message: '请选择启用状态',
          trigger: 'change'
        }],
        fixed_count: [{
          required: true,
          message: '请输入货币小数位',
          trigger: 'blur'
        }, {
          type: 'number',
          message: '输入的必须为数字',
          trigger: 'blur'
        }]
      }
    }
  },
  mounted() {
    let isUpdate = this.$route.query.type === 'update'
    if (isUpdate) {
      this.isCreate = false
      this.getData()
    } else {
      this.isLoadData = false
    }
  },
  methods: {
    getData() {
      // 如果是编辑就获取数据再展示
      setTimeout(() => {
        this.formInline = {
          currency_name: 'Chinese RMB',
          code: 'CNY',
          left_symbel: '¥',
          fixed_count: 2,
          rate: '1',
          status: 0
        }
        this.isLoadData = false
      }, 1000)
    },
    // 连接iso 货币代码标准
    iso() {
      window.open('http://www.xe.com/iso4217.php')
    },
    // 提交表单
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          // 验证通过
          console.log(this.formInline)
        } else {
          console.log('valid')
          return false
        }
      });
    },
    // 重置表单
    resetForm(formName) {
      // resetFields 只能重置需要验证的值
      this.$refs[formName].resetFields()
    },
    limitNumber: Api.UNITS.limitNumber
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
