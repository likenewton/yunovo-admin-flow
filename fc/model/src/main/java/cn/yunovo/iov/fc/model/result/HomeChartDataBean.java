package cn.yunovo.iov.fc.model.result;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "HomeChartDataBean对象", description = "首页图标结果bean")
public class HomeChartDataBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 统计日期
	 */
	@ApiModelProperty("统计日期")
	private String stats_time;
	
	@ApiModelProperty("机构名称")
	private String org_name;
	
	
	/**
	 * 值1
	 */
	@ApiModelProperty("值1")
	private Number val;
	
	/**
	 * 值2
	 */
	@ApiModelProperty("值2")
	private Number val2;
	
	/**
	 * 值2
	 */
	@ApiModelProperty("值3")
	private Number val3;
	
}
