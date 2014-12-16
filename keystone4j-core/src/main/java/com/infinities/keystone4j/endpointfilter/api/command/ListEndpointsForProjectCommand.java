//package com.infinities.keystone4j.endpointfilter.api.command;
//
//import java.util.List;
//
//import com.infinities.keystone4j.endpointfilter.EndpointFilterDriver;
//import com.infinities.keystone4j.model.catalog.Endpoint;
//
//public class ListEndpointsForProjectCommand extends AbstractEndpointFilterCommand<List<Endpoint>> {
//
//	private final String projectid;
//
//
//	public ListEndpointsForProjectCommand(EndpointFilterDriver endpointFilterDriver, String projectid) {
//		super(endpointFilterDriver);
//		this.projectid = projectid;
//	}
//
//	@Override
//	public List<Endpoint> execute() {
//		return this.getEndpointFilterDriver().listEndpointsForProject(projectid);
//	}
//
// }
