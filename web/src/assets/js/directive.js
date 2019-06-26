// 指令

// 让被绑定的元素无法被选择
Vue.directive('unselect', {
  // 当被绑定的元素插入到 DOM 中时……
  inserted: function(el) {
    $(el).find('input').attr('unselectable', 'on')
  }
})
