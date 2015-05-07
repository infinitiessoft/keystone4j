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
package com.infinities.keystone4j.catalog.controller.action.service;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.catalog.Service;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class CreateServiceAction extends AbstractServiceAction implements ProtectedAction<Service> {

	private final Service service;


	public CreateServiceAction(CatalogApi catalogApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi, Service service) {
		super(catalogApi, tokenProviderApi, policyApi);
		this.service = service;
	}

	@Override
	public MemberWrapper<Service> execute(ContainerRequestContext context) throws Exception {
		assignUniqueId(service);
		Service ref = catalogApi.createService(service.getId(), service);
		return this.wrapMember(context, ref);
	}

	@Override
	public String getName() {
		return "create_service";
	}
}
