<template>
  <div class="jg_manage">
    <el-card class="clearfix" shadow="never" v-loading="loadData">
      <el-button-group style="margin-bottom: 10px">
        <el-button size="small" type="success" @click="$router.push({ name: 'createjg' })" :disabled="!pageAuthBtn.FPC_04_001_ADD01">新增</el-button>
        <el-button size="small" type="danger" @click="deleteOrgs" :disabled="!pageAuthBtn.FCP_04_001_DELETE01">删除</el-button>
      </el-button-group>
      <el-form :inline="true" :model="formInline" class="search-form" size="small" @submit.native.prevent>
        <el-form-item>
          <el-select v-model="formInline.org_id" filterable clearable placeholder="机构名称" @change="searchData">
            <el-option v-for="(item, index) in orgs" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchData" :disabled="!pageAuthBtn.FPC_04_001_CHECK01">查询</el-button>
          <el-button type="warning" @click="resetData" :disabled="!pageAuthBtn.FPC_04_001_CHECK01">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table ref="listTable" :data="list.data" @sort-change="handleSortChange" @selection-change="handleSelectionChange" :row-key="getRowKeys" :max-height="maxTableHeight" border resizable size="mini">
        <el-table-column fixed="left" type="selection" :reserve-selection="true" min-width="60"></el-table-column>
        <el-table-column prop="org_id" fixed="left" label="机构名称" :min-width="widthMap.org_id[size]" sortable="custom">
          <template slot-scope="scope">
            <span v-if="pageAuthBtn.FCP_04_001_LINK01" class="btn-link" @click="$router.push({name: 'card', query: {org_id: scope.row.org_id}})">{{scope.row.name}}</span>
            <span v-else>{{scope.row.name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="partner_id" label="合作编号" :min-width="widthMap.partner_id[size]" sortable="custom"></el-table-column>
        <el-table-column prop="partner_key" label="合作秘钥" :width="widthMap.partner_key[size]" sortable="custom"></el-table-column>
        <el-table-column prop="notify_url" label="异步通知地址" :min-width="widthMap.notify_url[size]" sortable="custom"></el-table-column>
        <el-table-column prop="user_total" label="可开账户数量" width="110" sortable="custom" align="right"></el-table-column>
        <el-table-column prop="email" label="负责人邮箱" :min-width="widthMap.email[size]" sortable="custom"></el-table-column>
        <el-table-column prop="tel" label="负责人手机" min-width="108" sortable="custom"></el-table-column>
        <el-table-column prop="rebate_value" label="返利比率" width="87" sortable="custom" align="right"></el-table-column>
        <el-table-column prop="memo" label="机构描述" show-overflow-tooltip :min-width="widthMap.memo[size]" sortable="custom"></el-table-column>
        <el-table-column prop="user_id" label="创建者" :min-width="widthMap.user_id[size]" sortable="custom">
          <template slot-scope="scope">
            <span>{{scope.row.create_by_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="alter_id" label="更改者" :min-width="widthMap.alter_id[size]" sortable="custom">
          <template slot-scope="scope">
            <span>{{scope.row.update_by_name}}</span>
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="管理" width="88" v-if="pageAuthBtn.FCP_04_001_UPDATE01 || pageAuthBtn.FCP_04_001_DELETE01">
          <template slot-scope="scope">
            <el-button v-if="pageAuthBtn.FCP_04_001_UPDATE01" type="text" class="text_editor" @click="$router.push({ name: 'createjg', query: { type: 'update', org_id: scope.row.org_id } })">编辑</el-button>
            <el-button v-if="pageAuthBtn.FCP_04_001_DELETE01" type="text" class="text_danger" @click="deleteOrg(scope)">删除</el-button>
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
      // 在列表中选择的数据
      selectData: [],
      size: Api.UNITS.getSize(),
      widthMap: {
        org_id: [160, 126],
        partner_key: [150, 115],
        notify_url: [215, 130],
        memo: [160, 120],
        user_id: [80, 74],
        alter_id: [80, 74],
        email: [105, 98],
        partner_id: [140, 100],
      },
    }
  },
  mounted() {
    this.getData()
  },
  methods: {
    // 以org_id作为select的唯一标识
    getRowKeys(row) {
      return row.org_id
    },
    // 处理列表多选
    handleSelectionChange(selectData) {
      this.selectData = selectData
    },
    // 获取列表数据
    getData() {
      Api.UNITS.getListData({
        vue: this,
        url: _axios.ajaxAd.getOrgList
      })
    },
    // 删除机构
    deleteOrg(scope) {
      this.$confirm(`是否确认删除该机构?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        _axios.send({
          method: 'post',
          url: _axios.ajaxAd.deleteOrg,
          data: {
            orgs: [scope.row.org_id]
          },
          done: ((res) => {
            // 不管怎么样，如果该项被删除了，一定要清空该项选择
            this.$refs.listTable.toggleRowSelection(scope.row, false)
            this.getData()
            setTimeout(() => {
              this.showMsgBox({
                type: 'success',
                message: res.msg || '删除成功！'
              })
            }, 150)
          })
        })
      }).catch(() => {
        this.showMsgBox({
          type: 'info',
          message: '该操作已取消！'
        })
      })
    },
    // 批量删除机构
    deleteOrgs() {
      if (this.selectData.length === 0) {
        this.showMsgBox({
          type: 'warning',
          message: '请先勾选要卸载的机构！'
        })
      } else {
        this.$confirm(`您选中了${this.selectData.length}项，是否确认删除?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          _axios.send({
            method: 'post',
            url: _axios.ajaxAd.deleteOrg,
            data: {
              orgs: this.selectData.map((v) => v.org_id)
            },
            done: ((res) => {
              // 如果是批量删除就要清空所有的选择
              this.$refs.listTable.clearSelection()
              this.getData()
              setTimeout(() => {
                this.showMsgBox({
                  type: 'success',
                  message: res.msg || '删除成功！'
                })
              }, 150)
            })
          })
        }).catch(() => {
          this.showMsgBox({
            type: 'info',
            message: '该操作已取消！'
          })
        })
      }
    }
  }
}

</script>
<style lang="scss">
.jg_manage {
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
