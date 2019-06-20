// [chunk2](侧边栏菜单动态路由)
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
const Rechargemonth = r => require.ensure([], () => r(require('@/components/financial/rechargemonth.vue')), 'chunk2') // 充值月度统计
const RechargeParticulars = r => require.ensure([], () => r(require('@/components/financial/rechargeParticulars.vue')), 'chunk2') // 充值月度统计
const Jgrecharge = r => require.ensure([], () => r(require('@/components/financial/jgrecharge.vue')), 'chunk2') // 机构充值统计
const Comborecharge = r => require.ensure([], () => r(require('@/components/financial/comborecharge.vue')), 'chunk2') // 套餐充值
// 权限管理
const UserManage = r => require.ensure([], () => r(require('@/components/userauth/userManage.vue')), 'chunk2') // 用户管理
const JgManage = r => require.ensure([], () => r(require('@/components/userauth/jgManage.vue')), 'chunk2') // 机构管理
const AuthManage = r => require.ensure([], () => r(require('@/components/userauth/authManage.vue')), 'chunk2') // 权限管理
// 系统设置
const PaySet = r => require.ensure([], () => r(require('@/components/systemset/payset.vue')), 'chunk2') // 支付管理
const Langset = r => require.ensure([], () => r(require('@/components/systemset/langset.vue')), 'chunk2') // 语言设置
const Currencyset = r => require.ensure([], () => r(require('@/components/systemset/currencyset.vue')), 'chunk2') // 货币设置
const Nationset = r => require.ensure([], () => r(require('@/components/systemset/nationset.vue')), 'chunk2') // 国家区域
const Sysparaset = r => require.ensure([], () => r(require('@/components/systemset/sysparaset.vue')), 'chunk2') // 系统参数
const Datapreserve = r => require.ensure([], () => r(require('@/components/systemset/datapreserve.vue')), 'chunk2') // 数据维护

let menuRoute = {
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
      component: Monthuse
    }, {
      path: 'deadstatus',
      name: 'deadstatus',
      component: Deadstatus
    }, {
      path: 'stopcardlog',
      name: 'stopcardlog',
      component: Stopcardlog
    }, {
      path: 'useanomaly',
      name: 'useanomaly',
      component: Useanomaly
    }, {
      path: 'renewdata',
      name: 'renewdata',
      component: Renewdata
    }, {
      path: 'usedata',
      name: 'usedata',
      component: Usedata
    }, {
      path: 'unicomdata',
      name: 'unicomdata',
      component: Unicomdata
    }, {
      path: 'operatedata',
      name: 'operatedata',
      component: Operatedata
    }, {
      path: 'informsource',
      name: 'informsource',
      component: Informsource
    }]
  }, {
    path: 'financial',
    name: 'financial',
    component: Blank,
    redirect: '/asidemenu/financial/rechargesum',
    children: [{
      path: 'rechargesum',
      name: 'rechargesum',
      component: Rechargesum
    }, {
      path: 'rechargemonth',
      name: 'rechargemonth',
      component: Rechargemonth
    }, {
      path: 'rechargeParticulars',
      name: 'rechargeParticulars',
      component: RechargeParticulars
    }, {
      path: 'jgrecharge',
      name: 'jgrecharge',
      component: Jgrecharge
    }, {
      path: 'comborecharge',
      name: 'comborecharge',
      component: Comborecharge
    }]
  }, {
    path: 'userauth',
    name: 'userauth',
    component: Blank,
    redirect: '/asidemenu/userauth/userManage',
    children: [{
      path: 'userManage',
      name: 'userManage',
      component: UserManage
    }, {
      path: 'jgManage',
      name: 'jgManage',
      component: JgManage
    }, {
      path: 'authManage',
      name: 'authManage',
      component: AuthManage
    }]
  }, {
    path: 'setting',
    name: 'setting',
    component: Blank,
    redirect: '/asidemenu/setting/payset',
    children: [{
      path: 'paySet',
      name: 'paySet',
      component: PaySet
    }, {
      path: 'langset',
      name: 'langset',
      component: Langset
    }, {
      path: 'currencyset',
      name: 'currencyset',
      component: Currencyset
    }, {
      path: 'nationset',
      name: 'nationset',
      component: Nationset
    }, {
      path: 'sysparaset',
      name: 'sysparaset',
      component: Sysparaset
    }, {
      path: 'datapreserve',
      name: 'datapreserve',
      component: Datapreserve
    }]
  }]
}

export default menuRoute
