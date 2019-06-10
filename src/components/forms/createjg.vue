<template>
  <el-card class="create_org" shadow="never">
    <div slot="header" class="clearfix">
      <span>机构管理</span>
    </div>
    <el-form v-loading="loadData" :inline="false" :model="formInline" :rules="rules" ref="ruleForm" label-width="120px" size="small" :status-icon="true">
      <el-form-item prop="name">
        <span slot="label">机构名称：</span>
        <el-input v-model="formInline.name" placeholder="请选择机构名称"></el-input>
      </el-form-item>
      <el-form-item prop="parent_id">
        <span slot="label">所属父机构：</span>
        <el-select v-model="formInline.parent_id" filterable placeholder="请选择">
          <el-option v-for="(item, index) in orgs" :key="index" :label="item.label" :value="item.value - 0"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="user_total">
        <span slot="label">可开账户数量：</span>
        <el-input v-model="formInline.user_total" @input="formInline.user_total = limitNumber(formInline.user_total, 6, 0)" placeholder="请输入可开账户数量"></el-input>
      </el-form-item>
      <el-form-item prop="notify_url">
        <span slot="label">异步通知地址：</span>
        <el-input v-model="formInline.notify_url" placeholder="请输入异步通知地址"></el-input>
      </el-form-item>
      <el-form-item prop="memo">
        <span slot="label">机构描述：</span>
        <el-input type="textarea" v-model="formInline.memo" rows="4" placeholder="请输入"></el-input>
      </el-form-item>
      <el-form-item prop="email">
        <span slot="label">负责人邮箱：</span>
        <el-input v-model="formInline.email"></el-input>
      </el-form-item>
      <el-form-item prop="tel">
        <span type="password" slot="label">负责人手机：</span>
        <el-input v-model="formInline.tel"></el-input>
      </el-form-item>
      <el-form-item prop="rebate_value">
        <span slot="label">返利比率：</span>
        <el-input v-model="formInline.rebate_value" @input="formInline.rebate_value = limitNumber(formInline.rebate_value, 1, 3)" placeholder="请输入返利比率"></el-input>
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
import { mapState } from 'vuex'

export default {
  data() {
    return {
      loadData: true,
      isUpdate: false,
      formInline: {},
      fileList: [], // 文件上传列表
      rules: {
        name: [{
          required: true,
          message: '请输入机构名称',
          trigger: 'blur'
        }],
        parent_id: [{
          required: true,
          message: '请选择父机构',
          trigger: 'change'
        }],
        user_total: [{
          required: true,
          message: '请输入开户数量',
          trigger: 'blur'
        }],
        notify_url: [{
          required: true,
          message: '请输入异步通知地址',
          trigger: 'blur'
        }],
        email: [{
          validator: this.validatorEmall,
          trigger: 'blur'
        }],
        tel: [{
          validator: this.validatorPhoneNumber,
          trigger: 'blur'
        }],
        rebate_value: [{
          validator: this.validatorRebate,
          trigger: 'blur'
        }]
      }
    }
  },
  mounted() {
    let isUpdate = this.$route.query.type === 'update'
    if (isUpdate) {
      // 如果是编辑就获取数据再展示
      this.isUpdate = true
      this.getData()
    } else {
      this.loadData = false
    }
  },
  methods: {
    // 提交表单
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          // 验证通过
          if (this.isUpdate) { // 修改
            _axios.send({
              method: 'post',
              url: _axios.ajaxAd.updateOrg,
              data: this.formInline,
              done: ((res) => {
                this.$router.push({ name: 'jgManage' })
                setTimeout(() => {
                  // 加个延迟，动画更流畅
                  this.$message.success(res.msg || '修改成功')
                }, 150)
              })
            })
          } else { // 新增
            _axios.send({
              method: 'post',
              url: _axios.ajaxAd.addOrg,
              data: this.formInline,
              done: ((res) => {
                this.$router.push({ name: 'jgManage' })
                setTimeout(() => {
                  // 加个延迟，动画更流畅
                  this.$message.success(res.msg || '新增成功')
                }, 150)
              })
            })
          }
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
      this.getData()
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
    // 验证数值区间
    validatorRebate(rule, value, callback) {
      if (!value || Api.UNITS.validatorNumMaxandMin(value, 1, 0)) {
        callback()
      } else {
        callback(new Error('返利比率必须大于等于0，小于1'))
      }
    },
    getData() {
      _axios.send({
        method: 'get',
        url: _axios.ajaxAd.getOrgList,
        params: {
          org_id: Api.UNITS.getQuery('org_id')
        },
        done: ((res) => {
          this.loadData = false
          this.formInline = res.data.page.records[0] || []
        })
      })
    }
  },
  computed: {
    ...mapState({
      orgs: 'orgs',
    })
  }
}

</script>
<style lang="scss">
.create_org {
  .el-card__header {
    margin: 0 15px;
  }

  .el-transfer-panel {
    width: 30%;
    min-width: 250px;
  }
}

</style>
