<template>
  <div>
    <el-card class="box-card" style="margin-bottom: 20px" shadow="never">
      <el-form :inline="true" :model="formInline" class="demo-form-inline" size="small">
        <el-form-item label="卡ICCID">
          <el-input v-model="formInline.card_iccid" placeholder="请输入卡的iccid"></el-input>
        </el-form-item>
        <el-form-item label="卡商名称">
          <el-select v-model="formInline.card_type" placeholder="请选择">
            <el-option v-for="(item, index) in cardTypes" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="机构名称">
          <el-select v-model="formInline.jg_name" placeholder="请选择">
            <el-option label="机构1" value="0"></el-option>
            <el-option label="机构2" value="1"></el-option>
            <el-option label="机构3" value="2"></el-option>
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
        <el-table-column fixed="left" label="卡ICCID" width="200">
          <template slot-scope="scope">
            <el-button type="text" @click="checkRechargeDetail(scope.row.card_iccid)">{{scope.row.card_iccid}}</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="ks_name" label="卡商名称" min-width="140"></el-table-column>
        <el-table-column label="所属机构" min-width="140">
          <template slot-scope="scope">
            <el-button type="text">{{scope.row.jg_name}}</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="recharge_count" label="充值次数" min-width="110" sortable="custom"></el-table-column>
        <el-table-column prop="stopcard_count" label="停卡次数" min-width="110" sortable="custom"></el-table-column>
        <el-table-column label="上次停卡流量" min-width="105">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.stopcard_flow)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="recharge_time" label="上次充值时间" min-width="151" sortable="custom"></el-table-column>
        <el-table-column prop="stopcard_time" label="上次停卡时间" min-width="151" sortable="custom"></el-table-column>
        <el-table-column prop="last_time" label="最后更新时间" min-width="151" sortable="custom"></el-table-column>
        <el-table-column fixed="right" label="操作" width="110">
          <template slot-scope="scope">
            <el-button type="text" @click="showStopCardDetail(scope.row.card_iccid)">详情</el-button>
            <el-button type="text">套餐</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentPage" :page-sizes="pageSizes" :page-size="list.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="list.total" class="clearfix">
      </el-pagination>
    </el-card>
    <el-dialog title="停卡详情列表" :visible.sync="dialogTableVisible">
      <div slot class="clearfix">
        <el-table v-loading="dialogList.loadData" :data="dialogList.data" border size="mini">
          <el-table-column fixed="left" label="卡ICCID" width="200">
            <template slot-scope="scope">
              <el-button type="text" @click="checkRechargeDetail(scope.row.card_iccid)">{{scope.row.card_iccid}}</el-button>
            </template>
          </el-table-column>
          <el-table-column label="剩余流量" width="105">
            <template slot-scope="scope">
              <div v-html="formatFlowUnit(scope.row.left_flow)"></div>
            </template>
          </el-table-column>
          <el-table-column prop="op_declare" label="操作说明" min-width="200"></el-table-column>
          <el-table-column prop="stopcard_time" label="停卡时间" width="151"></el-table-column>
          <el-table-column prop="result_status" label="执行结果" width="100"></el-table-column>
        </el-table>
        <el-pagination @size-change="handleSizeChangeDetail" @current-change="handleCurrentChangeDetail" :current-page="currentPage" :page-sizes="pageSizes" :page-size="dialogList.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="dialogList.total" class="clearfix">
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
      loadData: true,
      pageSizes: Api.STATIC.pageSizes,
      list: {
        data: [],
        pagesize: Api.STATIC.pageSizes[1],
        currentPage: 1,
        total: 0,
      },
      dialogTableVisible: false,
      dialogList: {
        loadData: true,
        data: [],
        pagesize: Api.STATIC.pageSizes[1],
        currentPage: 1,
        total: 0,
      },
      curChoiceIccid: '',
      sort: {},
      formInline: {}
    }
  },
  mounted() {
    // 进入页面的时候请求数据
    if (this.list.data.length === 0) {
      this.getData()
    } else {
      this.loadData = false
    }
  },
  methods: {
    ...mapMutations([
      'SET_DIALOGVISIBLE'
    ]),
    handleSortChange(val) {
      Api.UNITS.setSortSearch(val, this)
      this.getData()
    },
    // 列表
    handleSizeChange(val) {
      this.list.pagesize = val
      this.getData()
    },
    handleCurrentChange(val) {
      this.list.currentPage = val
      this.getData()
    },
    // dialog中的列表
    handleSizeChangeDetail(val) {
      this.dialogList.pagesize = val
      this.getDetailData()
    },
    handleCurrentChangeDetail(val) {
      this.dialogList.currentPage = val
      this.getDetailData()
    },
    // 打开dialog 展示某一个iccid数据
    showStopCardDetail(card_iccid) {
      this.curChoiceIccid = card_iccid
      this.dialogTableVisible = true
      this.getDetailData()
    },
    // iccid 连接跳转
    checkRechargeDetail(card_iccid) {
      this.$router.push({ name: 'rechargeDetail', query: { card_iccid } })
    },
    // 获取列表数据
    getData() {
      // Api.UNITS.getListData({
      //   vue: this,
      //   url: _axios.ajaxAd.getStats
      // })
      this.loadData = true
      _axios.send({
        method: 'get',
        url: _axios.ajaxAd.getStats,
        params: Object.assign(this.formInline, {
          ascs: this.sort.ascs,
          descs: this.sort.descs,
          size: this.list.pagesize,
          current: this.list.currentPage
        }),
        done: (res) => {
          this.loadData = false
          this.list.data = [{
            id: 0,
            card_iccid: '89860617040000312399',
            ks_name: '智网科技 JASPER',
            jg_name: '卡仕特-西格玛',
            recharge_count: 1,
            stopcard_count: 1,
            stopcard_flow: 542.3,
            recharge_time: '2019-03-25 12:52:10',
            stopcard_time: '2019-03-25 12:52:10',
            last_time: '2019-03-25 12:52:10',
          }]
          this.list.total = this.list.data.length
        }
      })
    },
    // 获取dialog中列表数据
    getDetailData() {
      this.dialogList.loadData = true
      _axios.send({
        method: 'get',
        url: _axios.ajaxAd.getStats,
        params: {
          card_iccid: this.curChoiceIccid,
          size: this.dialogList.pagesize,
          current: this.dialogList.currentPage
        },
        done: (res) => {
          this.dialogList.loadData = false
          this.dialogList.data = [{
            id: 0,
            card_iccid: '89860617040000312399',
            left_flow: -100.3,
            op_declare: '一些说明',
            stopcard_time: '2019-03-25 12:52:10',
            result_status: '成功'
          }]
          this.dialogList.total = this.dialogList.data.length
        }
      })
    },
    // 过滤器
    formatFlowUnit: Api.UNITS.formatFlowUnit,
    calcLeftTime: Api.UNITS.calcLeftTime
  },
  computed: {
    ...mapState({
      dialogVisible: 'dialogVisible',
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
