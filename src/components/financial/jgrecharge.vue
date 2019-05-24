<template>
  <div>
    <el-card class="box-card clearfix" shadow="never">
      <el-form :inline="true" :model="formInline" class="demo-form-inline" size="small">
        <el-form-item label="机构名称">
          <el-select v-model="formInline.jg_name" placeholder="请选择">
            <el-option label="机构1" value="0"></el-option>
            <el-option label="机构2" value="1"></el-option>
            <el-option label="机构3" value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="付款方式">
          <el-select v-model="formInline.pay_way" placeholder="请选择">
            <el-option label="支付宝" value="0"></el-option>
            <el-option label="微信" value="1"></el-option>
            <el-option label="其他" value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="起止时间">
          <el-date-picker v-model="formInline.time" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary">查询</el-button>
          <el-button type="warning">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table v-loading="loadData" ref="multipleTable" :data="curTableData" border :default-sort="{prop: 'jg_name', order: 'descending'}" size="mini">
        <el-table-column show-overflow-tooltip label="机构名称" min-width="140" sortable>
          <template slot-scope="scope">
            <el-button type="text">{{scope.row.jg_name}}</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="pay_way" label="支付方式" show-overflow-tooltip min-width="95"></el-table-column>
        <el-table-column prop="pay_count" label="已付次数" show-overflow-tooltip min-width="95"></el-table-column>
        <el-table-column label="已付金额" show-overflow-tooltip min-width="110">
          <template slot-scope="scope">
            <div>￥{{scope.row.pay_money|formatMoney}}</div>
          </template>
        </el-table-column>
        <el-table-column prop="pay_rate" label="已付款率" show-overflow-tooltip min-width="95"></el-table-column>
        <el-table-column prop="nopay_count" label="未付次数" show-overflow-tooltip min-width="95"></el-table-column>
        <el-table-column label="未付金额" show-overflow-tooltip min-width="110">
          <template slot-scope="scope">
            <div>￥{{scope.row.nopay_money|formatMoney}}</div>
          </template>
        </el-table-column>
        <el-table-column prop="nopay_rate" label="未付款率" show-overflow-tooltip min-width="95"></el-table-column>
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
    // 获取列表数据
    getData() {
      setTimeout(() => {
        // 数据请求成功
        this.list.data = [{
          id: 0,
          jg_name: '卡仕特-西格玛',
          pay_way: '微信',
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

.el-date-editor .el-range-separator {
  width: auto;
}

</style>
