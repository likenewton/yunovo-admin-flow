package cn.yunovo.iov.fc.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

public final class DateUtil {

	/**
	 * 获取某一天是当月的第几天
	 */
	public  static Integer getDayOfMonth(Date date) {

		Date _date = new Date();
		if(date == null) {
			
			_date = new Date();
		}else {
			_date = date;
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(_date);
		
		return calendar.get(Calendar.DAY_OF_MONTH);
	}
	
	public static Date now() {
		return new Date();
	}
	
	public static String nowStr() {
		return DateFormatUtils.format(now(), "yyyy-MM-dd HH:mm:ss");
	}
	
	
	/**
	 * 两个日期进行比较
	 * @param d1 日期1
	 * @param d2 日期2
	 * @return if d1 > d2 then return 1 else if d1 == d2 then return 0 else return -1
	 */
	public static int compare(Date d1, Date d2) {
		
		Long time1 = d1.getTime();
		Long time2 = d2.getTime();
		
		return time1 == time2 ? 0 : (time1 > time2 ? 1 : -1);
	}
	
	
	public static Date parseDate(String dateStr, String pattern) {
		
		if(dateStr == null || dateStr.isEmpty()) {
			return null;
		}
		
		if(pattern == null || pattern.isEmpty()) {
			return null;
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
		
	}
}
