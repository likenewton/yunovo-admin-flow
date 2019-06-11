package cn.yunovo.iov.fc.model.exception;

public class FormValidateException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer code = 400;
	
	private String params;
	
	private String filed;
	
	public FormValidateException(String message, String filed, String params) {
		
		super(message);
		this.filed = filed;
		this.params = params;
	}

	public Integer getCode() {
		return code;
	}

	public String getParams() {
		return params;
	}

	public String getFiled() {
		return filed;
	}
	
}
