package com.infinities.keystone4j.utils;

import com.google.common.base.Strings;

public class TextUtils {

	private TextUtils() {

	}

	public static String rstrip(String text, String strip) {
		String rstrip = text;
		if (text.endsWith(strip)) {
			rstrip = text.substring(0, text.lastIndexOf(strip));
		}
		return rstrip;
	}

	public static String get(String target, String defaultVal) {
		if (Strings.isNullOrEmpty(target)) {
			return defaultVal;
		}
		return target;
	}
}
