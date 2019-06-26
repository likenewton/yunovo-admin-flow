<template>
  <div class="flow_gift">
    <el-card class="reset-card" shadow="never">
      <el-tabs @tab-click="changeTab" v-model="tabIndex">
        <el-tab-pane>
          <span slot="label">赠送流量</span>
          <el-form class="editor-form" :model="formInline" :rules="rules" ref="ruleForm" label-width="126px" size="small" :status-icon="true">
            <el-form-item prop="card_iccid">
              <span slot="label">卡ICCID列表：</span>
              <el-input type="textarea" v-model="formInline.card_iccid" rows="4" placeholder="请输入卡ICCID"></el-input>
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
            <el-form-item prop="live_month">
              <span slot="label">有效周期：</span>
              <el-select v-model="formInline.live_month" placeholder="请选择有效周期">
                <el-option v-for="(item, index) in liveMonthSelect" :key="index" :label="item.label" :value="item.value"></el-option>
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
              <el-input v-model="searchForm.card_iccid" @input="searchForm.card_iccid = limitNumber(searchForm.card_iccid, 20)" placeholder="请输入"></el-input>
            </el-form-item>
            <el-form-item label="机构名称">
              <el-select v-model="searchForm.org_id" filterable clearable placeholder="请选择机构">
                <el-option v-for="(item, index) in orgs" :key="index" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="添加时间">
              <el-date-picker v-model="searchForm.date_start" :picker-options="startDatePicker" type="date" value-format="yyyy-MM-dd" placeholder="选择开始日期"></el-date-picker> -
              <el-date-picker v-model="searchForm.date_end" :picker-options="endDatePicker" type="date" value-format="yyyy-MM-dd" placeholder="选择结束日期"></el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="searchData">查询</el-button>
              <el-button type="warning" @click="resetData">重置</el-button>
            </el-form-item>
          </el-form>
          <el-table v-loading="loadData" ref="listTable" :data="list.data" @sort-change="handleSortChange" :max-height="maxTableHeight" border resizable size="mini">
            <el-table-column prop="card_iccid" fixed="left" label="卡ICCID" width="182" sortable="custom">
              <template slot-scope="scope">
                <span class="btn-link" @click="$router.push({ name: 'rechargeDetail', query: {card_id: scope.row.card_id}})">{{scope.row.card_iccid}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="org_id" label="机构名称" min-width="140" sortable="custom">
              <template slot-scope="scope">
                <span class="btn-link" @click="$router.push({name: 'card', query: {org_id: scope.row.org_id}})">{{scope.row.org_name}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="gprs_amount" label="套餐流量" width="95" sortable="custom">
              <template slot-scope="scope">
                <div v-html="formatComboFlow(scope.row.gprs_amount)"></div>
              </template>
            </el-table-column>
            <el-table-column prop="allot_month" label="分配月数" width="95" sortable="custom">
              <template slot-scope="scope">
                <div>{{getLiveMonthAlias(scope.row.allot_month)}}</div>
              </template>
            </el-table-column>
            <el-table-column prop="allot_value" label="月均流量" width="95" sortable="custom">
              <template slot-scope="scope">
                <div v-html="formatComboFlow(scope.row.allot_value)"></div>
              </template>
            </el-table-column>
            <el-table-column prop="allot_reset" label="是否清零" width="90" sortable="custom">
              <template slot-scope="scope">
                <span v-if="scope.row.allot_reset==1">会清零</span>
                <span v-else>不清零</span>
              </template>
            </el-table-column>
            <el-table-column prop="live_month" label="有效周期" width="120" sortable="custom">
              <template slot-scope="scope">
                <div>{{getLiveMonthAlias(scope.row.live_month)}}</div>
              </template>
            </el-table-column>
            <el-table-column prop="gift_name" label="赠者&备注" min-width="160" sortable="custom">
              <template slot-scope="scope">
                <span v-html="scope.row.gift_name"></span>
              </template>
            </el-table-column>
            <el-table-column prop="user_id" label="操作者" width="100" sortable="custom">
              <template slot-scope="scope">
                <span>{{scope.row.first_name}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="time_added" label="添加时间" width="155" sortable="custom"></el-table-column>
            <el-table-column prop="time_expire" label="过期时间" width="210" sortable="custom">
              <template slot-scope="scope">
                <span v-html="calcLeftTime(scope.row.time_expire)"></span>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="list.currentPage" :page-sizes="pageSizes" :page-size="list.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="list.total">
          </el-pagination>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>
<script>
import Api from 'assets/js/api.js'
import { mapState } from 'vuex'

export default {
  data() {
    return {
      tabIndex: '1',
      searchForm: {},
      maxTableHeight: Api.UNITS.maxTableHeight(370),
      rules: {
        card_iccid: [{
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
        live_month: [{
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
    this.getData()
  },
  methods: {
    changeTab(para) {
      this.tabIndex = para.index
      if (this.tabIndex === '1') {
        // 历史赠送
        if (this.list.data.length === 0) {
          this.getData()
        } else {
          this.loadData = false
        }
      }
    },
    // 重置列表
    resetData() {
      this.list.currentPage = 1
      this.searchForm = {}
      this.sort = {}
      this.$refs.listTable.clearSort()
      this.getData()
    },
    // 提交表单
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          // 验证通过
        } else {
          Api.UNITS.showMsgBox()
          return false;
        }
      });
    },
    checkRechargeDetail(scope) {
      this.$router.push({ name: 'rechargeDetail', query: { card_iccid: scope.row.card_iccid } })
    },
    getData() {
      Api.UNITS.getListData({
        vue: this,
        url: _axios.ajaxAd.getGift,
        formInline: 'searchForm'
      })
    },
    getLiveMonthAlias(value) {
      let item = this.liveMonthSelect.filter((v) => v.value == value)[0]
      return item ? item.label : ''
    }
  },
  computed: {
    // 起始时间约数
    startDatePicker() {
      return Api.UNITS.startDatePicker(this, this.searchForm.date_end)
    },
    // 结束时间约数
    endDatePicker() {
      return Api.UNITS.endDatePicker(this, this.searchForm.date_start)
    }
  }
}

</script>
<style lang="scss">
.flow_gift {
  .el-pagination {
    float: right;
    margin: 25px 40px 0 0;
  }

  .el-table {

    td {
      * {
        font-size: 14px;
      }
    }
  }

}

</style>
