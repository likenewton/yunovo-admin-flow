<template>
  <div class="home-container">
    <el-row :gutter="20">
      <el-col :span="11" v-loading="colorCardLoadData">
        <el-card class="color_card" shadow="never" :body-style="{background: getColorList('success')}">
          <div class="card_item">
            <div class="card_title">今日订单数</div>
            <div class="card_value">{{formatMoney(colorCardData.tres.pay_count, 0)}}</div>
          </div>
          <div class="card_item">
            <div class="card_title">今日续费</div>
            <div class="card_value">￥{{formatMoney(colorCardData.tres.pay_money || 0)}}</div>
          </div>
          <div class="card_item">
            <div class="card_title">今日返利</div>
            <div class="card_value">￥{{formatMoney(colorCardData.tres.rebate_money || 0)}}</div>
          </div>
          <div class="card_item">
            <div class="card_title">今日激活卡数</div>
            <div class="card_value">{{formatMoney(colorCardData.tres.active_today, 0)}}</div>
          </div>
          <div class="card_item">
            <div class="card_title">今日停卡数</div>
            <div class="card_value">{{formatMoney(colorCardData.tres.stop_today, 0)}}</div>
          </div>
        </el-card>
        <el-card class="color_card" shadow="never" :body-style="{background: getColorList('primary')}">
          <div class="card_item">
            <div class="card_title">当月订单数</div>
            <div class="card_value">{{formatMoney(colorCardData.dy_pres.pay_count, 0)}}</div>
          </div>
          <div class="card_item">
            <div class="card_title">当月续费</div>
            <div class="card_value">￥{{formatMoney(colorCardData.dy_pres.pay_money)}}</div>
          </div>
          <div class="card_item">
            <div class="card_title">当月返利</div>
            <div class="card_value">￥{{formatMoney(colorCardData.dy_pres.rebate_money)}}</div>
          </div>
          <div class="card_item">
            <div class="card_title">当月激活卡数</div>
            <div class="card_value">{{formatMoney(colorCardData.tres.active_month, 0)}}</div>
          </div>
          <div class="card_item">
            <div class="card_title">当月停卡数</div>
            <div class="card_value">{{formatMoney(colorCardData.tres.stop_month, 0)}}</div>
          </div>
        </el-card>
        <el-card class="color_card" shadow="never" :body-style="{background: getColorList('warning')}" style="margin-bottom: 0">
          <div class="card_item">
            <div class="card_title">累计订单</div>
            <div class="card_value">{{formatMoney(colorCardData.pres.pay_count, 0)}}</div>
          </div>
          <div class="card_item">
            <div class="card_title">累计续费</div>
            <div class="card_value">￥{{formatMoney(colorCardData.pres.pay_money)}}</div>
          </div>
          <div class="card_item">
            <div class="card_title">累计返利</div>
            <div class="card_value">￥{{formatMoney(colorCardData.pres.rebate_money)}}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="13">
        <el-card class="pie-card" shadow="never" :style="{height: '336px'}" v-loading="pieCardLoadData">
          <div class="header">SIM卡统计</div>
          <el-row>
            <el-tabs @tab-click="changeTab_1" v-model="tabIndex_1">
              <el-tab-pane label="卡状态">
                <el-row>
                  <div class="pie_left">
                    <el-row>
                      <el-col>
                        <div id="canvas_1_0"></div>
                      </el-col>
                    </el-row>
                  </div>
                  <div class="pie_right">
                    <el-table class="table" :data="tableData_1_0" style="width: 100%" size="small" :header-cell-style="{background: '#f8f8f8', color: '#333', fontSize: '14px'}">
                      <el-table-column prop="date" label="卡状态" min-width="70" align="center">
                        <template slot-scope="scope">
                          <div>
                            <span class="table_circle" :style="{background: scope.row.color}"></span>
                            <span>{{scope.row.status}}</span>
                          </div>
                        </template>
                      </el-table-column>
                      <el-table-column prop="percent" label="占比" min-width="60" align="center"></el-table-column>
                      <el-table-column prop="card_num" label="卡数" min-width="60" align="center"></el-table-column>
                    </el-table>
                  </div>
                </el-row>
              </el-tab-pane>
              <el-tab-pane label="实名">
                <el-row>
                  <div class="pie_left">
                    <el-row>
                      <el-col>
                        <div id="canvas_1_1"></div>
                      </el-col>
                    </el-row>
                  </div>
                  <div class="pie_right">
                    <el-table class="table" :data="tableData_1_1" style="width: 100%" size="small" :header-cell-style="{background: '#f8f8f8', color: '#333', fontSize: '14px'}">
                      <el-table-column prop="date" label="实名状态" min-width="70" align="center">
                        <template slot-scope="scope">
                          <div>
                            <span class="table_circle" :style="{background: scope.row.color}"></span>
                            <span>{{scope.row.status}}</span>
                          </div>
                        </template>
                      </el-table-column>
                      <el-table-column prop="percent" label="占比%" min-width="60" align="center"></el-table-column>
                      <el-table-column prop="card_num" label="卡数" min-width="60" align="center"></el-table-column>
                    </el-table>
                  </div>
                </el-row>
              </el-tab-pane>
              <el-tab-pane label="激活">
                <el-row>
                  <div class="pie_left">
                    <el-row>
                      <el-col>
                        <div id="canvas_1_2"></div>
                      </el-col>
                    </el-row>
                  </div>
                  <div class="pie_right">
                    <el-table class="table" :data="tableData_1_2" style="width: 100%" size="small" :header-cell-style="{background: '#f8f8f8', color: '#333', fontSize: '14px'}">
                      <el-table-column prop="date" label="激活类型" min-width="75" align="center">
                        <template slot-scope="scope">
                          <div>
                            <span class="table_circle" :style="{background: scope.row.color}"></span>
                            <span>{{scope.row.status}}</span>
                          </div>
                        </template>
                      </el-table-column>
                      <el-table-column prop="percent" label="占比%" min-width="60" align="center"></el-table-column>
                      <el-table-column prop="card_num" label="卡数" min-width="60" align="center"></el-table-column>
                    </el-table>
                  </div>
                </el-row>
              </el-tab-pane>
            </el-tabs>
          </el-row>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="24">
        <el-card class="middle_card_left" shadow="never" v-loading="middleCardLoadData">
          <div class="header">数据总览</div>
          <el-row class="content" :gutter="8">
            <el-col :span="12">
              <el-card shadow="hover" style="border: none" class="pointer" @click.native="$router.push({name: 'cardauth'})">
                <div class="value">{{middleCardData.rlname_num}}</div>
                <div class="key">实名待审核数</div>
              </el-card>
            </el-col>
            <el-col :span="12">
              <el-card shadow="hover" style="border: none" class="pointer" @click.native="$router.push({name: 'cardcombo'})">
                <div class="value">{{middleCardData.pack_num}}</div>
                <div class="key">流量套餐总数</div>
              </el-card>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>
    <el-row style="margin-top: 20px">
      <el-card class="bottom-card" shadow="never" :style="{height: '450px'}">
        <div class="header">
          <span>平台经营趋势统计</span>
          <el-date-picker class="date_picker" v-model="bottomCardDate" type="daterange" align="right" unlink-panels range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" :picker-options="pickerOptions" size="small">
          </el-date-picker>
        </div>
        <el-row>
          <el-tabs @tab-click="changeTab_2" v-model="tabIndex_2">
            <el-tab-pane label="续费趋势">
              <el-row :gutter="20">
                <el-col :span="15">
                  <div id="canvas_2_0"></div>
                </el-col>
                <el-col :span="8" class="rank-list">
                  <el-row class="title">机构续费排名</el-row>
                  <el-row v-for="(item, index) in rankData_2_0" :key="index" class="rank-item" :gutter="5">
                    <el-col :class="'a_' + index" :span="3">{{index + 1}}</el-col>
                    <el-col :span="12">{{item.name}}</el-col>
                    <el-col :span="8">￥{{formatMoney(item.value)}}</el-col>
                  </el-row>
                </el-col>
              </el-row>
            </el-tab-pane>
            <el-tab-pane label="订单趋势">
              <el-row :gutter="20">
                <el-col>
                  <div id="canvas_2_1"></div>
                </el-col>
              </el-row>
            </el-tab-pane>
            <el-tab-pane label="流量消耗趋势">
              <el-row :gutter="20">
                <el-col>
                  <div id="canvas_2_2"></div>
                </el-col>
              </el-row>
            </el-tab-pane>
            <el-tab-pane label="客单价">
              <el-row :gutter="20">
                <el-col>
                  <div id="canvas_2_3"></div>
                </el-col>
              </el-row>
            </el-tab-pane>
            <el-tab-pane label="SIM卡使用趋势">
              <el-row :gutter="20">
                <el-col>
                  <div id="canvas_2_4"></div>
                </el-col>
              </el-row>
            </el-tab-pane>
          </el-tabs>
        </el-row>
      </el-card>
    </el-row>
  </div>
