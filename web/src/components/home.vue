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
          <el-date-picker class="date_picker" v-model="timePickData" type="daterange" align="right" unlink-panels range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" :picker-options="pickerOptions" @change="getData" value-format="yyyy-MM-dd" size="small" :clearable="false">
          </el-date-picker>
          <el-select class="stype_choice" v-model="stype" placeholder="请选择统计分组方式" @change="getData" size="small">
            <el-option label="按日统计" :value="0"></el-option>
            <el-option label="按月统计" :value="1"></el-option>
            <el-option label="按年统计" :value="2"></el-option>
          </el-select>
        </div>
        <el-row>
          <el-tabs @tab-click="changeTab_2" v-model="tabIndex_2">
            <el-tab-pane label="续费趋势" v-loading="bottomCardLoadData">
              <el-row :gutter="20">
                <el-col :span="15">
                  <div id="canvas_2_0"></div>
                </el-col>
                <el-col :span="8" class="rank-list">
                  <el-row class="title">机构续费排名</el-row>
                  <el-row v-if="bottomCardData_0.orgrank.length" v-for="(item, index) in bottomCardData_0.orgrank.slice(0, 8)" :key="index" class="rank-item" :gutter="5">
                    <el-col :class="'a_' + index" :span="3">{{index + 1}}</el-col>
                    <el-col :span="12">{{item.org_name}}</el-col>
                    <el-col :span="8" style="text-align: right">￥{{formatMoney(item.val)}}</el-col>
                  </el-row>
                  <el-row v-if="!bottomCardData_0.orgrank.length"><span style="color:#999">暂无数据</span></el-row>
                </el-col>
              </el-row>
            </el-tab-pane>
            <el-tab-pane label="订单趋势" v-loading="bottomCardLoadData">
              <el-row :gutter="20">
                <el-col :span="15">
                  <div id="canvas_2_1"></div>
                </el-col>
                <el-col :span="8" class="rank-list">
                  <el-row class="title">机构订单排名</el-row>
                  <el-row v-if="bottomCardData_1.orgrank.length" v-for="(item, index) in bottomCardData_1.orgrank.slice(0, 8)" :key="index" class="rank-item" :gutter="5">
                    <el-col :class="'a_' + index" :span="3">{{index + 1}}</el-col>
                    <el-col :span="12">{{item.org_name}}</el-col>
                    <el-col :span="8" style="text-align: right">{{item.val}} 笔</el-col>
                  </el-row>
                  <el-row v-if="!bottomCardData_1.orgrank.length"><span style="color:#999">暂无数据</span></el-row>
                </el-col>
              </el-row>
            </el-tab-pane>
            <el-tab-pane label="流量消耗趋势" v-loading="bottomCardLoadData">
              <el-row :gutter="20">
                <el-col :span="15">
                  <div id="canvas_2_2"></div>
                </el-col>
                <el-col :span="8" class="rank-list">
                  <el-row class="title">流量消耗排名</el-row>
                  <el-row v-if="bottomCardData_2.orgrank.length" v-for="(item, index) in bottomCardData_2.orgrank.slice(0, 8)" :key="index" class="rank-item" :gutter="5">
                    <el-col :class="'a_' + index" :span="3">{{index + 1}}</el-col>
                    <el-col :span="12">{{item.org_name}}</el-col>
                    <el-col :span="8" style="text-align: right">{{formatFlowUnit(item.val, 3, false)}}</el-col>
                  </el-row>
                  <el-row v-if="!bottomCardData_2.orgrank.length"><span style="color:#999">暂无数据</span></el-row>
                </el-col>
              </el-row>
            </el-tab-pane>
            <el-tab-pane label="客单价" v-loading="bottomCardLoadData">
              <el-row :gutter="20">
                <el-col :span="15">
                  <div id="canvas_2_3"></div>
                </el-col>
                <el-col :span="8" class="rank-list">
                  <el-row class="title">客单价排名</el-row>
                  <el-row v-if="bottomCardData_3.orgrank.length" v-for="(item, index) in bottomCardData_3.orgrank.slice(0, 8)" :key="index" class="rank-item" :gutter="5">
                    <el-col :class="'a_' + index" :span="3">{{index + 1}}</el-col>
                    <el-col :span="12">{{item.org_name}}</el-col>
                    <el-col :span="8" style="text-align: right">￥{{formatMoney(item.val)}}</el-col>
                  </el-row>
                  <el-row v-if="!bottomCardData_3.orgrank.length"><span style="color:#999">暂无数据</span></el-row>
                </el-col>
              </el-row>
            </el-tab-pane>
            <el-tab-pane label="SIM卡使用趋势" v-loading="bottomCardLoadData">
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
      bottomCardLoadData: true,
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
      bottomCardData_0: {
        orgrank: [],
        chart: []
      },
      bottomCardData_1: {
        orgrank: [],
        chart: []
      },
      bottomCardData_2: {
        orgrank: [],
        chart: []
      },
      bottomCardData_3: {
        orgrank: [],
        chart: []
      },
      bottomCardData_4: [],
      stype: 1,
      // 默认选择最近一年
      timePickData: [Api.UNITS.formatdate(new Date().getTime() - 3600000 * 24 * 365, 'yyyy-mm-dd'), Api.UNITS.formatdate(new Date(), 'yyyy-mm-dd')],
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now()
        },
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
        }, {
          text: '最近半年',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 183);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近一年',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 365);
            picker.$emit('pick', [start, end]);
          }
        }]
      },
      ajaxAd: {
        '0': 'getTopupTrend',
        '1': 'getOrderTrend',
        '2': 'getGprsTrend',
        '3': 'getPriceTrend',
        '4': 'getSimTrend',
      },
      option_1: {
        series: [{
          type: 'pie',
          radius: ['65%', '88%'],
          hoverAnimation: false,
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
            },
            emphasis: {

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
      this.getData()
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
    // pie-card
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
    // bottom-card数据
    getData() {
      this.bottomCardLoadData = true
      _axios.send({
        method: 'get',
        url: _axios.ajaxAd[this.ajaxAd[`${this.tabIndex_2}`]],
        params: {
          date_start: this.timePickData[0],
          date_end: this.timePickData[1],
          stype: this.stype
        },
        done: ((res) => {
          this.bottomCardLoadData = false
          this[`bottomCardData_${this.tabIndex_2}`] = res.data
          this[`showLine_${this.tabIndex_2}`]()
        })
      })
    },
    showPie() {
      this.option_1.series[0].data = this[`tableData_1_${this.tabIndex_1}`].map((v) => v.card_num)
      this.option_1.series[0].label.normal.formatter = this.formatterPie
      this.myChart_1.setOption(this.option_1)
    },
    showLine_0() {
      const _echart = new Api.ECHARTS({ dataViewTitle: '时间' })
      _echart.setOption({
        title: '续费趋势',
        legend: ['续费金额', '返利金额', '续费率'],
        xAxis: { data: this.bottomCardData_0.chart.map((v) => v.stats_time) },
        series: [{
          data: this.bottomCardData_0.chart.map((v) => v.val),
          type: 'bar',
          stack: '金额'
        }, {
          data: this.bottomCardData_0.chart.map((v) => v.val2),
          type: 'bar',
          stack: '金额'
        }, {
          data: this.bottomCardData_0.chart.map((v) => v.val3),
          yAxisIndex: 1
        }],
        formatter: this.formatter2
      })
      Vue.nextTick(() => {
        this.myChart_2.setOption(_echart.getOption(), true)
        $("[_echarts_instance_]").find(":last-child").trigger('click')
      })
    },
    showLine_1() {
      const _echart = new Api.ECHARTS({ dataViewTitle: '时间' })
      _echart.setOption({
        title: '订单趋势',
        legend: ['订单数'],
        xAxis: { data: this.bottomCardData_1.chart.map((v) => v.stats_time) },
        series: [{
          data: this.bottomCardData_1.chart.map((v) => v.val)
        }]
      })
      Vue.nextTick(() => {
        this.myChart_2.setOption(_echart.getOption(), true)
        $("[_echarts_instance_]").find(":last-child").trigger('click')
      })
    },
    showLine_2() {
      const _echart = new Api.ECHARTS({ dataViewTitle: '时间' })
      _echart.setOption({
        title: '流量消耗趋势',
        legend: ['流量消耗'],
        xAxis: { data: this.bottomCardData_2.chart.map((v) => v.stats_time) },
        series: [{
          data: this.bottomCardData_2.chart.map((v) => v.val)
        }],
        formatter: this.formatter
      })
      Vue.nextTick(() => {
        this.myChart_2.setOption(_echart.getOption(), true)
        $("[_echarts_instance_]").find(":last-child").trigger('click')
      })
    },
    showLine_3() {
      const _echart = new Api.ECHARTS({ dataViewTitle: '时间' })
      _echart.setOption({
        title: '客单价趋势',
        legend: ['客单价'],
        xAxis: { data: this.bottomCardData_3.chart.map((v) => v.stats_time) },
        series: [{
          data: this.bottomCardData_3.chart.map((v) => v.val)
        }],
        formatter: this.formatter1
      })
      Vue.nextTick(() => {
        this.myChart_2.setOption(_echart.getOption(), true)
        $("[_echarts_instance_]").find(":last-child").trigger('click')
      })
    },
    showLine_4() {
      const _echart = new Api.ECHARTS({ dataViewTitle: '时间' })
      _echart.setOption({
        title: 'SIM卡使用趋势',
        legend: ['SIM卡使用总数', '已激活', '未激活', '已停卡'],
        xAxis: { data: this.bottomCardData_4.map((v) => v.stats_time) },
        colorList: Api.UNITS.getColorList(['primary', 'success', 'warning', 'danger']),
        series: [{
          data: this.bottomCardData_4.map((v) => v.card_total)
        }, {
          data: this.bottomCardData_4.map((v) => v.active_total)
        }, {
          data: this.bottomCardData_4.map((v) => v.unactive_total)
        }, {
          data: this.bottomCardData_4.map((v) => v.stop_total)
        }]
      })
      Vue.nextTick(() => {
        this.myChart_2.setOption(_echart.getOption(), true)
        $("[_echarts_instance_]").find(":last-child").trigger('click')
      })
    },
    formatFlowUnit: Api.UNITS.formatFlowUnit,
    getColorList: Api.UNITS.getColorList,
    formatMoney: Api.UNITS.formatMoney,
    formatdate: Api.UNITS.formatdate,
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
      width: 260px;

      .el-range__close-icon {
        display: none;
      }
    }

    .stype_choice {
      position: absolute;
      width: 150px;
      right: 290px;
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

      // 数据视图dataView无法遮盖下面的cancas, 将top = 5px 手动修改为 4px
      div[style*="background-color: rgb(255, 255, 255)"] {
        top: 4px !important;
      }
    }

  }

}

</style>
