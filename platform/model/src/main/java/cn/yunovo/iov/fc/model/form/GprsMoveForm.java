package cn.yunovo.iov.fc.model.form;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import cn.yunovo.iov.fc.model.BaseForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@ApiModel
@EqualsAndHashCode(callSuper=false)
public class GprsMoveForm extends BaseForm implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotEmpty(message="请输入正确的旧卡ICCID")
	@ApiModelProperty("旧卡ICCID")
	private String old_card_iccid;
	
	@NotEmpty(message="请输入正确的新卡ICCID")
	@ApiModelProperty("新卡ICCID")
	private String new_card_iccid;
	
	@ApiModelProperty("迁移备注")
	private String move_memo;
}
