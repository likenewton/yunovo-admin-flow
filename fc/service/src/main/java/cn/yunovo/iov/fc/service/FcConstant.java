package cn.yunovo.iov.fc.service;


import org.apache.commons.lang3.StringUtils;

import cn.yunovo.iov.fc.common.utils.Md5Util;

public class FcConstant {
	
	public final static String DB_GET_ROW = "fetch_row";//获取一条数据
	public final static String DB_GET_ONE = "fetch_one";//获取一个数据
	public final static String DB_GET_ALL = "fetch_all"; //获取多条数据
	public final static String DB_GET_PAIRS = "fetch_pairs";//获取一对数据
	
	public final static String SITE_CODE = "FLOW_CLOUD_PLATFORM";

	public final static String CACHE_RESOURCE_PREFIX = "GPRS-MEM-RES-";
	public final static String CACHE_SQL_PREFIX_NEW = "GPRS-MEM-SQL-";
	public final static String CACHE_SQL_PREFIX = "GPRS-MEM-SQL-";
	
	
	public final static String CARD_DAY_USE_CACHEKEY = "CARD-DAYUSE#%s#%s#%s";
	
	public final static String CARD_MONTH_USE_CACHEKEY = "CARD-MONTHUSE#%s#%s#%s";
	
	public final static String CARD_MONTH_WLIST_TOTAL_CACHEKEY = "CARD#MonthWlistTotal#%s";
	
	public final static String CARD_LOCK_CACHEKEY = "CARD-LOCK-%s";
	
	/**
	 * 是否月结 
	 */
	public final static String MONTH_END_TYPE_CACHEKEY = "MONTH_END_TYPE";
	
	/**
	 * 前一日激活
	 */
	public final static String YESTERDAY_ACTIVE_CACHEKEY = "YESTERDAY_ACTIVE";
	
	public final static String CARD_INFO_CACHEKEY = "CARD-INFO-%s";
	
	public final static String SQL_QUEUE_CACHEKEY = "GPRS-MEM-SQL-SQL-QUEUE";
	public final static String PAY_QUEUE_CACHEKEY = "GPRS-MEM-RES-PAY-QUEUE";
	public final static String RES_QUEUE_CACHEKEY = "GPRS-MEM-RES-RES-QUEUE";
	
	
	public static String memResKey(String key) {
		
		return CACHE_RESOURCE_PREFIX + key;
	}
	
	/*public static String memSqlKey(String sql, String dtype) {
		
		return CACHE_SQL_PREFIX + Md5Util.getMD5String(sql);
	}*/
	
	public static String memSqlNewKey(String sql, String dtype) {
		
		return CACHE_SQL_PREFIX_NEW + Md5Util.getMD5String(sql+dtype);
	}
	
	public static String cardInfoKey(String iccid) {
		
		return memResKey(String.format(CARD_INFO_CACHEKEY, iccid));
	}

	public static String memSqlKey(String sql, String dtype) {
		dtype =  StringUtils.defaultString(dtype, "fetch_row");
		return CACHE_SQL_PREFIX + Md5Util.getMD5String(sql+dtype);
	}
}
