package cn.yunovo.iov.fc.model.form.unicom;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.JSONObject;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel
@Data
@EqualsAndHashCode(callSuper=false)
public class GetTerminalDetailsRequestForm extends JasperForm implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotEmpty(message="missing iccids")
	@ApiModelProperty("需要查询详情的流量卡iccid列表")
	private  String[] iccids;

	
}
