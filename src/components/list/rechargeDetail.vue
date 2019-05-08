<template>
  <div>
    <el-card class="reset-card" shadow="never">
      <el-row class="common-display" :gutter="40">
        <el-col :span="6">
          <span>ICCID：89860918700307506235</span>
        </el-col>
        <el-col :span="3">
          <span>累计用量：<span v-html="formatFlowUnit(6806)"></span></span>
        </el-col>
        <el-col :span="3">
          <span>当月用量：<span v-html="formatFlowUnit(20.5)"></span></span>
        </el-col>
        <el-col :span="3">
          <span>累记充值量：<span v-html="formatFlowUnit(0)"></span></span>
        </el-col>
        <el-col :span="3">
          <span>剩余流量：<span v-html="formatFlowUnit(1223.1)"></span></span>
        </el-col>
        <el-col :span="6">
          <span>设备更新时间：2019-05-06 09:51:57</span>
        </el-col>
      </el-row>
      <el-tabs @tab-click="changeTab">
        <!-- 充值详情列表 -->
        <el-tab-pane>
          <span slot="label"></i>充值详情列表</span>
          <el-form class="search-form" :inline="true" :model="searchForm" size="small">
            <el-form-item label="付款方式">
              <el-select v-model="searchForm.pay" placeholder="请选择">
                <el-option label="微信" value="0"></el-option>
                <el-option label="支付宝" value="1"></el-option>
                <el-option label="银行转账" value="2"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="支付状态">
              <el-select v-model="searchForm.pay_status" placeholder="请选择">
                <el-option label="未付款" value="0"></el-option>
                <el-option label="已付款" value="1"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="充值时间">
              <el-date-picker v-model="searchForm.recharge_time" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="付款时间">
              <el-date-picker v-model="searchForm.pay_time" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button size="small" type="primary">查询</el-button>
            </el-form-item>
          </el-form>
          <el-table :data="curTableData" border :default-sort="{prop: 'pay_time', order: 'descending'}" size="mini">
            <el-table-column show-overflow-tooltip label="分配总流量" min-width="100">
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.total_flow)"></div>
              </template>
            </el-table-column>
            <el-table-column show-overflow-tooltip prop="flow_price" label="流量价格" min-width="85"></el-table-column>
            <el-table-column show-overflow-tooltip prop="pay_way" label="付款方式" show-overflow-tooltip min-width="95" sortable></el-table-column>
            <el-table-column show-overflow-tooltip prop="pay_status" label="支付状态" show-overflow-tooltip min-width="95" sortable></el-table-column>
            <el-table-column show-overflow-tooltip prop="pay_code" label="支付流水号" show-overflow-tooltip min-width="110"></el-table-column>
            <el-table-column show-overflow-tooltip prop="recharge_remark" label="充值备注" show-overflow-tooltip min-width="150"></el-table-column>
            <el-table-column show-overflow-tooltip prop="recharge_time" label="充值时间" show-overflow-tooltip min-width="145" sortable></el-table-column>
            <el-table-column show-overflow-tooltip prop="pay_time" label="付款时间" show-overflow-tooltip min-width="145" sortable></el-table-column>
            <el-table-column show-overflow-tooltip label="过期时间" show-overflow-tooltip min-width="205">
              <template slot-scope="scope">
                <div v-html="calcLeftTime(scope.row.ex_time)"></div>
              </template>
            </el-table-column>
            <el-table-column show-overflow-tooltip prop="order_resource" label="订单来源" show-overflow-tooltip min-width="100"></el-table-column>
          </el-table>
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentPage_0" :page-sizes="[1, 2, 3, 4]" :page-size="pagesize_0" layout="total, sizes, prev, pager, next, jumper" :total="tableData_0.length">
          </el-pagination>
        </el-tab-pane>
        <!-- 流量分配详情 -->
        <el-tab-pane>
          <span slot="label">流量分配详情</span>
          <el-table :data="curTableData" border :default-sort="{prop: 'fp_time', order: 'descending'}" size="mini">
            <el-table-column show-overflow-tooltip prop="month" label="月份" min-width="85"></el-table-column>
            <el-table-column show-overflow-tooltip label="套餐流量" min-width="95">
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.tc_flow)"></div>
              </template>
            </el-table-column>
            <el-table-column show-overflow-tooltip label="设备剩余流量" min-width="120">
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.eqleft_flow)"></div>
              </template>
            </el-table-column>
            <el-table-column show-overflow-tooltip label="联通剩余流量" min-width="120">
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.ltleft_flow)"></div>
              </template>
            </el-table-column>
            <el-table-column show-overflow-tooltip prop="tc_type" label="套餐类型" min-width="85"></el-table-column>
            <el-table-column show-overflow-tooltip prop="is_clear" label="是否清零" min-width="85"></el-table-column>
            <el-table-column show-overflow-tooltip prop="fp_month" label="分配月数" min-width="85"></el-table-column>
            <el-table-column show-overflow-tooltip label="月均流量" min-width="95">
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.month_flow)"></div>
              </template>
            </el-table-column>
            <el-table-column show-overflow-tooltip prop="fp_time" label="分配时间" show-overflow-tooltip min-width="145" sortable></el-table-column>
            <el-table-column show-overflow-tooltip label="过期时间" show-overflow-tooltip min-width="180">
              <template slot-scope="scope">
                <div v-html="calcLeftTime(scope.row.ex_time)"></div>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentPage_1" :page-sizes="[1, 2, 3, 4]" :page-size="pagesize_1" layout="total, sizes, prev, pager, next, jumper" :total="tableData_1.length">
          </el-pagination>
        </el-tab-pane>
        <!-- 生命周期日志 -->
        <el-tab-pane>
          <span slot="label">生命周期日志</span>
        </el-tab-pane>
        <!-- 日使用情况 -->
        <el-tab-pane>
          <span slot="label">日使用情况</span>
        </el-tab-pane>
        <!-- 月使用情况 -->
        <el-tab-pane>
          <span slot="label">月使用情况</span>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>
