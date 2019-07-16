<template>
  <div class="card_auth">
    <el-card class="clearfix" shadow="never" v-loading="loadData">
      <el-button-group style="margin-bottom: 10px">
        <el-button size="small" type="warning" :disabled="!pageAuthBtn.FCP_01_007_EXPORT01" @click="exportExcel">导出实名信息</el-button>
      </el-button-group>
      <el-form :inline="true" :model="formInline" class="search-form" size="small" @submit.native.prevent>
        <el-form-item>
          <el-input v-model="formInline.card_iccid" @input="formInline.card_iccid = limitNumber(formInline.card_iccid, 20, 0)" @keyup.enter.native="simpleSearchData" placeholder="卡ICCID"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="simpleSearchData" :disabled="!pageAuthBtn.FCP_01_007_CHECK01">查询</el-button>
          <el-button type="primary" @click="searchVipVisible = true" :disabled="!pageAuthBtn.FCP_01_007_CHECK01">高级查询</el-button>
        </el-form-item>
      </el-form>
      <el-table ref="listTable" :data="list.data" @sort-change="handleSortChange" :max-height="maxTableHeight" border resizable size="mini">
        <el-table-column prop="card_iccid" fixed="left" label="卡ICCID" width="182" sortable="custom">
          <template slot-scope="scope">
            <span v-if="pageAuthBtn.FCP_01_007_LINK01" class="btn-link" @click="$router.push({ name: 'rechargeDetail', query: {card_id: scope.row.card_id}})">{{scope.row.card_iccid}}</span>
            <span v-else>{{scope.row.card_iccid}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="org_id" label="机构名称" width="140" sortable="custom">
          <template slot-scope="scope">
            <span>{{scope.row.org_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="owner_name" label="姓名" :width="widthMap.owner_name[size]" sortable="custom"></el-table-column>
        <el-table-column prop="owner_gender" label="姓别" :width="widthMap.owner_gender[size]" sortable="custom">
          <template slot-scope="scope">
            <span v-if="scope.row.owner_gender==1">男性</span>
            <span v-else-if="scope.row.owner_gender==2">女性</span>
            <span v-else>保密</span>
          </template>
        </el-table-column>
        <el-table-column prop="owner_cdi" label="身份证编号" width="175"></el-table-column>
        <el-table-column prop="time_last" label="最后上报" :min-width="widthMap.time_last[size]" sortable="custom"></el-table-column>
        <el-table-column prop="time_active" label="卡激活时间" :min-width="widthMap.time_active[size]" sortable="custom"></el-table-column>
        <el-table-column prop="time_audit" label="审核时间" :min-width="widthMap.time_audit[size]" sortable="custom"></el-table-column>
        <el-table-column prop="time_added" label="申请时间" :min-width="widthMap.time_added[size]" sortable="custom"></el-table-column>
        <el-table-column prop="cdi_status" label="审核状态" min-width="90" sortable="custom">
          <template slot-scope="scope">
            <span v-if="scope.row.cdi_status==1">无效</span>
            <span v-else-if="scope.row.cdi_status==2">有效</span>
            <span v-else>待审核</span>
          </template>
        </el-table-column>
        <el-table-column prop="user_id" label="审核者" :min-width="widthMap.user_id[size]">
          <template slot-scope="scope">
            <span>{{scope.row.first_name}}</span>
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="操作" :width="widthMap.op[size]" v-if="pageAuthBtn.FCP_01_007_OP01">
          <template slot-scope="scope">
            <el-button type="text" v-if="scope.row.cdi_status == 2" @click="showDialog(scope)">查看实名证件</el-button>
            <el-button type="text" v-else @click="showDialog(scope)">审核实名证件</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="list.currentPage" :page-sizes="pageSizes" :page-size="list.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="list.total" class="clearfix">
      </el-pagination>
    </el-card>
    <el-dialog title="机构卡实名审核" :visible.sync="authDialogVisible" top="6%" :close-on-click-modal="false">
      <div slot class="auth_dialog">
        <div class="content" :style="{'height': winHeight/2.2 + 'px'}">
          <div class="item">
            <span class="demonstration">身份证正面图</span>
            <el-image :src="choiceItem.cdi_img1" fit="contain">
              <div slot="error" class="image-slot">
                <img src="../../assets/images/no-image-800x500.jpg">
              </div>
            </el-image>
          </div>
          <div class="item">
            <span class="demonstration">身份证背面图</span>
            <el-image :src="choiceItem.cdi_img2" fit="contain">
              <div slot="error" class="image-slot">
                <img src="../../assets/images/no-image-800x500.jpg">
              </div>
            </el-image>
          </div>
          <div class="item">
            <span class="demonstration">手持身份证照</span>
            <el-image :src="choiceItem.cdi_img3" fit="contain">
              <div slot="error" class="image-slot">
                <img src="../../assets/images/no-image-800x500.jpg">
              </div>
            </el-image>
          </div>
        </div>
      </div>
      <div slot="footer">
        <el-button size="small" v-if="choiceItem.cdi_status != 2" type="danger" @click="authInvalid">实名信息无效</el-button>
        <el-button size="small" v-if="choiceItem.cdi_status != 2" type="success" @click="authValid">实名信息通过</el-button>
        <el-button size="small" v-if="choiceItem.cdi_status == 2" type="danger" @click="relieveAuth">解除实名绑定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="高级查询" :visible.sync="searchVipVisible" width="630px" :close-on-click-modal="false">
      <div slot>
        <div class="searchForm_vip" style="width:100%;overflow: auto">
          <el-form :inline="false" :model="formInline" size="small" label-width="90px" v-loading="loadData">
            <el-form-item label="卡ICCID">
              <el-input v-model="formInline.card_iccid" @input="formInline.card_iccid = limitNumber(formInline.card_iccid, 20, 0)" placeholder="卡ICCID"></el-input>
            </el-form-item>
            <el-form-item label="机构名称">
              <el-select v-model="formInline.org_id" filterable clearable placeholder="机构名称">
                <el-option v-for="(item, index) in orgs" :key="index" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="审核状态">
              <el-select v-model="formInline.status" clearable placeholder="审核状态">
                <el-option label="待审核" value="0"></el-option>
                <el-option label="无效" value="1"></el-option>
                <el-option label="有效" value="2"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="申请时间">
              <el-date-picker v-model="formInline.date_start" :picker-options="startDatePicker" type="date" value-format="yyyy-MM-dd" placeholder="申请时间开始"></el-date-picker> -
              <el-date-picker v-model="formInline.date_end" :picker-options="endDatePicker" type="date" value-format="yyyy-MM-dd" placeholder="申请时间结束"></el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="searchData">查询</el-button>
              <el-button type="warning" @click="resetData">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
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
      choiceItem: {}, // 当前选中项
      formInline: {
        card_iccid: Api.UNITS.getQuery('card_iccid')
      },
      size: Api.UNITS.getSize(),
      widthMap: {
        owner_name: [90, 65],
        owner_gender: [80, 65],
        time_last: [155, 95],
        time_active: [155, 100],
        time_audit: [155, 95],
        time_added: [155, 95],
        user_id: [100, 65],
        op: [115, 108],
      },
      authDialogVisible: false
    }
  },
  mounted() {
    this.getData()
  },
  methods: {
    // 导出excel
    exportExcel() {
      Api.UNITS.exportExcel(_axios.ajaxAd.cardAuthExport, this.formInline)
    },
    // 简单查询
    simpleSearchData() {
      let card_iccid = this.formInline.card_iccid
      this.formInline = { card_iccid }
      this.searchData()
    },
    resetData() {
      this.list.currentPage = 1
      this.formInline = {
        card_iccid: Api.UNITS.getQuery('card_iccid')
      } // 1、重置查询表单
      this.sort = {} // 2、重置排序
      this.$refs.listTable.clearSort() // 3、清空排序样式
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
      this.choiceItem = scope.row
      this.authDialogVisible = true
    },
    // 实名认证无效
    authInvalid() {
      this.$confirm(`该实名信息是否无效?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        _axios.send({
          method: 'post',
          url: _axios.ajaxAd.checkAudit,
          data: {
            card_id: this.choiceItem.card_id,
            status: 1 // 1无效，2有效
          },
          done: ((res) => {
            this.authDialogVisible = false
            this.getData()
            setTimeout(() => {
              this.showMsgBox({
                type: 'success',
                message: '操作成功，实名信息已无效！'
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
    // 实名验证通过
    authValid() {
      this.$confirm(`该实名信息是否通过?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        _axios.send({
          method: 'post',
          url: _axios.ajaxAd.checkAudit,
          data: {
            card_id: this.choiceItem.card_id,
            status: 2 // 1无效，2有效
          },
          done: ((res) => {
            this.authDialogVisible = false
            this.getData()
            setTimeout(() => {
              this.showMsgBox({
                type: 'success',
                message: '操作成功，实名信息已通过！'
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
    // 解除实名绑定
    relieveAuth() {
      if (this.choiceItem.card_iccid.indexOf('_') > -1) {
        this.showMsgBox({
          type: 'warning',
          message: '该卡尚未进行实名绑定'
        })
        return
      }
      this.$confirm(`是否解除该实名绑定？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        _axios.send({
          method: 'post',
          url: _axios.ajaxAd.unbindRealname,
          data: { card_id: this.choiceItem.card_id },
          done: ((res) => {
            this.authDialogVisible = false
            this.getData()
            setTimeout(() => {
              this.showMsgBox({
                type: 'success',
                message: '操作成功，实名信息绑定已解除！'
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
      width: 300px;
      margin: 0 auto;
      text-align: center;

      .demonstration {
        display: inline-block;
        line-height: 20px;
        margin: 20px 0;
        font-size: 18px;
      }
    }
  }
}

</style>
