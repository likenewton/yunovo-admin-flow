<template>
  <div class="iccid_list">
    <el-card class="clearfix" shadow="never" v-loading="loadData">
      <h3 class="page-title">{{orgName}}--机构充值明细</h3>
      <el-form class="search-form" :inline="true" :model="formInline" size="small">
        <el-form-item>
          <el-date-picker v-model="formInline.date_start" :picker-options="startDatePicker" type="date" value-format="yyyy-MM-dd" @change="searchData" placeholder="开始日期"></el-date-picker> -
          <el-date-picker v-model="formInline.date_end" :picker-options="endDatePicker" type="date" value-format="yyyy-MM-dd" @change="searchData" placeholder="结束日期"></el-date-picker>
        </el-form-item>
        <el-form-item>
          <!-- <el-button type="primary" @click="searchData">查询</el-button> -->
          <el-button type="warning" @click="resetData">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table ref="listTable" @sort-change="handleSortChange" :data="list.data" :max-height="maxTableHeight" border resizable size="mini">
        <el-table-column prop="card_iccid" fixed="left" label="卡ICCID" width="180" sortable="custom">
          <template slot-scope="scope">
            <span class="btn-link" @click="$router.push({name: 'rechargeDetail', query: {card_id: scope.row.card_id}})">{{scope.row.card_iccid}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="pay_count" label="充值总次数" min-width="140" sortable="custom" align="right"></el-table-column>
        <el-table-column prop="gprs_amount" label="分配总流量" min-width="140" sortable="custom" align="right">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.gprs_amount)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="money_count" label="充值总金额" min-width="140" sortable="custom" align="right">
          <template slot-scope="scope">
            <div>￥{{scope.row.money_count|formatMoney}}</div>
          </template>
        </el-table-column>
        <el-table-column prop="money_rebate" label="返利总金额" min-width="140" sortable="custom" align="right">
          <template slot-scope="scope">
            <div>￥{{scope.row.money_rebate|formatMoney}}</div>
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
        org_id: Api.UNITS.getQuery('org_id')
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
    }
  },
  computed: {
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
    display: inline-block;
    font-size: 16px;
    margin-top: 15px;
    color: #333;
  }

  .el-date-editor .el-range-separator {
    width: auto;
  }
}

</style>
