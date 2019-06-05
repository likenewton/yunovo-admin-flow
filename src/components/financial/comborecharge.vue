<template>
  <div class="combo_recharge">
    <el-card style="margin-bottom: 20px" shadow="never">
      <el-form :inline="true" :model="formInline" size="small">
        <el-form-item label="机构名称">
          <el-select v-model="formInline.org_id" filterable clearable placeholder="请选择">
            <el-option v-for="(item, index) in orgs" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="付款方式">
          <el-select v-model="formInline.pay_way" placeholder="请选择">
            <el-option label="支付宝" value="0"></el-option>
            <el-option label="微信" value="1"></el-option>
            <el-option label="其他" value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="起止日期">
          <el-date-picker v-model="formInline.date_start" type="date" value-format="yyyy-MM-dd" placeholder="选择开始日期"></el-date-picker> -
          <el-date-picker v-model="formInline.date_end" type="date" value-format="yyyy-MM-dd" placeholder="选择结束日期"></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getData">查询</el-button>
          <el-button type="warning" @click="resetData">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="clearfix" shadow="never" v-loading="loadData">
      <el-table ref="listTable" @sort-change="handleSortChange" :data="list.data" :max-height="maxTableHeight" border resizable size="mini">
        <el-table-column prop="org_name" label="机构名称" min-width="200" sortable="custom">
          <template slot-scope="scope">
            <span v-if="scope.row.sums">{{scope.row.org_name}}</span>
            <span v-else class="btn-link">{{scope.row.org_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="combo_flow" label="套餐流量" min-width="110" sortable="custom">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.combo_flow)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="combo_money" label="套餐金额" min-width="110" sortable="custom">
          <template slot-scope="scope">
            <div>￥{{scope.row.combo_money|formatMoney}}</div>
          </template>
        </el-table-column>
        <el-table-column prop="pay_count" label="已付次数" min-width="100" sortable="custom"></el-table-column>
        <el-table-column porp="pay_money" label="已付金额" min-width="110" sortable="custom">
          <template slot-scope="scope">
            <div>￥{{scope.row.pay_money|formatMoney}}</div>
          </template>
        </el-table-column>
        <el-table-column prop="pay_rate" label="已付款率" min-width="100" sortable="custom"></el-table-column>
        <el-table-column prop="nopay_count" label="未付次数" min-width="100" sortable="custom"></el-table-column>
        <el-table-column prop="nopay_money" label="未付金额" min-width="110" sortable="custom">
          <template slot-scope="scope">
            <div>￥{{scope.row.nopay_money|formatMoney}}</div>
          </template>
        </el-table-column>
        <el-table-column prop="nopay_rate" label="未付款率" min-width="100" sortable="custom"></el-table-column>
      </el-table>
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentPage" :page-sizes="pageSizes" :page-size="list.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="list.total" class="clearfix">
      </el-pagination>
    </el-card>
  </div>
</template>
<script>
import Api from 'assets/js/api.js'
import { mapState } from 'vuex'

export default {
  data() {
    return {
      loadData: true,
      tabIndex: '0',
      pageSizes: Api.STATIC.pageSizes,
      list: {
        data: [],
        pagesize: Api.STATIC.pageSizes[1],
        currentPage: 1,
        total: 0,
      },
      formInline: {},
      maxTableHeight: Api.UNITS.maxTableHeight(),
    }
  },
  mounted() {
    // 进入页面的时候请求数据
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
    handleSortChange(val = {}) {
      Api.UNITS.setSortSearch(val, this)
      this.getData()
    },
    // 重置列表
    resetData() {
      this.formInline = {} // 1、重置查询表单
      this.sort = {} // 2、重置排序
      this.$refs.listTable.clearSort() // 3、清空排序样式
      this.getData()
    },
    // 获取列表数据
    getData() {
      setTimeout(() => {
        // 数据请求成功
        this.list.data = [{
          id: 0,
          org_name: '卡仕特-西格玛',
          combo_flow: NaN,
          combo_money: 653,
          pay_count: 12,
          pay_money: 23453.23,
          pay_rate: '54.25%',
          nopay_count: 12,
          nopay_money: 2123,
          nopay_rate: '25.52%'
        }]
        this.list.total = this.list.data.length
        this.loadData = false
      }, 1000)
    },
    formatFlowUnit: Api.UNITS.formatFlowUnit,
    calcLeftTime: Api.UNITS.calcLeftTime
  },
  computed: {
    ...mapState({
      orgs: 'orgs',
    })
  }
}

</script>
<style lang="scss">
.combo_recharge {
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
}

</style>
