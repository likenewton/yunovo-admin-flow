<template>
  <div class="flow_warning">
    <el-card class="search-card" style="margin-bottom: 20px" shadow="never">
      <el-form :inline="true" :model="formInline" class="search-form" size="small">
        <el-form-item label="机构名称">
          <el-select v-model="formInline.org_id" filterable clearable placeholder="请选择机构名称">
            <el-option v-for="(item, index) in orgs" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchData">查询</el-button>
          <el-button type="warning" @click="resetData">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="clearfix" shadow="never" v-loading="loadData">
      <el-button-group style="margin-bottom: 10px">
        <el-button size="small" type="success" @click="$router.push({name: 'flowwarningset'})">新增</el-button>
      </el-button-group>
      <el-table ref="listTable" :data="list.data" @sort-change="handleSortChange" :max-height="maxTableHeight" border resizable size="mini">
        <el-table-column prop="org_id" label="机构名称" width="140" sortable="custom">
          <template slot-scope="scope">
            <span>{{scope.row.org_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="warning_flow" label="预警流量" width="95" sortable="custom">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.warning_flow)"></div>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="blow_tpl" label="低于预警值通知模板" min-width="200" sortable="custom"></el-table-column>
        <el-table-column show-overflow-tooltip prop="none_tpl" label="流量已用完通知模板" min-width="200" sortable="custom"></el-table-column>
        <el-table-column prop="create_p" label="创建者" width="100" sortable="custom"></el-table-column>
        <el-table-column prop="chg_p" label="更改者" width="100" sortable="custom"></el-table-column>
        <el-table-column fixed="right" label="操作" width="80">
          <template slot-scope="scope">
            <el-button type="text" @click="$router.push({name: 'flowwarningset'})">编辑</el-button>
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
      loadData: true,
      pageSizes: Api.STATIC.pageSizes,
      maxTableHeight: Api.UNITS.maxTableHeight(),
      list: {
        data: [],
        pagesize: Api.STATIC.pageSizes[1],
        currentPage: 1,
        total: 0,
      },
      sort: {},
      formInline: {}
    }
  },
  mounted() {
    this.getData()
  },
  methods: {
    handleSizeChange(val) {
      this.list.pagesize = val
      this.list.currentPage = 1
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
    // 重置列表
    resetData() {
      this.list.currentPage = 1
      this.formInline = {} // 1、重置查询表单
      this.sort = {} // 2、重置排序
      this.$refs.listTable.clearSort() // 3、清空排序样式
      this.getData()
    },
    searchData() {
      this.list.currentPage = 1
      this.getData()
    },
    // 获取列表数据
    getData() {
      this.loadData = false
    },
    formatFlowUnit: Api.UNITS.formatFlowUnit,
    calcLeftTime: Api.UNITS.calcLeftTime
  },
  computed: {
    ...mapState({
      orgs: 'orgs'
    })
  }
}

</script>
<style lang="scss">
.flow_warning {
  .el-pagination {
    float: right;
    margin: 25px 40px 0 0;
  }

  td {
    * {
      font-size: 14px;
    }
  }

  .el-date-editor .el-range-separator {
    width: auto;
  }
}

</style>
