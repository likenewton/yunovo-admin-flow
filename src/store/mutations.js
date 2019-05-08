const SET_ASIDECOLLAPSE = 'SET_ASIDECOLLAPSE'
const SET_ASIDEFLAG = 'SET_ASIDEFLAG'
const SET_DIALOGVISIBLE = 'SET_DIALOGVISIBLE'

export default {
  [SET_ASIDECOLLAPSE](state, paras) {
    state.asideCollapse = paras.asideCollapse
  },
  [SET_ASIDEFLAG](state, paras) {
    state.asideFlag = paras.asideFlag
  },  
  [SET_DIALOGVISIBLE](state, paras) {
    state.dialogVisible = paras.dialogVisible
  }
}
