<template>
  <div>
    <el-card class="box-card clearfix" style="margin-bottom: 20px" shadow="never">
      <el-form :inline="true" :model="formInline" class="demo-form-inline" size="small">
        <el-form-item label="机构名称">
          <el-select v-model="formInline.jg_name" placeholder="请选择">
            <el-option label="机构1" value="0"></el-option>
            <el-option label="机构2" value="1"></el-option>
            <el-option label="机构3" value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="起止时间">
          <el-date-picker v-model="formInline.time" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary">查询</el-button>
          <el-button type="warning">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table v-loading="loadData" ref="multipleTable" :data="curTableData" border :default-sort="{prop: 'jg_name', order: 'descending'}" size="mini">
        <el-table-column show-overflow-tooltip label="机构名称" min-width="125">
          <template slot-scope="scope">
            <el-button type="text">{{scope.row.jg_name}}</el-button>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="month" label="月份" show-overflow-tooltip min-width="110" sortable></el-table-column>
        <el-table-column show-overflow-tooltip prop="pay_count" label="已付款次数" show-overflow-tooltip min-width="110" sortable></el-table-column>
        <el-table-column show-overflow-tooltip prop="nopay_count" label="未付款次数" show-overflow-tooltip min-width="110" sortable></el-table-column>
        <el-table-column show-overflow-tooltip label="分配总流量" show-overflow-tooltip min-width="110">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.fptotal_flow)"></div>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip label="充值总金额" show-overflow-tooltip min-width="110">
          <template slot-scope="scope">
            <div>￥{{scope.row.recharge_total|formatMoney}}</div>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip label="返利总金额" show-overflow-tooltip min-width="110">
          <template slot-scope="scope">
            <div>￥{{scope.row.repay_total|formatMoney}}</div>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip label="未付款金额" show-overflow-tooltip min-width="110">
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
        pagesize: Api.STATIC.pageSizes[1],
        currentPage: 1,
        total: 0,
      },
      // 查询表单数据
      formInline: {
        jg_name: '自营'
      },
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
    setTimeout(() => {
      this.myChart = this.$echarts.init(document.getElementById('myChart_0'))
    }, 0)
    // 进入页面的时候请求数据
    if (this.list.data.length === 0) {
      this.getData()
    } else {
      this.loadData = false
    }
  },
  methods: {
    routeName() {
      return this.$route.name
    },
    changeTab(para) {
      this.tabIndex = para.index
      setTimeout(() => {
        this.myChart = this.$echarts.init(document.getElementById(`myChart_${this.tabIndex}`))
      }, 0)
      this.showEchart()
    },
    handleSizeChange(val) {
      this.list.pagesize = val
      this.getData()
    },
    handleCurrentChange(val) {
      this.list.currentPage = val
      this.getData()
    },
    showDetail() {
      this.$router.push({ name: 'iccidList' })
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
    // 获取列表数据
    getData() {
      setTimeout(() => {
        // 数据请求成功
        this.list.data = [{
          id: 0,
          jg_name: '自营',
          month: '2019-05',
          pay_count: 12,
          nopay_count: 2,
          fptotal_flow: 12452,
          recharge_total: 15621.3,
          repay_total: 2352.32,
          nopay_total: 4512.23
        }, {
          id: 0,
          jg_name: '自营',
          month: '2019-06',
          pay_count: 12,
          nopay_count: 2,
          fptotal_flow: 12452,
          recharge_total: 15621.23,
          repay_total: 2352.32,
          nopay_total: 4512.23
        }, {
          id: 0,
          jg_name: '自营',
          month: '2019-07',
          pay_count: 12,
          nopay_count: 2,
          fptotal_flow: 12452,
          recharge_total: 15621.2,
          repay_total: 2352.32,
          nopay_total: 4512.23
        }, {
          id: 0,
          jg_name: '自营',
          month: '2019-08',
          pay_count: 12,
          nopay_count: 2,
          fptotal_flow: 12452,
          recharge_total: 15621.23,
          repay_total: 2352.32,
          nopay_total: 4512.23
        }, {
          id: 0,
          jg_name: '自营',
          month: '2019-09',
          pay_count: 12,
          nopay_count: 2,
          fptotal_flow: 12452,
          recharge_total: 15621.23,
          repay_total: 2352.32,
          nopay_total: 4512.23
        }, {
          id: 0,
          jg_name: '自营',
          month: '2019-10',
          pay_count: 12,
          nopay_count: 2,
          fptotal_flow: 12452,
          recharge_total: 15621.23,
          repay_total: 2352.32,
          nopay_total: 4512.23
        }, {
          id: 0,
          jg_name: '自营',
          month: '2019-11',
          pay_count: 12,
          nopay_count: 2,
          fptotal_flow: 12452,
          recharge_total: 15621.23,
          repay_total: 2352.32,
          nopay_total: 4512.23
        }, {
          id: 0,
          jg_name: '自营',
          month: '2019-12',
          pay_count: 12,
          nopay_count: 2,
          fptotal_flow: 12452,
          recharge_total: 15621.23,
          repay_total: 2352.32,
          nopay_total: 4512.23
        }, {
          id: 0,
          jg_name: '自营',
          month: '2020-01',
          pay_count: 12,
          nopay_count: 2,
          fptotal_flow: 12452,
          recharge_total: 15621.23,
          repay_total: 2352.32,
          nopay_total: 4512.23
        }, {
          id: 0,
          jg_name: '自营',
          month: '2020-02',
          pay_count: 12,
          nopay_count: 2,
          fptotal_flow: 12452,
          recharge_total: 15621.23,
          repay_total: 2352.32,
          nopay_total: 4512.23
        }]
        this.list.total = this.list.data.length
        this.loadData = false
        // 数据拿到了就可以格式化图表数据了
        this.showEchart()
      }, 1000)
    },
    // 获取图表数据
    showEchart() {
      const chartConst = this.chartConst[this.tabIndex]
      let series = []

      if (this.tabIndex === '0') { // tab1
        series = [{}, {}]
        series[0].data = this.list.data.map((v) => v.pay_count)
        series[1].data = this.list.data.map((v) => v.nopay_count)
      } else if (this.tabIndex === '1') { // tab2
        series = [{}]
        series[0].data = this.list.data.map((v) => v.fptotal_flow)
      } else if (this.tabIndex === '2') { // tab3
        series = [{}, {}, {}]
        series[0].data = this.list.data.map((v) => v.recharge_total)
        series[1].data = this.list.data.map((v) => v.repay_total)
        series[2].data = this.list.data.map((v) => v.nopay_total)
      }
      _echart.setOption({
        title: this.formInline.jg_name,
        legend: chartConst.legend,
        xAxis: { data: this.list.data.map((v) => v.month) },
        series,
        formatter: this.formatter
      })
      // 绘图
      setTimeout(() => {
        this.myChart.setOption(_echart.getOption())
      }, 0)
    },
    formatFlowUnit: Api.UNITS.formatFlowUnit,
    calcLeftTime: Api.UNITS.calcLeftTime
  },
  computed: {
    curTableData() {
      return this.list.data.slice((this.list.currentPage - 1) * this.list.pagesize, this.list.currentPage * this.list.pagesize)
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
