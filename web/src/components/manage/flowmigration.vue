<template>
  <div class="flow_migration">
    <el-card shadow="never">
      <el-tabs @tab-click="changeTab" v-model="tabIndex">
        <el-tab-pane v-loading="loadData">
          <span slot="label">流量迁移</span>
          <el-form class="editor-form" :model="formInline" :rules="rules" ref="ruleForm" label-width="126px" size="small">
            <el-form-item prop="old_card_iccid">
              <span slot="label">旧卡ICCID：</span>
              <el-input v-model="formInline.old_card_iccid" @input="formInline.old_card_iccid = limitNumber(formInline.old_card_iccid, 20)" placeholder="请输入旧卡ICCID"></el-input>
            </el-form-item>
            <el-form-item prop="new_card_iccid">
              <span slot="label">新卡ICCID：</span>
              <el-input v-model="formInline.new_card_iccid" @input="formInline.new_card_iccid = limitNumber(formInline.new_card_iccid, 20)" placeholder="请输入新卡ICCID"></el-input>
            </el-form-item>
            <el-form-item prop="move_memo">
              <span slot="label">迁移备注：</span>
              <el-input type="textarea" v-model="formInline.move_memo" placeholder="" rows="4"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitForm('ruleForm')" :disabled="!pageAuthBtn.FCP_01_006_ADD01">保存</el-button>
              <el-button type="warning" @click="resetForm('ruleForm')">重置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane>
          <span slot="label"></i>历史迁移</span>
          <el-form class="search-form" :inline="true" :model="searchForm" size="small">
            <el-form-item>
              <el-input v-model="searchForm.old_card_iccid" @input="searchForm.old_card_iccid = limitNumber(searchForm.old_card_iccid, 20)" @keyup.enter.native="searchData" placeholder="旧卡ICCID"></el-input>
            </el-form-item>
            <el-form-item>
              <el-input v-model="searchForm.card_iccid" @input="searchForm.card_iccid = limitNumber(searchForm.card_iccid, 20)" @keyup.enter.native="searchData" placeholder="新卡ICCID"></el-input>
            </el-form-item>
            <el-form-item>
              <el-select v-model="searchForm.org_id" filterable clearable placeholder="新卡机构" @change="searchData">
                <el-option v-for="(item, index) in orgs" :key="index" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-date-picker v-model="searchForm.date_start" :picker-options="startDatePicker" type="date" value-format="yyyy-MM-dd" @change="searchData" placeholder="迁移时间开始"></el-date-picker> -
              <el-date-picker v-model="searchForm.date_end" :picker-options="endDatePicker" type="date" value-format="yyyy-MM-dd" @change="searchData" placeholder="迁移时间结束"></el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="searchData" :disabled="!pageAuthBtn.FCP_01_006_CHECK01">查询</el-button>
              <el-button type="warning" @click="resetData" :disabled="!pageAuthBtn.FCP_01_006_CHECK01">重置</el-button>
            </el-form-item>
          </el-form>
          <el-table v-loading="loadData" ref="listTable" :data="list.data" @sort-change="handleSortChange" :max-height="maxTableHeight" border resizable size="mini">
            <el-table-column prop="old_iccid" label="旧卡ICCID" min-width="180" sortable="custom">
              <template slot-scope="scope">
                <span class="btn-link" @click="toUnicomLink(scope.row.old_iccid)">{{scope.row.old_iccid}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="old_orgid" label="旧卡机构名称" min-width="160" sortable="custom">
              <template slot-scope="scope">
                <span>{{scope.row.old_org_name}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="new_iccid" label="新卡ICCID" min-width="180" sortable="custom">
              <template slot-scope="scope">
                <span class="btn-link" @click="toUnicomLink(scope.row.new_iccid)">{{scope.row.new_iccid}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="new_orgid" label="新卡机构名称" min-width="160" sortable="custom">
              <template slot-scope="scope">
                <span>{{scope.row.org_name}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="move_memo" label="迁移备注" show-overflow-tooltip min-width="160" sortable="custom"></el-table-column>
            <el-table-column prop="user_id" label="操作者" min-width="80" sortable="custom">
              <template slot-scope="scope">
                <span>{{scope.row.first_name}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="time_added" label="迁移时间" min-width="155" sortable="custom"></el-table-column>
          </el-table>
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="list.currentPage" :page-sizes="pageSizes" :page-size="list.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="list.total">
          </el-pagination>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>
<script>
import Api from 'assets/js/api.js'
import { mapState } from 'vuex'

export default {
  data() {
    return {
      maxTableHeight: Api.UNITS.maxTableHeight(370),
      tabIndex: Api.UNITS.getQuery('tabIndex') || '0',
      searchForm: {
        old_card_iccid: Api.UNITS.getQuery('old_card_iccid'),
        card_iccid: Api.UNITS.getQuery('card_iccid')
      },
      rules: {
        old_card_iccid: [{
          required: true,
          message: '请输入旧卡ICCID',
          trigger: 'blur'
        }],
        new_card_iccid: [{
          required: true,
          message: '请输入新卡ICCID',
          trigger: 'blur'
        }],
        move_memo: [{
          max: 200,
          message: '最多200个字符',
          trigger: ['blur', 'change']
        }]
      }
    }
  },
  mounted() {
    this.getData()
  },
  methods: {
    changeTab(para) {
      // 当切换tab栏到'1'的时候，如果没有数据要加载数据
      if (this.tabIndex === '1') {
        // 这里应当是ajax请求数据
        this.getData()
      }
    },
    // 重置列表
    resetData() {
      this.list.currentPage = 1
      this.searchForm = {
        old_card_iccid: Api.UNITS.getQuery('old_card_iccid'),
        card_iccid: Api.UNITS.getQuery('card_iccid')
      } // 1、重置查询表单
      this.sort = {} // 2、重置排序
      this.$refs.listTable.clearSort() // 3、清空排序样式
      this.getData()
    },
    // 提交表单
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.loadData = true
          // 验证通过
          _axios.send({
            method: 'post',
            url: _axios.ajaxAd.addCardMove,
            data: this.formInline,
            done: ((res) => {
              this.loadData = false
              if (res.status === 400) {
                this.formInline[res.data] = ''
                this.$refs.ruleForm.validateField([res.data])
              } else {
                this.resetForm('ruleForm')
                setTimeout(() => {
                  this.showMsgBox({
                    type: 'success',
                    message: '迁移成功'
                  })
                }, 150)
              }
            })
          })
        } else {
          Api.UNITS.showMsgBox()
          return false
        }
      });
    },
    checkRechargeDetail(scope) {
      this.$router.push({ name: 'rechargeDetail', query: { card_iccid: scope.row.card_iccid } })
    },
    getData() {
      Api.UNITS.getListData({
        vue: this,
        url: _axios.ajaxAd.getMoves,
        formInline: 'searchForm'
      })
    }
  },
  computed: {
    // 起始时间约数
    startDatePicker() {
      return Api.UNITS.startDatePicker(this, this.searchForm.date_end)
    },
    // 结束时间约数
    endDatePicker() {
      return Api.UNITS.endDatePicker(this, this.searchForm.date_start)
    }
  }
}

</script>
<style lang="scss">
.flow_migration {
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

}

</style>
