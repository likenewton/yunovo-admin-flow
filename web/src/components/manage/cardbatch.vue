<template>
  <div class="card_batch">
    <el-card class="clearfix" shadow="never" v-loading="loadData">
      <el-button-group style="margin-bottom: 10px">
        <el-button size="small" type="success" @click="$router.push({ name: 'batchcreate' })" :disabled="!pageAuthBtn.FCP_01_003_ADD01">新增</el-button>
        <el-button size="small" type="primary" @click="$refs.vCheckbox.openChoice()">展示列表</el-button>
      </el-button-group>
      <el-form :inline="true" :model="formInline" class="search-form" size="small">
        <el-form-item>
          <el-input v-model="formInline.batch_sn" placeholder="批次编号" @keyup.enter.native="searchData"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchData" :disabled="!pageAuthBtn.FCP_01_003_CHECK01">查询</el-button>
          <el-button type="primary" @click="searchVipVisible = true" :disabled="!pageAuthBtn.FCP_01_003_CHECK01">高级查询</el-button>
        </el-form-item>
      </el-form>
      <el-table ref="listTable" :data="list.data" @sort-change="handleSortChange" :max-height="maxTableHeight" border resizable size="mini">
        <el-table-column type="expand">
          <template slot-scope="props">
            <el-form label-position="left" inline class="table-expand">
              <el-form-item label="批次编号：">
                <span>{{props.row.batch_sn}}</span>
              </el-form-item>
              <el-form-item label="批次名称：">
                <span>{{props.row.batch_name}}</span>
              </el-form-item>
              <el-form-item label="机构名称：">
                <span>{{props.row.org_name}}</span>
              </el-form-item>
              <el-form-item label="套餐流量：">
                <span v-html="formatComboFlow(props.row.gprs_amount)"></span>
              </el-form-item>
              <el-form-item label="有效周期：">
                <span>{{getLiveMonthAlias(props.row.live_month)}}</span>
              </el-form-item>
              <el-form-item label="入卡数量：">
                <span>{{props.row.card_amount}}</span>
              </el-form-item>
              <el-form-item label="出货人名：">
                <span>{{props.row.batch_shipper}}</span>
              </el-form-item>
              <el-form-item label="销往城市：">
                <span>{{props.row.province_name}}{{props.row.city_name}}</span>
              </el-form-item>
              <el-form-item label="SIM卡类型：">
                <span>{{props.row.sim_type | valueToLabel(simType)}}</span>
              </el-form-item>
              <el-form-item label="设备ROM包机构编码：">
                <span>{{props.row.device_org_code | valueToLabel(devOrgs) || '无'}}</span>
              </el-form-item>
              <el-form-item label="设备项目型号：">
                <span>{{props.row.pro_name || '无'}}</span>
              </el-form-item>
              <el-form-item label="批次备注：">
                <span>{{props.row.batch_memo}}</span>
              </el-form-item>
              <el-form-item label="添加时间：">
                <span>{{props.row.time_added}}</span>
              </el-form-item>
              <el-form-item label="操作者：">
                <span>{{props.row.create_by}}</span>
              </el-form-item>
              <el-form-item label="更改者：">
                <span>{{props.row.update_by}}</span>
              </el-form-item>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column prop="batch_sn" label="批次编号" min-width="150" sortable="custom"></el-table-column>
        <el-table-column v-if="checkedData.includes('batch_name')" prop="batch_name" label="批次名称" min-width="150" sortable="custom"></el-table-column>
        <el-table-column v-if="checkedData.includes('org_id')" prop="org_id" label="机构名称" min-width="150" sortable="custom">
          <template slot-scope="scope">
            <span>{{scope.row.org_name}}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="checkedData.includes('gprs_amount')" prop="gprs_amount" label="套餐流量" width="100" sortable="custom" align="right">
          <template slot-scope="scope">
            <div v-html="formatComboFlow(scope.row.gprs_amount)"></div>
          </template>
        </el-table-column>
        <el-table-column v-if="checkedData.includes('live_month')" prop="live_month" label="有效周期" width="130" sortable="custom">
          <template slot-scope="scope">
            <div>{{getLiveMonthAlias(scope.row.live_month)}}</div>
          </template>
        </el-table-column>
        <el-table-column v-if="checkedData.includes('card_amount')" prop="card_amount" label="入卡数量" width="90" sortable="custom" align="right"></el-table-column>
        <el-table-column v-if="checkedData.includes('batch_shipper')" prop="batch_shipper" label="出货人名" min-width="100" sortable="custom"></el-table-column>
        <el-table-column v-if="checkedData.includes('city_name')" label="销往城市" min-width="150" show-overflow-tooltip>
          <template slot-scope="scope">
            <span>{{scope.row.province_name}}{{scope.row.city_name}}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="checkedData.includes('sim_type')" prop="sim_type" label="SIM卡类型" width="96" sortable="custom">
          <template slot-scope="scope">{{scope.row.sim_type | valueToLabel(simType)}}</template>
        </el-table-column>
        <el-table-column v-if="checkedData.includes('device_org_code')" prop="device_org_code" label="设备ROM包机构编码" min-width="160" show-overflow-tooltip sortable="custom">
          <template slot-scope="scope">{{scope.row.device_org_code | valueToLabel(devOrgs)}}</template>
        </el-table-column>
        <el-table-column v-if="checkedData.includes('pro_name')" prop="pro_name" label="设备项目型号" width="110" sortable="custom"></el-table-column>
        <el-table-column v-if="checkedData.includes('batch_memo')" prop="batch_memo" label="批次备注" min-width="150" sortable="custom" show-overflow-tooltip></el-table-column>
        <el-table-column v-if="checkedData.includes('time_added')" prop="time_added" label="添加时间" width="153" sortable="custom"></el-table-column>
        <el-table-column v-if="checkedData.includes('user_id')" prop="user_id" label="操作者" width="100" sortable="custom">
          <template slot-scope="scope">
            <span>{{scope.row.create_by}}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="checkedData.includes('alter_id')" prop="alter_id" label="更改者" width="100" sortable="custom">
          <template slot-scope="scope">
            <span>{{scope.row.update_by}}</span>
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="80" v-if="pageAuthBtn.FCP_01_003_UPDATE01">
          <template slot-scope="scope">
            <el-button type="text" class="text_editor" @click="$router.push({name: 'batchcreate', query:{type:'update', batch_id: scope.row.batch_id}})">编辑</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="list.currentPage" :page-sizes="pageSizes" :page-size="list.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="list.total" class="clearfix"></el-pagination>
    </el-card>
    <el-dialog title="高级查询" :visible.sync="searchVipVisible" width="690px" :close-on-click-modal="false">
      <div slot>
        <div class="searchForm_vip" style="width:100%;overflow: auto">
          <el-form :inline="false" :model="formInline" size="small" label-width="145px" v-loading="loadData">
            <el-form-item label="批次编号">
              <el-input v-model="formInline.batch_sn" placeholder="请输入"></el-input>
            </el-form-item>
            <el-form-item label="机构名称">
              <el-select v-model="formInline.org_id" filterable clearable placeholder="请选择">
                <el-option v-for="(item, index) in orgs" :key="index" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="SIM卡类型">
              <el-select v-model="formInline.sim_type" filterable clearable placeholder="请选择">
                <el-option v-for="(item, index) in simType" :key="index" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="设备ROM包机构编码">
              <el-select v-model="formInline.device_org_code" filterable clearable placeholder="请选择">
                <el-option v-for="(item, index) in devOrgs" :key="index" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="添加时间">
              <el-date-picker v-model="formInline.date_start" :picker-options="startDatePicker" type="date" value-format="yyyy-MM-dd" @change="searchData" placeholder="添加时间开始"></el-date-picker> -
              <el-date-picker v-model="formInline.date_end" :picker-options="endDatePicker" type="date" value-format="yyyy-MM-dd" @change="searchData" placeholder="添加时间结束"></el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="searchData">查询</el-button>
              <el-button type="warning" @click="resetData">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </el-dialog>
    <!-- 列表展示 -->
    <v-checkbox ref="vCheckbox" :checkboxData="checkboxData" :defaultData="defaultData" @checkSave="checkSave"></v-checkbox>
  </div>
