package com.infinities.keystone4j.catalog.controller.action.service;

import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.FilterProtectedAction;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.catalog.Service;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class ListServicesAction extends AbstractServiceAction implements FilterProtectedAction<Service> {

	public ListServicesAction(CatalogApi catalogApi, TokenProviderApi tokenProviderApi) {
		super(catalogApi, tokenProviderApi);
	}

	@Override
	public CollectionWrapper<Service> execute(ContainerRequestContext request, String... filters) throws Exception {
		Hints hints = buildDriverHints(request, filters);
		List<Service> services = this.getCatalogApi().listServices(hints);
		CollectionWrapper<Service> wrapper = wrapCollection(request, services, hints);
		return wrapper;
	}

	@Override
	public String getName() {
		return "list_services";
	}
}
