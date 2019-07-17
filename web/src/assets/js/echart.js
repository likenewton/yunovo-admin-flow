import { formatFlowUnit } from './unit.js'

class Echarts {
  constructor(para = {}) {
    // 实例化时初始的参数
    this.data = Object.assign({
      colorList: ['#3cb1ff', '#ffc367', '#ff7477', '#27da99', '#3ecec9', '#9a83da'],
      type: 'line',
      title: '',
      legend: [],
      xAxis: { data: [] }, // x轴坐标
      series: [{ data: [] }] // 里面放置数据
    }, para)
    this.option = {
      tooltip: {
        trigger: 'axis'
      },
      legend: {
        data: []
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      toolbox: {
        show: true,
        right: 20,
        feature: {
          dataView: {
            show: true,
            iconStyle: {
              borderColor: '#9a83da'
            },
            emphasis: {
              iconStyle: {
                borderColor: '#9a8dda'
              }
            },
            readOnly: true,
            optionToContent(opt) {
              let axisData = opt.xAxis[0].data
              let series = opt.series
              let table = `<div class="dataViewContainer"><table class="dataViewTable"><tbody>
                <tr class="thead">
                  <th class="th">${para.dataViewTitle || ''}</th>
                  ${function a() {
                    let str = ''
                    series.forEach((v) => {
                      str += `<th class="th">${v.name}</th>`
                    })
                    return str
                  }()}
                </tr>
                ${function b() {
                  let str = ''
                  axisData.forEach((v, i) => {
                    str += `<tr class="tbody"><td class="td">${v}</td>`
                    series.forEach((v) => {
                      if (v.name.indexOf('流量') > -1) {
                        str += `<td class="td">${formatFlowUnit(v.data[i], 3, false)}</td>`
                      } else {
                        str += `<td class="td">${v.data[i]}</td>`
                      }
                    })
                    str += '</tr>'
                  })
                  return str
                }()}
              </tbody></table></div>`
              return table
            },
            // 调用optionToContent之后一定要配置此项
            contentToOption() {},
            buttonColor: '#ff7477'
          },
          restore: {
            show: true,
            iconStyle: {
              borderColor: '#ffc367'
            },
            emphasis: {
              iconStyle: {
                borderColor: '#ffcf85'
              }
            }
          },
          saveAsImage: {
            show: true,
            iconStyle: {
              borderColor: '#3cb1ff'
            },
            emphasis: {
              iconStyle: {
                borderColor: '#63c1ff'
              }
            }
          }
        }
      },
      yAxis: [{
        type: 'value',
        splitLine: { show: false }
      }, {
        type: 'value',
        show: false,
        axisLabel: {
          formatter: function(value) {
            return value + '%'
          }
        },
        splitLine: { show: false }
      }],
      xAxis: {
        type: 'category',
        data: [], //要设置的
        axisLabel: {
          textStyle: {
            fontSize: 12
          }
        },
      },
      series: []
    }
  }

  setOption(para) {
    let _this = this;
    this.data = Object.assign(this.data, para)
    let xAxisData = this.data.xAxis.data
    this.option.series = []
    this.option.legend.data = this.data.legend.data
    this.option.xAxis.data = xAxisData
    this.option.dataZoom = {
      startValue: (xAxisData.length - 20) >= 0 ? xAxisData[xAxisData.length - 20] : xAxisData[0],
      type: 'inside'
    }
    if (this.data.title) {
      this.option.title = {
        text: this.data.title
      }
    }
    if (this.data.formatter) {
      this.option.tooltip.formatter = this.data.formatter
    }
    // this.option.toolbox.feature
    this.data.series.forEach((v, i) => {
      this.option.series.push({
        name: v.name || this.data.legend[i],
        type: v.type || this.data.type,
        data: v.data,
        stack: v.stack,
        barMaxWidth: 100,
        yAxisIndex: v.yAxisIndex || 0,
        itemStyle: {
          normal: {
            color: this.data.colorList[i],
          }
        }
      })
      if (v.yAxisIndex === 1) {
        this.option.yAxis[1].show = true
      }
    })
  }

  getOption() {
    return this.option
  }

  static init(para) {
    return new Echarts(para)
  }
}

export default Echarts

// _echart = new Api.ECHARTS()
// _echart.setOption({
//   title: '订单趋势',
//   legend: ['订单数'],
//   xAxis: { data: ['2019-05-20', '2019-05-21', '2019-05-22', '2019-05-23', '2019-05-24'] },
//   series: [{
//     data: [21, 52, 54, 32, 80]
//   }]
// this.myEchart.setOption(_echart.getOption())
