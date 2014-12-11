package com.infinities.keystone4j.endpointfilter.api.command;

import com.infinities.keystone4j.endpointfilter.EndpointFilterDriver;
import com.infinities.keystone4j.model.catalog.Endpoint;

public class CheckEndpointInProjectCommand extends AbstractEndpointFilterCommand<Endpoint> {

	private final String endpointid;
	private final String projectid;


	public CheckEndpointInProjectCommand(EndpointFilterDriver endpointFilterDriver, String endpointid, String projectid) {
		super(endpointFilterDriver);
		this.endpointid = endpointid;
		this.projectid = projectid;
	}

	@Override
	public Endpoint execute() {
		this.getEndpointFilterDriver().checkEndpointToProject(endpointid, projectid);
		return null;
	}

}
