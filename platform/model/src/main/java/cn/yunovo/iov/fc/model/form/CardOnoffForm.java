package cn.yunovo.iov.fc.model.form;

import java.io.Serializable;

import cn.yunovo.iov.fc.model.BaseForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@ApiModel
@EqualsAndHashCode(callSuper=false)
public class CardOnoffForm extends BaseForm implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty("流量卡iccid")
	private String card_iccid;
	
	@ApiModelProperty("开关：0为开卡，1为停卡")
	private Short status;

}
