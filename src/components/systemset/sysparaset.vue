<template>
  <div>
    <el-card class="system_set" shadow="never">
      <el-tabs @tab-click="changeTab">
        <!-- 编辑项目 -->
        <el-tab-pane>
          <span slot="label"></i>编辑项目</span>
          <el-form v-loading="loadData" :inline="false" :model="formInline_0" :rules="rules_0" ref="ruleForm_0" label-width="140px" size="small" :status-icon="true">
            <el-form-item prop="sys_name">
              <span slot="label">系统名称：</span>
              <el-input v-model="formInline_0.sys_name"></el-input>
            </el-form-item>
            <el-form-item prop="sys_master">
              <span slot="label">系统拥有者：</span>
              <el-input v-model="formInline_0.sys_master"></el-input>
            </el-form-item>
            <el-form-item prop="connect_addr">
              <span slot="label">联系地址：</span>
              <el-input type="textarea" v-model="formInline_0.connect_addr" rows="4"></el-input>
            </el-form-item>
            <el-form-item prop="e_mail">
              <span slot="label">电子邮箱：</span>
              <el-input v-model="formInline_0.e_mail"></el-input>
            </el-form-item>
            <el-form-item prop="connect_tele">
              <span slot="label">联系电话：</span>
              <el-input v-model="formInline_0.connect_tele"></el-input>
            </el-form-item>
            <el-form-item prop="fax_no">
              <span slot="label">传真号码：</span>
              <el-input v-model="formInline_0.fax_no"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitForm('ruleForm_0')">保存</el-button>
              <el-button type="warning" @click="resetForm('ruleForm_0')">重置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <!-- 系统设置 -->
        <el-tab-pane>
          <span slot="label">系统设置</span>
          <el-form v-loading="loadData" :inline="false" :model="formInline_1" :rules="rules_1" ref="ruleForm_1" label-width="140px" size="small" :status-icon="true">
            <el-form-item prop="home_title">
              <span slot="label">首页标题：</span>
              <el-input v-model="formInline_1.home_title"></el-input>
            </el-form-item>
            <el-form-item prop="seo_set">
              <span slot="label">SEO设置：</span>
              <el-input type="textarea" v-model="formInline_1.seo_set" rows="4"></el-input>
            </el-form-item>
            <el-form-item prop="template_set">
              <span slot="label">模板设置：</span>
              <el-select v-model="formInline_1.template_set" placeholder="请选择">
                <el-option label="default" :value="0"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item style="width: auto" class="inline">
              <span slot="label">模板预览：</span>
              <el-card :body-style="{ padding: '10px' }">
                <img src="../../assets/images/default.png" height="172" width="150">
              </el-card>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitForm('ruleForm_1')">保存</el-button>
              <el-button type="warning" @click="resetForm('ruleForm_1')">重置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <!-- 本地化设置 -->
        <el-tab-pane>
          <span slot="label">本地化设置</span>
          <el-form v-loading="loadData" :inline="false" :model="formInline_2" :rules="rules_2" ref="ruleForm_2" label-width="220px" size="small" :status-icon="true">
            <el-form-item prop="front_lang">
              <span slot="label">前台语言：</span>
              <el-select v-model="formInline_2.front_lang" placeholder="请选择">
                <el-option label="English" :value="0"></el-option>
                <el-option label="简体中文" :value="1"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item prop="end_lang">
              <span slot="label">后台语言：</span>
              <el-select v-model="formInline_2.end_lang" placeholder="请选择">
                <el-option label="English" :value="0"></el-option>
                <el-option label="简体中文" :value="1"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item prop="currency_set">
              <span slot="label">货币设置：</span>
              <el-select v-model="formInline_2.currency_set" placeholder="请选择">
                <el-option label="Chinese RMB" :value="0"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item prop="currency_update">
              <span slot="label">货币更新：</span>
              <el-radio v-model="formInline_2.currency_update" :label="0">是</el-radio>
              <el-radio v-model="formInline_2.currency_update" :label="1">否</el-radio>
              <div style="display: inline-block;color: #adadad;font-size: 12px">设置是否每天自动更新货币！</div>
            </el-form-item>
            <el-form-item prop="front_pro_num">
              <span slot="label">每页默认产品数量(前台管理)：</span>
              <el-input v-model.number="formInline_2.front_pro_num" type="text" placeholder="确定在产品分类中每个网页上显示多少产品（产品，种类等）"></el-input>
            </el-form-item>
            <el-form-item prop="end_pro_num">
              <span slot="label">每页默认产品数量(后台管理)：</span>
              <el-input v-model.number="formInline_2.end_pro_num" type="text" placeholder="确定每页显示多少管理项目（订单，客户等）"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitForm('ruleForm_2')">保存</el-button>
              <el-button type="warning" @click="resetForm('ruleForm_2')">重置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <!-- 邮件协议 -->
        <el-tab-pane>
          <span slot="label">邮件协议</span>
        </el-tab-pane>
        <!-- 服务器设置 -->
        <el-tab-pane>
          <span slot="label">服务器设置</span>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>
