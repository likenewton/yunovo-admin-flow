<template>
  <div class="_404_container" v-if="show">
    <div class="content">
      <i class="el-icon-font404"></i>
      <i class="el-icon-fonticon-test"></i>
      <div class="text">很抱歉！您访问的页面不存在</div>
      <el-button class="btn" size="small" type="primary" @click="$router.back()">返回上一页</el-button>
      <el-button class="btn" size="small" type="primary" @click="$router.push({name: 'home'})">回到首页</el-button>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      show: false
    }
  },
  mounted() {}
}

</script>
<style lang="scss">
._404_container {
  background: #f5f5f5;
  position: relative;
  height: 100%;

  .content {
    position: absolute;
    left: 50%;
    top: 35%;
    width: 260px;
    transform: translate3D(-50%, 0, 0);

    .el-icon-font404 {
      font-size: 130px;
      color: #666;
    }

    .el-icon-fonticon-test {
      font-size: 150px;
      position: absolute;
      top: -40%;
      left: 44%;
      color: $danger;
      background-image: -webkit-gradient(linear, 0 0, 0 bottom, from(rgba(0, 128, 0, 1)), to(rgba(51, 51, 51, 1)));
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
    }

    .text {
      text-align: center;
      margin-top: 10px;
      color: #666;
    }

    .btn {
      width: 47%;
      margin-top: 20px;
    }
  }
}

</style>
