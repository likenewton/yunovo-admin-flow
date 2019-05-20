<template>
  <div>
    <el-card class="box-card" style="margin-bottom: 20px" shadow="never">
      <el-form :inline="true" :model="formInline" class="demo-form-inline" size="small">
        <el-form-item label="机构名称">
          <el-select v-model="formInline.jg_name" placeholder="请选择">
            <el-option label="机构1" value="0"></el-option>
            <el-option label="机构2" value="1"></el-option>
            <el-option label="机构3" value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary">查询</el-button>
          <el-button type="warning">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="box-card clearfix" shadow="never">
      <el-button-group style="margin-bottom: 10px">
        <el-button size="mini" type="success" @click="createRechargeComboSet">新增</el-button>
      </el-button-group>
      <el-table v-loading="loadData" ref="multipleTable" :data="curTableData" border :default-sort="{prop: 'exceed_time', order: 'descending'}" size="mini">
        <el-table-column show-overflow-tooltip prop="jg_name" label="机构名称" min-width="140"></el-table-column>
        <el-table-column show-overflow-tooltip label="套餐流量" show-overflow-tooltip min-width="95" sortable>
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.tc_flow)"></div>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="tc_price" label="套餐价格" min-width="75"></el-table-column>
        <el-table-column show-overflow-tooltip prop="fp_month" label="分配月数" min-width="75"></el-table-column>
        <el-table-column show-overflow-tooltip label="月均流量" show-overflow-tooltip min-width="95" sortable>
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.tc_flow/scope.row.fp_month)"></div>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="is_clear" label="是否清零" min-width="75"></el-table-column>
        <el-table-column show-overflow-tooltip label="有效周期" show-overflow-tooltip min-width="95" sortable>
          <template slot-scope="scope">
            <div>{{scope.row.eff_pri}}</div>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="tc_desc" label="套餐描述" min-width="180" sortable></el-table-column>
        <el-table-column show-overflow-tooltip prop="rebate_money" label="返利金额" min-width="95" sortable></el-table-column>
        <el-table-column show-overflow-tooltip prop="is_recommend" label="是否推荐" min-width="95" sortable></el-table-column>
        <el-table-column show-overflow-tooltip prop="add_time" label="添加时间" min-width="155" sortable></el-table-column>
        <el-table-column show-overflow-tooltip prop="change_time" label="更改时间" min-width="155" sortable></el-table-column>
        <el-table-column show-overflow-tooltip prop="create_p" label="创建者" min-width="100" sortable></el-table-column>
        <el-table-column show-overflow-tooltip prop="chg_p" label="更改者" min-width="100" sortable></el-table-column>
        <el-table-column fixed="right" label="操作" width="105">
          <template slot-scope="scope">
            <el-button type="text">编辑</el-button>
            <el-button type="text" v-if="scope.row.is_op">停用</el-button>
            <el-button type="text" v-else>启用</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentPage" :page-sizes="pageSizes" :page-size="pagesize" layout="total, sizes, prev, pager, next, jumper" :total="tableData.length" class="clearfix">
      </el-pagination>
    </el-card>
  </div>
</template>
<script>
import Api from 'assets/js/api.js'

export default {
  data() {
    return {
      routeName: this.$route.name,
      currentPage: 1,
      pageSizes: Api.STATIC.pageSizes,
      pagesize: Api.STATIC.pageSizes[1],
      loadData: true,
      tabIndex: '0',
      tableData: [],
      formInline: {}
    }
  },
  mounted() {
    // 进入页面的时候请求数据
    if (this.tableData.length === 0) {
      this.getData()
    } else {
      this.loadData = false
    }
  },
  methods: {
    handleSizeChange(val) {
      this.pagesize = val
    },
    handleCurrentChange(val) {
      this.currentPage = val
    },
    createRechargeComboSet() {
      this.$router.push({ name: 'rechargecomboset' })
    },
    // 获取列表数据
    getData() {
      setTimeout(() => {
        // 数据请求成功
        this.tableData = [{
          id: 0,
          jg_name: '卡仕特-西格玛',
          tc_flow: 564,
          tc_price: '￥1.25',
          fp_month: 12,
          month_flow: 0, // tc_flow / fp_month
          is_clear: 0,
          eff_pri: 15,
          tc_desc: '2G畅享月叠加包1个月有效，流量月结清零，可叠加购买，全国通用；流量卡只允许在指定的设备端使用。',
          rebate_money: '￥0.00',
          is_recommend: 0,
          add_time: '2019-01-15 09:52:12',
          change_time: '2020-04-15 12:54:14',
          create_p: 'Newton',
          chg_p: '樱木花道',
          is_op: 0
        }, {
          id: 1,
          jg_name: '卡仕特-西格玛',
          tc_flow: 56454,
          tc_price: '￥1.25',
          fp_month: 36,
          month_flow: 0, // tc_flow / fp_month
          is_clear: 0,
          eff_pri: 15,
          tc_desc: '2G畅享月叠加包1个月有效，流量月结清零，可叠加购买，全国通用；流量卡只允许在指定的设备端使用。',
          rebate_money: '￥0.00',
          is_recommend: 1,
          add_time: '2019-01-15 09:52:12',
          change_time: '2020-04-15 12:54:14',
          create_p: 'Newton',
          chg_p: '樱木花道',
          is_op: 1
        }]
        this.loadData = false
      }, 1000)
    },
    formatFlowUnit: Api.UNITS.formatFlowUnit,
    calcLeftTime: Api.UNITS.calcLeftTime
  },
  computed: {
    curTableData() {
      return this.tableData.slice((this.currentPage - 1) * this.pagesize, this.currentPage * this.pagesize)
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
