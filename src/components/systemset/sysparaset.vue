<template>
  <div>
    <el-card class="system_set" shadow="never">
      <el-tabs @tab-click="changeTab">
        <!-- 编辑项目 -->
        <el-tab-pane>
          <span slot="label"></i>编辑项目</span>
          <el-form v-loading="loadData" :inline="false" :model="formInline_0" :rules="rules_0" ref="ruleForm_0" label-width="140px" size="small" :status-icon="true">
            <el-form-item prop="config_name">
              <span slot="label">系统名称：</span>
              <el-input v-model="formInline_0.config_name" placeholder="请输入系统名称"></el-input>
            </el-form-item>
            <el-form-item prop="config_owner">
              <span slot="label">系统拥有者：</span>
              <el-input v-model="formInline_0.config_owner" placeholder="请输入系统拥有者"></el-input>
            </el-form-item>
            <el-form-item prop="config_address">
              <span slot="label">联系地址：</span>
              <el-input type="textarea" v-model="formInline_0.config_address" rows="4" placeholder="请输入联系地址"></el-input>
            </el-form-item>
            <el-form-item prop="config_email">
              <span slot="label">电子邮箱：</span>
              <el-input v-model="formInline_0.config_email" placeholder="请输入电子邮箱"></el-input>
            </el-form-item>
            <el-form-item prop="config_telephone">
              <span slot="label">联系电话：</span>
              <el-input v-model="formInline_0.config_telephone" placeholder="请输入联系电话"></el-input>
            </el-form-item>
            <el-form-item prop="config_fax">
              <span slot="label">传真号码：</span>
              <el-input v-model="formInline_0.config_fax" placeholder="请输入传真号码"></el-input>
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
            <el-form-item prop="config_title">
              <span slot="label">首页标题：</span>
              <el-input v-model="formInline_1.config_title" placeholder="请输入首页标题"></el-input>
            </el-form-item>
            <el-form-item prop="config_meta_description">
              <span slot="label">SEO设置：</span>
              <el-input type="textarea" v-model="formInline_1.config_meta_description" rows="4" placeholder="请设置SEO"></el-input>
            </el-form-item>
            <el-form-item prop="config_template">
              <span slot="label">模板设置：</span>
              <el-select v-model="formInline_1.config_template" placeholder="请选择模板">
                <el-option label="default" :value="formInline_1.config_template"></el-option>
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
            <el-form-item prop="config_language">
              <span slot="label">前台语言：</span>
              <el-select v-model="formInline_2.config_language" placeholder="请选择">
                <el-option v-for="(item, index) in langSelect" :key="index" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item prop="config_admin_language">
              <span slot="label">后台语言：</span>
              <el-select v-model="formInline_2.config_admin_language" placeholder="请选择">
                <el-option v-for="(item, index) in langSelect" :key="index" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item prop="config_currency">
              <span slot="label">货币设置：</span>
              <el-select v-model="formInline_2.config_currency" placeholder="请选择">
                <el-option v-for="(item, index) in currencySelect" :key="index" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item prop="config_currency_auto">
              <span slot="label">货币更新：</span>
              <el-radio v-model="formInline_2.config_currency_auto" label="1">是</el-radio>
              <el-radio v-model="formInline_2.config_currency_auto" label="0">否</el-radio>
              <div class="annotation">设置是否每天自动更新货币！</div>
            </el-form-item>
            <el-form-item prop="config_catalog_limit">
              <span slot="label">每页默认产品数量(前台管理)：</span>
              <el-input v-model="formInline_2.config_catalog_limit" @input="formInline_2.config_catalog_limit = limitNumber(formInline_2.config_catalog_limit, 3, 0)" type="text" placeholder="请输入每页默认产品数量"></el-input>
              <div class="annotation">确定在产品分类中每个网页上显示多少产品（产品，种类等）</div>
            </el-form-item>
            <el-form-item prop="config_admin_limit">
              <span slot="label">每页默认产品数量(后台管理)：</span>
              <el-input v-model="formInline_2.config_admin_limit" @input="formInline_2.config_admin_limit = limitNumber(formInline_2.config_admin_limit, 3, 0)" type="text" placeholder="请输入每页默认产品数量"></el-input>
              <div class="annotation">确定每页显示多少管理项目（订单，客户等）</div>
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
          <el-form v-loading="loadData" :inline="false" :model="formInline_3" :rules="rules_3" ref="ruleForm_3" label-width="140px" size="small" :status-icon="true">
            <el-form-item prop="config_mail_protocol">
              <span slot="label">邮件协议：</span>
              <el-select v-model="formInline_3.config_mail_protocol" placeholder="请选择邮件协议">
                <el-option label="MAIL" value="mail"></el-option>
                <el-option label="SMTP" value="smtp"></el-option>
              </el-select>
              <div class="annotation">只需选择'MAIL' 除非您的主机已禁用PHP的邮件功能</div>
            </el-form-item>
            <el-form-item prop="config_mail_parameter">
              <span slot="label">邮件参数：</span>
              <el-input v-model="formInline_3.config_mail_parameter" placeholder="请输入邮件参数"></el-input>
              <div class="annotation">当使用 'MAIL', 在此可增加额外的邮件地址(如 '-f sender@hecart.com')</div>
            </el-form-item>
            <el-form-item prop="config_smtp_host">
              <span slot="label">SMTP 主机：</span>
              <el-input v-model="formInline_3.config_smtp_host" placeholder="请输入SMTP 主机号"></el-input>
            </el-form-item>
            <el-form-item prop="config_smtp_username">
              <span slot="label">SMTP 用户：</span>
              <el-input v-model="formInline_3.config_smtp_username" placeholder="请输入SMTP 用户"></el-input>
            </el-form-item>
            <el-form-item prop="config_smtp_password">
              <span slot="label">SMTP 密码：</span>
              <el-input v-model="formInline_3.config_smtp_password" placeholder="请输入SMTP 密码"></el-input>
            </el-form-item>
            <el-form-item prop="config_smtp_port">
              <span slot="label">SMTP 端口号：</span>
              <el-input v-model="formInline_3.config_smtp_port" @input="formInline_3.config_smtp_port = limitNumber(formInline_3.config_smtp_port, 4, 0)" placeholder="请输入SMTP 端口号"></el-input>
            </el-form-item>
            <el-form-item prop="config_smtp_timeout">
              <span slot="label">SMTP 超时：</span>
              <el-input v-model="formInline_3.config_smtp_timeout" @input="formInline_3.config_smtp_timeout = limitNumber(formInline_3.config_smtp_timeout, 5, 0)" placeholder="请输入SMTP 超时时间"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitForm('ruleForm_3')">保存</el-button>
              <el-button type="warning" @click="resetForm('ruleForm_3')">重置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <!-- 服务器设置 -->
        <el-tab-pane>
          <span slot="label">服务器设置</span>
          <el-form v-loading="loadData" :inline="false" :model="formInline_4" :rules="rules_4" ref="ruleForm_4" label-width="170px" size="small" :status-icon="true">
            <el-form-item prop="config_use_ssl">
              <span slot="label">使用 SSL：</span>
              <el-radio v-model="formInline_4.config_use_ssl" label="1">是</el-radio>
              <el-radio v-model="formInline_4.config_use_ssl" label="0">否</el-radio>
              <div class="annotation">要使用SSL, 你需要在你的服务安装SSL及在设定档有SSL地址</div>
            </el-form-item>
            <el-form-item prop="config_login_count_max">
              <span slot="label">最大登录次数：</span>
              <el-input v-model="formInline_4.config_login_count_max" @input="formInline_4.config_login_count_max = limitNumber(formInline_4.config_login_count_max, 1, 0)" placeholder="请设置最大登录次数"></el-input>
              <div class="annotation">当用户登录次数超过最大登录次数时，将提示锁定用户不让其连接登录，最大次数 0 - 9</div>
            </el-form-item>
            <el-form-item prop="config_login_locked_hours">
              <span slot="label">登录失败锁定小时：</span>
              <el-input v-model="formInline_4.config_login_locked_hours" @input="formInline_4.config_login_locked_hours = limitNumber(formInline_4.config_login_locked_hours, 2, 0)" placeholder="请设置登录失败锁定"></el-input>
              <div class="annotation">当用户被锁定时，超过多少个小时后方可再次重新登录，锁定小时 0 - 99</div>
            </el-form-item>
            <el-form-item prop="config_file_extension_allowed">
              <span slot="label">允许上传文件的扩展名：</span>
              <el-input type="textarea" v-model="formInline_4.config_file_extension_allowed" rows="4" placeholder="请一行输入一个扩展名"></el-input>
              <div class="annotation">设置上传文件时允许的文件扩展名，一行一个扩展名</div>
            </el-form-item>
            <el-form-item prop="config_file_mime_allowed">
              <span slot="label">允许上传文件的类型：</span>
              <el-input type="textarea" v-model="formInline_4.config_file_mime_allowed" rows="4" placeholder="请一行输入一个类型"></el-input>
              <div class="annotation">设置上传文件时允许的文件类型.一行一个文件类型值</div>
            </el-form-item>
            <el-form-item prop="config_maintenance">
              <span slot="label">系统维护模式：</span>
              <el-radio v-model="formInline_4.config_maintenance" label="1">是</el-radio>
              <el-radio v-model="formInline_4.config_maintenance" label="0">否</el-radio>
              <div class="annotation">本功能将在前台首页显示系统维护信息。 但在后台您可以正常使用本系统</div>
            </el-form-item>
            <el-form-item prop="config_encryption">
              <span slot="label">加密秘钥：</span>
              <el-input v-model="formInline_4.config_encryption" placeholder="请输入加密秘钥"></el-input>
              <div class="annotation">请提供一个密钥, 让处理订单时可加密个人的资料</div>
            </el-form-item>
            <el-form-item prop="config_compression">
              <span slot="label">输出压缩等级：</span>
              <el-input v-model="formInline_4.config_compression" @input="formInline_4.config_compression = limitNumber(formInline_4.config_compression, 1, 0)" placeholder="请设置输出压缩等级"></el-input>
              <div class="annotation">GZIP 可更有效地转移到要求的客户端。压缩级别必须在0 - 9</div>
            </el-form-item>
            <el-form-item prop="config_error_display">
              <span slot="label">显示错误：</span>
              <el-radio v-model="formInline_4.config_error_display" label="1">是</el-radio>
              <el-radio v-model="formInline_4.config_error_display" label="0">否</el-radio>
            </el-form-item>
            <el-form-item prop="config_error_log">
              <span slot="label">日志错误：</span>
              <el-radio v-model="formInline_4.config_error_log" label="1">是</el-radio>
              <el-radio v-model="formInline_4.config_error_log" label="0">否</el-radio>
            </el-form-item>
            <el-form-item prop="config_error_filename">
              <span slot="label">错误日志文件名：</span>
              <el-input v-model="formInline_4.config_error_filename" placeholder="请输入错误日志文件名"></el-input>
            </el-form-item>
            <el-form-item prop="config_google_analytics">
              <span slot="label">Google Analytics Code：</span>
              <el-input type="textarea" v-model="formInline_4.config_google_analytics" rows="4" placeholder="请输入Google Analytics Code"></el-input>
              <div class="annotation">登录您的 <a class="text_primary" href="http://www.google.com/analytics/" target="blank">Google Analytics</a> 账户，创建您的系统概述拷贝并粘贴统计代码到框内</div>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitForm('ruleForm_4')">保存</el-button>
              <el-button type="warning" @click="resetForm('ruleForm_4')">重置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>
