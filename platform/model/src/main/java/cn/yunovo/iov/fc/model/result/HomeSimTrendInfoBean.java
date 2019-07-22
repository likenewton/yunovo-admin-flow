package cn.yunovo.iov.fc.model.result;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "HomeSimTrendInfoBean对象", description = "首页-sim卡使用趋势")
public class HomeSimTrendInfoBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String stats_time;
	
	@ApiModelProperty(value = "售卡总数")
	private Integer card_total;
	
	@ApiModelProperty(value = "流量卡激活总数")
	private Integer active_total;
	
	@ApiModelProperty(value = "流量卡未激活总数")
	private Integer unactive_total;
	
	@ApiModelProperty(value = "停卡总数")
	private Integer stop_total;
	
}
