export default {
  getCardTypes(context) {
    _axios.send({
      method: 'get',
      url: _axios.ajaxAd.getCardTypes,
      done: (res) => {
        context.commit('SET_CARDTYPES', { cardTypes: res.data.data })
      }
    })
  },
  getOrgs(context) {
    _axios.send({
      method: 'get',
      url: _axios.ajaxAd.getOrgs,
      done: (res) => {
        context.commit('SET_ORGS', { orgs: res.data.data })
      }
    })
  }
}
