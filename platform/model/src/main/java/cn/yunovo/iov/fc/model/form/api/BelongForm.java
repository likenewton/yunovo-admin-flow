package cn.yunovo.iov.fc.model.form.api;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import cn.yunovo.iov.fc.model.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class BelongForm extends BaseForm{

	@NotNull(message="iccid missing")
	@Size(max=20, message="invalid iccid")
	private String iccid;
	
	@NotNull(message="device_org missing")
	@Size(max=32, message="invalid device_org")
	private String device_org;
	
	
}
