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
package com.infinities.keystone4j.controller.action.decorator;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.ControllerAction;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

@Deprecated
public abstract class AbstractActionDecorator<T> extends ControllerAction implements ProtectedAction<T> {

	protected ProtectedAction<T> command;


	public AbstractActionDecorator(ProtectedAction<T> command, TokenProviderApi tokenProviderApi, PolicyApi policyApi) {
		super(tokenProviderApi, policyApi);
		this.command = command;
	}

	public ProtectedAction<T> getAction() {
		return command;
	}

	public void setAction(ProtectedAction<T> command) {
		this.command = command;
	}

}
