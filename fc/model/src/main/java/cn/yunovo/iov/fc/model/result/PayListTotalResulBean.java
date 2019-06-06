package cn.yunovo.iov.fc.model.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel(value = "PayListTotalResulBean对象", description = "财务报表-充值明细汇总结果bean")
public class PayListTotalResulBean {

	@ApiModelProperty(value = "充值次数")
	private Integer gprs_cards;
	
	@ApiModelProperty(value="套餐流量")
	private Double gprs_amount;
	
	@ApiModelProperty(value="套餐价格")
	private Double gprs_price;
	
	@ApiModelProperty(value="返利金额")
	private Double rebate_money;
	
}
