<template>
  <div>
    <el-card class="box-card" style="margin-bottom: 20px" shadow="never">
      <el-form :inline="true" :model="formInline" class="demo-form-inline" size="small">
        <el-form-item label="机构名称">
          <el-select v-model="formInline.org_id" placeholder="请选择">
            <el-option v-for="(item, index) in orgs" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="起止时间">
          <el-date-picker v-model="formInline.time" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getData">查询</el-button>
          <el-button type="warning" @click="formInline = {}">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="box-card clearfix" style="margin-bottom: 20px" shadow="never" v-loading="loadData">
      <el-button-group style="margin-bottom: 10px">
        <el-button size="mini" type="warning">导出</el-button>
      </el-button-group>
      <el-table ref="multipleTable" :data="list.data" :sort-change="handleSortChange" border size="mini">
        <el-table-column label="机构名称" min-width="125">
          <template slot-scope="scope">
            <el-button type="text">{{scope.row.jg_name}}</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="sell_num" label="售卡数量" min-width="95" sortable="custom"></el-table-column>
        <el-table-column prop="noactive_num" label="未激活数" min-width="95" sortable="custom"></el-table-column>
        <el-table-column prop="noactive_rate" label="未激活率" min-width="95" sortable="custom"></el-table-column>
        <el-table-column prop="active_num" label="已激活数" min-width="95" sortable="custom"></el-table-column>
        <el-table-column prop="active_rate" label="已激活率" min-width="95" sortable="custom"></el-table-column>
        <el-table-column label="分配总流量" min-width="95">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.fptotal_flow)"></div>
          </template>
        </el-table-column>
        <el-table-column label="使用流量" min-width="95">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.usetotal_flow)"></div>
          </template>
        </el-table-column>
        <el-table-column label="剩余流量" min-width="95">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.lefttotal_flow)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="useflow_rate" label="使用流量率" min-width="108" sortable="custom"></el-table-column>
      </el-table>
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentPage" :page-sizes="pageSizes" :page-size="list.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="list.total" class="clearfix">
      </el-pagination>
    </el-card>
    <el-card class="box-card clearfix" shadow="never" v-loading="echartLoadData">
      <el-tabs @tab-click="changeTab">
        <el-tab-pane>
          <span slot="label">激活情况</span>
          <div id="myChart_0" style="width:100%; height:380px"></div>
        </el-tab-pane>
        <el-tab-pane>
          <span slot="label">使用情况</span>
          <div id="myChart_1" style="width:100%; height:380px"></div>
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
      echartLoadData: true,
      tabIndex: '0',
      pageSizes: Api.STATIC.pageSizes,
      formInline: {},
      // 列表数据
      list: {
        data: [],
        pagesize: Api.STATIC.pageSizes[0],
        currentPage: 1,
        total: 0,
      },
      sort: {},
      myChart_0: null,
      myChart_1: null,
      // 激活-未激活柱状图数据
      option_0: {
        title: {
          text: '流量卡激活统计'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        legend: {
          data: ['已激活', '未激活']
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
            interval: 0,
            textStyle: {
              fontSize: 12
            }
          },
        },
        series: [{
            name: '已激活',
            type: 'bar',
            stack: '总量',
            label: {
              normal: {
                show: true,
                formatter: ''
              }
            },
            data: [], //要设置的
            itemStyle: {
              normal: {
                color: Api.UNITS.getColorList('danger'),
              }
            }
          },
          {
            name: '未激活',
            type: 'bar',
            stack: '总量',
            barMaxWidth: 100,
            label: {
              normal: {
                show: true,
                position: 'top',
                formatter: '',
                rich: {
                  a: {
                    align: 'center'
                  },
                  b: {
                    color: Api.UNITS.getColorList('danger'),
                    align: 'center'
                  }
                }
              }
            },
            data: [], //要设置的
            itemStyle: {
              normal: {
                color: Api.UNITS.getColorList('primary'),
              }
            }
          }
        ]
      },
      // 使用流量-未使用流量柱状图数据
      option_1: {
        title: {
          text: '流量卡使用统计'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          },
          formatter(series) {
            return `<div>
            <div>${series[0].axisValueLabel}</div>
              <div>
                <span style="display:inline-block;width:10px;height:10px;border-radius:50%;background:${series[0].color}"></span>
                <span>${series[0].seriesName}:</span>
                <span>${Api.UNITS.formatFlowUnit(series[0].data, 2, false)}</span>
              </div>
              <div>
                <span style="display:inline-block;width:10px;height:10px;border-radius:50%;background:${series[1].color}"></span>
                <span>${series[1].seriesName}:</span>
                <span>${Api.UNITS.formatFlowUnit(series[1].data, 2, false)}</span>
              </div>
            </div>`
          }
        },
        legend: {
          data: ['剩余流量', '使用流量']
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
            interval: 0,
            textStyle: {
              fontSize: 12
            }
          },
        },
        series: [{
            name: '剩余流量',
            type: 'bar',
            stack: '总量',
            label: {
              normal: {
                show: true,
                formatter: ''
              }
            },
            data: [], //要设置的
            itemStyle: {
              normal: {
                color: Api.UNITS.getColorList('danger'),
              }
            }
          },
          {
            name: '使用流量',
            type: 'bar',
            stack: '总量',
            label: {
              normal: {
                show: true,
                position: 'top',
                formatter: '',
                rich: {
                  a: {
                    align: 'center'
                  },
                  b: {
                    color: Api.UNITS.getColorList('danger'),
                    align: 'center'
                  }
                }
              }
            },
            data: [], //要设置的
            itemStyle: {
              normal: {
                color: Api.UNITS.getColorList('primary'),
              }
            }
          }
        ]
      }
    }
  },
  mounted() {
    setTimeout(() => {
      this.myChart_0 = this.$echarts.init(document.getElementById('myChart_0'))
    }, 0)
    this.getData()
  },
  methods: {
    routeName() {
      return this.$route.name
    },
    changeTab(para) {
      this.tabIndex = para.index
      setTimeout(() => {
        this[`myChart_${this.tabIndex}`] = this.$echarts.init(document.getElementById(`myChart_${this.tabIndex}`))
        this[`myChart_${this.tabIndex}`].resize()
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
    handleSortChange(val) {
      Api.UNITS.setSortSearch(val, this)
      this.getData()
    },
    // 获取列表数据
    getData() {
      Api.UNITS.getListData({
        vue: this,
        url: _axios.ajaxAd.getStats,
        cb: () => {
          this.getOptionData()
        }
      })
      // setTimeout(() => {
      //   // 数据请求成功
      //   this.list.data = [{
      //     id: 0,
      //     jg_name: '卡仕特-西格玛',
      //     sell_num: 12,
      //     noactive_num: 11,
      //     noactive_rate: '91.67%',
      //     active_num: 1,
      //     active_rate: '8.33%',
      //     fptotal_flow: 12452,
      //     usetotal_flow: 845214,
      //     lefttotal_flow: 421414,
      //     useflow_rate: '41.24%'
      //   }, {
      //     id: 0,
      //     jg_name: '自营',
      //     sell_num: 33,
      //     noactive_num: 6,
      //     noactive_rate: '91.67%',
      //     active_num: 27,
      //     active_rate: '8.33%',
      //     fptotal_flow: 12452,
      //     usetotal_flow: 245214,
      //     lefttotal_flow: 1421414,
      //     useflow_rate: '41.24%'
      //   }, {
      //     id: 0,
      //     jg_name: '自营 > 双平泰',
      //     sell_num: 120,
      //     noactive_num: 55,
      //     noactive_rate: '91.67%',
      //     active_num: 65,
      //     active_rate: '8.33%',
      //     fptotal_flow: 12452,
      //     usetotal_flow: 145214,
      //     lefttotal_flow: 21414,
      //     useflow_rate: '41.24%'
      //   }, {
      //     id: 0,
      //     jg_name: '凯隆丁威特',
      //     sell_num: 45,
      //     noactive_num: 11,
      //     noactive_rate: '91.67%',
      //     active_num: 34,
      //     active_rate: '8.33%',
      //     fptotal_flow: 12452,
      //     usetotal_flow: 645214,
      //     lefttotal_flow: 821414,
      //     useflow_rate: '41.24%'
      //   }, {
      //     id: 0,
      //     jg_name: '博毅',
      //     sell_num: 66,
      //     noactive_num: 19,
      //     noactive_rate: '91.67%',
      //     active_num: 47,
      //     active_rate: '8.33%',
      //     fptotal_flow: 12452,
      //     usetotal_flow: 745214,
      //     lefttotal_flow: 421414,
      //     useflow_rate: '41.24%'
      //   }, {
      //     id: 0,
      //     jg_name: '天之眼',
      //     sell_num: 100,
      //     noactive_num: 80,
      //     noactive_rate: '91.67%',
      //     active_num: 20,
      //     active_rate: '8.33%',
      //     fptotal_flow: 12452,
      //     usetotal_flow: 545214,
      //     lefttotal_flow: 421414,
      //     useflow_rate: '41.24%'
      //   }, {
      //     id: 0,
      //     jg_name: '湖南纽曼',
      //     sell_num: 30,
      //     noactive_num: 24,
      //     noactive_rate: '91.67%',
      //     active_num: 6,
      //     active_rate: '8.33%',
      //     fptotal_flow: 12452,
      //     usetotal_flow: 545214,
      //     lefttotal_flow: 421414,
      //     useflow_rate: '41.24%'
      //   }, {
      //     id: 0,
      //     jg_name: '奇橙天下',
      //     sell_num: 140,
      //     noactive_num: 50,
      //     noactive_rate: '91.67%',
      //     active_num: 90,
      //     active_rate: '8.33%',
      //     fptotal_flow: 12452,
      //     usetotal_flow: 545214,
      //     lefttotal_flow: 421414,
      //     useflow_rate: '41.24%'
      //   }, {
      //     id: 0,
      //     jg_name: '自营 > 云智易联',
      //     sell_num: 110,
      //     noactive_num: 85,
      //     noactive_rate: '91.67%',
      //     active_num: 25,
      //     active_rate: '8.33%',
      //     fptotal_flow: 12452,
      //     usetotal_flow: 545214,
      //     lefttotal_flow: 421414,
      //     useflow_rate: '41.24%'
      //   }, {
      //     id: 0,
      //     jg_name: '自营 > 云智测试',
      //     sell_num: 47,
      //     noactive_num: 17,
      //     noactive_rate: '91.67%',
      //     active_num: 30,
      //     active_rate: '8.33%',
      //     fptotal_flow: 12452,
      //     usetotal_flow: 545214,
      //     lefttotal_flow: 421414,
      //     useflow_rate: '41.24%'
      //   }]
      //   this.list.total = this.list.data.length
      //   this.loadData = false
      //   this.getOptionData()
      // }, 1000)
    },
    // 获取图表数据
    getOptionData() {
      let option = this[`option_${this.tabIndex}`]
      let label = option.xAxis.data = [] // 底坐标标签
      let data1 = option.series[0].data = [] // 分类一数据
      let data2 = option.series[1].data = [] // 分类二数据
      this.list.data.forEach((v) => {
        label.push(v.jg_name)
        if (this.tabIndex === '0') {
          data1.push(v.active_num)
          data2.push(v.noactive_num)
          option.series[1].label.normal.formatter = function(series) {
            return `{b|${option.series[0].data[series.dataIndex]}}\n{a|${series.data}}`
          }
        } else if (this.tabIndex === '1') {
          data1.push(v.lefttotal_flow)
          data2.push(v.usetotal_flow)
          option.series[1].label.normal.formatter = function(series) {
            return `{b|${Api.UNITS.formatFlowUnit(option.series[0].data[1], 2, false)}}\n{a|${Api.UNITS.formatFlowUnit(series.data, 2, false)}}`
          }
        }
      })
      setTimeout(() => {
        this[`myChart_${this.tabIndex}`].setOption(option)
        this.echartLoadData = false
      }, 0)
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
        this[`myChart_${this.tabIndex}`].resize()
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
