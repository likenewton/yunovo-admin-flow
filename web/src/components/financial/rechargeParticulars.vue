<template>
  <div class="recharge_particulars">
    <el-card class="box-card clearfix" shadow="never" v-loading="loadData">
      <el-button-group style="margin-bottom: 10px">
        <el-button size="small" type="primary" @click="showEcharts()" :disabled="!pageAuthBtn.FCP_03_003_ECAHRT01">图表</el-button>
        <el-button size="small" type="warning" :disabled="!pageAuthBtn.FCP_03_003_EXPORT01" @click="exportExcel">导出明细</el-button>
        <el-button size="small" type="warning" :disabled="!pageAuthBtn.FCP_03_003_EXPORT02" @click="exportExcel_2">导出快照</el-button>
      </el-button-group>
      <el-form class="search-form" :inline="true" :model="formInline" size="small" @submit.native.prevent>
        <el-form-item>
          <el-input v-model="formInline.pay_sn" placeholder="订单编号" @keyup.enter.native="simpleSearchData"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="simpleSearchData" :disabled="!pageAuthBtn.FCP_03_003_CHECK01">查询</el-button>
          <el-button type="primary" @click="searchVipVisible = true" :disabled="!pageAuthBtn.FCP_03_003_CHECK01">高级查询</el-button>
        </el-form-item>
      </el-form>
      <el-table ref="listTable" @sort-change="handleSortChange" :data="list.data" :max-height="maxTableHeight" border resizable size="mini">
        <el-table-column prop="pay_sn" label="订单编号" width="145" sortable="custom"></el-table-column>
        <el-table-column prop="card_iccid" label="ICCID卡" width="178" sortable="custom">
          <template slot-scope="scope">
            <span v-if="scope.row.sums|| !pageAuthBtn.FCP_03_003_LINK01">{{scope.row.card_iccid}}</span>
            <span v-else class="btn-link" @click="$router.push({ name: 'rechargeDetail', query: {card_id: scope.row.card_id}})">{{scope.row.card_iccid}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="card_id" label="卡商名称" width="135" sortable="custom">
          <template slot-scope="scope">
            <span>{{scope.row.card_type_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="org_id" label="机构名称" min-width="135" sortable="custom">
          <template slot-scope="scope">
            <span v-if="scope.row.sums || !pageAuthBtn.FCP_03_003_LINK02">{{scope.row.org_name}}</span>
            <span v-else class="btn-link" @click="$router.push({name: 'card', query: {org_id: scope.row.org_id}})">{{scope.row.org_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="pay_method" label="付款方式" width="88" sortable="custom">
          <template slot-scope="scope">
            <span>{{scope.row.pay_method_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="transfer_id" label="支付流水号" width="135" sortable="custom"></el-table-column>
        <el-table-column prop="gprs_amount" label="套餐流量" width="95" sortable="custom" align="right">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.gprs_amount)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="gprs_price" label="套餐价格" width="125" sortable="custom" align="right">
          <template slot-scope="scope">
            <div>￥{{scope.row.gprs_price|formatMoney}}</div>
          </template>
        </el-table-column>
        <el-table-column porp="rebate_money" label="返利金额" width="106" sortable="custom" align="right">
          <template slot-scope="scope">
            <div>￥{{scope.row.rebate_money|formatMoney}}</div>
          </template>
        </el-table-column>
        <el-table-column prop="time_added" label="充值时间" width="153" sortable="custom"></el-table-column>
        <el-table-column prop="is_paid" label="支付状态" width="88" sortable="custom">
          <template slot-scope="scope" v-if="!scope.row.sums">
            <span v-if="scope.row.is_paid == 0">未付款</span>
            <span v-else>已付款</span>
          </template>
        </el-table-column>
        <el-table-column prop="time_paid" label="付款时间" width="153" sortable="custom"></el-table-column>
        <el-table-column prop="pay_from" label="订单来源" width="88" sortable="custom">
          <template slot-scope="scope">
            <span>{{scope.row.pay_from_name}}</span>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="list.currentPage" :page-sizes="pageSizes" :page-size="list.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="list.total" class="clearfix">
      </el-pagination>
    </el-card>
    <v-dialog :dialogPara="dialogPara"></v-dialog>
    <el-dialog title="高级查询" :visible.sync="searchVipVisible" width="730px">
      <div slot>
        <div class="searchForm_vip" style="width:100%;overflow: auto">
          <el-form :inline="false" :model="formInline" size="small" label-width="100px">
            <el-form-item label="订单编号">
              <el-input v-model="formInline.pay_sn" placeholder="请输入订单编号"></el-input>
            </el-form-item>
            <el-form-item label="卡ICCID">
              <el-input v-model="formInline.card_iccid" @input="formInline.card_iccid = limitNumber(formInline.card_iccid, 20)" placeholder="请输入卡的iccid"></el-input>
            </el-form-item>
            <el-form-item label="支付流水号">
              <el-input v-model="formInline.transfer_id" placeholder="请输入支付流水号"></el-input>
            </el-form-item>
            <el-form-item label="卡商名称">
              <el-select v-model="formInline.card_type" filterable clearable placeholder="请选择卡商">
                <el-option v-for="(item, index) in cardTypes" :key="index" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="机构名称">
              <el-select v-model="formInline.org_id" filterable clearable placeholder="请选择机构">
                <el-option v-for="(item, index) in orgs" :key="index" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="订单来源">
              <el-select v-model="formInline.pay_from" filterable clearable placeholder="请选择订单来源">
                <el-option v-for="(item, index) in notifysFrom" :key="index" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="支付状态">
              <el-select v-model="formInline.is_paid" filterable clearable placeholder="请选择支付状态">
                <el-option v-for="(item, index) in paySelect" :key="index" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="套餐流量">
              <el-select v-model="formInline.gprs_amount" filterable clearable placeholder="请选择套餐流量">
                <el-option v-for="(item, index) in gprsAmount" :key="index" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="付款方式">
              <el-select v-model="formInline.pay_method" filterable clearable placeholder="请选择">
                <el-option v-for="(item, index) in payMethodSelect" :key="index" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="充值日期">
              <el-date-picker v-model="formInline.date_start" :picker-options="startDatePicker" type="date" value-format="yyyy-MM-dd" placeholder="选择开始日期"></el-date-picker> -
              <el-date-picker v-model="formInline.date_end" :picker-options="endDatePicker" type="date" value-format="yyyy-MM-dd" placeholder="选择结束日期"></el-date-picker>
            </el-form-item>
            <el-form-item label="付款日期">
              <el-date-picker v-model="formInline.paid_start" :picker-options="startDatePicker_2" type="date" value-format="yyyy-MM-dd" placeholder="选择开始日期"></el-date-picker> -
              <el-date-picker v-model="formInline.paid_end" :picker-options="endDatePicker_2" type="date" value-format="yyyy-MM-dd" placeholder="选择结束日期"></el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="searchData">查询</el-button>
              <el-button type="warning" @click="resetData">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import Api from 'assets/js/api.js'
import { mapState, mapMutations } from 'vuex'

const _echart = new Api.ECHARTS()

export default {
  data() {
    return {
      gprsAmount: [], // 套餐流量下拉列表
      formInline: {
        pay_sn: Api.UNITS.getQuery('pay_sn'),
        org_id: Api.UNITS.getQuery('org_id')
      },
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
    // 导出excel
    exportExcel() {
      Api.UNITS.exportExcel(_axios.ajaxAd.rechargeParticularsExport, this.formInline)
    },
    exportExcel_2() {
      Api.UNITS.exportExcel(_axios.ajaxAd.rechargeParticularsExport_2, this.formInline)
    },
    // 重置列表
    resetData() {
      this.list.currentPage = 1
      this.formInline = {
        pay_sn: Api.UNITS.getQuery('pay_sn'),
        org_id: Api.UNITS.getQuery('org_id')
      } // 1、重置查询表单
      this.sort = {} // 2、重置排序
      this.$refs.listTable.clearSort() // 3、清空排序样式
      this.getData()
    },
    // 简单查询
    simpleSearchData() {
      let pay_sn = this.formInline.pay_sn
      this.formInline = {
        pay_sn,
        org_id: Api.UNITS.getQuery('org_id')
      }
      this.searchData()
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
            this.showMsgBox({
              type: 'info',
              message: '暂无数据！'
            })
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
    }
  },
  computed: {
    // 起始时间约数
    startDatePicker() {
      return Api.UNITS.startDatePicker(this, this.formInline.date_end)
    },
    startDatePicker_2() {
      return Api.UNITS.startDatePicker(this, this.formInline.paid_end)
    },
    // 结束时间约数
    endDatePicker() {
      return Api.UNITS.endDatePicker(this, this.formInline.date_start)
    },
    endDatePicker_2() {
      return Api.UNITS.endDatePicker(this, this.formInline.paid_start)
    }
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
