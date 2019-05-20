<template>
  <div>
    <el-card class="box-card clearfix" shadow="never">
      <h3 class="page-title">自营--机构充值明细</h3>
      <el-table v-loading="loadData" ref="multipleTable" :data="curTableData" border :default-sort="{prop: 'recharge_count', order: 'descending'}" size="mini">
        <el-table-column fixed="left" show-overflow-tooltip label="卡ICCID" width="200">
          <template slot-scope="scope">
            <el-button type="text" @click="showDetail(scope.row.iccid)">{{scope.row.iccid}}</el-button>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="recharge_count" label="充值总次数" min-width="140"></el-table-column>
        <el-table-column show-overflow-tooltip label="分配总流量" show-overflow-tooltip min-width="120">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.fptotal_flow)"></div>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip label="充值总金额" show-overflow-tooltip min-width="120">
          <template slot-scope="scope">
            <div>￥{{scope.row.recharge_money|formatMoney}}</div>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip label="返利总金额" show-overflow-tooltip min-width="120">
          <template slot-scope="scope">
            <div>￥{{scope.row.repay_money|formatMoney}}</div>
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
      tabIndex: '0',
      pageSizes: Api.STATIC.pageSizes,
      list: {
        data: [],
        pagesize: Api.STATIC.pageSizes[1],
        currentPage: 1,
        total: 0,
      },
      formInline: {}
    }
  },
  mounted() {
    // 进入页面的时候请求数据
    if (this.list.data.length === 0) {
      this.getData()
    } else {
      this.loadData = false
    }
  },
  methods: {
    routeName() {
      return this.$route.name
    },
    handleSizeChange(val) {
      this.list.pagesize = val
      this.getData()
    },
    handleCurrentChange(val) {
      this.list.currentPage = val
      this.getData()
    },
    showDetail(id) {
      this.$router.push({ name: 'rechargeDetail', query: {iccid: id} })
    },
    // 获取列表数据
    getData() {
      setTimeout(() => {
        // 数据请求成功
        this.list.data = [{
          id: 0,
          iccid: '89860617040000688970',
          recharge_count: 12,
          fptotal_flow: 548541,
          recharge_money: 6545.3,
          repay_money: 42.54
        }]
        this.list.total = this.list.data.length
        this.loadData = false
      }, 1000)
    },
    formatFlowUnit: Api.UNITS.formatFlowUnit,
    calcLeftTime: Api.UNITS.calcLeftTime
  },
  computed: {
    curTableData() {
      return this.list.data.slice((this.list.currentPage - 1) * this.list.pagesize, this.list.currentPage * this.list.pagesize)
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

.page-title {
  font-size: 16px;
  margin: 0 0 10px 0;
  color: #333;
}

.el-date-editor .el-range-separator {
  width: auto;
}

</style>
