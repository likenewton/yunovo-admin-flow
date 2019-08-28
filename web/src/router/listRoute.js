// [chunk2](侧边栏菜单动态路由)
const Asidemenu = r => require.ensure([], () => r(require('@/components/aside/index.vue')), 'chunk2')
const Blank = r => require.ensure([], () => r(require('@/components/aside/blank.vue')), 'chunk2')

// [chunk4](展示列表,页面里面的二级页面)
const RechargeDetail = r => require.ensure([], () => r(require('@/components/list/rechargeDetail.vue')), 'chunk4')
const IccidList = r => require.ensure([], () => r(require('@/components/list/iccidList.vue')), 'chunk4')
const OrgList = r => require.ensure([], () => r(require('@/components/list/orgList.vue')), 'chunk4')
const OrgListDetail = r => require.ensure([], () => r(require('@/components/list/orgListDetail.vue')), 'chunk4')

let listRoute = {
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
  }

export default listRoute
