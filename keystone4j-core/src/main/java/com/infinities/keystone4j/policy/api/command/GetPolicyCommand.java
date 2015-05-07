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

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.model.policy.Policy;
import com.infinities.keystone4j.policy.PolicyDriver;

public class GetPolicyCommand extends AbstractPolicyCommand implements NonTruncatedCommand<Policy> {

	private final String policyid;


	public GetPolicyCommand(PolicyDriver policyDriver, String policyid) {
		super(policyDriver);
		this.policyid = policyid;
	}

	@Override
	public Policy execute() {
		try {
			return this.getPolicyDriver().getPolicy(policyid);
		} catch (Exception e) {
			throw Exceptions.PolicyNotFoundException.getInstance(null, policyid);
		}
	}

}
