<template>
  <div class="card_combo">
    <el-card class="clearfix" shadow="never" v-loading="loadData">
      <el-button-group style="margin-bottom: 10px">
        <el-button size="small" type="success" @click="$router.push({ name: 'rechargecomboset' })" :disabled="!pageAuthBtn.FCP_01_004_ADD01">新增</el-button>
      </el-button-group>
      <el-form :inline="true" :model="formInline" class="search-form" size="small" @submit.native.prevent>
        <el-form-item>
          <el-select v-model="formInline.org_id" filterable clearable placeholder="机构名称" @change="searchData">
            <el-option :key="Newton" label="公共套餐" value="0"></el-option>
            <el-option v-for="(item, index) in orgs" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchData" :disabled="!pageAuthBtn.FCP_01_004_CHECK01">查询</el-button>
          <el-button type="warning" @click="resetData" :disabled="!pageAuthBtn.FCP_01_004_CHECK01">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table ref="listTable" :data="list.data" @sort-change="handleSortChange" :max-height="maxTableHeight" border resizable size="mini">
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
        <el-table-column prop="gprs_price" label="套餐价格" :width="widthMap.gprs_price[size]" sortable="custom" align="right">
          <template slot-scope="scope">
            <span>￥{{formatMoney(scope.row.gprs_price)}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="allot_month" label="分配月数" :width="widthMap.allot_month[size]" sortable="custom" align="right"></el-table-column>
        <el-table-column prop="allot_value" label="月均流量" :width="widthMap.allot_value[size]" sortable="custom" align="right">
          <template slot-scope="scope">
            <div v-html="formatComboFlow(scope.row.allot_value)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="allot_reset" label="是否清零" :width="widthMap.allot_reset[size]" sortable="custom">
          <template slot-scope="scope">
            <span v-if="scope.row.allot_reset==1">会清零</span>
            <span v-else>不清零</span>
          </template>
        </el-table-column>
        <el-table-column prop="live_month" label="有效周期" :width="widthMap.live_month[size]" sortable="custom">
          <template slot-scope="scope">
            <div>{{getLiveMonthAlias(scope.row.live_month)}}</div>
          </template>
        </el-table-column>
        <el-table-column label="套餐描述" :min-width="widthMap.descripe[size]">
          <template slot-scope="scope">
            <div>
              <span>{{scope.row.pack_name}}</span>
            </div>
            <div style="font-size: 12px; line-height: 16px">{{scope.row.pack_memo}}</div>
          </template>
        </el-table-column>
        <el-table-column prop="pack_rebate" label="返利金额" :width="widthMap.pack_rebate[size]" sortable="custom" align="right">
          <template slot-scope="scope">
            <span>￥{{formatMoney(scope.row.pack_rebate)}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="pack_recom" label="是否推荐" :width="widthMap.pack_recom[size]" sortable="custom">
          <template slot-scope="scope">
            <span v-if="scope.row.pack_recom==1">是</span>
            <span v-else>否</span>
          </template>
        </el-table-column>
        <el-table-column prop="time_added" label="添加时间" :width="widthMap.time_added[size]" sortable="custom"></el-table-column>
        <el-table-column prop="time_modify" label="更改时间" :width="widthMap.time_modify[size]" sortable="custom"></el-table-column>
        <el-table-column prop="user_id" label="创建者" :width="widthMap.user_id[size]" sortable="custom">
          <template slot-scope="scope">
            <span>{{scope.row.first_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="alter_id" label="更改者" :width="widthMap.alter_id[size]" sortable="custom">
          <template slot-scope="scope">
            <span>{{scope.row.alter_name}}</span>
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="88" v-if="pageAuthBtn.FCP_01_004_UPDATE01 || pageAuthBtn.FCP_01_004_OP02">
          <template slot-scope="scope">
            <el-button v-if="pageAuthBtn.FCP_01_004_UPDATE01" type="text" class="text_editor" @click="$router.push({name: 'rechargecomboset', query: {type: 'update', pack_id: scope.row.pack_id}})">编辑</el-button>
            <el-button type="text" class="text_danger" v-if="scope.row.pack_status==1 && pageAuthBtn.FCP_01_004_OP02" @click="checkComboStop(scope, 0)">停用</el-button>
            <el-button type="text" class="text_success" v-else-if="scope.row.pack_status==0 && pageAuthBtn.FCP_01_004_OP02" @click="checkComboStop(scope, 1)">启用</el-button>
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
        org_id: [130, 130],
        gprs_amount: [95, 88],
        gprs_price: [100, 100],
        allot_month: [85, 45],
        allot_value: [95, 95],
        allot_reset: [85, 70],
        live_month: [120, 75],
        descripe: [190, 130],
        pack_rebate: [90, 90],
        pack_recom: [85, 45],
        time_added: [153, 95],
        time_modify: [153, 95],
        user_id: [75, 65],
        alter_id: [75, 65],
      },
    }
  },
  mounted() {
    this.getData()
  },
  methods: {
    getData() {
      Api.UNITS.getListData({
        vue: this,
        url: _axios.ajaxAd.getPacks
      })
    },
    // 停用/启用
    checkComboStop(scope, pack_status) {
      let prompt = ''
      if (pack_status === 0) {
        prompt = '是否停用该套餐？'
      } else if (pack_status === 1) {
        prompt = '是否启用该套餐？'
      }
      this.$confirm(prompt, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        _axios.send({
          method: 'post',
          url: _axios.ajaxAd.checkComboStop,
          data: {
            pack_id: scope.row.pack_id,
            pack_status // 0停用，1启用
          },
          done: ((res) => {
            this.getData()
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
    },
    getLiveMonthAlias(value) {
      let item = this.liveMonthSelect.filter((v) => v.value == value)[0]
      return item ? item.label : ''
    }
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

  @media screen and (max-width: 1900px) {
    .is-sortable {
      .caret-wrapper {
        display: none;
      }
    }
  }
}

</style>
