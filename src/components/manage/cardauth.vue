<template>
  <div>
    <el-card class="box-card" style="margin-bottom: 20px" shadow="never">
      <el-form :inline="true" :model="formInline" class="demo-form-inline" size="small">
        <el-form-item label="卡ICCID">
          <el-input v-model="formInline.iccid" placeholder="请输入卡的iccid"></el-input>
        </el-form-item>
        <el-form-item label="机构名称">
          <el-select v-model="formInline.jg_name" placeholder="请选择">
            <el-option label="机构1" value="0"></el-option>
            <el-option label="机构2" value="1"></el-option>
            <el-option label="机构3" value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="审核状态">
          <el-select v-model="formInline.audit_status" placeholder="请选择">
            <el-option label="待审核" value="0"></el-option>
            <el-option label="有效" value="1"></el-option>
            <el-option label="无效" value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="申请时间">
          <el-date-picker v-model="formInline.apply_time" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary">查询</el-button>
          <el-button type="warning">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="box-card clearfix" shadow="never">
      <el-button-group style="margin-bottom: 10px">
        <el-button size="mini" type="primary">导出实名信息</el-button>
      </el-button-group>
      <el-table v-loading="loadData" ref="multipleTable" :data="curTableData" border :default-sort="{prop: 'last_time', order: 'descending'}" size="mini">
        <el-table-column fixed="left" show-overflow-tooltip label="卡ICCID" min-width="180">
          <template slot-scope="scope">
            <el-button type="text">{{scope.row.iccid}}</el-button>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="jg_name" label="机构名称" width="140"></el-table-column>
        <el-table-column show-overflow-tooltip prop="name" label="姓名" width="100"></el-table-column>
        <el-table-column show-overflow-tooltip prop="sex" label="姓别" width="70"></el-table-column>
        <el-table-column show-overflow-tooltip prop="identity_card" label="身份证编号" width="175"></el-table-column>
        <el-table-column show-overflow-tooltip prop="last_time" label="最后上报时间" show-overflow-tooltip min-width="151" sortable></el-table-column>
        <el-table-column show-overflow-tooltip prop="active_time" label="卡激活时间" show-overflow-tooltip min-width="151" sortable></el-table-column>
        <el-table-column show-overflow-tooltip prop="audit_time" label="审核时间" show-overflow-tooltip min-width="151" sortable></el-table-column>
        <el-table-column show-overflow-tooltip prop="apply_time" label="申请时间" show-overflow-tooltip min-width="151" sortable></el-table-column>
        <el-table-column show-overflow-tooltip prop="apply_status" label="审核状态" show-overflow-tooltip min-width="100" sortable></el-table-column>
        <el-table-column show-overflow-tooltip prop="applyer" label="审核者" show-overflow-tooltip min-width="100"></el-table-column>
        <el-table-column fixed="right" label="操作" width="115">
          <template slot-scope="scope">
            <el-button type="text" @click="showDialog">审核实名证件</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentPage" :page-sizes="pageSizes" :page-size="pagesize" layout="total, sizes, prev, pager, next, jumper" :total="tableData.length" class="clearfix">
      </el-pagination>
    </el-card>
    <v-dialog :dialogPara="dialogPara"></v-dialog>
  </div>
</template>
<script>
import Api from 'assets/js/api.js'
import { mapState, mapMutations } from 'vuex'

export default {
  data() {
    return {
      routeName: this.$route.name,
      currentPage: 1,
      pageSizes: Api.STATIC.pageSizes,
      pagesize: Api.STATIC.pageSizes[1],
      loadData: true,
      tabIndex: '0',
      tableData: [],
      formInline: {},
      dialogPara: {
        title: '流量卡实名审核',
        content: '<div id="cardauto" style="width:100%; height:300px"></div>'
      },
    }
  },
  mounted() {
    // 进入页面的时候请求数据
    if (this.tableData.length === 0) {
      this.getData()
    } else {
      this.loadData = false
    }
  },
  methods: {
    ...mapMutations([
      'SET_DIALOGVISIBLE'
    ]),
    handleSizeChange(val) {
      this.pagesize = val
    },
    handleCurrentChange(val) {
      this.currentPage = val
    },
    // 新增流量预警
    newAdd() {

    },
    // 获取列表数据
    getData() {
      setTimeout(() => {
        // 数据请求成功
        this.tableData = [{
          id: 0,
          iccid: '89860617040000688970',
          jg_name: '卡仕特-西格玛',
          name: 'Newton',
          sex: '男',
          identity_card: '456420194212064785',
          last_time: '2019-03-25 12:52:10',
          active_time: '2019-03-25 12:52:10',
          audit_time: '2019-03-25 12:52:10',
          apply_time: '2019-03-25 12:52:10',
          apply_status: '待审核',
          applyer: 'Newton'
        }]
        this.loadData = false
      }, 1000)
    },
    showDialog() {
      this.SET_DIALOGVISIBLE({ dialogVisible: true })
    },
    formatFlowUnit: Api.UNITS.formatFlowUnit,
    calcLeftTime: Api.UNITS.calcLeftTime
  },
  computed: {
    ...mapState({
      dialogVisible: 'dialogVisible',
    }),
    curTableData() {
      return this.tableData.slice((this.currentPage - 1) * this.pagesize, this.currentPage * this.pagesize)
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
