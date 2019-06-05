<template>
  <div class="recharge_detail">
    <el-card class="reset-card" shadow="never">
      <el-row class="common-display" :gutter="15">
        <span class="item">ICCID：89860918700307506235</span>
        <span class="item">累计用量：<span v-html="formatFlowUnit(6806)"></span></span>
        <span class="item">当月用量：<span v-html="formatFlowUnit(20.5)"></span></span>
        <span class="item">累记充值量：<span v-html="formatFlowUnit(0)"></span></span>
        <span class="item">剩余流量：<span v-html="formatFlowUnit(1223.1)"></span></span>
        <span class="item">设备更新时间：2019-05-06 09:51:57</span>
      </el-row>
      <el-tabs @tab-click="changeTab">
        <!-- 充值详情列表 -->
        <el-tab-pane v-loading="loadData">
          <span slot="label"></i>充值详情列表</span>
          <el-form class="search-form" :inline="true" :model="formInline" size="small">
            <el-form-item label="付款方式">
              <el-select v-model="formInline.pay" placeholder="请选择">
                <el-option v-for="(item, index) in paySelect" :key="index" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="支付状态">
              <el-select v-model="formInline.pay_status" placeholder="请选择">
                <el-option label="未付款" value="0"></el-option>
                <el-option label="已付款" value="1"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="充值日期">
              <el-date-picker v-model="formInline.date_start" type="date" value-format="yyyy-MM-dd" placeholder="选择开始日期"></el-date-picker> -
              <el-date-picker v-model="formInline.date_end" type="date" value-format="yyyy-MM-dd" placeholder="选择结束日期"></el-date-picker>
            </el-form-item>
            <el-form-item label="付款日期">
              <el-date-picker v-model="formInline.date_start" type="date" value-format="yyyy-MM-dd" placeholder="选择开始日期"></el-date-picker> -
              <el-date-picker v-model="formInline.date_end" type="date" value-format="yyyy-MM-dd" placeholder="选择结束日期"></el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button size="small" type="primary">查询</el-button>
              <el-button size="small" type="warning" @click="resetData">重置</el-button>
            </el-form-item>
          </el-form>
          <el-table ref="listTable" @sort-change="handleSortChange" :data="list_0.data" :max-height="maxTableHeight" border resizable size="mini">
            <el-table-column prop="total_flow" label="分配总流量" min-width="100" sortable="custom">
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.total_flow)"></div>
              </template>
            </el-table-column>
            <el-table-column prop="flow_price" label="流量价格" min-width="95" sortable="custom"></el-table-column>
            <el-table-column prop="pay_way" label="付款方式" min-width="94" sortable="custom"></el-table-column>
            <el-table-column prop="pay_status" label="支付状态" min-width="94" sortable="custom"></el-table-column>
            <el-table-column prop="pay_code" label="支付流水号" min-width="110" sortable="custom"></el-table-column>
            <el-table-column prop="recharge_remark" label="充值备注" min-width="150" sortable="custom"></el-table-column>
            <el-table-column prop="recharge_time" label="充值时间" width="153" sortable="custom"></el-table-column>
            <el-table-column prop="pay_time" label="付款时间" width="153" sortable="custom"></el-table-column>
            <el-table-column prop="ex_time" label="过期时间" width="210" sortable="custom">
              <template slot-scope="scope">
                <div v-html="calcLeftTime(scope.row.ex_time)"></div>
              </template>
            </el-table-column>
            <el-table-column prop="order_resource" label="订单来源" min-width="100" sortable="custom"></el-table-column>
          </el-table>
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="list_0.currentPage" :page-sizes="pageSizes" :page-size="list_0.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="list_0.total">
          </el-pagination>
        </el-tab-pane>
        <!-- 流量分配详情 -->
        <el-tab-pane v-loading="loadData">
          <span slot="label">流量分配详情</span>
          <el-table @sort-change="handleSortChange" :data="list_1.data" :max-height="maxTableHeight" border resizable size="mini">
            <el-table-column prop="month" label="月份" min-width="85" sortable="custom"></el-table-column>
            <el-table-column prop="tc_flow" label="套餐流量" min-width="95" sortable="custom">
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.tc_flow)"></div>
              </template>
            </el-table-column>
            <el-table-column prop="eqleft_flow" label="设备剩余流量" min-width="120" sortable="custom">
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.eqleft_flow)"></div>
              </template>
            </el-table-column>
            <el-table-column prop="ltleft_flow" label="联通剩余流量" min-width="120" sortable="custom">
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.ltleft_flow)"></div>
              </template>
            </el-table-column>
            <el-table-column prop="tc_type" label="套餐类型" min-width="100" sortable="custom"></el-table-column>
            <el-table-column prop="is_clear" label="是否清零" min-width="100" sortable="custom"></el-table-column>
            <el-table-column prop="fp_month" label="分配月数" min-width="100" sortable="custom"></el-table-column>
            <el-table-column prop="month_flow" label="月均流量" min-width="95">
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.month_flow)"></div>
              </template>
            </el-table-column>
            <el-table-column prop="fp_time" label="分配时间" min-width="145" sortable="custom"></el-table-column>
            <el-table-column prop="ex_time" label="过期时间" min-width="210" sortable="custom">
              <template slot-scope="scope">
                <div v-html="calcLeftTime(scope.row.ex_time)"></div>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="list_1.currentPage" :page-sizes="pageSizes" :page-size="list_1.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="list_1.total">
          </el-pagination>
        </el-tab-pane>
        <!-- 生命周期日志 -->
        <el-tab-pane v-loading="loadData">
          <span slot="label">生命周期日志</span>
          <el-table @sort-change="handleSortChange" :data="list_2.data" :max-height="maxTableHeight" border resizable size="mini">
            <el-table-column prop="log_time" label="日志时间" min-width="145" sortable="custom"></el-table-column>
            <el-table-column prop="log_type" label="日志类型" min-width="150" sortable="custom">
              <template slot-scope="scope">
                <el-button type="text">{{scope.row.log_type}}</el-button>
              </template>
            </el-table-column>
            <el-table-column prop="log_desc" label="日志描述" min-width="400" sortable="custom"></el-table-column>
          </el-table>
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="list_2.currentPage" :page-sizes="pageSizes" :page-size="list_2.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="list_2.total">
          </el-pagination>
        </el-tab-pane>
        <!-- 日使用情况 -->
        <el-tab-pane v-loading="loadData">
          <span slot="label">日使用情况</span>
          <el-table @sort-change="handleSortChange" :data="list_3.data" :max-height="maxTableHeight" border resizable size="mini">
            <el-table-column prop="statistics_date" label="统计日期" min-width="100" sortable="custom"></el-table-column>
            <el-table-column prop="day_use" label="日使用流量" min-width="100" sortable="custom">
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.day_use)"></div>
              </template>
            </el-table-column>
            <el-table-column prop="day_exceed" label="日超标流量" min-width="100" sortable="custom">
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.day_exceed)"></div>
              </template>
            </el-table-column>
            <el-table-column prop="yesday_leftflow" label="昨日剩余流量" min-width="100" sortable="custom">
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.yesday_leftflow)"></div>
              </template>
            </el-table-column>
            <el-table-column prop="day_leftflow" label="今日剩余流量" min-width="100" sortable="custom">
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.day_leftflow)"></div>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="list_3.currentPage" :page-sizes="pageSizes" :page-size="list_3.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="list_3.total">
          </el-pagination>
        </el-tab-pane>
        <!-- 月使用情况 -->
        <el-tab-pane v-loading="loadData">
          <span slot="label">月使用情况</span>
          <el-table @sort-change="handleSortChange" :data="list_4.data" :max-height="maxTableHeight" border resizable size="mini">
            <el-table-column prop="statistics_month" label="统计年月" min-width="100" sortable="custom"></el-table-column>
            <el-table-column prop="month_use" label="月使用流量" min-width="100" sortable="custom">
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.month_use)"></div>
              </template>
            </el-table-column>
            <el-table-column prop="month_left" label="月剩余流量" min-width="100" sortable="custom">
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.month_left)"></div>
              </template>
            </el-table-column>
            <el-table-column prop="month_exceed" label="月超标流量" min-width="100" sortable="custom">
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.month_exceed)"></div>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="list_3.currentPage" :page-sizes="pageSizes" :page-size="list_3.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="list_3.total">
          </el-pagination>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>
