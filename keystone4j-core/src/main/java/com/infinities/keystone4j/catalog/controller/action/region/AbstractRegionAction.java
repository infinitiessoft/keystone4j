package com.infinities.keystone4j.catalog.controller.action.region;

import com.infinities.keystone4j.AbstractAction;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.catalog.Region;
import com.infinities.keystone4j.model.catalog.wrapper.RegionWrapper;
import com.infinities.keystone4j.model.catalog.wrapper.RegionsWrapper;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public abstract class AbstractRegionAction extends AbstractAction<Region> {

	protected CatalogApi catalogApi;


	public AbstractRegionAction(CatalogApi catalogApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi) {
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
	public CollectionWrapper<Region> getCollectionWrapper() {
		return new RegionsWrapper();
	}

	@Override
	public MemberWrapper<Region> getMemberWrapper() {
		return new RegionWrapper();
	}

	@Override
	public String getCollectionName() {
		return "regions";
	}

	@Override
	public String getMemberName() {
		return "region";
	}

}
