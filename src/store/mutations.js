const SET_ASIDECOLLAPSE = 'SET_ASIDECOLLAPSE'
const SET_ASIDEFLAG = 'SET_ASIDEFLAG'
const SET_DIALOGVISIBLE = 'SET_DIALOGVISIBLE'
const SET_AUTHMENU = 'SET_AUTHMENU'
const SET_ISLOGIN = 'SET_ISLOGIN'

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
  [SET_ISLOGIN](state, paras) {
    state.authMenu = paras.isLogin
  }
}
