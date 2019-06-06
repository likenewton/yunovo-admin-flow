<template>
  <div class="pay_set">
    <el-card class="box-card clearfix" shadow="never" v-loading="loadData">
      <el-button-group style="margin-bottom: 10px">
        <el-button size="mini" type="danger" @click="deleteData">卸载</el-button>
      </el-button-group>
      <el-table class="payset_page" ref="listTable" @selection-change="handleSelectionChange" :data="list.data" border resizable size="mini">
        <el-table-column type="selection" min-width="60"></el-table-column>
        <el-table-column label="支付方式" min-width="150" sortable>
          <template slot-scope="scope">
            <i :class="scope.row.icon" class="pay_icon"></i>
            <span class="pointer" @click="openPayLink(scope)">{{scope.row.pay_way}}</span>
          </template>
        </el-table-column>
        <el-table-column label="管理" width="140">
          <template slot-scope="scope">
            <el-button type="text" @click="editor(scope.row.id)">编辑</el-button>
            <el-button type="text" @click="deleteSingle(scope.row.id)">卸载</el-button>
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
    // 处理列表多选
    handleSelectionChange(selectData) {
      this.selectData = selectData
    },
    editor(routeName) {
      this.$router.push({ name: routeName })
    },
    deleteSingle(id) {

    },
    // 批量卸载
    deleteData() {
      if (this.selectData.length === 0) {
        this.$message.warning('请先勾选要卸载的项')
      } else {
        this.$confirm(`您选中了${this.selectData.length}项，是否确认卸载?`, '提示', {
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
          id: 'alipay',
          pay_way: '支付宝支付',
          icon: 'el-icon-fontalipay',
          link: 'https://open.alipay.com/platform/home.htm'
        }, {
          id: 'wechart',
          pay_way: '微信支付',
          icon: 'el-icon-fontweixinzhifu1',
          link: 'https://pay.weixin.qq.com'
        }]
        this.list.total = this.list.data.length
        this.loadData = false
      }, 1000)
    },
    openPayLink(scope) {
      window.open(scope.row.link)
    },
    formatFlowUnit: Api.UNITS.formatFlowUnit,
    calcLeftTime: Api.UNITS.calcLeftTime
  }
}

</script>
<style lang="scss">
.pay_set {
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
}

</style>
