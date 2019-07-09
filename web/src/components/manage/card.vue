<template>
  <div class="card">
    <el-card class="clearfix" shadow="never" v-loading="loadData">
      <el-button-group style="margin-bottom: 10px">
        <el-button size="small" type="primary" @click="showEcharts" :disabled="!pageAuthBtn.FCP_01_001_ECHART01">图表</el-button>
        <el-button size="small" type="warning" @click="$router.push({name: 'cardbatch'})" :disabled="!pageAuthBtn.FCP_01_001_LINK2">导入</el-button>
        <el-button size="small" type="warning" :disabled="!pageAuthBtn.FCP_01_001_EXPORT01" @click="exportExcel">导出</el-button>
      </el-button-group>
      <el-form :inline="true" :model="formInline" class="search-form" size="small" @submit.native.prevent>
        <el-form-item>
          <el-input v-model="formInline.card_iccid" @input="formInline.card_iccid = limitNumber(formInline.card_iccid, 20)" @keyup.enter.native="simpleSearchData" placeholder="卡ICCID"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="simpleSearchData" :disabled="!pageAuthBtn.FCP_01_001_CHECK01">查询</el-button>
          <el-button type="primary" @click="searchVipVisible = true" :disabled="!pageAuthBtn.FCP_01_001_CHECK01">高级查询</el-button>
        </el-form-item>
      </el-form>
      <el-table ref="listTable" :data="list.data" @sort-change="handleSortChange" :max-height="maxTableHeight" border size="mini">
        <el-table-column prop="card_iccid" fixed="left" label="卡ICCID" width="175" sortable="custom">
          <template slot-scope="scope">
            <span v-if="pageAuthBtn.FCP_01_001_LINK1" class="btn-link" @click="$router.push({ name: 'rechargeDetail', query: {card_id: scope.row.card_id}})">{{scope.row.card_iccid}}</span>
            <span v-else>{{scope.row.card_iccid}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="card_type" label="卡商名称" width="135" sortable="custom">
          <template slot-scope="scope">
            <span>{{scope.row.card_type_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="org_id" label="机构名称" min-width="130" sortable="custom">
          <template slot-scope="scope">
            <span>{{scope.row.org_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="unicom_month" label="当月用量" width="97" sortable="custom" align="right">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.unicom_month)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="unicom_total" label="累计用量" width="97" sortable="custom" align="right">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.unicom_total)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="unicom_unused" label="剩余用量" width="100" sortable="custom" align="right">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.unicom_unused)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="time_added" label="导卡时间" width="153" sortable="custom"></el-table-column>
        <el-table-column prop="time_active" label="激活时间" width="153" sortable="custom"></el-table-column>
        <el-table-column prop="time_last" label="设备更新时间" width="153" sortable="custom"></el-table-column>
        <el-table-column prop="time_expire" label="过期时间" min-width="153" sortable="custom">
          <template slot-scope="scope">
            <div v-html="calcLeftTime(scope.row.time_expire)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="unicom_stop" label="运行状态" width="78">
          <template slot-scope="scope">
            <span class="text_success bold" v-if="scope.row.unicom_stop==0">正常运行</span>
            <span class="text_danger bold" v-else>已停卡</span>
          </template>
        </el-table-column>
        <el-table-column label="激活状态" width="70">
          <template slot-scope="scope">
            <span v-if="scope.row.time_active">已激活</span>
            <span v-else>未激活</span>
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="125">
          <template slot-scope="scope">
            <el-button type="text" @click="$refs.syncUniconData.getSyncUnicomData(scope)" v-if="pageAuthBtn.FCP_01_001_OP01">同步</el-button>
            <el-button type="text" @click="toUnicomLink(scope.row.card_iccid)">套餐</el-button>
            <el-button type="text" class="text_danger" v-if="scope.row.unicom_stop==0 && pageAuthBtn.FCP_01_001_OP02" @click="checkCardStop(scope, 1)">停用</el-button>
            <el-button type="text" class="text_success" v-else-if="scope.row.unicom_stop==1 && pageAuthBtn.FCP_01_001_OP02" @click="checkCardStop(scope, 0)">启用</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="list.currentPage" :page-sizes="pageSizes" :page-size="list.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="list.total" class="clearfix">
      </el-pagination>
    </el-card>
    <el-dialog title="机构流量ICCID卡统计" :visible.sync="dialogChartVisible" width="70%" center :close-on-click-modal="false">
      <div slot class="clearfix" v-loading="dialogChartLoadData">
        <div id="myChart" style="width:100%" :style="{height: winHeight / 2.2 + 'px'}"></div>
        <el-pagination @size-change="handleSizeChangeDetail" @current-change="handleCurrentChangeDetail" :current-page="dialogList.currentPage" :page-sizes="pageSizes" :page-size="dialogList.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="dialogList.total" class="clearfix">
        </el-pagination>
      </div>
    </el-dialog>
    <v-sync-unicom-data ref="syncUniconData"></v-sync-unicom-data>
    <el-dialog title="高级查询" :visible.sync="searchVipVisible" width="700px" :close-on-click-modal="false">
      <div slot>
        <div class="searchForm_vip" style="width:100%;overflow: auto">
          <el-form :inline="false" :model="formInline" size="small" label-width="90px" v-loading="loadData">
            <el-form-item label="卡ICCID">
              <el-input v-model="formInline.card_iccid" @input="formInline.card_iccid = limitNumber(formInline.card_iccid, 20)" placeholder="请输入"></el-input>
            </el-form-item>
            <el-form-item label="卡商名称">
              <el-select v-model="formInline.card_type" filterable clearable placeholder="请选择">
                <el-option v-for="(item, index) in cardTypes" :key="index" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="机构名称">
              <el-select v-model="formInline.org_id" filterable clearable placeholder="请选择">
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
              <el-date-picker v-model="formInline.date_start" :picker-options="startDatePicker" type="date" value-format="yyyy-MM-dd" placeholder="选择开始日期"></el-date-picker> -
              <el-date-picker v-model="formInline.date_end" :picker-options="endDatePicker" type="date" value-format="yyyy-MM-dd" placeholder="选择结束日期"></el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="searchData">查询</el-button>
              <el-button type="warning" @click="resetData">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import Api from 'assets/js/api.js'
const _echart = new Api.ECHARTS({
  dataViewTitle: '机构列表'
})

export default {
  data() {
    return {
      maxTableHeight: Api.UNITS.maxTableHeight(315),
      dialogChartLoadData: true,
      formInline: {
        card_iccid: Api.UNITS.getQuery('card_iccid'),
        org_id: Api.UNITS.getQuery('org_id')
      },
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
    // 导出excel
    exportExcel() {
      Api.UNITS.exportExcel(_axios.ajaxAd.cardExport, this.formInline)
    },
    // 简单查询
    simpleSearchData() {
      let card_iccid = this.formInline.card_iccid
      this.formInline = {
        card_iccid,
        org_id: Api.UNITS.getQuery('org_id')
      }
      this.searchData()
    },
    // 获取列表数据
    getData() {
      Api.UNITS.getListData({
        vue: this,
        url: _axios.ajaxAd.getCards
      })
    },
    resetData() {
      this.list.currentPage = 1
      this.formInline = {
        card_iccid: Api.UNITS.getQuery('card_iccid'),
        org_id: Api.UNITS.getQuery('org_id')
      }
      this.sort = {}
      this.$refs.listTable.clearSort()
      this.$refs.listTable.clearSelection()
      this.getData()
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
    // 卡开启/停用(card / deadstatus / useanomaly)
    checkCardStop(scope, status) {
      let prompt = ''
      if (status === 1) {
        prompt = '是否停用该流量卡？'
      } else if (status === 0) {
        prompt = '是否启用该流量卡？'
      }
      this.$confirm(prompt, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        _axios.send({
          method: 'post',
          url: _axios.ajaxAd.checkCardStop,
          data: {
            card_iccid: scope.row.card_iccid,
            status
          },
          done: ((res) => {
            this.modifiyData(this.list.data, scope.row, 'unicom_stop', status)
            setTimeout(() => {
              this.showMsgBox({
                type: 'success',
                message: '操作成功！'
              })
            }, 150)
          })
        })
      }).catch(() => {
        this.showMsgBox({
          type: 'info',
          message: '已取消操作'
        })
      })
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

}

</style>
