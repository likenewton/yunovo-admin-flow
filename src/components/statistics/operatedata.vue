<template>
  <div>
    <el-card class="box-card" style="margin-bottom: 20px" shadow="never">
      <el-form :inline="true" :model="formInline" class="demo-form-inline" size="small">
        <el-form-item label="统计日期">
          <el-date-picker v-model="formInline.statistic_data" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary">查询</el-button>
          <el-button type="warning">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="box-card clearfix" shadow="never">
      <el-button-group style="margin-bottom: 10px">
        <el-button size="mini" type="primary" @click="showEcharts('0')">支付图表</el-button>
        <el-button size="mini" type="primary" @click="showEcharts('1')">在线图表</el-button>
        <el-button size="mini" type="primary" @click="showEcharts('2')">激活图表</el-button>
        <el-button size="mini" type="warning">导出</el-button>
      </el-button-group>
      <el-table v-loading="loadData" ref="multipleTable" :data="curTableData" border :default-sort="{prop: 'statistic_data', order: 'descending'}" size="mini">
        <el-table-column show-overflow-tooltip prop="statistic_data" label="统计日期" width="93" sortable></el-table-column>
        <el-table-column show-overflow-tooltip prop="sell_num" label="售卡总数" width="93" sortable></el-table-column>
        <el-table-column show-overflow-tooltip prop="recharge_total" label="累冲成数" width="93" sortable></el-table-column>
        <el-table-column show-overflow-tooltip prop="recharge_day" label="日冲次数" width="73"></el-table-column>
        <el-table-column show-overflow-tooltip prop="recharge_fail_day" label="日冲败数" width="73"></el-table-column>
        <el-table-column show-overflow-tooltip prop="recharge_suc_day" label="日冲成数" width="93" sortable></el-table-column>
        <el-table-column show-overflow-tooltip prop="use_total" label="使用总量" width="93" sortable></el-table-column>
        <el-table-column show-overflow-tooltip prop="normal_use" label="正常使用" width="73"></el-table-column>
        <el-table-column show-overflow-tooltip prop="abnormal_use" label="异常使用" width="73"></el-table-column>
        <el-table-column show-overflow-tooltip prop="active_total" label="累计激活" width="93" sortable></el-table-column>
        <el-table-column show-overflow-tooltip prop="active_num" label="激活总数" width="93" sortable></el-table-column>
        <el-table-column show-overflow-tooltip prop="abeq_num" label="非设备激活" width="85"></el-table-column>
        <el-table-column show-overflow-tooltip prop="eq_num" label="设备端激活" width="85"></el-table-column>
        <el-table-column show-overflow-tooltip prop="stopcard_total" label="累计停卡" width="93" sortable></el-table-column>
        <el-table-column show-overflow-tooltip prop="stopcard_num" label="停卡数量" width="93" sortable></el-table-column>
        <el-table-column show-overflow-tooltip label="消耗流量" width="95">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.use_flow)"></div>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip label="已付金额" min-width="110">
          <template slot-scope="scope">
            <div>￥{{scope.row.pay_money|formatMoney}}</div>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip label="返利金额" min-width="110">
          <template slot-scope="scope">
            <div>￥{{scope.row.repay_money|formatMoney}}</div>
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

