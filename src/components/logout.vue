<template>
  <div class="logout-container">
    <div class="circle-wrapper">
      <el-row>
        <h3>恭喜您，注销成功</h3>
      </el-row>
      <el-row>
        <div class="prompt_text">页面将在 <span class="time">{{time}}s</span> 后跳转登录页面</div>
      </el-row>
      <el-row>
        <el-button :size="size" type="success" @click="gotologin">前往</el-button>
      </el-row>
    </div>
  </div>
</template>
<script>
import Api from 'assets/js/api.js'

export default {
  name: 'vAside',
  data() {
    return {
      size: {
        medium: 'medium',
        small: 'small',
        mini: 'mini',
      } [Api.UNITS.getSize()],
      time: 3,
      Timer: null // 定时器
    }
  },
  mounted() {
    Api.UNITS.getSize()
    this.Timer = setInterval(() => {
      if (--this.time <= 0) {
        clearInterval(this.Timer)
        // this.gotologin()
      }
    }, 1000)
  },
  computed: {

  },
  methods: {
    gotologin() {
      this.$router.push({ name: 'login' })
    }
  }
}

</script>
<style lang="scss">
.logout-container {
  position: relative;
  width: 100%;
  height: 100%;
  background: url('../assets/images/logout.jpg') no-repeat;
  background-position: center;
  background-size: cover;
  overflow-y: hidden;

  .circle-wrapper {
    display: flex;
    align-items: center;
    justify-content: center;
    flex-direction: column;
    position: relative;
    width: 250px;
    height: 250px;
    left: 50%;
    top: 23%;
    transform: translate3d(-50%, 0, 0);
    overflow: hidden;

    .el-row {
      margin: 15px 0;

      .prompt_text {
        font-size: 14px;
        text-align: center;
      }

      h3 {
        font-size: 28px;
        text-align: center;
      }

      .time {
        color: #ffc367;
      }
    }

    * {
      color: #fff;
    }
  }
}

@media screen and (max-width: 1600px) {
  .logout-container {

    .circle-wrapper {
      width: 200px;
      height: 200px;
      top: 23%;

      .el-row {
        margin: 12px 0;

        .prompt_text {
          font-size: 13px;
          text-align: center;
        }

        h3 {
          font-size: 24px;
          text-align: center;
        }
      }
    }
  }
}

@media screen and (max-width: 1300px) {
  .logout-container {

    .circle-wrapper {
      width: 170px;
      height: 170px;
      top: 23%;

      .el-row {
        margin: 10px 0;

        .prompt_text {
          font-size: 12px;
          text-align: center;
        }

        h3 {
          font-size: 20px;
          text-align: center;
        }
      }
    }
  }
}

</style>
