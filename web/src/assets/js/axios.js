class AXIOS {
  constructor(para) {
    this.data = {
      method: 'get',
      params: null,
      timeout: 60000,
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
      // 登录
      isLogin: '/fc/api/sso/isLogin',
      getLoginInfo: '/fc/api/sso/getLoginInfo',
      getAuthMenu: '/fc/api/sso/menus',
      getAuthButtons: '/fc/api/sso/buttons',
      // 下拉列表
      getCardTypes: '/fc/api/select/cardTypes',
      getOrgs: '/fc/api/select/orgs',
      getMonths: '/fc/api/select/stats/getMonths',
      getPayMonths: '/fc/api/select/payMonths',
      getNotifySelect: '/fc/api/select/notifyFrom/select',
      getPayMethodSelect: '/fc/api/select/payMethod/select',
      getComboFlow: '/fc/api/select/pack',
      getCurrencySelect: '/fc/api/select/currency',
      getLangSelect: '/fc/api/select/language',
      getNationSelect: '/fc/api/select/nation',
      getLiveMonth: '/fc/api/select/liveMonth',
      // table列表
      getStats: '/fc/api/gprs/stats/', // 月度用量
      getHalt: '/fc/api/gprs/halt/', // 已停卡况
      getOnOffLog: '/fc/api/gprs/onoffLog/', // 停卡日志
      getAbnormal: '/fc/api/gprs/abnormal/', // 用量异常
      getSell2pay: '/fc/api/gprs/sell2pay/', // 续费数据
      getCardUsed: '/fc/api/gprs/cardUsed/', // 累计用量
      getUnicomStat: '/fc/api/gprs/unicomStat/', // 联通流量卡
      getNotifysFrom: '/fc/api/gprs/notifyFrom/', // 通知来源
      getOrgPayReport: '/fc/api/gprs/report/orgPayReport', // 财务报表-机构充值
      getReport: '/fc/api/gprs/report/', // 财务表单-充值总额
      getPayListPage: '/fc/api/gprs/report/getPayListPage', // 财务报表-充值明细
      getpayPack: '/fc/api/gprs/report/payPack', // 财务报表-套餐充值
      getMonthReport: '/fc/api/gprs/report/monthReport', // 月度充值列表
      getOrgList: '/fc/api/user/org/', // 机构管理
      getNations: '/fc/api/system/nations/', // 国家区域
      getPays: '/fc/api/system/pays/', // 支付列表
      getCurrency: '/fc/api/system/currency/', // 货币列表
      getCards: '/fc/api/gprs/card/', // 流量卡
      getCardReset: '/fc/api/card/reset/', // 卡重置
      getBatchs: '/fc/api/gprs/batch/history', // 卡批次
      getPacks: '/fc/api/gprs/pack/', // 流量套餐
      getGift: '/fc/api/gprs/gift/history', // 流量赠送
      getMoves: '/fc/api/gprs/move/history', // 流量迁移
      getRealNames: '/fc/api/realname/', // 卡实名
      // 明细
      getReportPayDetail: '/fc/api/gprs/report/payDetail', // 充值列表详情
      getPayDetail: '/fc/api/system/pays/detail',
      getCardPayDetail: '/fc/api/gprs/card/payDetail',
      getOnOffLogDetail: '/fc/api/gprs/onoffLog/detail', // 停卡日志详情
      getNotifyFromOrg: '/fc/api/gprs/notifyFrom/org', // 通知来源详情
      getPayOnlineOrg: '/fc/api/gprs/payOnline/org',
      getNationDetail: '/fc/api/system/nations/detail',
      getCurrencyDetail: '/fc/api/system/currency/detail',
      getSystemParams: '/fc/api/system/params/',
      getGprsAllotList: '/fc/api/gprs/card/gprsAllotList',
      getLogList: '/fc/api/gprs/card/logList',
      getDayUse: '/fc/api/gprs/card/dayUse',
      getMonthUse: '/fc/api/gprs/card/monthUse',
      getCardDetail: '/fc/api/gprs/card/detail',
      getComboDetail: '/fc/api/gprs/pack/detail',
      getBatchDetail: '/fc/api/gprs/batch/detail', // 编辑批次
      // 导出
      statsExport: '/fc/api/gprs/stats/export', // 
      cardExport: '/fc/api/gprs/card/export', // 流量卡
      cardAuthExport: '/fc/api/realname/export', // 卡实名
      deadstatusExport: '/fc/api/gprs/halt/export', // 已停卡况
      useanomalyExport: '/fc/api/gprs/abnormal/export', // 用量异常
      usedataExport: '/fc/api/gprs/cardUsed/export', // 累计用量
      unicomdataExport: '/fc/api/gprs/unicomStat/export', // 联通流量卡
      unicomdataExport_2: '/fc/api/gprs/unicomStat/cardExport', // 联通流量卡-激活/未激活
      operatedataExport: '/fc/api/gprs/payOnline/export', // 运营统计
      orgListExport: '/fc/api/gprs/payOnline/org/export', // 运营统计-机构统计导出
      rechargeParticularsExport: '/fc/api/gprs/report/getPayListPageExport', // 充值明细-导出明细
      rechargeParticularsExport_2: '/fc/api/gprs/report/getPaySnapExport', // 充值明细-导出快照
      // 实名审核
      checkAudit: '/fc/api/realname/audit',
      unbindRealname: '/fc/api/realname/unbind', // 解除实名
      // 停用/启用
      checkComboStop: '/fc/api/gprs/pack/stop', // 套餐
      checkCardStop: '/fc/api/gprs/card/onoff', // 卡
      getPayOnline: '/fc/api/gprs/payOnline/', // 运营统计折线图
      // 数据同步
      checkSyncUnicomData: '/fc/api/gprs/card/syncUnicomData',
      // 图表
      getPayPie: '/fc/api/gprs/report/paylog/chart', // 财务报表-充值明细扇形图
      getCardBar: '/fc/api/gprs/card/cardTotalByOrgidGroup',
      // 表单提交
      addOrg: '/fc/api/user/org/insert', // 机构管理-新增机构
      updateOrg: '/fc/api/user/org/update', // 机构管理-编辑机构
      deleteOrg: '/fc/api/user/org/delete', // 机构管理-删除机构
      addNation: '/fc/api/system/nations/insert',
      upDateNation: '/fc/api/system/nations/update',
      deleteNation: '/fc/api/system/nations/delete',
      installPay: '/fc/api/system/pays/install',
      deletePay: '/fc/api/system/pays/uninstall',
      updatePay: '/fc/api/system/pays/update',
      addCurrency: '/fc/api/system/currency/insert',
      updateCurrency: '/fc/api/system/currency/update',
      updateSystemParams: '/fc/api/system/params/update',
      addGprsCombo: '/fc/api/gprs/pack/insert', // 流量套餐-新增
      updateGprsCombo: '/fc/api/gprs/pack/update',
      addResetGprs: '/fc/api/card/reset/gprs', // 卡重置
      addCardMove: '/fc/api/gprs/move/', // 卡迁移
      addGprsGift: '/fc/api/gprs/gift/', // 流量赠送
      addBatch: '/fc/api/gprs/batch/insert', // 新建批次
      updateBatch: '/fc/api/gprs/batch/update', // 编辑批次
      // 首页
      getPayCase: '/fc/api/home/payCase', // 数据看板
      getSiminfo: '/fc/api/home/siminfo', // 获取pie-card数据
      getEl2pack: '/fc/api/home/rl2pack', // 数据总览
      getTopupTrend: '/fc/api/home/topupTrend', // 续费趋势
      getOrderTrend: '/fc/api/home/orderTrend', // 订单趋势
      getGprsTrend: '/fc/api/home/gprsTrend', // 流量消耗趋势
      getPriceTrend: '/fc/api/home/priceTrend', // 客单价趋势
      getSimTrend: '/fc/api/home/simTrend', // sim卡使用趋势
    }
  }

  send(para) {
    let data = Object.assign({}, this.constData, para)
    data.headers = Object.assign(this.constData.headers, para.headers || {
      'Content-Type': 'application/json',
      'iov-token': localStorage.getItem('iov-token') || '' // 每次的请求都要在headers中携带token过去
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
      if (res.data.status === 401) {
        // 如果没有登录要记录跳转次数，大于5次就不跳转了
        let loginCount = localStorage.getItem('loginCount') || '0'
        if (loginCount - 0 <= 5) {
          localStorage.setItem('loginCount', ++loginCount + '')
          // 未登录状态跳转登录页
          location.replace(res.data.redirectUrl += '&target=' + encodeURIComponent(location.href))
        } else {
          Vue.prototype.$notify.error({
            title: '错误',
            message: 'token校验异常'
          })
          localStorage.setItem('loginCount', '0')
        }
      } else {
        localStorage.setItem('loginCount', '0')
        // status === 0 为正常返回, 表示已经登录了
        if (res.data.status === 0) {
          return data.done && data.done(res.data)
        } else {
          Vue.prototype.$notify.error({
            title: '错误',
            message: res.data.msg
          })
        }
        if (res.data.status === 400) {
          // 400 表单提交业务验证异常
          return data.done && data.done(res.data)
        }
      }
    }).catch(error => {
      console.log(error)
      Vue.prototype.$notify.error({
        title: '错误',
        message: '未连接到服务器'
      })
    })
  }

  static init(para) {
    return new AXIOS(para)
  }
}

export default AXIOS
