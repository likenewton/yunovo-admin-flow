<template>
  <div>
    <el-card class="clearfix" shadow="never" v-loading="loadData">
      <el-button-group style="margin-bottom: 10px">
        <el-button size="small" type="warning">导出</el-button>
      </el-button-group>
      <el-form :inline="true" :model="formInline" class="search-form" size="small" @submit.native.prevent>
        <el-form-item>
          <el-select v-model="formInline.org_id" filterable clearable placeholder="机构名称" @change="searchData">
            <el-option v-for="(item, index) in orgs" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <!-- <el-button type="primary" @click="searchData">查询</el-button> -->
          <el-button type="warning" @click="resetData">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table ref="listTable" @sort-change="handleSortChange" :max-height="maxTableHeight" :data="list.data" border resizable size="mini">
        <el-table-column prop="org_id" fixed="left" label="机构名称" min-width="126" sortable="custom">
          <template slot-scope="scope">
            <span class="btn-link" @click="$router.push({name: 'card', query: {org_id: scope.row.org_id}})">{{scope.row.org_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="card_total" label="售卡总数" min-width="87" sortable="custom" align="right"></el-table-column>
        <el-table-column prop="pay_total" label="累充成数" min-width="87" sortable="custom" align="right"></el-table-column>
        <el-table-column prop="pay_count" label="日充次数" min-width="87" sortable="custom" align="right"></el-table-column>
        <el-table-column prop="pay_failed" label="日充败数" min-width="87" sortable="custom" align="right"></el-table-column>
        <el-table-column prop="pay_succeed" label="日充成数" min-width="87" sortable="custom" align="right"></el-table-column>
        <el-table-column label="使用总数" min-width="73" align="right">
          <template slot-scope="scope">
            <span>{{scope.row.online_api + scope.row.online_other}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="online_api" label="正常使用" min-width="87" sortable="custom" align="right"></el-table-column>
        <el-table-column prop="online_other" label="异常使用" min-width="87" sortable="custom" align="right"></el-table-column>
        <el-table-column prop="active_total" label="累计激活" min-width="87" sortable="custom" align="right"></el-table-column>
        <el-table-column prop="active_count" label="激活总数" min-width="87" sortable="custom" align="right"></el-table-column>
        <el-table-column label="非设备激活" min-width="82" align="right">
          <template slot-scope="scope">
            <span>{{scope.row.active_count - scope.row.active_device}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="active_device" label="设备端激活" min-width="100" sortable="custom" align="right"></el-table-column>
        <el-table-column prop="stop_total" label="累计停卡" min-width="87" sortable="custom" align="right"></el-table-column>
        <el-table-column prop="stop_count" label="停卡数量" min-width="87" sortable="custom" align="right"></el-table-column>
        <el-table-column prop="used_amount" label="消耗流量" min-width="95" sortable="custom" align="right">
          <template slot-scope="scope">
            <span v-html="formatFlowUnit(scope.row.used_amount)"></span>
          </template>
        </el-table-column>
        <el-table-column prop="pay_succeed_money" label="已付金额" min-width="100" sortable="custom" align="right">
          <template slot-scope="scope">
            <span>￥{{scope.row.pay_succeed_money|formatMoney}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="rebate_money" label="返利金额" min-width="87" sortable="custom" align="right">
          <template slot-scope="scope">
            <span>￥{{scope.row.rebate_money|formatMoney}}</span>
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
      tabIndex: '0',
      formInline: {
        stats_date: Api.UNITS.getQuery('stats_date')
      }
    }
  },
  mounted() {
    this.getData()
  },
  methods: {
    resetData() {
      this.list.currentPage = 1
      this.formInline = {
        stats_date: Api.UNITS.getQuery('stats_date')
      } // 1、重置查询表单
      this.sort = {} // 2、重置排序
      this.$refs.listTable.clearSort() // 3、清空排序样式
      this.getData()
    },
    // 获取列表数据
    getData() {
      Api.UNITS.getListData({
        vue: this,
        url: _axios.ajaxAd.getPayOnlineOrg
      })
    }
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
