<template>
  <div class="card_combo">
    <el-card style="margin-bottom: 20px" shadow="never">
      <el-form :inline="true" :model="formInline" class="search-form" size="small">
        <el-form-item label="机构名称">
          <el-select v-model="formInline.org_id" filterable placeholder="请选择">
            <el-option v-for="(item, index) in orgs" :key="index" :label="item.label" :value="item.value"></el-option>
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
        <el-button size="mini" type="success" @click="$router.push({ name: 'rechargecomboset' })">新增</el-button>
      </el-button-group>
      <el-table ref="listTable" :data="list.data" @sort-change="handleSortChange" :max-height="maxTableHeight" border resizable size="mini">
        <el-table-column prop="org_id" label="机构名称" min-width="140" sortable="custom">
          <template slot-scope="scope">
            <span>{{scope.row.org_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="gprs_amount" label="套餐流量" min-width="90" sortable="custom">
          <template slot-scope="scope">
            <div v-html="formatComboFlow(scope.row.gprs_amount)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="gprs_price" label="套餐价格" min-width="90" sortable="custom">
          <template slot-scope="scope">
            <span>￥{{formatMoney(scope.row.gprs_price)}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="allot_month" label="分配月数" min-width="90" sortable="custom"></el-table-column>
        <el-table-column prop="allot_value" label="月均流量" min-width="90" sortable="custom">
          <template slot-scope="scope">
            <div v-html="formatComboFlow(scope.row.allot_value)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="allot_reset" label="是否清零" min-width="90" sortable="custom">
          <template slot-scope="scope">
            <span v-if="scope.row.allot_reset==1">会清零</span>
            <span v-else>不清零</span>
          </template>
        </el-table-column>
        <el-table-column prop="live_month" label="有效周期" min-width="110" sortable="custom">
          <template slot-scope="scope">
            <div>{{getLiveMonthAlias(scope.row.live_month)}}</div>
          </template>
        </el-table-column>
        <el-table-column label="套餐描述" min-width="250">
          <template slot-scope="scope">
            <div>
              <span>{{scope.row.pack_name}}</span>
            </div>
            <div style="font-size: 12px; line-height: 16px">{{scope.row.pack_memo}}</div>
          </template>
        </el-table-column>
        <el-table-column prop="pack_rebate" label="返利金额" min-width="95" sortable="custom">
          <template slot-scope="scope">
            <span>￥{{formatMoney(scope.row.pack_rebate)}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="pack_recom" label="是否推荐" min-width="95" sortable="custom">
          <template slot-scope="scope">
            <span v-if="scope.row.pack_recom==1">是</span>
            <span v-else>否</span>
          </template>
        </el-table-column>
        <el-table-column prop="time_added" label="添加时间" min-width="155" sortable="custom"></el-table-column>
        <el-table-column prop="time_modify" label="更改时间" min-width="155" sortable="custom"></el-table-column>
        <el-table-column prop="user_id" label="创建者" min-width="100" sortable="custom">
          <template slot-scope="scope">
            <span>{{scope.row.first_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="alter_id" label="更改者" min-width="100" sortable="custom">
          <template slot-scope="scope">
            <span>{{scope.row.alter_name}}</span>
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="105">
          <template slot-scope="scope">
            <el-button type="text">编辑</el-button>
            <el-button type="text" class="text_danger" v-if="scope.row.pack_status==1">停用</el-button>
            <el-button type="text" class="text_success" v-else>启用</el-button>
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
        url: _axios.ajaxAd.getPacks
      })
    },
    getLiveMonthAlias(value) {
      let item = this.liveMonthSelect.filter((v) => v.value == value)[0]
      return item ? item.label : ''
    },
    formatFlowUnit: Api.UNITS.formatFlowUnit,
    formatComboFlow: Api.UNITS.formatComboFlow,
    formatMoney: Api.UNITS.formatMoney,
  },
  computed: {
    ...mapState({
      orgs: 'orgs',
      liveMonthSelect: 'liveMonthSelect'
    })
  }
}

</script>
<style lang="scss">
.card_combo {
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
