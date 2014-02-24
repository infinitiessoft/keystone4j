package com.infinities.keystone4j.endpointfilter.command;

import com.infinities.keystone4j.catalog.model.Endpoint;
import com.infinities.keystone4j.endpointfilter.EndpointFilterDriver;

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
