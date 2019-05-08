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
      title: '流量卡重置',
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
    title: '充值详情列表',
    name: 'rechargeDetail'
  }]
}
