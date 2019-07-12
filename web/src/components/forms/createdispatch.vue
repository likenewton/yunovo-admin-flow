<template>
  <el-card class="alipay" shadow="never" v-loading="loadData">
    <div slot="header" class="clearfix">
      <span>机构权限分配</span>
    </div>
    <el-form class="editor-form" :inline="false" :model="formInline" :rules="rules" ref="ruleForm" label-width="140px" size="small">
      <el-form-item prop="alipay_partner">
        <span slot="label">所属机构：</span>
        <el-select v-model="formInline.org_id" filterable placeholder="请选择">
          <el-option v-for="(item, index) in orgs" :key="index" :label="item.label" :value="item.value - 0"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="username">
        <span slot="label">用户账号：</span>
        <el-input v-model="formInline.username" disabled placeholder="请输入用户账号"></el-input>
      </el-form-item>
      <el-form-item prop="firstname">
        <span slot="label">真实姓名：</span>
        <el-input v-model="formInline.firstname" disabled placeholder="请输入真实姓名"></el-input>
      </el-form-item>
      <el-form-item prop="status">
        <span slot="label">用户状态：</span>
        <el-select v-model="formInline.status" filterable placeholder="请选择用户状态">
          <el-option label="停用" :value="0"></el-option>
          <el-option label="启用" :value="1"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <span slot="label">可控机构：</span>
        <el-radio-group v-model="orgpos_alias">
          <el-radio :label="0" :disabled="!formInline.all_org">所有机构</el-radio>
          <el-radio :label="1">选择机构</el-radio>
        </el-radio-group>
        <el-button v-show="orgpos_alias === 1" type="primary" @click="openChoice" style="margin-left:8px">选择可控机构 <span>({{orgpos_name_arr_tmp.length}}/50)</span></el-button>
      </el-form-item>
      <el-form-item>
        <el-button @click="$router.back()">返回</el-button>
        <el-button type="primary" @click="submitForm('ruleForm')">保存</el-button>
        <el-button type="warning" @click="resetForm('ruleForm')">重置</el-button>
      </el-form-item>
    </el-form>
    <el-dialog :visible="dialogVisible" @close="cancelChoice" :close-on-click-modal="false" width="1200px">
      <span slot="title">可选机构<span>({{orgpos_name_arr.length}}/50)</span></span>
      <div class="dialog_content">
        <div slot>
          <!-- <el-checkbox class="title-checkbox" :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange">全选</el-checkbox> -->
          <el-checkbox-group v-model="orgpos_name_arr" @change="handleChoiceChange" :max="50" :style="{'maxHeight': winHeight/2 + 'px'}">
            <el-checkbox v-for="item in orgs" :label="item.value" :key="item.value">{{item.label}}</el-checkbox>
          </el-checkbox-group>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" type="warning" @click="clearChoice">清 空</el-button>
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
      isUpdate: false,
      checkAll: false,
      isIndeterminate: false,
      orgpos_alias: -1,
      orgpos_name_arr: [],
      orgpos_name_arr_tmp: [],
      rules: {
        org_id: [{
          required: true,
          message: '请选择所属机构',
          trigger: 'change'
        }],
        username: [{
          required: true,
          message: '请输入用户账号',
          trigger: 'blur'
        }, {
          max: 50,
          message: '不能超过50个字符',
          trigger: 'blur'
        }],
        firstname: [{
          required: true,
          message: '请输入真实姓名',
          trigger: 'blur'
        }, {
          max: 20,
          message: '不能超过20个字符',
          trigger: 'blur'
        }],
        status: [{
          required: true,
          message: '请选择用户状态',
          trigger: 'change'
        }]
      }
    }
  },
  mounted() {
    this.isUpdate = Api.UNITS.getQuery('type') === 'update'
    if (this.isUpdate) {
      this.getData()
    } else {
      this.loadData = false
    }
  },
  methods: {
    openChoice() {
      this.dialogVisible = true
      // checked弹框关闭的时候都会被清空，打开弹框的时候要从temp获取选择的数据
      this.orgpos_name_arr = this.orgpos_name_arr_tmp
      this.handleChoiceChange(this.orgpos_name_arr)
    },
    // 弹框关闭的时清空checked
    cancelChoice() {
      this.resetChoice()
      this.dialogVisible = false
    },
    clearChoice() {
      this.orgpos_name_arr = []
    },
    resetChoice() {
      this.orgpos_name_arr = this.formInline.orgpos ? this.formInline.orgpos.split(',') : []
    },
    handleChoiceChange(para) {
      let checkedCount = para.length
      this.orgpos_name_arr = para
      this.checkAll = checkedCount === this.orgs.length
      this.isIndeterminate = checkedCount > 0 && checkedCount < this.orgs.length
    },
    // 保存选择
    makesureChoice() {
      let list = this.list[this.choiceType]
      this.$message.success(`已选择 ${this.orgpos_name_arr.length} 项`)
      this.orgpos_name_arr_tmp = this.orgpos_name_arr
      this.dialogVisible = false
    },
    // 全选
    handleCheckAllChange(val) {
      this.orgpos_name_arr = val ? this.orgs.map((v) => v.value) : [];
      this.isIndeterminate = false;
    },
    // 获取数据
    getData() {
      this.loadData = true
      _axios.send({
        method: 'get',
        url: _axios.ajaxAd.getDispatchDetail,
        params: { username: Api.UNITS.getQuery('username') },
        done: ((res) => {
          this.loadData = false
          this.formInline = res.data || {}
          if (this.formInline.orgpos === '*') {
            this.orgpos_alias = 0
          } else {
            this.orgpos_alias = 1
            this.orgpos_name_arr_tmp = this.formInline.orgpos ? this.formInline.orgpos.split(',') : []
          }
          this.$nextTick(() => {
            this.$refs.ruleForm.clearValidate()
          })
        })
      })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields()
      this.formInline = {}
      this.orgpos_alias = -1
      this.orgpos_name_arr = []
      this.orgpos_name_arr_tmp = []
      this.isUpdate && this.getData()
    },
    // 提交表单
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.orgpos_alias === 0) {
            this.formInline.orgpos = '*'
          } else {
            this.formInline.orgpos = this.orgpos_name_arr_tmp.join(',')
          }
          // 验证通过
          if (this.isUpdate) {
            _axios.send({
              method: 'post',
              url: _axios.ajaxAd.updateUser,
              data: this.formInline,
              done: ((res) => {
                if (res.status === 400) {
                  // 这里服务器验证出了错误
                } else {
                  this.$router.push({ name: 'jgDispatch' })
                  setTimeout(() => {
                    this.showMsgBox({
                      type: 'success',
                      message: res.msg || '操作成功！'
                    })
                  }, 150)
                }
              })
            })
          }
        } else {
          Api.UNITS.showMsgBox()
          return false;
        }
      })
    }
  }
}

</script>
<style lang="scss">
.alipay {
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
        width: 370px;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;

        &.is-checked {
          .el-checkbox__label {
            color: #666;
          }
        }

        &:hover {
          background: #fafafa;
        }

        .el-checkbox__inner {
          vertical-align: initial;
        }
      }
    }

    .title-checkbox {
      &.is-checked {
        .el-checkbox__label {
          color: #666;
        }
      }

      height: 35px;
      padding: 5px;
    }
  }
}

</style>
