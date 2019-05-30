<template>
  <el-card class="batchcreate_container" shadow="never">
    <div slot="header" class="clearfix">
      <span>机构管理设置</span>
    </div>
    <el-form v-loading="isLoadData" :inline="false" :model="formInline" :rules="rules" ref="ruleForm" label-width="120px" size="small" :status-icon="true">
      <el-form-item prop="jg_name">
        <span slot="label">机构名称：</span>
        <el-input v-model="formInline.jg_name" placeholder="请选择机构名称"></el-input>
      </el-form-item>
      <el-form-item prop="parent_jgname">
        <span slot="label">所属父机构：</span>
        <el-select v-model="formInline.parent_jgname" placeholder="请选择">
          <el-option label="机构1" :value="0"></el-option>
          <el-option label="机构2" :value="1"></el-option>
          <el-option label="机构3" :value="2"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="acc_count">
        <span slot="label">可开账户数量：</span>
        <el-input v-model="formInline.acc_count" @input="formInline.acc_count = limitNumber(formInline.acc_count, 6, 0)" placeholder="请输入可开账户数量"></el-input>
      </el-form-item>
      <el-form-item prop="inform_addr">
        <span slot="label">异步通知地址：</span>
        <el-input v-model="formInline.inform_addr" placeholder="请输入异步通知地址"></el-input>
      </el-form-item>
      <el-form-item prop="jg_desc">
        <span slot="label">机构描述：</span>
        <el-input type="textarea" v-model="formInline.jg_desc" rows="4" placeholder="请输入"></el-input>
      </el-form-item>
      <el-form-item prop="e_mail">
        <span slot="label">负责人邮箱：</span>
        <el-input v-model="formInline.e_mail"></el-input>
      </el-form-item>
      <el-form-item prop="phone_number">
        <span type="password" slot="label">负责人手机：</span>
        <el-input v-model="formInline.phone_number"></el-input>
      </el-form-item>
      <el-form-item prop="repay_rate">
        <span slot="label">返利比率：</span>
        <el-input v-model="formInline.repay_rate" placeholder="请输入返利比率"></el-input>
        <div class="annotation">值需小于1大于等于0，返利比率 * 充值金额 = 返利金额</div>
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
      isLoadData: true,
      formInline: {},
      fileList: [], // 文件上传列表
      rules: {
        jg_name: [{
          required: true,
          message: '请输入机构名称',
          trigger: 'blur'
        }],
        parent_jgname: [{
          required: true,
          message: '请选择父机构',
          trigger: 'change'
        }],
        acc_count: [{
          required: true,
          message: '请输入开户数量',
          trigger: 'blur'
        }],
        inform_addr: [{
          required: true,
          message: '请输入异步通知地址',
          trigger: 'blur'
        }],
        e_mail: [{
          validator: this.validatorEmall,
          trigger: 'blur'
        }],
        phone_number: [{
          validator: this.validatorPhoneNumber,
          trigger: 'blur'
        }]
      }
    }
  },
  mounted() {
    let isUpdate = this.$route.query.type === 'update'
    if (isUpdate) {
      // 如果是编辑就获取数据再展示
      setTimeout(() => {
        this.formInline = {
          jg_name: '卡仕特-西格玛',
          parent_jgname: 1,
          acc_count: 4,
          e_mail: '52875440@qq.com',
          phone_number: '13286320200',
          repay_rate: 0,
          jg_desc: '深圳途乐车联网有限公司',
          inform_addr: '深圳途乐车联网有限公司'
        }
        this.isLoadData = false
      }, 1000)
    } else {
      // 如果是新增页面要加上密码验证（必须）
      this.isLoadData = false
    }

  },
  methods: {
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
    limitNumber: Api.UNITS.limitNumber,
    // 验证邮箱地址是否正确
    validatorEmall(rule, value, callback) {
      if (!value || Api.UNITS.validatorEmall(value)) {
        callback()
      } else {
        callback(new Error('邮箱格式不正确'))
      }
    },
    // 验证手机格式是否正确
    validatorPhoneNumber(rule, value, callback) {
      if (!value || Api.UNITS.validatorPhoneNumber(value)) {
        callback()
      } else {
        callback(new Error('手机格式不正确'))
      }
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
