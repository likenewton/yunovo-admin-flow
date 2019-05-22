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
              <el-input v-model="formInline_0.sys_name" placeholder="请输入系统名称"></el-input>
            </el-form-item>
            <el-form-item prop="sys_master">
              <span slot="label">系统拥有者：</span>
              <el-input v-model="formInline_0.sys_master" placeholder="请输入系统拥有者"></el-input>
            </el-form-item>
            <el-form-item prop="connect_addr">
              <span slot="label">联系地址：</span>
              <el-input type="textarea" v-model="formInline_0.connect_addr" rows="4" placeholder="请输入联系地址"></el-input>
            </el-form-item>
            <el-form-item prop="e_mail">
              <span slot="label">电子邮箱：</span>
              <el-input v-model="formInline_0.e_mail" placeholder="请输入电子邮箱"></el-input>
            </el-form-item>
            <el-form-item prop="connect_tele">
              <span slot="label">联系电话：</span>
              <el-input v-model="formInline_0.connect_tele" placeholder="请输入联系电话"></el-input>
            </el-form-item>
            <el-form-item prop="fax_no">
              <span slot="label">传真号码：</span>
              <el-input v-model="formInline_0.fax_no" placeholder="请输入传真号码"></el-input>
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
              <el-input v-model="formInline_1.home_title" placeholder="请输入首页标题"></el-input>
            </el-form-item>
            <el-form-item prop="seo_set">
              <span slot="label">SEO设置：</span>
              <el-input type="textarea" v-model="formInline_1.seo_set" rows="4" placeholder="请设置SEO"></el-input>
            </el-form-item>
            <el-form-item prop="template_set">
              <span slot="label">模板设置：</span>
              <el-select v-model="formInline_1.template_set" placeholder="请选择模板">
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
              <div class="annotation">设置是否每天自动更新货币！</div>
            </el-form-item>
            <el-form-item prop="front_pro_num">
              <span slot="label">每页默认产品数量(前台管理)：</span>
              <el-input v-model.number="formInline_2.front_pro_num" type="text" placeholder="请输入每页默认产品数量"></el-input>
              <div class="annotation">确定在产品分类中每个网页上显示多少产品（产品，种类等）</div>
            </el-form-item>
            <el-form-item prop="end_pro_num">
              <span slot="label">每页默认产品数量(后台管理)：</span>
              <el-input v-model.number="formInline_2.end_pro_num" type="text" placeholder="请输入每页默认产品数量"></el-input>
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
            <el-form-item prop="email_protocal">
              <span slot="label">邮件协议：</span>
              <el-select v-model="formInline_3.email_protocal" placeholder="请选择邮件协议">
                <el-option label="MAIL" :value="0"></el-option>
                <el-option label="SMTP" :value="1"></el-option>
              </el-select>
              <div class="annotation">只需选择'MAIL' 除非您的主机已禁用PHP的邮件功能</div>
            </el-form-item>
            <el-form-item prop="email_para">
              <span slot="label">邮件参数：</span>
              <el-input v-model="formInline_3.email_para" placeholder="请输入邮件参数"></el-input>
              <div class="annotation">当使用 'MAIL', 在此可增加额外的邮件地址(如 '-f sender@hecart.com')</div>
            </el-form-item>
            <el-form-item prop="smtp_host">
              <span slot="label">SMTP 主机：</span>
              <el-input v-model="formInline_3.smtp_host" placeholder="请输入SMTP 主机号"></el-input>
            </el-form-item>
            <el-form-item prop="smtp_user">
              <span slot="label">SMTP 用户：</span>
              <el-input v-model="formInline_3.smtp_user" placeholder="请输入SMTP 用户"></el-input>
            </el-form-item>
            <el-form-item prop="smtp_password">
              <span slot="label">SMTP 密码：</span>
              <el-input v-model="formInline_3.smtp_password" placeholder="请输入SMTP 密码"></el-input>
            </el-form-item>
            <el-form-item prop="smtp_port">
              <span slot="label">SMTP 端口号：</span>
              <el-input v-model="formInline_3.smtp_port" @input="formInline_3.smtp_port = limitNumber(formInline_3.smtp_port, 4, 0)" placeholder="请输入SMTP 端口号"></el-input>
            </el-form-item>
            <el-form-item prop="smtp_timeout">
              <span slot="label">SMTP 超时：</span>
              <el-input v-model="formInline_3.smtp_timeout" @input="formInline_3.smtp_timeout = limitNumber(formInline_3.smtp_timeout, 5, 0)" placeholder="请输入SMTP 超时时间"></el-input>
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
            <el-form-item prop="isuse_ssl">
              <span slot="label">使用 SSL：</span>
              <el-radio v-model="formInline_4.isuse_ssl" :label="1">是</el-radio>
              <el-radio v-model="formInline_4.isuse_ssl" :label="0">否</el-radio>
              <div class="annotation">要使用SSL, 你需要在你的服务安装SSL及在设定档有SSL地址</div>
            </el-form-item>
            <el-form-item prop="max_logincount">
              <span slot="label">最大登录次数：</span>
              <el-input v-model="formInline_4.max_logincount" @input="formInline_4.max_logincount = limitNumber(formInline_4.max_logincount, 1, 0)" placeholder="请设置最大登录次数"></el-input>
              <div class="annotation">当用户登录次数超过最大登录次数时，将提示锁定用户不让其连接登录，最大次数 0 - 9</div>
            </el-form-item>
            <el-form-item prop="lock_hour">
              <span slot="label">登录失败锁定小时：</span>
              <el-input v-model="formInline_4.lock_hour" @input="formInline_4.lock_hour = limitNumber(formInline_4.lock_hour, 2, 0)" placeholder="请设置登录失败锁定"></el-input>
              <div class="annotation">当用户被锁定时，超过多少个小时后方可再次重新登录，锁定小时 0 - 99</div>
            </el-form-item>
            <el-form-item prop="file_expand">
              <span slot="label">允许上传文件的扩展名：</span>
              <el-input type="textarea" v-model="formInline_4.file_expand" rows="4" placeholder="请一行输入一个扩展名"></el-input>
              <div class="annotation">设置上传文件时允许的文件扩展名，一行一个扩展名</div>
            </el-form-item>
            <el-form-item prop="file_type">
              <span slot="label">允许上传文件的类型：</span>
              <el-input type="textarea" v-model="formInline_4.file_type" rows="4" placeholder="请一行输入一个类型"></el-input>
              <div class="annotation">设置上传文件时允许的文件类型.一行一个文件类型值</div>
            </el-form-item>
            <el-form-item prop="is_preserve">
              <span slot="label">系统维护模式：</span>
              <el-radio v-model="formInline_4.is_preserve" :label="1">是</el-radio>
              <el-radio v-model="formInline_4.is_preserve" :label="0">否</el-radio>
              <div class="annotation">本功能将在前台首页显示系统维护信息。 但在后台您可以正常使用本系统</div>
            </el-form-item>
            <el-form-item prop="secre_key">
              <span slot="label">加密秘钥：</span>
              <el-input v-model="formInline_4.secre_key" placeholder="请输入加密秘钥"></el-input>
              <div class="annotation">请提供一个密钥, 让处理订单时可加密个人的资料</div>
            </el-form-item>
            <el-form-item prop="compress_level">
              <span slot="label">输出压缩等级：</span>
              <el-input v-model="formInline_4.compress_level" @input="formInline_4.compress_level = limitNumber(formInline_4.compress_level, 1, 0)" placeholder="请设置输出压缩等级"></el-input>
              <div class="annotation">GZIP 可更有效地转移到要求的客户端。压缩级别必须在0 - 9</div>
            </el-form-item>
            <el-form-item prop="diaplay_error">
              <span slot="label">显示错误：</span>
              <el-radio v-model="formInline_4.diaplay_error" :label="1">是</el-radio>
              <el-radio v-model="formInline_4.diaplay_error" :label="0">否</el-radio>
            </el-form-item>
            <el-form-item prop="log_error">
              <span slot="label">日志错误：</span>
              <el-radio v-model="formInline_4.log_error" :label="1">是</el-radio>
              <el-radio v-model="formInline_4.log_error" :label="0">否</el-radio>
            </el-form-item>
            <el-form-item prop="errlog_filename">
              <span slot="label">错误日志文件名：</span>
              <el-input v-model="formInline_4.errlog_filename" placeholder="请输入错误日志文件名"></el-input>
            </el-form-item>
            <el-form-item prop="google_analytics_code">
              <span slot="label">Google Analytics Code：</span>
              <el-input type="textarea" v-model="formInline_4.google_analytics_code" rows="4" placeholder="请输入Google Analytics Code"></el-input>
              <div class="annotation">登录您的 <el-button type="text" @click="openGoogleAnalytics">Google Analytics</el-button> 账户，创建您的系统概述拷贝并粘贴统计代码到框内</div>
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
      rules_4: {
        errlog_filename: [{
          required: true,
          message: '请输入错误日志文件名',
          trigger: 'blur'
        }]
      }
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
      } else if (tabIndex === '3') {
        setTimeout(() => {
          this.formInline_3 = {
            email_protocal: 0,
            email_para: '',
            smtp_host: 'smtp.exmail.qq.com',
            smtp_user: 'spt@spt-tek.com',
            smtp_password: 'Spt2017',
            smtp_port: 465,
            smtp_timeout: 20
          }
          this.loadData = false
        }, 1000)
      } else if (tabIndex === '4') {
        setTimeout(() => {
          this.formInline_4 = {
            isuse_ssl: 0,
            max_logincount: 3,
            lock_hour: 7,
            file_expand: 'png\njpg\njpeg\ngif\ncsv\nxls\nxlsn',
            file_type: 'image/png\nimage/jpeg\nimage/jpeg\nimage/jpeg\nimage/gif\napplication/octet-stream\napplication/vnd.ms-excel\napplication/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
            is_preserve: 0,
            secre_key: '235ef793cdd0637d5869907f03c874a8',
            compress_level: 7,
            diaplay_error: 0,
            log_error: 1,
            errlog_filename: 'error.txt'
          }
          this.loadData = false
        }, 1000)
      }
    },
    openGoogleAnalytics() {
      window.open('http://www.google.com/analytics/')
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
    },
    validatorEmpty(rule, value, callback) {
      if (!value) {
        callback()
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
