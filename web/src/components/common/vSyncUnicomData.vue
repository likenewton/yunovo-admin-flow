<template>
  <div class="v-SyncUnicomData-container">
    <el-dialog :title="'详细信息(' + selectData.card_iccid + ')'" :visible.sync="dialogDetailVisible" @close="closeDialogDetailVisible" :close-on-click-modal="false">
      <div slot class="clearfix" v-loading="dialogDetailLoadData">
        <div id="iccid_detail" style="width:100%">
          <div class="item" v-if="selectData.card_sn">
            <span class="fbs-left">MSISDN</span>
            <span class="fbs-right">{{selectData.card_sn}}</span>
          </div>
          <div class="item" v-if="selectData.card_imsi">
            <span class="fbs-left">IMSI</span>
            <span class="fbs-right">{{selectData.card_imsi}}</span>
          </div>
          <div class="item" v-if="selectData.time_paid">
            <span class="fbs-left">上次充值时间</span>
            <span class="fbs-right">{{selectData.time_paid}}</span>
          </div>
          <div class="item" v-if="selectData.time_last">
            <span class="fbs-left">设备更新时间</span>
            <span class="fbs-right">{{selectData.time_last}}</span>
          </div>
          <div class="item" v-if="selectData.msg">
            <span class="fbs-left">联通流量卡状态</span>
            <span class="fbs-right">{{selectData.msg}}</span>
          </div>
          <div class="item" v-if="selectData.consumeDataAll || selectData.consumeDataAll===0">
            <span class="fbs-left">联通当月使用流量</span>
            <span class="fbs-right" v-html="formatFlowUnit(selectData.consumeDataAll)"></span>
          </div>
          <div class="item" v-if="selectData.consumeDataMon || selectData.consumeDataMon===0">
            <span class="fbs-left">联通累计使用流量</span>
            <span class="fbs-right" v-html="formatFlowUnit(selectData.consumeDataMon)"></span>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>
<script>

export default {
  name: 'vSyncUnicomData',
  data() {
    return {
      selectData: {},
      dialogDetailVisible: false,
      dialogDetailLoadData: true
    }
  },
  methods: {
    closeDialogDetailVisible() {
      this.selectData = {}
      this.dialogDetailVisible = false
    },
    // 展示dialog iccid的详细
    getSyncUnicomData(scope) {
      this.dialogDetailVisible = true
      this.dialogDetailLoadData = true
      this.selectData = scope.row
      _axios.send({
        method: 'get',
        url: _axios.ajaxAd.checkSyncUnicomData,
        params: { 
          card_sn: scope.row.card_sn,
          card_iccid: scope.row.card_iccid
        },
        done: ((res) => {
          if (res.data.status === -1) {
            Vue.prototype.$notify.error({
              title: '错误',
              message: res.data.msg || '同步失败'
            })
            this.dialogDetailLoadData = false
          } else {
            this.dialogDetailLoadData = false
            this.selectData = Object.assign({}, scope.row, res.data)
          }
        }),
        fail: (() => {
          this.dialogDetailLoadData = false
        })
      })
    },
  }
}

</script>
<style lang="scss">
.v-SyncUnicomData-container {
  #iccid_detail {
    .item {
      height: 34px;
      line-height: 34px;

      >span {
        padding: 0 10px;
        text-align: center;
      }

      .fbs-left {
        width: 50%;
      }
    }
  }
}

</style>