<script>
import Api from 'assets/js/api.js'
import { mapState } from 'vuex'

export default {
  data() {
    return {
      loadData: true, // 是否显示加载动画
      tabIndex: '0', // 当前激活的tab的下标值
      alias: { // 不同的tab对应的别名(tabName)
        'formInline_0': 'tab-general',
        'formInline_1': 'tab-store',
        'formInline_2': 'tab-local',
        'formInline_3': 'tab-mail',
        'formInline_4': 'tab-server',
      },
      currencySelect: [], // 货币的下拉列表
      formInline_0: {},
      formInline_1: {},
      formInline_2: {},
      formInline_3: {},
      formInline_4: {},
      rules_0: {
        config_name: [{
          required: true,
          message: '请输入系统名称',
          trigger: 'blur'
        }],
        config_owner: [{
          required: true,
          message: '请输入系统拥有者',
          trigger: 'blur'
        }],
        config_address: [{
          required: true,
          message: '请输入联系地址',
          trigger: 'blur'
        }],
        config_email: [{
          required: true,
          message: '请输入电子邮箱',
          trigger: 'blur'
        }, {
          validator: this.validatorEmall,
          trigger: 'blur'
        }],
        config_telephone: [{
          required: true,
          message: '请输入联系电话',
          trigger: 'blur'
        }, {
          validator: this.validatorPhoneNumber,
          trigger: 'blur'
        }]
      },
      rules_1: {
        config_title: [{
          required: true,
          message: '请输入首页标题',
          trigger: 'blur'
        }],
        config_template: [{
          required: true,
          message: '请选择模板',
          trigger: 'change'
        }]
      },
      rules_2: {
        config_catalog_limit: [{
          required: true,
          message: '请输入产品数量',
          trigger: 'blur'
        }],
        config_admin_limit: [{
          required: true,
          message: '请输入产品数量',
          trigger: 'blur'
        }]
      },
      rules_3: {},
      rules_4: {
        config_error_filename: [{
          required: true,
          message: '请输入错误日志文件名',
          trigger: 'blur'
        }]
      }
    }
  },
  mounted() {
    this.getCurrencySelect()
    this.getData()
  },
  methods: {
    changeTab(para) {
      this.tabIndex = para.index
    },
    getData() {
      this.loadData = true
      _axios.send({
        method: 'get',
        url: _axios.ajaxAd.getSystemParams,
        done: ((res) => {
          // 该接口获取的是所有表单的数据
          this.formInline_0 = res.data
          this.formInline_1 = res.data
          this.formInline_2 = res.data
          this.formInline_3 = res.data
          this.formInline_4 = res.data
          this.loadData = false
        })
      })
    },
    getCurrencySelect() {
      _axios.send({
        method: 'get',
        url: _axios.ajaxAd.getCurrencySelect,
        done: ((res) => {
          this.currencySelect = res.data
        })
      })
    },
    // 表单提交
    submitForm(formName) {
      let tabIndex = this.tabIndex
      let data = this[`formInline_${tabIndex}`] // tab对应的表单数据
      this.$refs[formName].validate((valid) => {
        if (valid) {
          // 验证通过
          data.tabName = this.alias[`formInline_${tabIndex}`]
          _axios.send({
            method: 'post',
            url: _axios.ajaxAd.updateSystemParams,
            data,
            done: ((res) => {
              // 调用resetForm来清除表单验证的样式
              this.resetForm()
              this.$message.success(res.msg || '操作成功！')
            })
          })
        } else {
          Api.UNITS.showMsgBox()
          return false
        }
      });
    },
    resetForm(formName) {
      // 5个表单的数据都是从getData取得，所以重置要重置5个表单
      this.$refs['ruleForm_0'].resetFields()
      this.$refs['ruleForm_1'].resetFields()
      this.$refs['ruleForm_2'].resetFields()
      this.$refs['ruleForm_3'].resetFields()
      this.$refs['ruleForm_4'].resetFields()
      this.getData()
    },
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
    },
    validatorEmpty(rule, value, callback) {
      if (!value) {
        callback()
      }
    }
  },
  computed: {
    ...mapState({
      langSelect: 'langSelect',
    })
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
