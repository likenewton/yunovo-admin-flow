class Echarts {
  constructor(para) {
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
            optionToContent(opt) {
              console.log(opt)
              let axisData = opt.xAxis[0].data
              let series = opt.series
              let table = `<table style="width:100%;text-align:center"><tbody>
                <tr>
                  <td></td>
                  ${function a() {
                    let str = ''
                    series.forEach((v) => {
                      str += `<td>${v.name}</td>`
                    })
                    return str
                  }()}
                </tr>
                ${function b() {
                  let str = ''
                  axisData.forEach((v, i) => {
                    str += `<tr><td>${v}</td>`
                    series.forEach((v) => {
                      str += `<td>${v.data[i]}</td>`
                    })
                    str += '</tr>'
                  })
                  return str
                }()}
              </tbody></table>`
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
      yAxis: {
        type: 'value',
        splitLine: { show: false }
      },
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
      startValue: (xAxisData.length - 10) >= 0 ? xAxisData[xAxisData.length - 10] : xAxisData[0],
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
    this.data.series.forEach((v, i) => {
      this.option.series.push({
        name: this.data.legend[i],
        type: this.data.type,
        data: v.data,
        itemStyle: {
          normal: {
            color: this.data.colorList[i],
          }
        }
      })
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
