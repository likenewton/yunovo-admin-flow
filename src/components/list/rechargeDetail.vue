<template>
  <div class="recharge_detail">
    <el-card class="reset-card" shadow="never">
      <el-row class="common-display" :gutter="15">
        <span class="item">ICCID：89860918700307506235</span>
        <span class="item">累计用量：<span v-html="formatFlowUnit(6806)"></span></span>
        <span class="item">当月用量：<span v-html="formatFlowUnit(20.5)"></span></span>
        <span class="item">累记充值量：<span v-html="formatFlowUnit(0)"></span></span>
        <span class="item">剩余流量：<span v-html="formatFlowUnit(1223.1)"></span></span>
        <span class="item">设备更新时间：2019-05-06 09:51:57</span>
      </el-row>
      <el-tabs @tab-click="changeTab">
        <!-- 充值详情列表 -->
        <el-tab-pane v-loading="loadData">
          <span slot="label"></i>充值详情列表</span>
          <el-form class="search-form" :inline="true" :model="formInline_0" size="small">
            <el-form-item label="付款方式">
              <el-select v-model="formInline_0.pay_method" placeholder="请选择">
                <el-option v-for="(item, index) in payMethodSelect" :key="index" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="支付状态">
              <el-select v-model="formInline_0.is_paid" placeholder="请选择">
                <el-option v-for="(item, index) in paySelect" :key="index" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="充值日期">
              <el-date-picker v-model="formInline_0.date_start" type="date" value-format="yyyy-MM-dd" placeholder="选择开始日期"></el-date-picker> -
              <el-date-picker v-model="formInline_0.date_end" type="date" value-format="yyyy-MM-dd" placeholder="选择结束日期"></el-date-picker>
            </el-form-item>
            <el-form-item label="付款日期">
              <el-date-picker v-model="formInline_0.paid_start" type="date" value-format="yyyy-MM-dd" placeholder="选择开始日期"></el-date-picker> -
              <el-date-picker v-model="formInline_0.paid_end" type="date" value-format="yyyy-MM-dd" placeholder="选择结束日期"></el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button size="small" type="primary" @click="searchData">查询</el-button>
              <el-button size="small" type="warning" @click="resetData">重置</el-button>
            </el-form-item>
          </el-form>
          <el-table ref="listTable" @sort-change="handleSortChange" :data="list_0.data" :max-height="maxTableHeight" border resizable size="mini">
            <el-table-column prop="gprs_amount" label="分配总流量" min-width="100" sortable="custom">
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.gprs_amount)"></div>
              </template>
            </el-table-column>
            <el-table-column prop="gprs_price" label="流量价格" min-width="95" sortable="custom">
              <template slot-scope="scope">
                <span>￥{{formatMoney(scope.row.gprs_price)}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="pay_method" label="付款方式" min-width="94" sortable="custom">
              <template slot-scope="scope">
                <span>{{scope.row.pay_method_name}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="is_paid" label="支付状态" min-width="94" sortable="custom">
              <template slot-scope="scope">
                <span v-if="scope.row.is_paid==1">已付款</span>
                <span v-else>未付款</span>
              </template>
            </el-table-column>
            <el-table-column prop="transfer_id" label="支付流水号" min-width="110" sortable="custom"></el-table-column>
            <el-table-column prop="pay_memo" label="充值备注" show-overflow-tooltip min-width="150" sortable="custom"></el-table-column>
            <el-table-column prop="time_added" label="充值时间" width="153" sortable="custom"></el-table-column>
            <el-table-column prop="time_paid" label="付款时间" width="153" sortable="custom"></el-table-column>
            <el-table-column prop="time_expire" label="过期时间" width="210" sortable="custom">
              <template slot-scope="scope">
                <div v-html="calcLeftTime(scope.row.time_expire)"></div>
              </template>
            </el-table-column>
            <el-table-column prop="pay_from" label="订单来源" min-width="100" sortable="custom">
              <template slot-scope="scope">
                <span>{{scope.row.pay_from_name}}</span>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="list_0.currentPage" :page-sizes="pageSizes" :page-size="list_0.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="list_0.total">
          </el-pagination>
        </el-tab-pane>
        <!-- 流量分配详情 -->
        <el-tab-pane v-loading="loadData">
          <span slot="label">流量分配详情</span>
          <el-table @sort-change="handleSortChange" :data="list_1.data" :max-height="maxTableHeight" border resizable size="mini">
            <el-table-column prop="how_month" label="月份" min-width="85" sortable="custom"></el-table-column>
            <el-table-column prop="gprs_amount" label="套餐流量" min-width="95" sortable="custom">
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.gprs_amount)"></div>
              </template>
            </el-table-column>
            <el-table-column prop="balance_dval" label="设备剩余流量" min-width="120" sortable="custom">
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.balance_dval)"></div>
              </template>
            </el-table-column>
            <el-table-column prop="ltleft_flow" label="联通剩余流量" min-width="120" sortable="custom">
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.ltleft_flow)"></div>
              </template>
            </el-table-column>
            <el-table-column prop="tc_type" label="套餐类型" min-width="100" sortable="custom"></el-table-column>
            <el-table-column prop="is_clear" label="是否清零" min-width="100" sortable="custom"></el-table-column>
            <el-table-column prop="allot_month" label="分配月数" min-width="100" sortable="custom">
              <template slot-scope="scope">
                <span>{{getLiveMonthAlias(scope.row.allot_month)}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="month_flow" label="月均流量" min-width="95">
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.month_flow)"></div>
              </template>
            </el-table-column>
            <el-table-column prop="fp_time" label="分配时间" min-width="145" sortable="custom"></el-table-column>
            <el-table-column prop="ex_time" label="过期时间" min-width="210" sortable="custom">
              <template slot-scope="scope">
                <div v-html="calcLeftTime(scope.row.ex_time)"></div>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="list_1.currentPage" :page-sizes="pageSizes" :page-size="list_1.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="list_1.total">
          </el-pagination>
        </el-tab-pane>
        <!-- 生命周期日志 -->
        <el-tab-pane v-loading="loadData">
          <span slot="label">生命周期日志</span>
          <el-table @sort-change="handleSortChange" :data="list_2.data" :max-height="maxTableHeight" border resizable size="mini">
            <el-table-column prop="log_time" label="日志时间" min-width="145" sortable="custom"></el-table-column>
            <el-table-column prop="log_type" label="日志类型" min-width="150" sortable="custom">
              <template slot-scope="scope">
                <el-button type="text">{{scope.row.log_type}}</el-button>
              </template>
            </el-table-column>
            <el-table-column prop="log_desc" label="日志描述" min-width="400" sortable="custom"></el-table-column>
          </el-table>
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="list_2.currentPage" :page-sizes="pageSizes" :page-size="list_2.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="list_2.total">
          </el-pagination>
        </el-tab-pane>
        <!-- 日使用情况 -->
        <el-tab-pane v-loading="loadData">
          <span slot="label">日使用情况</span>
          <el-table @sort-change="handleSortChange" :data="list_3.data" :max-height="maxTableHeight" border resizable size="mini">
            <el-table-column prop="statistics_date" label="统计日期" min-width="100" sortable="custom"></el-table-column>
            <el-table-column prop="day_use" label="日使用流量" min-width="100" sortable="custom">
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.day_use)"></div>
              </template>
            </el-table-column>
            <el-table-column prop="day_exceed" label="日超标流量" min-width="100" sortable="custom">
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.day_exceed)"></div>
              </template>
            </el-table-column>
            <el-table-column prop="yesday_leftflow" label="昨日剩余流量" min-width="100" sortable="custom">
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.yesday_leftflow)"></div>
              </template>
            </el-table-column>
            <el-table-column prop="day_leftflow" label="今日剩余流量" min-width="100" sortable="custom">
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.day_leftflow)"></div>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="list_3.currentPage" :page-sizes="pageSizes" :page-size="list_3.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="list_3.total">
          </el-pagination>
        </el-tab-pane>
        <!-- 月使用情况 -->
        <el-tab-pane v-loading="loadData">
          <span slot="label">月使用情况</span>
          <el-table @sort-change="handleSortChange" :data="list_4.data" :max-height="maxTableHeight" border resizable size="mini">
            <el-table-column prop="statistics_month" label="统计年月" min-width="100" sortable="custom"></el-table-column>
            <el-table-column prop="month_use" label="月使用流量" min-width="100" sortable="custom">
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.month_use)"></div>
              </template>
            </el-table-column>
            <el-table-column prop="month_left" label="月剩余流量" min-width="100" sortable="custom">
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.month_left)"></div>
              </template>
            </el-table-column>
            <el-table-column prop="month_exceed" label="月超标流量" min-width="100" sortable="custom">
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.month_exceed)"></div>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="list_3.currentPage" :page-sizes="pageSizes" :page-size="list_3.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="list_3.total">
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
      loadData: true, // 是否显示加载动画
      tabIndex: '0', // 当前激活的tab的下标值
      pageSizes: Api.STATIC.pageSizes, // 显示页数列表，通配的
      // 列表数据
      list_0: { // 充值详情列表
        data: [],
        pagesize: Api.STATIC.pageSizes[1],
        currentPage: 1,
        total: 0
      },
      list_1: { // 流量分配详情
        data: [],
        pagesize: Api.STATIC.pageSizes[1],
        currentPage: 1,
        total: 0
      },
      list_2: { // 生命周期日志
        data: [],
        pagesize: Api.STATIC.pageSizes[1],
        currentPage: 1,
        total: 0
      },
      list_3: { // 日使用情况
        data: [],
        pagesize: Api.STATIC.pageSizes[1],
        currentPage: 1,
        total: 0
      },
      list_4: { // 月使用情况
        data: [],
        pagesize: Api.STATIC.pageSizes[1],
        currentPage: 1,
        total: 0
      },
      // 查询表单数据
      formInline_0: { card_id: Api.UNITS.getQuery('card_id') },
      formInline_1: { card_id: Api.UNITS.getQuery('card_id') },
      formInline_2: { card_id: Api.UNITS.getQuery('card_id') },
      formInline_3: { card_id: Api.UNITS.getQuery('card_id') },
      formInline_4: { card_id: Api.UNITS.getQuery('card_id') },
      sort_0: {},
      sort_1: {},
      sort_2: {},
      sort_3: {},
      sort_4: {},
      maxTableHeight: Api.UNITS.maxTableHeight(),
    }
  },
  mounted() {
    this.getData()
  },
  methods: {
    changeTab(para) {
      this.tabIndex = para.index
      if (this[`list_${this.tabIndex}`].data.length === 0) this.getData()
    },
    // 处理多个pagination的情况
    handleSizeChange(val) {
      this[`list_${this.tabIndex}`].currentPage = 1
      this[`list_${this.tabIndex}`].pagesize = val
      this.getData()
    },
    handleCurrentChange(val) {
      this[`list_${this.tabIndex}`].currentPage = val
      this.getData()
    },
    handleSortChange(val = {}) {
      Api.UNITS.setSortSearch(val, this, `sort_${this.tabIndex}`)
      this.getData()
    },
    searchData() {
      this.list_0.currentPage = 1
      this.getData()
    },
    // 重置列表
    resetData() {
      this.list_0.currentPage = 1
      this.formInline_0 = {
        card_id: Api.UNITS.getQuery('card_id')
      } // 1、重置查询表单
      this.sort_0 = {} // 2、重置排序
      this.$refs.listTable.clearSort() // 3、清空排序样式
      this.getData()
    },
    getData() {
      this.loadData = false
      // 根据 tabIndex, pagesize, currentpage来请求数据
      let tabIndex = this.tabIndex
      let list = this[`list_${tabIndex}`]
      // 一下针对不同的列表请求不同的资源
      if (tabIndex === '0') {
        Api.UNITS.getListData({
          vue: this,
          url: _axios.ajaxAd.getPayDetail,
          list: 'list_0',
          sort: 'sort_0',
          formInline: 'formInline_0'
        })
      } else if (tabIndex === '1') {
        Api.UNITS.getListData({
          vue: this,
          url: _axios.ajaxAd.getGprsAllotList,
          list: 'list_1',
          sort: 'sort_1',
          formInline: 'formInline_1'
        })
      }
    },
    getLiveMonthAlias(value) {
      let item = this.liveMonthSelect.filter((v) => v.value == value)[0]
      return item ? item.label : ''
    },
    formatFlowUnit: Api.UNITS.formatFlowUnit, // 格式化流量单位
    limitNumber: Api.UNITS.limitNumber, // 限制数字类型位数
    calcLeftTime: Api.UNITS.calcLeftTime, // 计算剩余时间
    formatComboFlow: Api.UNITS.formatComboFlow,
    formatMoney: Api.UNITS.formatMoney,
  },
  computed: {
    // 模拟分页
    ...mapState({
      paySelect: 'paySelect',
      payMethodSelect: 'payMethodSelect',
      liveMonthSelect: 'liveMonthSelect',
    })
  }
}

</script>
<style lang="scss">
.recharge_detail {
  .common-display {
    font-size: 14px;
    margin-bottom: 10px;

    .item {
      display: inline-block;
      margin: 5px 14px;
    }
  }

  .el-pagination {
    float: right;
    margin: 25px 40px 0 0;
  }

  td {
    * {
      font-size: 14px;
    }
  }
}

</style>
