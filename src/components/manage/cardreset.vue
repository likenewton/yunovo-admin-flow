<template>
  <div>
    <el-card class="reset-card" shadow="never">
      <el-tabs @tab-click="changeTab">
        <el-tab-pane>
          <span slot="label">重置操作</span>
          <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="126px" class="demo-ruleForm" size="small">
            <el-form-item prop="iccid">
              <span slot="label">卡ICCID列表：</span>
              <el-input type="textarea" v-model="ruleForm.iccid" rows="8"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary">保存</el-button>
              <el-button type="warning">重置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane>
          <span slot="label"></i>重置历史</span>
          <el-form class="search-form" :inline="true" :model="formInline" size="small">
            <el-form-item label="卡ICCID">
              <el-input v-model="formInline.iccid" placeholder="请输入"></el-input>
            </el-form-item>
            <el-form-item label="机构名称">
              <el-select v-model="formInline.jg_name" placeholder="请选择">
                <el-option label="机构1" value="0"></el-option>
                <el-option label="机构2" value="1"></el-option>
                <el-option label="机构3" value="2"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="重置时间">
              <el-date-picker v-model="formInline.reset_time" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button size="small" type="primary">查询</el-button>
              <el-button size="small" type="warning">重置</el-button>
            </el-form-item>
          </el-form>
          <el-table v-loading="loadTab1Data" ref="multipleTable" :data="curTableData" border :default-sort="{prop: 'ex_time', order: 'descending'}" size="mini">
            <el-table-column fixed="left" show-overflow-tooltip label="卡ICCID" min-width="170">
              <template slot-scope="scope">
                <el-button type="text">{{scope.row.iccid}}</el-button>
              </template>
            </el-table-column>
            <el-table-column show-overflow-tooltip prop="jg_name" label="机构名称" min-width="140"></el-table-column>
            <el-table-column label="已用流量" show-overflow-tooltip min-width="95" sortable>
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.right_use)"></div>
              </template>
            </el-table-column>
            <el-table-column label="剩余流量" show-overflow-tooltip min-width="95" sortable>
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.left_use)"></div>
              </template>
            </el-table-column>
            <el-table-column prop="card_status" label="卡状态" show-overflow-tooltip min-width="95" sortable></el-table-column>
            <el-table-column prop="real_ide" label="实名认证" show-overflow-tooltip min-width="95" sortable></el-table-column>
            <el-table-column prop="op_p" label="操作者" show-overflow-tooltip min-width="120" sortable></el-table-column>
            <el-table-column prop="ex_time" label="出货时间" show-overflow-tooltip min-width="155" sortable></el-table-column>
            <el-table-column prop="reset_time" label="重置时间" show-overflow-tooltip min-width="155" sortable></el-table-column>
          </el-table>
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentPage" :page-sizes="pageSizes" :page-size="pagesize" layout="total, sizes, prev, pager, next, jumper" :total="tableData.length">
          </el-pagination>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>
<script>
import Api from 'assets/js/api.js'

export default {
  data() {
    return {
      routeName: this.$route.name,
      currentDate: new Date(),
      tableData: [],
      pageSizes: Api.STATIC.pageSizes,
      pagesize: Api.STATIC.pageSizes[1],
      currentPage: 1,
      loadTab1Data: true,
      tabIndex: '0',
      formInline: {
        doi: '',
        status: ''
      },
      ruleForm: {},
      rules: {
        iccid: [{
          required: true,
          message: '请输入您需要重置的流量卡ICCID号',
          trigger: 'blur'
        }]
      }
    }
  },
  mounted() {

  },
  methods: {
    handleSizeChange(val) {
      this.pagesize = val
    },
    handleCurrentChange(val) {
      this.currentPage = val
    },
    changeTab(para) {
      this.tabIndex = para.index
      // 当切换tab栏到'1'的时候，要加载数据
      if (this.tabIndex === '1') {
        // 这里应当是ajax请求数据
        if (this.tableData.length === 0) {
          this.getTab1Data()
        } else {
          this.loadTab1Data = false
        }
      }
    },
    getTab1Data() {
      setTimeout(() => {
        this.tableData = [{
          id: 0,
          iccid: '8986011670901045280',
          jg_name: '卡仕特-西格玛',
          right_use: 214,
          left_use: 6542,
          card_status: 0,
          real_ide: 1,
          op_p: 'Newton',
          ex_time: '2019-01-21 09:58:45',
          reset_time: '2019-02-03 21:01:03'
        }]
        this.loadTab1Data = false
      }, 1000)
    },
    formatFlowUnit: Api.UNITS.formatFlowUnit
  },
  computed: {
    curTableData() {
      return this.tableData.slice((this.currentPage - 1) * this.pagesize, this.currentPage * this.pagesize)
    }
  }
}

</script>
<style lang="scss">
.el-pagination {
  float: right;
  margin: 25px 40px 0 0;
}

.el-card__body {
  img {
    width: 100%;
  }

  .small {
    font-size: 12px;
  }

}

</style>
