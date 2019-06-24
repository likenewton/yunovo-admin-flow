<template>
  <div class="combo_recharge">
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
        <el-table-column prop="gprs_amount" label="套餐流量" min-width="110" sortable="custom">
          <template slot-scope="scope">
            <span v-if="scope.row.sums">总计</span>
            <div v-else v-html="formatComboFlow(scope.row.gprs_amount)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="gprs_price" label="套餐金额" min-width="110" sortable="custom">
          <template slot-scope="scope">
            <div v-if="!scope.row.sums">￥{{scope.row.gprs_price|formatMoney}}</div>
          </template>
        </el-table-column>
        <el-table-column prop="paid_count" label="已付次数" min-width="100" sortable="custom"></el-table-column>
        <el-table-column prop="paid_money" label="已付金额" min-width="110" sortable="custom">
          <template slot-scope="scope">
            <div>￥{{scope.row.paid_money|formatMoney}}</div>
          </template>
        </el-table-column>
        <el-table-column label="已付款率" min-width="100" sortable="custom">
          <template slot-scope="scope">
            <span>{{(scope.row.paid_count/scope.row.pay_count*100).toFixed(3)}}%</span>
          </template>
        </el-table-column>
        <el-table-column prop="nopaid_count" label="未付次数" min-width="100" sortable="custom"></el-table-column>
        <el-table-column prop="nopaid_money" label="未付金额" min-width="110" sortable="custom">
          <template slot-scope="scope">
            <div>￥{{scope.row.nopaid_money|formatMoney}}</div>
          </template>
        </el-table-column>
        <el-table-column label="未付款率" min-width="100">
          <template slot-scope="scope">
            <span>{{(scope.row.nopaid_count/scope.row.pay_count*100).toFixed(3)}}%</span>
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
      maxTableHeight: Api.UNITS.maxTableHeight(360),
    }
  },
  mounted() {
    this.getData()
  },
  methods: {
    getData() {
      Api.UNITS.getListData({
        vue: this,
        url: _axios.ajaxAd.getpayPack,
        cb: (res) => {
          let other = res.data.other || {}
          let data = this.list.data || []
          if (data.length === 0) return
          data.push(...[{
            sums: true,
            gprs_amount: '总计',
            gprs_price: other.gprs_price,
            paid_count: other.paid_count,
            paid_money: other.paid_money,
            nopaid_count: other.nopaid_count,
            nopaid_money: other.nopaid_money,
            pay_count: other.pay_count
          }])
        }
      })
    }
  },
  computed: {
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
