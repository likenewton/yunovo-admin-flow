<template>
  <div>
    <el-card class="box-card" style="margin-bottom: 20px" shadow="never">
      <el-form :inline="true" :model="formInline" class="demo-form-inline" size="small">
        <el-form-item label="用户名">
          <el-input type="text" placeholder="请输入"></el-input>
        </el-form-item>
        <el-form-item label="所属群组">
          <el-select v-model="formInline.pay_way" placeholder="请选择">
            <el-option label="群组1" value="0"></el-option>
            <el-option label="群组2" value="1"></el-option>
            <el-option label="群组3" value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="所属机构">
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
        <el-button size="mini" type="success" @click="createuser">新增</el-button>
        <el-button size="mini" type="danger" @click="deleteData">删除</el-button>
      </el-button-group>
      <el-table v-loading="loadData" ref="multipleTable" :data="curTableData" border @selection-change="handleSelectionChange" :default-sort="{prop: 'user_name', order: 'descending'}" size="mini">
        <el-table-column fixed="left" type="selection" min-width="60"></el-table-column>
        <el-table-column show-overflow-tooltip prop="user_name" label="用户名" show-overflow-tooltip min-width="90" sortable></el-table-column>
        <el-table-column show-overflow-tooltip prop="real_name" label="真实姓名" show-overflow-tooltip min-width="90"></el-table-column>
        <el-table-column show-overflow-tooltip prop="belong_group" label="所属群组" show-overflow-tooltip min-width="120"></el-table-column>
        <el-table-column show-overflow-tooltip label="所属机构" min-width="140" sortable>
          <template slot-scope="scope">
            <el-button type="text">{{scope.row.jg_name}}</el-button>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="create_time" label="创建时间" show-overflow-tooltip min-width="153"></el-table-column>
        <el-table-column show-overflow-tooltip prop="update_time" label="更新时间" show-overflow-tooltip min-width="153"></el-table-column>
        <el-table-column show-overflow-tooltip prop="login_ip" label="最后登录IP" show-overflow-tooltip min-width="250"></el-table-column>
        <el-table-column fixed="right" show-overflow-tooltip label="管理" min-width="100">
          <template slot-scope="scope">
            <el-button type="text" @click="updateUser">编辑</el-button>
            <el-button type="text" v-if="scope.row.is_op">停用</el-button>
            <el-button type="text" v-else>启用</el-button>
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
    createuser() {
      this.$router.push({ name: 'createuser' })
    },
    updateUser() {
      this.$router.push({ name: 'createuser', query: { type: 'update' } })
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
          user_name: 'liudong',
          real_name: '柳栋',
          belong_group: ' 管理员－超级组',
          jg_name: '卡仕特-西格玛',
          create_time: '2019-01-12 12:56:00',
          update_time: '2019-01-12 12:56:00',
          login_ip: '广东省深圳市 BGP BGROUND 120.79.62.140',
          is_op: true
        }, {
          id: 1,
          user_name: 'yinyi',
          real_name: '尹怡',
          belong_group: ' 管理员－超级组',
          jg_name: '卡仕特-西格玛',
          create_time: '2019-01-12 12:56:00',
          update_time: '2019-01-12 12:56:00',
          login_ip: '广东省深圳市 BGP BGROUND 120.79.62.140',
          is_op: true
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
