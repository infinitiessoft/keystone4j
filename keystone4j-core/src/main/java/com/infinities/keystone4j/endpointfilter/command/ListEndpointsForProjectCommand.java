package com.infinities.keystone4j.endpointfilter.command;

import java.util.List;

import com.infinities.keystone4j.catalog.model.Endpoint;
import com.infinities.keystone4j.endpointfilter.EndpointFilterDriver;

public class ListEndpointsForProjectCommand extends AbstractEndpointFilterCommand<List<Endpoint>> {

	private final String projectid;


	public ListEndpointsForProjectCommand(EndpointFilterDriver endpointFilterDriver, String projectid) {
		super(endpointFilterDriver);
		this.projectid = projectid;
	}

	@Override
	public List<Endpoint> execute() {
		return this.getEndpointFilterDriver().listEndpointsForProject(projectid);
	}

}
