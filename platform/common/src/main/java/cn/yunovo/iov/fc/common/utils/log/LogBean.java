package cn.yunovo.iov.fc.common.utils.log;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LogBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty("日志类型,默认op-log 用户操作日志")
	private String type = "op-log";
	
	@ApiModelProperty("用户访问token")
	private String token;

	@ApiModelProperty("开始时间戳")
	private Long startTimestamp;
	
	@ApiModelProperty("结束时间戳")
	private Long endTimestamp;
	
	@ApiModelProperty("出现错误设置为true")
	private boolean error = false;
	
	@ApiModelProperty("错误代码")
	private String error_code;
	
	@ApiModelProperty("错误消息")
	private String error_msg;
	
	@ApiModelProperty("系统标识消息,fc-web")
	private String sysFlag;
	
	@ApiModelProperty("ip")
	private String ip;
	
	@ApiModelProperty("操作用户信息")
	private OpUser user;
	
	@ApiModelProperty("操作数据")
	private OpBean op;
	
	@ApiModelProperty("扩展字段")
	private JSONObject others;
	
	

}
