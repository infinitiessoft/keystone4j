package com.infinities.keystone4j.catalog.controller.action.endpoint;

import com.infinities.keystone4j.AbstractAction;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.catalog.Endpoint;
import com.infinities.keystone4j.model.catalog.EndpointWrapper;
import com.infinities.keystone4j.model.catalog.EndpointsWrapper;
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
	protected CollectionWrapper<Endpoint> getCollectionWrapper() {
		return new EndpointsWrapper();
	}

	@Override
	protected MemberWrapper<Endpoint> getMemberWrapper() {
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

	protected Endpoint validateEndpointRegion(Endpoint endpoint) {
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
