<template>
  <el-card class="batchcreate_container" shadow="never">
    <div slot="header" class="clearfix">
      <span>充值套餐设置</span>
    </div>
    <el-form class="editor-form" :inline="false" :model="formInline" :rules="rules" ref="ruleForm" label-width="120px" size="small" :status-icon="true" v-loading="loadData">
      <el-form-item prop="org_id">
        <span slot="label">机构名称：</span>
        <el-select v-model="formInline.org_id" filterable placeholder="请选择机构">
          <el-option v-for="(item, index) in orgs" :key="index" :label="item.label" :value="item.value - 0"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="pack_mode">
        <span slot="label">套餐模式：</span>
        <el-radio v-model="formInline.pack_mode" :label="0">叠加</el-radio>
        <el-radio v-model="formInline.pack_mode" :label="1">延期</el-radio>
      </el-form-item>
      <el-form-item prop="pack_name">
        <span slot="label">套餐名称：</span>
        <el-input v-model="formInline.pack_name" placeholder="请输入套餐名称"></el-input>
        <!-- <div class="annotation">若为空将根据套餐情况规则组合名称</div> -->
      </el-form-item>
      <el-form-item prop="gprs_amount">
        <span slot="label">套餐流量：</span>
        <el-input v-model="formInline.gprs_amount" @input="formInline.gprs_amount = limitNumber(formInline.gprs_amount)" placeholder="请输入套餐流量"></el-input>
        <div class="annotation">默认单位为M，精确到3位小数(无限制填：99999999)</div>
      </el-form-item>
      <el-form-item prop="gprs_price">
        <span slot="label">套餐价格：</span>
        <el-input v-model="formInline.gprs_price" @input="formInline.gprs_price = limitNumber(formInline.gprs_price, 5)" placeholder="请输入套餐价格"></el-input>
        <div class="annotation">最多精确到3位小数</div>
      </el-form-item>
      <el-form-item prop="gprs_discount">
        <span slot="label">套餐折扣：</span>
        <el-input v-model="formInline.gprs_discount" @input="formInline.gprs_discount = limitNumber(formInline.gprs_discount, 1)" placeholder="请输入套餐折扣"></el-input>
        <div class="annotation">折扣范围0.1至1，0.5代表打5折，1为不打折</div>
      </el-form-item>
      <el-form-item prop="allot">
        <span slot="label">套餐类型：</span>
        <el-radio v-model="formInline.allot" :label="1" @change="formInline.allot_month=1">固定套餐</el-radio>
        <el-radio v-model="formInline.allot" :label="0" @change="formInline.allot_month=2">月均套餐</el-radio>
      </el-form-item>
      <el-form-item prop="allot_month">
        <span slot="label">分配月数：</span>
        <el-select v-model="formInline.allot_month" placeholder="请选择分配月数" :disabled="formInline.allot==1" @change="changeAllotMonth">
          <el-option v-if="item.value >= 1 && item.value <= 48" v-for="(item, index) in liveMonthSelect" :disabled="formInline.allot==0&&item.value==1" :key="index" :label="item.label" :value="item.value - 0"></el-option>
        </el-select>
        <div class="annotation">月均流量：{{gprsMonth}}</div>
      </el-form-item>
      <el-form-item prop="allot_reset">
        <span slot="label">是否清零：</span>
        <el-radio v-model="formInline.allot_reset" :label="0">不清零</el-radio>
        <el-radio v-model="formInline.allot_reset" :label="1">会清零</el-radio>
      </el-form-item>
      <el-form-item prop="live_month">
        <span slot="label">有效周期：</span>
        <el-select v-model="formInline.live_month" placeholder="请选择有效周期">
          <el-option v-for="(item, index) in liveMonthSelect" v-if="item.value >= 1" :disabled="item.value < formInline.allot_month" :key="index" :label="item.label" :value="item.value - 0"></el-option>
        </el-select>
        <div class="annotation">有效周期时间不能比分配月数短</div>
      </el-form-item>
      <el-form-item prop="pack_rebate">
        <span slot="label">返利金额：</span>
        <el-input v-model="formInline.pack_rebate" @input="formInline.pack_rebate = limitNumber(formInline.pack_rebate, 5)" placeholder="请输入返利金额"></el-input>
        <div class="annotation">填 0 代表套餐无返利，将根据机构返利情况计算</div>
      </el-form-item>
      <el-form-item prop="pack_memo">
        <span slot="label">套餐备注：</span>
        <el-input type="textarea" v-model="formInline.pack_memo" placeholder="请输入套餐备注" rows="4"></el-input>
        <div class="annotation">显示在充值处给用户看的套餐详细描述</div>
      </el-form-item>
      <el-form-item prop="pack_recom">
        <span slot="label">是否推荐：</span>
        <el-select v-model="formInline.pack_recom" clearable placeholder="请选择是否推荐">
          <el-option label="否" :value="0"></el-option>
          <el-option label="是" :value="1"></el-option>
        </el-select>
        <div class="annotation">用户充值选择套餐处显示推荐</div>
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
      loadData: false,
      isUpdate: false,
      formInline: {
        pack_id: Api.UNITS.getQuery('pack_id'),
        allot_month: 1,
        allot: 1,
        allot_reset: 0,
        pack_mode: 0
      },
      rules: {
        org_id: [{
          required: true,
          message: '请输入批次名称',
          trigger: 'blur'
        }],
        pack_mode: [{
          required: true,
          message: '请选择套餐模式',
          trigger: 'change'
        }],
        pack_name: [{
          required: true,
          message: '请输入套餐名称',
          trigger: 'blur'
        }],
        gprs_amount: [{
          required: true,
          message: '请输入套餐流量',
          trigger: 'blur'
        }],
        gprs_price: [{
          required: true,
          message: '请输入套餐价格',
          trigger: 'blur'
        }],
        gprs_discount: [{
          required: true,
          message: '请输入套餐折扣',
          trigger: 'blur'
        }, {
          validator: this.limitNumberSize,
          trigger: 'blur'
        }],
        allot: [{
          required: true,
          message: '请选择套餐类型',
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
        }],
        pack_rebate: [{
          validator: this.limitPackRebate,
          trigger: 'blur'
        }]
      }
    }
  },
  mounted() {
    this.isUpdate = Api.UNITS.getQuery('type') === 'update'
    if (this.isUpdate) {
      this.getData()
    }
  },
  methods: {
    // 提交表单
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          // 验证通过
          this.formInline.allot_value = (this.formInline.gprs_amount / this.formInline.allot_month).toFixed(3)
          if (this.isUpdate) {
            _axios.send({
              method: 'post',
              url: _axios.ajaxAd.updateGprsCombo,
              data: this.formInline,
              done: ((res) => {
                this.$router.push({ name: 'cardcombo' })
                setTimeout(() => {
                  this.$message.success(res.msg || '新增成功')
                }, 150)
              })
            })
          } else {
            _axios.send({
              method: 'post',
              url: _axios.ajaxAd.addGprsCombo,
              data: this.formInline,
              done: ((res) => {
                this.$router.push({ name: 'cardcombo' })
                setTimeout(() => {
                  this.$message.success(res.msg || '编辑成功')
                }, 150)
              })
            })
          }
        } else {
          Api.UNITS.showMsgBox()
          return false;
        }
      })
    },
    // 重置表单
    resetForm(formName) {
      // resetFields 只能重置需要验证的值
      this.$refs[formName].resetFields()
      this.formInline = {
        pack_id: Api.UNITS.getQuery('pack_id'),
        allot_month: 1,
        allot: 1,
        allot_reset: 0,
        pack_mode: 0
      }
      this.isUpdate && this.getData()
    },
    getData() {
      this.loadData = true
      _axios.send({
        method: 'get',
        url: _axios.ajaxAd.getComboDetail,
        params: { pack_id: Api.UNITS.getQuery('pack_id') },
        done: ((res) => {
          this.formInline = res.data
          if (res.data.allot_month == 1) {
            // 固定套餐
            this.formInline.allot = 1
          } else {
            this.formInline.allot = 0
          }
          this.loadData = false
          Vue.nextTick(() => {
            this.$refs.ruleForm.clearValidate(['live_month'])
          })
        })
      })
    },
    // 当分配月数发生改变时有效周期可能要改变
    changeAllotMonth() {
      if (this.formInline.live_month) {
        if (this.formInline.allot_month > this.formInline.live_month) {
          this.formInline.live_month = this.formInline.allot_month
        }
      }
    },
    limitNumberSize(rule, value, callback) {
      if (value <= 0 || value > 1) {
        callback(new Error('折扣值必须在0.001~1之间'))
      } else {
        callback()
      }
    },
    limitPackRebate(rule, value, callback) {
      if (Number(value) > Number(this.formInline.gprs_price || 0)) {
        callback(new Error('返利金额不能大于套餐价格'))
      } else {
        callback()
      }
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
}

</style>