<script>
import Api from 'assets/js/api.js'

export default {
  data() {
    return {
      tableData_0: [{
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
      }, {
        id: 1,
        total_flow: 300,
        flow_price: '￥0.01',
        pay_way: '系统赠送',
        pay_status: '已付款',
        pay_code: ' system-give',
        recharge_remark: '系统赠送',
        recharge_time: '2019-01-21 09:58:45',
        pay_time: '2019-01-21 09:58:45',
        ex_time: '2020-11-21 09:58:45',
        order_resource: '未知来源'
      }],
      tableData_1: [{
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
      }, {
        id: 1,
        month: '2019-06',
        tc_flow: 300,
        eqleft_flow: 80,
        ltleft_flow: 130,
        tc_type: '固定套餐',
        is_clear: 0,
        fp_month: 12,
        month_flow: 30,
        fp_time: '20219-10-21 09:58:45',
        ex_time: '2020-11-21 09:58:45'
      }],
      tabIndex: '0', // 当前激活的tab的下标值
      pagesize_0: 1,
      pagesize_1: 1,
      pagesize_2: 1,
      pagesize_3: 1,
      pagesize_4: 1,
      currentPage_0: 1,
      currentPage_1: 1,
      currentPage_2: 1,
      currentPage_3: 1,
      currentPage_4: 1,
      // 查询表单数据
      searchForm: {}
    }
  },
  mounted() {},
  methods: {
    changeTab(para) {
      this.tabIndex = para.index
    },
    // 处理多个pagination的情况
    handleSizeChange(val) {
      this[`pagesize_${this.tabIndex}`] = val
    },
    handleCurrentChange(val) {
      this[`currentPage_${this.tabIndex}`] = val
    },
    formatFlowUnit: Api.UNITS.formatFlowUnit, // 格式化流量单位
    limitNumber: Api.UNITS.limitNumber, // 限制数字类型位数
    calcLeftTime: Api.UNITS.calcLeftTime // 计算剩余时间
  },
  computed: {
    curTableData() {
      const tableData = this[`tableData_${this.tabIndex}`]
      const currentPage = this[`currentPage_${this.tabIndex}`]
      const pagesize = this[`pagesize_${this.tabIndex}`]
      return tableData.slice((currentPage - 1) * pagesize, currentPage * pagesize)
    }
  }
}

</script>
<style lang="scss">
.common-display {
  font-size: 14px;
  margin-bottom: 15px;
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

  .search-form {
    margin-bottom: 30px;
  }
}

</style>
