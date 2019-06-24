<template>
  <div>
    <el-card class="search-card" style="margin-bottom: 20px" shadow="never">
      <el-form :inline="true" :model="formInline" class="search-form" size="small">
        <el-form-item label="统计日期">
          <el-date-picker v-model="formInline.date_start" :picker-options="startDatePicker" type="date" value-format="yyyy-MM-dd" placeholder="选择开始日期"></el-date-picker> -
          <el-date-picker v-model="formInline.date_end" :picker-options="endDatePicker" type="date" value-format="yyyy-MM-dd" placeholder="选择结束日期"></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchData">查询</el-button>
          <el-button type="warning" @click="resetData">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="clearfix" shadow="never" v-loading="loadData">
      <el-button-group style="margin-bottom: 10px">
        <el-button size="small" type="primary" @click="showEcharts('0')">支付图表</el-button>
        <el-button size="small" type="primary" @click="showEcharts('1')">在线图表</el-button>
        <el-button size="small" type="primary" @click="showEcharts('2')">激活图表</el-button>
        <el-button size="small" type="warning">导出</el-button>
      </el-button-group>
      <el-table ref="listTable" :data="list.data" @sort-change="handleSortChange" :max-height="maxTableHeight" border size="mini" resizable>
        <el-table-column prop="stats_date" label="统计日期" width="95" sortable="custom">
          <template slot-scope="scope">
            <span class="btn-link" @click="$router.push({ name: 'orgList', query: { stats_date: scope.row.stats_date } })">{{scope.row.stats_date}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="card_total" label="售卡总数" width="73"></el-table-column>
        <el-table-column prop="pay_total" label="累冲成数" width="73"></el-table-column>
        <el-table-column prop="pay_count" label="日冲次数" width="90" sortable="custom"></el-table-column>
        <el-table-column prop="pay_failed" label="日冲败数" width="90" sortable="custom"></el-table-column>
        <el-table-column prop="pay_succeed" label="日冲成数" width="90" sortable="custom"></el-table-column>
        <el-table-column prop="online_count" label="使用总量" width="73"></el-table-column>
        <el-table-column prop="online_api" label="正常使用" width="90" sortable="custom"></el-table-column>
        <el-table-column prop="online_other" label="异常使用" width="90" sortable="custom"></el-table-column>
        <el-table-column prop="active_total" label="累计激活" width="73"></el-table-column>
        <el-table-column prop="active_count" label="激活总数" width="90" sortable="custom"></el-table-column>
        <el-table-column prop="active_unicom" label="非设备激活" width="102" sortable="custom"></el-table-column>
        <el-table-column prop="active_device" label="设备端激活" width="102" sortable="custom"></el-table-column>
        <el-table-column prop="stop_total" label="累计停卡" width="90" sortable="custom"></el-table-column>
        <el-table-column prop="stop_count" label="停卡数量" width="90" sortable="custom"></el-table-column>
        <el-table-column prop="used_amount" label="消耗流量" width="90" sortable="custom">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.used_amount)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="pay_succeed_money" label="已付金额" min-width="106" sortable="custom">
          <template slot-scope="scope">
            <div>￥{{scope.row.pay_succeed_money|formatMoney}}</div>
          </template>
        </el-table-column>
        <el-table-column prop="rebate_money" label="返利金额" min-width="106" sortable="custom">
          <template slot-scope="scope">
            <div>￥{{scope.row.rebate_money|formatMoney}}</div>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="list.currentPage" :page-sizes="pageSizes" :page-size="list.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="list.total" class="clearfix">
      </el-pagination>
    </el-card>
    <v-dialog :dialogPara="dialogPara"></v-dialog>
  </div>
</template>
<script>
import Api from 'assets/js/api.js'
import { mapState, mapMutations } from 'vuex'
const _echart = new Api.ECHARTS()

export default {
  data() {
    return {
      // 要展开的对话框的参数
      dialogPara: {
        loadDialog: true,
        isShowCancelBtn: false,
        title: '',
        content: '<div id="myChart" style="width:100%; height:350px"></div>'
      },
      // 图表实例
      myChart: null,
      chartType: '0',
      chartData: null,
      chartConst: {
        '0': {
          title: '流量卡运营充值统计图表',
          legend: ['日冲次数', '日冲成数', '日冲败数']
        },
        '1': {
          title: '流量卡运营使用统计图表',
          legend: ['使用总数', '正常使用', '非正常使用']
        },
        '2': {
          title: '流量卡运营激活统计图表',
          legend: ['激活总数', '非设备激活', '设备端激活']
        },
      }
    }
  },
  mounted() {
    this.getData()
  },
  methods: {
    getData() {
      Api.UNITS.getListData({
        vue: this,
        url: _axios.ajaxAd.getPayOnline
      })
    },
    // 打开图表的dialog
    showEcharts(type) {
      this.SET_DIALOGVISIBLE({ dialogVisible: true })
      Vue.nextTick(() => {
        // myChart因为是要放在dialog中所以必须要等到dialog展示之后才能展示
        if (!this.myChart) this.myChart = this.$echarts.init(document.getElementById('myChart'))
        this.chartType = type
        this.dialogPara.title = this.chartConst[this.chartType].title
        this.dialogPara.loadDialog = true
        this.getEchartsData()
      })
    },
    // 获取图表数据
    getEchartsData() {
      if (!this.chartData) {
        _axios.send({
          method: 'get',
          url: _axios.ajaxAd.getPayOnline,
          params: {
            ascs: 'stats_date',
            size: 9999, // 不用分页直接获取所有数据
            current: 1
          },
          done: (res) => {
            this.chartData = res.data.page.records
            this.handleChartData()
          }
        })
      } else {
        this.handleChartData()
      }
    },
    // 拿到图表数据后处理图表数据用来渲染
    handleChartData() {
      let [xAxisData, data1, data2, data3] = [
        [],
        [],
        [],
        []
      ]
      this.chartData.forEach((v) => {
        xAxisData.push(v.stats_date)
        if (this.chartType === '0') {
          data1.push(v.pay_count)
          data2.push(v.pay_succeed)
          data3.push(v.pay_failed)
        } else if (this.chartType === '1') {
          data1.push(v.online_count)
          data2.push(v.online_api)
          data3.push(v.online_other)
        } else if (this.chartType === '2') {
          data1.push(v.active_count)
          data2.push(v.active_unicom)
          data3.push(v.active_device)
        }
      })
      _echart.setOption({
        legend: this.chartConst[this.chartType].legend,
        xAxis: { data: xAxisData },
        series: [{ data: data1 },
          { data: data2 },
          { data: data3 }
        ]
      })
      this.myChart.setOption(_echart.getOption())
      this.dialogPara.loadDialog = false
    }
  },
  computed: {
    // 起始时间约数
    startDatePicker() {
      return Api.UNITS.startDatePicker(this, this.formInline.date_end)
    },
    // 结束时间约数
    endDatePicker() {
      return Api.UNITS.endDatePicker(this, this.formInline.date_start)
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

</style>
