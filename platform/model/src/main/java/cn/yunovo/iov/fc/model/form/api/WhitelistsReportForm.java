package cn.yunovo.iov.fc.model.form.api;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import cn.yunovo.iov.fc.model.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class WhitelistsReportForm extends BaseForm implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotEmpty(message="sn missing")
	@ApiModelProperty(value="设备SN")
	@Length(max=50, message="invalid sn")
	private String sn;
	
	@NotNull(message="yunovo_gprs_month missing")
	@Min(value = 0, message = "invalid yunovo_gprs_month")
	@ApiModelProperty(value="当月云智apk白名单流量消耗数，单位Byte")
	private Long yunovo_gprs_month;
	
	@NotNull(message="org_gprs_month missing")
	@Min(value = 0, message = "invalid org_gprs_month")
	@ApiModelProperty(value="当月渠道商apk白名单流量消耗数，单位Byte")
	private Long org_gprs_month;
	
	@Length(max=20, min=19, message="invalid iccid")
	@NotEmpty(message="iccid missing")
	@ApiModelProperty(value="流量卡iccid")
	private String iccid;
	
	@NotNull(message="nonce missing")
	@ApiModelProperty(value="场景hash值")
	private Long nonce;

	
}
