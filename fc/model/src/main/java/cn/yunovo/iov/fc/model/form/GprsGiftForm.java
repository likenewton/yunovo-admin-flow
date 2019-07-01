package cn.yunovo.iov.fc.model.form;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import cn.yunovo.iov.fc.model.BaseForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@ApiModel
@EqualsAndHashCode(callSuper=false)
public class GprsGiftForm extends BaseForm implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotEmpty(message="请输入正确的流量卡ICCID列表；为了能赠送成功，同一批次赠送ICCID个数不能超过200个！")
	@ApiModelProperty("卡列表")
	private String iccids;
	
	@NotNull(message="请选择套餐模式")
	@ApiModelProperty("套餐模式")
	@Range(max=1, min=0, message="无效的参数【套餐模式】")
	private Integer pack_mode;
	
	@NotNull(message="请输入套餐流量")
	@ApiModelProperty("套餐流量")
	private Double gprs_amount;
	
	@NotNull(message="请选择分配月份")
	@ApiModelProperty("分配月数")
	private Integer allot_month;
	
	@NotNull(message="请选择是否清零")
	@ApiModelProperty("是否清零")
	@Range(min=0, max=1,message="无效的参数【是否清零】")
	private Short allot_reset;
	
	@NotNull(message="请选择有效周期")
	@ApiModelProperty("有效周期")
	private Float live_month;
	
	@NotEmpty(message="请输入赠者&备注")
	@ApiModelProperty("赠者&备注")
	@Size(min=2, max=200, message="请填写正确的赠送者名字")
	private String gift_name;
}
