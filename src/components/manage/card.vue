<template>
  <div>
    <el-card class="box-card" style="margin-bottom: 20px" shadow="never">
      <el-form :inline="true" :model="formInline" class="demo-form-inline" size="small">
        <el-form-item label="卡ICCID">
          <el-input v-model="formInline.card_iccid" placeholder="请输入"></el-input>
        </el-form-item>
        <el-form-item label="卡商名称">
          <el-select v-model="formInline.card_type" filterable placeholder="请选择">
            <el-option v-for="(item, index) in cardTypes" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="机构名称">
          <el-select v-model="formInline.org_id" filterable placeholder="请选择">
            <el-option v-for="(item, index) in orgs" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="激活状态">
          <el-select v-model="formInline.active_status" placeholder="请选择">
            <el-option label="已激活" value="0"></el-option>
            <el-option label="未激活" value="1"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="过期时间">
          <el-select v-model="formInline.exceed_time" placeholder="请选择">
            <el-option label="已过期" value="0"></el-option>
            <el-option label="未过期" value="1"></el-option>
          </el-select>
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
    <el-card class="box-card clearfix" shadow="never" v-loading="loadData">
      <el-button-group style="margin-bottom: 10px">
        <el-button size="mini" type="primary" @click="showEcharts">图表</el-button>
        <el-button size="mini" type="warning">导入</el-button>
        <el-button size="mini" type="warning">导出</el-button>
      </el-button-group>
      <el-table ref="multipleTable" :data="list.data" @sort-change="handleSortChange" border size="mini">
        <el-table-column fixed="left" label="卡ICCID" min-width="170">
          <template slot-scope="scope">
            <el-button type="text">{{scope.row.iccid}}</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="ks_name" label="卡商名称" min-width="120"></el-table-column>
        <el-table-column prop="jg_name" label="机构名称" min-width="120"></el-table-column>
        <el-table-column label="当月用量" min-width="93">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.m_use)"></div>
          </template>
        </el-table-column>
        <el-table-column label="累计用量" min-width="93">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.total_use)"></div>
          </template>
        </el-table-column>
        <el-table-column label="剩余用量" min-width="93">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.left_use)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="excard_time" label="导卡时间" min-width="151" sortable="custom"></el-table-column>
        <el-table-column prop="active_time" label="激活时间" min-width="151" sortable="custom"></el-table-column>
        <el-table-column prop="eq_time" label="设备更新时间" min-width="151" sortable="custom"></el-table-column>
        <el-table-column label="过期时间" min-width="215">
          <template slot-scope="scope">
            <div v-html="calcLeftTime(scope.row.exceed_time)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="op_status" label="运行状态" min-width="70"></el-table-column>
        <el-table-column prop="active_status" label="激活状态" min-width="70"></el-table-column>
        <el-table-column fixed="right" label="操作" min-width="140">
          <template slot-scope="scope">
            <el-button type="text" @click="showDetail(scope)">同步</el-button>
            <el-button type="text">停用</el-button>
            <el-button type="text">套餐</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="list.currentPage" :page-sizes="pageSizes" :page-size="list.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="list.total" class="clearfix">
      </el-pagination>
    </el-card>
    <v-dialog :dialogPara="dialogPara"></v-dialog>
    <el-dialog title="机构流量ICCID卡统计图表" :visible.sync="dialogChartVisible">
      <div slot class="clearfix" v-loading="dialogChartLoadData">
        <div id="myChart" style="width:100%; height:300px"></div>
        <el-pagination @size-change="handleSizeChangeDetail" @current-change="handleCurrentChangeDetail" :current-page="currentPage" :page-sizes="pageSizes" :page-size="dialogList.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="dialogList.total" class="clearfix">
        </el-pagination>
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
      loadData: true,
      dialogChartLoadData: true,
      pageSizes: Api.STATIC.pageSizes,
      list: {
        data: [],
        pagesize: Api.STATIC.pageSizes[1],
        currentPage: 1,
        total: 0,
      },
      sort: {},
      formInline: {},
      // 要展开的对话框的参数
      dialogPara: {},
      dialogList: {
        loadData: true,
        data: [],
        pagesize: Api.STATIC.pageSizes[0],
        currentPage: 1,
        total: 0,
      },
      dialogChartVisible: false,
      // 图表实例
      myChart: null,
      options: {
        xAxis: {
          type: 'category',
          data: ['自营', '凯龙丁威特', '博毅', '天之眼', '湖南纽曼', '奇橙天下', '自营>云智易联', '自营', '凯龙丁威特', '博毅', '天之眼', '湖南纽曼', '奇橙天下', '自营>云智易联', '自营', '凯龙丁威特', '博毅', '天之眼', '湖南纽曼', '奇橙天下', '自营>云智易联'] // 1
        },
        yAxis: {
          type: 'value',
          splitLine: { show: false }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          },
        },
        toolbox: _echart.getOption().toolbox,
        series: [{
          name: '机构流量ICCID卡',
          data: [120, 200, 150, 80, 70, 110, 130, 120, 200, 150, 80, 70, 110, 130, 120, 200, 150, 80, 70, 110, 130], // 2
          type: 'bar',
          itemStyle: {
            //通常情况下：
            normal: {
              label: {
                show: true,
                position: 'top'
              },
              //每个柱子的颜色即为colorList数组里的每一项，如果柱子数目多于colorList的长度，则柱子颜色循环使用该数组
              color(params) {
                return Api.UNITS.getColorList([], 40)[params.dataIndex]
              }
            },
            //鼠标悬停时：
            emphasis: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          },
        }]
      }
    }
  },
  mounted() {
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
    handleSortChange(val) {
      Api.UNITS.setSortSearch(val, this)
      this.getData()
    },
    // dialog中的列表(图表)
    handleSizeChangeDetail(val) {
      this.dialogList.pagesize = val
      this.getEchartsData()
    },
    handleCurrentChangeDetail(val) {
      this.dialogList.currentPage = val
      this.getEchartsData()
    },
    showEcharts() {
      this.dialogChartVisible = true
      setTimeout(() => {
        // myChart因为是要放在dialog中所以必须要等到dialog展示之后才能展示
        this.myChart = this.$echarts.init(document.getElementById('myChart'))
        this.getEchartsData()
      }, 0)
    },
    // 展示dialog iccid的详细
    showDetail(scope) {
      this.dialogPara = {
        loadDialog: true,
        isShowCancelBtn: false,
        title: `详细信息(${scope.row.iccid})`,
        content: '<div style="height: 100px"></div>'
      }
      this.SET_DIALOGVISIBLE({ dialogVisible: true })
      this.getIccidDetailData(scope)
    },
    // 获取列表数据
    getData() {
      // Api.UNITS.getListData({
      //   vue: this,
      //   url: _axios.ajaxAd.getStats
      // })
      setTimeout(() => {
        // 数据请求成功
        this.list.data = [{
          id: 0,
          iccid: '8986011670901045280',
          ks_name: '智网吉林11位卡',
          jg_name: '卡仕特-西格玛',
          m_use: 564,
          total_use: 6462,
          left_use: NaN,
          excard_time: '2019-04-15 12:54:14',
          active_time: '2019-04-15 12:54:14',
          eq_time: '2019-04-15 12:54:14',
          exceed_time: '2020-04-15 12:54:14',
          op_status: 1,
          active_status: 0
        }]
        this.list.total = this.list.data.length
        this.loadData = false
      }, 1000)
    },
    // 获取图表数据
    getEchartsData() {
      this.dialogChartLoadData = true
      setTimeout(() => {
        // this.dialogPara.loadDialog = false
        // 这里拿到数据后要将数据保存到options中在调用setOption
        // this.options.xAxis.data.push()
        // this.options.series.data.push()
        this.myChart.setOption(this.options)
        this.dialogChartLoadData = false
      }, 1000)
    },
    // 获取iccid详细信息
    getIccidDetailData(scope) {
      setTimeout(() => {
        this.dialogPara.loadDialog = false
        this.dialogPara.content = `<div class="iccid_detail">
          <div class="item">
            <span class="fbs-left">MSISDN</span>
            <span class="fbs-right">861064604868138</span>
          </div>
          <div class="item">
            <span class="fbs-left">IMSI</span>
            <span class="fbs-right">460064520075138</span>
          </div>
          <div class="item">
            <span class="fbs-left">上次充值时间</span>
            <span class="fbs-right">2019-04-15 09:20:20</span>
          </div>
          <div class="item">
            <span class="fbs-left">设备更新时间</span>
            <span class="fbs-right">2019-04-15 09:20:20</span>
          </div>
          <div class="item">
            <span class="fbs-left">联通流量卡状态</span>
            <span class="fbs-right">已启用</span>
          </div>
          <div class="item">
            <span class="fbs-left">联通当月使用流量</span>
            <span class="fbs-right">${this.formatFlowUnit(565.3)}</span>
          </div>
          <div class="item">
            <span class="fbs-left">联通累计使用流量</span>
            <span class="fbs-right">${this.formatFlowUnit(2565.3)}</span>
          </div>
        </div>`
      }, 1000)
    },
    formatFlowUnit: Api.UNITS.formatFlowUnit,
    calcLeftTime: Api.UNITS.calcLeftTime
  },
  computed: {
    ...mapState({
      dialogVisible: 'dialogVisible',
      orgs: 'orgs',
      cardTypes: 'cardTypes'
    })
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

.iccid_detail {
  .item {
    height: 30px;
    line-height: 30px;

    >span {
      padding: 0 10px;
      text-align: center;
    }

    .fbs-left {
      width: 50%;
    }
  }
}

</style>