</template>
<script>
import Api from 'assets/js/api.js'
import { mapMutations, mapState } from 'vuex'

export default {
  data() {
    return {
      tabIndex_1: '0',
      tabIndex_2: '0',
      myChart_1: null,
      myChart_2: null,
      colorCardLoadData: true,
      pieCardLoadData: true,
      middleCardLoadData: true,
      colorCardData: {
        pres: {}, // 累计
        tres: {}, // 今日
        dy_pres: {}, // 当月
      },
      pieCardData: null, // pie原始诗句
      middleCardData: {
        rlname_num: 0,
        pack_num: 0
      },
      tableData_1_0: [{ // 字段名_第几个tab_tabIndex对应值
        color: Api.UNITS.getColorList('success'),
        status: '已激活',
        percent: '0.00%',
        card_num: 0
      }, {
        color: Api.UNITS.getColorList('warning'),
        status: '未激活',
        percent: '0.00%',
        card_num: 0
      }, {
        color: Api.UNITS.getColorList('danger'),
        status: '已停卡',
        percent: '0.00%',
        card_num: 0
      }],
      tableData_1_1: [],
      tableData_1_2: [],
      rankData_2_0: [{
        name: '云智易联-测试环境',
        value: 365225.35
      }],
      bottomCardDate: '',
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
            picker.$emit('pick', [start, end]);
          }
        }]
      },
      option_1: {
        series: [{
          type: 'pie',
          radius: ['65%', '88%'],
          avoidLabelOverlap: false,
          label: {
            normal: {
              show: true,
              position: 'center',
              formatter: '',
              rich: {
                a: {
                  color: '#909399',
                  fontSize: '14',
                  lineHeight: '30'
                },
                b: {
                  color: '#333',
                  fontSize: '20',
                  lineHeight: '30'
                }
              }
            }
          },
          itemStyle: {
            normal: {
              color(params) {
                return Api.UNITS.getColorList(['success', 'warning', 'danger'])[params.dataIndex]
              }
            }
          },
          data: [] // 需设置
        }]
      }
    }
  },
  mounted() {
    this.getColorCardData()
    this.getEl2pack()
    this.changeTab_1({ index: '0' })
    this.changeTab_2({ index: '0' })
  },
  methods: {
    changeTab_1(para) {
      this.myChart_1 = this.$echarts.init(document.getElementById(`canvas_1_${para.index}`))
      if (this.pieCardData) {
        this.showPie()
      } else {
        this.getSiminfo()
      }
    },
    changeTab_2(para) {
      setTimeout(() => {
        this.myChart_2 = this.$echarts.init(document.getElementById(`canvas_2_${para.index}`))
        this.myChart_2.resize()
      }, 0)
      this.getTab2Data()
    },
    // 数据总览
    getEl2pack() {
      _axios.send({
        method: 'get',
        url: _axios.ajaxAd.getEl2pack,
        done: ((res) => {
          this.middleCardLoadData = false
          this.middleCardData = res.data
        })
      })
    },
    getColorCardData() {
      _axios.send({
        method: 'get',
        url: _axios.ajaxAd.getPayCase,
        done: ((res) => {
          this.colorCardLoadData = false
          this.colorCardData = res.data
        })
      })
    },
    getSiminfo() {
      _axios.send({
        method: 'get',
        url: _axios.ajaxAd.getSiminfo,
        done: ((res) => {
          this.pieCardLoadData = false
          this.pieCardData = res.data
          this.tableData_1_0 = [{ // card_total
            color: Api.UNITS.getColorList('success'),
            status: '已激活',
            percent: this.toFixed(res.data.active_total - res.data.stop_total, res.data.card_total),
            card_num: res.data.active_total - res.data.stop_total
          }, {
            color: Api.UNITS.getColorList('warning'),
            status: '未激活',
            percent: this.toFixed(res.data.card_total - res.data.active_total, res.data.card_total),
            card_num: res.data.card_total - res.data.active_total
          }, {
            color: Api.UNITS.getColorList('danger'),
            status: '已停卡',
            percent: this.toFixed(res.data.stop_total, res.data.card_total),
            card_num: res.data.stop_total
          }]
          this.tableData_1_1 = [{
            color: Api.UNITS.getColorList('success'),
            status: '已实名',
            percent: this.toFixed(res.data.rlname_total, res.data.card_total),
            card_num: res.data.rlname_total
          }, {
            color: Api.UNITS.getColorList('warning'),
            status: '未实名',
            percent: this.toFixed(res.data.card_total - res.data.rlname_total, res.data.card_total),
            card_num: res.data.card_total - res.data.rlname_total
          }]
          this.tableData_1_2 = [{
            color: Api.UNITS.getColorList('success'),
            status: '云智设备激活',
            percent: this.toFixed(res.data.active_total - res.data.unicom_total, res.data.active_total),
            card_num: res.data.active_total - res.data.unicom_total
          }, {
            color: Api.UNITS.getColorList('warning'),
            status: '其它设备激活',
            percent: this.toFixed(res.data.unicom_total, res.data.active_total),
            card_num: res.data.unicom_total
          }]
          this.showPie()
        })
      })
    },
    getTab2Data() {
      let tabIndex = this.tabIndex_2
      if (tabIndex === '0') {
        const _echart = new Api.ECHARTS()
        _echart.setOption({
          title: '续费趋势',
          legend: ['续费金额', '返利金额', '续费率'],
          xAxis: { data: ['2019-05-20', '2019-05-21', '2019-05-22', '2019-05-23', '2019-05-24', '2019-05-25', '2019-05-26', '2019-05-27'] },
          series: [{
            data: [151, 210, 180, 210, 185, 175, 200, 175],
            type: 'bar',
            stack: '金额'
          }, {
            data: [100, 200, 128, 56, 121, 110, 95, 100],
            type: 'bar',
            stack: '金额'
          }, {
            data: [20.3, 50.62, 38.24, 66, 51.7, 30.3, 25, 19],
            yAxisIndex: 1
          }],
          formatter: this.formatter2
        })
        setTimeout(() => {
          this.myChart_2.setOption(_echart.getOption())
        }, 0)
      } else if (tabIndex === '1') {
        const _echart = new Api.ECHARTS()
        _echart.setOption({
          title: '订单趋势',
          legend: ['订单数'],
          xAxis: { data: ['2019-05-20', '2019-05-21', '2019-05-22', '2019-05-23', '2019-05-24'] },
          series: [{
            data: [21, 52, 54, 32, 80]
          }]
        })
        setTimeout(() => {
          this.myChart_2.setOption(_echart.getOption())
        }, 0)
      } else if (tabIndex === '2') {
        const _echart = new Api.ECHARTS()
        _echart.setOption({
          title: '流量消耗趋势',
          legend: ['流量消耗'],
          xAxis: { data: ['2019-05-20', '2019-05-21', '2019-05-22', '2019-05-23', '2019-05-24'] },
          series: [{
            data: [2154, 5214, 545, 3265, 8000]
          }],
          formatter: this.formatter
        })
        setTimeout(() => {
          this.myChart_2.setOption(_echart.getOption())
        }, 0)
      } else if (tabIndex === '3') {
        const _echart = new Api.ECHARTS()
        _echart.setOption({
          title: '客单价趋势',
          legend: ['客单价'],
          xAxis: { data: ['2019-05-20', '2019-05-21', '2019-05-22', '2019-05-23', '2019-05-24'] },
          series: [{
            data: [6545.6, 5214, 545, 12542.3, 8000]
          }],
          formatter: this.formatter1
        })
        setTimeout(() => {
          this.myChart_2.setOption(_echart.getOption())
        }, 0)
      } else if (tabIndex === '4') {
        const _echart = new Api.ECHARTS()
        _echart.setOption({
          title: 'SIM卡使用趋势',
          legend: ['SIM卡使用总数', '已激活', '未激活', '已停卡'],
          xAxis: { data: ['2019-05-20', '2019-05-21', '2019-05-22', '2019-05-23', '2019-05-24'] },
          colorList: Api.UNITS.getColorList(['primary', 'success', 'warning', 'danger']),
          series: [{
            data: [51, 110, 180, 210, 185]
          }, {
            data: [10, 20, 128, 56, 121]
          }, {
            data: [15, 40, 28, 136, 31]
          }, {
            data: [20, 50, 38, 66, 51]
          }]
        })
        setTimeout(() => {
          this.myChart_2.setOption(_echart.getOption())
        }, 0)
      }
    },
    showPie() {
      this.option_1.series[0].data = this[`tableData_1_${this.tabIndex_1}`].map((v) => v.card_num)
      this.option_1.series[0].label.normal.formatter = this.formatterPie
      this.myChart_1.setOption(this.option_1)
    },
    formatFlowUnit: Api.UNITS.formatFlowUnit,
    getColorList: Api.UNITS.getColorList,
    formatMoney: Api.UNITS.formatMoney,
    toFixed: Api.UNITS.toFixed,
    formatterDealFn(data) {
      let variable = {
        // 数字对应的是tabIndex_2中的值
        '2': `${Api.UNITS.formatFlowUnit(data.data, 2, false)}`,
        '3': `${Api.UNITS.formatMoney(data.data)}元`
      }
      return variable[this.tabIndex_2]
    },
    // 格式化tooltip显示
    formatter(series) {
      return `
      <div>
        <div>${series[0].axisValueLabel}</div>
        ${function a() {
          let str = ''
          series.forEach((v, i) => {
            str += `<div>
              <span style="display:inline-block;width:10px;height:10px;border-radius:50%;background:${v.color}"></span>
              <span>${v.seriesName}:</span>
              <span>${Api.UNITS.formatFlowUnit(v.data, 2, false)}</span>
            </div>`
          })
          return str
        }()}
      </div>`
    },
    formatter1(series) {
      return `
      <div>
        <div>${series[0].axisValueLabel}</div>
        ${function a() {
          let str = ''
          series.forEach((v, i) => {
            str += `<div>
              <span style="display:inline-block;width:10px;height:10px;border-radius:50%;background:${v.color}"></span>
              <span>${v.seriesName}:</span>
              <span>${Api.UNITS.formatMoney(v.data)}元</span>
            </div>`
          })
          return str
        }()}
      </div>`
    },
    formatter2(series) {
      return `
      <div>
        <div>${series[0].axisValueLabel}</div>
        ${function a() {
          let str = ''
          series.forEach((v, i) => {
            str += `<div>
              <span style="display:inline-block;width:10px;height:10px;border-radius:50%;background:${v.color}"></span>
              <span>${v.seriesName}:</span>
              <span>${i === 2 ? Api.UNITS.formatMoney(v.data) + '%' : Api.UNITS.formatMoney(v.data) + '元'}</span>
            </div>`
          })
          return str
        }()}
      </div>`
    },
    formatterPie(series) {
      if (this.tabIndex_1 === '0' || this.tabIndex_1 === '1') {
        if (series.dataIndex === 0) return `{a|SIM卡总数}\n{b|${this.formatMoney(this.pieCardData.card_total, 0)}}`
        else return ''
      } else {
        if (series.dataIndex === 0) return `{a|激活卡总数}\n{b|${this.formatMoney(this.pieCardData.active_total, 0)}}`
        else return ''
      }
    }
  },
  computed: {
    ...mapState({
      asideCollapse: 'asideCollapse'
    })
  },
  watch: {
    asideCollapse(val, oldVal) {
      setTimeout(() => {
        this.myChart_2.resize()
      }, 300)
    }
  }
}

