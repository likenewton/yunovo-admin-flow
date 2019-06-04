
// 按需加载路由
// [chunk1](公共页面)
const Login = r => require.ensure([], () => r(require('@/components/login.vue')), 'chunk1')
const Home = r => require.ensure([], () => r(require('@/components/home.vue')), 'chunk1')
const Sysmenu = r => require.ensure([], () => r(require('@/components/sysmenu.vue')), 'chunk1')
const Logout = r => require.ensure([], () => r(require('@/components/logout.vue')), 'chunk1')

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

// [chunk4](展示列表,页面里面的二级页面)
const RechargeDetail = r => require.ensure([], () => r(require('@/components/list/rechargeDetail.vue')), 'chunk4')
const IccidList = r => require.ensure([], () => r(require('@/components/list/iccidList.vue')), 'chunk4')
const OrgList = r => require.ensure([], () => r(require('@/components/list/orgList.vue')), 'chunk4')
const OrgListDetail = r => require.ensure([], () => r(require('@/components/list/orgListDetail.vue')), 'chunk4')

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
      redirect: '/asidemenu/systemset/payset',
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
  }, {
    // 展示列表
    path: '/list',
    name: 'list',
    component: Asidemenu,
    children: [{
      path: 'rechargeDetail',
      name: 'rechargeDetail',
      component: RechargeDetail
    }, {
      path: 'iccidList',
      name: 'iccidList',
      component: IccidList
    }, {
      path: 'orgList',
      name: 'orgList',
      component: OrgList
    }, {
      path: 'orgListDetail',
      name: 'orgListDetail',
      component: OrgListDetail
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

export default router
