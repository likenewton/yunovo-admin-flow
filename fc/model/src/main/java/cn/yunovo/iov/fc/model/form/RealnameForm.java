package cn.yunovo.iov.fc.model.form;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import cn.yunovo.iov.fc.model.BaseForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@ApiModel
public class RealnameForm extends BaseForm implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty("流量卡id")
	@NotNull(message="请选择您要审批的申请记录")
	private Integer card_id;
	
	@NotNull(message="无效的审批状态")
	@ApiModelProperty("审批状态, 1无效, 2有效 ")
	private Short status;

}