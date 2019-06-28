<template>
  <div class="month_used">
    <el-card class="search-card" style="margin-bottom: 20px" shadow="never">
      <el-form :inline="true" :model="formInline" class="search-form" size="small">
        <el-form-item label="卡ICCID">
          <el-input v-model="formInline.card_iccid" @input="formInline.card_iccid = limitNumber(formInline.card_iccid, 20)" placeholder="请输入卡的iccid"></el-input>
        </el-form-item>
        <el-form-item label="卡商名称">
          <el-select v-model="formInline.card_type" filterable clearable placeholder="请选择卡商">
            <el-option v-for="(item, index) in cardTypes" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="所属机构">
          <el-select v-model="formInline.org_id" filterable clearable placeholder="请选择机构">
            <el-option v-for="(item, index) in orgs" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="月份">
          <el-select v-model="formInline.mdate" filterable clearable placeholder="请选择月份">
            <el-option v-for="(item, index) in months" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchData">查询</el-button>
          <el-button type="warning" @click="resetData">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="clearfix" shadow="never" v-loading="loadData">
      <el-button-group style="margin-bottom: 10px">
        <el-button size="small" type="warning" @click="exportExcel">导出</el-button>
      </el-button-group>
      <el-table class="list_table" ref="listTable" @sort-change="handleSortChange" :data="list.data" :max-height="maxTableHeight" border resizable size="mini">
        <el-table-column prop="card_iccid" fixed="left" label="卡ICCID" width="200">
          <template slot-scope="scope">
            <span v-if="scope.row.sums">{{scope.row.card_iccid}}</span>
            <span v-else class="btn-link" @click="$router.push({ name: 'rechargeDetail', query: {card_id: scope.row.card_id}})">{{scope.row.card_iccid}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="card_id" label="卡商名称" min-width="120" sortable="custom">
          <template slot-scope="scope">
            <span>{{scope.row.card_type_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="org_id" label="所属机构" min-width="160" sortable="custom">
          <template slot-scope="scope">
            <span class="btn-link" @click="$router.push({name: 'card', query: {org_id: scope.row.org_id}})">{{scope.row.org_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="time_active" label="卡激活时间" width="155" sortable="custom"></el-table-column>
        <el-table-column prop="how_month" label="月份" width="120" sortable="custom"></el-table-column>
        <el-table-column prop="month_used" label="月使用流量" width="120" align="right">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.month_used)"></div>
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
      usedTotal: 0, // 总使用流量
    }
  },
  mounted() {
    // 进入页面的时候请求数据
    this.getData()
  },
  methods: {
    getData() {
      Api.UNITS.getListData({
        vue: this,
        url: _axios.ajaxAd.getStats,
        cb: (res) => {
          this.usedTotal = res.data.other ? res.data.other.usedTotal : 0
          if (this.list.data.length === 0) return
          this.list.data.push(...[{
            sums: true,
            card_iccid: '总平均',
            month_used: this.avaused
          }, {
            sums: true,
            card_iccid: '小计',
            month_used: Api.UNITS.pageSums(this.list.data, 'month_used')
          }, {
            sums: true,
            card_iccid: '总计',
            month_used: this.usedTotal
          }])
        }
      })
    },
    // 导出excel
    exportExcel() {
      Api.UNITS.exportExcel(_axios.ajaxAd.statsExport, this.formInline)
    }
  },
  computed: {
    // 流量使用均值
    avaused() {
      return this.usedTotal ? this.usedTotal / this.list.total : 0
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
