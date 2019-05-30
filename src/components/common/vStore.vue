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
    if (this.cardTypes.length === 0) {
      this.getCardTypes()
    }
  },
  methods: {
    ...mapActions([
      'getCardTypes'
    ])
  },
  computed: {
    ...mapState({
      cardTypes: 'cardTypes' // 卡商列表
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
