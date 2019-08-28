<template>
  <div class="v-token-container"></div>
</template>
<script>
import Api from 'assets/js/api.js'

export default {
  name: 'vToken',
  data() {
    return {
      token: Api.STATIC.token
    }
  },
  mounted() {
    if (this.getURLToken()) {
      let name = sessionStorage.getItem('target_name') || 'home'
      let query = JSON.parse(sessionStorage.getItem('target_query') || '{}')
      // 如果url中包含有token, 将token保存到localStorage中
      localStorage.setItem(this.token, this.getURLToken())
      // 然后刷新页面消除url中的token
      delete query[this.token]
      this.$router.replace({ name, query })
    }
  },
  methods: {
    getURLToken() {
      return Api.UNITS.getQuery(this.token)
    }
  }
}

</script>
<style lang="scss">
.v-token-container {
  position: fixed;
  height: 0;
  width: 0;
}

</style>
