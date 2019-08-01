<template>
  <div class="month_used">
    <el-card class="clearfix" shadow="never" v-loading="loadData">
      <el-button-group style="margin-bottom: 10px">
        <el-button size="small" type="warning" @click="exportExcel" :disabled="!pageAuthBtn.FCP_02_001_EXPORT01">导出</el-button>
      </el-button-group>
      <el-form :inline="true" :model="formInline" class="search-form" size="small">
        <el-form-item>
          <el-input v-model="formInline.card_iccid" @input="formInline.card_iccid = expNumStr(formInline.card_iccid)"  @keyup.enter.native="searchData" placeholder="卡ICCID"></el-input>
        </el-form-item>
        <el-form-item>
          <el-select v-model="formInline.card_type" filterable clearable placeholder="卡商名称" @change="searchData">
            <el-option v-for="(item, index) in cardTypes" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-select v-model="formInline.org_id" filterable clearable placeholder="所属机构" @change="searchData">
            <el-option v-for="(item, index) in orgs" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-select v-model="formInline.mdate" filterable clearable placeholder="月份" @change="searchData">
            <el-option v-for="(item, index) in months" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchData" :disabled="!pageAuthBtn.FCP_02_001_CHECK01">查询</el-button>
          <el-button type="warning" @click="resetData" :disabled="!pageAuthBtn.FCP_02_001_CHECK01">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table class="list_table" ref="listTable" @sort-change="handleSortChange" :data="list.data" :max-height="maxTableHeight" border resizable size="mini">
        <el-table-column prop="card_iccid" fixed="left" label="卡ICCID" width="200" sortable="custom">
          <template slot-scope="scope">
            <span v-if="scope.row.sums || !pageAuthBtn.FCP_02_001_LINK1">{{scope.row.card_iccid}}</span>
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
            <span v-if="pageAuthBtn.FCP_02_001_LINK2" class="btn-link" @click="$router.push({name: 'card', query: {org_id: scope.row.org_id}})">{{scope.row.org_name}}</span>
            <span v-else>{{scope.row.org_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="time_active" label="卡激活时间" width="155" sortable="custom"></el-table-column>
        <el-table-column prop="how_month" label="月份" width="120" sortable="custom"></el-table-column>
        <el-table-column prop="month_used" label="月使用流量" width="120" align="right" sortable="custom">
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
      if (this.formInline.mdate) {
        Api.UNITS.exportExcel(_axios.ajaxAd.statsExport, this.formInline)
      } else {
        this.showMsgBox({
          type: 'warning',
          message: '请在搜索框中选择您要导出的月份！'
        })
      }
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
