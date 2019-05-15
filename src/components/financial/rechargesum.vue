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
      <el-table v-loading="loadData" ref="multipleTable" :data="curTableData" border :default-sort="{prop: 'recharge_total', order: 'descending'}" size="mini">
        <el-table-column show-overflow-tooltip label="机构名称" min-width="125">
          <template slot-scope="scope">
            <el-button type="text">{{scope.row.jg_name}}</el-button>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="recharge_count" label="充值次数" show-overflow-tooltip min-width="110" sortable></el-table-column>
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
        <el-table-column show-overflow-tooltip label="操作" show-overflow-tooltip min-width="80">
          <template slot-scope="scope">
            <el-button type="text">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentPage" :page-sizes="pageSizes" :page-size="list.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="list.total" class="clearfix">
      </el-pagination>
    </el-card>
    <el-card class="box-card clearfix" shadow="never">
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
                let axisData = opt.xAxis[0].data
                let series = opt.series
                let table = `<table style="width:100%;text-align:center"><tbody><tr>
                  <td>机构名称</td>
                    <td>${series[0].name}</td>
                  </tr>`
                for (let i = 0, l = axisData.length; i < l; i++) {
                  table += `<tr>
                    <td>${axisData[i]}</td>
                    <td>${series[0].data[i]}</td>
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
        yAxis: {
          type: 'value',
          splitLine: { show: false }
        },
        xAxis: {
          type: 'category',
          data: [], //要设置的
          axisLabel: {
            interval: 0,
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
              position: 'top'
            }
          },
          data: [], //要设置的
          itemStyle: {
            normal: {
              color(params) {
                const colorList = Api.STATIC.colorList
                return colorList[params.dataIndex]
              }
            }
          }
        }]
      }
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
      this.getOptionData()
    },
    handleSizeChange(val) {
      this.list.pagesize = val
      this.getData()
    },
    handleCurrentChange(val) {
      this.list.currentPage = val
      this.getData()
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
    // 获取列表数据
    getData() {
      setTimeout(() => {
        // 数据请求成功
        this.list.data = [{
          id: 0,
          jg_name: '卡仕特-西格玛',
          recharge_count: 28,
          fptotal_flow: 12452,
          recharge_total: 564521.23,
          repay_total: 2352.32
        }, {
          id: 0,
          jg_name: '自营',
          recharge_count: 14,
          fptotal_flow: 22452,
          recharge_total: 564521.23,
          repay_total: 2352.32
        }, {
          id: 0,
          jg_name: '自营 > 双平泰',
          recharge_count: 30,
          fptotal_flow: 8952,
          recharge_total: 564521.23,
          repay_total: 2352.32
        }, {
          id: 0,
          jg_name: '凯隆丁威特',
          recharge_count: 65,
          fptotal_flow: 85452,
          recharge_total: 564521.23,
          repay_total: 2352.32
        }, {
          id: 0,
          jg_name: '博毅',
          recharge_count: 15,
          fptotal_flow: 23562,
          recharge_total: 564521.23,
          repay_total: 2352.32
        }, {
          id: 0,
          jg_name: '天之眼',
          recharge_count: 33,
          fptotal_flow: 14585,
          recharge_total: 564521.23,
          repay_total: 2352.32
        }, {
          id: 0,
          jg_name: '湖南纽曼',
          recharge_count: 41,
          fptotal_flow: 8754,
          recharge_total: 564521.23,
          repay_total: 2352.32
        }, {
          id: 0,
          jg_name: '自营 > 云智测试',
          recharge_count: 9,
          fptotal_flow: 6854,
          recharge_total: 564521.23,
          repay_total: 2352.32
        }, {
          id: 0,
          jg_name: '奇橙天下',
          recharge_count: 48,
          fptotal_flow: 68745,
          recharge_total: 564521.23,
          repay_total: 2352.32
        }, {
          id: 0,
          jg_name: '自营 > 云智易联',
          recharge_count: 74,
          fptotal_flow: 85452,
          recharge_total: 564521.23,
          repay_total: 2352.32
        }]
        this.list.total = this.list.data.length
        this.loadData = false
        // 数据拿到了就可以格式化图表数据了
        this.getOptionData()
      }, 1000)
    },
    // 获取图表数据
    getOptionData() {
      let option = this.option
      let chartConst = this.chartConst[this.tabIndex]
      let label = option.xAxis.data = [] // 底坐标标签
      let data1 = option.series[0].data = [] // 分类一数据
      option.tooltip.formatter = this.formatter
      option.title.text = chartConst.title
      option.series[0].name = chartConst.name
      this.list.data.forEach((v) => {
        label.push(v.jg_name)
        if (this.tabIndex === '0') {
          data1.push(v.recharge_count)
        } else if (this.tabIndex === '1') {
          data1.push(v.fptotal_flow)
        } else if (this.tabIndex === '2') {
          data1.push(v.recharge_total)
        }
      })
      // 绘图
      setTimeout(() => {
        this.myChart.setOption(option)
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
