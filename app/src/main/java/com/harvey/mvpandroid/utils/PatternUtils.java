package com.harvey.mvpandroid.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternUtils {

	/**
	 * 手机号格式正则校验
	 *
	 * @param phone
	 *            手机号
	 * @return
	 */
	public static boolean matchPhone(String phone) {
		if (TextUtils.isEmpty(phone))
			return false;
		if (phone.startsWith("20") || phone.startsWith("21")) {
			return true;
		}
		Pattern patternPhone = Pattern.compile("^((13[0-9])|(147)|(177)|(15[^4,\\D])|(18[0-9]))\\d{8}$");
		Matcher matcherPhone = patternPhone.matcher(phone);
		return matcherPhone.matches();
	}

	/**
	 * 邮箱格式正则校验
	 *
	 * @param email
	 *            邮箱
	 * @return
	 */
	public static boolean matchEmail(String email) {
		String strPattern = "^[a-z0-9A-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(email);
		return m.matches();
		// String check =
		// "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		// Pattern patternEmail = Pattern.compile(check);
		// Matcher matcherEmail = patternEmail.matcher(email);
		// return matcherEmail.matches();
	}

	/**
	 * 身份证校验
	 *
	 * @param idCard
	 *            身份证号
	 * @return
	 */
	public static boolean matchIdCard(String idCard) {
		Pattern patternIdCard1 = Pattern.compile("[0-9]{17}x");
		Matcher matcherIdCard1 = patternIdCard1.matcher(idCard);

		Pattern patternIdCard2 = Pattern.compile("[0-9]{15}");
		Matcher matcherIdCard2 = patternIdCard2.matcher(idCard);

		Pattern patternIdCard3 = Pattern.compile("[0-9]{18}");
		Matcher matcherIdCard3 = patternIdCard3.matcher(idCard);

		return matcherIdCard1.matches() || matcherIdCard2.matches() || matcherIdCard3.matches();
	}

	/**
	 * 军官证号码校验 校验军官证号码格式是否正确
	 *
	 * @param officerCard
	 *            军官证号码
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static boolean matchOfficerCard(String officerCard) {
		Pattern patternOfficerCard = Pattern.compile("[0-9]{8}");
		Matcher matcherOfficerCard = patternOfficerCard.matcher(officerCard);

		return matcherOfficerCard.matches();
	}

	/**
	 * 生日和身份证号码校验 <功能详细描述>
	 *
	 * @param birth
	 * @param idCard
	 * @return 生日和身份证信息号码校验是否正确
	 * @see [类、类#方法、类#成员]
	 */
	public static boolean matchBirthAndIdCard(String birth, String idCard) {
		if (idCard.length() == 15) {
			String newBirth1 = birth.substring(2);
			String newBirth2 = idCard.substring(6, 12);
			if (newBirth1.equals(newBirth2)) {
				return true;
			}
		}
		if (idCard.length() == 18) {
			String newBirth = idCard.substring(6, 14);
			if (birth.equals(newBirth)) {
				return true;
			}
		}
		return false;
	}

}
