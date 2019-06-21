package cn.yunovo.iov.fc.model.form;

import java.io.Serializable;
import java.math.BigDecimal;

import cn.yunovo.iov.fc.model.BaseForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "GprsPackForm对象", description = "流量套餐表单")
public class GprsPackForm extends BaseForm implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "套餐编号")
	private Integer pack_id;

	@ApiModelProperty(value = "机构编号")
	private Integer org_id;

	@ApiModelProperty(value = "套餐名称")
	private String pack_name;

	@ApiModelProperty(value = "套餐备注")
	private String pack_memo;

	@ApiModelProperty(value = "套餐模式：0叠加套餐 1延期套餐")
	private Short pack_mode;

	@ApiModelProperty(value = "是否推荐：0否 1是")
	private Short pack_recom;

	@ApiModelProperty(value = "返利金额(元)")
	private BigDecimal pack_rebate;

	@ApiModelProperty(value = "套餐状态：0停用 1启用")
	private Short pack_status;

	@ApiModelProperty(value = "流量(MB)")
	private Double gprs_amount;

	@ApiModelProperty(value = "流量价格(元)")
	private BigDecimal gprs_price;

	@ApiModelProperty(value = "流量折扣>0<=1(0.5代表打五折)")
	private BigDecimal gprs_discount;

	@ApiModelProperty(value = "分配几个月")
	private Integer allot_month;

	@ApiModelProperty(value = "分配流量值(MB)")
	private Double allot_value;

	@ApiModelProperty(value = "分配流量是否清零:0不清 1清零")
	private Short allot_reset;

	@ApiModelProperty(value = "套餐生命周期：>=1按月，(>0<1)*100按天")
	private Float live_month;

}
