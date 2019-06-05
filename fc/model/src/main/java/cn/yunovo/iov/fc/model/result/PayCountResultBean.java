package cn.yunovo.iov.fc.model.result;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel(value = "PayCountResultBean对象", description = "财务报表-充值统计bean")
public class PayCountResultBean {

	
	@ApiModelProperty(value = "总充值数")
	private Integer pay_count;
	
	
	@ApiModelProperty(value = "分配总流量")
	private BigDecimal gprs_amount;
	
	@ApiModelProperty(value = "充值总金额")
	private BigDecimal money_count;
	
	@ApiModelProperty(value = "返利金额")
	private BigDecimal rebate_money;
	
	@ApiModelProperty(value = "机构id")
	private Integer org_id;
	
	@ApiModelProperty(value = "支付方式")
	private Integer pay_method;

	
	@ApiModelProperty(value = "机构名称")
	private String org_name;
	
	@ApiModelProperty(value = "支付方式名称")
	private String pay_method_name;
	
}
