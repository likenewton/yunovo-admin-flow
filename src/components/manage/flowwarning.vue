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
        <el-button size="mini" type="primary" @click="newAdd">新增</el-button>
      </el-button-group>
      <el-table v-loading="loadData" ref="multipleTable" :data="curTableData" border :default-sort="{prop: 'jg_name', order: 'descending'}" size="mini">
        <el-table-column show-overflow-tooltip prop="jg_name" label="机构名称" width="140"></el-table-column>
        <el-table-column show-overflow-tooltip label="预警流量" show-overflow-tooltip width="95" sortable>
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.warning_flow)"></div>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="blow_tpl" label="低于预警值通知模板" min-width="200"></el-table-column>
        <el-table-column show-overflow-tooltip prop="none_tpl" label="流量已用完通知模板" min-width="200"></el-table-column>
        <el-table-column show-overflow-tooltip prop="create_p" label="创建者" width="100" sortable></el-table-column>
        <el-table-column show-overflow-tooltip prop="chg_p" label="更改者" width="100" sortable></el-table-column>
        <el-table-column fixed="right" label="操作" width="80">
          <template slot-scope="scope">
            <el-button type="text" @click="$router.push({name: 'flowwarningset', query: {id: scope.row.id}})">编辑</el-button>
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
    // 新增流量预警
    newAdd() {
      this.$router.push({ name: 'flowwarningset' })
    },
    // 获取列表数据
    getData() {
      setTimeout(() => {
        // 数据请求成功
        this.tableData = [{
          id: 1,
          jg_name: '卡仕特-西格玛',
          warning_flow: 100,
          blow_tpl: '尊敬的客户，您的流量已低于100M，为了不影响您的正常使用，请及时充值。',
          none_tpl: '尊敬的客户，您的流量已用完，为了不影响您的正常使用，请及时充值。',
          create_p: 'Newton',
          chg_p: '樱木花道',
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
