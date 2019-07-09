<template>
  <el-card class="batchcreate_container" shadow="never" v-loading="loadDate">
    <div slot="header" class="clearfix">
      <span>流量卡出货批次</span>
    </div>
    <el-form class="editor-form" :inline="false" :model="formInline" :rules="rules" ref="ruleForm" label-width="225px" size="small" @submit.native.prevent>
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
        <el-select v-model="formInline.org_id" filterable placeholder="请选择" :disabled="isUpdate">
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
        <el-input v-model="formInline.clw_batch_id" placeholder="请输入车联网批次" :disabled="isUpdate"></el-input>
        <div class="annotation">若需将车联网设备中的卡归属到本批次初始化信息中，需关联车联网出货批次编号</div>
      </el-form-item>
      <el-form-item prop="card_type" v-if="!isUpdate">
        <span slot="label">流量卡商：</span>
        <el-select v-model="formInline.card_type" filterable clearable placeholder="请选择流量卡商">
          <el-option v-for="(item, index) in cardTypes" :key="index" :label="item.label" :value="item.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="gprs_amount" :disabled="isUpdate">
        <span slot="label">套餐流量：</span>
        <el-input v-model="formInline.gprs_amount" @input="formInline.gprs_amount = limitNumber(formInline.gprs_amount, 8, 3)" :disabled="isUpdate" placeholder="请输入套餐流量"></el-input>
        <div class="annotation">默认单位为M，精确到3位小数(无限制填：99999999)</div>
      </el-form-item>
      <el-form-item prop="allot_month" :disabled="isUpdate">
        <span slot="label">分配月数：</span>
        <el-select v-model="formInline.allot_month" placeholder="请选择分配月数" @change="changeAllotMonth" :disabled="isUpdate">
          <el-option v-if="item.value >= 1 && item.value <= 48" v-for="(item, index) in liveMonthSelect" :key="index" :label="item.label" :disabled="isUpdate" :value="item.value - 0"></el-option>
        </el-select>
        <div class="annotation">月均流量：{{gprsMonth}}</div>
      </el-form-item>
      <el-form-item prop="allot_reset">
        <span slot="label">是否清零：</span>
        <el-select v-model="formInline.allot_reset" placeholder="请选择" :disabled="isUpdate">
          <el-option label="不清零" :value="0"></el-option>
          <el-option label="清零" :value="1"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="live_month">
        <span slot="label">有效周期：</span>
        <el-select v-model="formInline.live_month" placeholder="请选择有效周期" :disabled="isUpdate">
          <el-option v-for="(item, index) in liveMonthSelect" v-if="item.value >= 1" :disabled="item.value < formInline.allot_month" :key="index" :label="item.label" :value="item.value - 0"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="give_value">
        <span slot="label">实名认证成功赠送流量：</span>
        <el-input v-model="formInline.give_value" @input="formInline.give_value = limitNumber(formInline.give_value, 8, 3)" :disabled="isUpdate" placeholder="请输入"></el-input>
        <div class="annotation">默认单位为M，精确到3位小数(无限制填：99999999)</div>
      </el-form-item>
      <el-form-item prop="give_live_month">
        <span slot="label">实名认证成功赠送流量有效周期：</span>
        <el-select v-model="formInline.give_live_month" clearable placeholder="请选择有效周期" :disabled="isUpdate">
          <el-option label="无周期" :value="0" disabled></el-option>
          <el-option v-for="(item, index) in liveMonthSelect" v-if="item.value >= 1" :disabled="item.value < formInline.allot_month" :key="index" :label="item.label" :value="item.value - 0"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="wszl_value">
        <span slot="label">完善资料成功赠送流量：</span>
        <el-input v-model="formInline.wszl_value" @input="formInline.wszl_value = limitNumber(formInline.wszl_value, 8, 3)" :disabled="isUpdate" placeholder="请输入"></el-input>
        <div class="annotation">默认单位为M，精确到3位小数(无限制填：99999999)</div>
      </el-form-item>
      <el-form-item prop="wszl_live_month">
        <span slot="label">完善资料成功赠送流量有效周期：</span>
        <el-select v-model="formInline.wszl_live_month" clearable placeholder="请选择有效周期" :disabled="isUpdate">
          <el-option label="无周期" :value="0" disabled></el-option>
          <el-option v-for="(item, index) in liveMonthSelect" v-if="item.value >= 1" :disabled="item.value < formInline.allot_month" :key="index" :label="item.label" :value="item.value - 0"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="bind_value">
        <span slot="label">绑定设备成功赠送流量：</span>
        <el-input v-model="formInline.bind_value" @input="formInline.bind_value = limitNumber(formInline.bind_value, 8, 3)" :disabled="isUpdate" placeholder="请输入"></el-input>
        <div class="annotation">默认单位为M，精确到3位小数(无限制填：99999999)</div>
      </el-form-item>
      <el-form-item prop="bind_live_month">
        <span slot="label">绑定设备成功赠送流量有效周期：</span>
        <el-select v-model="formInline.bind_live_month" clearable placeholder="请选择有效周期" :disabled="isUpdate">
          <el-option label="无周期" :value="0" disabled></el-option>
          <el-option v-for="(item, index) in liveMonthSelect" v-if="item.value >= 1" :disabled="item.value < formInline.allot_month" :key="index" :label="item.label" :value="item.value - 0"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item v-if="!isUpdate">
        <span slot="label">导入卡号：</span>
        <!-- 这里演示了如何在提交表单的时候再开始上传文件 -->
        <el-upload ref="upload" action="" :on-change="uploadHandleChange" :on-exceed="fileExceed" :before-remove="beforeRemove" :with-credentials="true" :limit="1" :file-list="fileList" :auto-upload="false">
          <el-button type="primary">点击上传</el-button>
          <div slot="tip" class="el-upload__tip">只能上传.xlsx文件 <el-button type="text" @click="dialogXlsxPreviewVisible = true">(.xlsx格式预览)</el-button>
          </div>
        </el-upload>
      </el-form-item>
      <el-form-item>
        <el-button @click="$router.back()">返回</el-button>
        <el-button type="primary" @click="submitForm('ruleForm')">保存</el-button>
        <el-button type="warning" @click="resetForm('ruleForm')">重置</el-button>
      </el-form-item>
    </el-form>
    <el-dialog title="xlsx文件上传内容规范" :visible.sync="dialogXlsxPreviewVisible" width="700px">
      <div slot>
        <div id="xlsx_preview" style="width:100%;overflow: auto">
          <el-row class="title">
            <el-col :span="8">MSISDN</el-col>
            <el-col :span="8">ICCID</el-col>
            <el-col :span="8">IMSI</el-col>
          </el-row>
          <el-row>
            <el-col :span="8">861064604869453</el-col>
            <el-col :span="8">89860619000004125005</el-col>
            <el-col :span="8">460064520076453</el-col>
          </el-row>
          <el-row>
            <el-col :span="8">861064604868004</el-col>
            <el-col :span="8">89860619000004110510</el-col>
            <el-col :span="8">460064520075004</el-col>
          </el-row>
        </div>
      </div>
      <div slot="footer">
        <el-button size="small" type="warning" @click="download">xlsx案例文件下载</el-button>
      </div>
    </el-dialog>
  </el-card>
