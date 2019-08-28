<template>
  <div class="operate_data">
    <el-card class="clearfix" shadow="never" v-loading="loadData">
      <el-button-group style="margin-bottom: 10px">
        <el-button size="small" type="primary" @click="showEcharts('0')" :disabled="!pageAuthBtn.FCP_02_008_ECHART01">支付图表</el-button>
        <el-button size="small" type="primary" @click="showEcharts('1')" :disabled="!pageAuthBtn.FCP_02_008_ECHART02">在线图表</el-button>
        <el-button size="small" type="primary" @click="showEcharts('2')" :disabled="!pageAuthBtn.FCP_02_008_ECHART03">激活图表</el-button>
        <el-button size="small" type="warning" :disabled="!pageAuthBtn.FCP_02_008_EXPORT01" @click="exportExcel">导出</el-button>
      </el-button-group>
      <el-form :inline="true" :model="formInline" class="search-form" size="small">
        <el-form-item>
          <el-date-picker v-model="formInline.date_start" :picker-options="startDatePicker" type="date" value-format="yyyy-MM-dd" @change="searchData" placeholder="统计日期开始"></el-date-picker> -
          <el-date-picker v-model="formInline.date_end" :picker-options="endDatePicker" type="date" value-format="yyyy-MM-dd" @change="searchData" placeholder="统计日期结束"></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchData" :disabled="!pageAuthBtn.FCP_02_008_CHECK01">查询</el-button>
          <el-button type="warning" @click="resetData" :disabled="!pageAuthBtn.FCP_02_008_CHECK01">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table ref="listTable" :data="list.data" @sort-change="handleSortChange" :max-height="maxTableHeight" border size="mini" resizable>
        <el-table-column prop="stats_date" label="统计日期" :width="widthMap.stats_date[size]" sortable="custom">
          <template slot-scope="scope">
            <span v-if="pageAuthBtn.FCP_02_008_LINK1" class="btn-link" @click="$router.push({ name: 'orgList', query: { stats_date: scope.row.stats_date } })">{{scope.row.stats_date}}</span>
            <span v-else>{{scope.row.stats_date}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="card_total" label="售卡总数" :min-width="widthMap.card_total[size]" align="right" sortable="custom"></el-table-column>
        <el-table-column prop="pay_total" label="累充成数" :min-width="widthMap.pay_total[size]" align="right" sortable="custom"></el-table-column>
        <el-table-column prop="pay_count" label="日充次数" :width="widthMap.pay_count[size]" sortable="custom" align="right"></el-table-column>
        <el-table-column prop="pay_failed" label="日充败数" :width="widthMap.pay_failed[size]" sortable="custom" align="right"></el-table-column>
        <el-table-column prop="pay_succeed" label="日充成数" :width="widthMap.pay_succeed[size]" sortable="custom" align="right"></el-table-column>
        <el-table-column prop="online_count" label="使用总量" :min-width="widthMap.online_count[size]" sortable="custom" align="right"></el-table-column>
        <el-table-column prop="online_api" label="正常使用" :min-width="widthMap.online_api[size]" sortable="custom" align="right"></el-table-column>
        <el-table-column prop="online_other" label="异常使用" :min-width="widthMap.online_other[size]" sortable="custom" align="right"></el-table-column>
        <el-table-column prop="active_total" label="累计激活" :min-width="widthMap.active_total[size]" sortable="custom" align="right"></el-table-column>
        <el-table-column prop="active_count" label="激活总数" :min-width="widthMap.active_count[size]" sortable="custom" align="right"></el-table-column>
        <el-table-column prop="active_unicom" label="非设备激活" :min-width="widthMap.active_unicom[size]" sortable="custom" align="right"></el-table-column>
        <el-table-column prop="active_device" label="设备端激活" :min-width="widthMap.active_device[size]" sortable="custom" align="right"></el-table-column>
        <el-table-column prop="stop_total" label="累计停卡" :min-width="widthMap.stop_total[size]" sortable="custom" align="right"></el-table-column>
        <el-table-column prop="stop_count" label="停卡数量" :width="widthMap.stop_count[size]" sortable="custom" align="right"></el-table-column>
        <el-table-column prop="used_amount" label="消耗流量" :min-width="widthMap.used_amount[size]" sortable="custom" align="right">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.used_amount)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="pay_succeed_money" label="已付金额" :min-width="widthMap.pay_succeed_money[size]" sortable="custom" align="right">
          <template slot-scope="scope">
            <div>￥{{scope.row.pay_succeed_money|formatMoney}}</div>
          </template>
        </el-table-column>
        <el-table-column prop="rebate_money" label="返利金额" :min-width="widthMap.rebate_money[size]" sortable="custom" align="right">
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
const _echart = new Api.ECHARTS({
  dataViewTitle: '统计日期'
})

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
      size: Api.UNITS.getSize(),
      widthMap: {
        stats_date: [93, 93],
        card_total: [87, 68],
        pay_total: [87, 68],
        pay_count: [87, 45],
        pay_failed: [87, 45],
        pay_succeed: [87, 45],
        online_count: [87, 68],
        online_api: [87, 70],
        online_other: [87, 70],
        active_total: [87, 68],
        active_count: [87, 70],
        active_unicom: [96, 80],
        active_device: [96, 80],
        stop_total: [87, 68],
        stop_count: [87, 45],
        used_amount: [87, 85],
        pay_succeed_money: [98, 95],
        rebate_money: [98, 95],
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
    // 导出excel
    exportExcel() {
      Api.UNITS.exportExcel(_axios.ajaxAd.operatedataExport, this.formInline)
    },
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
.operate_data {
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

  @media screen and (max-width: 1900px) {
    .is-sortable {
      .caret-wrapper {
        display: none;
      }
    }
  }

}

</style>
