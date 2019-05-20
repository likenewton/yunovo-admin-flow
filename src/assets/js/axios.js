class AXIOS {
  constructor(para) {
    this.data = {
      method: 'get',
      params: null,
      timeout: 5000,
      data: null, // 请求体所带的参数
      done: null, // 接口请求成功回调函数
      headers: { 'Content-Type': 'application/json' }
    }
    // 实例化时初始的参数
    this.constData = Object.assign(this.data, para)
  }

  send(para) {
    const data = Object.assign(this.constData, para)

    axios({
      method: data.method,
      url: data.url,
      data: data.data,
      params: data.params,
      timeout: data.timeout,
      headers: data.headers
    }).then(res => {
      // 这里要根据状态码来对不同的相应状态做处理
      data.done && data.done(res)
    }).catch(error => {
      console.log(error)
      Vue.prototype.$notify.error({
        title: '错误',
        message: error
      });
    })
  }

  static init(para) {
    return new AXIOS(para)
  }
}

export default AXIOS
