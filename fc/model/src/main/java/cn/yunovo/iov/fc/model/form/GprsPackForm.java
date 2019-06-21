package cn.yunovo.iov.fc.model.form;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.alibaba.fastjson.JSONObject;

import cn.yunovo.iov.fc.model.BaseForm;
import cn.yunovo.iov.fc.model.ValidateGroup;
import cn.yunovo.iov.fc.model.exception.FormValidateException;
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

	@NotNull(message="请选择机构！", groups= {UpdateGroupValidate.class, InsertGroupValidate.class})
	@ApiModelProperty(value = "机构编号")
	private Integer org_id;

	@Length(max=50, message="套餐名称字数不能超过50字！",groups= {UpdateGroupValidate.class, InsertGroupValidate.class})
	@ApiModelProperty(value = "套餐名称")
	private String pack_name;

	@Length(max=200, message="备注字数不能超过200字！",groups= {UpdateGroupValidate.class, InsertGroupValidate.class})
	@ApiModelProperty(value = "套餐备注")
	private String pack_memo;

	@NotNull(message="请选择套餐模式", groups= {UpdateGroupValidate.class, InsertGroupValidate.class})
	@Max(value=1, message="套餐模式填写有误", groups= {UpdateGroupValidate.class, InsertGroupValidate.class})
	@Min(value=0, message="套餐模式填写有误", groups= {UpdateGroupValidate.class, InsertGroupValidate.class})
	@ApiModelProperty(value = "套餐模式：0叠加套餐 1延期套餐")
	private Short pack_mode;

	@Max(value=1, message="是否推荐填写有误", groups= {UpdateGroupValidate.class, InsertGroupValidate.class})
	@Min(value=0, message="是否推荐填写有误", groups= {UpdateGroupValidate.class, InsertGroupValidate.class})
	@ApiModelProperty(value = "是否推荐：0否 1是")
	private Short pack_recom;

	@ApiModelProperty(value = "返利金额(元)")
	private BigDecimal pack_rebate ;

	@NotNull(message="套餐状态填写有误", groups= {DeleteGroupValidate.class})
	@Range(max=1,min=0,message="套餐状态填写有误",groups= {DeleteGroupValidate.class})
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

	@ApiModelProperty(value = "分配流量值(MB)")
	private Double allot_value;

	@Max(value=1, message="请选择正确的套餐类型", groups= {UpdateGroupValidate.class, InsertGroupValidate.class})
	@Min(value=0, message="请选择正确的套餐类型", groups= {UpdateGroupValidate.class, InsertGroupValidate.class})
	@ApiModelProperty(value = "套餐类型,1 固定套餐, 0 非固定套餐")
	private Integer allot;
	
	@NotNull(message="是否清零填写有误", groups= {UpdateGroupValidate.class, InsertGroupValidate.class})
	@Max(value=1, message="是否清零填写有误", groups= {UpdateGroupValidate.class, InsertGroupValidate.class})
	@Min(value=0, message="是否清零填写有误", groups= {UpdateGroupValidate.class, InsertGroupValidate.class})
	@ApiModelProperty(value = "分配流量是否清零:0不清 1清零")
	private Short allot_reset;

	@NotNull(message="请选择有效周期", groups= {UpdateGroupValidate.class, InsertGroupValidate.class})
	@ApiModelProperty(value = "套餐生命周期：>=1按月，(>0<1)*100按天")
	@DecimalMin(value="0.000001", message="请填写正确的有效周期", groups= {UpdateGroupValidate.class, InsertGroupValidate.class} )
	private Float live_month;
	
	public void validate(Class<? extends ValidateGroup>... groups) {
		
		super.validate(groups);
		if(this.allot == 0 && this.allot_month > this.live_month) {
			throw new FormValidateException("套餐类型为月均套餐时，有效周期必须大于或等于分配月数","allot_month", JSONObject.toJSONString(this));
		}
		this.pack_rebate = this.pack_rebate == null ? new BigDecimal(0) : this.pack_rebate;
		float pack_pack_rebate =pack_rebate.floatValue();
		float pack_gprs_price = gprs_price.floatValue();
		if( pack_pack_rebate < 0F  || pack_pack_rebate > pack_gprs_price ) {
			throw new FormValidateException("返利金额不能小于零且不能大于套餐金额","allot_month", JSONObject.toJSONString(this));
		}
		
	}
	
	public Double computeAllotValue() {
		return gprs_amount / this.getAllot_month();
	}

}
