<template>
  <div class="jg_recharge">
    <el-card class="search-card" style="margin-bottom: 20px" shadow="never">
      <el-form class="search-form" :inline="true" :model="formInline" size="small">
        <el-form-item label="机构名称">
          <el-select v-model="formInline.org_id" filterable clearable placeholder="请选择机构">
            <el-option v-for="(item, index) in orgs" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="付款方式">
          <el-select v-model="formInline.pay_method" clearable placeholder="请选择付款方式">
            <el-option v-for="(item, index) in payMethodSelect" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="起止日期">
          <el-date-picker v-model="formInline.date_start" :picker-options="startDatePicker" type="date" value-format="yyyy-MM-dd" placeholder="选择开始日期"></el-date-picker> -
          <el-date-picker v-model="formInline.date_end" :picker-options="endDatePicker" type="date" value-format="yyyy-MM-dd" placeholder="选择结束日期"></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchData">查询</el-button>
          <el-button type="warning" @click="resetData">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="clearfix" shadow="never" v-loading="loadData">
      <el-table ref="listTable" @sort-change="handleSortChange" :data="list.data" :max-height="maxTableHeight" border resizable size="mini">
        <el-table-column prop="org_id" label="机构名称" min-width="200" sortable="custom">
          <template slot-scope="scope">
            <span v-if="scope.row.sums">{{scope.row.org_name}}</span>
            <span v-else class="btn-link" @click="$router.push({name: 'rechargeParticulars', query: {org_id: scope.row.org_id}})">{{scope.row.org_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="pay_method" label="支付方式" min-width="100" sortable="custom">
          <template slot-scope="scope">
            <span>{{scope.row.pay_method_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="paid_amount" label="已付次数" min-width="100" sortable="custom"></el-table-column>
        <el-table-column prop="paid_money" label="已付金额" min-width="140" sortable="custom">
          <template slot-scope="scope">
            <div>￥{{scope.row.paid_money|formatMoney}}</div>
          </template>
        </el-table-column>
        <el-table-column label="已付款率" min-width="100">
          <template slot-scope="scope">
            <span>{{(scope.row.paid_amount/scope.row.paid_count*100).toFixed(3)}}%</span>
          </template>
        </el-table-column>
        <el-table-column prop="nopaid_amount" label="未付次数" min-width="100" sortable="custom"></el-table-column>
        <el-table-column prop="nopaid_money" label="未付金额" min-width="140" sortable="custom">
          <template slot-scope="scope">
            <div>￥{{scope.row.nopaid_money|formatMoney}}</div>
          </template>
        </el-table-column>
        <el-table-column label="未付款率" min-width="100">
          <template slot-scope="scope">
            <span>{{(scope.row.nopaid_amount/scope.row.paid_count*100).toFixed(3)}}%</span>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="list.currentPage" :page-sizes="pageSizes" :page-size="list.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="list.total" class="clearfix">
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
      pageSizes: Api.STATIC.pageSizes,
      list: {
        data: [],
        pagesize: Api.STATIC.pageSizes[1],
        currentPage: 1,
        total: 0,
      },
      sort: {},
      formInline: {
        org_id: Api.UNITS.getQuery('org_id')
      },
      maxTableHeight: Api.UNITS.maxTableHeight(360),
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
    // 查询
    searchData() {
      this.list.currentPage = 1
      this.getData()
    },
    // 重置列表
    resetData() {
      this.list.currentPage = 1
      this.formInline = {
        org_id: Api.UNITS.getQuery('org_id')
      } // 1、重置查询表单
      this.sort = {} // 2、重置排序
      this.$refs.listTable.clearSort() // 3、清空排序样式
      this.getData()
    },
    // 获取列表数据
    getData() {
      Api.UNITS.getListData({
        vue: this,
        url: _axios.ajaxAd.getOrgPayReport,
        cb: (res) => {
          let other = res.data.other || {}
          let data = this.list.data || []
          if (data.length === 0) return
          data.push(...[{
            sums: true,
            org_name: '总计',
            paid_amount: other.paid_amount,
            paid_money: other.paid_money,
            nopaid_amount: other.nopaid_amount,
            nopaid_money: other.nopaid_money,
            paid_count: other.paid_count
          }])
        }
      })
    },
    formatFlowUnit: Api.UNITS.formatFlowUnit,
    calcLeftTime: Api.UNITS.calcLeftTime
  },
  computed: {
    ...mapState({
      orgs: 'orgs',
      payMethodSelect: 'payMethodSelect'
    }),
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
