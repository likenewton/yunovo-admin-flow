<template>
  <div class="flow_migration">
    <el-card shadow="never">
      <el-tabs @tab-click="changeTab" v-model="tabIndex">
        <el-tab-pane>
          <span slot="label">流量迁移</span>
          <el-form :model="formInline" :rules="rules" ref="ruleForm" label-width="126px" size="small" :status-icon="true">
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
              <el-input v-model="searchForm.old_card_iccid" placeholder="请输入旧卡ICCID"></el-input>
            </el-form-item>
            <el-form-item label="新卡ICCID">
              <el-input v-model="searchForm.card_iccid" placeholder="请输入新卡ICCID"></el-input>
            </el-form-item>
            <el-form-item label="新卡机构">
              <el-select v-model="searchForm.org_id" filterable placeholder="请选择">
                <el-option v-for="(item, index) in orgs" :key="index" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="迁移时间">
              <el-date-picker v-model="searchForm.date_start" type="date" value-format="yyyy-MM-dd" placeholder="选择开始日期"></el-date-picker> -
              <el-date-picker v-model="searchForm.date_end" type="date" value-format="yyyy-MM-dd" placeholder="选择结束日期"></el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="searchData">查询</el-button>
              <el-button type="warning" @click="resetData">重置</el-button>
            </el-form-item>
          </el-form>
          <el-table v-loading="loadData" ref="listTable" :data="list.data" @sort-change="handleSortChange" :max-height="maxTableHeight" border resizable size="mini">
            <el-table-column prop="old_iccid" label="旧卡ICCID" min-width="180" sortable="custom">
              <template slot-scope="scope">
                <span class="btn-link" @click="toUnicomLink(scope.row.old_iccid)">{{scope.row.old_iccid}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="old_orgid" label="旧卡机构名称" min-width="160" sortable="custom">
              <template slot-scope="scope">
                <span>{{scope.row.old_org_name}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="new_iccid" label="新卡ICCID" min-width="180" sortable="custom">
              <template slot-scope="scope">
                <span class="btn-link"  @click="toUnicomLink(scope.row.new_iccid)">{{scope.row.new_iccid}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="new_orgid" label="新卡机构名称" min-width="160" sortable="custom">
              <template slot-scope="scope">
                <span>{{scope.row.org_name}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="move_memo" label="迁移备注" min-width="160" sortable="custom"></el-table-column>
            <el-table-column prop="user_id" label="操作者" min-width="80" sortable="custom">
              <template slot-scope="scope">
                <span>{{scope.row.first_name}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="time_added" label="迁移时间" min-width="155" sortable="custom"></el-table-column>
          </el-table>
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="list.currentPage" :page-sizes="pageSizes" :page-size="list.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="list.total">
          </el-pagination>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>
<script>
import Api from 'assets/js/api.js'
import { mapState } from 'vuex'

export default {
  data() {
    return {
      pageSizes: Api.STATIC.pageSizes,
      tabIndex: Api.UNITS.getQuery('tabIndex') || '0',
      loadData: true,
      maxTableHeight: Api.UNITS.maxTableHeight(),
      list: {
        data: [],
        pagesize: Api.STATIC.pageSizes[1],
        currentPage: 1,
        total: 0,
      },
      sort: {},
      formInline: {},
      searchForm: {
        old_card_iccid: Api.UNITS.getQuery('old_card_iccid'),
        card_iccid: Api.UNITS.getQuery('card_iccid')
      },
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
    this.getData()
  },
  methods: {
    changeTab(para) {
      // 当切换tab栏到'1'的时候，如果没有数据要加载数据
      if (this.tabIndex === '1') {
        // 这里应当是ajax请求数据
        if (this.list.data.length === 0) {
          this.getData()
        } else {
          this.loadData = false
        }
      }
    },
    handleSizeChange(val) {
      this.list.pagesize = val
      this.list.currentPage = 1
      this.getData()
    },
    handleCurrentChange(val) {
      this.list.currentPage = val
      this.getData()
    },
    handleSortChange(val) {
      Api.UNITS.setSortSearch(val, this)
      this.getData()
    },
    // 重置列表
    resetData() {
      this.list.currentPage = 1
      this.searchForm = {
        old_card_iccid: Api.UNITS.getQuery('old_card_iccid'),
        card_iccid: Api.UNITS.getQuery('card_iccid')
      } // 1、重置查询表单
      this.sort = {} // 2、重置排序
      this.$refs.listTable.clearSort() // 3、清空排序样式
      this.getData()
    },
    searchData() {
      this.list.currentPage = 1
      this.getData()
    },
    // 提交表单
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          // 验证通过
        } else {
          Api.UNITS.showMsgBox()
          return false;
        }
      });
    },
    // 重置表单
    resetForm(formName) {
      this.$refs[formName].resetFields()
    },
    checkRechargeDetail(scope) {
      this.$router.push({ name: 'rechargeDetail', query: { card_iccid: scope.row.card_iccid } })
    },
    getData() {
      Api.UNITS.getListData({
        vue: this,
        url: _axios.ajaxAd.getMoves,
        formInline: 'searchForm'
      })
    },
    formatFlowUnit: Api.UNITS.formatFlowUnit,
    limitNumber: Api.UNITS.limitNumber,
    toUnicomLink(iccid) {
      window.open(`http://t.gprs.yunovo.cn/app/main/info?iccid=${iccid}`)
    }
  },
  computed: {
    ...mapState({
      orgs: 'orgs',
    })
  }
}

</script>
<style lang="scss">
.flow_migration {
  .el-pagination {
    float: right;
    margin: 25px 40px 0 0;
  }

  .el-table {

    td {
      * {
        font-size: 14px;
      }
    }
  }

}

</style>
