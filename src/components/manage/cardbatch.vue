<template>
  <div>
    <el-card class="box-card" style="margin-bottom: 20px" shadow="never">
      <el-form :inline="true" :model="formInline" class="demo-form-inline" size="small">
        <el-form-item label="批次编号">
          <el-input v-model="formInline.bt_code" placeholder="请输入"></el-input>
        </el-form-item>
        <el-form-item label="机构名称">
          <el-select v-model="formInline.jg_name" placeholder="请选择">
            <el-option label="机构1" value="0"></el-option>
            <el-option label="机构2" value="1"></el-option>
            <el-option label="机构3" value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="添加时间">
          <el-date-picker v-model="formInline.add_time" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary">查询</el-button>
          <el-button type="warning">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="box-card clearfix" shadow="never">
      <el-button-group style="margin-bottom: 10px">
        <el-button size="mini" type="success" @click="createBatch">新增</el-button>
      </el-button-group>
      <el-table v-loading='loadData' ref="multipleTable" :data="curTableData" border :default-sort="{prop: 'exceed_time', order: 'descending'}" size="mini">
        <el-table-column show-overflow-tooltip prop="batch_code" label="批次编号" min-width="120"></el-table-column>
        <el-table-column show-overflow-tooltip prop="batch_name" label="批次名称" min-width="120"></el-table-column>
        <el-table-column show-overflow-tooltip prop="jg_name" label="机构名称" min-width="140"></el-table-column>
        <el-table-column show-overflow-tooltip label="套餐流量" show-overflow-tooltip min-width="95" sortable>
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.tc_flow)"></div>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip label="有效周期" show-overflow-tooltip min-width="95" sortable>
          <template slot-scope="scope">
            <div>{{scope.row.eff_pri}}</div>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="card_num" label="入卡数量" min-width="95" sortable></el-table-column>
        <el-table-column show-overflow-tooltip prop="ex_name" label="出货人名" min-width="110"></el-table-column>
        <el-table-column show-overflow-tooltip prop="city" label="销往城市" min-width="110"></el-table-column>
        <el-table-column show-overflow-tooltip prop="batch_remark" label="批次备注" min-width="220"></el-table-column>
        <el-table-column show-overflow-tooltip prop="add_time" label="添加时间" min-width="155" sortable></el-table-column>
        <el-table-column show-overflow-tooltip prop="op_p" label="操作者" min-width="110" sortable></el-table-column>
        <el-table-column show-overflow-tooltip prop="chg_p" label="更改者" min-width="110" sortable></el-table-column>
        <el-table-column fixed="right" label="操作" width="80">
          <template slot-scope="scope">
            <el-button type="text">编辑</el-button>
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
    createBatch() {
      this.$router.push({ name: 'batchcreate' })
    },
    // 获取列表数据
    getData() {
      setTimeout(() => {
        // 数据请求成功
        this.tableData = [{
          id: 0,
          batch_code: 'dfklj_d22',
          batch_name: 'lxdf_345',
          jg_name: '卡仕特-西格玛',
          tc_flow: 564,
          eff_pri: 15,
          card_num: 10,
          ex_name: 'Newton',
          city: '呼和浩特',
          batch_remark: '这个是一个备注',
          add_time: '2020-04-15 12:54:14',
          op_p: 'Newton',
          chg_p: '樱木花道'
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
