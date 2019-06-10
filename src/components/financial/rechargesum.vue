<template>
  <div>
    <el-card class="box-card" style="margin-bottom: 20px" shadow="never">
      <el-form class="search-form" :inline="true" :model="formInline" size="small">
        <el-form-item label="机构名称">
          <el-select v-model="formInline.org_id" filterable clearable placeholder="请选择">
            <el-option v-for="(item, index) in orgs" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="起止日期">
          <el-date-picker v-model="formInline.date_start" type="date" value-format="yyyy-MM-dd" placeholder="选择开始日期"></el-date-picker> -
          <el-date-picker v-model="formInline.date_end" type="date" value-format="yyyy-MM-dd" placeholder="选择结束日期"></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchData">查询</el-button>
          <el-button type="warning" @click="resetData">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="recharge_sum clearfix" style="margin-bottom: 20px" shadow="never" v-loading="loadData">
      <el-table ref="listTable" @sort-change="handleSortChange" :data="list.data" :max-height="maxTableHeight" border resizable size="mini">
        <el-table-column label="机构名称" min-width="200" sortable="custom">
          <template slot-scope="scope">
            <span v-if="scope.row.sums">{{scope.row.org_name}}</span>
            <span v-else class="btn-link">{{scope.row.org_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="pay_count" label="充值次数" min-width="110" sortable="custom"></el-table-column>
        <el-table-column prop="gprs_amount" label="分配总流量" min-width="110" sortable="custom">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.gprs_amount)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="money_count" label="充值总金额" min-width="110" sortable="custom">
          <template slot-scope="scope">
            <div>￥{{scope.row.money_count|formatMoney}}</div>
          </template>
        </el-table-column>
        <el-table-column prop="rebate_money" label="返利总金额" min-width="110" sortable="custom">
          <template slot-scope="scope">
            <div>￥{{scope.row.rebate_money|formatMoney}}</div>
          </template>
        </el-table-column>
        <el-table-column label="操作" min-width="80">
          <template slot-scope="scope">
            <el-button v-if="!scope.row.sums" type="text" @click="$router.push({ name: 'iccidList', query: { 'org_id': scope.row.org_id } })">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :page-sizes="pageSizes" :page-size="list.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="list.total" class="clearfix">
      </el-pagination>
    </el-card>
    <el-card class="box-card clearfix" shadow="never" v-loading="loadData">
      <el-tabs @tab-click="changeTab">
        <el-tab-pane>
          <span slot="label">充值次数</span>
          <div id="myChart_0" style="width:100%; height:380px"></div>
        </el-tab-pane>
        <el-tab-pane>
          <span slot="label">分配总流量</span>
          <div id="myChart_1" style="width:100%; height:380px"></div>
        </el-tab-pane>
        <el-tab-pane>
          <span slot="label">充值总金额</span>
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
      formInline: {},
      sort: {},
      maxTableHeight: Api.UNITS.maxTableHeight(),
      chartConst: {
        '0': {
          title: '机构充值记录统计',
          name: '充值总次数'
        },
        '1': {
          title: '机构充值总流量(MB)',
          name: '充值总流量'
        },
        '2': {
          title: '机构充值总金额(元)',
          name: '充值总金额'
        }
      },
      myChart: null,
      // 激活-未激活柱状图数据
      option: {
        title: {
          text: '',
          x: 'center'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          },
          formatter: null
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        toolbox: _echart.getOption().toolbox,
        yAxis: {
          type: 'value',
          splitLine: { show: false }
        },
        xAxis: {
          type: 'category',
          data: [], //要设置的
          axisLabel: {
            textStyle: {
              fontSize: 12
            }
          },
        },
        series: [{
          name: '',
          type: 'bar',
          barMaxWidth: 100,
          label: {
            normal: {
              show: true,
              formatter: '',
              position: 'top'
            }
          },
          data: [], //要设置的
          itemStyle: {
            normal: {
              color(params) {
                return Api.UNITS.getColorList([], 60)[params.dataIndex]
              }
            }
          }
        }]
      }
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
      this.setOptionData()
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
    // 重置列表
    resetData() {
      this.list.currentPage = 1
      this.formInline = {} // 1、重置查询表单
      this.sort = {} // 2、重置排序
      this.$refs.listTable.clearSort() // 3、清空排序样式
      this.getData()
    },
    // 获取列表数据
    getData() {
      Api.UNITS.getListData({
        vue: this,
        url: _axios.ajaxAd.getReport,
        cb: (res) => {
          let other = res.data.other || {}
          let data = this.list.data || []
          this.setOptionData()
          if (data.length === 0) return
          data.push(...[{
            sums: true,
            org_name: '总计',
            gprs_amount: other.gprs_amount,
            money_count: other.money_count,
            pay_count: other.pay_count,
            rebate_money: other.rebate_money
          }])
        }
      })
    },
    // 获取图表数据
    setOptionData() {
      let option = this.option
      let chartConst = this.chartConst[this.tabIndex]
      let label = option.xAxis.data = [] // 底坐标标签
      let data1 = option.series[0].data = [] // 分类一数据
      option.tooltip.formatter = this.formatter
      option.series[0].label.normal.formatter = this.formatterToolTip
      option.title.text = chartConst.title
      option.series[0].name = chartConst.name
      this.list.data.forEach((v) => {
        if (this.tabIndex === '0' && !v.sums) {
          label.push(v.org_name)
          data1.push(v.pay_count)
        } else if (this.tabIndex === '1' && !v.sums) {
          label.push(v.org_name)
          data1.push(v.gprs_amount)
        } else if (this.tabIndex === '2' && !v.sums) {
          label.push(v.org_name)
          data1.push(v.money_count)
        }
      })
      // 绘图
      Vue.nextTick(() => {
        this.myChart.setOption(option)
      })
    },
    // 格式化option
    formatter(series) {
      let variable = {
        '0': `${series[0].data}次`,
        '1': `${Api.UNITS.formatFlowUnit(series[0].data, 2, false)}`,
        '2': `${Api.UNITS.formatMoney(series[0].data)}元`
      }
      return `
      <div>
        <div>${series[0].axisValueLabel}</div>
        <div>
          <span style="display:inline-block;width:10px;height:10px;border-radius:50%;background:${series[0].color}"></span>
          <span>${series[0].seriesName}:</span>
          <span>${variable[this.tabIndex]}</span>
        </div>
      </div>`
    },
    formatterToolTip(series) {
      let variable = {
        '0': `${series.data}次`,
        '1': `${Api.UNITS.formatFlowUnit(series.data, 2, false)}`,
        '2': `${Api.UNITS.formatMoney(series.data)}元`
      }
      return `${variable[this.tabIndex]}`
    },
    formatFlowUnit: Api.UNITS.formatFlowUnit,
    calcLeftTime: Api.UNITS.calcLeftTime
  },
  computed: {
    ...mapState({
      asideCollapse: 'asideCollapse',
      orgs: 'orgs'
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
.recharge_sum {
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
}

</style>
