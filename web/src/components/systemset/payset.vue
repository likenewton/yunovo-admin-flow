<template>
  <div class="pay_set">
    <el-card class="clearfix" shadow="never" v-loading="loadData">
      <el-table class="payset_page" ref="listTable" @selection-change="handleSelectionChange" :data="list.data" border resizable size="mini">
        <el-table-column prop="name" label="支付方式" min-width="150" sortable></el-table-column>
        </el-table-column>
        <el-table-column align="center" min-width="150">
          <template slot-scope="scope">
            <a class="pointer" :href="scope.row.link_url" target="blank">
              <img :src="`${require('../../assets/images/' + scope.row.icon_url)}`">
            </a>
          </template>
        </el-table-column>
        <el-table-column label="管理" width="140">
          <template slot-scope="scope">
            <el-button v-if="!scope.row.is_install" type="text" class="text_success" @click="installData(scope)">安装</el-button>
            <el-button v-if="scope.row.is_install" type="text" class="text_editor" @click="editorData(scope)">编辑</el-button>
            <el-button v-if="scope.row.is_install" type="text" class="text_danger" @click="deleteData(scope)">卸载</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>
<script>

export default {
  data() {
    return {
      loadData: true,
      // 列表
      list: {
        data: []
      },
      formInline: {}
    }
  },
  mounted() {
    this.getData()
  },
  methods: {
    // 编辑
    editorData(scope) {
      this.$router.push({
        name: scope.row.form_url,
        query: {
          type: 'update',
          pay: scope.row.type
        }
      })
    },
    // 卸载
    deleteData(scope) {
      this.$confirm(`是否确认卸载${scope.row.name}?`, '提示', {
        type: 'warning'
      }).then(() => {
        _axios.send({
          method: 'post',
          url: _axios.ajaxAd.deletePay,
          data: { type: scope.row.type },
          done: ((res) => {
            this.getData()
            setTimeout(() => {
              this.$message.success(res.msg || '操作成功')
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
    // 安装
    installData(scope) {
      this.$confirm(`是否确认安装${scope.row.name}?`, '提示', {
        type: 'warning'
      }).then(() => {
        _axios.send({
          method: 'post',
          url: _axios.ajaxAd.installPay,
          data: { type: scope.row.type },
          done: ((res) => {
            this.getData()
            setTimeout(() => {
              this.$message.success(res.msg || '操作成功')
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
    // 获取列表数据
    getData() {
      this.loadData = true
      _axios.send({
        method: 'get',
        url: _axios.ajaxAd.getPays,
        done: ((res) => {
          this.list.data = res.data
          this.list.total = res.data.length
          this.loadData = false
        })
      })
    }
  }
}

</script>
<style lang="scss">
.pay_set {

  td {
    * {
      font-size: 14px;
    }
  }
}

</style>
