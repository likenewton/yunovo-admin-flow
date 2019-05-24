<template>
  <div>
    <el-card class="box-card clearfix" shadow="never">
      <el-button-group style="margin-bottom: 10px">
        <el-button size="mini" type="success" @click="createLang">新增</el-button>
        <el-button size="mini" type="danger" @click="deleteData">删除</el-button>
      </el-button-group>
      <el-table class="payset_page" v-loading="loadData" ref="multipleTable" :data="curTableData" border @selection-change="handleSelectionChange" :default-sort="{prop: 'sort', order: 'descending'}" size="mini">
        <el-table-column fixed="left" type="selection" min-width="60"></el-table-column>
        <el-table-column show-overflow-tooltip label="语言名称" min-width="150">
          <template slot-scope="scope">
            <span>{{scope.row.lang_name}}</span>
            <span v-if="scope.row.isDefault" style="font-weight:bold">(默认)</span>
          </template>
        </el-table-column>
        <el-table-column fixed="right" prop="code" show-overflow-tooltip label="代码" min-width="140"></el-table-column>
        <el-table-column fixed="right" prop="sort" show-overflow-tooltip label="排序" min-width="140" sortable></el-table-column>
        <el-table-column fixed="right" show-overflow-tooltip label="管理" width="140">
          <template slot-scope="scope">
            <el-button type="text" @click="editor(scope.row.id)">编辑</el-button>
            <el-button type="text" @click="deleteSingle(scope.row.id)">删除</el-button>
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
      // 列表
      list: {
        data: [],
        pagesize: Api.STATIC.pageSizes[1],
        currentPage: 1,
        total: 0,
      },
      // 在列表中选择的数据
      selectData: [],
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
    // 处理列表多选
    handleSelectionChange(selectData) {
      this.selectData = selectData
    },
    createLang() {
      this.$router.push({ name: 'createlang' })
    },
    editor(id) {
      this.$router.push({ name: 'createlang', query: { type: 'update' } })
    },
    deleteSingle(id) {

    },
    // 批量卸载
    deleteData() {
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
      setTimeout(() => {
        // 数据请求成功
        this.list.data = [{
          id: 0,
          lang_name: 'English',
          code: 'en',
          sort: 0,
          isDefault: false
        }, {
          id: 1,
          lang_name: '简体中文',
          code: 'ch',
          sort: 1,
          isDefault: true
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

.payset_page {
  .pay_icon {
    font-size: 22px;
    vertical-align: middle;

    &.el-icon-fontalipay {
      color: #00aaee;
    }

    &.el-icon-fontweixinzhifu1 {
      color: #09bb07;
    }
  }

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
