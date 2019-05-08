import getters from './getter'
import mutations from './mutations'

Vue.use(Vuex)

const state = {
  asideCollapse: false,
  asideFlag: {},
  dialogVisible: false
}

export default new Vuex.Store({
  state,
  getters,
  mutations
})
