<template>
  <div class="jg_recharge">
    <el-card class="clearfix" shadow="never" v-loading="loadData">
      <el-form class="search-form" :inline="true" :model="formInline" size="small">
        <el-form-item>
          <el-select v-model="formInline.org_id" filterable clearable placeholder="机构名称" @change="searchData">
            <el-option v-for="(item, index) in orgs" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-select v-model="formInline.pay_method" clearable placeholder="付款方式" @change="searchData">
            <el-option v-for="(item, index) in payMethodSelect" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-date-picker v-model="formInline.date_start" :picker-options="startDatePicker" type="date" value-format="yyyy-MM-dd" @change="searchData" placeholder="选择开始日期"></el-date-picker> -
          <el-date-picker v-model="formInline.date_end" :picker-options="endDatePicker" type="date" value-format="yyyy-MM-dd" @change="searchData" placeholder="选择结束日期"></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchData" :disabled="!pageAuthBtn.FCP_03_004_CHECK01">查询</el-button>
          <el-button type="warning" @click="resetData" :disabled="!pageAuthBtn.FCP_03_004_CHECK01">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table ref="listTable" @sort-change="handleSortChange" :data="list.data" :max-height="maxTableHeight" border resizable size="mini">
        <el-table-column prop="org_id" label="机构名称" min-width="200" sortable="custom">
          <template slot-scope="scope">
            <span v-if="scope.row.sums || !pageAuthBtn.FCP_03_004_LINK1">{{scope.row.org_name}}</span>
            <span v-else class="btn-link" @click="$router.push({name: 'rechargeParticulars', query: {org_id: scope.row.org_id}})">{{scope.row.org_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="pay_method" label="支付方式" min-width="100" sortable="custom">
          <template slot-scope="scope">
            <span>{{scope.row.pay_method_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="paid_amount" label="已付次数" min-width="100" sortable="custom" align="right"></el-table-column>
        <el-table-column prop="paid_money" label="已付金额" min-width="140" sortable="custom" align="right">
          <template slot-scope="scope">
            <div>￥{{scope.row.paid_money|formatMoney}}</div>
          </template>
        </el-table-column>
        <el-table-column label="已付款率" min-width="100" align="right">
          <template slot-scope="scope">
            <span>{{(scope.row.paid_amount/scope.row.paid_count*100).toFixed(3)}}%</span>
          </template>
        </el-table-column>
        <el-table-column prop="nopaid_amount" label="未付次数" min-width="100" sortable="custom" align="right"></el-table-column>
        <el-table-column prop="nopaid_money" label="未付金额" min-width="140" sortable="custom" align="right">
          <template slot-scope="scope">
            <div>￥{{scope.row.nopaid_money|formatMoney}}</div>
          </template>
        </el-table-column>
        <el-table-column label="未付款率" min-width="100" align="right">
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
      formInline: {
        org_id: Api.UNITS.getQuery('org_id')
      }
    }
  },
  mounted() {
    // 进入页面的时候请求数据
    this.getData()
  },
  methods: {
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
