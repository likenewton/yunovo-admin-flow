module.exports = {
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

  // 限制小数位数和整数位数
  limitNumber(val, num1 = 8, num2 = 3) {
    let expStr = `^\\d{1,${num1}}(\\.\\d{0,${num2}})?`
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
  getBreadArr(name, asideData) {
    let breadArr = []
    if(name === 'home') return breadArr
    asideData.forEach((v1) => {
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
      sessionStorage.setItem('asideData', breadArr)
    } else {
      // 打开的不是侧边栏页面，将目录拉取下来
      breadArr = sessionStorage.getItem('asideData').split(',')
    }
    return breadArr
  },
  // 格式化 流量M / G
  formatFlowUnit(count) {
    count -= 0
    let htmlStr = ''
    if (count < 0) {
      htmlStr = `<span style="color:#e92322;font-weight:bold">无限制</span>`
    } else if (count < 1024) {
      htmlStr = `<span>${count}</span><span style="color:#008000;font-weight:bold">&nbsp;M</span>`
    } else if (count < 1024 * 1024) {
      htmlStr = `<span>${(count / 1024).toFixed(3)}</span><span style="color:#0000FF;font-weight:bold">&nbsp;G</span>`
    } else {
      htmlStr = `<span>${(count / 1024 / 1024).toFixed(3)}</span><span style="color:#e92322;font-weight:bold">&nbsp;T</span>`
    }
    return htmlStr
  },
  // 计算到期时间
  calcLeftTime(time) {
    let htmlStr = '<span style="color:#e92322;font-weight:bold">已过期</span>'
    let now = new Date()
    let a = new Date(time)
    if (a - now > 0) {
      htmlStr = `${time} <span style="display:inline-block;color:#008000;font-weight:bold"">(${Math.ceil((a - now) / 24 / 3600000)}天)</span>`
    }
    return htmlStr
  }
}
