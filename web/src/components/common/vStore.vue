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
  created() {
    this.getAuthButtons()
    this.getCardTypes()
    this.getOrgs()
    this.getNotifySelect()
    this.getMonths()
    this.getPayMethodSelect()
    this.getLiveMonth()
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
