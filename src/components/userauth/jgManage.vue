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
        <el-button size="mini" type="success" @click="createJg">新增</el-button>
      </el-button-group>
      <el-table v-loading="loadData" ref="multipleTable" :data="curTableData" border @selection-change="handleSelectionChange" :default-sort="{prop: 'jg_name', order: 'descending'}" size="mini">
        <el-table-column fixed="left" type="selection" min-width="60"></el-table-column>
        <el-table-column show-overflow-tooltip label="机构名称" min-width="140" sortable>
          <template slot-scope="scope">
            <el-button type="text">{{scope.row.jg_name}}</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="coop_code" label="合作编号" show-overflow-tooltip min-width="125"></el-table-column>
        <el-table-column prop="coop_key" label="合作秘钥" show-overflow-tooltip min-width="300"></el-table-column>
        <el-table-column prop="acc_count" label="可开账号数量" show-overflow-tooltip width="95"></el-table-column>
        <el-table-column prop="e_mail" label="负责人邮箱" show-overflow-tooltip min-width="140"></el-table-column>
        <el-table-column prop="phone_number" label="负责人手机" show-overflow-tooltip width="110"></el-table-column>
        <el-table-column prop="repay_rate" label="返利比率" show-overflow-tooltip width="70"></el-table-column>
        <el-table-column prop="jg_desc" label="机构描述" show-overflow-tooltip min-width="160"></el-table-column>
        <el-table-column prop="create_p" label="创建者" show-overflow-tooltip min-width="80"></el-table-column>
        <el-table-column prop="update_p" label="更改者" show-overflow-tooltip min-width="80"></el-table-column>
        <el-table-column fixed="right" show-overflow-tooltip label="管理" min-width="60">
          <template slot-scope="scope">
            <el-button type="text" @click="updateJg">编辑</el-button>
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
    createJg() {
      this.$router.push({ name: 'createjg' })
    },
    updateJg() {
      this.$router.push({ name: 'createjg', query: { type: 'update' } })
    },
    // 获取列表数据
    getData() {
      setTimeout(() => {
        // 数据请求成功
        this.list.data = [{
          id: 0,
          jg_name: '卡仕特-西格玛',
          coop_code: 'mhn356uz28zclmw',
          coop_key: ' http://weixin.wenwen.vstchina.com/weixinYG/FocusOnServlet',
          acc_count: 4,
          e_mail: '52875440@qq.com',
          phone_number: '13286320200',
          repay_rate: 0,
          jg_desc: '深圳途乐车联网有限公司',
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
