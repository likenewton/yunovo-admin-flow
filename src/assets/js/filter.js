// 过滤器
Vue.filter('formatMoney', function(value, type) {
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
})
