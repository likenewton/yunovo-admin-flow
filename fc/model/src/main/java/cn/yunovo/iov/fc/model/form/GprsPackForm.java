package cn.yunovo.iov.fc.model.form;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

import cn.yunovo.iov.fc.model.BaseForm;
import cn.yunovo.iov.fc.model.form.group.DeleteGroupValidate;
import cn.yunovo.iov.fc.model.form.group.InsertGroupValidate;
import cn.yunovo.iov.fc.model.form.group.UpdateGroupValidate;
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
	
	@NotNull(message="请选择对应的套餐信息", groups= {UpdateGroupValidate.class, DeleteGroupValidate.class})
	@ApiModelProperty(value = "套餐编号")
	private Integer pack_id;

	@NotEmpty(message="请选择机构！", groups= {UpdateGroupValidate.class, InsertGroupValidate.class})
	@ApiModelProperty(value = "机构编号")
	private Integer org_id;

	@Length(max=50, message="套餐名称字数不能超过50字！",groups= {UpdateGroupValidate.class, InsertGroupValidate.class})
	@ApiModelProperty(value = "套餐名称")
	private String pack_name;

	@Length(max=200, message="备注字数不能超过200字！",groups= {UpdateGroupValidate.class, InsertGroupValidate.class})
	@ApiModelProperty(value = "套餐备注")
	private String pack_memo;

	@NotNull(message="请选择套餐模式", groups= {UpdateGroupValidate.class, InsertGroupValidate.class})
	@Size(max=1, min=0, message="套餐模式填写有误", groups= {UpdateGroupValidate.class, InsertGroupValidate.class})
	@ApiModelProperty(value = "套餐模式：0叠加套餐 1延期套餐")
	private Short pack_mode;

	@Size(max=1, min=0, message="是否推荐填写有误", groups= {UpdateGroupValidate.class, InsertGroupValidate.class})
	@ApiModelProperty(value = "是否推荐：0否 1是")
	private Short pack_recom;

	@ApiModelProperty(value = "返利金额(元)")
	private BigDecimal pack_rebate;

	@NotNull(message="套餐状态填写有误", groups= {DeleteGroupValidate.class})
	@Size(max=1, min=0, message="套餐状态填写有误", groups= {DeleteGroupValidate.class})
	@ApiModelProperty(value = "套餐状态：0停用 1启用")
	private Short pack_status;

	@NotNull(message="请填写正确的流量值！", groups= {UpdateGroupValidate.class, InsertGroupValidate.class})
	@DecimalMin(value="0.000001", message="请填写正确的流量值！", groups= {UpdateGroupValidate.class,InsertGroupValidate.class} )
	@ApiModelProperty(value = "流量(MB)")
	private Double gprs_amount;

	@NotNull(message="请填写正确的价格！", groups= {UpdateGroupValidate.class, InsertGroupValidate.class})
	@DecimalMin(value="0.000001", message="请填写正确的价格！", groups= {UpdateGroupValidate.class,InsertGroupValidate.class} )
	@ApiModelProperty(value = "流量价格(元)")
	private BigDecimal gprs_price;

	@NotNull(message="请填写正确的价格！", groups= {UpdateGroupValidate.class, InsertGroupValidate.class})
	@DecimalMin(value="0.1", message="请填写正确的折扣数值,需大于0.1小等于1", groups= {UpdateGroupValidate.class, InsertGroupValidate.class} )
	@DecimalMax(value="1.0", message="请填写正确的折扣数值,需大于0.1小等于1", groups= {UpdateGroupValidate.class, InsertGroupValidate.class})
	@ApiModelProperty(value = "流量折扣>0<=1(0.5代表打五折)")
	private BigDecimal gprs_discount;

	@NotNull(message="请填写正确的分配期数", groups= {UpdateGroupValidate.class, InsertGroupValidate.class})
	@Min(value = 1, message="请填写正确的分配期数", groups= {UpdateGroupValidate.class, InsertGroupValidate.class})
	@ApiModelProperty(value = "分配几个月")
	private Integer allot_month;

	@NotNull(message="请填写正确的分配流量值", groups= {UpdateGroupValidate.class, InsertGroupValidate.class})
	@DecimalMin(value="0.000001",message="请填写正确的分配流量值", groups= {UpdateGroupValidate.class, InsertGroupValidate.class})
	@ApiModelProperty(value = "分配流量值(MB)")
	private Double allot_value;

	@NotNull(message="请选择正确的套餐类型", groups= {UpdateGroupValidate.class, InsertGroupValidate.class})
	@Size(max=1, min=0, message="请选择正确的套餐类型", groups= {UpdateGroupValidate.class, InsertGroupValidate.class})
	@ApiModelProperty(value = "套餐类型,1 固定套餐, 0 非固定套餐")
	private Integer allot;
	
	@NotNull(message="是否清零填写有误", groups= {UpdateGroupValidate.class, InsertGroupValidate.class})
	@Size(max=1, min=0, message="是否清零填写有误", groups= {UpdateGroupValidate.class, InsertGroupValidate.class})
	@ApiModelProperty(value = "分配流量是否清零:0不清 1清零")
	private Short allot_reset;

	@ApiModelProperty(value = "套餐生命周期：>=1按月，(>0<1)*100按天")
	private Float live_month;

}
