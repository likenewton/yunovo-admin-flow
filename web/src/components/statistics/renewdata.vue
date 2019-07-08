<template>
  <div>
    <el-card class="clearfix" shadow="never" v-loading="loadData">
      <el-form :inline="true" :model="formInline" class="search-form" size="small">
        <el-form-item>
          <el-select v-model="formInline.org_id" filterable clearable placeholder="机构名称" @change="searchData">
            <el-option v-for="(item, index) in orgs" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-date-picker v-model="formInline.date_start" :picker-options="startDatePicker" type="date" value-format="yyyy-MM-dd" @change="searchData" placeholder="开始日期"></el-date-picker> -
          <el-date-picker v-model="formInline.date_end" :picker-options="endDatePicker" type="date" value-format="yyyy-MM-dd" @change="searchData" placeholder="结束日期"></el-date-picker>
        </el-form-item>
        <el-form-item>
          <!-- <el-button type="primary" @click="searchData" :disabled="!pageAuthBtn.FCP_02_005_CHECK01">查询</el-button> -->
          <el-button type="warning" @click="resetData" :disabled="!pageAuthBtn.FCP_02_005_CHECK01">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table ref="listTable" @sort-change="handleSortChange" :max-height="maxTableHeight" :data="list.data" border resizable size="mini">
        <el-table-column prop="org_id" label="机构名称" min-width="180" sortable="custom">
          <template slot-scope="scope">
            <span v-if="pageAuthBtn.FCP_02_005_LINK01" class="btn-link" @click="$router.push({name: 'card', query: {org_id: scope.row.org_id}})">{{scope.row.org_name}}</span>
            <span v-else>{{scope.row.org_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="card_count" label="售卡数量" min-width="100" sortable="custom" align="right"></el-table-column>
        <el-table-column prop="act_count" label="激活卡数" min-width="100" sortable="custom" align="right"></el-table-column>
        <el-table-column prop="pay_cards" label="续费卡数" min-width="100" sortable="custom" align="right"></el-table-column>
        <el-table-column prop="pay_rate" label="续费比率" min-width="100" sortable="custom" align="right">
          <template slot-scope="scope">
            <span>{{scope.row.pay_rate ? scope.row.pay_rate.toFixed(3) : 0}}%</span>
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
    }
  },
  mounted() {
    this.getData()
  },
  methods: {
    getData() {
      Api.UNITS.getListData({
        vue: this,
        url: _axios.ajaxAd.getSell2pay
      })
    }
  },
  computed: {
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
