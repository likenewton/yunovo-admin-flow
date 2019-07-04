const SET_ASIDECOLLAPSE = 'SET_ASIDECOLLAPSE' // 侧边栏是否处于折叠状态
const SET_ASIDEFLAG = 'SET_ASIDEFLAG' // 设置面包屑
const SET_DIALOGVISIBLE = 'SET_DIALOGVISIBLE' // 公共dialog对话框是否显示
const SET_AUTHMENU = 'SET_AUTHMENU' // 设置菜单权限列表
const SET_AUTHBUTTONS = 'SET_AUTHBUTTONS' // 设置授权页面中的功能按钮权限
const SET_ISLOGIN = 'SET_ISLOGIN' // 是否登录
const SET_CARDTYPES = 'SET_CARDTYPES' // 设置卡商列表
const SET_ORGS = 'SET_ORGS' // 设置机构列表
const SET_MONTHS = 'SET_MONTHS' // 设置月份列表
const SET_NOTIFYSFROM = 'SET_NOTIFYSFROM' // 设置通知来源
const SET_PAYMETHODSELECT = 'SET_PAYMETHODSELECT' // 设置付款方式
const SET_LIVEMONTHSELECT = 'SET_LIVEMONTHSELECT' // 有效周期下拉

export default {
  [SET_ASIDECOLLAPSE](state, paras) {
    state.asideCollapse = paras.asideCollapse
  },
  [SET_ASIDEFLAG](state, paras) {
    state.asideFlag = paras.asideFlag
  },  
  [SET_DIALOGVISIBLE](state, paras) {
    state.dialogVisible = paras.dialogVisible
  },
  [SET_AUTHMENU](state, paras) {
    state.authMenu = paras.authMenu
  },
  [SET_AUTHBUTTONS](state, paras) {
    state.authButtons = paras.authButtons
  },
  [SET_ISLOGIN](state, paras) {
    state.isLogin = paras.isLogin
  },
  [SET_CARDTYPES](state, paras) {
    state.cardTypes = paras.cardTypes
  },
  [SET_ORGS](state, paras) {
    state.orgs = paras.orgs
  },
  [SET_MONTHS](state, paras) {
    state.months = paras.months
  },
  [SET_NOTIFYSFROM](state, paras) {
    state.notifysFrom = paras.notifysFrom
  },
  [SET_PAYMETHODSELECT](state, paras) {
    state.payMethodSelect = paras.payMethodSelect
  },
  [SET_LIVEMONTHSELECT](state, paras) {
    state.liveMonthSelect = paras.liveMonthSelect
  }
}
