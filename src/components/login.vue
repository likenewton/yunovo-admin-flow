<template>
  <div class="login-container">
    <img class="left-img" src="../assets/images/45.png">
    <el-card shadow="never" class="login-card">
      <el-row>
        <el-col>
          <div class="logo-wrapper">
            <img src="../assets/images/LOGO-188X56.png">
          </div>
        </el-col>
      </el-row>
      <el-row>
        <el-col>
          <h2 class="login-text">车联网云平台</h2>
        </el-col>
      </el-row>
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" :size="size">
        <el-form-item prop="name">
          <el-input :size="size" v-model="ruleForm.name" placeholder="请输入用户名">
            <i slot="prefix" class="el-input__icon el-icon-fontyonghu"></i>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input :size="size" type="password" v-model="ruleForm.password" placeholder="请输入密码">
            <i slot="prefix" class="el-input__icon el-icon-fontmima"></i>
          </el-input>
        </el-form-item>
        <el-form-item prop="code">
          <el-row :gutter="10" style="margin-top: 0">
            <el-col :span="14">
              <el-input :size="size" v-model="ruleForm.code" placeholder="请输入验证码">
                <i slot="prefix" class="el-input__icon el-icon-fontyanzhengma"></i>
              </el-input>
            </el-col>
            <el-col :span="10">
              <v-piccode :contentWidth="contentWidth" :contentHeight="contentHeight"></v-piccode>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item>
          <el-col>
            <el-button :size="size" type="primary" class="login-btn" @click="login('ruleForm')">登录</el-button>
          </el-col>
        </el-form-item>
      </el-form>
    </el-card>
    <footer class="footer3">
      <span>Copyright©2014-2019&nbsp;深圳市云智易联科技有限公司&nbsp;版权所有</span>
    </footer>
  </div>
</template>
<script>
import Api from 'assets/js/api.js'

export default {
  name: 'vAside',
  data() {
    return {
      size: {
        medium: 'medium',
        small: 'small',
        mini: 'mini',
      } [Api.UNITS.getSize()],
      contentHeight: {
        medium: 36,
        small: 34,
        mini: 28,
      } [Api.UNITS.getSize()],
      contentWidth: {
        medium: 114,
        small: 107,
        mini: 83,
      } [Api.UNITS.getSize()],
      ruleForm: {
        name: 'Newton',
        password: '123456',
        code: '1234'
      },
      rules: {
        name: [{
          required: true,
          message: '请输入用户名',
          trigger: 'blur'
        }, {
          min: 1,
          max: 6,
          message: '长度在 1 到 6 个字符',
          trigger: 'blur'
        }],
        password: [{
          required: true,
          message: '请输入密码',
          trigger: 'blur'
        }, {
          validator: this.validatePass,
          trigger: 'blur'
        }],
        code: [{
          required: true,
          message: '请输入验证码',
          trigger: 'blur'
        }, {
          min: 6,
          max: 6,
          message: '验证码长度为6个字符',
          trigger: 'blur'
        }]
      }
    }
  },
  mounted() {
    Api.UNITS.getSize()
  },
  computed: {},
  methods: {
    login(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          // 合法就直接登录吧
          // 测试跨域
          // this.$http.post('/cas/v1/tickets?username=admin&password=123456', null, {
          //   headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
          // }).then( res => {
          //   console.log(res)
          // })
          // return
          this.$router.push({ name: 'sysmenu' })
        } else {
          console.log('error submit!!');
          return false;
        }
      })
    },
    // 密码验证
    validatePass(rule, value, callback) {
      if (value.trim().length < 6) {
        callback(new Error('密码长度不能小于6位'))
      } else if (value.trim().length > 18) {
        callback(new Error('密码长度不能大于18位'))
      } else {
        callback();
      }
    }
  }
}

</script>
<style lang="scss">
.login-container {
  position: relative;
  width: 100%;
  height: 100%;
  background: url('../assets/images/99.jpg') no-repeat;
  background-position: center;
  background-size: cover;
  overflow-y: hidden;

  .left-img {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate3D(-50%, -50%, 0);
    margin-top: -140px;
    margin-left: -300px;
  }

  .login-card {
    position: absolute;
    padding: 35px;
    width: 400px;
    height: 500px;
    top: 50%;
    right: 0;
    transform: translate3D(0, -60%, 0);
    margin-right: 13%;

    .el-form {
      margin-top: 50px;

      .login-btn {
        width: 100%;
      }
    }

    .logo-wrapper {
      width: 65%;
      margin: 0 auto;

      img {
        width: 100%;
      }
    }

    .login-text {
      text-align: center;
      font-size: 24px;
      color: #666;
    }

    .el-row {
      margin-top: 20px;
    }

  }

  .footer3 {
    position: absolute;
    bottom: 0;
    height: 70px;
    width: 100%;
    background: rgba(6, 44, 56, 0.5);
    display: flex;
    align-items: center;
    justify-content: center;

    * {
      font-size: 14px;
      color: #fff;
    }
  }
}

@media screen and (max-width: 1600px) {
  .login-container {
    .left-img {
      width: 620px;
      margin-top: -110px;
      margin-left: -265px;
    }

    .login-card {
      padding: 18px;
      width: 350px;
      height: 420px;

      .login-text {
        font-size: 20px;
      }

      .el-row {
        margin-top: 17px;
      }

      .el-form {
        margin-top: 42px;

        .el-form-item {
          margin-bottom: 16px;

          .el-input__icon {
            font-size: 14px;
            line-height: 34px;
          }
        }
      }
    }

    .footer3 {
      height: 60px;

      * {
        font-size: 13px;
      }
    }
  }
}

@media screen and (max-width: 1300px) {
  .login-container {
    .left-img {
      width: 520px;
      margin-top: -90px;
      margin-left: -220px;
    }

    .login-card {
      padding: 5px;
      width: 270px;
      height: 340px;

      .login-text {
        font-size: 16px;
      }

      .el-row {
        margin-top: 14px;
      }

      .el-form {
        margin-top: 35px;

        .el-form-item {
          margin-bottom: 12px;

          .el-input__icon {
            font-size: 14px;
            line-height: 34px;
          }
        }
      }
    }

    .footer3 {
      height: 50px;

      * {
        font-size: 12px;
      }
    }
  }
}

</style>
