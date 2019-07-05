<template>
  <div class="dead_status">
    <el-card class="clearfix" shadow="never" v-loading="loadData">
      <el-button-group style="margin-bottom: 10px">
        <el-button size="small" type="warning" :disabled="!pageAuthBtn.FCP_02_002_EXPORT01" @click="exportExcel">导出</el-button>
      </el-button-group>
      <el-form :inline="true" :model="formInline" class="search-form" size="small">
        <el-form-item>
          <el-input v-model="formInline.card_iccid" @input="formInline.card_iccid = limitNumber(formInline.card_iccid, 20)" @keyup.enter.native="searchData" placeholder="卡ICCID"></el-input>
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
          <el-select v-model="formInline.time_expire" filterable clearable placeholder="是否过期" @change="searchData">
            <el-option v-for="(item, index) in exceedSelect" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchData" :disabled="!pageAuthBtn.FCP_02_002_CHECK01">查询</el-button>
          <el-button type="warning" @click="resetData" :disabled="!pageAuthBtn.FCP_02_002_CHECK01">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table ref="listTable" :data="list.data" @sort-change="handleSortChange" :max-height="maxTableHeight" border resizable size="mini">
        <el-table-column fixed="left" prop="card_iccid" label="卡ICCID" width="178" sortable="custom">
          <template slot-scope="scope">
            <span v-if="scope.row.sums || !pageAuthBtn.FCP_02_002_LINK1">{{scope.row.card_iccid}}</span>
            <span v-else class="btn-link" @click="$router.push({ name: 'rechargeDetail', query: {card_id: scope.row.card_id}})">{{scope.row.card_iccid}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="card_id" label="卡商名称" width="130" sortable="custom">
          <template slot-scope="scope">
            <span>{{scope.row.card_type_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="org_id" label="所属机构" min-width="135" sortable="custom">
          <template slot-scope="scope" prop="org_name">
            <span v-if="pageAuthBtn.FCP_02_002_LINK02" class="btn-link" @click="$router.push({name: 'card', query: {org_id: scope.row.org_id}})">{{scope.row.org_name}}</span>
            <span v-else>{{scope.row.org_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="used_month" label="当月用量" width="100" sortable="custom" align="right">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.used_month)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="used_total" label="累计用量" width="100" sortable="custom" align="right">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.used_total)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="max_unused" label="剩余用量" width="100" sortable="custom" align="right">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.max_unused)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="time_added" label="导卡时间" width="153" sortable="custom"></el-table-column>
        <el-table-column prop="time_active" label="激活时间" width="153" sortable="custom"></el-table-column>
        <el-table-column prop="time_last" label="设备更新时间" width="153" sortable="custom"></el-table-column>
        <el-table-column prop="time_stop" label="上次停用时间" width="153" sortable="custom"></el-table-column>
        <el-table-column prop="time_expire" label="过期时间" min-width="153">
          <template slot-scope="scope">
            <div v-if="!scope.row.sums" v-html="calcLeftTime(scope.row.time_expire)"></div>
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="90" v-if="pageAuthBtn.FCP_02_002_OP01 || pageAuthBtn.FCP_02_002_OP2">
          <template slot-scope="scope" v-if="!scope.row.sums">
            <el-button type="text" @click="$refs.syncUniconData.getSyncUnicomData(scope)" v-if="pageAuthBtn.FCP_02_002_OP01">同步</el-button>
            <el-button type="text" class="text_success" v-if="scope.row.unicom_stop && pageAuthBtn.FCP_02_002_OP2" @click="checkCardStop(scope, 0)">启用</el-button>
            <el-button type="text" class="text_danger" v-else-if="!scope.row.unicom_stop && pageAuthBtn.FCP_02_002_OP2" @click="checkCardStop(scope, 1)">停用</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="list.currentPage" :page-sizes="pageSizes" :page-size="list.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="list.total" class="clearfix">
      </el-pagination>
    </el-card>
    <v-sync-unicom-data ref="syncUniconData"></v-sync-unicom-data>
  </div>
</template>
<script>
import Api from 'assets/js/api.js'
import { mapState } from 'vuex'

export default {
  data() {
    return {}
  },
  mounted() {
    this.getData()
  },
  methods: {
    // 导出excel
    exportExcel() {
      Api.UNITS.exportExcel(_axios.ajaxAd.deadstatusExport, this.formInline)
    },
    getData() {
      Api.UNITS.getListData({
        vue: this,
        url: _axios.ajaxAd.getHalt,
        cb: (res) => {
          let other = res.data.other || {}
          if (this.list.data.length === 0) return
          // 一下计算合计
          this.list.data.push(...[{
            sums: true,
            card_iccid: '小计',
            used_month: Api.UNITS.pageSums(this.list.data, 'used_month'),
            max_unused: Api.UNITS.pageSums(this.list.data, 'max_unused'),
            used_total: Api.UNITS.pageSums(this.list.data, 'used_total')
          }, {
            sums: true,
            card_iccid: '总计',
            used_month: other.used_month || 0,
            used_total: other.used_total || 0,
            max_unused: other.max_unused || 0
          }])
        }
      })
    },
    // 卡开启/停用(card / deadstatus / useanomaly)
    checkCardStop(scope, status) {
      let prompt = ''
      if (status === 1) {
        prompt = '是否停用该流量卡？'
      } else if (status === 0) {
        prompt = '是否启用该流量卡？'
      }
      this.$confirm(prompt, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        _axios.send({
          method: 'post',
          url: _axios.ajaxAd.checkCardStop,
          data: {
            card_iccid: scope.row.card_iccid,
            status
          },
          done: ((res) => {
            setTimeout(() => {
              this.modifiyData(this.list.data, scope.row, 'unicom_stop', status)
              this.showMsgBox({
                type: 'success',
                message: '操作成功！'
              })
            }, 150)
          })
        })
      }).catch(() => {
        this.showMsgBox({
          type: 'info',
          message: '已取消操作！'
        })
      })
    }
  }
}

</script>
<style lang="scss">
.dead_status {
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

}

</style>
