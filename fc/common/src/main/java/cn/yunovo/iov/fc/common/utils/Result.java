package cn.yunovo.iov.fc.common.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public  class Result<T> {

	
	@ApiModelProperty(value="接口处理结果状态,0 表示成功，非0表示异常",dataType="int")
	private int status;
	
	@ApiModelProperty(value="接口处理信息,非0 则该字段表示异常原因",dataType="int")
	private String msg;
	
	private T data;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
	public static <T> Result<T> build(int status, String msg){
		
		Result<T> result = new Result<>();
		result.setStatus(status);
		result.setMsg(msg);
		return result;
	}
	
	public static <T> Result<T> build(int status, String msg, T data){
		
		Result<T> result = new Result<>();
		result.setStatus(status);
		result.setMsg(msg);
		result.setData(data);
		return result;
	}
}
