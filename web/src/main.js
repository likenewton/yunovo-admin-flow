import App from './App'
import router from './router'
import store from './store'
import upperFirst from 'lodash/upperFirst'
import camelCase from 'lodash/camelCase'
import Api from 'assets/js/api.js'
import MinXin from '@/components/MinXins/index.js'
import menuRoute from './router/menuRoute.js' // 菜单页面路由
import 'babel-polyfill'
import '../theme/index.css'
import '../static/iconfont/iconfont.css'
import 'assets/js/filter.js'
import 'assets/js/directive.js'

const requireComponent = require.context(
  // 其组件目录的相对路径
  './components/common',
  // 是否查询其子目录
  false,
  // 匹配基础组件文件名的正则表达式
  /[A-Z]\w+\.(vue|js)$/
)

requireComponent.keys().forEach(fileName => {
  // 获取组件配置
  const componentConfig = requireComponent(fileName)

  // 获取组件的 PascalCase 命名
  const componentName = upperFirst(
    camelCase(
      // 剥去文件名开头的 `./` 和结尾的扩展名
      fileName.replace(/^\.\/(.*)\.\w+$/, '$1')
    )
  )

  // 全局注册组件
  Vue.component(
    componentName,
    // 如果这个组件选项是通过 `export default` 导出的，
    // 那么就会优先使用 `.default`，
    // 否则回退到使用模块的根。
    componentConfig.default || componentConfig
  )
})

Vue.mixin(MinXin) // 全局混入
window._axios = Api.AXIOS.init() // 将_axios注册到全局，方便调用
Vue.prototype.$echarts = echarts
Vue.prototype.$http = axios
Vue.prototype.$store = store
Vue.config.productionTip = false

// 路由进入前的全局钩子
router.beforeEach((to, from, next) => {
  if (Api.UNITS.getQuery(Api.STATIC.token)) {
    // 当页面重定向过来的时候带的token 要保存进去
    localStorage.setItem(Api.STATIC.token, Api.UNITS.getQuery(Api.STATIC.token))
  }
  _axios.send({
    method: 'get',
    url: _axios.ajaxAd.isLogin,
    done: (res) => {
      // 这里一定登录了
      if (store.state.authMenu.length === 0) {
        // 如果权限列表为空就从后台拉取菜单权限信息
        _axios.send({
          method: 'get', // 模拟获取菜单权限
          url: '../flowCenter/static/authMenu.json', // 这里因config/index.js中配置了flowCenter/static
          done: ((res) => {
            let resources = res.userResources
            let asideData = Api.STATIC.asideData
            let authMenu = Api.UNITS.getAuthMenu(asideData, resources) // 这里将本地的asideData与后台提交的数据生成authMenu
            let dynamicMenuRoute = Api.UNITS.getMenuRoute(menuRoute, resources) // 这里将本地的菜单路由与后台提供的数据生成动态路由
            store.commit('SET_AUTHMENU', { authMenu }) // 保存动态菜单
            router.addRoutes([dynamicMenuRoute]) // 生成动态路由
          })
        })
      }
      next()
    }
  })
})

new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
})

// 测试卡  89860619000004125005  89860619000004110510
