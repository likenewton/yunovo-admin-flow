module.exports = {
  // 获取权限菜单
  getAuthMenu(asideData) {
    return asideData
  },
  // 获取当前页面某个字段求和
  pageSums(data, key) {
    let sum = 0
    data.forEach((v) => {
      sum += Number(v[key]) || 0
    })
    return sum
  },
  // 设置升降序字段
  setSortSearch(val, _this, sort = 'sort') {
    if (!val.prop) return _this[sort] = {}
    if (val.order === 'descending') {
      _this[sort] = {
        descs: val.prop
      }
    } else if (val.order === 'ascending') {
      _this[sort] = {
        ascs: val.prop,
      }
    }
  },
  // 获取列表数据（通用） this, url, list, formInline, sort
  getListData(paras) {
    let para = paras
    let list = para.vue[para.list || 'list']
    let sort = para.vue[para.sort || 'sort']
    let formInline = para.vue[para.formInline || 'formInline'] || []

    para.vue[para.loadData || 'loadData'] = true
    _axios.send({
      method: para.method || 'get',
      url: para.url,
      params: $.extend({}, formInline, {
        ascs: sort.ascs,
        descs: sort.descs,
        size: list.pagesize,
        current: list.currentPage
      }),
      done: (res) => {
        para.vue[para.loadData || 'loadData'] = false
        list.data = res.data ? res.data.page.records : []
        list.total = res.data ? res.data.page.total : 0
        para.cb && para.cb(res)
      }
    })
  },

  // 限制小数位数和整数位数 num1(整数)， num2(小数)
  limitNumber(val, num1 = 8, num2 = 3) {
    let expStr = ''
    if (num2 === 0) {
      // 如果是整数
      expStr = `^\\d{1,${num1}}`
    } else {
      // 如果是小数
      expStr = `^\\d{1,${num1}}(\\.\\d{0,${num2}})?`
    }
    return val.match(new RegExp(expStr)) && val.match(new RegExp(expStr))[0]
  },

  // 加载动画
  loading(vue, paras = {
    lock: true,
    text: 'Loading',
    spinner: 'el-icon-loading',
    background: 'rgba(0, 0, 0, 0.7)'
  }) {
    return vue.$loading(paras)
  },

  // 获取页面的尺寸
  getSize() {
    let size = 'medium'
    let width = $(window).width()
    if (width > 1600) size = 'medium'
    else if (width <= 1600 && width > 1300) size = 'small'
    else size = 'mini'
    return size
  },

  // 获取页面面包屑数组
  getBreadArr(name, authMenu) {
    let breadArr = []
    if (name === 'home') return [, , '首页']
    authMenu.forEach((v1) => {
      if (v1.name === name) {
        breadArr.push('首页', v1.title)
        return false
      }
      v1.children.forEach((v2) => {
        if (v2.name === name) {
          breadArr.push('首页', v1.title, v2.title)
          return false
        }
      })
    })
    if (breadArr.length > 0) {
      // 点击的是侧边栏页面
      sessionStorage.setItem('authMenu', breadArr)
    } else {
      // 打开的不是侧边栏页面，将目录拉取下来
      breadArr = sessionStorage.getItem('authMenu')
      if (breadArr) {
        // 如果有数据就代表是通过其他路由进入的
        breadArr = breadArr.split(',')
      } else {
        // 如果没有数据就代表是直接通过url进入的默认进入 第一个菜单第一项
        breadArr = ['首页', authMenu[0].title, authMenu[0].children[0].title]
      }
    }
    return breadArr
  },
  // 格式化 流量M / G / T, 默认保留三位小数
  formatFlowUnit(count, fix = 3, isHtmlStr = true) {
    count -= 0
    let htmlStr = ''
    if (isHtmlStr) {
      if (isNaN(count) || count == '99999999') {
        htmlStr = `<span style="font-weight:bold" class="text_danger">无限制</span>`
      } else if (Math.abs(count / 1024) < 1) {
        htmlStr = `<span>${count.toFixed(fix)}</span><span style="font-weight:bold" class="text_success">&nbsp;M</span>`
      } else if (Math.abs(count / 1024 / 1024) < 1) {
        htmlStr = `<span>${(count / 1024).toFixed(fix)}</span><span style="font-weight:bold" class="text_primary">&nbsp;G</span>`
      } else {
        htmlStr = `<span>${(count / 1024 / 1024).toFixed(fix)}</span><span style="font-weight:bold" class="text_danger">&nbsp;T</span>`
      }
    } else {
      if (Math.abs(count / 1024) < 1) {
        htmlStr = `${count} M`
      } else if (Math.abs(count / 1024 / 1024) < 1) {
        htmlStr = `${(count / 1024).toFixed(fix)} G`
      } else {
        htmlStr = `${(count / 1024 / 1024).toFixed(fix)} T`
      }
    }
    return htmlStr
  },
  formatMoney(value, type = 2) {
    if (!value) {
      if (type === 0) return '0'
      else return '0.' + '0'.repeat(type)
    }
    value = value.toString().replace(/^(\d*)$/, "$1.")
    value = (value + "00").replace(/(\d*\.\d\d)\d*/, "$1")
    value = value.replace(".", ",")
    var re = /(\d)(\d{3},)/
    while (re.test(value))
      value = value.replace(re, "$1,$2")
    value = value.replace(/,(\d\d)$/, ".$1")
    if (type === 0) {
      value = value.split(".")[0]
    }
    return value
  },
  // 格式化流量套餐
  formatComboFlow(val, isHtml = true) {
    let htmlStr = ''
    if (isHtml) {
      if (val == '0.01') {
        htmlStr = '<span style="font-weight:bold" class="text_purple">畅想无限</span>'
      } else if (val == '0.02') {
        htmlStr = '<span style="font-weight:bold" class="text_purple">定向无限</span>'
      } else if (val == '99999999') {
        htmlStr = '<span style="font-weight:bold" class="text_danger">无限制</span>'
      } else {
        htmlStr = this.formatFlowUnit(val, 0)
      }
    } else {
      if (val == '0.01') {
        htmlStr = '畅想无限'
      } else if (val == '0.02') {
        htmlStr = '定向无限'
      } else if (val == '99999999') {
        htmlStr = '无限制'
      } else {
        htmlStr = this.formatFlowUnit(val, 0, false)
      }
    }
    return htmlStr
  },
  // 计算到期时间
  calcLeftTime(time) {
    let htmlStr = ''
    let now = new Date().getTime()
    // ie 下兼容性问题
    if (time) {
      var a = new Date(time).getTime() || new Date(time.replace(/-/g, "/")).getTime()
    } else {
      return htmlStr
    }
    if (a - now > 0) {
      htmlStr = `<span style="display:inline-block">${time}</span> <span style="display:inline-block;font-weight:bold" class="text_success">(${Math.floor((a - now) / 24 / 3600000)}天)</span>`
    } else {
      htmlStr = `<span style="display:inline-block">${time}</span> <span style="font-weight:bold" class="text_danger">(${Math.floor((a - now) / 24 / 3600000)}天)</span>`
    }
    return htmlStr
  },
  getColorList(para, count) {
    let colorList = ['#3cb1ff', '#ffc367', '#ff7477', '#27da99', '#3ecec9', '#9a83da']
    let colorData = {
      primary: '#3cb1ff',
      warning: '#ffc367',
      danger: '#ff7477',
      success: '#27da99',
      editor: '#3ecec9',
      purple: '#9a83da'
    }
    if (!count) {
      if (!para) return colorList
      else if (typeof para === 'string') {
        return colorData[para]
      } else if (Array.isArray(para)) {
        let arr = []
        para.forEach((v) => {
          arr.push(colorData[v])
        })
        return arr
      }
    } else {
      if (typeof para === 'string') {
        let arr = []
        for (let i = 0; i < count; i++) {
          arr.push(colorData[para])
        }
        return arr
      }
      if (Array.isArray(para)) {
        if (para.length === 0) {
          let arr = []
          for (let i = 0; i < Math.ceil(count / 6); i++) {
            arr = arr.concat(colorList)
          }
          return arr.slice(0, count)
        } else {
          let arr = []
          for (let i = 0; i < Math.ceil(count / para.length); i++) {
            arr = arr.concat(para)
          }
          return arr.slice(0, count)
        }
      }
    }
  },
  // 验证邮箱地址
  validatorEmall(value) {
    return /^([0-9A-Za-z\-_\.]+)@([0-9a-z]+\.[a-z]{2,3}(\.[a-z]{2})?)$/.test(value)
  },
  // 验证电话号码
  validatorPhoneNumber(value) {
    return /^1[3|4|5|6|8|9][0-9]\d{8}$/.test(value)
  },
  // 验证密码格式
  validatorPassword(value) {
    return /^(?![a-zA-z]+$)(?!\d+$)(?![!@#$%^&*]+$)(?![a-zA-z\d]+$)(?![a-zA-z!@#$%^&*]+$)(?![\d!@#$%^&*]+$)[a-zA-Z\d!@#$%^&*]+$/.test(value) && value.length >= 6
  },
  validatorNumMaxandMin(value, max = 1, min = 0, containMax = false, containMin = true) {
    if (containMax && containMin) {
      return value <= max && value >= 0
    } else if (!containMax && !containMin) {
      return value < max && value > 0
    } else if (containMax && !containMin) {
      return value <= max && value > 0
    } else if (!containMax && containMin) {
      return value < max && value >= 0
    }
  },
  getQuery(attr) {
    let href = location.href
    let queryStr = href.substr(href.indexOf('?') + 1)
    let queryArr = queryStr.split('&')
    let queryObj = {}
    queryArr.forEach(v => {
      let tplArr = v.split('=')
      queryObj[tplArr[0]] = tplArr[1]
    })
    if (attr) return queryObj[attr]
    else return queryObj
  },
  maxTableHeight() {
    let height = $(window).height() - (220 + 300000 / ($(window).height() + 1000))
    if (height < 400) height = 400
    return height
  },
  // 时间格式化
  formatdate(date, fmt) {
    date = new Date(date);
    let timeString = fmt || 'yyyy-mm-dd hh:ff:ss';
    let getFullYear = String(date.getFullYear());

    function padLeftZero(str) {
      var padLeft = '00';
      return (padLeft + str).substr(str.length);
    }

    // 如果存在至少一个y
    if (/(y+)/.test(timeString)) {
      // RegExp.$1 为匹配第一个大括号的内容
      timeString = timeString.replace(RegExp.$1, getFullYear.substr(4 - RegExp.$1.length));
    }
    let o = {
      'm+': date.getMonth() + 1,
      'd+': date.getDate(),
      'h+': date.getHours(),
      'f+': date.getMinutes(),
      's+': date.getSeconds(),
    };
    for (var k in o) {
      if (new RegExp(`(${k})`).test(timeString)) {
        let str = String(o[k]);
        timeString = timeString.replace(RegExp.$1, str.length == 1 ? padLeftZero(str) : str);
      }
    }
    return timeString;
  },
  showMsgBox(para = {}) {
    Vue.prototype.$notify({
      type: para.type || 'error',
      title: para.title || '错误',
      message: para.message || '提交的表单数据不符合规范！'
    })
  },
  toUnicomLink(iccid) {
    window.open(`http://t.gprs.yunovo.cn/app/main/info?iccid=${iccid}`)
  },
  toFixed(value, total, count = 2) {
    return (value / total * 100).toFixed(count) + '%'
  }
}
