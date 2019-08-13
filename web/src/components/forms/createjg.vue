<template>
  <el-card class="create_org" shadow="never">
    <div slot="header" class="clearfix">
      <span>机构管理</span>
    </div>
    <div class="form-wrapper" v-shadow :style="{maxHeight: maxTableHeight + 'px'}">
      <el-form class="editor-form" v-loading="loadData" :inline="false" :model="formInline" :rules="rules" ref="ruleForm" label-width="140px" size="small">
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
          <el-input v-model="formInline.user_total" @input="formInline.user_total = limitNumber(formInline.user_total, 2, 0)" placeholder="请输入可开账户数量"></el-input>
          <div class="annotation">最多可开账户数为99</div>
        </el-form-item>
        <el-form-item prop="notify_url">
          <span slot="label">异步通知地址：</span>
          <el-input v-model="formInline.notify_url" placeholder="请输入异步通知地址"></el-input>
        </el-form-item>
        <el-form-item prop="device_orgs">
          <span slot="label">设备中心机构关联：</span>
          <el-transfer v-model="formInline.device_orgs" :props="{key: 'value'}" :data="devOrgs" target-order="unshift" filterable :titles="['未关联', '已关联']" :render-content="renderFunc"></el-transfer>
          <div class="annotation">此配置用于同步SIM卡与设备机构归属</div>
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
          <el-input v-model="formInline.rebate_value" @input="formInline.rebate_value = limitNumber(formInline.rebate_value, 1, 4)" placeholder="请输入返利比率"></el-input>
          <div class="annotation">值需小于1大于等于0，返利比率 * 充值金额 = 返利金额</div>
        </el-form-item>
        <el-form-item>
          <el-button @click="$router.back()">返回</el-button>
          <el-button type="primary" @click="submitForm('ruleForm')">保存</el-button>
          <el-button type="warning" @click="resetForm('ruleForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
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
      devOrgs: [],
      formInline: {},
      fileList: [], // 文件上传列表
      rules: {
        name: [{
          required: true,
          message: '请输入机构名称',
          trigger: 'blur'
        }, {
          min: 1,
          max: 100,
          message: '机构名必须在1至100个字符之间',
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
        memo: [{
          min: 0,
          max: 255,
          message: '机构备注不能多于255个字符',
          trigger: ['blur', 'change']
        }],
        notify_url: [{
          required: true,
          message: '请输入异步通知地址',
          trigger: 'blur'
        }, {
          validator: this.validatorNotifyUrl,
          trigger: 'blur'
        }],
        device_orgs: [{
          validator: this.validatorTransfer,
          trigger: 'change'
        }],
        email: [{
          validator: this.validatorEmall,
          trigger: ['blur']
        }],
        tel: [{
          validator: this.validatorPhoneNumber,
          trigger: ['blur']
        }],
        rebate_value: [{
          validator: this.validatorRebate,
          trigger: ['blur']
        }]
      },
      emailRule: null,
      telRule: null,
      maxTableHeight: Api.UNITS.maxTableHeight(266),
    }
  },
  mounted() {
    this.isUpdate = this.$route.query.type === 'update'
    this.getDeviceOrgs()
    if (this.isUpdate) {
      this.getData()
    } else {
      this.loadData = false
    }
  },
  methods: {
    getDeviceOrgs() { // 获取机构中心机构列表
      _axios.send({
        method: 'get',
        url: _axios.ajaxAd.getDeviceOrgs,
        done: ((res) => {
          this.devOrgs = res.data || []
          this.devOrgs.forEach((v) => {
            v.label = v.label + `(${v.value})`
          })
          Vue.nextTick(() => {
            this.$refs.ruleForm.clearValidate(['device_orgs'])
          })
        })
      })
    },
    getData() {
      _axios.send({
        method: 'get',
        url: _axios.ajaxAd.getOrgsDetail,
        params: {
          org_id: Api.UNITS.getQuery('org_id')
        },
        done: ((res) => {
          this.loadData = false
          this.formInline = res.data || []
          this.$set(this.formInline, 'device_orgs', this.formInline.device_orgs || [])
          this.$nextTick(() => {
            this.$refs.ruleForm.clearValidate()
          })
        })
      })
    },
    // 提交表单
    submitForm(formName) {
      // 全校验
      this.$refs[formName].validate((valid) => {
        console.log(this.formInline)
        if (valid) {
          // 验证通过
          let url = _axios.ajaxAd.addOrg
          if (this.isUpdate) url = _axios.ajaxAd.updateOrg
          _axios.send({
            method: 'post',
            url,
            data: this.formInline,
            done: ((res) => {
              if (res.status === 400) {
                this.formInline[res.data] = ''
                this.$refs.ruleForm.validateField([res.data])
              } else {
                this.$router.push({ name: 'jgManage' })
                setTimeout(() => {
                  // 加个延迟，动画更流畅
                  this.showMsgBox({
                    type: 'success',
                    message: res.msg || (this.isUpdate ? '修改成功！' : '新增成功！')
                  })
                }, 150)
              }
            })
          })
        } else {
          Api.UNITS.showMsgBox()
          return false
        }
      })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields()
      this.formInline = {}
      if (this.isUpdate) {
        this.getDeviceOrgs()
        this.getData()
      }
      // this.emailRuleChange()
    },
    // emailRuleChange() {
    //   if (this.formInline.email) {
    //     this.emailRule = [{ type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur'] }]
    //     Vue.nextTick(() => {
    //       this.$refs.ruleForm.validateField(['email'])
    //     })
    //   } else {
    //     this.emailRule = null
    //     Vue.nextTick(() => {
    //       this.$refs.ruleForm.clearValidate(['email'])
    //     })
    //   }
    // },
    renderFunc(h, option) {
      // 添加一个title属性，鼠标移到上面的时候能有完整的提示
      return h('span', { attrs: { title: option.label } }, `${option.label}`)
    },
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
    validatorNotifyUrl(rule, value, callback) {
      let exp = new RegExp('(^$)|(^([hH][tT]{2}[pP]:/*|[hH][tT]{2}[pP][sS]:/*|[fF][tT][pP]:/*)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+(\\?{0,1}(([A-Za-z0-9-~]+\\={0,1})([A-Za-z0-9-~]*)\\&{0,1})*)$)')
      if (exp.test(value)) {
        callback()
      } else {
        callback(new Error('异步通知地址格式不正确'))
      }
    },
    validatorTransfer(rule, value = {}, callback) {
      if (value.length > 12) {
        callback(new Error(`关联的机构最多为12个，目前已关联${this.formInline.device_orgs.length}个`))
      } else {
        callback()
      }
    }
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

  .form-wrapper {
    overflow: auto;
  }

  input,
  textarea,
  .el-transfer-panel {
    background: transparent;

    label {
      width: 87%;
    }
  }

  .el-transfer {
    input {
      border: 1px solid #DCDFE6 !important;
    }
  }
}

</style>
