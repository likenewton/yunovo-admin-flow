<template>
  <el-dialog :visible="dialogVisible" @close="cancel" :close-on-click-modal="false" width="65%">
    <span slot="title">{{config.title}}</span>
    <div v-loading="config.loadDialog" class="dialog_content">
      <div slot v-html="config.content"></div>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button size="small" v-if="config.isShowCancelBtn" @click="cancel">{{config.cancelText}}</el-button>
      <el-button size="small" v-if="config.isShowOkBtn" type="primary" @click="makesure">{{config.okText}}</el-button>
    </div>
  </el-dialog>
</template>
<script>
import { mapState, mapMutations } from 'vuex'

export default {
  name: 'vDialog',
  data() {
    return {
      defaultPara: {
        loadDialog: false, // 默认情况下不显示加载动画
        title: '标题',
        content: '',
        cancelText: '取 消',
        isShowCancelBtn: true,
        okText: '确 定',
        isShowOkBtn: true,
        // 确定按钮回调
        okCb: () => true
      }
    }
  },
  props: {
    dialogPara: {
      type: Object,
      default: {}
    }
  },
  mounted() {},
  computed: {
    ...mapState({
      dialogVisible: 'dialogVisible',
    }),
    config() {
      return Object.assign(this.defaultPara, this.dialogPara)
    }
  },
  methods: {
    ...mapMutations([
      'SET_DIALOGVISIBLE'
    ]),
    cancel() {
      this.SET_DIALOGVISIBLE({ dialogVisible: false })
    },
    makesure() {
      if (this.config.okCb()) {
        this.SET_DIALOGVISIBLE({ dialogVisible: false })
      }
    }
  }
}

</script>
<style lang="scss">
</style>