</template>
<script>
import Api from 'assets/js/api.js'
import { mapState } from 'vuex'

export default {
  data() {
    return {
      isUpdate: false,
      dialogXlsxPreviewVisible: false,
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
      let formInline = this.formInline
      delete formInline.city_id
      delete formInline.district_id
    },
    citySelect(id) {
      this.setRegionData(id, 'districtData')
      let formInline = this.formInline
      delete formInline.district_id
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
      this.loadDate = true
      _axios.send({
        method: 'get',
        url: _axios.ajaxAd.getBatchDetail,
        params: {
          batch_id: Api.UNITS.getQuery('batch_id')
        },
        done: ((res) => {
          this.loadDate = false
          this.formInline = res.data || {}
          this.$nextTick(() => {
            this.$refs.ruleForm.clearValidate()
          })
          if (this.formInline.province_id) this.setRegionData(this.formInline.province_id, 'cityData')
          if (this.formInline.city_id) this.setRegionData(this.formInline.city_id, 'districtData')
        })
      })
    },
    setRegionData(id, key) {
      this.getNations(id, (res) => {
        this[key] = res.data || []
      })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields()
      this.formInline = {
        allot_month: 1
      }
      this.fileList = []
      this.isUpdate && this.getData()
    },
    // 提交表单
    submitForm(formName) {
      console.log(this.formInline)
      this.$refs[formName].validate((valid) => {
        if (valid) {
          // 提交ajax
          if (this.isUpdate) { // 修改操作
            _axios.send({
              method: 'post',
              url: _axios.ajaxAd.updateBatch,
              data: this.formInline,
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
          } else { // 新增操作
            // 如果上传的文件列表为空
            if (this.fileList.length === 0) {
              this.showMsgBox({
                type: 'error',
                message: '请选择上传的文件'
              })
              return
            }
            // 验证通过先将上传的文件与formInline中数据整合
            this.formData = new FormData()
            this.formData.append('file', this.fileList[0].raw)
            for (let key in this.formInline) {
              this.formData.append(key, this.formInline[key])
            }
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
                  let data = res.data
                  this.$router.push({ name: 'cardbatch' })
                  setTimeout(() => {
                    this.showMsgBox({
                      type: 'success',
                      duration: 0,
                      message: `操作成功！
                      批次号 ${data.batch_sn} ，
                      本次导入 ${data.iccid_count} 张手机卡，
                      导入成功 ${data.success_count} 张，
                      失败 ${data.failed_count} 张，
                      更新 ${data.update_count} 张`
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
    beforeRemove(file) {
      this.fileList = []
    },
    // 上传文件超出上限的钩子
    fileExceed() {
      this.showMsgBox({
        type: 'error',
        message: '一次最多只能上传一个文件，如想修改上传的文件请先删除原文件！'
      })
    },
    // 处理上传文件(同时上传一个文件)
    uploadHandleChange(file, fileList) {
      this.fileList = fileList
      let type = file.name
      if (!/\.xlsx$/.test(type)) {
        this.showMsgBox({
          type: 'error',
          message: '只能上传.xlsx格式文件！'
        })
        this.fileList = []
        return false
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
    // 下载xlsx模板文件
    download() {
      window.open('../../../flowCenter/static/template.xlsx')
    }
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

  #xlsx_preview {
    .title {
      .el-col {
        font-weight: bold;
      }
    }

    .el-col {
      line-height: 30px;
      text-align: center;
    }
  }
}

</style>
