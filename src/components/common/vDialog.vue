<template>
  <el-dialog :visible="dialogVisible" @close="cancel">
    <span slot="title">{{dialogPara.title}}</span>
    <div v-loading="dialogPara.loadDialog" class="dialog_content">
      <div slot v-html="dialogPara.content"></div>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button size="small" @click="cancel">取 消</el-button>
      <el-button size="small" type="primary" @click="makesure">确 定</el-button>
    </div>
  </el-dialog>
</template>
<script>
import { mapState, mapMutations } from 'vuex'

export default {
  name: 'vDialog',
  data() {
    return {}
  },
  props: {
    dialogPara: {
      type: Object,
      default: {
        loadDialog: false, // 默认情况下不显示加载动画
        title: '标题',
        content: ''
      }
    }
  },
  mounted() {

  },
  computed: {
    ...mapState({
      dialogVisible: 'dialogVisible',
    })
  },
  methods: {
    ...mapMutations([
      'SET_DIALOGVISIBLE'
    ]),
    cancel() {
      this.SET_DIALOGVISIBLE({dialogVisible: false})
    },
    makesure() {
      this.SET_DIALOGVISIBLE({dialogVisible: false})
    }
  }
}

</script>
<style lang="scss">
</style>
