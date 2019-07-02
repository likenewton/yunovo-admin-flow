<template>
  <el-card class="batchcreate_container" shadow="never">
    <div slot="header" class="clearfix">
      <span>权限管理设置</span>
    </div>
    <el-form class="editor-form" v-loading="isLoadData" :inline="false" :model="formInline" :rules="rules" ref="ruleForm" label-width="120px" size="small">
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
        <el-button v-show="formInline.jg_name === 2" type="primary" @click="openChoice(0)">选择可控机构</el-button>
      </el-form-item>
      <el-form-item prop="authgroup_desc">
        <span slot="label">查看权限：</span>
        <el-radio v-model="formInline.read_auth" :label="1">所有权限</el-radio>
        <el-radio v-model="formInline.read_auth" :label="2">选择权限</el-radio>
        <el-button v-show="formInline.read_auth === 2" type="primary" @click="openChoice(1)">选择查看权限</el-button>
      </el-form-item>
      <el-form-item prop="authgroup_desc">
        <span slot="label">更改权限：</span>
        <el-radio v-model="formInline.write_auth" :label="1">所有权限</el-radio>
        <el-radio v-model="formInline.write_auth" :label="2">选择权限</el-radio>
        <el-button v-show="formInline.write_auth === 2" type="primary" @click="openChoice(2)">选择更改权限</el-button>
      </el-form-item>
      <el-form-item>
        <el-button @click="$router.back()">返回</el-button>
        <el-button type="primary" @click="submitForm('ruleForm')">保存</el-button>
        <el-button type="warning" @click="resetForm('ruleForm')">重置</el-button>
      </el-form-item>
    </el-form>
    <el-dialog :visible="dialogVisible" @close="cancelChoice">
      <span slot="title">{{list[choiceType].title}}</span>
      <div v-loading="loadDialog" class="dialog_content">
        <div slot>
          <el-checkbox-group v-model="list[choiceType].checked" @change="handleChoiceChange" :style="{'maxHeight': winHeight/2 + 'px'}">
            <el-checkbox v-for="item in list[choiceType].data" :label="item.id" :key="item.id">{{item.text}}-{{item.id}}</el-checkbox>
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
      choiceType: 0,
      // 数据列表
      list: {
        '0': {
          title: '机构列表',
          data: [],
          checked: [],
          temp: []
        },
        '1': {
          title: '可查看权限列表',
          data: [],
          checked: [],
          temp: []
        },
        '2': {
          title: '可更改权限列表',
          data: [],
          checked: [],
          temp: []
        }
      },
      formInline: {
        jg_name: 1,
        read_auth: 1,
        write_auth: 1
      },
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
      this.getData()
    } else {
      // 如果是新增页面要加上密码验证（必须）
      this.isLoadData = false
    }
  },
  methods: {
    // 打开选择机构弹框
    openChoice(type) {
      this.choiceType = type
      this.winHeight = $(window).height()
      this.dialogVisible = true
      let list = this.list[this.choiceType]
      // checked弹框关闭的时候都会被清空，打开弹框的时候要从temp获取选择的数据
      list.checked = list.temp
      if (list.data.length === 0) {
        this.getCheckData()
      }
    },
    // 弹框关闭的时清空checked
    cancelChoice() {
      this.resetChoice()
      this.dialogVisible = false
    },
    resetChoice() {
      this.list[this.choiceType].checked = []
    },
    handleChoiceChange(para) {
      this.list[this.choiceType].checked = para
    },
    // 保存选择
    makesureChoice() {
      let list = this.list[this.choiceType]
      this.$message.success(`已选择 ${list.checked.length} 项`)
      list.temp = list.checked
      this.dialogVisible = false
    },
    // 获取要选择的数据
    getCheckData() {
      this.loadDialog = true
      // 根据choiceType 来取数据
      if (this.choiceType === 0) {
        setTimeout(() => {
          this.list[this.choiceType].data = function(count) {
            let arr = []
            for (let i = 0; i < count; i++) {
              arr.push({
                id: i,
                text: '选择可控机构'
              })
            }
            return arr
          }(100)
          this.loadDialog = false
        }, 1000)
      } else if (this.choiceType === 1) {
        setTimeout(() => {
          this.list[this.choiceType].data = function(count) {
            let arr = []
            for (let i = 0; i < count; i++) {
              arr.push({
                id: i,
                text: '选择查看权限'
              })
            }
            return arr
          }(100)
          this.loadDialog = false
        }, 1000)
      } else if (this.choiceType === 2) {
        setTimeout(() => {
          this.list[this.choiceType].data = function(count) {
            let arr = []
            for (let i = 0; i < count; i++) {
              arr.push({
                id: i,
                text: '选择更改权限'
              })
            }
            return arr
          }(100)
          this.loadDialog = false
        }, 1000)
      }
    },
    // 获取该页面的表单数据(type=update)
    getData() {
      // 如果是编辑就获取数据再展示
      setTimeout(() => {
        this.formInline = {
          authgroup_name: '管理员－超级组',
          authgroup_desc: '拥有最高权限，主管生杀大权，至高权利！',
          jg_name: 1,
          read_auth: 1,
          write_auth: 1
        }
        this.isLoadData = false
      }, 1000)
    },
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
