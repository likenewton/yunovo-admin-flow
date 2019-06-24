<template>
  <div class="card_reset">
    <el-card shadow="never">
      <el-tabs @tab-click="changeTab" v-model="tabIndex">
        <el-tab-pane>
          <span slot="label">重置操作</span>
          <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="126px" size="small">
            <el-form-item prop="iccid_list">
              <span slot="label">卡ICCID列表：</span>
              <el-input type="textarea" v-model="ruleForm.iccid_list" rows="8"></el-input>
              <div class="annotation">一行代表一个ICCID，多行代表多个ICCID，建议不超过100个ICCID</div>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitForm('ruleForm')">保存</el-button>
              <el-button type="warning" @click="resetForm('ruleForm')">重置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane v-loading="loadData">
          <span slot="label"></i>重置历史</span>
          <el-form class="search-form" :inline="true" :model="formInline" size="small">
            <el-form-item label="卡ICCID">
              <el-input v-model="formInline.card_iccid" @input="formInline.card_iccid = limitNumber(formInline.card_iccid, 20)" placeholder="请输入"></el-input>
            </el-form-item>
            <el-form-item label="机构名称">
              <el-select v-model="formInline.org_id" filterable placeholder="请选择">
                <el-option v-for="(item, index) in orgs" :key="index" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="重置时间">
              <el-date-picker v-model="formInline.date_start" :picker-options="startDatePicker" type="date" value-format="yyyy-MM-dd" placeholder="选择开始日期"></el-date-picker> -
              <el-date-picker v-model="formInline.date_end" :picker-options="endDatePicker" type="date" value-format="yyyy-MM-dd" placeholder="选择结束日期"></el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button size="small" type="primary" @click="searchData">查询</el-button>
              <el-button size="small" type="warning" @click="resetData">重置</el-button>
            </el-form-item>
          </el-form>
          <el-table ref="listTable" :data="list.data" @sort-change="handleSortChange" :max-height="maxTableHeight" border resizable size="mini">
            <el-table-column prop="card_iccid" fixed="left" label="卡ICCID" width="182" sortable="custom">
              <template slot-scope="scope">
                <span class="btn-link" @click="$router.push({ name: 'rechargeDetail', query: {card_id: scope.row.card_id}})">{{scope.row.card_iccid}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="org_id" label="机构名称" min-width="200">
              <template slot-scope="scope">
                <span>{{scope.row.org_name}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="card_used" label="已用流量" min-width="95" sortable="custom">
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.card_used)"></div>
              </template>
            </el-table-column>
            <el-table-column prop="card_unused" label="剩余流量" min-width="95" sortable="custom">
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.card_unused)"></div>
              </template>
            </el-table-column>
            <el-table-column prop="card_status" label="卡状态" min-width="95" sortable="custom">
              <template slot-scope="scope">
                <span v-if="scope.row.card_status==0">未激活</span>
                <span v-else-if="scope.row.card_status==1">正常</span>
                <span v-else>停卡</span>
              </template>
            </el-table-column>
            <el-table-column prop="owner_real" label="实名认证" min-width="95" sortable="custom">
              <template slot-scope="scope">
                <span v-if="scope.row.owner_real==1">已实名</span>
                <span v-else>未实名</span>
              </template>
            </el-table-column>
            <el-table-column prop="user_id" label="操作者" min-width="120" sortable="custom">
              <template slot-scope="scope">
                <span>{{scope.row.user_name}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="batch_time" label="出货时间" min-width="155" sortable="custom"></el-table-column>
            <el-table-column prop="time_added" label="重置时间" min-width="155" sortable="custom"></el-table-column>
          </el-table>
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :page-sizes="pageSizes" :page-size="list.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="list.total" class="clearfix">
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
      loadData: true,
      tabIndex: '1',
      pageSizes: Api.STATIC.pageSizes,
      maxTableHeight: Api.UNITS.maxTableHeight(370),
      list: {
        data: [],
        pagesize: Api.STATIC.pageSizes[1],
        currentPage: 1,
        total: 0,
      },
      ruleForm: {},
      formInline: {},
      sort: {},
      rules: {
        iccid_list: [{
          required: true,
          message: '请输入您需要重置的流量卡ICCID号',
          trigger: 'blur'
        }]
      }
    }
  },
  mounted() {
    this.getData()
  },
  methods: {
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
    changeTab(para) {
      if (this.tabIndex === '1') {
        // 重置历史查询
        if (this.list.data.length === 0) {
          this.getData()
        } else {
          this.loadData = false
        }
      }
    },
    // 重置列表
    resetData() {
      this.list.currentPage = 1
      this.formInline = {} // 1、重置查询表单
      this.sort = {} // 2、重置排序
      this.$refs.listTable.clearSort() // 3、清空排序样式
      this.getData()
    },
    searchData() {
      this.list.currentPage = 1
      this.getData()
    },
    getData() {
      Api.UNITS.getListData({
        vue: this,
        url: _axios.ajaxAd.getCardReset
      })
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
      })
    },
    // 重置表单
    resetForm(formName) {
      this.$refs[formName].resetFields()
      this.formInline = {}
    },
    formatFlowUnit: Api.UNITS.formatFlowUnit,
    limitNumber: Api.UNITS.limitNumber
  },
  computed: {
    ...mapState({
      orgs: 'orgs',
      cardTypes: 'cardTypes'
    }),
    // 起始时间约数
    startDatePicker() {
      return Api.UNITS.startDatePicker(this, this.formInline.date_end)
    },
    // 结束时间约数
    endDatePicker() {
      return Api.UNITS.endDatePicker(this, this.formInline.date_start)
    }
  }
}

</script>
<style lang="scss">
.card_reset {
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
