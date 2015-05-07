/*******************************************************************************
 * # Copyright 2015 InfinitiesSoft Solutions Inc.
 * #
 * # Licensed under the Apache License, Version 2.0 (the "License"); you may
 * # not use this file except in compliance with the License. You may obtain
 * # a copy of the License at
 * #
 * #      http://www.apache.org/licenses/LICENSE-2.0
 * #
 * # Unless required by applicable law or agreed to in writing, software
 * # distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * # WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * # License for the specific language governing permissions and limitations
 * # under the License.
 *******************************************************************************/
package com.infinities.keystone4j.utils;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.hash.Hashing;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.model.identity.User;

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
		logger.debug("password: {}, hashed: {}", new Object[] { password, hashed });
		if (Strings.isNullOrEmpty(password) || Strings.isNullOrEmpty(hashed)) {
			return false;
		}

		String passwordUtf8 = null;
		try {
			passwordUtf8 = new String(verifyLengthAndtruncPassword(password).getBytes(UTF8), UTF8);
		} catch (UnsupportedEncodingException e) {
			logger.error(ENCODE_FAILED, e);
			return false;
		}
		String passwordSha512 = Hashing.sha512().hashString(passwordUtf8, Charsets.UTF_8).toString();
		logger.debug("password sha512: {}, hashed: {}", new Object[] { passwordSha512, hashed });
		return passwordSha512.equals(hashed);
	}

	public static User hashUserPassword(User user) {
		String password = user.getPassword();
		if (Strings.isNullOrEmpty(password)) {
			return user;
		}

		user.setPassword(hashPassword(password));
		return user;
	}

	public static String hashPassword(String password) {
		if (Strings.isNullOrEmpty(password)) {
			throw new IllegalArgumentException();
		}
		String passwordUtf8 = verifyLengthAndtruncPassword(password);

		// hash round? and identify
		String hashed = Hashing.sha512().hashString(passwordUtf8, Charsets.UTF_8).toString();
		return hashed;
	}

	private static String verifyLengthAndtruncPassword(String password) {
		int maxLength = Config.Instance.getOpt(Config.Type.identity, MAX_PASSWORD_LENGTH).asInteger();

		if (password.length() > maxLength) {
			if (Config.Instance.getOpt(Config.Type.DEFAULT, "strict_password_check").asBoolean()) {
				throw Exceptions.PasswordVerificationError.getInstance(null, maxLength);
			} else {
				logger.warn(TRUNCATE_USER_PASSWORD, maxLength);
				return password.substring(0, maxLength);
			}
		} else {
			return password;
		}
	}
}
