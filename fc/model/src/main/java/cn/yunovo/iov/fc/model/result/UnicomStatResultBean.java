package cn.yunovo.iov.fc.model.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel(value = "UnicomStatResultBean对象", description = "统计分析-联通情况查询结果bean")
public class UnicomStatResultBean {

	@ApiModelProperty(value = "机构id")
	private Integer org_id;
	
	@ApiModelProperty(value = "机构名称")
	private String org_name;
	
	@ApiModelProperty(value = "售卡数量")
	private String card_count;
	
	@ApiModelProperty(value = "已激活数")
	private Integer activated;
	
	@ApiModelProperty(value = "未激活数")
	private Integer nonactivated;
	
	@ApiModelProperty(value = "使用总流量")
	private Double unicom_count;
	
	@ApiModelProperty(value = "当月使用流量")
	private Double month_count;
	
}
