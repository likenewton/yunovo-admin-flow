<template>
  <div>
    <el-card class="box-card" style="margin-bottom: 20px" shadow="never">
      <el-form :inline="true" :model="formInline" class="demo-form-inline" size="small">
        <el-form-item label="卡ICCID">
          <el-input v-model="formInline.card_iccid" placeholder="请输入卡的iccid"></el-input>
        </el-form-item>
        <el-form-item label="卡商名称">
          <el-select v-model="formInline.card_type" placeholder="请选择">
            <el-option label="卡商1" value="0"></el-option>
            <el-option label="卡商2" value="1"></el-option>
            <el-option label="卡商3" value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="所属机构">
          <el-select v-model="formInline.org_id" placeholder="请选择">
            <el-option label="机构1" value="0"></el-option>
            <el-option label="机构2" value="1"></el-option>
            <el-option label="机构3" value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="月份">
          <el-select v-model="formInline.mdate" placeholder="请选择">
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
    <el-card class="box-card clearfix" shadow="never">
      <el-button-group style="margin-bottom: 10px">
        <el-button size="mini" type="warning">导出</el-button>
      </el-button-group>
      <el-table v-loading="loadData" ref="multipleTable" :data="list.data" border :default-sort="{prop: 'active_time', order: 'descending'}" size="mini">
        <el-table-column fixed="left" show-overflow-tooltip label="卡ICCID" width="200">
          <template slot-scope="scope">
            <el-button type="text">{{scope.row.iccid}}</el-button>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="ks_name" label="卡商名称" min-width="140"></el-table-column>
        <el-table-column show-overflow-tooltip label="所属机构" min-width="140">
          <template slot-scope="scope">
            <el-button type="text">{{scope.row.jg_name}}</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="active_time" label="卡激活时间" show-overflow-tooltip width="158" sortable></el-table-column>
        <el-table-column prop="month" label="月份" show-overflow-tooltip width="100"></el-table-column>
        <el-table-column label="月使用流量" show-overflow-tooltip width="110">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.month_use)"></div>
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
      formInline: {}
    }
  },
  mounted() {
    // 进入页面的时候请求数据
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
    // 获取列表数据
    getData() {
      this.loadData = true
      _axios.send({
        method: 'post',
        url: _axios.ajaxAd.getStats,
        data: Object.assign(this.formInline, {
          size: this.list.pagesize,
          current: this.list.currentPage
        }),
        done: (res) => {
          this.loadData = false
          console.log(res)
          this.list.data = [{
            id: 0,
            iccid: '89860617040000688970',
            ks_name: '智网科技 JASPER',
            jg_name: '卡仕特-西格玛',
            active_time: '2019-03-25 12:52:10',
            month: '2019-05',
            month_use: 6565.5
          }]
          this.list.total = this.list.data.length
        }
      })
    },
    formatFlowUnit: Api.UNITS.formatFlowUnit,
    calcLeftTime: Api.UNITS.calcLeftTime
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
