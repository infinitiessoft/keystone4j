package com.infinities.keystone4j.catalog.action.service;

import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.model.catalog.Service;

public class ListServicesAction extends AbstractServiceAction<List<Service>> {

	private final String type;


	public ListServicesAction(CatalogApi catalogApi, String type) {
		super(catalogApi);
		this.type = type;
	}

	@Override
	public List<Service> execute(ContainerRequestContext request) {
		Iterable<Service> services = this.getCatalogApi().listServices();

		List<Predicate<Service>> filters = Lists.newArrayList();

		if (!Strings.isNullOrEmpty(type)) {
			Predicate<Service> filter = new Predicate<Service>() {

				@Override
				public boolean apply(Service s) {
					return type.equals(s.getType());
				}
			};
			filters.add(filter);
		}

		if (filters.size() > 0) {
			Predicate<Service> filter = Predicates.and(filters);
			services = Iterables.filter(services, filter);
		}

		return Lists.newArrayList(services);
	}

	@Override
	public String getName() {
		return "list_services";
	}
}
