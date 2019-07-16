<template>
  <div class="card_batch">
    <el-card class="clearfix" shadow="never" v-loading="loadData">
      <el-button-group style="margin-bottom: 10px">
        <el-button size="small" type="success" @click="$router.push({ name: 'batchcreate' })" :disabled="!pageAuthBtn.FCP_01_003_ADD01">新增</el-button>
      </el-button-group>
      <el-form :inline="true" :model="formInline" class="search-form" size="small">
        <el-form-item>
          <el-input v-model="formInline.batch_sn" placeholder="批次编号" @keyup.enter.native="searchData"></el-input>
        </el-form-item>
        <el-form-item>
          <el-select v-model="formInline.org_id" filterable clearable placeholder="机构名称" @change="searchData">
            <el-option v-for="(item, index) in orgs" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-date-picker v-model="formInline.date_start" :picker-options="startDatePicker" type="date" value-format="yyyy-MM-dd" @change="searchData" placeholder="添加时间开始"></el-date-picker> -
          <el-date-picker v-model="formInline.date_end" :picker-options="endDatePicker" type="date" value-format="yyyy-MM-dd" @change="searchData" placeholder="添加时间结束"></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchData" :disabled="!pageAuthBtn.FCP_01_003_CHECK01">查询</el-button>
          <el-button type="warning" @click="resetData" :disabled="!pageAuthBtn.FCP_01_003_CHECK01">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table ref="listTable" :data="list.data" @sort-change="handleSortChange" :max-height="maxTableHeight" border resizable size="mini">
        <el-table-column prop="batch_sn" label="批次编号" :width="widthMap.batch_sn[size]" sortable="custom"></el-table-column>
        <el-table-column prop="batch_name" label="批次名称" :min-width="widthMap.batch_name[size]" sortable="custom"></el-table-column>
        <el-table-column prop="org_id" label="机构名称" :min-width="widthMap.org_id[size]" sortable="custom">
          <template slot-scope="scope">
            <span>{{scope.row.org_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="gprs_amount" label="套餐流量" :width="widthMap.gprs_amount[size]" sortable="custom" align="right">
          <template slot-scope="scope">
            <div v-html="formatComboFlow(scope.row.gprs_amount)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="live_month" label="有效周期" :width="widthMap.live_month[size]" sortable="custom">
          <template slot-scope="scope">
            <div>{{getLiveMonthAlias(scope.row.live_month)}}</div>
          </template>
        </el-table-column>
        <el-table-column prop="card_amount" label="入卡数量" :width="widthMap.card_amount[size]" sortable="custom" align="right"></el-table-column>
        <el-table-column prop="batch_shipper" label="出货人名" :width="widthMap.batch_shipper[size]" sortable="custom"></el-table-column>
        <el-table-column label="销往城市" :width="widthMap.city[size]" show-overflow-tooltip>
          <template slot-scope="scope">
            <span>{{scope.row.province_name}}{{scope.row.city_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="batch_memo" label="批次备注" :width="widthMap.batch_memo[size]" sortable="custom" show-overflow-tooltip></el-table-column>
        <el-table-column prop="time_added" label="添加时间" :width="widthMap.time_added[size]" sortable="custom"></el-table-column>
        <el-table-column prop="user_id" label="操作者" :width="widthMap.user_id[size]" sortable="custom">
          <template slot-scope="scope">
            <span>{{scope.row.first_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="alter_id" label="更改者":width="widthMap.alter_id[size]" sortable="custom">
          <template slot-scope="scope">
            <span>{{scope.row.alter_name}}</span>
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="操作" :width="widthMap.op[size]" v-if="pageAuthBtn.FCP_01_003_UPDATE01">
          <template slot-scope="scope">
            <el-button type="text" class="text_editor" @click="$router.push({name: 'batchcreate', query:{type:'update', batch_id: scope.row.batch_id}})">编辑</el-button>
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
      size: Api.UNITS.getSize(),
      widthMap: {
        batch_sn: [105, 90],
        batch_name: [140, 110],
        org_id: [180, 140],
        gprs_amount: [105, 88],
        live_month: [120, 95],
        card_amount: [105, 88],
        batch_shipper: [105, 88],
        batch_memo: [150, 120],
        city: [140, 100],
        time_added: [153, 153],
        user_id: [90, 75],
        alter_id: [90, 75],
        op: [80, 55],
      }
    }
  },
  mounted() {
    this.getData()
  },
  methods: {
    getData() {
      Api.UNITS.getListData({
        vue: this,
        url: _axios.ajaxAd.getBatchs
      })
    },
    getLiveMonthAlias(value) {
      let item = this.liveMonthSelect.filter((v) => v.value == value)[0]
      return item ? item.label : ''
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
