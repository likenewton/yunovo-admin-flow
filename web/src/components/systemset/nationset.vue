<template>
  <div class="nation_set">
    <el-card class="clearfix" shadow="never" v-loading="loadData">
      <el-breadcrumb separator="/" style="margin-bottom: 10px">
        <span class="el-icon-fonticonset0403" style="display: inline-block;margin-right: 10px;font-size: 22px;color: lightblue"></span>
        <el-breadcrumb-item :to="{name: 'nationset'}">国家区域</el-breadcrumb-item>
        <el-breadcrumb-item v-for="(item, index) in list.other" :key="index" :to="{name: 'nationset', query: {ntid: item.ntid}}">{{item.ntname}}</el-breadcrumb-item>
      </el-breadcrumb>
      <el-button-group style="margin-bottom: 10px">
        <el-button size="small" type="success" @click="addData">新增</el-button>
        <el-button size="small" type="danger" @click="deleteDatas">删除</el-button>
      </el-button-group>
      <el-table ref="listTable" :data="list.data" @selection-change="handleSelectionChange" @sort-change="handleSortChange" :max-height="maxTableHeight" border resizable size="mini">
        <el-table-column fixed="left" type="selection" min-width="60"></el-table-column>
        <el-table-column prop="ntid" label="区域名称" min-width="150" sortable="custom">
          <template slot-scope="scope">
            <span class="btn-link" @click="$router.push({name: 'nationset', query: {ntid: scope.row.ntid}})">{{scope.row.ntname}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="zipcode" label="邮政编码" min-width="140" sortable="custom"></el-table-column>
        <el-table-column label="管理" width="120">
          <template slot-scope="scope">
            <el-button type="text" class="text_editor" @click="editor(scope)">编辑</el-button>
            <el-button type="text" class="text_danger" @click="deleteData(scope)">删除</el-button>
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
    return {
      // 列表
      list: {
        data: [],
        other: [],
        pagesize: Api.STATIC.pageSizes[3],
        currentPage: 1,
        total: 0,
      },
      // 在列表中选择的数据
      selectData: [],
      formInline: {
        ntid: Api.UNITS.getQuery('ntid')
      },
      maxTableHeight: Api.UNITS.maxTableHeight(354)
    }
  },
  mounted() {
    this.getData()
  },
  methods: {
    // 处理列表选择
    handleSelectionChange(data) {
      this.selectData = data
    },
    editor(scope) {
      this.$router.push({
        name: 'createnation',
        query: {
          type: 'update',
          ntid: scope.row.ntid // 编辑必须要携带ntid
        }
      })
    },
    // 单项删除
    deleteData(scope) {
      this.$confirm(`是否删除该地区?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        _axios.send({
          method: 'post',
          url: _axios.ajaxAd.deleteNation,
          data: {
            ntids: [scope.row.ntid]
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
          message: '操作已取消'
        })
      })
    },
    // 批量删除
    deleteDatas() {
      if (this.selectData.length === 0) {
        this.$message.warning('请先勾选要删除的项')
      } else {
        this.$confirm(`您选中了${this.selectData.length}项，是否确认删除?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          _axios.send({
            method: 'post',
            url: _axios.ajaxAd.deleteNation,
            data: {
              ntids: this.selectData.map((v) => v.ntid)
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
            message: '操作已取消'
          })
        })
      }
    },
    addData() {
      this.$router.push({
        name: 'createnation',
        query: {
          parent: this.list.data[0].parent // 添加必须要携带parent
        }
      })
    },
    // 获取列表数据
    getData() {
      Api.UNITS.getListData({
        vue: this,
        url: _axios.ajaxAd.getNations,
        cb: ((res) => {
          // list中的other主要用于面包屑导航
          this.list.other = (res.data.other || []).reverse()
        })
      })
    }
  },
  watch: {
    '$route': function() {
      this.formInline.ntid = Api.UNITS.getQuery('ntid')
      this.list.currentPage = 1 // 路由发生改变的时候重置当前页到第一页
      this.getData()
    }
  }
}

</script>
<style lang="scss">
.nation_set {
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
}

</style>
