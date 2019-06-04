<template>
  <div>
    <el-card class="box-card" style="margin-bottom: 20px" shadow="never">
      <el-form :inline="true" :model="formInline" class="demo-form-inline" size="small">
        <el-form-item label="机构名称">
          <el-select v-model="formInline.org_id" filterable clearable placeholder="请选择">
            <el-option v-for="(item, index) in orgs" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getData">查询</el-button>
          <el-button type="warning" @click="resetData">重置</el-button>
          <el-button @click="$router.back()">返回</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="box-card clearfix" shadow="never" v-loading="loadData">
      <el-button-group style="margin-bottom: 10px">
        <el-button size="mini" type="warning">导出</el-button>
      </el-button-group>
      <el-table ref="listTable" @sort-change="handleSortChange" :max-height="maxTableHeight" :data="list.data" border resizable size="mini">
        <el-table-column prop="org_name" fixed="left" label="机构名称" min-width="160" sortable="custom">
          <template slot-scope="scope">
            <span class="btn-link">{{scope.row.org_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="1" label="售卡总数" min-width="87" sortable="custom"></el-table-column>
        <el-table-column prop="2" label="累充成数" min-width="87" sortable="custom"></el-table-column>
        <el-table-column prop="3" label="日充次数" min-width="87" sortable="custom"></el-table-column>
        <el-table-column prop="4" label="日充败数" min-width="87" sortable="custom"></el-table-column>
        <el-table-column prop="5" label="日充成数" min-width="87" sortable="custom"></el-table-column>
        <el-table-column prop="6" label="使用总数" min-width="87" sortable="custom"></el-table-column>
        <el-table-column prop="7" label="正常使用" min-width="87" sortable="custom"></el-table-column>
        <el-table-column prop="8" label="异常使用" min-width="87" sortable="custom"></el-table-column>
        <el-table-column prop="9" label="累计激活" min-width="87" sortable="custom"></el-table-column>
        <el-table-column prop="10" label="激活总数" min-width="87" sortable="custom"></el-table-column>
        <el-table-column prop="11" label="非设备激活" min-width="100" sortable="custom"></el-table-column>
        <el-table-column prop="12" label="设备端激活" min-width="100" sortable="custom"></el-table-column>
        <el-table-column prop="13" label="累计停卡" min-width="87" sortable="custom"></el-table-column>
        <el-table-column prop="14" label="停卡数量" min-width="87" sortable="custom"></el-table-column>
        <el-table-column prop="15" label="消耗流量" min-width="95" sortable="custom">
          <template slot-scope="scope">
            <span v-html="formatFlowUnit(scope.row['15'])"></span>
          </template>
        </el-table-column>
        <el-table-column prop="16" label="已付金额" min-width="87" sortable="custom"></el-table-column>
        <el-table-column prop="17" label="返利金额" min-width="87" sortable="custom"></el-table-column>

      </el-table>
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentPage" :page-sizes="pageSizes" :page-size="list.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="list.total" class="clearfix">
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
      tabIndex: '0',
      pageSizes: Api.STATIC.pageSizes,
      list: {
        data: [],
        pagesize: Api.STATIC.pageSizes[1],
        currentPage: 1,
        total: 0,
      },
      sort: {},
      formInline: {},
      maxTableHeight: Api.UNITS.maxTableHeight()
    }
  },
  mounted() {
    // 进入页面的时候请求数据
    this.getData()
  },
  methods: {
    handleSizeChange(val) {
      this.list.pagesize = val
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
    resetData() {
      this.formInline = {} // 1、重置查询表单
      this.sort = {} // 2、重置排序
      this.$refs.listTable.clearSort() // 3、清空排序样式
      this.getData()
    },
    // 获取列表数据
    getData() {
      Api.UNITS.getListData({
        vue: this,
        url: _axios.ajaxAd.getCardUsed
      })
    },
    formatFlowUnit: Api.UNITS.formatFlowUnit,
    calcLeftTime: Api.UNITS.calcLeftTime
  },
  computed: {
    ...mapState({
      orgs: 'orgs', // 机构列表
    })
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
