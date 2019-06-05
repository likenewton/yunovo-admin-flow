<template>
  <div>
    <el-card class="box-card" style="margin-bottom: 20px" shadow="never">
      <el-form class="search-form" :inline="true" :model="formInline" size="small">
        <el-form-item label="机构名称">
          <el-select v-model="formInline.org_id" filterable clearable placeholder="请选择">
            <el-option v-for="(item, index) in orgs" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="月份">
          <el-select v-model="formInline.mdate" filterable clearable placeholder="请选择">
            <el-option v-for="(item, index) in months" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getData">查询</el-button>
          <el-button type="warning" @click="resetData">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="box-card clearfix" style="margin-bottom: 20px" shadow="never" v-loading="loadData">
      <el-table ref="listTable" @sort-change="handleSortChange" :data="list.data" :max-height="maxTableHeight" border resizable size="mini">
        <el-table-column prop="org_name" label="机构名称" min-width="200" sortable="custom">
          <template slot-scope="scope">
            <span v-if="scope.row.sums">{{scope.row.org_name}}</span>
            <span v-else class="btn-link">{{scope.row.org_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="how_month" label="月份" min-width="110" sortable="custom"></el-table-column>
        <el-table-column prop="pay_count" label="已付款次数" min-width="110" sortable="custom"></el-table-column>
        <el-table-column prop="nopay_count" label="未付款次数" min-width="110" sortable="custom"></el-table-column>
        <el-table-column prop="fptotal_flow" label="分配总流量" min-width="110" sortable="custom">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.fptotal_flow)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="recharge_total" label="充值总金额" min-width="110" sortable="custom">
          <template slot-scope="scope">
            <div>￥{{scope.row.recharge_total|formatMoney}}</div>
          </template>
        </el-table-column>
        <el-table-column prop="repay_total" label="返利总金额" min-width="110" sortable="custom">
          <template slot-scope="scope">
            <div>￥{{scope.row.repay_total|formatMoney}}</div>
          </template>
        </el-table-column>
        <el-table-column prop="nopay_total" label="未付款金额" min-width="110" sortable="custom">
          <template slot-scope="scope">
            <div>￥{{scope.row.nopay_total|formatMoney}}</div>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentPage" :page-sizes="pageSizes" :page-size="list.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="list.total" class="clearfix">
      </el-pagination>
    </el-card>
    <el-card class="box-card clearfix" shadow="never">
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
        pagesize: Api.STATIC.pageSizes[0],
        currentPage: 1,
        total: 0,
      },
      // 查询表单数据
      formInline: {},
      sort: {},
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
      myChart: null,
    }
  },
  mounted() {
    Vue.nextTick(() => {
      this.myChart = this.$echarts.init(document.getElementById('myChart_0'))
    })
    // 进入页面的时候请求数据
    this.getData()
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
    // 重置列表
    resetData() {
      this.formInline = {} // 1、重置查询表单
      this.sort = {} // 2、重置排序
      this.$refs.listTable.clearSort() // 3、清空排序样式
      this.getData()
    },
    // 获取列表数据
    getData() {
      Api.UNITS.getListData({
        vue: this,
        url: _axios.ajaxAd.getStats,
        cb: (res) => {
          let other = res.data.other || {}
          this.setEchartOption()
          if (this.list.data.length === 0) return
          this.list.data.push(...[{
            sums: true,
            org_name: '总计',
            card_count: other.card_count,
            nonactivated: other.nonactivated,
            activated: other.activated,
            unicom_count: other.unicom_count,
            month_count: other.month_count
          }])
        }
      })
    },
    // 获取图表数据
    setEchartOption() {
      const chartConst = this.chartConst[this.tabIndex]
      let series = []

      if (this.tabIndex === '0') { // tab1
        series = [{}, {}]
        this.list.data.forEach((v) => {
          series[0].data = v.pay_count
          series[1].data = v.nopay_count
        })
      } else if (this.tabIndex === '1') { // tab2
        series = [{}]
        this.list.data.forEach((v) => {
          series[0].data = v.fptotal_flow
        })
      } else if (this.tabIndex === '2') { // tab3
        series = [{}, {}, {}]
        this.list.data.forEach((v) => {
          series[0].data = v.recharge_total
          series[0].data = v.repay_total
          series[0].data = v.nopay_total
        })
      }
      _echart.setOption({
        title: this.list.data[0].org_name,
        legend: chartConst.legend,
        xAxis: { data: this.list.data.map((v) => v.how_month) },
        series,
        formatter: this.formatter
      })
      // 绘图
      Vue.nextTick(() => {
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
