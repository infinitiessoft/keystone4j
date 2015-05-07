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

import com.infinities.keystone4j.model.policy.Policy;
import com.infinities.keystone4j.notification.NotifiableCommand;
import com.infinities.keystone4j.policy.PolicyDriver;

public class CreatePolicyCommand extends AbstractPolicyCommand implements NotifiableCommand<Policy> {

	private final String policyid;
	private final Policy policy;


	public CreatePolicyCommand(PolicyDriver policyDriver, String policyid, Policy policy) {
		super(policyDriver);
		this.policyid = policyid;
		this.policy = policy;
	}

	@Override
	public Policy execute() {
		return this.getPolicyDriver().createPolicy(policyid, policy);
	}

	@Override
	public Object getArgs(int index) {
		if (index == 1) {
			return policyid;
		} else if (index == 2) {
			return policy;
		}
		throw new IllegalArgumentException("invalid index");
	}

}
