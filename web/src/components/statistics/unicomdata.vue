<template>
  <div>
    <el-card class="search-card" style="margin-bottom: 20px" shadow="never">
      <el-form :inline="true" :model="formInline" class="search-form" size="small">
        <el-form-item label="机构名称">
          <el-select v-model="formInline.org_id" filterable clearable placeholder="请选择机构">
            <el-option v-for="(item, index) in orgs" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="导卡日期">
          <el-date-picker v-model="formInline.date_start" :picker-options="startDatePicker" type="date" value-format="yyyy-MM-dd" placeholder="选择开始日期"></el-date-picker> -
          <el-date-picker v-model="formInline.date_end" :picker-options="endDatePicker" type="date" value-format="yyyy-MM-dd" placeholder="选择结束日期"></el-date-picker>
        </el-form-item>
        <el-form-item label="激活日期">
          <el-date-picker v-model="formInline.jstart" :picker-options="startDatePicker_2" type="date" value-format="yyyy-MM-dd" placeholder="选择开始日期"></el-date-picker> -
          <el-date-picker v-model="formInline.jend" :picker-options="endDatePicker_2" type="date" value-format="yyyy-MM-dd" placeholder="选择结束日期"></el-date-picker>
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
      <el-table ref="listTable" @sort-change="handleSortChange" :data="list.data" :max-height="maxTableHeight" border size="mini" resizable>
        <el-table-column fixed="left" prop="org_id" label="机构名称" min-width="180" sortable="custom">
          <template slot-scope="scope">
            <span v-if="scope.row.sums">{{scope.row.org_name}}</span>
            <span v-else class="btn-link" @click="$router.push({name: 'card', query: {org_id: scope.row.org_id}})">{{scope.row.org_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="card_count" label="售卡数量" min-width="95" sortable="custom"></el-table-column>
        <el-table-column prop="nonactivated" label="未激活数" min-width="95" sortable="custom"></el-table-column>
        <el-table-column prop="noactive_rate" label="未激活率" min-width="95">
          <template slot-scope="scope">
            <span>{{(scope.row.nonactivated/scope.row.card_count*100).toFixed(3)}}%</span>
          </template>
        </el-table-column>
        <el-table-column prop="activated" label="已激活数" min-width="95" sortable="custom"></el-table-column>
        <el-table-column prop="active_rate" label="已激活率" min-width="95">
          <template slot-scope="scope">
            <span>{{(scope.row.activated/scope.row.card_count*100).toFixed(3)}}%</span>
          </template>
        </el-table-column>
        <el-table-column prop="unicom_count" label="使用总流量" min-width="105" sortable="custom">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.unicom_count)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="month_count" label="当月使用流量" min-width="117" sortable="custom">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.month_count)"></div>
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="导出" width="140">
          <template slot-scope="scope">
            <el-button type="text" class="text_warning">已激活</el-button>
            <el-button type="text" class="text_warning">未激活</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="list.currentPage" :page-sizes="pageSizes" :page-size="list.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="list.total" class="clearfix">
      </el-pagination>
    </el-card>
    <el-card class="clearfix" shadow="never" v-loading="loadData">
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
      tabIndex: '0',
      // 列表数据
      list: {
        data: [],
        pagesize: Api.STATIC.pageSizes[0],
        currentPage: 1,
        total: 0,
      },
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
    getData() {
      Api.UNITS.getListData({
        vue: this,
        url: _axios.ajaxAd.getUnicomStat,
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
            unicom_count: Api.UNITS.pageSums(this.list.data, 'unicom_count'),
            month_count: Api.UNITS.pageSums(this.list.data, 'month_count')
          }, {
            sums: true,
            org_name: '总计',
            card_count: other.card_count,
            nonactivated: other.nonactivated,
            activated: other.activated,
            unicom_count: other.unicom_count,
            month_count: other.month_count
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
        }
      })
      setTimeout(() => {
        this[`myChart_${this.tabIndex}`].setOption(option)
      }, 0)
    }
  },
  computed: {
    // 起始时间约数
    startDatePicker() {
      return Api.UNITS.startDatePicker(this, this.formInline.date_end)
    },
    startDatePicker_2() {
      return Api.UNITS.startDatePicker(this, this.formInline.jend)
    },
    // 结束时间约数
    endDatePicker() {
      return Api.UNITS.endDatePicker(this, this.formInline.date_start)
    },
    endDatePicker_2() {
      return Api.UNITS.endDatePicker(this, this.formInline.jstart)
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
