<template>
  <el-card class="datapreserve_container" shadow="never">
    <div slot="header" class="clearfix">
      <span>备份 / 恢复</span>
    </div>
    <el-form v-loading="loadData" :inline="false" :model="formInline" :rules="rules" ref="ruleForm" label-width="120px" size="small">
      <el-form-item>
        <span slot="label">恢复备份：</span>
        <!-- 这里演示了如何在提交表单的时候再开始上传文件 -->
        <el-upload class="upload-demo" action="https://jsonplaceholder.typicode.com/posts/" :on-success="uploadHandleSuccess" :on-change="uploadHandleChange" ref="upload" :with-credentials="true" :limit="1" :file-list="fileList" :auto-upload="false">
          <el-button type="primary">点击上传</el-button>
          <div slot="tip" class="el-upload__tip">只能上传.txt、.xlsx文件</div>
        </el-upload>
      </el-form-item>
      <el-form-item prop="data_backups">
        <span slot="label">数据备份：</span>
        <el-radio v-model="formInline.data_backups" :label="0">全部备份</el-radio>
        <el-radio v-model="formInline.data_backups" :label="1">选择备份</el-radio>
        <el-button v-show="formInline.data_backups === 1" type="primary" @click="openChoice(0)">选择备份数据</el-button>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm('ruleForm')">保存</el-button>
        <el-button type="warning" @click="resetForm('ruleForm')">重置</el-button>
      </el-form-item>
    </el-form>
    <el-dialog :visible="dialogVisible" @close="cancelChoice">
      <span slot="title">{{list.title}}</span>
      <div v-loading="loadDialog" class="dialog_content">
        <div slot>
          <el-checkbox-group v-model="list.checked" @change="handleChoiceChange" :style="{'maxHeight': winHeight/2 + 'px'}">
            <el-checkbox v-for="item in list.data" :label="item.id" :key="item.id">{{item.text}}</el-checkbox>
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
export default {
  data() {
    return {
      dialogVisible: false,
      loadData: true,
      formInline: {},
      fileList: [], // 文件上传列表
      list: {
        title: '数据备份列表',
        data: [],
        checked: [],
        temp: []
      },
      rules: {}
    }
  },
  mounted() {
    this.getData()
  },
  methods: {
    // 提交表单
    submitForm(formName) {
      console.log(this.formInline)
      this.$refs[formName].validate((valid) => {
        if (valid) {
          // 验证通过
          this.$refs.upload.submit()
        } else {
          return false;
        }
      });
    },
    // 重置表单
    resetForm(formName) {
      // resetFields 只能重置需要验证的值
      this.$refs[formName].resetFields()
      this.fileList = []
      this.list = {
        title: '数据备份列表',
        data: [],
        checked: [],
        temp: []
      }
      this.getData()
    },
    getData() {
      // 这里的data还包括 数据备份的，先空着
      this.loadData = true
      setTimeout(() => {
        this.formInline = {
          data_backups: 1
        }
        this.loadData = false
      }, 1000)
    },
    // 处理上传文件(同时上传一个文件)
    uploadHandleChange(file, fileList) {
      if (fileList[0].raw) {
        let type = fileList[0].raw.name
        if (!/\.txt$/.test(type) && !/\.xlsx$/.test(type)) {
          this.$message.error('只能上传.txt/.xlsx文件');
          this.fileList = []
          return false
        }
      }
    },
    // 文件上传成功回调函数
    uploadHandleSuccess(res, file) {
      this.$message.success('文件上传成功')
    },
    // 弹框关闭的时清空checked
    cancelChoice() {
      this.resetChoice()
      this.dialogVisible = false
    },
    resetChoice() {
      this.list.checked = []
    },
    // 打开备份数据选择dialog
    openChoice(type) {
      this.winHeight = $(window).height()
      this.dialogVisible = true
      // checked弹框关闭的时候都会被清空，打开弹框的时候要从temp获取选择的数据
      this.list.checked = this.list.temp
      if (this.list.data.length === 0) {
        this.getCheckData()
      }
    },
    // 获取备份数据
    getCheckData() {
      this.loadDialog = true
      // 根据choiceType 来取数据
      setTimeout(() => {
        this.list.data = function(count) {
          let arr = []
          for (let i = 0; i < count; i++) {
            arr.push({
              id: i,
              text: 'GPRS流量推送策略表'
            })
          }
          return arr
        }(100)
        this.loadDialog = false
      }, 1000)
    },
    // 保存选择
    makesureChoice() {
      this.$message.success(`已选择 ${this.list.checked.length} 项`)
      this.list.temp = this.list.checked
      this.dialogVisible = false
    }
  }
}

</script>
<style lang="scss">
.datapreserve_container {
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
