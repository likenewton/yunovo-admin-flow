<template>
  <div>
    <el-card class="reset-card" shadow="never">
      <el-tabs @tab-click="changeTab">
        <el-tab-pane>
          <span slot="label">赠送流量</span>
          <el-form :model="formInline" :rules="rules" ref="ruleForm" label-width="126px" class="demo-ruleForm" size="small" :status-icon="true">
            <el-form-item prop="iccid">
              <span slot="label">卡ICCID列表：</span>
              <el-input type="textarea" v-model="formInline.iccid" rows="4" placeholder="请输入卡ICCID"></el-input>
              <div class="annotation">一行代表一个ICCID，多行代表多个ICCID，建议不超过200个ICCID</div>
            </el-form-item>
            <el-form-item prop="model">
              <span slot="label">套餐模式：</span>
              <el-radio v-model="formInline.model" label="1">叠加</el-radio>
              <el-radio v-model="formInline.model" label="2">延期</el-radio>
            </el-form-item>
            <el-form-item prop="tc_flow">
              <span slot="label">套餐流量：</span>
              <el-input v-model="formInline.tc_flow" @input="formInline.tc_flow = limitNumber(formInline.tc_flow)" placeholder="请输入套餐流量"></el-input>
              <div class="annotation">默认单位为M，精确到3位小数(无限制填：99999999)</div>
            </el-form-item>
            <el-form-item prop="is_clear">
              <span slot="label">是否清零：</span>
              <el-radio v-model="formInline.is_clear" label="1">不清零</el-radio>
              <el-radio v-model="formInline.is_clear" label="2">会清零</el-radio>
            </el-form-item>
            <el-form-item prop="eff_pro">
              <span slot="label">有效周期：</span>
              <el-select v-model="formInline.eff_pro" placeholder="请选择有效周期">
                <el-option label="1个月" :value="0"></el-option>
                <el-option label="2个月" :value="1"></el-option>
                <el-option label="3个月" :value="2"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item prop="donator_remark">
              <span slot="label">赠者&备注：</span>
              <el-input type="textarea" v-model="formInline.donator_remark" placeholder="请输入赠者&备注" rows="4"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitForm('ruleForm')">保存</el-button>
              <el-button type="warning" @click="resetForm('ruleForm')">重置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane>
          <span slot="label"></i>历史赠送</span>
          <el-form class="search-form" :inline="true" :model="searchForm" size="small">
            <el-form-item label="卡ICCID">
              <el-input v-model="searchForm.iccid" placeholder="请输入"></el-input>
            </el-form-item>
            <el-form-item label="机构名称">
              <el-select v-model="searchForm.jg_name" placeholder="请选择">
                <el-option label="机构1" value="0"></el-option>
                <el-option label="机构2" value="1"></el-option>
                <el-option label="机构3" value="2"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="添加时间">
              <el-date-picker v-model="searchForm.add_time" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button size="small" type="primary">查询</el-button>
              <el-button size="small" type="warning">重置</el-button>
            </el-form-item>
          </el-form>
          <el-table v-loading="loadTab1Data" ref="multipleTable" :data="curTableData" border :default-sort="{prop: 'ex_time', order: 'descending'}" size="mini">
            <el-table-column fixed="left" show-overflow-tooltip label="卡ICCID" min-width="170">
              <template slot-scope="scope">
                <el-button type="text" @click="checkRechargeDetail(scope.row.iccid)">{{scope.row.iccid}}</el-button>
              </template>
            </el-table-column>
            <el-table-column show-overflow-tooltip prop="jg_name" label="机构名称" min-width="140"></el-table-column>
            <el-table-column show-overflow-tooltip label="套餐流量" min-width="95" sortable>
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.tc_flow)"></div>
              </template>
            </el-table-column>
            <el-table-column show-overflow-tooltip prop="fp_month" label="分配月数" min-width="95" sortable></el-table-column>
            <el-table-column show-overflow-tooltip label="月均流量" min-width="95" sortable>
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.tc_flow/scope.row.fp_month)"></div>
              </template>
            </el-table-column>
            <el-table-column prop="is_clear" label="是否清零" show-overflow-tooltip min-width="80"></el-table-column>
            <el-table-column prop="eff_pri" label="有效周期" show-overflow-tooltip min-width="80"></el-table-column>
            <el-table-column prop="donator_remark" label="赠者&备注" show-overflow-tooltip min-width="120"></el-table-column>
            <el-table-column prop="op_p" label="操作者" show-overflow-tooltip min-width="120" sortable></el-table-column>
            <el-table-column prop="add_time" label="添加时间" show-overflow-tooltip min-width="155" sortable></el-table-column>
            <el-table-column prop="ex_time" label="过期时间" show-overflow-tooltip min-width="155" sortable></el-table-column>
          </el-table>
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentPage" :page-sizes="pageSizes" :page-size="pagesize" layout="total, sizes, prev, pager, next, jumper" :total="tableData.length">
          </el-pagination>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>
<script>
import Api from 'assets/js/api.js'

export default {
  data() {
    return {
      routeName: this.$route.name,
      currentDate: new Date(),
      tableData: [],
      pageSizes: Api.STATIC.pageSizes,
      pagesize: Api.STATIC.pageSizes[1],
      currentPage: 1,
      tabIndex: '0',
      loadTab1Data: true,
      formInline: {
        model: '1',
        is_clear: '1'
      },
      searchForm: {},
      rules: {
        iccid: [{
          required: true,
          message: '请输入您需要重置的流量卡ICCID号',
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
        is_clear: [{
          required: true,
          message: '请选择是否清零',
          trigger: 'change'
        }],
        eff_pro: [{
          required: true,
          message: '请选择有效周期',
          trigger: 'change'
        }],
        donator_remark: [{
          required: true,
          message: '请输入备注',
          trigger: 'blur'
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
          return false;
        }
      });
    },
    // 重置表单
    resetForm(formName) {
      // resetFields 只能重置需要验证的值
      this.$refs[formName].resetFields()
    },
    handleSizeChange(val) {
      this.pagesize = val
    },
    handleCurrentChange(val) {
      this.currentPage = val
    },
    changeTab(para) {
      this.tabIndex = para.index
      // 当切换tab栏到'1'的时候，要加载数据
      if (this.tabIndex === '1') {
        // 这里应当是ajax请求数据
        if (this.tableData.length === 0) {
          this.getTab1Data()
        } else {
          this.loadTab1Data = false
        }
      }
    },
    checkRechargeDetail(iccid) {
      this.$router.push({ name: 'rechargeDetail', query: { iccid } })
    },
    getTab1Data() {
      setTimeout(() => {
        this.tableData = [{
          id: 0,
          iccid: '8986011670901045280',
          jg_name: '卡仕特-西格玛',
          tc_flow: 65421,
          fp_month: 12,
          month_flow: 0,
          is_clear: 1,
          eff_pri: '1个月',
          donator_remark: 'Newton',
          op_p: 'Lucy',
          add_time: '2019-02-03 21:01:03',
          ex_time: '2019-01-21 09:58:45'
        }]
        this.loadTab1Data = false
      }, 1000)
    },
    formatFlowUnit: Api.UNITS.formatFlowUnit,
    limitNumber: Api.UNITS.limitNumber,
  },
  computed: {
    curTableData() {
      return this.tableData.slice((this.currentPage - 1) * this.pagesize, this.currentPage * this.pagesize)
    }
  }
}

</script>
<style lang="scss">
.el-pagination {
  float: right;
  margin: 25px 40px 0 0;
}

.el-card__body {
  img {
    width: 100%;
  }

  .small {
    font-size: 12px;
  }

}

</style>
