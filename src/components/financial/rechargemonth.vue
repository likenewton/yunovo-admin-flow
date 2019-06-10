<template>
  <div class="recharge_month">
    <el-card style="margin-bottom: 20px" shadow="never">
      <el-form class="search-form" :inline="true" :model="formInline" size="small">
        <el-form-item label="机构名称">
          <el-select v-model="formInline.org_id" filterable placeholder="请选择">
            <el-option v-for="(item, index) in orgs" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="月份">
          <el-select v-model="formInline.mdate" filterable clearable placeholder="请选择">
            <el-option v-for="(item, index) in months" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchData">查询</el-button>
          <el-button type="warning" @click="resetData">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="clearfix" style="margin-bottom: 20px" shadow="never" v-loading="loadData">
      <el-table ref="listTable" @sort-change="handleSortChange" :data="list.data" :max-height="maxTableHeight" :default-sort="defaultSort" border resizable size="mini">
        <el-table-column prop="org_name" label="机构名称" min-width="200" sortable="custom">
          <template slot-scope="scope">
            <span v-if="scope.row.sums">{{scope.row.org_name}}</span>
            <span v-else class="btn-link" @click="choiceOrgName(scope)">{{scope.row.org_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="mdate" label="月份" min-width="100" sortable="custom"></el-table-column>
        <el-table-column prop="paid_count" label="已付款次数" min-width="95" sortable="custom"></el-table-column>
        <el-table-column prop="nopaid_count" label="未付款次数" min-width="95" sortable="custom"></el-table-column>
        <el-table-column prop="gprs_amount" label="分配总流量" min-width="110" sortable="custom">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.gprs_amount)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="paid_money" label="充值总金额" min-width="130" sortable="custom">
          <template slot-scope="scope">
            <div>￥{{scope.row.paid_money|formatMoney}}</div>
          </template>
        </el-table-column>
        <el-table-column prop="rebate_money" label="返利总金额" min-width="130" sortable="custom">
          <template slot-scope="scope">
            <div>￥{{scope.row.rebate_money|formatMoney}}</div>
          </template>
        </el-table-column>
        <el-table-column prop="nopaid_money" label="未付款金额" min-width="130" sortable="custom">
          <template slot-scope="scope">
            <div>￥{{scope.row.nopaid_money|formatMoney}}</div>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="list.currentPage" :page-sizes="pageSizes" :page-size="list.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="list.total" class="clearfix">
      </el-pagination>
    </el-card>
    <el-card class="box-card clearfix" shadow="never" v-loading="loadData" v-show="formInline.org_id && list.data.length > 0">
      <el-tabs @tab-click="changeTab">
        <el-tab-pane>
          <span slot="label">次数统计</span>
          <div id="myChart_0" style="width:100%; height:380px"></div>
        </el-tab-pane>
        <el-tab-pane>
          <span slot="label">流量统计</span>
          <div id="myChart_1" style="width:100%; height:380px"></div>
        </el-tab-pane>
        <el-tab-pane>
          <span slot="label">金额统计</span>
          <div id="myChart_2" style="width:100%; height:380px"></div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>
<script>
import Api from 'assets/js/api.js'
import { mapMutations, mapState } from 'vuex'
const _echart = new Api.ECHARTS()

export default {
  data() {
    return {
      loadData: true,
      tabIndex: '0',
      pageSizes: Api.STATIC.pageSizes,
      // 列表数据
      list: {
        data: [],
        pagesize: Api.STATIC.pageSizes[2],
        currentPage: 1,
        total: 0,
      },
      // 查询表单数据
      formInline: {},
      sort: {},
      defaultSort: { prop: 'org_name', order: 'ascending' },
      maxTableHeight: Api.UNITS.maxTableHeight(),
      chartConst: {
        '0': {
          title: '次数统计',
          legend: ['已付款次数', '未付款次数']
        },
        '1': {
          title: '流量统计',
          legend: ['分配总流量']
        },
        '2': {
          title: '金额统计',
          legend: ['充值总金额', '返利总金额', '未付款总金额']
        },
      },
      myChart: null
    }
  },
  mounted() {
    // 有默认的排序，所以会执行一次handleSortChange，不用再getData了
  },
  methods: {
    changeTab(para) {
      this.tabIndex = para.index
      Vue.nextTick(() => {
        this.myChart = this.$echarts.init(document.getElementById(`myChart_${this.tabIndex}`))
        this.myChart.resize()
      })
      this.setEchartOption()
    },
    handleSizeChange(val) {
      this.list.pagesize = val
      this.getData()
    },
    handleCurrentChange(val) {
      this.list.currentPage = val
      this.getData()
    },
    handleSortChange(val = {}) {
      Api.UNITS.setSortSearch(val, this)
      this.getData()
    },
    // 查询
    searchData() {
      this.list.currentPage = 1
      this.getData()
    },
    // 重置
    resetData() {
      this.list.currentPage = 1
      this.formInline = {} // 1、重置查询表单
      this.$refs.listTable.clearSort() // 3、清空排序样式
      this.handleSortChange(this.defaultSort) // 2、重置排序
    },
    // 获取列表数据
    getData() {
      Api.UNITS.getListData({
        vue: this,
        url: _axios.ajaxAd.getMonthReport,
        cb: (res) => {
          let other = res.data.other || {}
          let data = this.list.data = this.list.data || []
          this.setEchartOption()
          if (data.length === 0) return
          data.push(...[{
            sums: true,
            org_name: '总计',
            paid_count: other.paid_amount,
            nopaid_count: other.nopaid_amount,
            gprs_amount: other.gprs_amount,
            paid_money: other.paid_money,
            rebate_money: other.rebate_money,
            nopaid_money: other.nopaid_money
          }])
        }
      })
    },
    choiceOrgName(scope) {
      this.formInline.org_id = scope.row.org_id + ''
      this.list.currentPage = 1 // 选择机构跳转的时候当前页码要回到第一页
      setTimeout(() => {
        this.myChart = this.$echarts.init(document.getElementById('myChart_0'))
      }, 0)
      this.getData()
    },
    // 获取图表数据
    setEchartOption() {
      if (!this.formInline.org_id || this.list.data.length === 0) return
      const chartConst = this.chartConst[this.tabIndex]
      let series = []
      let label = []

      if (this.tabIndex === '0') { // tab1
        series = [{ data: [] }, { data: [] }]
        this.list.data.forEach((v) => {
          if (v.sums) return false
          label.push(v.mdate + '')
          series[0].data.push(v.paid_count)
          series[1].data.push(v.nopaid_count)
        })
      } else if (this.tabIndex === '1') { // tab2
        series = [{ data: [] }]
        this.list.data.forEach((v) => {
          if (v.sums) return false
          label.push(v.mdate + '')
          series[0].data.push(v.gprs_amount)
        })
      } else if (this.tabIndex === '2') { // tab3
        series = [{ data: [] }, { data: [] }, { data: [] }]
        this.list.data.forEach((v) => {
          if (v.sums) return false
          label.push(v.mdate + '')
          series[0].data.push(v.paid_money)
          series[1].data.push(v.rebate_money)
          series[2].data.push(v.nopaid_money)
        })
      }

      _echart.setOption({
        title: this.list.data[0].org_name,
        legend: chartConst.legend,
        xAxis: { data: label },
        series,
        formatter: this.formatter
      })

      // 绘图
      Vue.nextTick(() => {
        this.myChart = this.$echarts.init(document.getElementById(`myChart_${this.tabIndex}`))
        this.myChart.setOption(_echart.getOption())
      })
    },
    formatterDealFn(data) {
      let variable = {
        '0': `${data.data}次`,
        '1': `${Api.UNITS.formatFlowUnit(data.data, 2, false)}`,
        '2': `${Api.UNITS.formatMoney(data.data)}元`
      }
      return variable[this.tabIndex]
    },
    // 格式化option
    formatter(series) {
      let formatterDealFn = this.formatterDealFn
      return `
      <div>
        <div>${series[0].axisValueLabel}</div>
        ${function a() {
          let str = ''
          series.forEach((v, i) => {
            str += `<div>
              <span style="display:inline-block;width:10px;height:10px;border-radius:50%;background:${v.color}"></span>
              <span>${v.seriesName}:</span>
              <span>${formatterDealFn(v)}</span>
            </div>`
          })
          return str
        }()}
      </div>`
    },
    formatFlowUnit: Api.UNITS.formatFlowUnit,
    calcLeftTime: Api.UNITS.calcLeftTime
  },
  computed: {
    ...mapState({
      asideCollapse: 'asideCollapse',
      orgs: 'orgs',
      months: 'months'
    })
  },
  watch: {
    asideCollapse(val, oldVal) {
      // 监听侧边栏的折叠变化，一旦发生变化要重新生成ecarts
      setTimeout(() => {
        this.myChart.resize()
      }, 300)
    }
  }
}

</script>
<style lang="scss">
.el-pagination {
  float: right;
  margin: 25px 40px 0 0;
}

.el-table {
  .table-head {}

  td {
    * {
      font-size: 14px;
    }
  }
}

.el-date-editor .el-range-separator {
  width: auto;
}

</style>
