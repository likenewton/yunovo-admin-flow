<template>
  <div class="home-container">
    这里什么都没有，--首页--
    <router-link :to="{name: 'login'}">
    	<el-button type="primary">点我去登录页吧</el-button>
    </router-link>
  </div>
</template>
<script>
export default {
  name: 'home',
  data() {
    return {}
  }
}

</script>
<style lang="scss"></style>
