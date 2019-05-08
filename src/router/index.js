// 按需加载路由
// chunk1(公共页面)
const Login = r => require.ensure([], () => r(require('@/components/login.vue')), 'chunk1')
const Home = r => require.ensure([], () => r(require('@/components/home.vue')), 'chunk1')
const Sysmenu = r => require.ensure([], () => r(require('@/components/sysmenu.vue')), 'chunk1')
const Logout = r => require.ensure([], () => r(require('@/components/logout.vue')), 'chunk1')

// chunk2(侧边栏菜单动态路由)
const Asidemenu = r => require.ensure([], () => r(require('@/components/aside/index.vue')), 'chunk2')
const Blank = r => require.ensure([], () => r(require('@/components/aside/blank.vue')), 'chunk2')
const Card = r => require.ensure([], () => r(require('@/components/manage/card.vue')), 'chunk2') // 流量卡
const Cardreset = r => require.ensure([], () => r(require('@/components/manage/cardreset.vue')), 'chunk2') // 卡重置
const Cardbatch = r => require.ensure([], () => r(require('@/components/manage/cardbatch.vue')), 'chunk2') // 出货批次
const Cardcombo = r => require.ensure([], () => r(require('@/components/manage/cardcombo.vue')), 'chunk2') // 充值套餐
const Flowgift = r => require.ensure([], () => r(require('@/components/manage/flowgift.vue')), 'chunk2') // 流量赠送

// chunk3(一般是表单增删改查页面)
const Batchcreate = r => require.ensure([], () => r(require('@/components/forms/batchcreate.vue')), 'chunk3')
const Rechargecomboset = r => require.ensure([], () => r(require('@/components/forms/rechargecomboset.vue')), 'chunk3')

// 存展示列表
const RechargeDetail = r => require.ensure([], () => r(require('@/components/list/rechargeDetail.vue')), 'chunk4')

let router = new VueRouter({
  // 路由第一大类必须是侧边栏对应路由，非侧边栏的路由从第二大类开始部署
  routes: [{
    path: '/asidemenu',
    name: 'asidemenu',
    component: Asidemenu,
    redirect: '/asidemenu/menu',
    // 侧边栏菜单
    children: [{
      path: 'menu',
      name: 'menu',
      component: Blank,
      redirect: '/asidemenu/menu/card',
      children: [{
        path: 'card',
        name: 'card',
        component: Card
      }, {
        path: 'cardreset',
        name: 'cardreset',
        component: Cardreset
      }, {
        path: 'cardbatch',
        name: 'cardbatch',
        component: Cardbatch
      }, {
        path: 'cardcombo',
        name: 'cardcombo',
        component: Cardcombo
      }, {
        path: 'flowgift',
        name: 'flowgift',
        component: Flowgift
      }]
    }]
  }, {
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
    }]
  }, {
    // 展示列表
    path: '/list',
    name: 'list',
    component: Asidemenu,
    children: [{
      path: 'rechargeDetail',
      name: 'rechargeDetail',
      component: RechargeDetail
    }]
  }, {
    path: '',
    name: 'home',
    component: Home
  }, {
    path: '/login',
    name: 'login',
    component: Login
  }, {
    path: '/sysmenu',
    name: 'sysmenu',
    component: Sysmenu
  }, {
    path: '/logout',
    name: 'logout',
    component: Logout
  }]
})

export default router
