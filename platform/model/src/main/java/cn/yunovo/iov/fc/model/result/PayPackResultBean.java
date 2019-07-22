package cn.yunovo.iov.fc.model.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel(value = "PayPackResultBean对象", description = "财务报表-套餐充值统计结果bean")
public class PayPackResultBean {

	@ApiModelProperty(value="套餐金额")
	private Double gprs_price;
	
	@ApiModelProperty(value="套餐流量")
	private Double gprs_amount;
	
	@ApiModelProperty(value="充值次数")
	private Integer pay_count;
	
	@ApiModelProperty(value="已付次数")
	private Integer paid_count;
	
	@ApiModelProperty(value="未付次数")
	private Integer nopaid_count;
	
	@ApiModelProperty(value="已付金额")
	private Double paid_money;
	
	@ApiModelProperty(value="未付金额")
	private Double nopaid_money;
	
}
