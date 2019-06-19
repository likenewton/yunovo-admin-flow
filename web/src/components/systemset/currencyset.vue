<template>
  <div class="currency_set">
    <el-card class="clearfix" shadow="never" v-loading="loadData">
      <el-button-group style="margin-bottom: 10px">
        <el-button size="small" type="success" @click="$router.push({ name: 'createcurrency' })">新增</el-button>
      </el-button-group>
      <el-table ref="listTable" :data="list.data" @sort-change="handleSortChange" border resizable size="mini">
        <el-table-column prop="title" label="货币名称" min-width="150" sortable="custom">
          <template slot-scope="scope">
            <span>{{scope.row.title}}</span>
            <span v-if="scope.row.is_default" style="font-weight:bold">(默认)</span>
          </template>
        </el-table-column>
        <el-table-column prop="code" label="代码" min-width="140" sortable="custom"></el-table-column>
        <el-table-column prop="value" label="汇率" min-width="140" sortable="custom"></el-table-column>
        <el-table-column prop="date_modified" label="最近更新" min-width="140" sortable="custom"></el-table-column>
        <el-table-column label="管理" width="100">
          <template slot-scope="scope">
            <el-button type="text" class="text_editor" @click="editorData(scope)">编辑</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :page-sizes="pageSizes" :page-size="list.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="list.total" class="clearfix">
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
      pageSizes: Api.STATIC.pageSizes,
      // 列表
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
    handleSortChange(val = {}) {
      Api.UNITS.setSortSearch(val, this)
      this.getData()
    },
    editorData(scope) {
      this.$router.push({
        name: 'createcurrency',
        query: {
          type: 'update',
          currency_id: scope.row.currency_id
        }
      })
    },
    // 获取列表数据
    getData() {
      Api.UNITS.getListData({
        vue: this,
        url: _axios.ajaxAd.getCurrency
      })
    },
    formatFlowUnit: Api.UNITS.formatFlowUnit,
    calcLeftTime: Api.UNITS.calcLeftTime
  }
}

</script>
<style lang="scss">
.currency_set {
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
