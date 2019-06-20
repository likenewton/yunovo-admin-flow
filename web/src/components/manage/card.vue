<template>
  <div class="card">
    <el-card class="box-card search-card" style="margin-bottom: 20px" shadow="never">
      <el-form :inline="true" :model="formInline" class="search-form" size="small">
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
          <el-select v-model="formInline.status" filterable clearable placeholder="请选择">
            <el-option v-for="(item, index) in activeSelect" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否过期">
          <el-select v-model="formInline.time_expire" filterable clearable placeholder="请选择">
            <el-option v-for="(item, index) in exceedSelect" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="运行状态">
          <el-select v-model="formInline.unicom_stop" filterable clearable placeholder="请选择">
            <el-option label="正常运行" value="0"></el-option>
            <el-option label="已停卡" value="1"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="激活日期">
          <el-date-picker v-model="formInline.date_start" type="date" value-format="yyyy-MM-dd" placeholder="选择开始日期"></el-date-picker> -
          <el-date-picker v-model="formInline.date_end" type="date" value-format="yyyy-MM-dd" placeholder="选择结束日期"></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchData">查询</el-button>
          <el-button type="warning" @click="resetData">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="clearfix" shadow="never" v-loading="loadData">
      <el-button-group style="margin-bottom: 10px">
        <el-button size="small" type="primary" @click="showEcharts">图表</el-button>
        <el-button size="small" type="warning" @click="$router.push({name: 'cardbatch'})">导入</el-button>
        <el-button size="small" type="warning">导出</el-button>
      </el-button-group>
      <el-table ref="listTable" :data="list.data" @sort-change="handleSortChange" :max-height="maxTableHeight" border resizable size="mini">
        <el-table-column prop="card_iccid" fixed="left" label="卡ICCID" width="182" sortable="custom">
          <template slot-scope="scope">
            <span class="btn-link" @click="$router.push({ name: 'rechargeDetail', query: {card_id: scope.row.card_id}})">{{scope.row.card_iccid}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="card_type" label="卡商名称" min-width="120" sortable="custom">
          <template slot-scope="scope">
            <span>{{scope.row.card_type_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="org_id" label="机构名称" min-width="135" sortable="custom">
          <template slot-scope="scope">
            <span>{{scope.row.org_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="unicom_month" label="当月用量" width="105" sortable="custom">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.unicom_month)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="unicom_total" label="累计用量" width="105" sortable="custom">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.unicom_total)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="unicom_unused" label="剩余用量" width="105" sortable="custom">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.unicom_unused)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="time_added" label="导卡时间" min-width="155" sortable="custom"></el-table-column>
        <el-table-column prop="time_active" label="激活时间" min-width="155" sortable="custom"></el-table-column>
        <el-table-column prop="time_last" label="设备更新时间" min-width="155" sortable="custom"></el-table-column>
        <el-table-column prop="time_expire" label="过期时间" min-width="220" sortable="custom">
          <template slot-scope="scope">
            <div v-html="calcLeftTime(scope.row.time_expire)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="unicom_stop" label="运行状态" width="75">
          <template slot-scope="scope">
            <span class="text_success bold" v-if="scope.row.unicom_stop==0">正常运行</span>
            <span class="text_danger bold" v-else>已停卡</span>
          </template>
        </el-table-column>
        <el-table-column label="激活状态" width="75">
          <template slot-scope="scope">
            <span v-if="scope.row.time_active">已激活</span>
            <span v-else>未激活</span>
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="145">
          <template slot-scope="scope">
            <el-button type="text" @click="showDetail(scope)">同步</el-button>
            <el-button type="text" @click="toUnicomLink(scope.row.card_iccid)">套餐</el-button>
            <el-button type="text" class="text_danger" v-if="scope.row.unicom_stop==0">停用</el-button>
            <el-button type="text" class="text_success" v-else>启用</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="list.currentPage" :page-sizes="pageSizes" :page-size="list.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="list.total" class="clearfix">
      </el-pagination>
    </el-card>
    <v-dialog :dialogPara="dialogPara"></v-dialog>
    <el-dialog title="机构流量ICCID卡统计" :visible.sync="dialogChartVisible" width="70%" center>
      <div slot class="clearfix" v-loading="dialogChartLoadData">
        <div id="myChart" style="width:100%; height:300px"></div>
        <el-pagination @size-change="handleSizeChangeDetail" @current-change="handleCurrentChangeDetail" :current-page="dialogList.currentPage" :page-sizes="pageSizes" :page-size="dialogList.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="dialogList.total" class="clearfix">
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
      formInline: {
        card_id: Api.UNITS.getQuery('card_id'),
        org_id: Api.UNITS.getQuery('org_id')
      },
      maxTableHeight: Api.UNITS.maxTableHeight(),
      // 要展开的对话框的参数
      dialogPara: {},
      dialogList: {
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
          data: [] // 1
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
          data: [], // 2
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
                return Api.UNITS.getColorList([], 60)[params.dataIndex]
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
      this.list.currentPage = 1
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
    // dialog(图表)中的分页
    handleSizeChangeDetail(val) {
      this.dialogList.pagesize = val
      this.dialogList.currentPage = 1
      this.getEchartsData()
    },
    handleCurrentChangeDetail(val) {
      this.dialogList.currentPage = val
      this.getEchartsData()
    },
    // 重置列表
    resetData() {
      this.list.currentPage = 1
      this.formInline = {
        card_id: Api.UNITS.getQuery('card_id'),
        org_id: Api.UNITS.getQuery('org_id')
      } // 1、重置查询表单
      this.sort = {} // 2、重置排序
      this.$refs.listTable.clearSort() // 3、清空排序样式
      this.getData()
    },
    searchData() {
      this.list.currentPage = 1
      this.getData()
    },
    // 获取列表数据
    getData() {
      Api.UNITS.getListData({
        vue: this,
        url: _axios.ajaxAd.getCards
      })
    },
    showEcharts() {
      this.dialogChartVisible = true
      Vue.nextTick(() => {
        // myChart因为是要放在dialog中所以必须要等到dialog展示之后才能展示
        this.myChart = this.$echarts.init(document.getElementById('myChart'))
        this.getEchartsData()
      })
    },
    // 获取图表数据
    getEchartsData() {
      Api.UNITS.getListData({
        vue: this,
        url: _axios.ajaxAd.getCardBar,
        list: 'dialogList',
        loadData: 'dialogChartLoadData',
        cb: (res) => {
          let labals = this.options.xAxis.data = []
          let data = this.options.series[0].data = []
          let dialogListData = this.dialogList.data || []
          dialogListData.forEach((v) => {
            labals.push(v.org_name)
            data.push(v.total)
          })
          this.myChart.setOption(this.options)
        }
      })
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
    calcLeftTime: Api.UNITS.calcLeftTime,
    toUnicomLink: Api.UNITS.toUnicomLink
  },
  computed: {
    ...mapState({
      dialogVisible: 'dialogVisible',
      orgs: 'orgs',
      cardTypes: 'cardTypes',
      exceedSelect: 'exceedSelect',
      activeSelect: 'activeSelect'
    })
  }
}

</script>
<style lang="scss">
.card {
  .el-pagination {
    float: right;
    margin: 25px 40px 0 0;
  }

  .el-table {

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
}

</style>