<script>
import Api from 'assets/js/api.js'

export default {
  data() {
    return {
      loadData: true, // 是否显示加载动画
      tabIndex: '0', // 当前激活的tab的下标值
      formInline_0: {},
      formInline_1: {},
      formInline_2: {},
      formInline_3: {},
      formInline_4: {},
      rules_0: {
        sys_name: [{
          required: true,
          message: '请输入系统名称',
          trigger: 'blur'
        }],
        sys_master: [{
          required: true,
          message: '请输入系统拥有者',
          trigger: 'blur'
        }],
        connect_addr: [{
          required: true,
          message: '请输入联系地址',
          trigger: 'blur'
        }],
        e_mail: [{
          required: true,
          message: '请输入电子邮箱',
          trigger: 'blur'
        }, {
          validator: this.validatorEmall,
          trigger: 'blur'
        }],
        connect_tele: [{
          required: true,
          message: '请输入联系电话',
          trigger: 'blur'
        }, {
          validator: this.validatorPhoneNumber,
          trigger: 'blur'
        }]
      },
      rules_1: {
        home_title: [{
          required: true,
          message: '请输入首页标题',
          trigger: 'blur'
        }],
        template_set: [{
          required: true,
          message: '请选择模板',
          trigger: 'change'
        }]
      },
      rules_2: {
        front_pro_num: [{
          required: true,
          message: '请输入产品数量',
          trigger: 'blur'
        }, {
          type: 'number',
          message: '必须为整数',
          trigger: 'blur'
        }],
        end_pro_num: [{
          required: true,
          message: '请输入产品数量',
          trigger: 'blur'
        }, {
          type: 'number',
          message: '必须为整数',
          trigger: 'blur'
        }]
      },
      rules_3: {},
      rules_4: {}
    }
  },
  mounted() {
    this.changeTab({ index: '0' })
  },
  methods: {
    changeTab(para) {
      this.tabIndex = para.index
      // 如果没有数据就加载数据
      if (Object.keys(this[`formInline_${this.tabIndex}`]).length === 0) {
        this.getData()
      } else {
        this.loadData = false
      }
    },
    getData() {
      this.loadData = true
      let tabIndex = this.tabIndex
      if (tabIndex === '0') {
        setTimeout(() => {
          this.formInline_0 = {
            sys_name: '云智易联流量平台 - ',
            sys_master: '深圳市云智易联科技有限公司',
            connect_addr: '深圳市南山科技园科苑北广志道博讯大厦2层',
            e_mail: 'zhangsl@yunovo.cn',
            connect_tele: '13048835581',
            fax_no: '0755-86603626'
          }
          this.loadData = false
        }, 1000)
      } else if (tabIndex === '1') {
        setTimeout(() => {
          this.formInline_1 = {
            home_title: '云智易联－流量运营平台',
            seo_set: '云智易联－流量运营平台',
            template_set: 0
          }
          this.loadData = false
        }, 1000)
      } else if (tabIndex === '2') {
        setTimeout(() => {
          this.formInline_2 = {
            front_lang: 1,
            end_lang: 1,
            currency_set: 0,
            currency_update: 0,
            front_pro_num: 32,
            end_pro_num: 50
          }
          this.loadData = false
        }, 1000)
      }
    },
    // 表单提交
    submitForm(formName) {
      let tabIndex = this.tabIndex
      let data = this[`formInline_${tabIndex}`] // tab对应的表单数据
      this.$refs[formName].validate((valid) => {
        if (valid) {
          // 验证通过
          console.log(data)
        } else {
          console.log('valid')
          return false
        }
      });
    },
    resetForm(formName) {
      // resetFields 只能重置需要验证的值
      this.$refs[formName].resetFields()
      // 因为初始是有值得，所以重置必须再次获取数据
      this.getData()
    },
    formatFlowUnit: Api.UNITS.formatFlowUnit, // 格式化流量单位
    limitNumber: Api.UNITS.limitNumber, // 限制数字类型位数
    // 验证邮箱地址是否正确
    validatorEmall(rule, value, callback) {
      if (Api.UNITS.validatorEmall(value)) {
        callback()
      } else {
        callback(new Error('邮箱格式不正确'))
      }
    },
    // 验证手机格式是否正确
    validatorPhoneNumber(rule, value, callback) {
      if (Api.UNITS.validatorPhoneNumber(value)) {
        callback()
      } else {
        callback(new Error('手机格式不正确'))
      }
    }
  }
}

</script>
<style lang="scss">
.system_set {
  .inline {
    .el-form-item__content {
      width: 170px;
    }
  }
}

</style>
