<template>
  <el-card class="batchcreate_container" shadow="never">
    <div slot="header" class="clearfix">
      <span>充值套餐设置</span>
    </div>
    <el-form v-loading="loadData" :inline="false" :model="formInline" :rules="rules" ref="ruleForm" label-width="160px" size="small" :status-icon="true">
      <el-form-item prop="jg_name">
        <span slot="label">机构名称：</span>
        <el-select v-model="formInline.jg_name" placeholder="请选择">
          <el-option label="机构1" :value="0"></el-option>
          <el-option label="机构2" :value="1"></el-option>
          <el-option label="机构3" :value="2"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="warning_flow">
        <span slot="label">预警流量：</span>
        <el-input v-model="formInline.warning_flow" @input="formInline.warning_flow = limitNumber(formInline.warning_flow)" placeholder="请输入预警流量"></el-input>
        <div class="annotation">单位：M</div>
      </el-form-item>
      <el-form-item prop="blow_tpl">
        <span slot="label">低于预警值通知模板：</span>
        <el-input type="textarea" v-model="formInline.blow_tpl" rows="4"></el-input>
      </el-form-item>
      <el-form-item prop="none_tpl">
        <span slot="label">低于预警值通知模板：</span>
        <el-input type="textarea" v-model="formInline.none_tpl" rows="4"></el-input>
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
      loadData: false,
      formInline: {
        blow_tpl: '尊敬的客户，您的流量已低于60M，为了不影响设备的正常使用请到微信端为绑定的此车机充值。',
        none_tpl: '尊敬的客户，您的流量已用完，为了不影响您的正常使用，请及时充值。',
      },
      fileList: [], // 文件上传列表
      rules: {
        jg_name: [{
          required: true,
          message: '请选择机构名称',
        }],
        warning_flow: [{
          required: true,
          message: '请输入预警流量',
          trigger: 'blur'
        }]
      }
    }
  },
  mounted() {
    if (this.$route.query.id) {
      // 存在id即为编辑
      this.getData()
    } else {
      // 否则为创建
      this.loadData = false
    }
  },
  methods: {
    // 提交表单
    submitForm(formName) {
      console.log(this.formInline)
      this.$refs[formName].validate((valid) => {
        if (valid) {
          // 验证通过
        } else {
          Api.UNITS.showMsgBox()
          return false;
        }
      });
    },
    // 重置表单
    resetForm(formName) {
      // resetFields 只能重置需要验证的值
      this.$refs[formName].resetFields()
      this.formInline = {}
    },
    getData() {
      this.loadData = true
      setTimeout(() => {
        this.formInline = {
          id: 1,
          jg_name: '卡仕特-西格玛',
          warning_flow: 100,
          blow_tpl: '尊敬的客户，您的流量已低于100M，为了不影响您的正常使用，请及时充值。',
          none_tpl: '尊敬的客户，您的流量已用完，为了不影响您的正常使用，请及时充值。',
          create_p: 'Newton',
          chg_p: '樱木花道',
        }
        this.loadData = false
      }, 1000)
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
