class AXIOS {
  constructor(para) {
    this.data = {
      method: 'get',
      params: null,
      timeout: 30000,
      data: null, // 请求体所带的参数
      done: null, // 接口请求成功回调函数
      headers: {
        'Content-Type': 'application/json'
      }
    }
    // 实例化时初始的参数
    this.constData = Object.assign(this.data, para)

    // ajax请求地址
    this.ajaxAd = {
      // 公共
      isLogin: '/fc/api/sso/isLogin',
      getLoginInfo: '/fc/api/sso/getLoginInfo',
      // 下拉列表
      getCardTypes: '/fc/api/select/api/select/cardTypes',
      getOrgs: '/fc/api/select/api/select/orgs',
      getMonths: '/fc/api/select/api/select/stats/getMonths',
      getNotifySelect: '/fc/api/select/api/select/notifyFrom/select',
      getPayMethodSelect: '/fc/api/select/api/select/payMethod/select',
      // table列表
      getStats: '/fc/api/gprs/stats/',
      getHalt: '/fc/api/gprs/halt/',
      getOnOffLog: '/fc/api/gprs/onoffLog/',
      getAbnormal: '/fc/api/gprs/abnormal/',
      getSell2pay: '/fc/api/gprs/sell2pay/',
      getCardUsed: '/fc/api/gprs/cardUsed/',
      getUnicomStat: '/fc/api/gprs/unicomStat/',
      getPayOnline: '/fc/api/gprs/payOnline/',
      getNotifysFrom: '/fc/api/gprs/notifyFrom/',
      getOrgPayReport: '/fc/api/gprs/report/orgPayReport',
      getReport: '/fc/api/gprs/report/',
      // 明细
      getOnOffLogDetail: '/fc/api/gprs/onoffLog/detail',
      getNotifyFromOrg: '/fc/api/gprs/notifyFrom/org',
      getPayOnlineOrg: '/fc/api/gprs/payOnline/org',
      // 导出
      statsExport: '/fc/api/gprs/stats/export',
    }
  }

  send(para) {
    let data = Object.assign({}, this.constData, para)
    data.headers = Object.assign(this.constData.headers, para.headers || {
      '_iov_token_': localStorage.getItem('_iov_token_') || '' // 每次的请求都要在headers中携带token过去
    })

    axios({
      method: data.method,
      url: data.url,
      data: data.data,
      params: data.params,
      timeout: data.timeout,
      headers: data.headers
    }).then(res => {
      // 这里要根据状态码来对不同的响应状态做处理
      // console.log(res)
      if (res.data.status === 401) {
        // 未登录状态跳转登录页
        location.replace(res.data.redirectUrl)
      } else {
        // status === 0 为正常返回
        if (res.data.status === 0) {
          data.done && data.done(res.data)
        } else if (res.data.status === 500) {
          Vue.prototype.$notify.error({
            title: '错误',
            message: 'status: 500,' + res.data.msg
          })
        }
      }
    }).catch(error => {
      console.log(error)
      Vue.prototype.$notify.error({
        title: '错误',
        message: '服务器异常'
      })
    })
  }

  static init(para) {
    return new AXIOS(para)
  }
}

export default AXIOS
