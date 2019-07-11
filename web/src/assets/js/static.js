module.exports = {
  // 侧边栏菜单配置数据(最高权限)
  asideData: [{
    title: '业务管理',
    icon: 'el-icon-fontyewu-tianchong',
    name: 'manage'
  }, {
    title: '统计分析',
    icon: 'el-icon-fonttongjibaobiao',
    name: 'statistics'
  }, {
    title: '财务报表',
    icon: 'el-icon-fontcaiwufenxi',
    name: 'financial'
  }, {
    title: '用户权限',
    icon: 'el-icon-fontyonghu1',
    name: 'userauth'
  }, {
    title: '系统设置',
    icon: 'el-icon-fontxitongshezhi',
    name: 'setting'
  }],
  // 必须、误删
  operData: [{
    title: {
      create: '新增流量卡出货批次',
      update: '编辑流量卡出货批次',
      check: '预览流量卡出货批次'
    },
    name: 'batchcreate'
  }, {
    title: {
      create: '新增充值套餐设置',
      update: '编辑充值套餐设置',
      check: '预览充值套餐设置'
    },
    name: 'rechargecomboset'
  }, {
    title: {
      create: '新增用户',
      update: '编辑用户',
      check: '预览用户'
    },
    name: 'createuser'
  }, {
    title: {
      create: '新增机构',
      update: '编辑机构',
      check: '预览机构'
    },
    name: 'createjg'
  }, {
    title: {
      create: '新增语言设置',
      update: '编辑语言设置',
      check: '预览语言设置'
    },
    name: 'createlang'
  }, {
    title: {
      create: '新增货币设置',
      update: '编辑货币设置',
      check: '预览货币设置'
    },
    name: 'createcurrency'
  }, {
    title: {
      create: '新增国家区域',
      update: '编辑国家区域',
      check: '预览国家区域'
    },
    name: 'createnation'
  }, {
    title: {
      create: '新增权限',
      update: '编辑机权限',
      check: '预览权限'
    },
    name: 'createauth'
  }, {
    title: '流量预警设置',
    name: 'flowwarningset'
  }, {
    title: 'ICCID卡详情列表',
    name: 'rechargeDetail'
  }, {
    title: '机构权限分配',
    name: 'createdispatch'
  }, {
    title: '充值总额统计',
    name: 'iccidList'
  }, {
    title: '支付宝支付',
    name: 'alipay'
  }, {
    title: '微信支付',
    name: 'wechart'
  }, {
    title: '机构统计',
    name: 'orgList'
  }, {
    title: '机构统计详情',
    name: 'orgListDetail'
  }],
  token: 'iov-token',
  // 分页器公共配置项
  pageSizes: [10, 20, 40, 60],
  colorList: ['#3cb1ff', '#ffc367', '#ff7477', '#27da99', '#3ecec9', '#9a83da'],
}
