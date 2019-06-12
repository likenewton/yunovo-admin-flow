package cn.yunovo.iov.fc.model.form;

import java.io.Serializable;

import cn.yunovo.iov.fc.model.BaseForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@ApiModel
public class PayForm extends BaseForm implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value="支付方式,ex: 支付宝支付 = alipay")
	private String type;
}
