<template>
  <div class="card_batch">
    <el-card class="search-card" style="margin-bottom: 20px" shadow="never">
      <el-form :inline="true" :model="formInline" class="search-form" size="small">
        <el-form-item label="批次编号">
          <el-input v-model="formInline.batch_sn" placeholder="请输入批次编号"></el-input>
        </el-form-item>
        <el-form-item label="机构名称">
          <el-select v-model="formInline.org_id" filterable clearable placeholder="请选择机构名称">
            <el-option v-for="(item, index) in orgs" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="添加时间">
          <el-date-picker v-model="formInline.date_start" :picker-options="startDatePicker" type="date" value-format="yyyy-MM-dd" placeholder="选择开始日期"></el-date-picker> -
          <el-date-picker v-model="formInline.date_end" :picker-options="endDatePicker" type="date" value-format="yyyy-MM-dd" placeholder="选择结束日期"></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchData">查询</el-button>
          <el-button type="warning" @click="resetData">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="clearfix" shadow="never" v-loading="loadData">
      <el-button-group style="margin-bottom: 10px">
        <el-button size="small" type="success" @click="$router.push({ name: 'batchcreate' })">新增</el-button>
      </el-button-group>
      <el-table ref="listTable" :data="list.data" @sort-change="handleSortChange" :max-height="maxTableHeight" border resizable size="mini">
        <el-table-column prop="batch_sn" label="批次编号" width="105" sortable="custom"></el-table-column>
        <el-table-column prop="batch_name" label="批次名称" min-width="140" sortable="custom"></el-table-column>
        <el-table-column prop="org_id" label="机构名称" min-width="180" sortable="custom">
          <template slot-scope="scope">
            <span>{{scope.row.org_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="gprs_amount" label="套餐流量" width="105" sortable="custom">
          <template slot-scope="scope">
            <div v-html="formatComboFlow(scope.row.gprs_amount)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="live_month" label="有效周期" width="105" sortable="custom">
          <template slot-scope="scope">
            <div>{{getLiveMonthAlias(scope.row.live_month)}}</div>
          </template>
        </el-table-column>
        <el-table-column prop="card_amount" label="入卡数量" width="105" sortable="custom"></el-table-column>
        <el-table-column prop="batch_shipper" label="出货人名" width="105" sortable="custom"></el-table-column>
        <el-table-column label="销往城市" width="140">
          <template slot-scope="scope">
            <span>{{scope.row.province_name}}{{scope.row.city_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="batch_memo" label="批次备注" width="150" sortable="custom" show-overflow-tooltip></el-table-column>
        <el-table-column prop="time_added" label="添加时间" width="155" sortable="custom"></el-table-column>
        <el-table-column prop="user_id" label="操作者" width="90" sortable="custom">
          <template slot-scope="scope">
            <span>{{scope.row.first_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="alter_id" label="更改者" width="90" sortable="custom">
          <template slot-scope="scope">
            <span>{{scope.row.alter_name}}</span>
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="80">
          <template slot-scope="scope">
            <el-button type="text" class="text_editor">编辑</el-button>
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
import { mapState } from 'vuex'

export default {
  data() {
    return {
      loadData: true,
      pageSizes: Api.STATIC.pageSizes,
      maxTableHeight: Api.UNITS.maxTableHeight(),
      list: {
        data: [],
        pagesize: Api.STATIC.pageSizes[1],
        currentPage: 1,
        total: 0,
      },
      sort: {},
      formInline: {}
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
    // 获取列表数据
    getData() {
      Api.UNITS.getListData({
        vue: this,
        url: _axios.ajaxAd.getBatchs
      })
    },
    getLiveMonthAlias(value) {
      let item = this.liveMonthSelect.filter((v) => v.value == value)[0]
      return item ? item.label : ''
    },
    formatFlowUnit: Api.UNITS.formatFlowUnit,
    formatComboFlow: Api.UNITS.formatComboFlow,
  },
  computed: {
    ...mapState({
      orgs: 'orgs',
      liveMonthSelect: 'liveMonthSelect'
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
.card_batch {
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

  .el-date-editor .el-range-separator {
    width: auto;
  }
}

</style>
