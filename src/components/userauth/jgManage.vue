<template>
  <div class="jg_manage">
    <el-card style="margin-bottom: 20px" shadow="never">
      <el-form :inline="true" :model="formInline" class="demo-form-inline" size="small">
        <el-form-item label="机构名称">
          <el-select v-model="formInline.org_id" filterable clearable placeholder="请选择">
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
        <el-button size="mini" type="success" @click="$router.push({ name: 'createjg' })">新增</el-button>
        <el-button size="mini" type="danger" @click="deleteOrgs">删除</el-button>
      </el-button-group>
      <el-table ref="listTable" :data="list.data" @sort-change="handleSortChange" @selection-change="handleSelectionChange" :row-key="getRowKeys" :max-height="maxTableHeight" border resizable size="mini">
        <el-table-column fixed="left" type="selection" :reserve-selection="true" min-width="60"></el-table-column>
        <el-table-column prop="org_id" fixed="left" label="机构名称" min-width="140" sortable="custom">
          <template slot-scope="scope">
            <span class="btn-link">{{scope.row.name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="partner_id" label="合作编号" min-width="140" sortable="custom"></el-table-column>
        <el-table-column prop="partner_key" label="合作秘钥" min-width="240" sortable="custom"></el-table-column>
        <el-table-column prop="notify_url" label="异步通知地址" min-width="215" sortable="custom"></el-table-column>
        <el-table-column prop="user_total" label="可开账户数量" width="113" sortable="custom"></el-table-column>
        <el-table-column prop="email" label="负责人邮箱" min-width="105" sortable="custom"></el-table-column>
        <el-table-column prop="tel" label="负责人手机" min-width="108" sortable="custom"></el-table-column>
        <el-table-column prop="rebate_value" label="返利比率" width="87" sortable="custom"></el-table-column>
        <el-table-column prop="memo" label="机构描述" min-width="160" sortable="custom"></el-table-column>
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
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :page-sizes="pageSizes" :page-size="list.pagesize" layout="total, sizes, prev, pager, next, jumper" :total="list.total" class="clearfix">
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
      loadData: true,
      pageSizes: Api.STATIC.pageSizes,
      // 列表
      list: {
        data: [],
        pagesize: Api.STATIC.pageSizes[1],
        currentPage: 1,
        total: 0,
      },
      // 在列表中选择的数据
      selectData: [],
      sort: {},
      formInline: {},
      maxTableHeight: Api.UNITS.maxTableHeight(),
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
    handleSizeChange(val) {
      this.list.pagesize = val
      this.getData()
    },
    handleCurrentChange(val) {
      this.list.currentPage = val
      this.getData()
    },
    handleSortChange(val = {}) {
      Api.UNITS.setSortSearch(val, this)
      this.getData()
    },
    // 处理列表多选
    handleSelectionChange(selectData) {
      this.selectData = selectData
    },
    // 查询
    searchData() {
      this.list.currentPage = 1
      this.getData()
    },
    // 重置列表
    resetData() {
      this.list.currentPage = 1 // 0、重置列表，当前页要重置到 1
      this.formInline = {} // 1、重置查询表单
      this.sort = {} // 2、重置排序
      this.$refs.listTable.clearSort() // 3、清空排序样式
      this.$refs.listTable.clearSelection() // 4、清空选中样式
      this.getData()
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
              this.$message.success(res.msg || '删除成功')
            }, 150)
          })
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '该操作已取消'
        })
      })
    },
    // 批量删除机构
    deleteOrgs() {
      if (this.selectData.length === 0) {
        this.$message.warning('请先勾选要卸载的项')
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
                this.$message.success(res.msg || '删除成功')
              }, 150)
            })
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '该操作已取消'
          })
        })
      }
    },
    formatFlowUnit: Api.UNITS.formatFlowUnit,
    calcLeftTime: Api.UNITS.calcLeftTime
  },
  computed: {
    ...mapState({
      orgs: 'orgs',
    })
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
