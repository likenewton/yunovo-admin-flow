<template>
  <div>
    <el-card class="box-card" style="margin-bottom: 20px" shadow="never">
      <el-form :inline="true" :model="formInline" class="demo-form-inline" size="small">
        <el-form-item label="卡ICCID">
          <el-input v-model="formInline.card_iccid" placeholder="请输入卡的iccid"></el-input>
        </el-form-item>
        <el-form-item label="卡商名称">
          <el-select v-model="formInline.card_type" filterable placeholder="请选择">
            <el-option v-for="(item, index) in cardTypes" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="机构名称">
          <el-select v-model="formInline.org_id" filterable placeholder="请选择">
            <el-option v-for="(item, index) in orgs" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="日差异流量">
          <el-select v-model="formInline.unicom_diff" placeholder="请选择">
            <el-option v-for="(item, index) in unicomDiffSelect" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="剩余流量">
          <el-select v-model="formInline.max_unused" placeholder="请选择">
            <el-option v-for="(item, index) in maxUnusedSelect" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getData">查询</el-button>
          <el-button type="warning" @click="formInline = {}">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="box-card clearfix" shadow="never" v-loading="loadData">
      <el-button-group style="margin-bottom: 10px">
        <el-button size="mini" type="warning">导出</el-button>
      </el-button-group>
      <el-table ref="multipleTable" @sort-change="handleSortChange" :data="list.data" :height="maxTableHeight" border size="mini">
        <el-table-column fixed="left" prop="card_iccid" label="卡ICCID" width="180">
          <template slot-scope="scope">
            <span class="btn-link">{{scope.row.card_iccid}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="card_type_name" label="卡商名称" min-width="150" sortable="custom"></el-table-column>
        <el-table-column prop="org_name" label="机构名称" min-width="180" sortable="custom">
          <template slot-scope="scope">
            <span class="btn-link">{{scope.row.org_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="unicom_diff" label="日差异流量" min-width="105" sortable="custom">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.unicom_diff)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="max_unused" label="剩余用量" min-width="95" sortable="custom">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.max_unused)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="used_total" label="平台使用总流量" min-width="130" sortable="custom">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.used_total)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="unicom_total" label="联通使用总流量" min-width="130" sortable="custom">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.unicom_total)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="time_last" label="设备更新时间" min-width="155" sortable="custom"></el-table-column>
        <el-table-column fixed="right" prop='unicom_stop' label="操作" width="95">
          <template slot-scope="scope">
            <el-button type="text" class="text_success" v-if="scope.row.unicom_stop == 1">启用</el-button>
            <el-button type="text" class="text_danger" v-else>停用</el-button>
            <el-button type="text">套餐</el-button>
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
import { mapState } from 'vuex'

export default {
  data() {
    return {
      loadData: true,
      pageSizes: Api.STATIC.pageSizes,
      list: {
        data: [],
        pagesize: Api.STATIC.pageSizes[1],
        currentPage: 1,
        total: 0,
      },
      sort: {},
      formInline: {},
      maxTableHeight: Api.UNITS.maxTableHeight()
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
    handleSortChange(val) {
      Api.UNITS.setSortSearch(val, this)
      this.getData()
    },
    // 获取列表数据
    getData() {
      Api.UNITS.getListData({
        vue: this,
        url: _axios.ajaxAd.getAbnormal
      })
    },
    formatFlowUnit: Api.UNITS.formatFlowUnit,
    calcLeftTime: Api.UNITS.calcLeftTime
  },
  computed: {
    ...mapState({
      cardTypes: 'cardTypes', // 卡商列表
      orgs: 'orgs', // 机构列表
      unicomDiffSelect: 'unicomDiffSelect', // 日差异流量
      maxUnusedSelect: 'maxUnusedSelect', // 剩余流量
    })
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
