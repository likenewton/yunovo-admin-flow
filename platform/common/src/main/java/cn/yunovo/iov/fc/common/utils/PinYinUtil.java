package cn.yunovo.iov.fc.common.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinYinUtil {

	/**
	 * @Title: 获取中文串拼音首字母，英文字符不变
	 * @methodName: getFirstSpell
	 * @param chinese
	 *            汉字串
	 * @param caseType 大小写 ,HanyuPinyinCaseType.LOWERCASE 返回小写,HanyuPinyinCaseType.UPPERCASE 返回大写
	 * @return java.lang.String 中文拼音首字母
	 * @Description:
	 *
	 * @author: bill
	 */
	public static String getFirstSpell(String chinese, HanyuPinyinCaseType caseType) {

		StringBuffer pybf = new StringBuffer();
		char[] arr = chinese.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(caseType);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > 128) {
				try {
					String[] temp = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
					if (temp != null) {
						pybf.append(temp[0].charAt(0));
					}
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				pybf.append(arr[i]);
			}
		}
		return pybf.toString().replaceAll("\\W", "").trim();
	}

	/**
	 * @Title: 获取中文串拼音，英文字符不变
	 * @methodName: getFullSpell
	 * @param chinese
	 *            中文字符串
	 * @param caseType 大小写 ,HanyuPinyinCaseType.LOWERCASE 返回小写,HanyuPinyinCaseType.UPPERCASE 返回大写
	 * @return java.lang.String 中文拼音
	 * @Description:
	 *
	 * @author: bill
	 */
	public static String getFullSpell(String chinese, HanyuPinyinCaseType caseType) {

		StringBuffer pybf = new StringBuffer();
		char[] arr = chinese.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(caseType);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > 128) {
				try {
					pybf.append(PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat)[0]);
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				pybf.append(arr[i]);
			}
		}
		return pybf.toString();
	}

	
	public static void main(String[] args) {
		System.out.println(getFirstSpell("陈继中",HanyuPinyinCaseType.UPPERCASE));
	}
}
