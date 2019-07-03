package cn.yunovo.iov.fc.common.utils.math;

import java.math.BigDecimal;

public class MathUtils {

	public static Float round(float data, Integer scale) {
		BigDecimal b = new BigDecimal(data);
		b = b.setScale(scale, BigDecimal.ROUND_HALF_UP);
		return b.floatValue();
	}
	
	public static Double round(double data, Integer scale) {
		BigDecimal b = new BigDecimal(data);
		b = b.setScale(scale, BigDecimal.ROUND_HALF_UP);
		return b.doubleValue();
	}
}
