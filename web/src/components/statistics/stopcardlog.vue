<template>
  <div>
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
        <el-form-item label="机构名称">
          <el-select v-model="formInline.org_id" filterable clearable placeholder="请选择机构">
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
        <el-button size="small" type="warning">导出</el-button>
      </el-button-group>
      <el-table ref="listTable" :data="list.data" @sort-change="handleSortChange" :max-height="maxTableHeight" border resizable size="mini">
        <el-table-column fixed="left" prop="card_iccid" label="卡ICCID" width="200" sortable="custom">
          <template slot-scope="scope">
            <span class="btn-link" @click="$router.push({ name: 'rechargeDetail', query: {card_id: scope.row.card_id}})">{{scope.row.card_iccid}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="card_id" label="卡商名称" min-width="140" sortable="custom">
          <template slot-scope="scope">
            <span>{{scope.row.card_type_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="org_id" label="所属机构" min-width="140" sortable="custom">
          <template slot-scope="scope">
            <span class="btn-link" @click="$router.push({name: 'card', query: {org_id: scope.row.org_id}})">{{scope.row.org_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="pay_count" label="充值次数" min-width="95" sortable="custom"></el-table-column>
        <el-table-column prop="stop_num" label="停卡次数" min-width="95" sortable="custom"></el-table-column>
        <el-table-column prop="stop_value" label="上次停卡流量" min-width="105">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.stop_value)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="time_paid" label="上次充值时间" min-width="155" sortable="custom"></el-table-column>
        <el-table-column prop="time_stop" label="上次停卡时间" min-width="155" sortable="custom"></el-table-column>
        <el-table-column prop="time_last" label="最后更新时间" min-width="155" sortable="custom"></el-table-column>
        <el-table-column fixed="right" label="操作" width="110">
          <template slot-scope="scope">
            <el-button type="text" @click="showStopCardDetail(scope.row)">详情</el-button>
            <el-button type="text" @click="toUnicomLink(scope.row.card_iccid)">套餐</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :page-sizes="pageSizes" :page-size="list.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="list.total" class="clearfix">
      </el-pagination>
    </el-card>
    <el-dialog title="停卡详情列表" :visible.sync="dialogTableVisible">
      <div slot class="clearfix">
        <el-table v-loading="dialogList.loadData" :data="curTableData" :max-height="winHeight / 2" border size="mini">
          <el-table-column fixed="left" prop="card_iccid" label="卡ICCID" width="180"></el-table-column>
          <el-table-column prop="balance_value" label="剩余流量" width="105">
            <template slot-scope="scope">
              <div v-html="formatFlowUnit(scope.row.balance_value)"></div>
            </template>
          </el-table-column>
          <el-table-column prop="user_name" show-overflow-tooltip label="操作说明" min-width="200"></el-table-column>
          <el-table-column prop="time_added" label="停卡时间" width="155"></el-table-column>
          <el-table-column prop="exec_status" label="执行结果" width="75">
            <template slot-scope="scope">
              <span class="text_success" v-if="scope.row.exec_status === 1">成功</span>
              <span class="text_danger" v-else>失败</span>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination @size-change="handleSizeChangeDetail" @current-change="handleCurrentChangeDetail" :current-page="list.currentPage" :page-sizes="dialogList.pageSizes" :page-size="dialogList.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="dialogList.total" class="clearfix">
        </el-pagination>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import Api from 'assets/js/api.js'
import { mapState, mapMutations } from 'vuex'

export default {
  data() {
    return {
      dialogTableVisible: false,
      dialogList: { // dialog table列表数据
        loadData: true,
        pageSizes: [5, 10, 15, 20],
        data: [],
        pagesize: 10,
        currentPage: 1,
        total: 0,
      },
      curChoiceRow: {}, // iccid详情列表 当前选择项保存项(list.data中的某一项)
      formInline: {
        card_id: Api.UNITS.getQuery('card_id')
      }
    }
  },
  mounted() {
    this.getData()
  },
  methods: {
    // dialog中的列表
    handleSizeChangeDetail(val) {
      this.dialogList.pagesize = val
    },
    handleCurrentChangeDetail(val) {
      this.dialogList.currentPage = val
    },
    // 打开dialog 展示某一个iccid数据
    showStopCardDetail(row) {
      this.curChoiceRow = row
      this.dialogTableVisible = true
      this.getDetailData()
    },
    resetData() {
      this.list.currentPage = 1
      this.formInline = {
        card_id: Api.UNITS.getQuery('card_id')
      } // 1、重置查询表单
      this.sort = {} // 2、重置排序
      this.$refs.listTable.clearSort() // 3、清空排序样式
      this.getData()
    },
    // 获取列表数据
    getData() {
      Api.UNITS.getListData({
        vue: this,
        url: _axios.ajaxAd.getOnOffLog
      })
    },
    // 获取dialog中列表数据
    getDetailData() {
      this.dialogList.loadData = true
      _axios.send({
        method: 'get',
        url: _axios.ajaxAd.getOnOffLogDetail,
        params: {
          card_id: this.curChoiceRow.card_id,
          card_iccid: this.curChoiceRow.card_iccid,
          size: this.dialogList.pagesize,
          current: this.dialogList.currentPage
        },
        done: (res) => {
          this.dialogList.loadData = false
          this.dialogList.data = res.data
          this.dialogList.total = res.data.length
        }
      })
    }
  },
  computed: {
    // dialog中的表单采用前端分页
    curTableData() {
      return this.dialogList.data.slice((this.dialogList.currentPage - 1) * this.dialogList.pagesize, this.dialogList.currentPage * this.dialogList.pagesize)
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
