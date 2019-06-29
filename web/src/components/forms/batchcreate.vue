<template>
  <el-card class="batchcreate_container" shadow="never">
    <div slot="header" class="clearfix">
      <span>流量卡出货批次</span>
    </div>
    <el-form class="editor-form" :inline="false" :model="formInline" :rules="rules" ref="ruleForm" label-width="225px" size="small" :status-icon="true">
      <el-form-item prop="batch_code">
        <span slot="label">批次编号：</span>
        <el-input v-model="formInline.batch_code" placeholder="请输入批次编号"></el-input>
      </el-form-item>
      <el-form-item prop="batch_name">
        <span slot="label">批次名称：</span>
        <el-input v-model="formInline.batch_name" placeholder="请输入批次名称"></el-input>
      </el-form-item>
      <el-form-item prop="batch_remark">
        <span slot="label">批次备注：</span>
        <el-input type="textarea" v-model="formInline.batch_remark" placeholder="请输入批次备注" rows="4"></el-input>
      </el-form-item>
      <el-form-item prop="ex_name">
        <span slot="label">出货人名：</span>
        <el-input v-model="formInline.ex_name" placeholder="请输入出货人名"></el-input>
      </el-form-item>
      <el-form-item prop="jg_name">
        <span slot="label">机构名称：</span>
        <el-select v-model="formInline.jg_name" filterable placeholder="请选择">
          <el-option v-for="(item, index) in orgs" :key="index" :label="item.label" :value="item.value - 0"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="city">
        <span slot="label">销往地区：</span>
        <el-cascader change-on-select :options="options" @change="handleCascaderChange" :props="props" v-model="formInline.city"></el-cascader>
      </el-form-item>
      <el-form-item prop="batch_clw">
        <span slot="label">车联网批次：</span>
        <el-input v-model="formInline.batch_clw" placeholder="请输入车联网批次"></el-input>
        <div class="annotation">若需将车联网设备中的卡归属到本批次初始化信息中，需关联车联网出货批次编号</div>
      </el-form-item>
      <el-form-item label="流量卡商：">
        <el-select v-model="formInline.ks_name" filterable clearable placeholder="请选择流量卡商">
          <el-option v-for="(item, index) in cardTypes" :key="index" :label="item.label" :value="item.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="tc_flow">
        <span slot="label">套餐流量：</span>
        <el-input v-model="formInline.tc_flow" @input="formInline.tc_flow = limitNumber(formInline.tc_flow, 8, 3)" placeholder="请输入套餐流量"></el-input>
        <div class="annotation">默认单位为M，精确到3位小数(无限制填：99999999)</div>
      </el-form-item>
      <el-form-item prop="months">
        <span slot="label">分配月数：</span>
        <el-select v-model="formInline.months" placeholder="请选择分配月数">
          <el-option v-if="item.value >= 1 && item.value <= 48" v-for="(item, index) in liveMonthSelect" :key="index" :label="item.label" :value="item.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="yj_flow">
        <span slot="label">月均流量：</span>
        <el-input v-model="formInline.yj_flow" @input="formInline.yj_flow = limitNumber(formInline.yj_flow, 8, 3)" placeholder="请输入月均流量"></el-input>
        <div class="annotation">默认单位为M，精确到3位小数(无限制填：99999999)</div>
      </el-form-item>
      <el-form-item prop="is_clear">
        <span slot="label">是否清零：</span>
        <el-select v-model="formInline.is_clear" placeholder="请选择">
          <el-option label="不清零" :value="0"></el-option>
          <el-option label="清零" :value="1"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="live_month">
        <span slot="label">有效周期：</span>
        <el-select v-model="formInline.live_month" placeholder="请选择有效周期">
          <el-option v-for="(item, index) in liveMonthSelect" :key="index" :label="item.label" :value="item.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="real_flow">
        <span slot="label">实名认证成功赠送流量：</span>
        <el-input v-model="formInline.real_flow" @input="formInline.real_flow = limitNumber(formInline.real_flow, 8, 3)" placeholder="请输入"></el-input>
        <div class="annotation">默认单位为M，精确到3位小数(无限制填：99999999)</div>
      </el-form-item>
      <el-form-item prop="real_period">
        <span slot="label">实名认证成功赠送流量有效周期：</span>
        <el-select v-model="formInline.live_month" clearable placeholder="请选择有效周期">
          <el-option v-for="(item, index) in liveMonthSelect" :key="index" :label="item.label" :value="item.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="perf_flow">
        <span slot="label">完善资料成功赠送流量：</span>
        <el-input v-model="formInline.perf_flow" @input="formInline.perf_flow = limitNumber(formInline.perf_flow, 8, 3)" placeholder="请输入"></el-input>
        <div class="annotation">默认单位为M，精确到3位小数(无限制填：99999999)</div>
      </el-form-item>
      <el-form-item prop="perf_period">
        <span slot="label">完善资料成功赠送流量有效周期：</span>
        <el-select v-model="formInline.live_month" clearable placeholder="请选择有效周期">
          <el-option v-for="(item, index) in liveMonthSelect" :key="index" :label="item.label" :value="item.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="bind_flow">
        <span slot="label">绑定设备成功赠送流量：</span>
        <el-input v-model="formInline.bind_flow" @input="formInline.bind_flow = limitNumber(formInline.bind_flow, 8, 3)" placeholder="请输入"></el-input>
        <div class="annotation">默认单位为M，精确到3位小数(无限制填：99999999)</div>
      </el-form-item>
      <el-form-item prop="bind_period">
        <span slot="label">绑定设备成功赠送流量有效周期：</span>
        <el-select v-model="formInline.live_month" clearable placeholder="请选择有效周期">
          <el-option v-for="(item, index) in liveMonthSelect" :key="index" :label="item.label" :value="item.value"></el-option>
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
import Api from 'assets/js/api.js'
import { mapState } from 'vuex'

