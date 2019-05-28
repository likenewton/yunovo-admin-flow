class AXIOS {
  constructor(para) {
    this.data = {
      method: 'get',
      params: null,
      timeout: 5000,
      data: null, // 请求体所带的参数
      done: null, // 接口请求成功回调函数
      headers: {
        '_clw_token_': localStorage.getItem('_clw_token_') || '', // 每次的请求都要在headers中携带token过去
        'Content-Type': 'application/json'
      }
    }
    // 实例化时初始的参数
    this.constData = Object.assign(this.data, para)

    // ajax请求地址
    this.ajaxAd = {
      isLogin: '/uc/api/isLogin'
    }
  }

  send(para) {
    let data = Object.assign({}, this.constData, para)
    data.headers = Object.assign(this.constData.headers, para.headers || {})

    axios({
      method: data.method,
      url: data.url,
      data: data.data,
      params: data.params,
      timeout: data.timeout,
      headers: data.headers
    }).then(res => {
      // 这里要根据状态码来对不同的响应状态做处理
      console.log(res)
      if (res.data.status === 401) {
        // 未登录状态跳转登录页
        location.replace(res.data.redirectUrl)
      } else {
        data.done && data.done(res)
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
