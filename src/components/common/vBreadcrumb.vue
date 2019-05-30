<template>
  <el-breadcrumb v-if="asideFlag.length > 0" separator-class="el-icon-arrow-right">
    <el-breadcrumb-item :to="{name: getBreadRouteName(item)}" v-for="item in asideFlag" :key="item">{{item}}</el-breadcrumb-item>
    <el-breadcrumb-item v-if="thirdTitle">{{thirdTitle}}</el-breadcrumb-item>
  </el-breadcrumb>
</template>
<script>
import Api from 'assets/js/api.js'
import { mapState } from 'vuex'

export default {
  name: 'vBreadcrumb',
  data() {
    return {
      operData: Api.STATIC.operData
    }
  },
  methods: {
    // 获取相应状态的标题
    getBranchTitle(data) {
      if (typeof data === 'string') {
        return data
      } else {
        if (this.$route.query.type) {
          return data[this.$route.query.type]
        } else {
          return data['create']
        }
      }
    },
    // 获取跳转路由
    getBreadRouteName(title) {
      let routeName = ''
      if (title === '首页') {
        return 'home'
      }
      this.isShowBreadCrumb = true
      this.authMenu.forEach((v1) => {
        if (v1.title === title) {
          routeName = v1.name
          return false
        }
        v1.children.forEach((v2) => {
          if (v2.title === title) {
            routeName = v2.name
            return false
          }
        })
      })
      return routeName
    }
  },
  computed: {
    ...mapState({
      asideFlag: 'asideFlag',
      authMenu: 'authMenu'
    }),
    routeName() {
      return this.$route.name
    },
    thirdTitle() {
      let thirdTitle = ''
      this.operData.forEach((v) => {
        if (v.name === this.$route.name) {
          thirdTitle = this.getBranchTitle(v.title)
        }
      })
      return thirdTitle
    }
  },
  watch: {
    '$route': function(newVal, oldVal) {

    }
  }
}

</script>
<style lang="scss">
.el-breadcrumb {
  position: relative;
  display: flex;
  width: 100%;
  align-items: center;
  height: 35px;
  min-height: 35px;
  padding: 0 20px;

  * {
    font-size: 14px;
  }
}

</style>
