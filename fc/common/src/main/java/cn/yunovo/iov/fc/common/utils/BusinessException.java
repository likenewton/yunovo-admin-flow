package cn.yunovo.iov.fc.common.utils;

/**
 * 业务异常处理类
 * 
 * @author bill
 *
 */
public class BusinessException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 异常代码
	 */
	private Integer exception_code;
	
	public BusinessException(Integer exception_code, String message) {
		super(message);
		this.exception_code = exception_code;
	}
	
	public BusinessException(String message) {
		
		super(message);
		this.exception_code = -1;
	}

	public Integer getException_code() {
		return exception_code;
	}

	public void setException_code(Integer exception_code) {
		this.exception_code = exception_code;
	}
	
	
}
