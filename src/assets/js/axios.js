
class AXIOS {
  constructor(para) {
    this.data = {
      method: 'get',
      params: null,
      timeout: 10000,
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
      // 统计分析
      getStats: '/fc/api/gprs/stats/'
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
          data.done && data.done(res)
        }
      }
    }).catch(error => {
      console.log(error)
      Vue.prototype.$notify.error({
        title: '错误',
        message: error
      })
    })
  }

  static init(para) {
    return new AXIOS(para)
  }
}

export default AXIOS
