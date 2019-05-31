<template>
  <div>
    <el-card class="box-card clearfix" shadow="never" v-loading="loadData">
      <el-form :inline="true" :model="formInline" class="demo-form-inline" size="small">
        <el-form-item label="通知或来源">
          <el-select v-model="formInline.inform_source" filterable placeholder="请选择">
            <el-option label="选项1" value="0"></el-option>
            <el-option label="选项2" value="1"></el-option>
            <el-option label="选项3" value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="统计日期">
          <el-date-picker v-model="formInline.statistics_data" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getData">查询</el-button>
          <el-button type="warning" @click="formInline = {}">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table ref="multipleTable" :data="list.data" @sort-change="handleSortChange" border size="mini">
        <el-table-column prop="statistics_data" label="统计日期" min-width="100" sortable="custom"></el-table-column>
        <el-table-column prop="inform_source" label="通知或来源" min-width="150"></el-table-column>
        <el-table-column prop="imform_suc_count" label="通知成功数" min-width="110" sortable="custom"></el-table-column>
        <el-table-column prop="imform_fail_count" label="通知失败数" min-width="110" sortable="custom"></el-table-column>
        <el-table-column prop="imform_time" label="通知时长" min-width="80"></el-table-column>
        <el-table-column prop="order_suc_count" label="下单成功数" min-width="105" sortable="custom"></el-table-column>
        <el-table-column prop="order_fail_count" label="下单失败数" min-width="105" sortable="custom"></el-table-column>
        <el-table-column prop="order_suc_rate" label="下单成功比率" min-width="110"></el-table-column>
        <el-table-column prop="order_fail_rate" label="下单失败比率" min-width="110"></el-table-column>
        <el-table-column label="已付金额" min-width="110">
          <template slot-scope="scope">
            <div>￥{{scope.row.pay_money|formatMoney}}</div>
          </template>
        </el-table-column>
        <el-table-column label="未付金额" min-width="110">
          <template slot-scope="scope">
            <div>￥{{scope.row.abpay_money|formatMoney}}</div>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentPage" :page-sizes="pageSizes" :page-size="list.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="list.total" class="clearfix">
      </el-pagination>
    </el-card>
  </div>
</template>
<script>
import Api from 'assets/js/api.js'

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
      sort: {},
      formInline: {}
    }
  },
  mounted() {
    this.getData()
  },
  methods: {
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
    // 获取列表数据
    getData() {
      Api.UNITS.getListData({
        vue: this,
        url: _axios.ajaxAd.getStats
      })
      // setTimeout(() => {
      //   // 数据请求成功
      //   this.list.data = [{
      //     id: 0,
      //     statistics_data: '2019-03-21',
      //     inform_source: '流量用完第三次预警',
      //     imform_suc_count: 12,
      //     imform_fail_count: 2,
      //     imform_time: '2秒',
      //     order_suc_count: 2,
      //     order_fail_count: 1,
      //     order_suc_rate: '66.66%',
      //     order_fail_rate: '33.33%',
      //     pay_money: 512.21,
      //     abpay_money: 0
      //   }]
      //   this.list.total = this.list.data.length
      //   this.loadData = false
      // }, 1000)
    },
    formatFlowUnit: Api.UNITS.formatFlowUnit,
    calcLeftTime: Api.UNITS.calcLeftTime
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
