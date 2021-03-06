<template>
  <div>
    <el-card class="box-card" style="margin-bottom: 20px" shadow="never">
      <el-form :inline="true" :model="formInline" class="demo-form-inline" size="small">
        <el-form-item label="机构名称">
          <el-select v-model="formInline.org_id" filterable clearable placeholder="请选择">
            <el-option v-for="(item, index) in orgs" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="通知或来源">
          <el-select v-model="formInline.ntf_type" filterable placeholder="请选择">
            <el-option v-for="(item, index) in notifysFrom" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchData">查询</el-button>
          <el-button type="warning" @click="resetData">重置</el-button>
          <el-button @click="$router.back()">返回</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="box-card clearfix" shadow="never" v-loading="loadData">
      <el-table ref="listTable" @sort-change="handleSortChange" :max-height="maxTableHeight" :data="list.data" border resizable size="mini">
        <el-table-column prop="org_id" label="机构名称" min-width="220" sortable="custom">
          <template slot-scope="scope">
            <span class="btn-link" @click="$router.push({name: 'card', query: {org_id: scope.row.org_id}})">{{scope.row.org_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="ntf_type" label="通知或来源" min-width="200" sortable="custom">
          <template slot-scope="scope">
            <span>{{scope.row.ntf_type_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="ntf_succeed" label="通知成功数" min-width="100" sortable="custom"></el-table-column>
        <el-table-column prop="ntf_failed" label="通知失败数" min-width="100" sortable="custom"></el-table-column>
        <el-table-column prop="ntf_duration" label="通知时长值" min-width="130" sortable="custom">
          <template slot-scope="scope">
            <span>{{scope.row.ntf_duration|formatSecond}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="ord_succeed" label="下单成功数" min-width="100" sortable="custom"></el-table-column>
        <el-table-column prop="ord_failed" label="下单失败数" min-width="100" sortable="custom"></el-table-column>
        <el-table-column prop="ord_srate" label="下单成功比率" min-width="115" sortable="custom">
          <template slot-scope="scope">
            <span>{{scope.row.ord_srate}}%</span>
          </template>
        </el-table-column>
        <el-table-column prop="ord_frate" label="下单失败比率" min-width="115" sortable="custom">
          <template slot-scope="scope">
            <span>{{scope.row.ord_frate}}%</span>
          </template>
        </el-table-column>
        <el-table-column prop="money_succeed" label="已付金额" min-width="100" sortable="custom">
          <template slot-scope="scope">
            <div>￥{{scope.row.money_succeed|formatMoney}}</div>
          </template>
        </el-table-column>
        <el-table-column prop="money_failed" label="未付金额" min-width="100" sortable="custom">
          <template slot-scope="scope">
            <div>￥{{scope.row.money_failed|formatMoney}}</div>
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
        ntf_date: Api.UNITS.getQuery('ntf_date')
      },
      maxTableHeight: Api.UNITS.maxTableHeight()
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
    handleSortChange(val) {
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
        ntf_date: Api.UNITS.getQuery('ntf_date')
      } // 1、重置查询表单
      this.sort = {} // 2、重置排序
      this.$refs.listTable.clearSort() // 3、清空排序样式
      this.getData()
    },
    // 获取列表数据
    getData() {
      Api.UNITS.getListData({
        vue: this,
        url: _axios.ajaxAd.getNotifyFromOrg
      })
    },
    formatFlowUnit: Api.UNITS.formatFlowUnit,
    calcLeftTime: Api.UNITS.calcLeftTime
  },
  computed: {
    ...mapState({
      orgs: 'orgs', // 机构列表
      notifysFrom: 'notifysFrom'
    })
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
