package cn.yunovo.iov.fc.common.utils;

public class ResultUtil {
	
	public static final int SUCCESS = 0;
	
	public static <T> Result<T> success(String msg, T data){
		return Result.build(SUCCESS, msg, data);
	}
	
	public static <T> Result<T> success(T data){
		return Result.build(SUCCESS, "ok", data);
	}
	
	public static <T> Result<T> build(Integer status, String msg, T data) {
		return Result.build(status, msg, data);
	}
	
	public static <T> Result<T> build(Integer status, String msg) {
		return Result.build(status, msg);
	}
	
	public static <T> Result<T> exception() {
		return Result.build(500,"操作失败");
	}
}
