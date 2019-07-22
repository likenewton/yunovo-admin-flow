package cn.yunovo.iov.fc.model.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel(value = "PayListChartDataResultBean对象", description = "财务报表-充值分析统计图表bean")
public class PayListChartDataResultBean {

	@ApiModelProperty("值")
	private Integer value;
	
	@ApiModelProperty("统计项")
	private String name;
	
	@ApiModelProperty("金额")
	private Double price;
}
