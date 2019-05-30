<template>
  <div class="sysmenu-container">
    <el-header>
      <v-head :messageCount="0" :showMenu="false"></v-head>
    </el-header>
    <div class="menu-warpper">
      <div class="menu-item item_1 pointer">
        <img src="../assets/images/diaodu.png">
        <span class="menu-text">MTP任务调度</span>
      </div>
      <div class="menu-item item_2"></div>
      <div class="menu-item item_3 pointer">
        <img src="../assets/images/yunyin.png">
        <span class="menu-text">运营中心</span>
      </div>
      <div class="menu-item item_4 pointer">
        <img src="../assets/images/guanli.png">
        <span class="menu-text">CDP管理平台</span>
      </div>
      <div class="menu-item item_5 pointer">
        <img src="../assets/images/shebei.png">
        <span class="menu-text">设备中心</span>
      </div>
      <div class="menu-item item_6 pointer" @click="enter_6">
        <img src="../assets/images/liuliang.png">
        <span class="menu-text">流量中心</span>
      </div>
      <div class="menu-item item_7 pointer">
        <img src="../assets/images/shouquan.png">
        <span class="menu-text">系统授权中心</span>
      </div>
      <div class="menu-item item_8"></div>
      <div class="menu-item item_9"></div>
    </div>
    <footer class="footer2">
      <span>Copyright©2014-2019&nbsp;深圳市云智易联科技有限公司&nbsp;版权所有</span>
    </footer>
  </div>
</template>
<script>
export default {
  name: 'vAside',
  data() {
    return {
      form: {

      }
    }
  },
  mounted() {
    
  },
  methods: {
    enter_6() {
      console.log('enter_6')
    }
  }
}

</script>
<style lang="scss">
.sysmenu-container {
  position: relative;
  width: 100%;
  height: 100%;
  background: url('../assets/images/menu_bg.jpg') no-repeat;
  background-position: center;
  background-size: cover;
  overflow-y: hidden;

  .menu-warpper {
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate3D(-50%, -50%, 0);
    width: 800px;
    height: 500px;

    .menu-item {
      position: absolute;
      width: 250px;
      height: 215px;
      background: url('../assets/images/border_default.png') no-repeat;
      background-size: 100% 100%;
      top: 50%;
      left: 50%;
      transform: translate3D(-50%, -65%, 0);

      &:hover {
        background-image: url('../assets/images/border_hover.png');
      }

      img {
        width: 45%;
        position: absolute;
        left: 50%;
        top: 50%;
        transform: translate3D(-50%, -60%, 0);
      }

      .menu-text {
        position: absolute;
        width: 100%;
        text-align: center;
        bottom: 32px;
        font-size: 15px;
        color: #fff;
      }

      &.item_1 {
        margin-left: -383px;
        margin-top: 0px;
      }

      &.item_2 {
        margin-left: -383px;
        margin-top: 219px;
        background-image: url('../assets/images/border_more.png');
      }

      &.item_3 {
        margin-left: -192px;
        margin-top: -109px;
      }

      &.item_4 {
        margin-left: -192px;
        margin-top: 109px;
      }

      &.item_6 {
        margin-left: 192px;
        margin-top: -109px;
      }

      &.item_7 {
        margin-left: 192px;
        margin-top: 109px;
      }

      &.item_8 {
        margin-left: 383px;
        margin-top: 0px;
        background-image: url('../assets/images/border_more.png');
      }

      &.item_9 {
        margin-left: 383px;
        margin-top: 219px;
        background-image: url('../assets/images/border_more.png');
      }
    }
  }

  .footer2 {
    position: absolute;
    bottom: 0;
    height: 70px;
    width: 100%;
    background: rgba(0, 0, 0, 0.3);
    display: flex;
    align-items: center;
    justify-content: center;

    * {
      font-size: 14px;
      color: #fff;
    }
  }
}

@media screen and (max-width: 1600px) {
  .sysmenu-container {
    .menu-warpper {
      width: 640px;
      height: 400px;

      .menu-item {
        width: 200px;
        height: 172px;
        transform: translate3D(-50%, -65%, 0);

        img {
          width: 45%;
          transform: translate3D(-50%, -60%, 0);
        }

        .menu-text {
          bottom: 25px;
          font-size: 14px;
        }

        &.item_1 {
          margin-left: -308px;
          margin-top: 0px;
        }

        &.item_2 {
          margin-left: -308px;
          margin-top: 176px;
        }

        &.item_3 {
          margin-left: -154px;
          margin-top: -88px;
        }

        &.item_4 {
          margin-left: -154px;
          margin-top: 88px;
        }

        &.item_6 {
          margin-left: 154px;
          margin-top: -88px;
        }

        &.item_7 {
          margin-left: 154px;
          margin-top: 88px;
        }

        &.item_8 {
          margin-left: 308px;
          margin-top: 0px;
        }

        &.item_9 {
          margin-left: 308px;
          margin-top: 176px;
        }
      }
    }

    .footer2 {
      height: 60px;

      * {
        font-size: 14px;
      }
    }
  }
}

@media screen and (max-width: 1300px) {
  .sysmenu-container {
    .menu-warpper {
      width: 540px;
      height: 338px;

      .menu-item {
        width: 170px;
        height: 145px;
        transform: translate3D(-50%, -65%, 0);

        img {
          width: 45%;
          transform: translate3D(-50%, -60%, 0);
        }

        .menu-text {
          bottom: 22px;
          font-size: 12px;
        }

        &.item_1 {
          margin-left: -261px;
          margin-top: 0px;
        }

        &.item_2 {
          margin-left: -261px;
          margin-top: 149px;
        }

        &.item_3 {
          margin-left: -130px;
          margin-top: -74px;
        }

        &.item_4 {
          margin-left: -130px;
          margin-top: 74px;
        }

        &.item_6 {
          margin-left: 130px;
          margin-top: -74px;
        }

        &.item_7 {
          margin-left: 130px;
          margin-top: 74px;
        }

        &.item_8 {
          margin-left: 261px;
          margin-top: 0px;
        }

        &.item_9 {
          margin-left: 261px;
          margin-top: 149px;
        }
      }
    }

    .footer2 {
      height: 50px;

      * {
        font-size: 12px;
      }
    }
  }

}

</style>
