import getters from './getter'
import mutations from './mutations'

Vue.use(Vuex)

const state = {
  asideCollapse: false, // 侧边栏折叠状态
  asideFlag: [], // 面包屑地址导航数据，配合static.js中的数据使用，用于同步面包屑与侧边栏在选中菜单层级的一致性
  dialogVisible: false // 公共dialog是否显示
}

export default new Vuex.Store({
  state,
  getters,
  mutations
})
