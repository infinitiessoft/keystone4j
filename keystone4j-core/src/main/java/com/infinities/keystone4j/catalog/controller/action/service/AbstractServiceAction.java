package com.infinities.keystone4j.catalog.controller.action.service;

import com.infinities.keystone4j.AbstractAction;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.catalog.Service;
import com.infinities.keystone4j.model.catalog.ServiceWrapper;
import com.infinities.keystone4j.model.catalog.ServicesWrapper;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public abstract class AbstractServiceAction extends AbstractAction<Service> {

	protected CatalogApi catalogApi;


	public AbstractServiceAction(CatalogApi catalogApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi) {
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
	protected CollectionWrapper<Service> getCollectionWrapper() {
		return new ServicesWrapper();
	}

	@Override
	protected MemberWrapper<Service> getMemberWrapper() {
		return new ServiceWrapper();
	}

	@Override
	public String getCollectionName() {
		return "services";
	}

	@Override
	public String getMemberName() {
		return "service";
	}

}
