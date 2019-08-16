package cn.yunovo.iov.fc.common.utils.log;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OpBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	@ApiModelProperty(value="操作类型，OpTypeEnum")
	private String type;
	
	@ApiModelProperty(value="操作名称")
	private String name;
	
	@ApiModelProperty(value="操作描述")
	private String desc;
	
	@ApiModelProperty(value="操作返回的结果")
	private String result;
	
	@ApiModelProperty(value="操作的数据 ")
	private JSONObject data;
	
	@ApiModelProperty(value="扩展数据")
	private JSONObject other;
	
	@ApiModelProperty(value="方法")
	private String method;
	
}
