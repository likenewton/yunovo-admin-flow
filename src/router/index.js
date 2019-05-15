// 按需加载路由
// chunk1(公共页面)
const Login = r => require.ensure([], () => r(require('@/components/login.vue')), 'chunk1')
const Home = r => require.ensure([], () => r(require('@/components/home.vue')), 'chunk1')
const Sysmenu = r => require.ensure([], () => r(require('@/components/sysmenu.vue')), 'chunk1')
const Logout = r => require.ensure([], () => r(require('@/components/logout.vue')), 'chunk1')

// chunk2(侧边栏菜单动态路由)
const Asidemenu = r => require.ensure([], () => r(require('@/components/aside/index.vue')), 'chunk2')
const Blank = r => require.ensure([], () => r(require('@/components/aside/blank.vue')), 'chunk2')
// 业务管理
const Card = r => require.ensure([], () => r(require('@/components/manage/card.vue')), 'chunk2') // 流量卡
const Cardreset = r => require.ensure([], () => r(require('@/components/manage/cardreset.vue')), 'chunk2') // 卡重置
const Cardbatch = r => require.ensure([], () => r(require('@/components/manage/cardbatch.vue')), 'chunk2') // 出货批次
const Cardcombo = r => require.ensure([], () => r(require('@/components/manage/cardcombo.vue')), 'chunk2') // 充值套餐
const Flowgift = r => require.ensure([], () => r(require('@/components/manage/flowgift.vue')), 'chunk2') // 流量赠送
const Flowmigration = r => require.ensure([], () => r(require('@/components/manage/flowmigration.vue')), 'chunk2') // 流量迁移
const Flowwarning = r => require.ensure([], () => r(require('@/components/manage/flowwarning.vue')), 'chunk2') // 流量预警
const Cardauth = r => require.ensure([], () => r(require('@/components/manage/cardauth.vue')), 'chunk2') // 卡实名制
// 统计分析
const Monthuse = r => require.ensure([], () => r(require('@/components/statistics/monthuse.vue')), 'chunk2') // 月度用量
const Deadstatus = r => require.ensure([], () => r(require('@/components/statistics/deadstatus.vue')), 'chunk2') // 已停卡况
const Stopcardlog = r => require.ensure([], () => r(require('@/components/statistics/stopcardlog.vue')), 'chunk2') // 停卡日志
const Useanomaly = r => require.ensure([], () => r(require('@/components/statistics/useanomaly.vue')), 'chunk2') // 用量异常
const Renewdata = r => require.ensure([], () => r(require('@/components/statistics/renewdata.vue')), 'chunk2') // 续费数据
const Usedata = r => require.ensure([], () => r(require('@/components/statistics/usedata.vue')), 'chunk2') // 用量数据
const Unicomdata = r => require.ensure([], () => r(require('@/components/statistics/unicomdata.vue')), 'chunk2') // 联通情况
const Operatedata = r => require.ensure([], () => r(require('@/components/statistics/operatedata.vue')), 'chunk2') // 运营数据
const Informsource = r => require.ensure([], () => r(require('@/components/statistics/informsource.vue')), 'chunk2') // 充值通知与支付来源统计
// 财务报表
const Rechargesum = r => require.ensure([], () => r(require('@/components/financial/rechargesum.vue')), 'chunk2') // 流量卡充值总额

// chunk3(一般是表单增删改查页面)
const Batchcreate = r => require.ensure([], () => r(require('@/components/forms/batchcreate.vue')), 'chunk3')
const Rechargecomboset = r => require.ensure([], () => r(require('@/components/forms/rechargecomboset.vue')), 'chunk3')
const Flowwarningset = r => require.ensure([], () => r(require('@/components/forms/flowwarningset.vue')), 'chunk3')

// chunk4(展示列表)
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
      }, {
        path: 'flowmigration',
        name: 'flowmigration',
        component: Flowmigration
      }, {
        path: 'flowwarning',
        name: 'flowwarning',
        component: Flowwarning
      }, {
        path: 'cardauth',
        name: 'cardauth',
        component: Cardauth
      }]
    }, {
      path: 'statistics',
      name: 'statistics',
      component: Blank,
      redirect: '/asidemenu/statistics/monthuse',
      children: [{
        path: 'monthuse',
        name: 'monthuse',
        component: Monthuse,
      }, {
        path: 'deadstatus',
        name: 'deadstatus',
        component: Deadstatus,
      }, {
        path: 'stopcardlog',
        name: 'stopcardlog',
        component: Stopcardlog,
      }, {
        path: 'useanomaly',
        name: 'useanomaly',
        component: Useanomaly,
      }, {
        path: 'renewdata',
        name: 'renewdata',
        component: Renewdata,
      }, {
        path: 'usedata',
        name: 'usedata',
        component: Usedata,
      }, {
        path: 'unicomdata',
        name: 'unicomdata',
        component: Unicomdata,
      }, {
        path: 'operatedata',
        name: 'operatedata',
        component: Operatedata,
      }, {
        path: 'informsource',
        name: 'informsource',
        component: Informsource,
      }]
    }, {
      path: 'financial',
      name: 'financial',
      component: Blank,
      redirect: '/asidemenu/financial/rechargesum',
      children: [{
        path: 'rechargesum',
        name: 'rechargesum',
        component: Rechargesum,
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
    }, {
      path: 'flowwarningset',
      name: 'flowwarningset',
      component: Flowwarningset
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
    component: Asidemenu,
    children: [{
      path: '',
      name: 'home',
      component: Home
    }]
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

router.beforeEach((to, from, next) => {
  //会在任意路由跳转前执行, 检测当前是否还处于登录状态

  // 测试， 永远处于登录状态
  (true || to.path === '/login') ? next(): next('/login')
})

export default router
