import Api from 'assets/js/api.js'
import { mapState, mapMutations } from 'vuex'

// 全局混入对象
export default {
  data() {
    return {
      loadData: true,
      searchVipVisible: false, // 是否显示高级查询dialog
      now: new Date().getTime(),
      pageSizes: Api.STATIC.pageSizes,
      maxTableHeight: Api.UNITS.maxTableHeight(),
      list: {
        data: [],
        pagesize: Api.STATIC.pageSizes[2],
        currentPage: 1,
        total: 0,
      },
      sort: {},
      formInline: {},
      winHeight: $(window).height(),
    }
  },
  methods: {
    ...mapMutations([
      'SET_ASIDEFLAG',
      'SET_ASIDECOLLAPSE',
      'SET_DIALOGVISIBLE'
    ]),
    // 操作导航条
    handleSizeChange(val) {
      this.list.pagesize = val
      this.list.currentPage = 1
      this.getData()
    },
    handleCurrentChange(val) {
      this.list.currentPage = val
      this.getData()
    },
    // 处理排序
    handleSortChange(val) {
      Api.UNITS.setSortSearch(val, this)
      this.getData()
    },
    // 表单
    resetData() {
      this.list.currentPage = 1
      this.formInline = {}
      this.sort = {}
      this.$refs.listTable.clearSort()
      this.$refs.listTable.clearSelection()
      this.getData()
    },
    searchData() {
      this.list.currentPage = 1
      this.getData()
    },
    resetForm(formName) {
      this.$refs[formName].resetFields()
      this.formInline = {}
      this.isUpdate && this.getData()
    },
    // 本地修改list.data中的值(因数据的修改存在延迟不好直接getData)
    modifiyData(listData, itemData, modifiyKey, modifiyValue) {
      let stringifyItemData = JSON.stringify(itemData)
      listData.forEach((v, i) => {
        if (JSON.stringify(v) === stringifyItemData) {
          listData[i][modifiyKey] = modifiyValue
        }
      })
    },
    expNumStr: Api.UNITS.expNumStr,
    showMsgBox: Api.UNITS.showMsgBox,
    showCfmBox: Api.UNITS.showCfmBox,
    limitNumber: Api.UNITS.limitNumber,
    formatMoney: Api.UNITS.formatMoney,
    toUnicomLink: Api.UNITS.toUnicomLink,
    calcLeftTime: Api.UNITS.calcLeftTime,
    formatFlowUnit: Api.UNITS.formatFlowUnit,
    formatComboFlow: Api.UNITS.formatComboFlow,
  },
  computed: {
    ...mapState({
      orgs: 'orgs', // 机构
      months: 'months', // 月份
      simType: 'simType', // sim卡类型
      authMenu: 'authMenu', // 菜单权限
      paySelect: 'paySelect',
      cardTypes: 'cardTypes', // 卡商列表
      authButtons: 'authButtons', // 按钮权限
      notifysFrom: 'notifysFrom',
      statusSelect: 'statusSelect',
      exceedSelect: 'exceedSelect', // 是否过期
      activeSelect: 'activeSelect',
      asideCollapse: 'asideCollapse', // 侧边栏是否隐藏
      dialogVisible: 'dialogVisible', // 公共dialog
      liveMonthSelect: 'liveMonthSelect', // 分配月数
      maxUnusedSelect: 'maxUnusedSelect', // 剩余流量
      payMethodSelect: 'payMethodSelect',
      unicomDiffSelect: 'unicomDiffSelect', // 日差异流量
    }),
    routeName() {
      return this.$route.name
    },
    pageAuthBtn() {
      let authArr = this.authButtons[`FCP_${this.routeName}`]
      if (!authArr) return {}
      return authArr
    }
  }
}
