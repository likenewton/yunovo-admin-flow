<template>
  <div>
    <el-card class="reset-card" shadow="never">
      <el-tabs>
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
          <el-table ref="multipleTable" :data="curTableData" border :default-sort="{prop: 'ex_time', order: 'descending'}" size="mini">
            <el-table-column fixed="left" show-overflow-tooltip label="卡ICCID" min-width="170">
              <template slot-scope="scope">
                <el-button type="text">{{scope.row.iccid}}</el-button>
              </template>
            </el-table-column>
            <el-table-column show-overflow-tooltip prop="jg_name" label="机构名称" min-width="140"></el-table-column>
            <el-table-column show-overflow-tooltip label="套餐流量" show-overflow-tooltip min-width="95" sortable>
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.tc_flow)"></div>
              </template>
            </el-table-column>
            <el-table-column show-overflow-tooltip prop="fp_month" label="分配月数" show-overflow-tooltip min-width="95" sortable></el-table-column>
            <el-table-column show-overflow-tooltip label="月均流量" show-overflow-tooltip min-width="95" sortable>
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.tc_flow/scope.row.fp_month)"></div>
              </template>
            </el-table-column>
            <el-table-column show-overflow-tooltip prop="is_clear" label="是否清零" show-overflow-tooltip min-width="80"></el-table-column>
            <el-table-column show-overflow-tooltip prop="eff_pri" label="有效周期" show-overflow-tooltip min-width="80"></el-table-column>
            <el-table-column show-overflow-tooltip prop="donator_remark" label="赠者&备注" show-overflow-tooltip min-width="120"></el-table-column>
            <el-table-column show-overflow-tooltip prop="op_p" label="操作者" show-overflow-tooltip min-width="100"></el-table-column>
            <el-table-column show-overflow-tooltip prop="add_time" label="添加时间" show-overflow-tooltip min-width="155" sortable></el-table-column>
            <el-table-column show-overflow-tooltip prop="ex_time" label="过期时间" show-overflow-tooltip min-width="155" sortable></el-table-column>
          </el-table>
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentPage" :page-sizes="[10, 20, 30, 40]" :page-size="pagesize" layout="total, sizes, prev, pager, next, jumper" :total="tableData.length">
          </el-pagination>
        </el-tab-pane>
        <el-tab-pane>
          <span slot="label">流量分配详情</span>
        </el-tab-pane>
        <el-tab-pane>
          <span slot="label">生命周期日志</span>
        </el-tab-pane>
        <el-tab-pane>
          <span slot="label">日使用情况</span>
        </el-tab-pane>
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
      routeName: this.$route.name,
      currentDate: new Date(),
      tableData: [{
        id: 0,
        iccid: '8986011670901045280',
        jg_name: '卡仕特-西格玛',
        tc_flow: 65421,
        fp_month: 12,
        month_flow: 0,
        is_clear: 1,
        eff_pri: '1个月',
        donator_remark: 'Newton',
        op_p: 'Lucy',
        add_time: '2019-02-03 21:01:03',
        ex_time: '2019-01-21 09:58:45'
      }],
      pagesize: 20,
      currentPage: 1,
      searchForm: {}
    }
  },
  mounted() {

  },
  methods: {
    handleSizeChange(val) {
      this.pagesize = val
    },
    handleCurrentChange(val) {
      this.currentPage = val
    },
    formatFlowUnit: Api.UNITS.formatFlowUnit,
    limitNumber: Api.UNITS.limitNumber,
  },
  computed: {
    curTableData() {
      return this.tableData.slice((this.currentPage - 1) * this.pagesize, this.currentPage * this.pagesize)
    }
  }
}

</script>
<style lang="scss">
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
