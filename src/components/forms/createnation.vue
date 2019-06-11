<template>
  <el-card class="batchcreate_container" shadow="never">
    <div slot="header" class="clearfix">
      <span>国家区域</span>
    </div>
    <el-form v-loading="loadData" :inline="false" :model="formInline" :rules="rules" ref="ruleForm" label-width="140px" size="small" :status-icon="true">
      <el-form-item prop="ntname">
        <span slot="label">区域名称：</span>
        <el-input v-model="formInline.ntname" placeholder="请输入区域名称"></el-input>
      </el-form-item>
      <el-form-item prop="zipcode">
        <span slot="label">区域邮政：</span>
        <el-input v-model="formInline.zipcode" @input="formInline.zipcode = limitNumber(formInline.zipcode, 6, 0)" placeholder="请输入区域邮政"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="$router.back()">返回</el-button>
        <el-button type="primary" @click="submitForm('ruleForm')">保存</el-button>
        <el-button type="warning" @click="resetForm('ruleForm')">重置</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>
<script>
import Api from 'assets/js/api.js'

export default {
  data() {
    return {
      loadData: true,
      isUpdate: false,
      formInline: {
        parent: Api.UNITS.getQuery('parent'),
        ntid: Api.UNITS.getQuery('ntid')
      },
      rules: {
        ntname: [{
          required: true,
          message: '请输入区域名称',
          trigger: 'blur'
        }],
        zipcode: [{
          required: true,
          message: '请输入区域编号',
          trigger: 'blur'
        }]
      }
    }
  },
  mounted() {
    this.isUpdate = this.$route.query.type === 'update'
    if (this.isUpdate) {
      this.getData()
    } else {
      this.loadData = false
    }
  },
  methods: {
    // 获取数据
    getData() {
      this.loadData = true
      _axios.send({
        method: 'get',
        url: _axios.ajaxAd.getNationDetail,
        params: this.formInline,
        done: ((res) => {
          this.formInline = res.data
          this.loadData = false
        })
      })
    },
    // 提交表单
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          // 验证通过
          if (this.isUpdate) { // 修改
            _axios.send({
              method: 'post',
              url: _axios.ajaxAd.upDateNation,
              data: this.formInline,
              done: ((res) => {
                this.$router.push({ name: 'nationset', query: { ntid: this.formInline.parent } })
                setTimeout(() => {
                  this.$message.success(res.msg || '修改成功')
                }, 150)
              })
            })
          } else { // 新增
            _axios.send({
              method: 'post',
              url: _axios.ajaxAd.addNation,
              data: this.formInline,
              done: ((res) => {
                this.$router.push({ name: 'nationset', query: { ntid: this.formInline.parent } })
                setTimeout(() => {
                  this.$message.success(res.msg || '新增成功')
                }, 150)
              })
            })
          }
        } else {
          console.log('valid')
          return false;
        }
      });
    },
    // 重置表单
    resetForm(formName) {
      this.$refs[formName].resetFields()
      this.isUpdate && this.getData()
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