export default {
  data() {
    return {
      formInline: {},
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
        }],
        is_clear: [{
          required: true,
          message: '请选择是否清零',
          trigger: 'change'
        }],
        live_month: [{
          required: true,
          message: '请选择有效周期',
          trigger: 'change'
        }]
      },
      // 地区选择的数据
      options: [],
      props: {
        value: 'value', // 最后选取的值
        label: 'label', // 值对应的名称
        children: 'cities'
      }
    }
  },
  mounted() {
    this.getNations(1, (res) => {
      let data = res.data || []
      data.forEach((v) => { v.cities = [] })
      // 要先添加cities属性，这样才能watch它的变化
      this.options = data
    })
  },
  methods: {
    getNations(parent = 1, cb) {
      _axios.send({
        method: 'get',
        url: _axios.ajaxAd.getNationSelect,
        params: { parent },
        done: ((res) => {
          cb && cb(res)
        })
      })
    },
    // 联级选择触发值变化
    handleCascaderChange(val) {
      if (val.length === 1) {
        this.options.forEach((v, i) => {
          if (v.value === val[0]) {
            if (v.cities.length === 0) {
              this.getNations(v.value, (res) => {
                let data = res.data || []
                data.forEach((v1) => { v1.cities = [] })
                v.cities = data
              })
            }
          }
        })
      } else if (val.length === 2) {
        this.options.forEach((v, i) => {
          if (v.value === val[0]) {
            v.cities.forEach((v1, i1) => {
              if (v1.value === val[1]) {
                if (v1.cities.length === 0) {
                  this.getNations(v1.value, (res) => {
                    v1.cities = res.data || []
                  })
                }
              }
            })
          }
        })
      }
    },
    // 提交表单
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          // 验证通过
          this.$refs.upload.submit()
        } else {
          Api.UNITS.showMsgBox()
          return false;
        }
      });
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
          this.showMsgBox({
            type: 'error',
            message: '只能上传.txt/.xlsx文件！'
          })
          this.fileList = []
          return false
        }
      }
    },
    // 文件上传成功回调函数
    uploadHandleSuccess(res, file) {
      this.showMsgBox({
        type: 'success',
        message: '文件上传成功！'
      })
    },
    limitNumber: Api.UNITS.limitNumber
  },
  computed: {
    ...mapState({
      orgs: 'orgs',
      cardTypes: 'cardTypes',
      liveMonthSelect: 'liveMonthSelect',
      months: 'months'
    })
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
