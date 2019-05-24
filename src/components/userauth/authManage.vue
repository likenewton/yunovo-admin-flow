<template>
  <div>
    <el-card class="box-card clearfix" shadow="never">
      <el-button-group style="margin-bottom: 10px">
        <el-button size="mini" type="success" @click="createAuth">新增</el-button>
        <el-button size="mini" type="danger" @click="deleteData">删除</el-button>
      </el-button-group>
      <el-table v-loading="loadData" ref="multipleTable" :data="curTableData" border @selection-change="handleSelectionChange" :default-sort="{prop: 'authgroup_name', order: 'descending'}" size="mini">
        <el-table-column fixed="left" type="selection" min-width="60"></el-table-column>
        <el-table-column prop="authgroup_name" label="权限组名称" show-overflow-tooltip min-width="150" sortable></el-table-column>
        <el-table-column prop="authgroup_desc" label="权限组描述" show-overflow-tooltip min-width="300"></el-table-column>
        <el-table-column prop="add_time" label="添加时间" show-overflow-tooltip min-width="150"></el-table-column>
        <el-table-column prop="update_time" label="更改时间" show-overflow-tooltip min-width="150"></el-table-column>
        <el-table-column prop="create_p" label="创建者" show-overflow-tooltip min-width="80"></el-table-column>
        <el-table-column prop="update_p" label="更改者" show-overflow-tooltip min-width="80"></el-table-column>
        <el-table-column fixed="right" show-overflow-tooltip label="管理" min-width="60">
          <template slot-scope="scope">
            <el-button type="text" @click="updateAuth">编辑</el-button>
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
    createAuth() {
      this.$router.push({ name: 'createauth' })
    },
    updateAuth() {
      this.$router.push({ name: 'createauth', query: { type: 'update' } })
    },
    // 删除数据
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
          authgroup_name: '管理员－超级组',
          authgroup_desc: '拥有最高权限，主管生杀大权，至高权利！',
          add_time: '2019-05-12 21:10:01',
          update_time: '2019-05-12 21:10:01',
          create_p: 'Newton',
          update_p: 'Newton'
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
