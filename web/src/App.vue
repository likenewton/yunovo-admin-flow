<template>
  <div id="app">
    <div class="page-container">
      <router-view />
    </div>
  </div>
</template>
<script>

if (!!window.ActiveXObject || "ActiveXObject" in window) {
  setTimeout(() => {
    // 如果是IE浏览器，不要显示v-loading遮罩
    $('#app').addClass('IE')
  }, 0)
}

export default {
  name: 'App'
}

</script>
<style lang="scss">
@import '../src/assets/style/base.scss';

html,
body {
  min-height: 100%;
  height: 100px;
  background: #fff;
  min-width: 1200px;
}

#app {
  height: 100%;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;

  .page-container {
    height: 100%;
  }
}

</style>
