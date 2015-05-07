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
package com.infinities.keystone4j.catalog.controller.action.endpoint;

import com.infinities.keystone4j.AbstractAction;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.catalog.Endpoint;
import com.infinities.keystone4j.model.catalog.wrapper.EndpointWrapper;
import com.infinities.keystone4j.model.catalog.wrapper.EndpointsWrapper;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public abstract class AbstractEndpointAction extends AbstractAction<Endpoint> {

	protected CatalogApi catalogApi;


	public AbstractEndpointAction(CatalogApi catalogApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi) {
		super(tokenProviderApi, policyApi);
		this.catalogApi = catalogApi;
	}

	public CatalogApi getCatalogApi() {
		return catalogApi;
	}

	public void setCatalogApi(CatalogApi catalogApi) {
		this.catalogApi = catalogApi;
	}

	@Override
	public CollectionWrapper<Endpoint> getCollectionWrapper() {
		return new EndpointsWrapper();
	}

	@Override
	public MemberWrapper<Endpoint> getMemberWrapper() {
		return new EndpointWrapper();
	}

	@Override
	public String getCollectionName() {
		return "endpoints";
	}

	@Override
	public String getMemberName() {
		return "endpoint";
	}

	protected Endpoint validateEndpointRegion(Endpoint endpoint) throws Exception {
		if (endpoint.getRegion() != null) {
			try {
				catalogApi.getRegion(endpoint.getRegionid());
			} catch (Exception e) {
				catalogApi.createRegion(endpoint.getRegion());
			}
		}

		return endpoint;
	}

}