export default {
  data() {
    return {
      loadData: true,
      pageSizes: Api.STATIC.pageSizes,
      list: {
        data: [],
        pagesize: Api.STATIC.pageSizes[1],
        currentPage: 1,
        total: 0,
      },
      formInline: {},
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
      },
      option: {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: []
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        toolbox: {
          show: true,
          right: 20,
          feature: {
            dataZoom: {
              yAxisIndex: 'none'
            },
            dataView: {
              show: true,
              iconStyle: {
                borderColor: '#9a83da'
              },
              emphasis: {
                iconStyle: {
                  borderColor: '#9a8dda'
                }
              },
              optionToContent(opt) {
                let axisData = opt.xAxis[0].data
                let series = opt.series
                let table = `<table style="width:100%;text-align:center"><tbody><tr>
                  <td>时间</td>
                    <td>${series[0].name}</td>
                    <td>${series[1].name}</td>
                    <td>${series[2].name}</td>
                  </tr>`
                for (let i = 0, l = axisData.length; i < l; i++) {
                  table += `<tr>
                    <td>${axisData[i]}</td>
                    <td>${series[0].data[i]}</td>
                    <td>${series[1].data[i]}</td>
                    <td>${series[2].data[i]}</td>
                    </tr>`
                }
                table += '</tbody></table>'
                return table
              },
              // 调用optionToContent之后一定要配置此项
              contentToOption() {}
            },
            restore: {
              show: true,
              iconStyle: {
                borderColor: '#ffc367'
              },
              emphasis: {
                iconStyle: {
                  borderColor: '#ffcf85'
                }
              }
            },
            saveAsImage: {
              show: true,
              iconStyle: {
                borderColor: '#3cb1ff'
              },
              emphasis: {
                iconStyle: {
                  borderColor: '#63c1ff'
                }
              }
            }
          }
        },
        dataZoom: [{
          startValue: '2019-01-14'
        }, {
          type: 'inside'
        }],
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
          name: '日冲次数',
          type: 'line',
          data: [], //要设置的
          itemStyle: {
            normal: {
              color: '#3cb1ff',
            }
          }
        }, {
          name: '日冲成数',
          type: 'line',
          data: [], //要设置的
          itemStyle: {
            normal: {
              color: '#27da99'
            }
          }
        }, {
          name: '日冲败数',
          type: 'line',
          data: [], //要设置的
          itemStyle: {
            normal: {
              color: '#ff7477'
            }
          }
        }]
      },
    }
  },
  mounted() {
    // 进入页面的时候请求数据
    if (this.list.data.length === 0) {
      this.getData()
    } else {
      this.loadData = false
    }
  },
  methods: {
    ...mapMutations([
      'SET_DIALOGVISIBLE'
    ]),
    handleSizeChange(val) {
      this.list.pagesize = val
      this.getData()
    },
    handleCurrentChange(val) {
      this.list.currentPage = val
      this.getData()
    },
    showEcharts(type) {
      this.SET_DIALOGVISIBLE({ dialogVisible: true })
      setTimeout(() => {
        // myChart因为是要放在dialog中所以必须要等到dialog展示之后才能展示
        if (!this.myChart) this.myChart = this.$echarts.init(document.getElementById('myChart'))
        this.chartType = type
        this.getEchartsData()
      }, 0)
    },
    // 获取列表数据
    getData() {
      setTimeout(() => {
        // 数据请求成功
        this.list.data = [{
          id: 0,
          statistic_data: '2019-05-06',
          sell_num: 56546,
          recharge_total: 41451,
          recharge_day: 121,
          recharge_fail_day: 71,
          recharge_suc_day: 50,
          use_total: 80000,
          normal_use: 50000,
          abnormal_use: 30000,
          active_total: 50000,
          active_num: 80,
          abeq_num: 50,
          eq_num: 30,
          stopcard_total: 60000,
          stopcard_num: 100,
          use_flow: 65452.3,
          pay_money: -2030.94,
          repay_money: 451112.51
        }]
        this.list.total = this.list.data.length
        this.loadData = false
      }, 1000)
    },
    // 获取图标数据
    getEchartsData() {
      // 该echart 中的数据似乎跟list没有关系
      const constData = this.chartConst[this.chartType]
      this.dialogPara.title = constData.title
      this.dialogPara.loadDialog = true
      setTimeout(() => {
        // 这里拿到数据后要将数据保存到options中在调用setOption
        this.option.legend.data = constData.legend
        this.option.xAxis.data = Api.STATIC.echarts.legend[this.chartType]
        constData.legend.forEach((v, i) => {
          this.option.series[i].name = v
          this.option.series[i].data = Api.STATIC.echarts.data[this.chartType][i]
        })
        this.myChart.setOption(this.option)
        this.dialogPara.loadDialog = false
      }, 1000)
    },
    formatFlowUnit: Api.UNITS.formatFlowUnit,
    calcLeftTime: Api.UNITS.calcLeftTime
  },
  computed: {
    ...mapState({
      dialogVisible: 'dialogVisible',
    }),
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

</style>
