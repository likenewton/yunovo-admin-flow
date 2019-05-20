<template>
  <el-card class="batchcreate_container" shadow="never">
    <div slot="header" class="clearfix">
      <span>权限管理设置</span>
    </div>
    <el-form v-loading="isLoadData" :inline="false" :model="formInline" :rules="rules" ref="ruleForm" label-width="120px" size="small">
      <el-form-item prop="authgroup_name">
        <span slot="label">权限组名称：</span>
        <el-input v-model="formInline.authgroup_name"></el-input>
      </el-form-item>
      <el-form-item prop="authgroup_desc">
        <span slot="label">权限组描述：</span>
        <el-input type="textarea" v-model="formInline.authgroup_desc" rows="4"></el-input>
      </el-form-item>
      <el-form-item prop="jg_name">
        <span slot="label">可控机构：</span>
        <el-radio v-model="formInline.jg_name" :label="1">所有机构</el-radio>
        <el-radio v-model="formInline.jg_name" :label="2">选择机构</el-radio>
        <el-button v-show="formInline.jg_name === 2" type="primary" @click="openChoiceJg">选择可控机构</el-button>
      </el-form-item>
      <el-form-item prop="authgroup_desc">
        <span slot="label">查看权限：</span>
        <el-radio v-model="formInline.read_auth" :label="1">所有权限</el-radio>
        <el-radio v-model="formInline.read_auth" :label="2">选择权限</el-radio>
        <el-button v-show="formInline.read_auth === 2" type="primary" @click="openChoiceJg">选择查看权限</el-button>
      </el-form-item>
      <el-form-item prop="authgroup_desc">
        <span slot="label">更改权限：</span>
        <el-radio v-model="formInline.write_auth" :label="1">所有权限</el-radio>
        <el-radio v-model="formInline.write_auth" :label="2">选择权限</el-radio>
        <el-button v-show="formInline.write_auth === 2" type="primary" @click="openChoiceJg">选择更改权限</el-button>
      </el-form-item>
      <el-form-item>
        <el-button @click="$router.back()">返回</el-button>
        <el-button type="primary" @click="submitForm('ruleForm')">保存</el-button>
        <el-button type="warning" @click="resetForm('ruleForm')">重置</el-button>
      </el-form-item>
    </el-form>
    <el-dialog :visible="dialogVisible" @close="cancelJgChoice">
      <span slot="title">机构列表</span>
      <div v-loading="loadDialog" class="dialog_content">
        <div slot>
          <el-checkbox-group v-model="jgCheckedList" @change="handleJgChoiceChange" :style="{'maxHeight': winHeight/2 + 'px'}">
            <el-checkbox v-for="item in jgList" :label="item.id" :key="item.id">{{item.text}}-{{item.id}}</el-checkbox>
          </el-checkbox-group>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" type="warning" @click="resetChoice">重 选</el-button>
        <el-button size="small" type="primary" @click="makesureChoice">保 存</el-button>
      </div>
    </el-dialog>
  </el-card>
</template>
<script>
import Api from 'assets/js/api.js'

export default {
  data() {
    return {
      dialogVisible: false,
      loadDialog: true,
      isLoadData: true,
      formInline: {
        jg_name: 2,
        read_auth: 2,
        write_auth: 2
      },
      // 机构列表
      jgList: [],
      // 选中的机构列表
      jgCheckedList: [],
      tempCheckedList: [],
      rules: {
        authgroup_name: [{
          required: true,
          message: '请输入权限组名称',
          trigger: 'blur'
        }]
      },
      winHeight: 0
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
    // 打开选择机构弹框
    openChoiceJg() {
      this.winHeight = $(window).height()
      this.dialogVisible = true
      // jgCheckedList在弹框关闭的时候都会被清空，打开弹框的时候要从tempCheckedList获取选择的数据
      this.jgCheckedList = this.tempCheckedList
      if (this.jgList.length === 0) {
        this.getJgData()
      }
    },
    // 弹框关闭的时清空jgCheckedList
    cancelJgChoice() {
      this.jgCheckedList = []
      this.dialogVisible = false
    },
    resetChoice() {
      this.jgCheckedList = []
    },
    makesureChoice() {
      this.$message.success(`已选择 ${this.jgCheckedList.length} 项`)
      this.tempCheckedList = this.jgCheckedList
      this.dialogVisible = false
    },
    getJgData() {
      this.loadDialog = true
      setTimeout(() => {
        this.jgList = function(count) {
          let arr = []
          for (let i = 0; i < count; i++) {
            arr.push({
              id: i,
              text: ' 系统设置-支付管理'
            })
          }
          return arr
        }(100)
        this.loadDialog = false
      }, 1000)
    },
    handleJgChoiceChange(para) {
      this.jgCheckedList = para
    },
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

  .el-dialog__body {
    padding: 20px;

    .el-checkbox-group {
      overflow: auto;

      label {
        margin: 0;
        padding: 5px;
        width: 30%;
        max-width: 220px;

        &:hover {
          background: #fafafa;
        }

        .el-checkbox__inner {
          vertical-align: initial;
        }
      }
    }
  }
}

</style>
