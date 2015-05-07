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

import com.google.common.base.Joiner;
import com.infinities.keystone4j.model.policy.Context;
import com.infinities.keystone4j.policy.Enforcer;

public class AndCheck implements BaseCheck {

	private final List<BaseCheck> rules;


	public AndCheck(List<BaseCheck> rules) {
		this.rules = rules;
	}

	@Override
	public String getRule() {
		return "and";
	}

	@Override
	public boolean check(Map<String, Object> target, Context creds, Enforcer enforcer) {

		for (BaseCheck rule : rules) {
			if (!rule.check(target, creds, enforcer)) {
				return false;
			}
		}
		return true;
	}

	public void addCheck(BaseCheck check) {
		this.rules.add(check);
	}

	@Override
	public String toString() {
		String join = Joiner.on(" and ").join(rules);
		return String.format("(%s)", join);
	}
}
