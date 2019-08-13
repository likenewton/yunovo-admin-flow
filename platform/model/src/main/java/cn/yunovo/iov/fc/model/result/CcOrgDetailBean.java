package cn.yunovo.iov.fc.model.result;

import java.util.List;

import cn.yunovo.iov.fc.model.entity.CcOrg;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class CcOrgDetailBean extends CcOrg{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<String> device_orgs;
	
}
