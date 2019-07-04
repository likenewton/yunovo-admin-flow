import Api from 'assets/js/api.js'
import router from '../router'
import menuRoute from '../router/menuRoute.js'

export default {
  // 菜单权限(同时生成动态路由)
  getAuthMenu(context) {
    _axios.send({
      method: 'get',
      url: _axios.ajaxAd.getAuthMenu,
      done: ((res) => {
        context.commit('SET_AUTHMENU', { authMenu: Api.UNITS.getAuthMenu(Api.STATIC.asideData, res.data) }) // 保存菜单数据
        router.addRoutes([Api.UNITS.getMenuRoute(menuRoute, res.data)]) // 生成动态路由
      })
    })
  },
  // 功能按钮权限列表（所有页面）
  getAuthButtons(context) {
    _axios.send({
      method: 'get',
      url: _axios.ajaxAd.getAuthButtons,
      done: ((res) => {
        context.commit('SET_AUTHBUTTONS', { authButtons: res.data }) // 保存功能按钮权限
      })
    })
  },
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
