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
        context.commit('SET_MONTHS', { months: res.data })
      }
    })
  },
  // 通知来源
  getNotifySelect(context) {
    _axios.send({
      method: 'get',
      url: _axios.ajaxAd.getNotifySelect,
      done: (res) => {
        context.commit('SET_NOTIFYSFROM', { notifysFrom: res.data })
      }
    })
  },
  // 支付方式
  getPayMethodSelect(context) {
    _axios.send({
      method: 'get',
      url: _axios.ajaxAd.getPayMethodSelect,
      done: (res) => {
        context.commit('SET_PAYMETHODSELECT', { payMethodSelect: res.data })
      }
    })
  },
  // 有效周期
  getLiveMonth(context) {
    _axios.send({
      method: 'get',
      url: _axios.ajaxAd.getLiveMonth,
      done: (res) => {
        context.commit('SET_LIVEMONTHSELECT', { liveMonthSelect: res.data })
      }
    })
  }
}
