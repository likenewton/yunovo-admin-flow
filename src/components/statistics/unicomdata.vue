<template>
  <div>
    <el-card class="box-card" style="margin-bottom: 20px" shadow="never">
      <el-form :inline="true" :model="formInline" class="demo-form-inline" size="small">
        <el-form-item label="机构名称">
          <el-select v-model="formInline.org_id" filterable placeholder="请选择">
            <el-option v-for="(item, index) in orgs" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="导卡时间">
          <el-date-picker v-model="formInline.import_time" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="激活时间">
          <el-date-picker v-model="formInline.active_time" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
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
      <el-table ref="multipleTable" @sort-change="handleSortChange" :data="list.data" border size="mini">
        <el-table-column fixed="left" label="机构名称" min-width="125">
          <template slot-scope="scope">
            <el-button type="text">{{scope.row.jg_name}}</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="sell_num" label="售卡数量" min-width="93" sortable="custom"></el-table-column>
        <el-table-column prop="noactive_num" label="未激活数" min-width="93" sortable="custom"></el-table-column>
        <el-table-column prop="noactive_rate" label="未激活率" min-width="93" sortable="custom"></el-table-column>
        <el-table-column prop="active_num" label="已激活数" min-width="93" sortable="custom"></el-table-column>
        <el-table-column prop="active_rate" label="已激活率" min-width="93" sortable="custom"></el-table-column>
        <el-table-column label="使用总流量" min-width="93">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.usetotal_flow)"></div>
          </template>
        </el-table-column>
        <el-table-column label="当月使用流量" min-width="93">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.usemonth_flow)"></div>
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="导出明细" width="125">
          <template slot-scope="scope">
            <el-button type="text">已激活</el-button>
            <el-button type="text">未激活</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentPage" :page-sizes="pageSizes" :page-size="list.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="list.total" class="clearfix">
      </el-pagination>
    </el-card>
    <el-card class="box-card clearfix" shadow="never" v-loading="echartLoadData">
      <div id="myChart_0" style="width:100%; height:380px"></div>
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
      // 列表数据
      list: {
        data: [],
        pagesize: Api.STATIC.pageSizes[0],
        currentPage: 1,
        total: 0,
      },
      sort: {},
      formInline: {},
      myChart_0: null,
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
                color: '#ff7477',
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
                    color: '#ff7477',
                    align: 'center'
                  }
                }
              }
            },
            data: [], //要设置的
            itemStyle: {
              normal: {
                color: '#3cb1ff'
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
      //     usetotal_flow: 52145631,
      //     usemonth_flow: 995421
      //   }, {
      //     id: 1,
      //     jg_name: '奇橙天下',
      //     sell_num: 25,
      //     noactive_num: 10,
      //     noactive_rate: '91.67%',
      //     active_num: 15,
      //     active_rate: '8.33%',
      //     usetotal_flow: 52145631,
      //     usemonth_flow: 995421
      //   }]
      //   this.list.total = this.list.data.length
      //   this.loadData = false
      //   // 数据拿到了就可以格式化图表数据了
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
