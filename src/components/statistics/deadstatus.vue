<template>
  <div>
    <el-card class="box-card" style="margin-bottom: 20px" shadow="never">
      <el-form :inline="true" :model="formInline" class="search-form" size="small">
        <el-form-item label="卡ICCID">
          <el-input v-model="formInline.card_iccid" placeholder="请输入卡的iccid"></el-input>
        </el-form-item>
        <el-form-item label="卡商名称">
          <el-select v-model="formInline.card_type" filterable clearable placeholder="请选择">
            <el-option v-for="(item, index) in cardTypes" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="所属机构">
          <el-select v-model="formInline.org_id" filterable clearable placeholder="请选择">
            <el-option v-for="(item, index) in orgs" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否过期">
          <el-select v-model="formInline.time_expire" filterable clearable placeholder="请选择">
            <el-option v-for="(item, index) in exceedSelect" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchData">查询</el-button>
          <el-button type="warning" @click="resetData">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="box-card clearfix" shadow="never" v-loading="loadData">
      <el-button-group style="margin-bottom: 10px">
        <el-button size="mini" type="warning">导出</el-button>
      </el-button-group>
      <el-table ref="listTable" :data="list.data" @sort-change="handleSortChange" :max-height="maxTableHeight" border resizable size="mini">
        <el-table-column fixed="left" prop="card_iccid" label="卡ICCID" width="178" sortable="custom">
          <template slot-scope="scope">
            <span v-if="scope.row.sums">{{scope.row.card_iccid}}</span>
            <span v-else class="btn-link">{{scope.row.card_iccid}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="card_id" label="卡商名称" min-width="120" sortable="custom">
          <template slot-scope="scope">
            <span>{{scope.row.card_type_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="org_id" label="所属机构" min-width="120" sortable="custom">
          <template slot-scope="scope" prop="org_name">
            <span class="btn-link">{{scope.row.org_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="used_month" label="当月用量" width="95" sortable="custom">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.used_month)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="used_total" label="累计用量" width="95" sortable="custom">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.used_total)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="max_unused" label="剩余用量" width="95" sortable="custom">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.max_unused)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="time_added" label="导卡时间" width="153" sortable="custom"></el-table-column>
        <el-table-column prop="time_active" label="激活时间" width="153" sortable="custom"></el-table-column>
        <el-table-column prop="time_last" label="设备更新时间" width="153" sortable="custom"></el-table-column>
        <el-table-column prop="time_stop" label="上次停用时间" width="153" sortable="custom"></el-table-column>
        <el-table-column prop="time_expire" label="过期时间" width="210">
          <template slot-scope="scope">
            <div v-if="!scope.row.sums" v-html="calcLeftTime(scope.row.time_expire)"></div>
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="98">
          <template slot-scope="scope" v-if="!scope.row.sums">
            <el-button type="text">同步</el-button>
            <el-button type="text" class="text_success" v-if="scope.row.unicom_stop">开启</el-button>
            <el-button type="text" class="text_danger" v-else>停用</el-button>
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
    // 查询
    searchData() {
      this.list.currentPage = 1
      this.getData()
    },
    resetData() {
      this.list.currentPage = 1
      this.formInline = {} // 1、重置查询表单
      this.sort = {} // 2、重置排序
      this.$refs.listTable.clearSort() // 3、清空排序样式
      this.getData()
    },
    // 获取列表数据
    getData() {
      Api.UNITS.getListData({
        vue: this,
        url: _axios.ajaxAd.getHalt,
        cb: (res) => {
          let other = res.data.other || {}
          if (this.list.data.length === 0) return
          // 一下计算合计
          this.list.data.push(...[{
            sums: true,
            card_iccid: '小计',
            used_month: Api.UNITS.pageSums(this.list.data, 'used_month'),
            max_unused: Api.UNITS.pageSums(this.list.data, 'max_unused'),
            used_total: Api.UNITS.pageSums(this.list.data, 'used_total')
          }, {
            sums: true,
            card_iccid: '总计',
            used_month: other.used_month || 0,
            used_total: other.used_total || 0,
            max_unused: other.max_unused || 0
          }])
        }
      })
    },
    formatFlowUnit: Api.UNITS.formatFlowUnit,
    calcLeftTime: Api.UNITS.calcLeftTime
  },
  computed: {
    ...mapState({
      cardTypes: 'cardTypes', // 卡商列表
      orgs: 'orgs', // 机构列表
      exceedSelect: 'exceedSelect'
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
