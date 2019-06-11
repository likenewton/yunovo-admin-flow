<template>
  <div class="recharge_particulars">
    <el-card style="margin-bottom: 20px" shadow="never">
      <el-form class="search-form" :inline="true" :model="formInline" size="small">
        <el-form-item label="订单编号">
          <el-input v-model="formInline.pay_sn" placeholder="请输入订单编号"></el-input>
        </el-form-item>
        <el-form-item label="卡ICCID">
          <el-input v-model="formInline.cardd_iccid" placeholder="请输入卡的iccid"></el-input>
        </el-form-item>
        <el-form-item label="支付流水号">
          <el-input v-model="formInline.transfer_id" placeholder="请输入支付流水号"></el-input>
        </el-form-item>
        <el-form-item label="卡商名称">
          <el-select v-model="formInline.card_type" filterable clearable placeholder="请选择">
            <el-option v-for="(item, index) in cardTypes" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="机构名称">
          <el-select v-model="formInline.org_id" filterable clearable placeholder="请选择">
            <el-option v-for="(item, index) in orgs" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="订单来源">
          <el-select v-model="formInline.pay_from" filterable clearable placeholder="请选择">
            <el-option v-for="(item, index) in notifysFrom" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="支付状态">
          <el-select v-model="formInline.is_paid" filterable clearable placeholder="请选择">
            <el-option v-for="(item, index) in paySelect" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="套餐流量">
          <el-select v-model="formInline.gprs_amount" filterable clearable placeholder="请选择">
            <el-option v-for="(item, index) in gprsAmount" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="充值日期">
          <el-date-picker v-model="formInline.date_start" type="date" value-format="yyyy-MM-dd" placeholder="选择开始日期"></el-date-picker> -
          <el-date-picker v-model="formInline.date_end" type="date" value-format="yyyy-MM-dd" placeholder="选择结束日期"></el-date-picker>
        </el-form-item>
        <el-form-item label="付款日期">
          <el-date-picker v-model="formInline.paid_start" type="date" value-format="yyyy-MM-dd" placeholder="选择开始日期"></el-date-picker> -
          <el-date-picker v-model="formInline.paid_end" type="date" value-format="yyyy-MM-dd" placeholder="选择结束日期"></el-date-picker>
        </el-form-item>
        <el-form-item label="付款方式">
          <el-select v-model="formInline.pay_method" filterable clearable placeholder="请选择">
            <el-option v-for="(item, index) in payMethodSelect" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchData">查询</el-button>
          <el-button type="warning" @click="resetData">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="box-card clearfix" shadow="never" v-loading="loadData">
      <el-button-group style="margin-bottom: 10px">
        <el-button size="mini" type="primary" @click="showEcharts()">图表</el-button>
        <el-button size="mini" type="warning">导出明细</el-button>
        <el-button size="mini" type="warning">导出快照</el-button>
      </el-button-group>
      <el-table ref="listTable" @sort-change="handleSortChange" :data="list.data" :max-height="maxTableHeight" border resizable size="mini">
        <el-table-column prop="pay_sn" label="订单编号" min-width="172" sortable="custom"></el-table-column>
        <el-table-column prop="card_iccid" label="ICCID卡" min-width="180" sortable="custom">
          <template slot-scope="scope">
            <span v-if="scope.row.sums">{{scope.row.card_iccid}}</span>
            <span v-else class="btn-link">{{scope.row.card_iccid}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="card_id" label="卡商名称" min-width="135" sortable="custom">
          <template slot-scope="scope">
            <span>{{scope.row.card_type_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="org_id" label="机构名称" min-width="135" sortable="custom">
          <template slot-scope="scope">
            <span v-if="scope.row.sums">{{scope.row.org_name}}</span>
            <span v-else class="btn-link">{{scope.row.org_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="pay_method" label="付款方式" width="90" sortable="custom">
          <template slot-scope="scope">
            <span>{{scope.row.pay_method_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="transfer_id" label="支付流水号" min-width="140" sortable="custom"></el-table-column>
        <el-table-column prop="gprs_amount" label="套餐流量" width="95" sortable="custom">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.gprs_amount)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="gprs_price" label="套餐价格" width="100" sortable="custom">
          <template slot-scope="scope">
            <div>￥{{scope.row.gprs_price|formatMoney}}</div>
          </template>
        </el-table-column>
        <el-table-column porp="rebate_money" label="返利金额" width="90" sortable="custom">
          <template slot-scope="scope">
            <div>￥{{scope.row.rebate_money|formatMoney}}</div>
          </template>
        </el-table-column>
        <el-table-column prop="time_added" label="充值时间" width="153" sortable="custom"></el-table-column>
        <el-table-column prop="is_paid" label="支付状态" width="90" sortable="custom">
          <template slot-scope="scope" v-if="!scope.row.sums">
            <span v-if="scope.row.is_paid == 0">未付款</span>
            <span v-else>已付款</span>
          </template>
        </el-table-column>
        <el-table-column prop="time_paid" label="付款时间" width="153" sortable="custom"></el-table-column>
        <el-table-column prop="pay_from" label="订单来源" width="90" sortable="custom">
          <template slot-scope="scope">
            <span>{{scope.row.pay_from_name}}</span>
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
      loadData: true,
      pageSizes: Api.STATIC.pageSizes,
      gprsAmount: [], // 套餐流量下拉列表
      list: {
        data: [],
        pagesize: Api.STATIC.pageSizes[1],
        currentPage: 1,
        total: 0,
      },
      formInline: {},
      sort: {},
      maxTableHeight: Api.UNITS.maxTableHeight(),
      // 要展开的对话框的参数
      dialogPara: {
        loadDialog: true,
        isShowCancelBtn: false,
        title: '充值与分析统计图表',
        content: '<div id="myChart" style="width:100%; height:350px"></div>'
      },
      // 图表实例
      myChart: null,
      option: {
        tooltip: {
          trigger: 'item',
          formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        toolbox: {
          show: true,
          right: 20,
          feature: {
            dataView: {
              show: true,
              iconStyle: {
                borderColor: Api.UNITS.getColorList('purple')
              },
              emphasis: {
                iconStyle: {
                  borderColor: '#9a8dda'
                }
              },
              optionToContent(opt) {
                let series = opt.series
                let table = `<table style="width:100%;text-align:center"><tbody>
                ${function a() {
                  let str = ''
                  series.forEach((v) => {
                    str += `<tr><td style="font-weight:bold">${v.name}</td></tr>`
                    v.data.forEach((v2) => {
                      str += `<tr><td>${v2.name}</td><td>${v2.value}</td></tr>`
                    })
                    str += '<tr style="height: 20px"></tr>'
                  })
                  return str
                }()}
              </tbody></table>`
                return table
              },
              contentToOption() {},
              buttonColor: '#ff7477'
            },
            restore: {
              show: true,
              iconStyle: {
                borderColor: Api.UNITS.getColorList('warning')
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
                borderColor: Api.UNITS.getColorList('primary')
              },
              emphasis: {
                iconStyle: {
                  borderColor: '#63c1ff'
                }
              }
            }
          }
        },
        legend: {
          top: 40,
          data: [],
        },
        series: [{
          name: '支付方式',
          type: 'pie',
          radius: '60%',
          center: ['30%', '60%'],
          data: [],
          itemStyle: {
            normal: {
              color(params) {
                return Api.UNITS.getColorList(['success', 'primary', 'warning', 'purple'])[params.dataIndex]
              }
            },
            emphasis: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }, {
          name: '付款情况',
          type: 'pie',
          radius: '60%',
          center: ['70%', '60%'],
          data: [],
          itemStyle: {
            normal: {
              color(params) {
                return Api.UNITS.getColorList(['danger', 'editor'])[params.dataIndex]
              }
            },
            emphasis: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }]
      }
    }
  },
  mounted() {
    // 进入页面的时候请求数据
    this.getGprsAmount()
    this.getData()
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
        url: _axios.ajaxAd.getPayListPage,
        cb: (res) => {
          let other = res.data.other || {}
          let data = this.list.data || []
          if (data === 0) return
          data.push(...[{
            sums: true,
            pay_sn: '总计',
            gprs_amount: other.gprs_amount,
            gprs_price: other.gprs_price,
            rebate_money: other.rebate_money
          }])
        }
      })
    },
    showEcharts(type) {
      this.SET_DIALOGVISIBLE({ dialogVisible: true })
      Vue.nextTick(() => {
        // myChart因为是要放在dialog中所以必须要等到dialog展示之后才能展示
        if (!this.myChart) this.myChart = this.$echarts.init(document.getElementById('myChart'))
        this.getEchartsData()
      })
    },
    // 获取图表数据
    getEchartsData() {
      this.dialogPara.loadDialog = true
      _axios.send({
        methods: 'get',
        url: _axios.ajaxAd.getPayPie,
        params: Object.assign({}, this.formInline),
        done: (res) => {
          this.dialogPara.loadDialog = false
          let legend = this.option.legend.data = []
          let data1 = this.option.series[0].data = []
          let data2 = this.option.series[1].data = []
          let methodChart = res.data.methodChart || []
          let paidChart = res.data.paidChart || []
          if (methodChart.length === 0 && paidChart.length === 0) {
            this.$message('暂无数据！')
          }
          methodChart.forEach((v) => {
            legend.push(v.name)
            data1.push({
              value: v.value,
              name: v.name
            })
          })
          paidChart.forEach((v) => {
            legend.push(this.paySelect.filter((v1) => v1.value == v.name)[0].label)
            data2.push({
              value: v.value,
              name: this.paySelect.filter((v1) => v1.value == v.name)[0].label
            })
          })
          this.myChart.setOption(this.option)
        }
      })
    },
    getGprsAmount() {
      _axios.send({
        methods: 'get',
        url: _axios.ajaxAd.getComboFlow,
        done: (res) => {
          this.gprsAmount = []
          res.data.forEach((v) => {
            this.gprsAmount.push({
              label: Api.UNITS.formatComboFlow(v.label, false),
              value: v.value
            })
          })
        }
      })
    },
    formatFlowUnit: Api.UNITS.formatFlowUnit,
    calcLeftTime: Api.UNITS.calcLeftTime
  },
  computed: {
    ...mapState({
      dialogVisible: 'dialogVisible',
      cardTypes: 'cardTypes',
      orgs: 'orgs',
      notifysFrom: 'notifysFrom',
      paySelect: 'paySelect',
      payMethodSelect: 'payMethodSelect',
      paySelect: 'paySelect'
    })
  }
}

</script>
<style lang="scss">
.recharge_particulars {
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
}

</style>
