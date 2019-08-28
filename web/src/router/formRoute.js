// [chunk2](侧边栏菜单动态路由)
const Asidemenu = r => require.ensure([], () => r(require('@/components/aside/index.vue')), 'chunk2')
const Blank = r => require.ensure([], () => r(require('@/components/aside/blank.vue')), 'chunk2')

// [chunk3](一般是表单增删改查页面)
const Batchcreate = r => require.ensure([], () => r(require('@/components/forms/batchcreate.vue')), 'chunk3')
const Rechargecomboset = r => require.ensure([], () => r(require('@/components/forms/rechargecomboset.vue')), 'chunk3')
const Createjg = r => require.ensure([], () => r(require('@/components/forms/createjg.vue')), 'chunk3')
const Createdispatch = r => require.ensure([], () => r(require('@/components/forms/createdispatch.vue')), 'chunk3') // 机构权限分配
const Alipay = r => require.ensure([], () => r(require('@/components/forms/alipay.vue')), 'chunk3')
const Wechart = r => require.ensure([], () => r(require('@/components/forms/wechart.vue')), 'chunk3')
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
      path: 'createjg',
      name: 'createjg',
      component: Createjg
    }, {
      path: 'createdispatch',
      name: 'createdispatch',
      component: Createdispatch
    }, {
      path: 'alipay',
      name: 'alipay',
      component: Alipay
    }, {
      path: 'wechart',
      name: 'wechart',
      component: Wechart
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
