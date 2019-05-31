<template>
  <div>
    <el-card class="box-card" style="margin-bottom: 20px" shadow="never">
      <el-form :inline="true" :model="formInline" class="demo-form-inline" size="small">
        <el-form-item label="卡ICCID">
          <el-input v-model="formInline.card_iccid" placeholder="请输入卡的iccid"></el-input>
        </el-form-item>
        <el-form-item label="卡商名称">
          <el-select v-model="formInline.card_type" placeholder="请选择">
            <el-option v-for="(item, index) in cardTypes" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="机构名称">
          <el-select v-model="formInline.org_id" placeholder="请选择">
            <el-option v-for="(item, index) in orgs" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="日差异流量">
          <el-select v-model="formInline.ex_time" placeholder="请选择">
            <el-option label=">=150M" value="0"></el-option>
            <el-option label=">=500M" value="1"></el-option>
            <el-option label=">=1G" value="2"></el-option>
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
      <el-table ref="multipleTable" @sort-change="handleSortChange" :data="list.data" border size="mini">
        <el-table-column fixed="left" label="卡ICCID" width="180">
          <template slot-scope="scope">
            <el-button type="text">{{scope.row.iccid}}</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="ks_name" label="卡商名称" min-width="140"></el-table-column>
        <el-table-column label="机构名称" min-width="140">
          <template slot-scope="scope">
            <el-button type="text">{{scope.row.jg_name}}</el-button>
          </template>
        </el-table-column>
        <el-table-column label="日差异流量" min-width="95">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.daydif_flow)"></div>
          </template>
        </el-table-column>
        <el-table-column label="剩余用量" min-width="95">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.left_flow)"></div>
          </template>
        </el-table-column>
        <el-table-column label="平台使用总流量" min-width="110">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.plattotal_flow)"></div>
          </template>
        </el-table-column>
        <el-table-column label="联通使用总流量" min-width="110">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.unicomtotal_flow)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="eq_time" label="设备更新时间" min-width="155" sortable="custom"></el-table-column>
        <el-table-column fixed="right" label="操作" width="95">
          <template slot-scope="scope">
            <el-button type="text">套餐</el-button>
            <el-button type="text" v-if="scope.row.is_op">停用</el-button>
            <el-button type="text" v-else>启用</el-button>
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
      tabIndex: '0',
      pageSizes: Api.STATIC.pageSizes,
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
    handleSortChange(val) {
      Api.UNITS.setSortSearch(val, this)
      this.getData()
    },
    // 获取列表数据
    getData() {
      Api.UNITS.getListData({
        vue: this,
        url: _axios.ajaxAd.getStats
      })
      // setTimeout(() => {
      //   // 数据请求成功
      //   this.list.data = [{
      //     id: 0,
      //     iccid: '89860617040000312399',
      //     ks_name: '智网科技 JASPER',
      //     jg_name: '卡仕特-西格玛',
      //     daydif_flow: 1245,
      //     left_flow: 98542,
      //     plattotal_flow: 54657554,
      //     unicomtotal_flow: 75452454,
      //     eq_time: '2019-03-25 12:52:10',
      //     is_op: true
      //   }]
      //   this.list.total = this.list.data.length
      //   this.loadData = false
      // }, 1000)
    },
    formatFlowUnit: Api.UNITS.formatFlowUnit,
    calcLeftTime: Api.UNITS.calcLeftTime
  },
  computed: {
    ...mapState({
      cardTypes: 'cardTypes', // 卡商列表
      orgs: 'orgs', // 机构列表
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
