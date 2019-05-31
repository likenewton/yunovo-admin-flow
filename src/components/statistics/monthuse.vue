<template>
  <div class="month_used">
    <el-card style="margin-bottom: 20px" shadow="never">
      <el-form :inline="true" :model="formInline" class="demo-form-inline" size="small">
        <el-form-item label="卡ICCID">
          <el-input v-model="formInline.card_iccid" placeholder="请输入卡的iccid"></el-input>
        </el-form-item>
        <el-form-item label="卡商名称">
          <el-select v-model="formInline.card_type" filterable placeholder="请选择">
            <el-option v-for="(item, index) in cardTypes" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="所属机构">
          <el-select v-model="formInline.org_id" filterable placeholder="请选择">
            <el-option v-for="(item, index) in orgs" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="月份">
          <el-select v-model="formInline.mdate" filterable placeholder="请选择">
            <el-option v-for="(item, index) in months" :key="index" :label="item.label" :value="item.value"></el-option>
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
        <el-table-column fixed="left" label="卡ICCID" width="200">
          <template slot-scope="scope">
            <span class="btn-link">{{scope.row.card_iccid}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="card_type_name" label="卡商名称" min-width="120" sortable="custom"></el-table-column>
        <el-table-column prop="org_name" label="所属机构" min-width="160" sortable="custom">
          <template slot-scope="scope">
            <span class="btn-link">{{scope.row.org_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="time_active" label="卡激活时间" width="158" sortable="custom"></el-table-column>
        <el-table-column prop="how_month" label="月份" width="100" sortable="custom"></el-table-column>
        <el-table-column label="月使用流量" width="110">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.month_used)"></div>
          </template>
        </el-table-column>
      </el-table>
      <el-row class="result-list">
        <span class="result-item">流量使用小计：<span v-html="formatFlowUnit(usedPage)"></span></span>
        <span class="result-item">流量使用总计：<span v-html="formatFlowUnit(usedTotal)"></span></span>
        <span class="result-item">流量使用平均：<span v-html="formatFlowUnit(avaused)"></span></span>
      </el-row>
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
        pagesize: Api.STATIC.pageSizes[3],
        currentPage: 1,
        total: 0,
      },
      usedTotal: 0, // 总使用流量
      sort: {},
      formInline: {},
      months: [] // 下拉列表月份
    }
  },
  mounted() {
    // 进入页面的时候请求数据
    this.getMonths()
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
      this.loadData = true
      Api.UNITS.getListData({
        vue: this,
        url: _axios.ajaxAd.getStats,
        cb: (res) => {
          this.loadData = false
          this.list = Object.assign(this.list, {
            data: res.data.page.records || [],
            total: res.data.page.total
          })
          console.log(res.data)
          this.usedTotal = res.data.other.usedTotal
        }
      })
    },
    getMonths() {
      _axios.send({
        method: 'get',
        url: _axios.ajaxAd.getMonths,
        done: (res) => {
          this.months = res.data
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
    }),
    // 流量使用均值
    avaused() {
      return this.usedTotal ? this.usedTotal / this.list.total : 0
    },
    // 流量使用小计(该页面总计)
    usedPage() {
      let sum = 0
      this.list.data.forEach((v) => {
        sum += v.month_used
      })
      return sum
    }
  }
}

</script>
<style lang="scss">
.month_used {
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

  .result-list {
    margin: 0 auto;
    text-align: right;
    .result-item {
      display: inline-block;
      padding: 10px 15px;
    }
  }


  .el-date-editor .el-range-separator {
    width: auto;
  }
}

</style>
