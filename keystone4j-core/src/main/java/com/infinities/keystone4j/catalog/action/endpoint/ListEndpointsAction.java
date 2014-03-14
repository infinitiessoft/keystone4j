package com.infinities.keystone4j.catalog.action.endpoint;

import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.catalog.model.Endpoint;

public class ListEndpointsAction extends AbstractEndpointAction<List<Endpoint>> {

	private final String interfaceType;
	private final String serviceid;


	public ListEndpointsAction(CatalogApi catalogApi, String interfaceType, String serviceid) {
		super(catalogApi);
		this.interfaceType = interfaceType;
		this.serviceid = serviceid;
	}

	@Override
	public List<Endpoint> execute(ContainerRequestContext request) {
		Iterable<Endpoint> endpoints = this.getCatalogApi().listEndpoints();

		List<Predicate<Endpoint>> filters = Lists.newArrayList();

		if (!Strings.isNullOrEmpty(interfaceType)) {
			Predicate<Endpoint> filter = new Predicate<Endpoint>() {

				@Override
				public boolean apply(Endpoint e) {
					return interfaceType.equals(e.getInterfaceType());
				}
			};
			filters.add(filter);
		}

		if (!Strings.isNullOrEmpty(serviceid)) {
			Predicate<Endpoint> filter = new Predicate<Endpoint>() {

				@Override
				public boolean apply(Endpoint e) {
					return e.getService() != null && serviceid.equals(e.getService().getId());
				}
			};
			filters.add(filter);
		}

		if (filters.size() > 0) {
			Predicate<Endpoint> filter = Predicates.and(filters);

			endpoints = Iterables.filter(endpoints, filter);
		}

		return Lists.newArrayList(endpoints);
	}

	@Override
	public String getName() {
		return "list_endpoints";
	}
}
