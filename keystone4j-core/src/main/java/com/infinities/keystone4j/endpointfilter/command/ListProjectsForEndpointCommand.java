package com.infinities.keystone4j.endpointfilter.command;

import java.util.List;

import com.infinities.keystone4j.assignment.model.Project;
import com.infinities.keystone4j.endpointfilter.EndpointFilterDriver;

public class ListProjectsForEndpointCommand extends AbstractEndpointFilterCommand<List<Project>> {

	private final String endpointid;


	public ListProjectsForEndpointCommand(EndpointFilterDriver endpointFilterDriver, String endpointid) {
		super(endpointFilterDriver);
		this.endpointid = endpointid;
	}

	@Override
	public List<Project> execute() {
		return this.getEndpointFilterDriver().listProjectsForEndpoint(endpointid);
	}

}
