<template>
  <el-dialog :visible="checkboxVisible" @close="cancelChoice" :close-on-click-modal="false" :width="width" class="v-checkbox">
    <span slot="title">{{title}}<span>({{checkedData.length}}/{{checkboxData.length}})</span></span>
    <div class="dialog_content">
      <div slot>
        <el-checkbox class="title-checkbox" :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange">全选</el-checkbox>
        <el-checkbox-group v-model="checkedData" @change="handleChoiceChange" :style="{'maxHeight': winHeight/2 + 'px'}">
          <el-checkbox v-for="item in checkboxData" :label="item.value" :key="item.value">{{item.label}}</el-checkbox>
        </el-checkbox-group>
      </div>
    </div>
    <div slot="footer" class="dialog-footer">
      <!-- <el-button size="small" type="warning" @click="handleChoiceChange([])">清 空</el-button> -->
      <el-button size="small" type="warning" @click="handleChoiceChange(defaultData)">恢复默认</el-button>
      <el-button size="small" type="primary" @click="makesureChoice">保 存</el-button>
    </div>
  </el-dialog>
</template>
<script>
export default {
  name: 'vCheckbox',
  data() {
    return {
      checkboxVisible: false,
      checkedData: [], // 当前的选择（显示）
      checkedSaveData: this.defaultData, // 保存的选择
      isIndeterminate: false, // 是否半选
      checkAll: false, // 是否全选
    }
  },
  props: {
    title: {
      type: 'string',
      default: '表格列展示'
    },
    width: {
      type: "string",
      default: '800px'
    },
    // checkbox 全数据 { label, value }
    checkboxData: {
      type: Array,
      default: []
    },
    // 默认勾选数据([value1, value2])
    defaultData: {
      type: Array,
      default: []
    }
  },
  mounted() {
    let checkedData = JSON.parse(localStorage.getItem(`__${this.routeName}Check__`))
    if (checkedData) {
      this.checkedSaveData = checkedData
    } else {
      this.checkedSaveData = this.defaultData
    }
  },
  methods: {
    openChoice() {
      this.checkboxVisible = true
      // checked弹框关闭的时候都会被清空，打开弹框的时候要从temp获取选择的数据
      this.handleChoiceChange(this.checkedSaveData)
    },
    // 弹框关闭的时清空checked
    cancelChoice() {
      this.checkboxVisible = false
      this.handleChoiceChange(this.defaultData)
    },
    // 多选发生改变时执行
    handleChoiceChange(para = []) {
      let checkedCount = para.length
      this.checkedData = para
      this.checkAll = checkedCount === this.checkboxData.length
      this.isIndeterminate = checkedCount > 0 && checkedCount < this.checkboxData.length
    },
    // 保存选择
    makesureChoice() {
      this.$message.success(`已选择 ${this.checkedData.length} 项`)
      this.checkedSaveData = this.checkedData
      this.checkboxVisible = false
      setTimeout(() => {
        this.$emit('checkSave', this.checkedSaveData)
      }, 200)
    },
    // 全选
    handleCheckAllChange(val) {
      this.checkedData = val ? this.checkboxData.map((v) => v.value) : [];
      this.isIndeterminate = false;
    },
  }
}

</script>
<style lang="scss">
.v-checkbox {
  .el-checkbox-group {
    overflow: auto;

    label {
      margin: 0;
      padding: 5px;
      width: 370px;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;

      &.is-checked {
        .el-checkbox__label {
          color: #666;
        }
      }

      &:hover {
        background: #fafafa;
      }

      .el-checkbox__inner {
        vertical-align: initial;
      }
    }
  }

  .title-checkbox {
    &.is-checked {
      .el-checkbox__label {
        color: #666;
      }
    }

    height: 35px;
    padding: 5px;
  }
}

</style>
