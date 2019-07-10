<template>
  <div class="inner-head">
    <!-- <v-token></v-token> -->
    <v-store></v-store>
    <img class="logo" src="../../assets/images/LOGO-188X56.png">
    <i v-if="showMenu" class="el-icon-fontcaidan1 pointer menu-icon" @click="collapseAside"></i>
    <el-tooltip v-if="showMenu" class="sysmenu fr pointer" effect="dark" content="回到门户" placement="bottom-end">
      <i class="el-icon-fonttuichu1" style="color:#606266" @click="toUcIndexUrl"></i>
    </el-tooltip>
    <el-dropdown trigger="click" class="pointer fr" @command="handleCommand">
      <span class="el-dropdown-link">{{userInfo.userName}}<i class="el-icon-caret-bottom el-icon--right"></i></span>
      <el-dropdown-menu slot="dropdown" style="">
        <!-- <el-dropdown-item command="modify" icon="el-icon-fontxiugaimima1">修改密码</el-dropdown-item> -->
        <el-dropdown-item command="quit" icon="el-icon-fonticon-tuichu">退出</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
    <div class="avatar fr">
      <img src="../../assets/images/liuliang.png">
    </div>
    <el-badge v-if="messageCount" :value="messageCount" class="item fr pointer" type="danger">
      <i class="el-icon-fontziyuan"></i>
    </el-badge>
  </div>
</template>
<script>
import { mapMutations, mapState } from 'vuex'

export default {
  name: 'vHead',
  data() {
    return {
      // 控制侧边栏的展示
      isShow: true,
      userInfo: {}
    }
  },
  props: {
    messageCount: {
      type: Number,
      default: 0
    },
    showMenu: {
      type: Boolean,
      default: true
    }
  },
  mounted() {
    _axios.send({
      method: 'get',
      url: _axios.ajaxAd.getLoginInfo,
      done: (res) => {
        this.userInfo = res.data
      }
    })
  },
  methods: {
    ...mapMutations([
      'SET_ASIDECOLLAPSE'
    ]),
    handleCommand(command) {
      if (command === 'modify') {
        // 这里进行修改密码的操作

      } else if (command === 'quit') {
        // 这里做一些退出的操作然后跳转注销页面
        location.href = this.userInfo.logoutUrl
      }
    },
    // 回到门户
    toUcIndexUrl() {
      location.href = this.userInfo.ucIndexUrl
    },
    collapseAside() {
      if (this.isShow && this.asideCollapse) {
        this.SET_ASIDECOLLAPSE({ asideCollapse: false })
        return
      }
      if (this.isShow) {
        $('.el-aside').addClass('el-collapse--none')
      } else {
        $('.el-aside').removeClass('el-collapse--none')
      }
      this.SET_ASIDECOLLAPSE({ asideCollapse: !this.asideCollapse })
      this.isShow = !this.isShow
    }
  },
  computed: {
    ...mapState({
      asideCollapse: 'asideCollapse'
    }),
    routename() {
      return this.$route.name
    },
    choiceArticleTitle() {
      return this.titleList[this.TYPE].filter((v) => v.ID === this.ID)[0]
    }
  }
}

</script>
<style lang="scss">
.el-header {
  font-size: 12px;
  background-color: #fff;
  border-bottom: 1px solid #D9DEE4;
  color: #333;
  line-height: 60px;

  .inner-head {
    padding: 0 20px 0 0;

    .logo {
      width: 132px;
      height: 26px;
      margin-top: -8px;
    }

    .favicon-wrapper {
      display: inline-block;
      font-size: 22px;
    }

    .menu-icon {
      color: #5A738E;
      font-size: 25px;
      vertical-align: middle;
      margin-left: 10px;
    }

    .avatar {
      position: relative;
      width: 40px;
      height: 40px;
      border-radius: 50%;
      margin: 10px 20px 0 0;
      overflow: hidden;

      img {
        position: absolute;
        width: 40px;
        height: 40px;
        top: 0;
        left: 0;
        width: 100%;
      }
    }

    .el-badge {
      display: flex;
      margin-right: 30px;
      margin-top: 20px;

      i {
        color: #c6c6c6;
        font-size: 24px;
      }
    }

    .exit-btn {
      margin-top: 15px;
      margin-left: 30px;

      * {
        font-size: 14px;
      }
    }

    .sysmenu {
      font-size: 22px;
      margin: 19px 0 0 15px;
    }
  }
}

</style>
