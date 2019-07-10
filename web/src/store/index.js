import getters from './getter'
import mutations from './mutations'
import actions from './actions'

Vue.use(Vuex)

const state = {
  asideCollapse: false, // 侧边栏折叠状态
  asideFlag: [], // 面包屑地址导航数据，配合static.js中的数据使用，用于同步面包屑与侧边栏在选中菜单层级的一致性
  dialogVisible: false, // 公共dialog是否显示
  authMenu: [], // 授权可以展示的菜单
  authButtons: {}, // 授权页面中的功能按钮权限
  cardTypes: [], // 卡商列表
  orgs: [], // 机构列表
  months: [], // 月份列表
  notifysFrom: [], // 通知来源
  payMethodSelect: [], // 付款方式
  liveMonthSelect: [], // 有效周期
  exceedSelect: [{ value: '1', label: '未过期' }, { value: '0', label: '已过期' }], // 下拉选择是否过期
  activeSelect: [{ value: '0', label: '未激活' }, { value: '1', label: '已激活' }], // 下拉选择激活状态
  statusSelect: [{ value: 1, label: '启用' }, { value: 0, label: '停用' }], // 下拉选择启用状态
  paySelect: [{ value: '0', label: '未付款' }, { value: '1', label: '已付款' }], // 下拉选择是否支付
  langSelect: [{ value: 'cn', label: '简体中文' }, { value: 'en', label: 'English' }], // 下拉选语言
  unicomDiffSelect: [{ value: '150', label: '>=150M' }, { value: '200', label: '>=200M' }, { value: '500', label: '>=500M' }, { value: '750', label: '>=750M' }, { value: '1024', label: '>=1G' }, { value: '2048', label: '>=2G' }, { value: '3072', label: '>=3G' }, { value: '4096', label: '>=4G' }], // 下拉选择日差异流量
  maxUnusedSelect: [{ value: '10', label: '超出 10M' }, { value: '25', label: '超出 25M' }, { value: '50', label: '超出 50M' }, { value: '100', label: '超出 100M' }, { value: '150', label: '超出 150M' }, { value: '200', label: '超出 200M' }, { value: '500', label: '超出 500M' }, { value: '750', label: '超出 750M' }, { value: '1024', label: '超出 1G' }, { value: '2048', label: '超出 2G' }], // 剩余流量下拉列表
}

export default new Vuex.Store({
  state,
  getters,
  mutations,
  actions
})
