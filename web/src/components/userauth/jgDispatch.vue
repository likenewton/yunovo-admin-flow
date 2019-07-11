<template>
  <div class="jg_dispatch">
    <el-card class="clearfix" shadow="never" v-loading="loadData">
      <el-form :inline="true" :model="formInline" class="search-form" size="small" @submit.native.prevent>
        <el-form-item>
          <el-input v-model="formInline.username" placeholder="用户账号" @keyup.enter.native="searchData"></el-input>
        </el-form-item>
        <el-form-item>
          <el-input v-model="formInline.firstname" placeholder="真实姓名" @keyup.enter.native="searchData"></el-input>
        </el-form-item>
        <el-form-item>
          <el-select v-model="formInline.org_id" filterable clearable placeholder="所属机构" @change="searchData">
            <el-option v-for="(item, index) in orgs" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchData">查询</el-button>
          <el-button type="warning" @click="resetData">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table ref="listTable" :data="list.data" @sort-change="handleSortChange" :max-height="maxTableHeight" border resizable size="mini">
        <el-table-column prop="username" fixed="left" label="用户账号" min-width="120" sortable="custom"></el-table-column>
        <el-table-column prop="firstname" label="真实姓名" show-overflow-tooltip min-width="90" sortable="custom"></el-table-column>
        <el-table-column prop="org_id" label="机构名称" min-width="135" sortable="custom">
          <template slot-scope="scope">
            <span>{{scope.row.org_name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="orgpos_name" label="可控机构" show-overflow-tooltip min-width="140" sortable="custom"></el-table-column>
        <el-table-column prop="create_by" label="创建者" show-overflow-tooltip min-width="80"></el-table-column>
        <el-table-column prop="update_by" label="更改者" show-overflow-tooltip min-width="80"></el-table-column>
        <el-table-column prop="date_added" label="创建时间" width="153" sortable="custom"></el-table-column>
        <el-table-column prop="date_last" label="更改时间" width="153" sortable="custom"></el-table-column>
        <el-table-column prop="status" label="用户状态" width="85" sortable="custom">
          <template slot-scope="scope">
            <span v-if="scope.row.status==0" class="text_danger bold">已停用</span>
            <span v-else class="text_success bold">已启用</span>
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="管理" width="140">
          <template slot-scope="scope">
            <el-button type="text" class="text_editor" @click="$router.push({name:'createdispatch', query:{type:'update',username:scope.row.username}})">编辑</el-button>
            <el-button type="text" class="text_danger" @click="deleteUser(scope)">删除</el-button>
            <el-button v-if="scope.row.status==0" type="text" class="text_success" @click="checkUserStop(scope, 1)">启用</el-button>
            <el-button v-else type="text" class="text_danger" @click="checkUserStop(scope, 0)">停用</el-button>
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

export default {
  data() {
    return {}
  },
  mounted() {
    this.getData()
  },
  methods: {
    // 获取列表数据
    getData() {
      Api.UNITS.getListData({
        vue: this,
        url: _axios.ajaxAd.getDispatchList
      })
    },
    // 删除
    deleteUser(scope) {
      this.$confirm(`是否删除该用户账号?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        _axios.send({
          method: 'post',
          url: _axios.ajaxAd.deleteUser,
          data: { username: scope.row.username },
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
    // 停用启用
    checkUserStop(scope, status) {
      this.$confirm(`是否${status === 1 ? '启用' : '停用'}该用户账号?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        _axios.send({
          method: 'post',
          url: _axios.ajaxAd.checkUserStop,
          data: { 
            status,
            username: scope.row.username
          },
          done: ((res) => {
            this.getData()
            setTimeout(() => {
              this.showMsgBox({
                type: 'success',
                message: res.msg || '操作成功！'
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

</script>
<style lang="scss">
.jg_dispatch {
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
