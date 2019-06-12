<template>
  <div class="currency_set">
    <el-card class="clearfix" shadow="never" v-loading="loadData">
      <el-button-group style="margin-bottom: 10px">
        <el-button size="mini" type="success" @click="$router.push({ name: 'createcurrency' })" icon="el-icon-circle-plus-outline">新增</el-button>
        <el-button size="mini" type="danger" @click="deleteDatas" icon="el-icon-delete">删除</el-button>
      </el-button-group>
      <el-table ref="listTable" :data="list.data" @selection-change="handleSelectionChange" @sort-change="handleSortChange" border resizable size="mini">
        <el-table-column type="selection" min-width="60"></el-table-column>
        <el-table-column prop="title" label="货币名称" min-width="150" sortable="custom">
          <template slot-scope="scope">
            <span>{{scope.row.title}}</span>
            <span v-if="scope.row.is_default" style="font-weight:bold">(默认)</span>
          </template>
        </el-table-column>
        <el-table-column prop="code" label="代码" min-width="140" sortable="custom"></el-table-column>
        <el-table-column prop="value" label="汇率" min-width="140" sortable="custom"></el-table-column>
        <el-table-column prop="date_modified" label="最近更新" min-width="140" sortable="custom"></el-table-column>
        <el-table-column label="管理" width="140">
          <template slot-scope="scope">
            <el-button type="text" class="text_editor" @click="editorData(scope)">编辑</el-button>
            <el-button type="text" class="text_danger" @click="deleteData(scope)">删除</el-button>
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
      // 在列表中选择的数据
      selectData: [],
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
    handleSelectionChange(selectData) {
      this.selectData = selectData
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
    deleteData(scope) {

    },
    // 批量卸载
    deleteDatas() {
      if (this.selectData.length === 0) {
        this.$message.warning('请先勾选要删除的项')
      } else {
        this.$confirm(`您选中了${this.selectData.length}项，是否确认删除?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$message({
            type: 'success',
            message: '删除成功!'
          });
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
        })
      }
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
