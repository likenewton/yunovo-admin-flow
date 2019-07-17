package cn.yunovo.iov.fc.common.utils.math;

import java.math.BigDecimal;

public class MathUtils {

	/**
	 * 四舍五入
	 * @param data
	 * @param scale
	 * @return
	 */
	public static Float round(float data, Integer scale) {
		
		BigDecimal b = new BigDecimal(data);
		b = b.setScale(scale, BigDecimal.ROUND_HALF_UP);
		return b.floatValue();
	}
	
	/**
	 * 四舍五入
	 * @param data
	 * @param scale
	 * @return
	 */
	public static Double round(double data, Integer scale) {
		
		BigDecimal b = new BigDecimal(data);
		b = b.setScale(scale, BigDecimal.ROUND_HALF_UP);
		return b.doubleValue();
	}
	
	/**
	 * @param data
	 * @param scale
	 * @param round_model  BigDecimal.ROUND_HALF_UP 、 ROUND_DOWN ...
	 * @return
	 */
	public static Double round(double data, Integer scale, Integer round_model) {
		
		BigDecimal b = new BigDecimal(data);
		b = b.setScale(scale, round_model);
		return b.doubleValue();
	}
	
	
	/**
	 * @param data
	 * @param scale
	 * @param round_model  BigDecimal.ROUND_HALF_UP 、 ROUND_DOWN ...
	 * @return
	 */
	public static Float round(float data, Integer scale, Integer round_model) {
		
		BigDecimal b = new BigDecimal(data);
		b = b.setScale(scale, round_model);
		return b.floatValue();
	}
	
}
