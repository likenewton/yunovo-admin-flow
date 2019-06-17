<template>
  <div class="recharge_detail">
    <el-card class="reset-card" shadow="never">
      <el-row class="common-display" :gutter="15">
        <span class="item">ICCID：{{cardDetail.card_iccid}}</span>
        <span class="item" v-if="tabIndex==='4'">累计白名单用量：<span v-html="formatFlowUnit(cardDetail.wlistTotal)"></span></span>
        <span class="item">累计用量：<span v-html="formatFlowUnit(cardDetail.used_total)"></span></span>
        <span class="item">当月用量：<span v-html="formatFlowUnit(cardDetail.used_month)"></span></span>
        <span class="item">累记充值量：<span v-html="formatFlowUnit(cardDetail.pay_total)"></span></span>
        <span class="item">剩余流量：<span v-html="formatFlowUnit(cardDetail.max_unused)"></span></span>
        <span class="item text_danger">设备更新时间：{{cardDetail.time_last || '暂无数据'}}</span>
      </el-row>
      <el-tabs @tab-click="changeTab" v-model="tabIndex">
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
            <el-table-column prop="gprs_amount" label="分配总流量" width="110" sortable="custom">
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.gprs_amount)"></div>
              </template>
            </el-table-column>
            <el-table-column prop="gprs_price" label="流量价格" width="110" sortable="custom">
              <template slot-scope="scope">
                <span>￥{{formatMoney(scope.row.gprs_price)}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="pay_method" label="付款方式" width="100" sortable="custom">
              <template slot-scope="scope">
                <span>{{scope.row.pay_method_name}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="is_paid" label="支付状态" width="90" sortable="custom">
              <template slot-scope="scope">
                <span v-if="scope.row.is_paid==1">已付款</span>
                <span v-else>未付款</span>
              </template>
            </el-table-column>
            <el-table-column prop="transfer_id" label="支付流水号" min-width="140" sortable="custom"></el-table-column>
            <el-table-column prop="pay_memo" label="充值备注" show-overflow-tooltip min-width="100" sortable="custom"></el-table-column>
            <el-table-column prop="time_added" label="充值时间" width="153" sortable="custom"></el-table-column>
            <el-table-column prop="time_paid" label="付款时间" width="153" sortable="custom"></el-table-column>
            <el-table-column prop="time_expire" label="过期时间" width="210" sortable="custom">
              <template slot-scope="scope">
                <div v-html="calcLeftTime(scope.row.time_expire)"></div>
              </template>
            </el-table-column>
            <el-table-column prop="pay_from" label="订单来源" min-width="120" sortable="custom">
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
            <el-table-column prop="gprs_value" label="套餐流量" min-width="170" sortable="custom">
              <template slot-scope="scope">
                <span v-html="formatComboFlow(calcComboFlow(scope))"></span>
                <span v-if="scope.row.gprs_value != scope.row.allot_value" style="vertical-align: bottom">
                  <span style="line-height: 25px">(</span>结转：
                  <span v-html="formatFlowUnit(scope.row.gprs_value)"></span>
                  <span style="line-height: 25px">)</span>
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="balance_dval" label="设备剩余流量" min-width="120" sortable="custom">
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.balance_dval)"></div>
              </template>
            </el-table-column>
            <el-table-column prop="balance_value" label="联通剩余流量" min-width="120" sortable="custom">
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.balance_value)"></div>
              </template>
            </el-table-column>
            <el-table-column label="套餐类型" min-width="100" sortable="custom">
              <template slot-scope="scope">
                <span v-if="scope.row.allot_month==1">固定套餐</span>
                <span v-else>月均套餐</span>
              </template>
            </el-table-column>
            <el-table-column prop="allot_reset" label="是否清零" min-width="100" sortable="custom">
              <template slot-scope="scope">
                <span v-if="scope.row.allot_reset==1">会清零</span>
                <span v-else>不清零</span>
              </template>
            </el-table-column>
            <el-table-column prop="allot_month" label="分配月数" min-width="120" sortable="custom">
              <template slot-scope="scope">
                <span>{{getLiveMonthAlias(scope.row.allot_month)}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="gprs_value" label="月均流量" min-width="95">
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.gprs_value)"></div>
              </template>
            </el-table-column>
            <el-table-column prop="time_added" label="分配时间" min-width="155" sortable="custom"></el-table-column>
            <el-table-column prop="time_expire" label="过期时间" min-width="210" sortable="custom">
              <template slot-scope="scope">
                <div v-html="calcLeftTime(scope.row.time_expire)"></div>
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
            <el-table-column prop="time_added" label="日志时间" min-width="145" sortable="custom"></el-table-column>
            <el-table-column prop="log_type" label="日志类型" min-width="150" sortable="custom">
              <template slot-scope="scope">
                <span :class="{'btn-link': scope.row.log_url}" @click="logTypeLink(scope)">{{scope.row.log_type_name}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="log_text" label="日志描述" show-overflow-tooltip min-width="400" sortable="custom"></el-table-column>
          </el-table>
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="list_2.currentPage" :page-sizes="pageSizes" :page-size="list_2.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="list_2.total">
          </el-pagination>
        </el-tab-pane>
        <!-- 日使用情况 -->
        <el-tab-pane v-loading="loadData">
          <span slot="label">日使用情况</span>
          <el-table @sort-change="handleSortChange" :data="list_3.data" :max-height="maxTableHeight" border resizable size="mini">
            <el-table-column prop="stats_date" label="统计日期" min-width="100" sortable="custom"></el-table-column>
            <el-table-column prop="day_used" label="日使用流量" min-width="100" sortable="custom">
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.day_used)"></div>
              </template>
            </el-table-column>
            <el-table-column prop="day_over" label="日超标流量" min-width="100" sortable="custom">
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.day_over)"></div>
              </template>
            </el-table-column>
            <el-table-column prop="day_wlist" label="日白名单用量" min-width="100" sortable="custom">
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.day_wlist)"></div>
              </template>
            </el-table-column>
            <el-table-column prop="ayer_unused" label="昨日剩余流量" min-width="100" sortable="custom">
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.ayer_unused)"></div>
              </template>
            </el-table-column>
            <el-table-column prop="today_unused" label="今日剩余流量" min-width="100" sortable="custom">
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.today_unused)"></div>
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
            <el-table-column prop="how_month" label="统计年月" min-width="100" sortable="custom"></el-table-column>
            <el-table-column prop="month_used" label="月使用流量" min-width="100" sortable="custom">
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.month_used)"></div>
              </template>
            </el-table-column>
            <el-table-column prop="month_unused" label="月剩余流量" min-width="100" sortable="custom">
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.month_unused)"></div>
              </template>
            </el-table-column>
            <el-table-column prop="month_over" label="月超标流量" min-width="100" sortable="custom">
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.month_over)"></div>
              </template>
            </el-table-column>
            <el-table-column prop="month_wlist" label="月白名单用量" min-width="100" sortable="custom">
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.month_wlist)"></div>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="list_4.currentPage" :page-sizes="pageSizes" :page-size="list_4.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="list_4.total">
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
      maxTableHeight: Api.UNITS.maxTableHeight(),
      cardDetail: {},
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
        pagesize: Api.STATIC.pageSizes[2],
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
      ajaxData: {
        '0': 'getCardPayDetail',
        '1': 'getGprsAllotList',
        '2': 'getLogList',
        '3': 'getDayUse',
        '4': 'getMonthUse',
      }
    }
  },
  mounted() {
    this.getCardDetail()
    this.getData()
  },
  methods: {
    changeTab(para) {
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
      Api.UNITS.getListData({
        vue: this,
        url: _axios.ajaxAd[this.ajaxData[tabIndex]],
        list: `list_${tabIndex}`,
        sort: `sort_${tabIndex}`,
        formInline: `formInline_${tabIndex}`
      })
    },
    // tab上方的card详情
    getCardDetail() {
      _axios.send({
        method: 'get',
        url: _axios.ajaxAd.getCardDetail,
        params: { card_id: Api.UNITS.getQuery('card_id') },
        done: ((res) => {
          this.cardDetail = res.data
        })
      })
    },
    // 日志类型连接
    logTypeLink(scope) {
      let type = scope.row.log_type
      // 如果有链接接就跳转
      if (scope.row.log_url) {
        if (type === 1 || type === 7) {
          let batch_id = scope.row.log_url.split('?batch_id=')[1]
          this.$router.push({
            name: 'batchcreate',
            query: {
              type: 'update',
              batch_id,
            }
          })
        } else if (type === 2 || type === 3) {
          this.tabIndex = '0'
        } else if (type === 4) {
          let pay_sn = scope.row.log_url.split('?pay_sn=')[1]
          this.$router.push({
            name: 'rechargeParticulars',
            query: {
              pay_sn,
            }
          })
        } else if (type === 5) {
          let card_id = scope.row.log_url.split('?card_id=')[1]
          this.$router.push({
            name: 'stopcardlog',
            query: {
              card_id,
            }
          })
        } else if (type === 8) {
          let old_card_iccid = scope.row.log_url.split('?oiccid=')[1]
          let card_iccid = scope.row.log_url.split('?iccid=')[1]
          this.$router.push({
            name: 'flowmigration',
            query: {
              old_card_iccid,
              card_iccid,
              tabIndex: '1'
            }
          })
        } else if (type === 10) {
          let card_iccid = scope.row.log_url.split('?iccid=')[1]
          this.$router.push({
            name: 'cardauth',
            query: {
              card_iccid,
            }
          })
        }
      }
    },
    getLiveMonthAlias(value) {
      let item = this.liveMonthSelect.filter((v) => v.value == value)[0]
      return item ? item.label : ''
    },
    // 计算套餐值
    calcComboFlow(scope) {
      if (scope.row.gprs_amount) {
        return scope.row.gprs_amount
      } else {
        if (scope.row.gprs_value === scope.row.allot_value) {
          return Math.ceil(scope.row.gprs_value * scope.row.allot_month)
        } else {
          return scope.row.allot_value
        }
      }
    },
    formatFlowUnit: Api.UNITS.formatFlowUnit, // 格式化流量单位
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
