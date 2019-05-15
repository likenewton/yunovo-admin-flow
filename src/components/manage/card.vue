<template>
  <div>
    <el-card class="box-card" style="margin-bottom: 20px" shadow="never">
      <el-form :inline="true" :model="formInline" class="demo-form-inline" size="small">
        <el-form-item label="卡ICCID">
          <el-input v-model="formInline.iccid" placeholder="请输入"></el-input>
        </el-form-item>
        <el-form-item label="卡商名称">
          <el-select v-model="formInline.ks_name" placeholder="请选择">
            <el-option label="名称1" value="0"></el-option>
            <el-option label="名称2" value="1"></el-option>
            <el-option label="名称3" value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="机构名称">
          <el-select v-model="formInline.jg_name" placeholder="请选择">
            <el-option label="机构1" value="0"></el-option>
            <el-option label="机构2" value="1"></el-option>
            <el-option label="机构3" value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="激活状态">
          <el-select v-model="formInline.active_status" placeholder="请选择">
            <el-option label="已激活" value="0"></el-option>
            <el-option label="未激活" value="1"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="激活时间">
          <el-date-picker v-model="formInline.active_time" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="过期时间">
          <el-select v-model="formInline.exceed_time" placeholder="请选择">
            <el-option label="已过期" value="0"></el-option>
            <el-option label="未过期" value="1"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary">查询</el-button>
          <el-button type="warning">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="box-card clearfix" shadow="never">
      <el-button-group style="margin-bottom: 10px">
        <el-button size="mini" type="primary" @click="showEcharts">图表</el-button>
        <el-button size="mini" type="primary">导入</el-button>
        <el-button size="mini" type="primary">导出</el-button>
      </el-button-group>
      <el-table v-loading="loadData" ref="multipleTable" :data="curTableData" border :default-sort="{prop: 'exceed_time', order: 'descending'}" size="mini">
        <el-table-column fixed="left" show-overflow-tooltip label="卡ICCID" min-width="170">
          <template slot-scope="scope">
            <el-button type="text">{{scope.row.iccid}}</el-button>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="ks_name" label="卡商名称" min-width="120"></el-table-column>
        <el-table-column show-overflow-tooltip prop="jg_name" label="机构名称" min-width="120"></el-table-column>
        <el-table-column show-overflow-tooltip label="当月用量" show-overflow-tooltip min-width="93" sortable>
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.m_use)"></div>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip label="累计用量" show-overflow-tooltip min-width="93" sortable>
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.total_use)"></div>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip label="剩余用量" show-overflow-tooltip min-width="93" sortable>
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.left_use)"></div>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="excard_time" label="导卡时间" show-overflow-tooltip min-width="151" sortable></el-table-column>
        <el-table-column show-overflow-tooltip prop="active_time" label="激活时间" show-overflow-tooltip min-width="151" sortable></el-table-column>
        <el-table-column show-overflow-tooltip prop="eq_time" label="设备更新时间" show-overflow-tooltip min-width="151" sortable></el-table-column>
        <el-table-column show-overflow-tooltip label="过期时间" show-overflow-tooltip min-width="215" sortable>
          <template slot-scope="scope">
            <div v-html="calcLeftTime(scope.row.exceed_time)"></div>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="op_status" label="运行状态" show-overflow-tooltip min-width="70"></el-table-column>
        <el-table-column show-overflow-tooltip prop="active_status" label="激活状态" show-overflow-tooltip min-width="70"></el-table-column>
        <el-table-column fixed="right" label="操作" min-width="140">
          <template slot-scope="scope">
            <el-button type="text">同步</el-button>
            <el-button type="text">停用</el-button>
            <el-button type="text">套餐</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="list.currentPage" :page-sizes="pageSizes" :page-size="list.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="list.total" class="clearfix">
      </el-pagination>
    </el-card>
    <v-dialog :dialogPara="dialogPara"></v-dialog>
  </div>
</template>
<script>
import Api from 'assets/js/api.js'
import { mapState, mapMutations } from 'vuex'

export default {
  data() {
    return {
      loadData: true,
      pageSizes: Api.STATIC.pageSizes,
      list: {
        data: [],
        pagesize: Api.STATIC.pageSizes[1],
        currentPage: 1,
        total: 0,
      },
      formInline: {},
      // 要展开的对话框的参数
      dialogPara: {
        loadDialog: true,
        title: '机构流量ICCID卡统计图表',
        content: '<div id="myChart" style="width:100%; height:300px"></div>'
      },
      // 图表实例
      myChart: null,
      options: {
        xAxis: {
          type: 'category',
          data: ['自营', '凯龙丁威特', '博毅', '天之眼', '湖南纽曼', '奇橙天下', '自营>云智易联']
        },
        yAxis: {
          type: 'value',
          splitLine: { show: false }
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          },
        },
        series: [{
          data: [120, 200, 150, 80, 70, 110, 130],
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
                const colorList = ['#e92322', 'rgb(42,170,227)', 'rgb(25,46,94)', 'rgb(195,229,235)', 'grey', 'pink', 'lightblue'];
                return colorList[params.dataIndex]
              }
            },
            //鼠标悬停时：
            emphasis: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          },
        }],
        toolbox: { //可视化的工具箱
          show: true,
          feature: {
            saveAsImage: { show: true }
          }
        },
      }
    }
  },
  mounted() {
    // 进入页面的时候请求数据
    if (this.list.data.length === 0) {
      this.getData()
    } else {
      this.loadData = false
    }
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
    showEcharts() {
      this.SET_DIALOGVISIBLE({ dialogVisible: true })
      setTimeout(() => {
        // myChart因为是要放在dialog中所以必须要等到dialog展示之后才能展示
        this.myChart = this.$echarts.init(document.getElementById('myChart'))
        this.getEchartsData()
      }, 0)
    },
    // 获取列表数据
    getData() {
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
    // 获取图标数据
    getEchartsData() {
      setTimeout(() => {
        this.dialogPara.loadDialog = false
        // 这里拿到数据后要将数据保存到options中在调用setOption
        this.myChart.setOption(this.options)
      }, 1000)
    },
    formatFlowUnit: Api.UNITS.formatFlowUnit,
    calcLeftTime: Api.UNITS.calcLeftTime
  },
  computed: {
    ...mapState({
      dialogVisible: 'dialogVisible',
    }),
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

</style>
