<template>
  <div>
    <el-card class="box-card" style="margin-bottom: 20px" shadow="never">
      <el-form :inline="true" :model="formInline" class="demo-form-inline" size="small">
        <el-form-item label="卡ICCID">
          <el-input v-model="formInline.iccid" placeholder="请输入卡的iccid"></el-input>
        </el-form-item>
        <el-form-item label="卡商名称">
          <el-select v-model="formInline.card_type" placeholder="请选择">
            <el-option v-for="(item, index) in cardTypes" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="所属机构">
          <el-select v-model="formInline.jg_name" placeholder="请选择">
            <el-option label="机构1" value="0"></el-option>
            <el-option label="机构2" value="1"></el-option>
            <el-option label="机构3" value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="过期时间">
          <el-select v-model="formInline.ex_time" placeholder="请选择">
            <el-option label="2019-05" value="0"></el-option>
            <el-option label="2019-06" value="1"></el-option>
            <el-option label="2019-07" value="2"></el-option>
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
      <el-table ref="multipleTable" :data="list.data" @sort-change="handleSortChange" border size="mini">
        <el-table-column fixed="left" label="卡ICCID" width="180">
          <template slot-scope="scope">
            <el-button type="text">{{scope.row.iccid}}</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="ks_name" label="卡商名称" min-width="123"></el-table-column>
        <el-table-column label="所属机构" min-width="130">
          <template slot-scope="scope">
            <el-button type="text">{{scope.row.jg_name}}</el-button>
          </template>
        </el-table-column>
        <el-table-column label="当月用量" width="95">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.month_flow)"></div>
          </template>
        </el-table-column>
        <el-table-column label="累计用量" width="95">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.total_flow)"></div>
          </template>
        </el-table-column>
        <el-table-column label="剩余用量" width="95">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.left_flow)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="import_time" label="导卡时间" width="153" sortable="custom"></el-table-column>
        <el-table-column prop="active_time" label="激活时间" width="153" sortable="custom"></el-table-column>
        <el-table-column prop="eq_time" label="设备更新时间" width="153" sortable="custom"></el-table-column>
        <el-table-column prop="stop_time" label="上次停用时间" width="153" sortable="custom"></el-table-column>
        <el-table-column label="过期时间" width="205">
          <template slot-scope="scope">
            <div v-html="calcLeftTime(scope.row.ex_time)"></div>
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="98">
          <template slot-scope="scope">
            <el-button type="text">同步</el-button>
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
        url: _axios.ajaxAd.getStats
      })
    },
    formatFlowUnit: Api.UNITS.formatFlowUnit,
    calcLeftTime: Api.UNITS.calcLeftTime
  },
  computed: {
    ...mapState({
      cardTypes: 'cardTypes' // 卡商列表
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
