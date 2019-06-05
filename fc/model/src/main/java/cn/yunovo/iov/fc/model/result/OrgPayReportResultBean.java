package cn.yunovo.iov.fc.model.result;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel(value = "OrgPayReportResultBean对象", description = "统计分析-联通情况查询结果bean")
public class OrgPayReportResultBean {

	
	@ApiModelProperty(value = "总充值数")
	private Integer paid_count;
	
	@ApiModelProperty(value = "已付次数")
	private Integer paid_amount;
	
	@ApiModelProperty(value = "未付次数")
	private Integer nopaid_amount;
	
	@ApiModelProperty(value = "机构名称")
	private BigDecimal gprs_amount;
	
	@ApiModelProperty(value = "已付金额")
	private BigDecimal paid_money;
	
	@ApiModelProperty(value = "未付金额")
	private BigDecimal nopaid_money;
	
	@ApiModelProperty(value = "返利金额")
	private BigDecimal rebate_money;
	
	@ApiModelProperty(value = "机构id")
	private Integer org_id;
	
	@ApiModelProperty(value = "支付方式")
	private Integer pay_method;
	
	@ApiModelProperty(value = "交易月份")
	private String mdate;
	
	@ApiModelProperty(value = "机构名称")
	private String org_name;
	
	@ApiModelProperty(value = "支付方式名称")
	private String pay_method_name;
	
}
