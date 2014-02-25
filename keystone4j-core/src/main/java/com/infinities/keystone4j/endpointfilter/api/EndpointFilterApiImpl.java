package com.infinities.keystone4j.endpointfilter.api;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.infinities.keystone4j.assignment.model.Project;
import com.infinities.keystone4j.catalog.model.Endpoint;
import com.infinities.keystone4j.common.model.Link;
import com.infinities.keystone4j.endpointfilter.EndpointFilterApi;
import com.infinities.keystone4j.endpointfilter.EndpointFilterDriver;
import com.infinities.keystone4j.endpointfilter.command.AddEndpointToProjectCommand;
import com.infinities.keystone4j.endpointfilter.command.CheckEndpointInProjectCommand;
import com.infinities.keystone4j.endpointfilter.command.ListEndpointsForProjectCommand;
import com.infinities.keystone4j.endpointfilter.command.ListProjectsForEndpointCommand;
import com.infinities.keystone4j.endpointfilter.command.RemoveEndpointFromProjectCommand;
import com.infinities.keystone4j.extension.ExtensionApi;
import com.infinities.keystone4j.extension.model.Extension;

public class EndpointFilterApiImpl implements EndpointFilterApi {

	private final EndpointFilterDriver endpointFilterDriver;


	public EndpointFilterApiImpl(EndpointFilterDriver endpointFilterDriver, ExtensionApi extensionApi) {
		super();
		this.endpointFilterDriver = endpointFilterDriver;
		Extension extension = new Extension();
		extension.setName("Openstack keystone Endpoint Filter API");
		extension.setNamespace("http://docs.openstack.org/identity/api/ext/OS-EP-FILTER/v1.0");
		extension.setAlias("OS-EP-FILTER");
		Calendar calendar = Calendar.getInstance();
		calendar.set(2013, 7, 23, 12, 0);
		Date updated = calendar.getTime();
		extension.setUpdated(updated);
		extension.setDescription("Openstack keystone Endpoint Filter API.");
		Link link = new Link();
		link.setRel("describeby");
		link.setType("text/html");
		link.setHref("https://github.com/openstack/identity-api/blob/master/openstack-identity-api/v3/src/markdown/identity-api-v3-os-ep-filter-ext.md");
		extension.getLinks().add(link);
		extensionApi.registerExtension(extension.getAlias(), extension);
	}

	@Override
	public void addEndpointToProject(String endpointid, String projectid) {
		AddEndpointToProjectCommand command = new AddEndpointToProjectCommand(endpointFilterDriver, endpointid, projectid);
		command.execute();
	}

	@Override
	public void checkEndpointInProject(String endpointid, String projectid) {
		CheckEndpointInProjectCommand command = new CheckEndpointInProjectCommand(endpointFilterDriver, endpointid,
				projectid);
		command.execute();
	}

	@Override
	public void removeEndpointFromProject(String endpointid, String projectid) {
		RemoveEndpointFromProjectCommand command = new RemoveEndpointFromProjectCommand(endpointFilterDriver, endpointid,
				projectid);
		command.execute();
	}

	@Override
	public List<Endpoint> listEndpointsForProject(String projectid) {
		ListEndpointsForProjectCommand command = new ListEndpointsForProjectCommand(endpointFilterDriver, projectid);
		return command.execute();
	}

	@Override
	public List<Project> listProjectsForEndpoint(String endpointid) {
		ListProjectsForEndpointCommand command = new ListProjectsForEndpointCommand(endpointFilterDriver, endpointid);
		return command.execute();
	}

}
