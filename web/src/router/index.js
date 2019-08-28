
import formRoute from './formRoute.js' // 表单页面
import listRoute from './listRoute.js' // 二级列表页面
// 按需加载路由
// [chunk1](公共页面)
const Home = r => require.ensure([], () => r(require('@/components/home.vue')), 'chunk1')
const Asidemenu = r => require.ensure([], () => r(require('@/components/aside/index.vue')), 'chunk2')

let router = new VueRouter({
  mode: 'history', // history模式，上线使用，测试环境使用hash模式
  base: '/flowCenter/', // 因为项目是在根目录下的flowCenter目录中所有要加一个base路由
  routes: [{
    path: '',
    component: Asidemenu,
    children: [{
      path: '',
      name: 'home',
      component: Home
    }]
  }]
})

router.addRoutes([formRoute])
router.addRoutes([listRoute])

export default router
