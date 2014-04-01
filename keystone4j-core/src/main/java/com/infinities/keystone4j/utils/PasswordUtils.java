package com.infinities.keystone4j.utils;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.hash.Hashing;
import com.infinities.keystone4j.common.Config;

public class PasswordUtils {

	private final static Logger logger = LoggerFactory.getLogger(PasswordUtils.class);
	private final static String MAX_PASSWORD_LENGTH = "max_password_length";
	private final static String TRUNCATE_USER_PASSWORD = "Truncating user password to {} characters.";
	private final static String ENCODE_FAILED = "Encode failed";
	private final static String UTF8 = "UTF-8";


	private PasswordUtils() {

	}

	public static void main(String args[]) {
		String p = hashPassword("demo");
		System.out.println(p);
		System.out.println(p.length());

	}

	public static boolean checkPassword(String password, String hashed) {
		if (Strings.isNullOrEmpty(password) || Strings.isNullOrEmpty(hashed)) {
			return false;
		}

		String passwordUtf8 = null;
		try {
			passwordUtf8 = new String(truncPassword(password).getBytes(UTF8), UTF8);
		} catch (UnsupportedEncodingException e) {
			logger.error(ENCODE_FAILED, e);
			return false;
		}
		String passwordSha512 = Hashing.sha512().hashString(passwordUtf8, Charsets.UTF_8).toString();
		return passwordSha512.equals(hashed);
	}

	public static String hashPassword(String password) {
		if (Strings.isNullOrEmpty(password)) {
			throw new IllegalArgumentException();
		}
		String passwordUtf8 = truncPassword(password);

		// hash round? and identify
		String hashed = Hashing.sha512().hashString(passwordUtf8, Charsets.UTF_8).toString();
		return hashed;
	}

	private static String truncPassword(String password) {
		int length = Config.Instance.getOpt(Config.Type.identity, MAX_PASSWORD_LENGTH).asInteger();

		if (password.length() > length) {
			logger.warn(TRUNCATE_USER_PASSWORD, length);
		} else {
			length = password.length();
		}

		return password.substring(0, length);
	}
}