<script>
import Api from 'assets/js/api.js'
import { mapState } from 'vuex'

export default {
  data() {
    return {
      loadData: true, // 是否显示加载动画
      tabIndex: '0', // 当前激活的tab的下标值
      pageSizes: Api.STATIC.pageSizes, // 显示页数列表，通配的
      // 列表数据
      list_0: { // 充值详情列表
        data: [],
        pagesize: Api.STATIC.pageSizes[1],
        currentPage: 1,
        total: 0
      },
      list_1: { // 流量分配详情
        data: [],
        pagesize: Api.STATIC.pageSizes[1],
        currentPage: 1,
        total: 0
      },
      list_2: { // 生命周期日志
        data: [],
        pagesize: Api.STATIC.pageSizes[1],
        currentPage: 1,
        total: 0
      },
      list_3: { // 日使用情况
        data: [],
        pagesize: Api.STATIC.pageSizes[1],
        currentPage: 1,
        total: 0
      },
      list_4: { // 月使用情况
        data: [],
        pagesize: Api.STATIC.pageSizes[1],
        currentPage: 1,
        total: 0
      },
      // 查询表单数据
      formInline: {},
      sort_0: {},
      sort_1: {},
      sort_2: {},
      sort_3: {},
      sort_4: {},
      maxTableHeight: Api.UNITS.maxTableHeight(),
    }
  },
  mounted() {
    this.getData()
  },
  methods: {
    changeTab(para) {
      this.tabIndex = para.index
      if (this[`list_${this.tabIndex}`].data.length === 0) {
        // 如果当前的列表没有数据
        this.getData()
      }
    },
    // 处理多个pagination的情况
    handleSizeChange(val) {
      this[`list_${this.tabIndex}`].pagesize = val
      this.getData()
    },
    handleCurrentChange(val) {
      this[`list_${this.tabIndex}`].currentPage = val
      this.getData()
    },
    handleSortChange(val = {}) {
      Api.UNITS.setSortSearch(val, this, `sort_${this.tabIndex}`)
      this.getData()
    },
    // 重置列表
    resetData() {
      this.formInline = {} // 1、重置查询表单
      this.sort_0 = {} // 2、重置排序
      this.$refs.listTable.clearSort() // 3、清空排序样式
      this.getData()
    },
    getData() {
      this.loadData = true
      // 根据 tabIndex, pagesize, currentpage来请求数据
      let tabIndex = this.tabIndex
      let list = this[`list_${tabIndex}`]
      // 一下针对不同的列表请求不同的资源
      if (tabIndex === '0') {
        setTimeout(() => {
          list.data = [{
            id: 0,
            total_flow: 30,
            flow_price: '￥0.01',
            pay_way: '系统赠送',
            pay_status: '已付款',
            pay_code: ' system-give',
            recharge_remark: '系统赠送',
            recharge_time: '2019-01-21 09:58:45',
            pay_time: '2019-01-21 09:58:45',
            ex_time: '2020-01-21 09:58:45',
            order_resource: '未知来源'
          }]
          list.total = list.data.length
          this.loadData = false
        }, 1000)
      } else if (tabIndex === '1') {
        setTimeout(() => {
          list.data = [{
            id: 0,
            month: '2019-05',
            tc_flow: 200,
            eqleft_flow: 90,
            ltleft_flow: 120,
            tc_type: '固定套餐',
            is_clear: 0,
            fp_month: 12,
            month_flow: 30,
            fp_time: '20219-11-21 09:58:45',
            ex_time: '2020-11-21 09:58:45'
          }]
          list.total = list.data.length
          this.loadData = false
        }, 1000)
      } else if (tabIndex === '2') {
        setTimeout(() => {
          list.data = [{
            id: 0,
            log_time: '2019-05-21 15:56:85',
            log_type: '流量卡入库',
            log_desc: '从机构 “陆风-待用” 变更到 “云智测试” 机构，初始流量10M 有效周期21天'
          }]
          list.total = list.data.length
          this.loadData = false
        }, 1000)
      } else if (tabIndex === '3') {
        setTimeout(() => {
          list.data = [{
            id: 0,
            statistics_date: '2019-05-21',
            day_use: 125,
            day_exceed: 0,
            yesday_leftflow: 1452,
            day_leftflow: 1521.325
          }]
          list.total = list.data.length
          this.loadData = false
        }, 1000)
      } else if (tabIndex === '4') {
        setTimeout(() => {
          list.data = [{
            id: 0,
            statistics_month: '2019-05',
            month_use: 125,
            month_left: 5645.654,
            month_exceed: 0
          }]
          list.total = list.data.length
          this.loadData = false
        }, 1000)
      }
    },
    formatFlowUnit: Api.UNITS.formatFlowUnit, // 格式化流量单位
    limitNumber: Api.UNITS.limitNumber, // 限制数字类型位数
    calcLeftTime: Api.UNITS.calcLeftTime // 计算剩余时间
  },
  computed: {
    // 模拟分页
    ...mapState({
      paySelect: 'paySelect',
    })
  }
}

</script>
<style lang="scss">
.recharge_detail {
  .common-display {
    font-size: 14px;
    margin-bottom: 10px;

    .item {
      display: inline-block;
      margin: 5px 14px;
    }
  }

  .el-pagination {
    float: right;
    margin: 25px 40px 0 0;
  }

  .el-card__body {
    img {
      width: 100%;
    }

    .small {
      font-size: 12px;
    }

  }
}

</style>
