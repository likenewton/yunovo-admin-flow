module.exports = {
  // 侧边栏菜单配置数据
  asideData: [{
    title: '业务管理',
    icon: 'el-icon-fontmima',
    name: 'menu',
    children: [{
      title: '流量卡',
      name: 'card'
    }, {
      title: '卡重置',
      name: 'cardreset'
    }, {
      title: '出货批次',
      name: 'cardbatch'
    }, {
      title: '充值套餐',
      name: 'cardcombo'
    }, {
      title: '流量赠送',
      name: 'flowgift'
    }, {
      title: '流量迁移',
      name: 'flowmigration'
    }, {
      title: '流量预警',
      name: 'flowwarning'
    }, {
      title: '卡实名制',
      name: 'cardauth'
    }]
  }, {
    title: '统计分析',
    icon: 'el-icon-fonttongjibaobiao',
    name: 'statistics',
    children: [{
      title: '月度统计',
      name: 'monthuse'
    }, {
      title: '已停卡况',
      name: 'deadstatus'
    }, {
      title: '停卡日志',
      name: 'stopcardlog'
    }, {
      title: '用量异常',
      name: 'useanomaly'
    }, {
      title: '续费数据',
      name: 'renewdata'
    }, {
      title: '用量数据',
      name: 'usedata'
    }]
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
    title: '流量预警设置',
    name: 'flowwarningset'
  }, {
    title: '充值详情列表',
    name: 'rechargeDetail'
  }],
  pageSizes: [10, 20, 30, 40]
}