</script>
<style lang="scss">
.home-container {
  .color_card {
    margin-bottom: 15px;

    * {
      color: #fff;
    }

    .el-card__body {
      display: flex;
      justify-content: space-around;
      height: 100px;
    }

    .card_item {
      display: inline-block;
      justify-content: space-around;
      flex: 1;
      text-align: center;

      .card_title {
        font-size: 14px;
        height: 30px;
        line-height: 30px;
      }

      .card_value {
        font-weight: bold;
        height: 30px;
        line-height: 30px;
      }
    }
  }

  .pie-card {

    .pie_left {
      padding-right: 10px;
      width: 210px;
      float: left;
    }

    .pie_right {
      display: block;
      overflow: hidden;
    }

    .table {
      margin-top: 28px;

      .table_circle {
        position: relative;
        display: inline-block;
        width: 10px;
        height: 10px;
        border-radius: 50%;
        background: #ccc;
        margin: 3px;
        top: -1px;
      }
    }

    #canvas_1_0,
    #canvas_1_1,
    #canvas_1_2 {
      width: 200px;
      height: 200px;
      margin-top: 15px;
    }
  }

  .middle_card_left {
    >.el-card__body {
      padding-bottom: 0;
    }

    .header {
      padding-bottom: 18px;
      border-bottom: 1px solid $borderColor;
    }

    .content {
      height: 140px;

      .el-col {
        height: 100%;
        text-align: center;

        .el-card {
          height: 100%;
        }

        .value {
          font-size: 24px;
          font-weight: bold;
          color: $primary;
          height: 40px;
          line-height: 40px;
          margin-top: 10px;
        }

        .key {
          font-size: 14px;
          height: 35px;
          line-height: 35px;
        }
      }
    }
  }

  .bottom-card {
    .date_picker {
      position: absolute;
      right: 20px;
      top: 35px;
      z-index: 10;
    }

    .rank-list {
      .title {
        font-size: 20px;
        margin: 15px 0;
        color: #333;
      }

      .rank-item {
        .el-col {
          padding: 8px 0;
          line-height: 20px;

          &:first-of-type {
            width: 20px;
            height: 20px;
            text-align: center;
            border-radius: 50%;
            background: $borderColor;
            line-height: 6px;
            font-size: 12px;
            margin: 8px 20px 0 0;
          }

          &.a_0 {
            background: #FFD700;
          }

          &.a_1 {
            background: #C0C0C0;
          }

          &.a_2 {
            background: #D2B48C;
          }
        }
      }
    }

    #canvas_2_0,
    #canvas_2_1,
    #canvas_2_2,
    #canvas_2_3,
    #canvas_2_4 {
      width: 100%;
      height: 340px;
      font-size: 14px;
    }

  }

}

</style>
