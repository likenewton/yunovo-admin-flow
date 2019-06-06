package cn.yunovo.iov.fc.model.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * GPRS流量充值表
 * </p>
 *
 * @author bill
 * @since 2019-06-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Cc_gprs_pay对象", description = "GPRS流量充值表")
@TableName("cc_gprs_pay")
public class CcGprsPay implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "充值编号")
	@TableId(value = "pay_id", type = IdType.AUTO)
	private Integer pay_id;

	@ApiModelProperty(value = "充值订单号")
	private String pay_sn;

	@ApiModelProperty(value = "流量卡编号")
	private Integer card_id;

	@ApiModelProperty(value = "机构编号")
	private Integer org_id;

	@ApiModelProperty(value = "套餐编号")
	private Integer pack_id;

	@ApiModelProperty(value = "套餐流量(MB)")
	private Double gprs_amount;

	@ApiModelProperty(value = "套餐金额(元)")
	private BigDecimal gprs_price;

	@ApiModelProperty(value = "套餐生命周期：>=1按月，(>0<1)*100按天")
	private Float live_month;

	@ApiModelProperty(value = "支付订单来源")
	private String pay_from;

	@ApiModelProperty(value = "充值备注")
	private String pay_memo;

	@ApiModelProperty(value = "付款方式：0赠送,1微信,2支付宝,3充值卡,4转账")
	private Short pay_method;

	@ApiModelProperty(value = "返利点数")
	private Float rebate_value;

	@ApiModelProperty(value = "返利金额")
	private BigDecimal rebate_money;

	@ApiModelProperty(value = "支付流水号")
	private String transfer_id;

	@ApiModelProperty(value = "是否已支付:0未付款,1已付款")
	private Short is_paid;

	@ApiModelProperty(value = "付款时间")
	private String time_paid;

	@ApiModelProperty(value = "充值时间")
	private String time_added;

	@ApiModelProperty(value = "过期时间")
	private String time_expire;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "机构名称")
	private String org_name;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "支付方式名称")
	private String pay_method_name;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "机构名称")
	private Integer card_type;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "卡商名称")
	private String card_type_name;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "流量卡号")
	private String card_iccid;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "订单来源中文名称")
	private String pay_from_name;
	
	

}
