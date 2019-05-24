<template>
  <div>
    <el-card class="box-card clearfix" shadow="never">
      <el-breadcrumb separator="/" style="margin-bottom: 15px">
        <el-breadcrumb-item>中华人民共和国</el-breadcrumb-item>
        <el-breadcrumb-item>湖南省</el-breadcrumb-item>
        <el-breadcrumb-item>衡阳市</el-breadcrumb-item>
      </el-breadcrumb>
      <el-button-group style="margin-bottom: 10px">
        <el-button size="mini" type="success" @click="createNation">新增</el-button>
        <el-button size="mini" type="danger" @click="deleteData">删除</el-button>
      </el-button-group>
      <el-table class="payset_page" v-loading="loadData" ref="multipleTable" :data="curTableData" border @selection-change="handleSelectionChange" size="mini">
        <el-table-column fixed="left" type="selection" min-width="60"></el-table-column>
        <el-table-column show-overflow-tooltip label="区域名称" min-width="150" sortable>
          <template slot-scope="scope">
            <el-button type="text">{{scope.row.area_name}}</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="postal_code" show-overflow-tooltip label="邮政编码" min-width="140"></el-table-column>
        <el-table-column show-overflow-tooltip label="编辑" width="150">
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
        pagesize: Api.STATIC.pageSizes[3],
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
    this.getData()
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
    createNation() {
      this.$router.push({ name: 'createnation' })
    },
    editor(id) {
      this.$router.push({
        name: 'createnation',
        query: {
          type: 'update',
          ntid: 23
        }
      })
    },
    deleteSingle(id) {},
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
          ntid: 10,
          parent_id: 1,
          area_name: '蒸湘区',
          postal_code: '430408',
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
