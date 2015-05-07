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
package com.infinities.keystone4j.policy.check;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.model.policy.Context;
import com.infinities.keystone4j.policy.Enforcer;
import com.infinities.keystone4j.utils.ReflectUtils;

public class GenericCheck extends Check {

	private final static Logger logger = LoggerFactory.getLogger(GenericCheck.class);


	@Override
	public String getRule() {
		return "none";
	}

	@Override
	public boolean check(Map<String, Object> target, Context creds, Enforcer enforcer) {
		// try {
		logger.debug("match: {}, kind:{}", new Object[] { this.getMatch(), this.getKind() });
		String match = this.getMatch();
		Object expect = null;
		Pattern pattern = Pattern.compile("%\\((.+?)\\)s");
		Matcher matcher = pattern.matcher(match);
		if (matcher.matches()) {
			match = matcher.group(1);
		}

		String[] split = match.split("\\.");
		logger.debug("match: {}, split size: {}", new Object[] { match, split.length });

		try {
			expect = ReflectUtils.reflact(target, match);
		} catch (Exception e) {
			logger.warn("reflect value from tartget failed", e);
			expect = match;
		}

		if (expect == null) {
			logger.warn("unreadable policy");
			return false;
		}

		split = this.getKind().split("\\.");
		try {
			logger.debug("split size: {}", split.length);
			Object leftval = ReflectUtils.reflact(creds, this.getKind());
			logger.debug("expect: {}, actual: {}", new Object[] { expect, leftval });

			return expect.equals(leftval);
		} catch (Exception e) {
			logger.warn("reflect value from credentials failed", e);
			return false;
		}
	}

	@Override
	public Check newInstance(String kind, String match) {
		Check check = new GenericCheck();
		check.setKind(kind);
		check.setMatch(match);
		return check;
	}
}
