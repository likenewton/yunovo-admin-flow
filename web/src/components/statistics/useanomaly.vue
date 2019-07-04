<template>
  <div>
    <el-card class="clearfix" shadow="never" v-loading="loadData">
      <el-button-group style="margin-bottom: 10px">
        <el-button size="small" type="warning" :disabled="!pageAuthBtn.FCP_02_004_EXPORT01">导出</el-button>
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
          <el-select v-model="formInline.org_id" filterable clearable placeholder="机构名称" @change="searchData">
            <el-option v-for="(item, index) in orgs" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-select v-model="formInline.unicom_diff" filterable clearable placeholder="日差异流量" @change="searchData">
            <el-option v-for="(item, index) in unicomDiffSelect" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-select v-model="formInline.max_unused" filterable clearable placeholder="剩余流量" @change="searchData">
            <el-option v-for="(item, index) in maxUnusedSelect" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchData" :disabled="!pageAuthBtn.FCP_02_004_CHECK01">查询</el-button>
          <el-button type="warning" @click="resetData" :disabled="!pageAuthBtn.FCP_02_004_CHECK01">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table ref="listTable" @sort-change="handleSortChange" :data="list.data" :max-height="maxTableHeight" border resizable size="mini">
        <el-table-column fixed="left" prop="card_iccid" label="卡ICCID" width="180">
          <template slot-scope="scope">
            <span v-if="pageAuthBtn.FCP_02_004_LINK01" class="btn-link" @click="$router.push({ name: 'rechargeDetail', query: {card_id: scope.row.card_id}})">{{scope.row.card_iccid}}</span>
            <span v-else>{{scope.row.card_iccid}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="card_id" label="卡商名称" min-width="120" sortable="custom">
          <template slot-scope="scope">
            <span>{{scope.row.card_type_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="org_id" label="机构名称" min-width="180" sortable="custom">
          <template slot-scope="scope">
            <span v-if="pageAuthBtn.FCP_02_004_LINK02" class="btn-link" @click="$router.push({name: 'card', query: {org_id: scope.row.org_id}})">{{scope.row.org_name}}</span>
            <span v-else>{{scope.row.org_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="unicom_diff" label="日差异流量" width="125" sortable="custom" align="right">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.unicom_diff)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="max_unused" label="剩余用量" width="125" sortable="custom" align="right">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.max_unused)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="used_total" label="平台使用总流量" width="125" sortable="custom" align="right">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.used_total)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="unicom_total" label="联通使用总流量" width="125" sortable="custom" align="right">
          <template slot-scope="scope">
            <div v-html="formatFlowUnit(scope.row.unicom_total)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="time_last" label="设备更新时间" width="155" sortable="custom"></el-table-column>
        <el-table-column fixed="right" prop='unicom_stop' label="操作" width="125">
          <template slot-scope="scope">
            <el-button type="text" @click="toUnicomLink(scope.row.card_iccid)">套餐</el-button>
            <el-button type="text" class="text_success" v-if="scope.row.unicom_stop == 1 && pageAuthBtn.FCP_02_004_OP01" @click="checkCardStop(scope, 0)">启用</el-button>
            <el-button type="text" class="text_danger" v-else-if="scope.row.unicom_stop == 0 && pageAuthBtn.FCP_02_004_OP01" @click="checkCardStop(scope, 1)">停用</el-button>
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
    return {}
  },
  mounted() {
    this.getData()
  },
  methods: {
    getData() {
      Api.UNITS.getListData({
        vue: this,
        url: _axios.ajaxAd.getAbnormal
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
            this.modifiyData(this.list.data, scope.row, 'unicom_stop', status)
            setTimeout(() => {
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
