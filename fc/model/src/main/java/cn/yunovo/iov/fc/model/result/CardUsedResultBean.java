package cn.yunovo.iov.fc.model.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel(value = "CardUsedResultBean对象", description = "累计用量查询结果bean")
public class CardUsedResultBean {

	@ApiModelProperty(value = "机构id")
	private Integer org_id;
	
	@ApiModelProperty(value = "机构名称")
	private String org_name;
	
	@ApiModelProperty(value = "售卡数量")
	private String card_count;
	
	@ApiModelProperty(value = "停卡数")
	private Integer unicom_stop;
	
	@ApiModelProperty(value = "已激活数")
	private Integer activated;
	
	@ApiModelProperty(value = "未激活数")
	private Integer nonactivated;
	
	@ApiModelProperty(value = "剩余流量")
	private Double unused_count;
	
	@ApiModelProperty(value = "使用流量")
	private Double used_count;
	
	@ApiModelProperty(value = "分配总流量")
	private Double pay_total;
}
