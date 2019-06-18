<template>
  <el-card class="batchcreate_container" shadow="never">
    <div slot="header" class="clearfix">
      <span>充值套餐设置</span>
    </div>
    <el-form :inline="false" :model="formInline" :rules="rules" ref="ruleForm" label-width="120px" size="small" :status-icon="true">
      <el-form-item prop="jg_name">
        <span slot="label">机构名称：</span>
        <el-select v-model="formInline.org_id" filterable placeholder="请选择">
          <el-option v-for="(item, index) in orgs" :key="index" :label="item.label" :value="item.value - 0"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="model">
        <span slot="label">套餐模式：</span>
        <el-radio v-model="formInline.model" label="1">叠加</el-radio>
        <el-radio v-model="formInline.model" label="2">延期</el-radio>
      </el-form-item>
      <el-form-item prop="tc_name">
        <span slot="label">套餐名称：</span>
        <el-input v-model="formInline.tc_name" placeholder="请输入套餐名称"></el-input>
        <div class="annotation">若为空将根据套餐情况规则组合名称</div>
      </el-form-item>
      <el-form-item prop="tc_flow">
        <span slot="label">套餐流量：</span>
        <el-input v-model="formInline.tc_flow" @input="formInline.tc_flow = limitNumber(formInline.tc_flow)" placeholder="请输入套餐流量"></el-input>
        <div class="annotation">默认单位为M，精确到3位小数(无限制填：99999999)</div>
      </el-form-item>
      <el-form-item prop="tc_price">
        <span slot="label">套餐价格：</span>
        <el-input v-model="formInline.tc_price" @input="formInline.tc_price = limitNumber(formInline.tc_price, 5)" placeholder="请输入套餐价格"></el-input>
        <div class="annotation">最多精确到3位小数</div>
      </el-form-item>
      <el-form-item prop="tc_discount">
        <span slot="label">套餐折扣：</span>
        <el-input v-model="formInline.tc_discount" @input="formInline.tc_discount = limitNumber(formInline.tc_discount, 1)" placeholder="请输入套餐折扣"></el-input>
        <div class="annotation">折扣范围0.1至1，0.5代表打5折，1为不打折</div>
      </el-form-item>
      <el-form-item prop="type">
        <span slot="label">套餐类型：</span>
        <el-radio v-model="formInline.type" label="1">固定套餐</el-radio>
        <el-radio v-model="formInline.type" label="2">月均套餐</el-radio>
      </el-form-item>
      <el-form-item prop="fp_month">
        <span slot="label">分配月数：</span>
        <el-select v-model="formInline.fp_month" placeholder="请选择分配月数">
          <el-option v-if="item.value >= 1 && item.value <= 48" v-for="(item, index) in liveMonthSelect" :key="index" :label="item.label" :value="item.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="is_clear">
        <span slot="label">是否清零：</span>
        <el-radio v-model="formInline.is_clear" label="1">不清零</el-radio>
        <el-radio v-model="formInline.is_clear" label="2">会清零</el-radio>
      </el-form-item>
      <el-form-item prop="live_month">
        <span slot="label">有效周期：</span>
        <el-select v-model="formInline.live_month" placeholder="请选择有效周期">
          <el-option v-for="(item, index) in liveMonthSelect" :key="index" :label="item.label" :value="item.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="rebate_money">
        <span slot="label">返利金额：</span>
        <el-input v-model="formInline.rebate_money" @input="formInline.rebate_money = limitNumber(formInline.rebate_money, 5)" placeholder="请输入返利金额"></el-input>
        <div class="annotation">填 0 代表套餐无返利，将根据机构返利情况计算</div>
      </el-form-item>
      <el-form-item prop="tc_remark">
        <span slot="label">套餐备注：</span>
        <el-input type="textarea" v-model="formInline.tc_remark" placeholder="请输入套餐备注" rows="4"></el-input>
        <div class="annotation">显示在充值处给用户看的套餐详细描述</div>
      </el-form-item>
      <el-form-item prop="is_recommend">
        <span slot="label">是否推荐：</span>
        <el-select v-model="formInline.is_recommend" placeholder="请选择是否推荐">
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
      formInline: {},
      rules: {
        org_id: [{
          required: true,
          message: '请输入批次名称',
          trigger: 'blur'
        }],
        model: [{
          required: true,
          message: '请选择套餐模式',
          trigger: 'change'
        }],
        tc_flow: [{
          required: true,
          message: '请输入套餐流量',
          trigger: 'blur'
        }],
        tc_price: [{
          required: true,
          message: '请输入套餐价格',
          trigger: 'blur'
        }],
        tc_discount: [{
          required: true,
          message: '请输入套餐折扣',
          trigger: 'blur'
        }, {
          validator: this.limitNumberSize,
          trigger: 'blur'
        }],
        type: [{
          required: true,
          message: '请选择套餐类型',
          trigger: 'change'
        }],
        fp_month: [{
          required: true,
          message: '请选择分配月数',
          trigger: 'change'
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
      }
    }
  },
  mounted() {

  },
  methods: {
    // 提交表单
    submitForm(formName) {
      console.log(this.formInline)
      this.$refs[formName].validate((valid) => {
        if (valid) {
          // 验证通过
        } else {
          Api.UNITS.showMsgBox()
          return false;
        }
      });
    },
    // 重置表单
    resetForm(formName) {
      // resetFields 只能重置需要验证的值
      this.$refs[formName].resetFields()
    },
    limitNumber: Api.UNITS.limitNumber,
    limitNumberSize(rule, value, callback) {
      if (value <= 0 || value > 1) {
        callback(new Error('折扣值必须在0.001~1之间'))
      } else {
        callback()
      }
    }
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
