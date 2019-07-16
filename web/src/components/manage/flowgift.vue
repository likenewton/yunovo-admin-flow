<template>
  <div class="flow_gift">
    <el-card class="reset-card" shadow="never">
      <el-tabs @tab-click="changeTab" v-model="tabIndex">
        <el-tab-pane v-loading="loadData">
          <span slot="label">赠送流量</span>
          <el-form class="editor-form" :model="formInline" :rules="rules" ref="ruleForm" label-width="126px" size="small">
            <el-form-item prop="iccids">
              <span slot="label">卡ICCID列表：</span>
              <el-input type="textarea" v-model="formInline.iccids" rows="4" placeholder="请输入卡ICCID"></el-input>
              <div class="annotation">一行代表一个ICCID，多行代表多个ICCID，建议不超过200个ICCID</div>
            </el-form-item>
            <el-form-item prop="pack_mode">
              <span slot="label">套餐模式：</span>
              <el-radio v-model="formInline.pack_mode" :label="0">叠加</el-radio>
              <el-radio v-model="formInline.pack_mode" :label="1">延期</el-radio>
            </el-form-item>
            <el-form-item prop="gprs_amount">
              <span slot="label">套餐流量：</span>
              <el-input v-model="formInline.gprs_amount" @input="formInline.gprs_amount = limitNumber(formInline.gprs_amount)" placeholder="请输入套餐流量"></el-input>
              <div class="annotation">默认单位为M，精确到3位小数(无限制填：99999999)</div>
            </el-form-item>
            <el-form-item prop="allot_month">
              <span slot="label">分配月数：</span>
              <el-select v-model="formInline.allot_month" placeholder="请选择分配月数" @change="changeAllotMonth">
                <el-option v-if="item.value >= 1 && item.value <= 48" v-for="(item, index) in liveMonthSelect" :disabled="formInline.allot==0&&item.value==1" :key="index" :label="item.label" :value="item.value - 0"></el-option>
              </el-select>
              <div class="annotation">月均流量：{{gprsMonth}}</div>
            </el-form-item>
            <el-form-item prop="allot_reset">
              <span slot="label">是否清零：</span>
              <el-radio v-model="formInline.allot_reset" :label="1">不清零</el-radio>
              <el-radio v-model="formInline.allot_reset" :label="0">会清零</el-radio>
            </el-form-item>
            <el-form-item prop="live_month">
              <span slot="label">有效周期：</span>
              <el-select v-model="formInline.live_month" placeholder="请选择有效周期">
                <el-option v-for="(item, index) in liveMonthSelect" v-if="item.value >= 1" :disabled="item.value < formInline.allot_month" :key="index" :label="item.label" :value="item.value - 0"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item prop="gift_name">
              <span slot="label">赠者&备注：</span>
              <el-input type="textarea" v-model="formInline.gift_name" placeholder="请输入赠者&备注" rows="4"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitForm('ruleForm')" :disabled="!pageAuthBtn.FCP_01_005_ADD01">保存</el-button>
              <el-button type="warning" @click="resetForm('ruleForm')">重置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane>
          <span slot="label"></i>历史赠送</span>
          <el-form class="search-form" :inline="true" :model="searchForm" size="small">
            <el-form-item>
              <el-input v-model="searchForm.card_iccid" @input="searchForm.card_iccid = limitNumber(searchForm.card_iccid, 20)" placeholder="卡ICCID" @keyup.enter.native="searchData"></el-input>
            </el-form-item>
            <el-form-item>
              <el-select v-model="searchForm.org_id" filterable clearable placeholder="机构名称" @change="searchData">
                <el-option v-for="(item, index) in orgs" :key="index" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-date-picker v-model="searchForm.date_start" :picker-options="startDatePicker" type="date" value-format="yyyy-MM-dd" @change="searchData" placeholder="添加时间开始"></el-date-picker> -
              <el-date-picker v-model="searchForm.date_end" :picker-options="endDatePicker" type="date" value-format="yyyy-MM-dd" @change="searchData" placeholder="添加时间结束"></el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="searchData" :disabled="!pageAuthBtn.FCP_01_005_CHECK01">查询</el-button>
              <el-button type="warning" @click="resetData" :disabled="!pageAuthBtn.FCP_01_005_CHECK01">重置</el-button>
            </el-form-item>
          </el-form>
          <el-table v-loading="loadData" ref="listTable" :data="list.data" @sort-change="handleSortChange" :max-height="maxTableHeight" border resizable size="mini">
            <el-table-column prop="card_iccid" fixed="left" label="卡ICCID" width="182" sortable="custom">
              <template slot-scope="scope">
                <span v-if="pageAuthBtn.FCP_01_005_LINK01" class="btn-link" @click="$router.push({ name: 'rechargeDetail', query: {card_id: scope.row.card_id}})">{{scope.row.card_iccid}}</span>
                <span v-else>{{scope.row.card_iccid}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="org_id" label="机构名称" :min-width="widthMap.org_id[size]" sortable="custom">
              <template slot-scope="scope">
                <span v-if="pageAuthBtn.FCP_01_005_LINK2" class="btn-link" @click="$router.push({name: 'card', query: {org_id: scope.row.org_id}})">{{scope.row.org_name}}</span>
                <span v-else>{{scope.row.org_name}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="gprs_amount" label="套餐流量" :width="widthMap.gprs_amount[size]" sortable="custom" align="right">
              <template slot-scope="scope">
                <div v-html="formatComboFlow(scope.row.gprs_amount)"></div>
              </template>
            </el-table-column>
            <el-table-column prop="allot_month" label="分配月数" width="90" sortable="custom" align="right">
              <template slot-scope="scope">
                <div>{{scope.row.allot_month}}</div>
              </template>
            </el-table-column>
            <el-table-column prop="allot_value" label="月均流量" :width="widthMap.allot_value[size]" sortable="custom" align="right">
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
            <el-table-column prop="gift_name" label="赠者&备注" :min-width="widthMap.gift_name[size]" sortable="custom" show-overflow-tooltip>
              <template slot-scope="scope">
                <span v-html="scope.row.gift_name"></span>
              </template>
            </el-table-column>
            <el-table-column prop="user_id" label="操作者" width="widthMap.user_id[size]" sortable="custom">
              <template slot-scope="scope">
                <span>{{scope.row.first_name}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="time_added" label="添加时间" :width="widthMap.time_added[size]" sortable="custom"></el-table-column>
            <el-table-column prop="time_expire" label="过期时间" :width="widthMap.time_expire[size]" sortable="custom">
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
    <el-dialog title="赠送信息" :visible.sync="dialogGiftVisible" :close-on-click-modal="false">
      <div slot>
        <div id="iccid_reset" style="width:100%;overflow: auto">
          <el-table :data="giftData" border resizable size="mini" :max-height="winHeight / 2.2">
            <el-table-column prop="iccid" label="卡iccid" min-width="200" show-overflow-tooltip></el-table-column>
            <el-table-column prop="msg" label="执行结果" min-width="200">
              <template slot-scope="scope">
                <span class="text_danger bold" v-if="scope.row.ret === '2'">{{scope.row.msg}}</span>
                <span class="text_warning bold" v-else-if="scope.row.ret === '1'">{{scope.row.msg}}</span>
                <span class="text_success bold" v-else>{{scope.row.msg}}</span>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import Api from 'assets/js/api.js'
import { mapState } from 'vuex'

export default {
  data() {
    return {
      tabIndex: '0',
      dialogGiftVisible: false,
      formInline: {
        allot_month: 1,
        allot_reset: 1,
        pack_mode: 0
      },
      size: Api.UNITS.getSize(),
      widthMap: {
        org_id: [140, 130],
        gprs_amount: [100, 88],
        allot_value: [100, 98],
        gift_name: [160, 145],
        user_id: [100, 88],
        time_added: [155, 94],
        time_expire: [210, 153],
      },
      searchForm: {},
      giftData: [],
      maxTableHeight: Api.UNITS.maxTableHeight(370),
      rules: {
        iccids: [{
          required: true,
          message: '请输入流量卡ICCID号',
          trigger: 'blur'
        }, {
          validator: this.validateIccidCount,
          trigger: ['blur']
        }],
        pack_mode: [{
          required: true,
          message: '请选择套餐模式',
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
        }],
        gift_name: [{
          required: true,
          message: '请输入备注',
          trigger: 'blur'
        }, {
          min: 2,
          max: 255,
          message: '长度必须2~255个字符之间',
          trigger: ['blur']
        }]
      }
    }
  },
  mounted() {
    this.loadData = false
  },
  methods: {
    changeTab(para) {
      this.getData()
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
          this.formInline.iccids = this.formInline.iccids.split('\n').filter((v) => v.trim()).join('\n')
          this.loadData = true
          // 验证通过
          _axios.send({
            method: 'post',
            url: _axios.ajaxAd.addGprsGift,
            data: this.formInline,
            done: ((res) => {
              this.loadData = false
              if (res.status === 400) {
                this.formInline[res.data] = ''
                this.$refs.ruleForm.validateField([res.data])
              } else {
                this.dialogGiftVisible = true
                this.giftData = res.data
                this.resetForm('ruleForm')
                let count = 0
                this.giftData.forEach((v) => {
                  if (v.ret === '0') count++
                })
                setTimeout(() => {
                  this.showMsgBox({
                    type: 'success',
                    message: `操作成功！本次赠送 ${count} 张卡`
                  })
                }, 150)
              }
            })
          })
        } else {
          Api.UNITS.showMsgBox()
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields()
      this.formInline = {
        allot_month: 1,
        allot_reset: 1,
        pack_mode: 0
      }
      this.isUpdate && this.getData()
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
    validateIccidCount(rule, value, callback) {
      let iccidList = value.split('\n').filter((v) => v.trim())
      if (iccidList.length > 200) {
        callback(new Error('一次最多处理200张iccid卡'))
      } else if (iccidList.length === 0) {
        callback(new Error('请输入您的流量卡ICCID号'))
      } else {
        callback()
      }
    },
    // 当分配月数发生改变时有效周期可能要改变
    changeAllotMonth() {
      if (this.formInline.live_month) {
        if (this.formInline.allot_month > this.formInline.live_month) {
          this.formInline.live_month = this.formInline.allot_month
        }
      }
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
    },
    gprsMonth() {
      let total = this.formInline.gprs_amount || 0
      let month = this.formInline.allot_month || 1
      return Api.UNITS.formatFlowUnit(total / month, 3, false)
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
