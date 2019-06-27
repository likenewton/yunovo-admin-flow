package cn.yunovo.iov.fc.model;

import com.alibaba.fastjson.JSONObject;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class LoginInfo {

	@ApiModelProperty(value="用户登录名",dataType="String")
	private String loginName;
	
	@ApiModelProperty(value="用户名称",dataType="String")
	private String userName;
	
	@ApiModelProperty(value="系统登出地址",dataType="String")
	private String logoutUrl;
	
	@ApiModelProperty(value="门户页地址",dataType="String")
	private String ucIndexUrl;
	
	@ApiModelProperty(value="用户id",dataType="String")
	private Integer id;
	
	@ApiModelProperty(value="机构代码",dataType="String")
	private String organCode;
	
	public String buildJsonString() {
		return JSONObject.toJSONString(this);
	}
}
