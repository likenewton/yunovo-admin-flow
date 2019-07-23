package cn.yunovo.iov.fc.api.form;

import javax.validation.constraints.NotEmpty;

import cn.yunovo.iov.fc.model.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ApiCommonParamsForm extends BaseForm{

	@NotEmpty(message="api_key missing")
	private String api_key;
	
	@NotEmpty(message="partner_id missing")
	private String partner_id;
	
	@NotEmpty(message="app_name missing")
	private String app_name;
	
	@NotEmpty(message="app_ver missing")
	private String app_ver;
	
	@NotEmpty(message="rom_ver missing")
	private String rom_ver;
	
	@NotEmpty(message="platform missing")
	private String platform;
	
	@NotEmpty(message="imei missing")
	private String imei;
	
	private String imsi;
	
	@NotEmpty(message="timestamp missing")
	private String timestamp;
	
	@NotEmpty(message="api_sign missing")
	private String api_sign;
}
