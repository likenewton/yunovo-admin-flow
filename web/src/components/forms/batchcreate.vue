<template>
  <el-card class="batchcreate_container" shadow="never">
    <div slot="header" class="clearfix">
      <span>流量卡出货批次</span>
    </div>
    <el-form class="editor-form" :inline="false" :model="formInline" :rules="rules" ref="ruleForm" label-width="225px" size="small">
      <el-form-item prop="batch_sn">
        <span slot="label">批次编号：</span>
        <el-input v-model="formInline.batch_sn" placeholder="请输入批次编号"></el-input>
      </el-form-item>
      <el-form-item prop="batch_name">
        <span slot="label">批次名称：</span>
        <el-input v-model="formInline.batch_name" placeholder="请输入批次名称"></el-input>
      </el-form-item>
      <el-form-item prop="batch_memo">
        <span slot="label">批次备注：</span>
        <el-input type="textarea" v-model="formInline.batch_memo" placeholder="请输入批次备注" rows="4"></el-input>
      </el-form-item>
      <el-form-item prop="batch_shipper">
        <span slot="label">出货人名：</span>
        <el-input v-model="formInline.batch_shipper" placeholder="请输入出货人名"></el-input>
      </el-form-item>
      <el-form-item prop="org_id">
        <span slot="label">机构名称：</span>
        <el-select v-model="formInline.org_id" filterable placeholder="请选择">
          <el-option v-for="(item, index) in orgs" :key="index" :label="item.label" :value="item.value - 0"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="province_id">
        <span slot="label">销往省份：</span>
        <el-select v-model="formInline.province_id" filterable placeholder="请选择" @change="provinceSelect">
          <el-option v-for="(item, index) in provinceData" :key="index" :label="item.label" :value="item.value - 0"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="city_id">
        <span slot="label">销往城市：</span>
        <el-select v-model="formInline.city_id" :disabled="!formInline.province_id" filterable placeholder="请先选择省份" @change="citySelect">
          <el-option v-for="(item, index) in cityData" :key="index" :label="item.label" :value="item.value - 0"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="district_id">
        <span slot="label">销往区县：</span>
        <el-select v-model="formInline.district_id" :disabled="!formInline.city_id" filterable clearable placeholder="请先选择城市">
          <el-option v-for="(item, index) in districtData" :key="index" :label="item.label" :value="item.value - 0"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="clw_batch_id">
        <span slot="label">车联网批次：</span>
        <el-input v-model="formInline.clw_batch_id" placeholder="请输入车联网批次"></el-input>
        <div class="annotation">若需将车联网设备中的卡归属到本批次初始化信息中，需关联车联网出货批次编号</div>
      </el-form-item>
      <el-form-item prop="card_type">
        <span slot="label">流量卡商：</span>
        <el-select v-model="formInline.card_type" filterable clearable placeholder="请选择流量卡商">
          <el-option v-for="(item, index) in cardTypes" :key="index" :label="item.label" :value="item.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="gprs_amount">
        <span slot="label">套餐流量：</span>
        <el-input v-model="formInline.gprs_amount" @input="formInline.gprs_amount = limitNumber(formInline.gprs_amount, 8, 3)" placeholder="请输入套餐流量"></el-input>
        <div class="annotation">默认单位为M，精确到3位小数(无限制填：99999999)</div>
      </el-form-item>
      <el-form-item prop="allot_month">
        <span slot="label">分配月数：</span>
        <el-select v-model="formInline.allot_month" placeholder="请选择分配月数" @change="changeAllotMonth">
          <el-option v-if="item.value >= 1 && item.value <= 48" v-for="(item, index) in liveMonthSelect" :key="index" :label="item.label" :value="item.value - 0"></el-option>
        </el-select>
        <div class="annotation">月均流量：{{gprsMonth}}</div>
      </el-form-item>
      <el-form-item prop="allot_reset">
        <span slot="label">是否清零：</span>
        <el-select v-model="formInline.allot_reset" placeholder="请选择">
          <el-option label="不清零" :value="0"></el-option>
          <el-option label="清零" :value="1"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="live_month">
        <span slot="label">有效周期：</span>
        <el-select v-model="formInline.live_month" placeholder="请选择有效周期">
          <el-option v-for="(item, index) in liveMonthSelect" v-if="item.value >= 1" :disabled="item.value < formInline.allot_month" :key="index" :label="item.label" :value="item.value - 0"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="give_value">
        <span slot="label">实名认证成功赠送流量：</span>
        <el-input v-model="formInline.give_value" @input="formInline.give_value = limitNumber(formInline.give_value, 8, 3)" placeholder="请输入"></el-input>
        <div class="annotation">默认单位为M，精确到3位小数(无限制填：99999999)</div>
      </el-form-item>
      <el-form-item prop="give_live_month">
        <span slot="label">实名认证成功赠送流量有效周期：</span>
        <el-select v-model="formInline.give_live_month" clearable placeholder="请选择有效周期">
          <el-option v-for="(item, index) in liveMonthSelect" v-if="item.value >= 1" :disabled="item.value < formInline.allot_month" :key="index" :label="item.label" :value="item.value - 0"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="wszl_value">
        <span slot="label">完善资料成功赠送流量：</span>
        <el-input v-model="formInline.wszl_value" @input="formInline.wszl_value = limitNumber(formInline.wszl_value, 8, 3)" placeholder="请输入"></el-input>
        <div class="annotation">默认单位为M，精确到3位小数(无限制填：99999999)</div>
      </el-form-item>
      <el-form-item prop="wszl_live_month">
        <span slot="label">完善资料成功赠送流量有效周期：</span>
        <el-select v-model="formInline.wszl_live_month" clearable placeholder="请选择有效周期">
          <el-option v-for="(item, index) in liveMonthSelect" v-if="item.value >= 1" :disabled="item.value < formInline.allot_month" :key="index" :label="item.label" :value="item.value - 0"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="bind_value">
        <span slot="label">绑定设备成功赠送流量：</span>
        <el-input v-model="formInline.bind_value" @input="formInline.bind_value = limitNumber(formInline.bind_value, 8, 3)" placeholder="请输入"></el-input>
        <div class="annotation">默认单位为M，精确到3位小数(无限制填：99999999)</div>
      </el-form-item>
      <el-form-item prop="bind_live_month">
        <span slot="label">绑定设备成功赠送流量有效周期：</span>
        <el-select v-model="formInline.bind_live_month" clearable placeholder="请选择有效周期">
          <el-option v-for="(item, index) in liveMonthSelect" v-if="item.value >= 1" :disabled="item.value < formInline.allot_month" :key="index" :label="item.label" :value="item.value - 0"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <span slot="label">导入卡号：</span>
        <!-- 这里演示了如何在提交表单的时候再开始上传文件 -->
        <el-upload ref="upload" action="" :on-change="uploadHandleChange" :before-upload="beforeUpload" :with-credentials="true" :limit="2" :file-list="fileList" :auto-upload="false">
          <el-button type="primary">点击上传</el-button>
          <div slot="tip" class="el-upload__tip">只能上传.xlsx文件</div>
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
      isUpdate: false,
      provinceData: [], // 省份
      cityData: [], // 城市
      districtData: [], // 县区
      formInline: {
        allot_month: 1
      },
      formData: new FormData(), // new FormData() 对象
      fileList: [], // 文件上传列表
      rules: {
        batch_sn: [{
          required: true,
          message: '请输入批次编号',
          trigger: 'blur'
        }, {
          max: 50,
          message: '批次编号不能多于50个字符',
          trigger: ['blur', 'change']
        }],
        batch_name: [{
          required: true,
          message: '请输入批次名称',
          trigger: 'blur'
        }, {
          max: 50,
          message: '批次名称不能多于50个字符',
          trigger: ['blur', 'change']
        }],
        batch_memo: [{
          max: 200,
          message: '备注不能多于200个字符',
          trigger: ['blur', 'change']
        }],
        batch_shipper: [{
          required: true,
          message: '请输入出货人名',
          trigger: 'blur'
        }, {
          max: 50,
          message: '出货人名不能多于50个字符',
          trigger: ['blur', 'change']
        }],
        card_type: [{
          required: true,
          message: '请选择流量卡商',
          trigger: 'change'
        }],
        org_id: [{
          required: true,
          message: '请选择机构名称',
          trigger: 'change'
        }],
        province_id: [{
          required: true,
          message: '请选择省份',
          trigger: 'change'
        }],
        city_id: [{
          required: true,
          message: '请选择城市',
          trigger: 'change'
        }],
        gprs_amount: [{
          required: true,
          message: '请输入套餐流量',
          trigger: 'blur'
        }],
        allot_month: [{
          required: true,
          message: '请选择分配月数',
          trigger: 'change'
        }],
        allot_reset: [{
          required: true,
          message: '请选择是否清零',
          trigger: 'change'
        }],
        live_month: [{
          required: true,
          message: '请选择有效周期',
          trigger: 'change'
        }]
      }
    }
  },
  mounted() {
    this.isUpdate = Api.UNITS.getQuery('type') === 'update'
    this.setRegionData(1, 'provinceData')
    if (this.isUpdate) {
      this.getData()
    }
  },
  methods: {
    // 省级选择
    provinceSelect(id) {
      this.setRegionData(id, 'cityData')
      delete this.formInline.city_id
      delete this.formInline.district_id
    },
    citySelect(id) {
      this.setRegionData(id, 'districtData')
      delete this.formInline.district_id
    },
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
    // 获取修改数据
    getData() {
      if (this.formInline.province_id) this.setRegionData(this.formInline.province_id, 'cityData')
      if (this.formInline.city_id) this.setRegionData(this.formInline.city_id, 'districtData')
    },
    setRegionData(id, key) {
      this.getNations(id, (res) => {
        this[key] = res.data || []
      })
    },
    // 提交表单
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          // 验证通过先将上传的文件与formInline中数据整合
          this.$refs.upload.submit()
          // 提交ajax
          if (this.isUpdate) { // 修改操作

          } else { // 新增操作
            _axios.send({
              method: 'post',
              url: _axios.ajaxAd.addBatch,
              data: this.formData,
              headers: {
                'Content-Type': 'multipart/form-data'
              },
              done: ((res) => {
                if (res.status === 400) {
                  this.formInline[res.data] = ''
                  this.$refs.ruleForm.validateField([res.data])
                } else {
                  this.$router.push({ name: 'cardbatch' })
                  setTimeout(() => {
                    this.showMsgBox({
                      type: 'success',
                      message: `操作成功！`
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
      });
    },
    // 文件提交之前的钩子
    beforeUpload(file) {
      // 这里面的内容是同步的
      this.formData = new FormData()
      this.formData.append('file', file, file.name)
      for (let key in this.formInline) {
        this.formData.append(key, this.formInline[key])
      }
      return false
    },
    // 处理上传文件(同时上传一个文件)
    uploadHandleChange(file, fileList) {
      console.log(0)
      if (fileList.length <= 1) {
        if (fileList[0].raw) {
          let type = fileList[0].raw.name
          if (!/\.xlsx$/.test(type)) {
            this.showMsgBox({
              type: 'error',
              message: '只能上传.xlsx格式文件！'
            })
            this.fileList = []
            return false
          }
        }
      } else {
        this.fileList = [fileList[0]]
        this.showMsgBox({
          type: 'error',
          message: '一次只能上传一个文件'
        })
      }
    },
    // 当分配月数发生改变时有效周期可能要改变
    changeAllotMonth() {
      if (this.formInline.live_month) {
        if (this.formInline.allot_month > this.formInline.live_month) {
          this.formInline.live_month = this.formInline.allot_month
        }
      }
      if (this.formInline.give_live_month) {
        if (this.formInline.allot_month > this.formInline.give_live_month) {
          this.formInline.give_live_month = this.formInline.allot_month
        }
      }
      if (this.formInline.wszl_live_month) {
        if (this.formInline.allot_month > this.formInline.wszl_live_month) {
          this.formInline.wszl_live_month = this.formInline.allot_month
        }
      }
      if (this.formInline.bind_live_month) {
        if (this.formInline.allot_month > this.formInline.bind_live_month) {
          this.formInline.bind_live_month = this.formInline.allot_month
        }
      }
    },
  },
  computed: {
    gprsMonth() {
      let total = this.formInline.gprs_amount || 0
      let month = this.formInline.allot_month || 1
      return Api.UNITS.formatFlowUnit(total / month, 3, false)
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
