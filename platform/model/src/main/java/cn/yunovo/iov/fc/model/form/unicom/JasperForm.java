package cn.yunovo.iov.fc.model.form.unicom;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;

import cn.yunovo.iov.fc.model.BaseForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;

@ApiModel
@EqualsAndHashCode(callSuper=false)
public class JasperForm extends BaseForm implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull(message="missing card_type")
	@ApiModelProperty("卡商类型,1智网吉林,2智网JASPER卡,3吉林长春,4智网定向卡,5双apn。具体描述参考gprs.cc_grps_card.card_type 字段")
	private Integer card_type;
	
	@ApiModelProperty("Message id used to uniquely identify (from the API client's perspective) this request.\r\n" + 
			"                    Client code can use \"correlationId\" in the response (from the ResponseType) to identify which\r\n" + 
			"                    request this response is regarding. Any string value can be provided if client does not care\r\n" + 
			"                    about this feature.")
	private String messageId;
	
	@ApiModelProperty("The API version (schema version) this request is based upon.")
	private String version;

	public Integer getCard_type() {
		return card_type;
	}

	public void setCard_type(Integer card_type) {
		this.card_type = card_type;
	}

	public String getMessageId() {
		
		return StringUtils.defaultIfEmpty(this.messageId, "");
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getVersion() {
		return StringUtils.defaultIfEmpty(this.version, "");
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	
}
