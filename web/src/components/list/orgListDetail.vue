<template>
  <div>
    <el-card class="clearfix" shadow="never" v-loading="loadData">
      <el-form :inline="true" :model="formInline" class="search-form" size="small">
        <el-form-item>
          <el-select v-model="formInline.org_id" filterable clearable placeholder="机构名称" @change="searchData">
            <el-option v-for="(item, index) in orgs" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-select v-model="formInline.ntf_type" filterable clearable placeholder="通知或来源" @change="searchData">
            <el-option v-for="(item, index) in notifysFrom" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchData">查询</el-button>
          <el-button type="warning" @click="resetData">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table ref="listTable" @sort-change="handleSortChange" :max-height="maxTableHeight" :data="list.data" border resizable size="mini">
        <el-table-column prop="org_id" label="机构名称" min-width="160" sortable="custom">
          <template slot-scope="scope">
            <span class="btn-link" @click="$router.push({name: 'card', query: {org_id: scope.row.org_id}})">{{scope.row.org_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="ntf_type" label="通知或来源" min-width="150" sortable="custom">
          <template slot-scope="scope">
            <span>{{scope.row.ntf_type_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="ntf_succeed" label="通知成功数" min-width="102" sortable="custom" align="right"></el-table-column>
        <el-table-column prop="ntf_failed" label="通知失败数" min-width="102" sortable="custom" align="right"></el-table-column>
        <el-table-column prop="ntf_duration" label="通知时长值" min-width="130" sortable="custom" align="right">
          <template slot-scope="scope">
            <span>{{scope.row.ntf_duration|formatSecond}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="ord_succeed" label="下单成功数" min-width="102" sortable="custom" align="right"></el-table-column>
        <el-table-column prop="ord_failed" label="下单失败数" min-width="102" sortable="custom" align="right"></el-table-column>
        <el-table-column prop="ord_srate" label="下单成功比率" min-width="110" sortable="custom" align="right">
          <template slot-scope="scope">
            <span>{{scope.row.ord_srate}}%</span>
          </template>
        </el-table-column>
        <el-table-column prop="ord_frate" label="下单失败比率" min-width="110" sortable="custom" align="right">
          <template slot-scope="scope">
            <span>{{scope.row.ord_frate}}%</span>
          </template>
        </el-table-column>
        <el-table-column prop="money_succeed" label="已付金额" min-width="110" sortable="custom" align="right">
          <template slot-scope="scope">
            <div>￥{{scope.row.money_succeed|formatMoney}}</div>
          </template>
        </el-table-column>
        <el-table-column prop="money_failed" label="未付金额" min-width="110" sortable="custom" align="right">
          <template slot-scope="scope">
            <div>￥{{scope.row.money_failed|formatMoney}}</div>
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
      tabIndex: '0',
      formInline: {
        ntf_date: Api.UNITS.getQuery('ntf_date')
      }
    }
  },
  mounted() {
    this.getData()
  },
  methods: {
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
