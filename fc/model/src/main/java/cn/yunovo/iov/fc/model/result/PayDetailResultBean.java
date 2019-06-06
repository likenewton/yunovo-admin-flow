package cn.yunovo.iov.fc.model.result;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel(value = "PayDetailResultBean对象", description = "财务报表-充值详情")
public class PayDetailResultBean {

	@ApiModelProperty(value = "卡ICCID")
	private String card_iccid;
	
	@ApiModelProperty(value = "流量卡编号")
	private Integer card_id;
	
	@ApiModelProperty(value = "卡号码")
	private String card_sn;
	
	@ApiModelProperty(value = "充值次数")
	private Integer pay_count;
	
	@ApiModelProperty(value = "分配总流量")
	private Double gprs_amount;
	
	@ApiModelProperty(value = "充值总金额")
	private Double money_count;
	
	@ApiModelProperty(value = "返利总金额")
	private Double money_rebate;
}
