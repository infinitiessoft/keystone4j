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
package com.infinities.keystone4j.policy.api.command;

import java.util.Map;

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.model.policy.Context;
import com.infinities.keystone4j.model.policy.Policy;
import com.infinities.keystone4j.policy.PolicyDriver;

public class EnforceCommand extends AbstractPolicyCommand implements NonTruncatedCommand<Policy> {

	private final Context context;
	private final String action;
	private final Map<String, Object> target;


	public EnforceCommand(PolicyDriver policyDriver, Context context, String action, Map<String, Object> target) {
		super(policyDriver);
		this.context = context;
		this.action = action;
		this.target = target;
	}

	@Override
	public Policy execute() throws Exception {
		return this.getPolicyDriver().enforce(context, action, target);
	}
}
