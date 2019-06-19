<template>
  <div class="v-store-container"></div>
</template>
<script>
import { mapState, mapActions } from 'vuex'

// 该组件主要用于vuex 中获取只需获取一次的数据，用于公共的展示
export default {
  name: 'vStore',
  data() {
    return {}
  },
  mounted() {
    // 首页不加载
    if (this.cardTypes.length === 0) this.getCardTypes()
    if (this.orgs.length === 0) this.getOrgs()
    if (this.notifysFrom.length === 0) this.getNotifySelect()
    if (this.months.length === 0) this.getMonths()
    if (this.payMethodSelect.length === 0) this.getPayMethodSelect()
    if (this.liveMonthSelect.length === 0) this.getLiveMonth()
  },
  methods: {
    ...mapActions([
      'getCardTypes',
      'getOrgs',
      'getNotifySelect',
      'getMonths',
      'getPayMethodSelect',
      'getLiveMonth',
    ])
  },
  computed: {
    ...mapState({
      cardTypes: 'cardTypes', // 卡商列表
      orgs: 'orgs', // 机构列表
      notifysFrom: 'notifysFrom', // 通知来源
      months: 'months', // 月份
      payMethodSelect: 'payMethodSelect', // 支付方式
      liveMonthSelect: 'liveMonthSelect', // 有效周期
    })
  }
}

</script>
<style lang="scss">
.v-store-container {
  position: fixed;
  height: 0;
  width: 0;
}

</style>
