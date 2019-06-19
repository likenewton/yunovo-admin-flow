// [chunk2](侧边栏菜单动态路由)
const Asidemenu = r => require.ensure([], () => r(require('@/components/aside/index.vue')), 'chunk2')
const Blank = r => require.ensure([], () => r(require('@/components/aside/blank.vue')), 'chunk2')

// [chunk3](一般是表单增删改查页面)
const Batchcreate = r => require.ensure([], () => r(require('@/components/forms/batchcreate.vue')), 'chunk3')
const Rechargecomboset = r => require.ensure([], () => r(require('@/components/forms/rechargecomboset.vue')), 'chunk3')
const Flowwarningset = r => require.ensure([], () => r(require('@/components/forms/flowwarningset.vue')), 'chunk3')
const Createuser = r => require.ensure([], () => r(require('@/components/forms/createuser.vue')), 'chunk3')
const Createjg = r => require.ensure([], () => r(require('@/components/forms/createjg.vue')), 'chunk3')
const Createauth = r => require.ensure([], () => r(require('@/components/forms/createauth.vue')), 'chunk3')
const Alipay = r => require.ensure([], () => r(require('@/components/forms/alipay.vue')), 'chunk3')
const Wechart = r => require.ensure([], () => r(require('@/components/forms/wechart.vue')), 'chunk3')
const Createlang = r => require.ensure([], () => r(require('@/components/forms/createlang.vue')), 'chunk3') // 语言设置(添加语言)
const Createcurrency = r => require.ensure([], () => r(require('@/components/forms/createcurrency.vue')), 'chunk3') // 货币设置(添加货币)
const Createnation = r => require.ensure([], () => r(require('@/components/forms/createnation.vue')), 'chunk3') // 添加地区(邮编)

let formRoute = {
    // 表单文件
    path: '/forms',
    name: 'forms',
    component: Asidemenu,
    children: [{
      path: 'batchcreate',
      name: 'batchcreate',
      component: Batchcreate
    }, {
      path: 'rechargecomboset',
      name: 'rechargecomboset',
      component: Rechargecomboset
    }, {
      path: 'flowwarningset',
      name: 'flowwarningset',
      component: Flowwarningset
    }, {
      path: 'createuser',
      name: 'createuser',
      component: Createuser
    }, {
      path: 'createjg',
      name: 'createjg',
      component: Createjg
    }, {
      path: 'createauth',
      name: 'createauth',
      component: Createauth
    }, {
      path: 'alipay',
      name: 'alipay',
      component: Alipay
    }, {
      path: 'wechart',
      name: 'wechart',
      component: Wechart
    }, {
      path: 'createlang',
      name: 'createlang',
      component: Createlang
    }, {
      path: 'createcurrency',
      name: 'createcurrency',
      component: Createcurrency
    }, {
      path: 'createnation',
      name: 'createnation',
      component: Createnation
    }]
  }

export default formRoute
