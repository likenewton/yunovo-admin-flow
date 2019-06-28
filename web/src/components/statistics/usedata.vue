<template>
  <div>
    <el-card class="search-card" style="margin-bottom: 20px" shadow="never">
      <el-form class="search-form" :inline="true" :model="formInline" size="small">
        <el-form-item label="机构名称">
          <el-select v-model="formInline.org_id" filterable clearable placeholder="请选择机构">
            <el-option v-for="(item, index) in orgs" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="激活日期">
          <el-date-picker v-model="formInline.date_start" :picker-options="startDatePicker" type="date" value-format="yyyy-MM-dd" placeholder="选择开始日期"></el-date-picker> -
          <el-date-picker v-model="formInline.date_end" :picker-options="endDatePicker" type="date" value-format="yyyy-MM-dd" placeholder="选择结束日期"></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchData">查询</el-button>
          <el-button type="warning" @click="resetData">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="clearfix" style="margin-bottom: 20px" shadow="never" v-loading="loadData">
      <el-button-group style="margin-bottom: 10px">
        <el-button size="small" type="warning">导出</el-button>
      </el-button-group>
      <el-table ref="listTable" :data="list.data" @sort-change="handleSortChange" :max-height="maxTableHeight" border resizable size="mini">
        <el-table-column prop="org_id" label="机构名称" min-width="200" sortable="custom">
          <template slot-scope="scope">
            <span v-if="scope.row.sums">{{scope.row.org_name}}</span>
            <span v-else class="btn-link" @click="$router.push({name: 'card', query: {org_id: scope.row.org_id}})">{{scope.row.org_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="card_count" label="售卡数量" min-width="100" sortable="custom" align="right"></el-table-column>
        <el-table-column prop="nonactivated" label="未激活数" min-width="100" sortable="custom" align="right"></el-table-column>
        <el-table-column label="未激活率" min-width="100" align="right">
          <template slot-scope="scope">
            <span>{{(scope.row.nonactivated/scope.row.card_count*100).toFixed(3)}}%</span>
          </template>
        </el-table-column>
        <el-table-column prop="activated" label="已激活数" min-width="100" sortable="custom" align="right"></el-table-column>
        <el-table-column label="已激活率" min-width="100" align="right">
          <template slot-scope="scope">
            <span>{{(scope.row.activated/scope.row.card_count*100).toFixed(3)}}%</span>
          </template>
        </el-table-column>
        <el-table-column prop="pay_total" label="分配总流量" min-width="100" align="right">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.used_count + scope.row.unused_count)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="used_count" label="使用流量" min-width="100" sortable="custom" align="right">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.used_count)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="unused_count" label="剩余流量" min-width="100" sortable="custom" align="right">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.unused_count)"></div>
          </template>
        </el-table-column>
        <el-table-column label="使用流量率" min-width="100" align="right">
          <template slot-scope="scope">
            <span>{{(scope.row.used_count/(scope.row.used_count + scope.row.unused_count)*100).toFixed(3)}}%</span>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="list.currentPage" :page-sizes="pageSizes" :page-size="list.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="list.total" class="clearfix">
      </el-pagination>
    </el-card>
    <el-card class="clearfix" shadow="never" v-loading="echartLoadData">
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
      echartLoadData: true,
      tabIndex: '0',
      list: {
        data: [],
        pagesize: Api.STATIC.pageSizes[0],
        currentPage: 1,
        total: 0,
      },
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
    changeTab(para) {
      this.tabIndex = para.index
      setTimeout(() => {
        this[`myChart_${this.tabIndex}`] = this.$echarts.init(document.getElementById(`myChart_${this.tabIndex}`))
        this[`myChart_${this.tabIndex}`].resize()
      }, 0)
      this.getOptionData()
    },
    getData() {
      Api.UNITS.getListData({
        vue: this,
        url: _axios.ajaxAd.getCardUsed,
        cb: (res) => {
          let other = res.data.other || {}
          this.getOptionData()
          if (this.list.data.length === 0) return
          this.list.data.push(...[{
            sums: true,
            org_name: '小计',
            card_count: Api.UNITS.pageSums(this.list.data, 'card_count'),
            nonactivated: Api.UNITS.pageSums(this.list.data, 'nonactivated'),
            activated: Api.UNITS.pageSums(this.list.data, 'activated'),
            used_count: Api.UNITS.pageSums(this.list.data, 'used_count'),
            unused_count: Api.UNITS.pageSums(this.list.data, 'unused_count')
          }, {
            sums: true,
            org_name: '总计',
            card_count: other.card_count,
            nonactivated: other.nonactivated,
            activated: other.activated,
            used_count: other.used_count,
            unused_count: other.unused_count
          }])
        }
      })
    },
    // 获取图表数据
    getOptionData() {
      let option = this[`option_${this.tabIndex}`]
      let label = option.xAxis.data = [] // 底坐标标签
      let data1 = option.series[0].data = [] // 分类一数据
      let data2 = option.series[1].data = [] // 分类二数据
      this.list.data.forEach((v) => {
        if (this.tabIndex === '0' && !v.sums) {
          label.push(v.org_name)
          data1.push(v.activated)
          data2.push(v.nonactivated)
          option.series[1].label.normal.formatter = function(series) {
            return `{b|${option.series[0].data[series.dataIndex]}}\n{a|${series.data}}`
          }
        } else if (this.tabIndex === '1' && !v.sums) {
          label.push(v.org_name)
          data1.push(v.unused_count)
          data2.push(v.used_count)
          option.series[1].label.normal.formatter = function(series) {
            return `{b|${Api.UNITS.formatFlowUnit(option.series[0].data[series.dataIndex], 2, false)}}\n{a|${Api.UNITS.formatFlowUnit(series.data, 2, false)}}`
          }
        }
      })
      setTimeout(() => {
        this[`myChart_${this.tabIndex}`].setOption(option)
        this.echartLoadData = false
      }, 0)
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