</template>
<script>
import Api from 'assets/js/api.js'
import { mapState } from 'vuex'

export default {
  data() {
    return {
      devOrgs: [],
      // === 列表显示控制静态数据 ===
      checkboxData: [{
        label: '批次名称',
        value: 'batch_name'
      }, {
        label: '机构名称',
        value: 'org_id'
      }, {
        label: '套餐流量',
        value: 'gprs_amount'
      }, {
        label: '有效周期',
        value: 'live_month'
      }, {
        label: '入卡数量',
        value: 'card_amount'
      }, {
        label: '出货人名',
        value: 'batch_shipper'
      }, {
        label: '销往城市',
        value: 'city_name'
      }, {
        label: 'SIM卡类型',
        value: 'sim_type'
      }, {
        label: '设备ROM包机构编码',
        value: 'device_org_code'
      }, {
        label: '设备项目型号',
        value: 'pro_name'
      }, {
        label: '批次备注',
        value: 'batch_memo'
      }, {
        label: '添加时间',
        value: 'time_added'
      }, {
        label: '操作者',
        value: 'user_id'
      }, {
        label: '更改者',
        value: 'alter_id'
      }],
      defaultData: ['batch_name', 'org_id', 'gprs_amount', 'live_month', 'card_amount', 'sim_type'],
      checkedData: [],
    }
  },
  mounted() {
    this.getDeviceOrgs()
    this.checkGet()
    this.getData()
  },
  methods: {
    // === 列表选择性展示 start ===
    checkSave(data) { // v-checkbox组件保存后回调
      localStorage.setItem(`__${this.routeName}Check__`, JSON.stringify(data))
      this.checkGet()
    },
    checkGet() {
      let checkedData = JSON.parse(localStorage.getItem(`__${this.routeName}Check__`))
      if (checkedData) this.checkedData = checkedData
      else this.checkedData = this.defaultData
    },
    // === 列表选择性展示 end ===
    getData() {
      Api.UNITS.getListData({
        vue: this,
        url: _axios.ajaxAd.getBatchs
      })
    },
    getLiveMonthAlias(value) {
      let item = this.liveMonthSelect.filter((v) => v.value == value)[0]
      return item ? item.label : ''
    },
    getDeviceOrgs() { // 获取机构中心机构列表
      _axios.send({
        method: 'get',
        url: _axios.ajaxAd.getDeviceOrgs,
        done: ((res) => {
          this.devOrgs = res.data || []
          this.devOrgs.forEach((v) => {
            v.label = v.label + `(${v.value})`
          })
        })
      })
    },
  },
  computed: {
    // 起始时间约数
    startDatePicker() {
      return Api.UNITS.startDatePicker(this, this.formInline.date_end)
    },
    // 结束时间约数
    endDatePicker() {
      return Api.UNITS.endDatePicker(this, this.formInline.date_start)
    }
  }
}

</script>
<style lang="scss">
.card_batch {
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

  .table-expand label {
    width: 165px !important;
  }
}

</style>
