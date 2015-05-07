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
package com.infinities.keystone4j.trust.api.command;

import java.util.List;

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.model.trust.Trust;
import com.infinities.keystone4j.trust.TrustDriver;

public class ListTrustsForTrusteeCommand extends AbstractTrustCommand implements NonTruncatedCommand<List<Trust>> {

	private final String trusteeid;


	public ListTrustsForTrusteeCommand(TrustDriver trustDriver, String trusteeid) {
		super(trustDriver);
		this.trusteeid = trusteeid;
	}

	@Override
	public List<Trust> execute() {
		return this.getTrustDriver().listTrustsForTrustee(trusteeid);
	}

}
