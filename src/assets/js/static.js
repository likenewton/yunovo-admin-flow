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
    }, {
      title: '联通流量卡',
      name: 'unicomdata'
    }, {
      title: '运营统计',
      name: 'operatedata'
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
  // 分页器公共配置项
  pageSizes: [10, 20, 30, 40],
  // svg path:// '路径'
  svg: {
    save: 'M831.4 252.2L711.5 132.4c-10.3-10.3-24.2-16.1-38.8-16.1h-450c-23.3 0-42.2 18.9-42.2 42.2v709.4c0 23.3 18.9 42.2 42.2 42.2h582.6c23.3 0 42.2-18.9 42.2-42.2V291c-0.1-14.5-5.8-28.5-16.1-38.8zM619.6 159v159.8c0 4-3.3 7.3-7.3 7.3H387.2c-4 0-7.3-3.3-7.3-7.3V159h239.7z m67.7 708.4H340.8v-292c0-3.9 3.2-7.1 7.1-7.1h332.4c3.9 0 7.1 3.2 7.1 7.1v292z m117.4 0H730v-292c0-27.5-22.3-49.8-49.8-49.8H347.8c-27.5 0-49.8 22.3-49.8 49.8v292h-74.7V159.1h113.9v159.8c0 27.6 22.4 50.1 50.1 50.1h225.1c27.6 0 50.1-22.4 50.1-50.1V159h10.4c3.2 0 6.3 1.3 8.6 3.6l119.9 119.9c2.3 2.3 3.6 5.3 3.6 8.6l-0.3 576.3z'
  }
}
