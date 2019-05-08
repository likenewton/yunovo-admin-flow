<template>
  <el-card class="batchcreate_container" shadow="never">
    <div slot="header" class="clearfix">
      <span>流量卡出货批次</span>
    </div>
    <el-form :inline="false" :model="formInline" :rules="rules" ref="ruleForm" label-width="225px" size="small">
      <el-form-item prop="batch_code">
        <span slot="label">批次编号：</span>
        <el-input v-model="formInline.batch_code" placeholder="请输入"></el-input>
      </el-form-item>
      <el-form-item prop="batch_name">
        <span slot="label">批次名称：</span>
        <el-input v-model="formInline.batch_name" placeholder="请输入"></el-input>
      </el-form-item>
      <el-form-item prop="batch_remark">
        <span slot="label">批次备注：</span>
        <el-input type="textarea" v-model="formInline.batch_remark" placeholder="请输入" rows="4"></el-input>
      </el-form-item>
      <el-form-item prop="ex_name">
        <span slot="label">出货人名：</span>
        <el-input v-model="formInline.ex_name" placeholder="请输入"></el-input>
      </el-form-item>
      <el-form-item prop="jg_name">
        <span slot="label">机构名称：</span>
        <el-select v-model="formInline.jg_name" placeholder="请选择">
          <el-option label="机构1" :value="0"></el-option>
          <el-option label="机构2" :value="1"></el-option>
          <el-option label="机构3" :value="2"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="city">
        <span slot="label">销往地区：</span>
        <el-cascader change-on-select :options="options" @change="handleCascaderChange" :props="props" v-model="formInline.city" clearable></el-cascader>
      </el-form-item>
      <el-form-item prop="batch_clw">
        <span slot="label">车联网批次：</span>
        <el-input v-model="formInline.batch_clw" placeholder="若需将车联网设备中的卡归属到本批次初始化信息中，需关联车联网出货批次编号"></el-input>
      </el-form-item>
      <el-form-item prop="ks_name">
        <span slot="label">流量卡商：</span>
        <el-select v-model="formInline.ks_name" placeholder="请选择">
          <el-option label="卡商1" :value="0"></el-option>
          <el-option label="卡商2" :value="1"></el-option>
          <el-option label="卡商3" :value="2"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="tc_flow">
        <span slot="label">套餐流量：</span>
        <el-input v-model.number="formInline.tc_flow" placeholder="默认单位为M，精确到3位小数(无限制填：99999999)"></el-input>
      </el-form-item>
      <el-form-item prop="months">
        <span slot="label">分配月数：</span>
        <el-select v-model="formInline.months" placeholder="请选择">
          <el-option label="1个月" :value="0"></el-option>
          <el-option label="2个月" :value="1"></el-option>
          <el-option label="3个月" :value="2"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="yj_flow">
        <span slot="label">月均流量：</span>
        <el-input v-model.number="formInline.yj_flow" placeholder="默认单位为M，精确到3位小数(无限制填：99999999)"></el-input>
      </el-form-item>
      <el-form-item prop="is_clear">
        <span slot="label">是否清零：</span>
        <el-select v-model="formInline.is_clear" placeholder="请选择">
          <el-option label="不清零" :value="0"></el-option>
          <el-option label="清零" :value="1"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="eff_period">
        <span slot="label">有效周期：</span>
        <el-select v-model="formInline.eff_period" placeholder="请选择">
          <el-option label="1个月" :value="0"></el-option>
          <el-option label="2个月" :value="1"></el-option>
          <el-option label="3个月" :value="2"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="real_flow">
        <span slot="label">实名认证成功赠送流量：</span>
        <el-input v-model.number="formInline.real_flow" placeholder="默认单位为M，精确到3位小数(无限制填：99999999)"></el-input>
      </el-form-item>
      <el-form-item prop="real_period">
        <span slot="label">实名认证成功赠送流量有效周期：</span>
        <el-select v-model="formInline.real_period" placeholder="请选择">
          <el-option label="1个月" :value="0"></el-option>
          <el-option label="2个月" :value="1"></el-option>
          <el-option label="3个月" :value="2"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="perf_flow">
        <span slot="label">完善资料成功赠送流量：</span>
        <el-input v-model.number="formInline.perf_flow" placeholder="默认单位为M，精确到3位小数(无限制填：99999999)"></el-input>
      </el-form-item>
      <el-form-item prop="perf_period">
        <span slot="label">完善资料成功赠送流量有效周期：</span>
        <el-select v-model="formInline.perf_period" placeholder="请选择">
          <el-option label="1个月" :value="0"></el-option>
          <el-option label="2个月" :value="1"></el-option>
          <el-option label="3个月" :value="2"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="bind_flow">
        <span slot="label">绑定设备成功赠送流量：</span>
        <el-input v-model.number="formInline.bind_flow" placeholder="默认单位为M，精确到3位小数(无限制填：99999999)"></el-input>
      </el-form-item>
      <el-form-item prop="bind_period">
        <span slot="label">绑定设备成功赠送流量有效周期：</span>
        <el-select v-model="formInline.bind_period" placeholder="请选择">
          <el-option label="1个月" :value="0"></el-option>
          <el-option label="2个月" :value="1"></el-option>
          <el-option label="3个月" :value="2"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <span slot="label">导入卡号：</span>
        <!-- 这里演示了如何在提交表单的时候再开始上传文件 -->
        <el-upload class="upload-demo" action="https://jsonplaceholder.typicode.com/posts/" :on-success="uploadHandleSuccess" :on-change="uploadHandleChange" ref="upload" :with-credentials="true" :limit="1" :file-list="fileList" :auto-upload="false">
          <el-button type="primary">点击上传</el-button>
          <div slot="tip" class="el-upload__tip">只能上传.txt、.xlsx文件</div>
        </el-upload>
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
export default {
  data() {
    return {
      formInline: {
        batch_code: '',
        batch_name: '',
        ks_name: '',
        ex_name: '',
        jg_name: '',
        city: '',
        tc_flow: '',
        months: '',
        yj_flow: '',
        is_clear: '',
        eff_period: '',
        real_flow: '',
        real_period: '',
        perf_flow: '',
        perf_period: '',
        bind_flow: '',
        bind_period: ''
      },
      fileList: [], // 文件上传列表
      rules: {
        batch_code: [{
          required: true,
          message: '请输入批次编号',
          trigger: 'blur'
        }],
        batch_name: [{
          required: true,
          message: '请输入批次名称',
          trigger: 'blur'
        }],
        ks_name: [{
          required: true,
          message: '请选择流量卡商',
          trigger: 'change'
        }],
        ex_name: [{
          required: true,
          message: '请输入出货人名',
          trigger: 'blur'
        }],
        jg_name: [{
          required: true,
          message: '请选择机构名称',
          trigger: 'change'
        }],
        city: [{
          required: true,
          message: '请选择地区',
          trigger: 'change'
        }, {
          validator: this.validateCity,
          trigger: 'blur'
        }],
        tc_flow: [{
          required: true,
          message: '请输入套餐流量',
          trigger: 'blur'
        }, {
          type: 'number',
          message: '只能输入数字',
          trigger: 'blur'
        }],
        months: [{
          required: true,
          message: '请选择分配月数',
          trigger: 'change'
        }],
        yj_flow: [{
          required: true,
          message: '请输入月均流量',
          trigger: 'blur'
        }, {
          type: 'number',
          message: '只能输入数字',
          trigger: 'blur'
        }],
        is_clear: [{
          required: true,
          message: '请选择是否清零',
          trigger: 'change'
        }],
        eff_period: [{
          required: true,
          message: '请选择有效周期',
          trigger: 'change'
        }]
      },
      // 地区选择的数据
      options: [{
        id: 1,
        name: '湖南',
        cities: []
      }],
      props: {
        value: 'id', // 最后选取的值
        label: 'name', // 值对应的名称
        children: 'cities'
      }
    }
  },
  mounted() {
    // 一进入页面就先拿到省
  },
  methods: {
    // 提交表单
    submitForm(formName) {
      console.log(this.formInline)
      this.$refs[formName].validate((valid) => {
        this.$refs.upload.submit() // 上传应当是验证通过才开始，测试阶段先放在外面
        if (valid) {
          // 验证通过
        } else {
          return false;
        }
      });
    },
    // 重置表单
    resetForm(formName) {
      // resetFields 只能重置需要验证的值
      this.$refs[formName].resetFields()
    },
    // 联级选择触发值变化
    handleCascaderChange(val) {
      console.log(val)
      if (val.length === 1) {
        if (val.includes(1) && !this.options[0].cities.length) {
          // 这里掉接口拿市
          this.options[0].cities = [{
            id: 11,
            name: '衡阳',
            cities: []
          }];
        }
      } else if (val.length === 2) {
        if (val.includes(11) && !this.options[0].cities[0].cities.length) {
          // 这里调接口拿区
          this.options[0].cities[0].cities = [{
            id: 111,
            name: '蒸湘区'
          }];
        }
      }
    },
    // 自定义验证城市选择（区为可选）
    validateCity(rule, value, callback) {
      if (value.length === 0) {
        callback(new Error('请选择省份'))
      } else if (value.length === 1) {
        callback(new Error('请选择城市'))
      } else {
        callback();
      }
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
}

</style>
