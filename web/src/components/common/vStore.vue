<template>
  <div class="v-store-container"></div>
</template>
<script>
import { mapActions } from 'vuex'

// 该组件主要用于vuex 中获取只需获取一次的数据，用于公共的展示
export default {
  name: 'vStore',
  data() {
    return {}
  },
  mounted() {
    this.getAuthButtons()
    if (this.cardTypes.length === 0) this.getCardTypes()
    if (this.orgs.length === 0) this.getOrgs()
    if (this.notifysFrom.length === 0) this.getNotifySelect()
    if (this.months.length === 0) this.getMonths()
    if (this.payMethodSelect.length === 0) this.getPayMethodSelect()
    if (this.liveMonthSelect.length === 0) this.getLiveMonth()
  },
  methods: {
    ...mapActions([
      'getAuthButtons',
      'getCardTypes',
      'getOrgs',
      'getNotifySelect',
      'getMonths',
      'getPayMethodSelect',
      'getLiveMonth',
    ])
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
