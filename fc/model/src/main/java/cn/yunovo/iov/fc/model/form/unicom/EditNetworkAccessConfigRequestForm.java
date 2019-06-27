package cn.yunovo.iov.fc.model.form.unicom;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@ApiModel
public class EditNetworkAccessConfigRequestForm extends JasperForm implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotEmpty(message="nacid missing")
	@ApiModelProperty("通信计划编号 (PNL:公网不限速，PL:公网限速，NVNL:无视频不限速，NVL:无视频限速)")
	private String nacid;
	
	@NotEmpty(message="iccid missing")
	@ApiModelProperty("需要设置的流量卡iccid")
	private String iccid;

}
