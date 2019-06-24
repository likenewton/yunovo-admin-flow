<template>
  <div class="iccid_list">
    <el-card class="search-card" style="margin-bottom: 20px" shadow="never">
      <el-form class="search-form" :inline="true" :model="formInline" size="small">
        <el-form-item label="起止日期">
          <el-date-picker v-model="formInline.date_start" :picker-options="startDatePicker" type="date" value-format="yyyy-MM-dd" placeholder="选择开始日期"></el-date-picker> -
          <el-date-picker v-model="formInline.date_end" :picker-options="endDatePicker" type="date" value-format="yyyy-MM-dd" placeholder="选择结束日期"></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchData">查询</el-button>
          <el-button type="warning" @click="resetData">重置</el-button>
          <el-button @click="$router.back()">返回</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="clearfix" shadow="never" v-loading="loadData">
      <h3 class="page-title">{{orgName}}--机构充值明细</h3>
      <el-table ref="listTable" @sort-change="handleSortChange" :data="list.data" :max-height="maxTableHeight" border resizable size="mini">
        <el-table-column prop="card_iccid" fixed="left" label="卡ICCID" min-width="200" sortable="custom">
          <template slot-scope="scope">
            <span class="btn-link" @click="$router.push({name: 'rechargeDetail', query: {card_id: scope.row.card_id}})">{{scope.row.card_iccid}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="pay_count" label="充值总次数" min-width="140" sortable="custom"></el-table-column>
        <el-table-column prop="gprs_amount" label="分配总流量" min-width="140" sortable="custom">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.gprs_amount)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="money_count" label="充值总金额" min-width="140" sortable="custom">
          <template slot-scope="scope">
            <div>￥{{scope.row.money_count|formatMoney}}</div>
          </template>
        </el-table-column>
        <el-table-column prop="money_rebate" label="返利总金额" min-width="140" sortable="custom">
          <template slot-scope="scope">
            <div>￥{{scope.row.money_rebate|formatMoney}}</div>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :page-sizes="pageSizes" :page-size="list.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="list.total" class="clearfix">
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
      sort: {},
      formInline: {
        org_id: Api.UNITS.getQuery('org_id')
      },
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
    searchData() {
      this.list.currentPage = 1
      this.getData()
    },
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
        url: _axios.ajaxAd.getReportPayDetail
      })
    },
    formatFlowUnit: Api.UNITS.formatFlowUnit,
    calcLeftTime: Api.UNITS.calcLeftTime
  },
  computed: {
    ...mapState({
      orgs: 'orgs'
    }),
    orgName() {
      let item = this.orgs.filter((v) => v.value == this.formInline.org_id)[0]
      return item && item.label
    },
    // 起始时间约数
    startDatePicker() {
      return Api.UNITS.startDatePicker(this, this.formInline.date_end)
    },
    // 结束时间约数
    endDatePicker() {
      return Api.UNITS.endDatePicker(this, this.formInline.date_start)
    }
  },
}

</script>
<style lang="scss">
.iccid_list {
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

  .page-title {
    font-size: 16px;
    margin: 0 0 10px 0;
    color: #333;
  }

  .el-date-editor .el-range-separator {
    width: auto;
  }
}

</style>
