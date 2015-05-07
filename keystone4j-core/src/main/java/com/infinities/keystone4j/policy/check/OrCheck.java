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

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.infinities.keystone4j.model.policy.Context;
import com.infinities.keystone4j.policy.Enforcer;

public class OrCheck implements BaseCheck {

	private final List<BaseCheck> rules;
	private final Logger logger = LoggerFactory.getLogger(OrCheck.class);


	public OrCheck(List<BaseCheck> rules) {
		this.rules = rules;
	}

	@Override
	public String getRule() {
		return "or";
	}

	@Override
	public boolean check(Map<String, Object> target, Context creds, Enforcer enforcer) {
		logger.debug("rules size: {}", rules.size());
		for (BaseCheck rule : rules) {
			logger.debug("rules: {}", rule.getRule());
			if (rule.check(target, creds, enforcer)) {
				return true;
			}
		}
		return false;
	}

	public void addCheck(BaseCheck check) {
		this.rules.add(check);
	}

	@Override
	public String toString() {
		String join = Joiner.on(" or ").join(rules);
		return String.format("(%s)", join);
	}
}
