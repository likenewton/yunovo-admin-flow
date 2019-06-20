<template>
  <el-card class="batchcreate_container" shadow="never">
    <div slot="header" class="clearfix">
      <span>用户管理设置</span>
    </div>
    <el-form v-loading="isLoadData" :inline="false" :model="formInline" :rules="rules" ref="ruleForm" label-width="120px" size="small" :status-icon="true">
      <el-form-item prop="jg_name">
        <span slot="label">所属机构：</span>
        <el-select v-model="formInline.jg_name" placeholder="请选择">
          <el-option label="机构1" :value="0"></el-option>
          <el-option label="机构2" :value="1"></el-option>
          <el-option label="机构3" :value="2"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="user_name">
        <span slot="label">用户账号：</span>
        <el-input v-model="formInline.user_name"></el-input>
      </el-form-item>
      <el-form-item prop="real_name">
        <span slot="label">真实姓名：</span>
        <el-input v-model="formInline.real_name"></el-input>
      </el-form-item>
      <el-form-item prop="nick_name">
        <span slot="label">用户昵称：</span>
        <el-input v-model="formInline.nick_name"></el-input>
      </el-form-item>
      <el-form-item prop="user_lang">
        <span slot="label">用户语言：</span>
        <el-select v-model="formInline.user_lang" placeholder="请选择">
          <el-option label="Engligh" :value="0"></el-option>
          <el-option label="简体中文" :value="1"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="user_group">
        <span slot="label">用户群组：</span>
        <el-select v-model="formInline.user_group" placeholder="请选择">
          <el-option label="群组1" :value="0"></el-option>
          <el-option label="群组2" :value="1"></el-option>
          <el-option label="群组3" :value="2"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="user_pass1">
        <span slot="label">设置密码：</span>
        <el-input type="password" v-model="formInline.user_pass1"></el-input>
      </el-form-item>
      <el-form-item prop="user_pass2">
        <span slot="label">确认密码：</span>
        <el-input type="password" v-model="formInline.user_pass2"></el-input>
      </el-form-item>
      <el-form-item prop="e_mail">
        <span slot="label">联系邮箱：</span>
        <el-input v-model="formInline.e_mail"></el-input>
      </el-form-item>
      <el-form-item prop="phone_number">
        <span type="password" slot="label">联系电话：</span>
        <el-input v-model="formInline.phone_number"></el-input>
      </el-form-item>
      <el-form-item prop="acc_status">
        <span slot="label">账号状态：</span>
        <el-select v-model="formInline.acc_status" placeholder="请选择">
          <el-option label="启用" :value="0"></el-option>
          <el-option label="停用" :value="1"></el-option>
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
        user_name: [{
          required: true,
          message: '请输入用户账号',
          trigger: 'blur'
        }],
        real_name: [{
          required: true,
          message: '请输入真实姓名',
          trigger: 'blur'
        }],
        user_pass1: [{
          required: false,
          message: '请输入用户密码',
          trigger: 'blur'
        }, {
          validator: this.validatorPassword,
          trigger: 'blur'
        }],
        user_pass2: [{
          required: false,
          message: '请输入确认密码',
          trigger: 'blur'
        }, {
          validator: this.validatorPassword2,
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
          user_name: 'liudong',
          real_name: '柳栋',
          user_group: 1,
          user_lang: 1,
          jg_name: '卡仕特-西格玛',
          acc_status: 1
        }
        this.isLoadData = false
      }, 1000)
    } else {
      // 如果是新增页面要加上密码验证（必须）
      this.rules.user_pass1[0].required = true
      this.rules.user_pass2[0].required = true
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
    },
    validatorPassword(rule, value, callback) {
      if (!value || Api.UNITS.validatorPassword(value)) {
        callback()
      } else {
        callback(new Error('密码格式不正确, 必须包含字母、数字和特殊字符'))
      }
    },
    validatorPassword2(rule, value, callback) {
      if (value === this.formInline.user_pass1) {
        callback()
      } else {
        callback(new Error('两次密码输入不一致'))
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
