<template>
  <div class="card_auth">
    <el-card style="margin-bottom: 20px" shadow="never">
      <el-form :inline="true" :model="formInline" class="search-form" size="small">
        <el-form-item label="卡ICCID">
          <el-input v-model="formInline.card_iccid" placeholder="请输入卡的iccid"></el-input>
        </el-form-item>
        <el-form-item label="机构名称">
          <el-select v-model="formInline.org_id" filterable placeholder="请选择机构名称">
            <el-option v-for="(item, index) in orgs" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="审核状态">
          <el-select v-model="formInline.status" placeholder="请选择">
            <el-option label="待审核" value="0"></el-option>
            <el-option label="无效" value="1"></el-option>
            <el-option label="有效" value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="申请时间">
          <el-date-picker v-model="formInline.date_start" type="date" value-format="yyyy-MM-dd" placeholder="选择开始日期"></el-date-picker> -
          <el-date-picker v-model="formInline.date_end" type="date" value-format="yyyy-MM-dd" placeholder="选择结束日期"></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchData">查询</el-button>
          <el-button type="warning" @click="resetData">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="clearfix" shadow="never" v-loading="loadData">
      <el-button-group style="margin-bottom: 10px">
        <el-button size="mini" type="warning">导出实名信息</el-button>
      </el-button-group>
      <el-table ref="listTable" :data="list.data" @sort-change="handleSortChange" :max-height="maxTableHeight" border resizable size="mini">
        <el-table-column prop="card_iccid" fixed="left" label="卡ICCID" min-width="180" sortable="custom">
          <template slot-scope="scope">
            <span class="btn-link" @click="$router.push({ name: 'rechargeDetail', query: {card_id: scope.row.card_id}})">{{scope.row.card_iccid}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="org_id" label="机构名称" width="140" sortable="custom">
          <template slot-scope="scope">
            <span>{{scope.row.org_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="owner_name" label="姓名" width="90" sortable="custom"></el-table-column>
        <el-table-column prop="owner_gender" label="姓别" width="80" sortable="custom">
          <template slot-scope="scope">
            <span v-if="scope.row.owner_gender==1">男性</span>
            <span v-else-if="scope.row.owner_gender==2">女性</span>
            <span v-else>保密</span>
          </template>
        </el-table-column>
        <el-table-column prop="owner_cdi" label="身份证编号" width="175"></el-table-column>
        <el-table-column prop="time_last" label="最后上报时间" min-width="155" sortable="custom"></el-table-column>
        <el-table-column prop="time_active" label="卡激活时间" min-width="155" sortable="custom"></el-table-column>
        <el-table-column prop="time_audit" label="审核时间" min-width="155" sortable="custom"></el-table-column>
        <el-table-column prop="time_added" label="申请时间" min-width="155" sortable="custom"></el-table-column>
        <el-table-column prop="cdi_status" label="审核状态" min-width="90" sortable="custom">
          <template slot-scope="scope">
            <span v-if="scope.row.cdi_status==1">无效</span>
            <span v-else-if="scope.row.cdi_status==2">有效</span>
            <span v-else>待审核</span>
          </template>
        </el-table-column>
        <el-table-column prop="user_id" label="审核者" min-width="100">
          <template slot-scope="scope">
            <span>{{scope.row.first_name}}</span>
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="115">
          <template slot-scope="scope">
            <el-button type="text" @click="showDialog(scope)">审核实名证件</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :page-sizes="pageSizes" :page-size="list.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="list.total" class="clearfix">
      </el-pagination>
    </el-card>
    <el-dialog title="机构卡实名审核" :visible.sync="authDialogVisible">
      <div slot class="auth_dialog">
        <div class="content" :style="{'maxHeight': winHeight/2 + 'px'}">
          <div class="item">
            <div class="title">身份证正面图</div>
            <img src="../../assets/images/no-image-800x500.jpg" height="224" width="224">
          </div>
          <div class="item">
            <div class="title">身份证背面图</div>
            <img src="../../assets/images/no-image-800x500.jpg" height="224" width="224">
          </div>
          <div class="item">
            <div class="title">手持身份证照</div>
            <img src="../../assets/images/no-image-800x500.jpg" height="224" width="224">
          </div>
        </div>
      </div>
      <div slot="footer">
        <el-button size="small" type="danger">实名信息无效</el-button>
        <el-button size="small" type="success">实名信息通过</el-button>
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
      maxTableHeight: Api.UNITS.maxTableHeight(),
      list: {
        data: [],
        pagesize: Api.STATIC.pageSizes[1],
        currentPage: 1,
        total: 0,
      },
      sort: {},
      formInline: {
        card_iccid: Api.UNITS.getQuery('card_iccid')
      },
      winHeight: 0,
      authDialogVisible: false
    }
  },
  mounted() {
    this.getData()
  },
  methods: {
    ...mapMutations([
      'SET_DIALOGVISIBLE'
    ]),
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
      this.formInline = {
        card_iccid: Api.UNITS.getQuery('card_iccid')
      } // 1、重置查询表单
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
        url: _axios.ajaxAd.getRealNames
      })
    },
    showDialog(scope) {
      this.winHeight = $(window).height()
      this.authDialogVisible = true
    },
    formatFlowUnit: Api.UNITS.formatFlowUnit,
    calcLeftTime: Api.UNITS.calcLeftTime
  },
  computed: {
    ...mapState({
      orgs: 'orgs'
    })
  }
}

</script>
<style lang="scss">
.card_auth {
  .el-pagination {
    float: right;
    margin: 25px 40px 0 0;
  }

  td {
    * {
      font-size: 14px;
    }
  }

  .el-date-editor .el-range-separator {
    width: auto;
  }

  .auth_dialog {
    overflow: auto;

    .item {
      text-align: center;

      .title {
        line-height: 20px;
        margin: 20px 0;
        font-size: 18px;
      }
    }
  }
}

</style>
