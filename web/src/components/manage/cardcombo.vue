<template>
  <div class="card_combo">
    <el-card class="search-card" style="margin-bottom: 20px" shadow="never">
      <el-form :inline="true" :model="formInline" class="search-form" size="small">
        <el-form-item label="机构名称">
          <el-select v-model="formInline.org_id" filterable clearable placeholder="请选择机构">
            <el-option :key="Newton" label="公共套餐" value="0"></el-option>
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
        <el-button size="small" type="success" @click="$router.push({ name: 'rechargecomboset' })">新增</el-button>
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
        <el-table-column prop="user_id" label="创建者" width="90" sortable="custom">
          <template slot-scope="scope">
            <span>{{scope.row.first_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="alter_id" label="更改者" width="90" sortable="custom">
          <template slot-scope="scope">
            <span>{{scope.row.alter_name}}</span>
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="105">
          <template slot-scope="scope">
            <el-button type="text" class="text_editor" @click="$router.push({name: 'rechargecomboset', query: {type: 'update', pack_id: scope.row.pack_id}})">编辑</el-button>
            <el-button type="text" class="text_danger" v-if="scope.row.pack_status==1" @click="checkComboStop(scope, 0)">停用</el-button>
            <el-button type="text" class="text_success" v-else @click="checkComboStop(scope, 1)">启用</el-button>
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
              this.$message({
                type: 'success',
                message: '操作成功!'
              })
            }, 150)
          })
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消操作'
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
}

</style>
