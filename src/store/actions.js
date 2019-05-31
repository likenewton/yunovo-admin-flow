export default {
  // 卡商列表
  getCardTypes(context) {
    _axios.send({
      method: 'get',
      url: _axios.ajaxAd.getCardTypes,
      done: (res) => {
        context.commit('SET_CARDTYPES', { cardTypes: res.data })
      }
    })
  },
  // 机构列表
  getOrgs(context) {
    _axios.send({
      method: 'get',
      url: _axios.ajaxAd.getOrgs,
      done: (res) => {
        context.commit('SET_ORGS', { orgs: res.data })
      }
    })
  },
  // 月份
  getMonths(context) {
    _axios.send({
      method: 'get',
      url: _axios.ajaxAd.getMonths,
      done: (res) => {
        context.commit('SET_MONTHS', { orgs: res.data })
      }
    })
  }
}
