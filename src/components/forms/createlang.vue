<template>
  <el-card class="batchcreate_container" shadow="never">
    <div slot="header" class="clearfix">
      <span>语言设置</span>
    </div>
    <el-form v-loading="isLoadData" :inline="false" :model="formInline" :rules="rules" ref="ruleForm" label-width="120px" size="small" :status-icon="true">
      <el-form-item prop="lang_name">
        <span slot="label">语言名称：</span>
        <el-input v-model="formInline.lang_name" placeholder="请输入语言名称"></el-input>
      </el-form-item>      
      <el-form-item prop="code">
        <span slot="label">代码：</span>
        <el-input v-model="formInline.code" placeholder="请输入代码"></el-input>
        <div class="annotation">不要改变，如果这是你的默认语言</div>
      </el-form-item>
      <el-form-item prop="regional">
        <span slot="label">区域：</span>
        <el-input v-model="formInline.regional" placeholder="请输入区域"></el-input>
        <div class="annotation">例：en_US.UTF-8,en_US,en-gb,english</div>
      </el-form-item>
      <el-form-item prop="picture">
        <span slot="label">图片：</span>
        <el-input v-model="formInline.picture" placeholder="请输入图片"></el-input>
        <div class="annotation">例：gb.png</div>
      </el-form-item>
      <el-form-item prop="dir_name">
        <span slot="label">文件目录：</span>
        <el-input v-model="formInline.dir_name" placeholder="请输入文件目录"></el-input>
        <div class="annotation">语言文件目录(注意大小写)</div>
      </el-form-item>
      <el-form-item prop="file_name">
        <span slot="label">文件名：</span>
        <el-input v-model="formInline.file_name" placeholder="请输入文件名"></el-input>
        <div class="annotation">调用的语言文件名，不带后缀</div>
      </el-form-item>
      <el-form-item prop="status">
        <span slot="label">状态：</span>
        <el-select v-model="formInline.status" placeholder="请选择">
          <el-option label="启用" :value="0"></el-option>
          <el-option label="停用" :value="1"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="sort">
        <span slot="label">排序：</span>
        <el-input v-model="formInline.sort"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="$router.back()">返回</el-button>
        <el-button type="primary" @click="submitForm('ruleForm')">保存</el-button>
        <el-button v-if="isCreate" type="warning" @click="resetForm('ruleForm')">重置</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>
<script>
import Api from 'assets/js/api.js'

export default {
  data() {
    return {
      isLoadData: true,
      isCreate: true,
      formInline: {},
      rules: {
        lang_name: [{
          required: true,
          message: '请输入语言名称',
          trigger: 'blur'
        }],
        code: [{
          required: true,
          message: '请输入代码',
          trigger: 'blur'
        }],
        regional: [{
          required: true,
          message: '请输入区域',
          trigger: 'blur'
        }],
        picture: [{
          required: true,
          message: '请输入图片名称',
          trigger: 'blur'
        }],
        dir_name: [{
          required: true,
          message: '请输入文件目录',
          trigger: 'blur'
        }],
        file_name: [{
          required: true,
          message: '请输入文件名',
          trigger: 'blur'
        }]
      }
    }
  },
  mounted() {
    let isUpdate = this.$route.query.type === 'update'
    if (isUpdate) {
      this.isCreate = false
      this.getData()
    } else {
      this.isLoadData = false
    }
  },
  methods: {
    getData() {
      // 如果是编辑就获取数据再展示
      setTimeout(() => {
        this.formInline = {
          lang_name: 'English',
          code: 'en',
          regional: 'en_US.UTF-8,en_US,en-gb,english',
          picture: 'pg.png',
          dir_name: 'english',
          file_name: 'english',
          status: 0
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
          console.log('valid')
          return false
        }
      });
    },
    // 重置表单
    resetForm(formName) {
      // resetFields 只能重置需要验证的值
      this.$refs[formName].resetFields()
    },
    limitNumber: Api.UNITS.limitNumber
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
}

</style>
