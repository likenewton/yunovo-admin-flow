package cn.yunovo.iov.fc.common.utils.log;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Data
public class OpUser implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("id")
	private String id;
	
	@ApiModelProperty("用户登录名")
	private String loginName;
	
	@ApiModelProperty("用户姓名")
	private String userName;
	
	@ApiModelProperty("机构代码")
	private String organCode;

}
