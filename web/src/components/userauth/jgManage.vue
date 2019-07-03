<template>
  <div class="jg_manage">
    <el-card class="clearfix" shadow="never" v-loading="loadData">
      <el-button-group style="margin-bottom: 10px">
        <el-button size="small" type="success" @click="$router.push({ name: 'createjg' })">新增</el-button>
        <el-button size="small" type="danger" @click="deleteOrgs">删除</el-button>
      </el-button-group>
      <el-form :inline="true" :model="formInline" class="search-form" size="small" @submit.native.prevent>
        <el-form-item>
          <el-select v-model="formInline.org_id" filterable clearable placeholder="机构名称" @change="searchData">
            <el-option v-for="(item, index) in orgs" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchData">查询</el-button>
          <el-button type="warning" @click="resetData">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table ref="listTable" :data="list.data" @sort-change="handleSortChange" @selection-change="handleSelectionChange" :row-key="getRowKeys" :max-height="maxTableHeight" border resizable size="mini">
        <el-table-column fixed="left" type="selection" :reserve-selection="true" min-width="60"></el-table-column>
        <el-table-column prop="org_id" fixed="left" label="机构名称" min-width="160" sortable="custom">
          <template slot-scope="scope">
            <span class="btn-link" @click="$router.push({name: 'card', query: {org_id: scope.row.org_id}})">{{scope.row.name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="partner_id" label="合作编号" min-width="140" sortable="custom"></el-table-column>
        <el-table-column prop="partner_key" label="合作秘钥" width="150" sortable="custom"></el-table-column>
        <el-table-column prop="notify_url" label="异步通知地址" min-width="215" sortable="custom"></el-table-column>
        <el-table-column prop="user_total" label="可开账户数量" width="110" sortable="custom" align="right"></el-table-column>
        <el-table-column prop="email" label="负责人邮箱" min-width="105" sortable="custom"></el-table-column>
        <el-table-column prop="tel" label="负责人手机" min-width="108" sortable="custom"></el-table-column>
        <el-table-column prop="rebate_value" label="返利比率" width="90" sortable="custom" align="right"></el-table-column>
        <el-table-column prop="memo" label="机构描述" show-overflow-tooltip min-width="160" sortable="custom"></el-table-column>
        <el-table-column prop="user_id" label="创建者" min-width="80" sortable="custom">
          <template slot-scope="scope">
            <span>{{scope.row.create_by_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="alter_id" label="更改者" min-width="80" sortable="custom">
          <template slot-scope="scope">
            <span>{{scope.row.update_by_name}}</span>
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="管理" width="100">
          <template slot-scope="scope">
            <el-button type="text" class="text_editor" @click="$router.push({ name: 'createjg', query: { type: 'update', org_id: scope.row.org_id } })">编辑</el-button>
            <el-button type="text" class="text_danger" @click="deleteOrg(scope)">删除</el-button>
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
      selectData: []
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
