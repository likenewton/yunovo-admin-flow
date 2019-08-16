package cn.yunovo.iov.fc.common.utils.log;

public enum OpTypeEnum {

	/**
	 * 创建
	 */
	CREATE("CREATE"),
	/**
	 * 查询
	 */
	QUERY("QUERY"),
	/**
	 * 更新
	 */
	UPDATE("UPDATE"),
	/**
	 * 删除
	 */
	DELETE("DELETE"),
	/**
	 * 其他
	 */
	OTHER("OTHER"),
	/**
	 * 下载
	 */
	DOWNLOAD("DOWNLOAD"),
	/**
	 * 登录
	 */
	LOGIN("LOGIN"),
	/**
	 * 登出
	 */
	LOGOUT("LOGOUT");
	
	private String value;
	
	private OpTypeEnum(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
}
