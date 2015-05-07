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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.model.policy.Context;
import com.infinities.keystone4j.policy.Enforcer;

public class RuleCheck extends Check {

	private final static Logger logger = LoggerFactory.getLogger(RuleCheck.class);


	@Override
	public String getRule() {
		return "rule";
	}

	@Override
	public boolean check(Map<String, Object> target, Context creds, Enforcer enforcer) {
		try {
			return enforcer.getRules().get(this.getMatch()).check(target, creds, enforcer);
		} catch (Exception e) {
			logger.warn("invalid match", e);
			return false;
		}
	}

	@Override
	public Check newInstance(String kind, String match) {
		logger.debug("new rule check: {}, {}", new Object[] { kind, match });
		Check check = new RuleCheck();
		check.setKind(kind);
		check.setMatch(match);
		return check;
	}
}
