<template>
  <div class="card_reset">
    <el-card shadow="never">
      <el-tabs @tab-click="changeTab" v-model="tabIndex">
        <el-tab-pane v-loading="loadData">
          <span slot="label">重置操作</span>
          <el-form class="editor-form" :model="ruleForm" :rules="rules" ref="ruleForm" label-width="126px" size="small">
            <el-form-item prop="iccids">
              <span slot="label">卡ICCID列表：</span>
              <el-input type="textarea" v-model="ruleForm.iccids" rows="8"></el-input>
              <div class="annotation">一行代表一个ICCID，多行代表多个ICCID，建议不超过100个ICCID</div>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitForm('ruleForm')">保存</el-button>
              <el-button type="warning" @click="resetForm('ruleForm')">重置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane v-loading="loadData">
          <span slot="label"></i>重置历史</span>
          <el-form class="search-form" :inline="true" :model="formInline" size="small">
            <el-form-item>
              <el-input v-model="formInline.card_iccid" @input="formInline.card_iccid = limitNumber(formInline.card_iccid, 20)" @keyup.enter.native="searchData" placeholder="卡ICCID"></el-input>
            </el-form-item>
            <el-form-item>
              <el-select v-model="formInline.org_id" filterable clearable placeholder="机构名称" @change="searchData">
                <el-option v-for="(item, index) in orgs" :key="index" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-date-picker v-model="formInline.date_start" :picker-options="startDatePicker" type="date" value-format="yyyy-MM-dd" @change="searchData" placeholder="重置时间开始"></el-date-picker> -
              <el-date-picker v-model="formInline.date_end" :picker-options="endDatePicker" type="date" value-format="yyyy-MM-dd" @change="searchData" placeholder="选重置时间结束"></el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button size="small" type="primary" @click="searchData">查询</el-button>
              <el-button size="small" type="warning" @click="resetData">重置</el-button>
            </el-form-item>
          </el-form>
          <el-table ref="listTable" :data="list.data" @sort-change="handleSortChange" :max-height="maxTableHeight" border resizable size="mini">
            <el-table-column prop="card_iccid" fixed="left" label="卡ICCID" width="182" sortable="custom">
              <template slot-scope="scope">
                <span class="btn-link" @click="$router.push({ name: 'rechargeDetail', query: {card_id: scope.row.card_id}})">{{scope.row.card_iccid}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="org_id" label="机构名称" min-width="200">
              <template slot-scope="scope">
                <span>{{scope.row.org_name}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="card_used" label="已用流量" min-width="95" sortable="custom" align="right">
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.card_used)"></div>
              </template>
            </el-table-column>
            <el-table-column prop="card_unused" label="剩余流量" min-width="95" sortable="custom" align="right">
              <template slot-scope="scope">
                <div v-html="formatFlowUnit(scope.row.card_unused)"></div>
              </template>
            </el-table-column>
            <el-table-column prop="card_status" label="卡状态" min-width="95" sortable="custom">
              <template slot-scope="scope">
                <span v-if="scope.row.card_status==0">未激活</span>
                <span v-else-if="scope.row.card_status==1">正常</span>
                <span v-else>停卡</span>
              </template>
            </el-table-column>
            <el-table-column prop="owner_real" label="实名认证" min-width="95" sortable="custom">
              <template slot-scope="scope">
                <span v-if="scope.row.owner_real==1">已实名</span>
                <span v-else>未实名</span>
              </template>
            </el-table-column>
            <el-table-column prop="user_id" label="操作者" min-width="120" sortable="custom">
              <template slot-scope="scope">
                <span>{{scope.row.user_name}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="batch_time" label="出货时间" min-width="155" sortable="custom"></el-table-column>
            <el-table-column prop="time_added" label="重置时间" min-width="155" sortable="custom"></el-table-column>
          </el-table>
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="list.currentPage" :page-sizes="pageSizes" :page-size="list.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="list.total" class="clearfix">
          </el-pagination>
        </el-tab-pane>
      </el-tabs>
    </el-card>
    <el-dialog title="重置信息" :visible.sync="dialogResetVisible">
      <div slot>
        <div id="iccid_reset" style="width:100%;overflow: auto">
          <el-table :data="resetResultData" border resizable size="mini" :max-height="winHeight / 2.2">
            <el-table-column prop="iccid" label="卡iccid" min-width="200" show-overflow-tooltip></el-table-column>
            <el-table-column prop="msg" label="执行结果" min-width="200">
              <template slot-scope="scope">
                <span class="text_danger bold" v-if="scope.row.ret === '2'">{{scope.row.msg}}</span>
                <span class="text_warning bold" v-else-if="scope.row.ret === '1'">{{scope.row.msg}}</span>
                <span class="text_success bold" v-else>{{scope.row.msg}}</span>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import Api from 'assets/js/api.js'
import { mapState } from 'vuex'

export default {
  data() {
    return {
      tabIndex: '0',
      ruleForm: {},
      maxTableHeight: Api.UNITS.maxTableHeight(370),
      rules: {
        iccids: [{
          required: true,
          message: '请输入您的流量卡ICCID号',
          trigger: 'blur'
        }, {
          validator: this.validateIccidCount,
          trigger: ['blur']
        }]
      },
      resetResultData: [],
      dialogResetVisible: false
    }
  },
  mounted() {
    this.getData()
  },
  methods: {
    changeTab(para) {
      if (this.tabIndex === '1') {
        // 重置历史查询
        this.getData()
      }
    },
    getData() {
      Api.UNITS.getListData({
        vue: this,
        url: _axios.ajaxAd.getCardReset
      })
    },
    // 提交表单
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.loadData = true
          this.ruleForm.iccids = this.ruleForm.iccids.split('\n').filter((v) => v.trim()).join('\n') // 去除为空的行
          // 验证通过
          _axios.send({
            method: 'post',
            url: _axios.ajaxAd.addResetGprs,
            data: this.ruleForm,
            done: ((res) => {
              this.loadData = false
              this.dialogResetVisible = true
              this.resetResultData = res.data || []
              this.resetForm('ruleForm')
              let count = 0
              this.resetResultData.forEach((v) => {
                if (v.ret === '0') count++
              })
              setTimeout(() => {
                this.showMsgBox({
                  type: 'success',
                  message: `操作成功！本次重置 ${count} 张卡`
                })
              }, 150)
            })
          })
        } else {
          Api.UNITS.showMsgBox()
          return false;
        }
      })
    },
    validateIccidCount(rule, value, callback) {
      let iccidList = value.split('\n').filter((v) => v.trim())
      if (iccidList.length > 100) {
        callback(new Error('一次最多处理100张iccid卡'))
      } else if (iccidList.length === 0) {
        callback(new Error('请输入您的流量卡ICCID号'))
      } else {
        callback()
      }
    }
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
.card_reset {
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

  .iccid_reset {}
}

</style>
