package cn.yunovo.iov.fc.service;

import cn.yunovo.iov.fc.common.utils.Md5Util;

public class FcConstant {

	public final static String CACHE_RESOURCE_PREFIX = "GPRS-MEM-RES-";
	public final static String CACHE_SQL_PREFIX = "GPRS-MEM-SQL-";
	
	public static String memResKey(String key) {
		
		return CACHE_RESOURCE_PREFIX + key;
	}
	
	public static String memSqlKey(String sql) {
		
		return CACHE_SQL_PREFIX + Md5Util.getMD5String(sql);
	}
	
}
