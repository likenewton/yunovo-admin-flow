// 过滤器
Vue.filter('formatFlowUnit', function(count) {
	console.log(count)
  count -= 0
  let htmlStr = ''
  if (count < 0) {
    htmlStr = `<span style="color:#e92322;font-weight:bold">无限制</span>`
  } else if (count < 1024) {
    htmlStr = `<span>${count}</span><span style="color:#008000;font-weight:bold">&nbsp;M</span>`
  } else {
    htmlStr = `<span>${(count / 1024).toFixed(3)}</span><span style="color:#0000FF;font-weight:bold">&nbsp;G</span>`
  }
  return htmlStr
})
