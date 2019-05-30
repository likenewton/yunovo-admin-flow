module.exports = {
  // 获取权限菜单
  getAuthMenu(asideData) {
    return asideData
  },
  // 设置升降序字段
  setSortSearch(val, _this, sort = 'sort') {
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
  getListData(para) {
    let list = para.list || 'list'
    let sort = para.sort || 'sort'
    let loadData = para.loadData || 'loadData'
    para.vue[loadData] = true
    _axios.send({
      method: para.method || 'get',
      url: para.url,
      params: $.extend({}, para.vue[para.formInline || 'formInline'], {
        ascs: para.vue[sort].ascs,
        descs:para.vue[sort].descs,
        size: para.vue[list].pagesize,
        current: para.vue[list].currentPage
      }),
      done: (res) => {
        para.vue[loadData] = false
        para.vue[list].data = res.data.data || []
        para.vue[list].total = para.vue[list].data.length
      }
    })
  },
  // 验证xml文件格式
  validateXML(xmlContent) {
    //errorCode 0是xml正确，1是xml错误，2是无法验证
    var xmlDoc, errorMessage, errorCode = 0;
    // code for IE
    if (window.ActiveXObject) {
      xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
      xmlDoc.async = "false";
      xmlDoc.loadXML(xmlContent);

      if (xmlDoc.parseError.errorCode != 0) {
        errorMessage = "错误code: " + xmlDoc.parseError.errorCode + "\n";
        errorMessage = errorMessage + "错误原因: " + xmlDoc.parseError.reason;
        errorMessage = errorMessage + "错误位置: " + xmlDoc.parseError.line;
        errorCode = 1;
      } else {
        errorMessage = "格式正确";
      }
    }
    // code for Mozilla, Firefox, Opera, chrome, safari,etc.
    else if (document.implementation.createDocument) {
      var parser = new DOMParser();
      xmlDoc = parser.parseFromString(xmlContent, "text/xml");
      var error = xmlDoc.getElementsByTagName("parsererror");
      if (error.length > 0) {
        if (xmlDoc.documentElement.nodeName == "parsererror") {
          errorCode = 1;
          errorMessage = xmlDoc.documentElement.childNodes[0].nodeValue;
        } else {
          errorCode = 1;
          errorMessage = xmlDoc.getElementsByTagName("parsererror")[0].innerHTML;
        }
      } else {
        errorMessage = "格式正确";
      }
    } else {
      errorCode = 2;
      errorMessage = "浏览器不支持验证，无法验证xml正确性";
    }
    return {
      "msg": errorMessage,
      "error_code": errorCode
    }
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

  // 限制数值得最小值与最大值
  limitNumberSize(val, max, min) {
    if (typeof val === 'number') {
      console.log(1)
    }
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
      if (isNaN(count)) {
        htmlStr = `<span style="color:#e92322;font-weight:bold">无限制</span>`
      } else if (Math.abs(count / 1024) < 1) {
        htmlStr = `<span>${count}</span><span style="color:#008000;font-weight:bold">&nbsp;M</span>`
      } else if (Math.abs(count / 1024 / 1024) < 1) {
        htmlStr = `<span>${(count / 1024).toFixed(fix)}</span><span style="color:#0000FF;font-weight:bold">&nbsp;G</span>`
      } else {
        htmlStr = `<span>${(count / 1024 / 1024).toFixed(fix)}</span><span style="color:#e92322;font-weight:bold">&nbsp;T</span>`
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
  formatMoney(value, type) {
    if (!value) return '0.00'
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
  // 计算到期时间
  calcLeftTime(time) {
    let htmlStr = '<span style="color:#e92322;font-weight:bold">已过期</span>'
    let now = new Date().getTime()
    // ie 下兼容性问题
    if (time) {
      var a = new Date(time).getTime() || new Date(time.replace(/-/g, "/")).getTime()
    }
    if (a - now > 0) {
      htmlStr = `<span style="display:inline-block">${time}</span> <span style="display:inline-block;color:#008000;font-weight:bold"">(${Math.ceil((a - now) / 24 / 3600000)}天)</span>`
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
}
