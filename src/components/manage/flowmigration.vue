<template>
  <div>
    <el-card class="reset-card" shadow="never">
      <el-tabs @tab-click="changeTab">
        <el-tab-pane>
          <span slot="label">流量迁移</span>
          <el-form :model="formInline" :rules="rules" ref="ruleForm" label-width="126px" class="demo-ruleForm" size="small" :status-icon="true">
            <el-form-item prop="old_iccid">
              <span slot="label">旧卡ICCID：</span>
              <el-input v-model="formInline.old_iccid" placeholder="请输入旧卡ICCID"></el-input>
            </el-form-item>
            <el-form-item prop="new_iccid">
              <span slot="label">新卡ICCID：</span>
              <el-input v-model="formInline.new_iccid" placeholder="请输入新卡ICCID"></el-input>
            </el-form-item>
            <el-form-item prop="migration_remark">
              <span slot="label">迁移备注：</span>
              <el-input type="textarea" v-model="formInline.migration_remark" placeholder="" rows="4"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitForm('ruleForm')">保存</el-button>
              <el-button type="warning" @click="resetForm('ruleForm')">重置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane>
          <span slot="label"></i>历史迁移</span>
          <el-form class="search-form" :inline="true" :model="searchForm" size="small">
            <el-form-item label="旧卡ICCID">
              <el-input v-model="searchForm.old_iccid" placeholder="请输入旧卡ICCID"></el-input>
            </el-form-item>
            <el-form-item label="新卡ICCID">
              <el-input v-model="searchForm.new_iccid" placeholder="请输入新卡ICCID"></el-input>
            </el-form-item>
            <el-form-item label="新卡机构">
              <el-select v-model="searchForm.jg_name" placeholder="请选择">
                <el-option label="机构1" value="0"></el-option>
                <el-option label="机构2" value="1"></el-option>
                <el-option label="机构3" value="2"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="迁移时间">
              <el-date-picker v-model="searchForm.migration_time" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button size="small" type="primary">查询</el-button>
              <el-button size="small" type="warning">重置</el-button>
            </el-form-item>
          </el-form>
          <el-table v-loading="loadTab1Data" ref="multipleTable" :data="curTableData" border :default-sort="{prop: 'migration_time', order: 'descending'}" size="mini">
            <el-table-column show-overflow-tooltip label="旧卡ICCID" min-width="170">
              <template slot-scope="scope">
                <el-button type="text" @click="checkRechargeDetail(scope.row.old_iccid)">{{scope.row.old_iccid}}</el-button>
              </template>
            </el-table-column>
            <el-table-column show-overflow-tooltip prop="oldjg_name" label="旧卡机构名称" min-width="160"></el-table-column>
            <el-table-column show-overflow-tooltip label="新卡ICCID" min-width="170">
              <template slot-scope="scope">
                <el-button type="text" @click="checkRechargeDetail(scope.row.new_iccid)">{{scope.row.new_iccid}}</el-button>
              </template>
            </el-table-column>
            <el-table-column show-overflow-tooltip prop="newjg_name" label="新卡机构名称" min-width="160"></el-table-column>
            <el-table-column show-overflow-tooltip prop="migration_remark" label="迁移备注" min-width="160"></el-table-column>
            <el-table-column show-overflow-tooltip prop="op_p" label="操作者" min-width="80"></el-table-column>
            <el-table-column show-overflow-tooltip prop="migration_time" label="迁移时间" min-width="155" sortable></el-table-column>
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
      pageSizes: Api.STATIC.pageSizes,
      pagesize: Api.STATIC.pageSizes[1],
      currentPage: 1,
      tabIndex: '0',
      loadTab1Data: true,
      tableData: [], // tab1中列表数据
      formInline: {}, // tab0中表单数据
      searchForm: {}, // tab0中查询表单数据
      rules: {
        old_iccid: [{
          required: true,
          message: '请输入旧卡ICCID',
          trigger: 'blur'
        }],
        new_iccid: [{
          required: true,
          message: '请输入新卡ICCID',
          trigger: 'blur'
        }]
      }
    }
  },
  mounted() {

  },
  methods: {
    // 提交表单
    submitForm(formName) {
      console.log(this.formInline)
      this.$refs[formName].validate((valid) => {
        if (valid) {
          // 验证通过
        } else {
          return false;
        }
      });
    },
    // 重置表单
    resetForm(formName) {
      // resetFields 只能重置需要验证的值
      this.$refs[formName].resetFields()
    },
    changeTab(para) {
      this.tabIndex = para.index
      // 当切换tab栏到'1'的时候，如果没有数据要加载数据
      if (this.tabIndex === '1') {
        // 这里应当是ajax请求数据
        if (this.tableData.length === 0) {
          this.getTab1Data()
        } else {
          this.loadTab1Data = false
        }
      }
    },
    handleSizeChange(val) {
      this.pagesize = val
    },
    handleCurrentChange(val) {
      this.currentPage = val
    },
    checkRechargeDetail(iccid) {
      this.$router.push({ name: 'rechargeDetail', query: { iccid } })
    },
    getTab1Data() {
      setTimeout(() => {
        this.tableData = [{
          id: 0,
          old_iccid: '8986011670901045280',
          oldjg_name: '卡仕特-西格玛',
          new_iccid: '8986011670901045281',
          newjg_name: '威仕特 > 威仕特定向流量',
          migration_remark: 'Newton',
          op_p: 'Lucy',
          migration_time: '2019-01-21 09:58:45'
        }]
        this.loadTab1Data = false
      }, 1000)
    },
    formatFlowUnit: Api.UNITS.formatFlowUnit,
    limitNumber: Api.UNITS.limitNumber,
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
