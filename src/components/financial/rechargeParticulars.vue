<template>
  <div class="recharge_particulars">
    <el-card class="box-card" style="margin-bottom: 20px" shadow="never">
      <el-form :inline="true" :model="formInline" class="demo-form-inline" size="small">
        <el-form-item label="卡ICCID">
          <el-input v-model="formInline.iccid" placeholder="请输入卡的iccid"></el-input>
        </el-form-item>
        <el-form-item label="卡商名称">
          <el-select v-model="formInline.ks_name" placeholder="请选择">
            <el-option label="卡商1" value="0"></el-option>
            <el-option label="卡商2" value="1"></el-option>
            <el-option label="卡商3" value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="机构名称">
          <el-select v-model="formInline.jg_name" placeholder="请选择">
            <el-option label="机构1" value="0"></el-option>
            <el-option label="机构2" value="1"></el-option>
            <el-option label="机构3" value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary">查询</el-button>
          <el-button type="warning">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="box-card clearfix" shadow="never">
      <el-button-group style="margin-bottom: 10px">
        <el-button size="mini" type="primary" @click="showEcharts()">图表</el-button>
        <el-button size="mini" type="warning">导出明细</el-button>
        <el-button size="mini" type="warning">导出快照</el-button>
      </el-button-group>
      <el-table v-loading="loadData" ref="multipleTable" :data="curTableData" border :default-sort="{prop: 'statistic_data', order: 'descending'}" size="mini">
        <el-table-column show-overflow-tooltip prop="order_code" label="5订单编号" width="172"></el-table-column>
        <el-table-column show-overflow-tooltip label="ICCID卡" width="176">
          <template slot-scope="scope">
            <el-button type="text">{{scope.row.iccid}}</el-button>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="ks_name" label="卡商名称" min-width="135"></el-table-column>
        <el-table-column show-overflow-tooltip prop="jg_name" label="机构名称" min-width="135"></el-table-column>
        <el-table-column show-overflow-tooltip prop="pay_way" label="付款方式" width="80"></el-table-column>
        <el-table-column show-overflow-tooltip prop="pay_code" label="支付流水号" min-width="160"></el-table-column>
        <el-table-column show-overflow-tooltip label="套餐流量" width="95">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.combo_flow)"></div>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip label="套餐价格" width="110">
          <template slot-scope="scope">
            <div>￥{{scope.row.combo_price|formatMoney}}</div>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip label="返利金额" width="110">
          <template slot-scope="scope">
            <div>￥{{scope.row.repay_money|formatMoney}}</div>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="recharge_time" label="充值时间" width="153" sortable></el-table-column>
        <el-table-column show-overflow-tooltip prop="pay_status" label="支付状态" width="75"></el-table-column>
        <el-table-column show-overflow-tooltip prop="pay_time" label="付款时间" width="153" sortable></el-table-column>
        <el-table-column show-overflow-tooltip prop="order_resource" label="订单来源" width="80"></el-table-column>
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
                borderColor: '#9a83da'
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
        legend: {
          top: 40,
          data: ['微信', '支付宝', '充值卡', '银行转账', '已付款', '未付款'],
          selected: {
            '微信': true,
            '支付宝': true,
            '充值卡': false,
            '银行转账': false,
            '已付款': true,
            '未付款': true
          }
        },
        series: [{
          name: '支付方式',
          type: 'pie',
          radius: '60%',
          center: ['30%', '60%'],
          data: [
            { value: 500, name: '微信' },
            { value: 310, name: '支付宝' },
            { value: 0, name: '充值卡' },
            { value: 0, name: '银行转账' }
          ],
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
          data: [
            { value: 200, name: '已付款' },
            { value: 100, name: '未付款' }
          ],
          itemStyle: {
            normal: {
              color(params) {
                return Api.UNITS.getColorList(['editor', 'danger'])[params.dataIndex]
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
        this.getEchartsData()
      }, 0)
    },
    // 获取列表数据
    getData() {
      setTimeout(() => {
        // 数据请求成功
        this.list.data = [{
          id: 0,
          order_code: '190429-140217-873768',
          iccid: '89860918700307506235',
          ks_name: '智网吉林11位卡',
          jg_name: '自营 > 云智测试',
          pay_way: '系统赠送',
          pay_code: 'system-give 系统赠送',
          combo_flow: 563.5,
          combo_price: 545.25,
          repay_money: 54.21,
          recharge_time: '2018-05-54 09:14:14',
          pay_status: '已支付',
          pay_time: '2018-05-54 09:14:14',
          order_resource: '未知来源'
        }]
        this.list.total = this.list.data.length
        this.loadData = false
      }, 1000)
    },
    // 获取图表数据
    getEchartsData() {
      // 该echart 中的数据似乎跟list没有关系
      this.dialogPara.loadDialog = true
      setTimeout(() => {
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
