package com.infinities.keystone4j.endpointfilter.controller.action;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.endpointfilter.EndpointFilterApi;

public abstract class AbstractEndpointFilterAction<T> implements ProtectedAction<T> {

	protected AssignmentApi assignmentApi;
	protected CatalogApi catalogApi;
	protected EndpointFilterApi endpointFilterApi;


	public AbstractEndpointFilterAction(AssignmentApi assignmentApi, CatalogApi catalogApi,
			EndpointFilterApi endpointFilterApi) {
		super();
		this.assignmentApi = assignmentApi;
		this.catalogApi = catalogApi;
		this.endpointFilterApi = endpointFilterApi;
	}

	public AssignmentApi getAssignmentApi() {
		return assignmentApi;
	}

	public void setAssignmentApi(AssignmentApi assignmentApi) {
		this.assignmentApi = assignmentApi;
	}

	public CatalogApi getCatalogApi() {
		return catalogApi;
	}

	public void setCatalogApi(CatalogApi catalogApi) {
		this.catalogApi = catalogApi;
	}

	public EndpointFilterApi getEndpointFilterApi() {
		return endpointFilterApi;
	}

	public void setEndpointFilterApi(EndpointFilterApi endpointFilterApi) {
		this.endpointFilterApi = endpointFilterApi;
	}

}
