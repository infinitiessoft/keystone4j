/*******************************************************************************
 * # Copyright 2015 InfinitiesSoft Solutions Inc.
 * #
 * # Licensed under the Apache License, Version 2.0 (the "License"); you may
 * # not use this file except in compliance with the License. You may obtain
 * # a copy of the License at
 * #
 * #      http://www.apache.org/licenses/LICENSE-2.0
 * #
 * # Unless required by applicable law or agreed to in writing, software
 * # distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * # WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * # License for the specific language governing permissions and limitations
 * # under the License.
 *******************************************************************************/
//package com.infinities.keystone4j.endpointfilter.api;
//
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//
//import com.infinities.keystone4j.endpointfilter.EndpointFilterApi;
//import com.infinities.keystone4j.endpointfilter.EndpointFilterDriver;
//import com.infinities.keystone4j.endpointfilter.api.command.AddEndpointToProjectCommand;
//import com.infinities.keystone4j.endpointfilter.api.command.CheckEndpointInProjectCommand;
//import com.infinities.keystone4j.endpointfilter.api.command.ListEndpointsForProjectCommand;
//import com.infinities.keystone4j.endpointfilter.api.command.ListProjectsForEndpointCommand;
//import com.infinities.keystone4j.endpointfilter.api.command.RemoveEndpointFromProjectCommand;
//import com.infinities.keystone4j.extension.ExtensionApi;
//import com.infinities.keystone4j.extension.model.Extension;
//import com.infinities.keystone4j.model.assignment.Project;
//import com.infinities.keystone4j.model.catalog.Endpoint;
//import com.infinities.keystone4j.model.common.Link;
//
//public class EndpointFilterApiImpl implements EndpointFilterApi {
//
//	private final EndpointFilterDriver endpointFilterDriver;
//
//
//	public EndpointFilterApiImpl(EndpointFilterDriver endpointFilterDriver, ExtensionApi extensionApi) {
//		super();
//		this.endpointFilterDriver = endpointFilterDriver;
//		Extension extension = new Extension();
//		extension.setName("Openstack keystone Endpoint Filter API");
//		extension.setNamespace("http://docs.openstack.org/identity/api/ext/OS-EP-FILTER/v1.0");
//		extension.setAlias("OS-EP-FILTER");
//		Calendar calendar = Calendar.getInstance();
//		calendar.set(2013, 7, 23, 12, 0);
//		Date updated = calendar.getTime();
//		extension.setUpdated(updated);
//		extension.setDescription("Openstack keystone Endpoint Filter API.");
//		Link link = new Link();
//		link.setRel("describeby");
//		link.setType("text/html");
//		link.setHref("https://github.com/openstack/identity-api/blob/master/openstack-identity-api/v3/src/markdown/identity-api-v3-os-ep-filter-ext.md");
//		extension.getLinks().add(link);
//		extensionApi.registerExtension(extension.getAlias(), extension);
//	}
//
//	@Override
//	public void addEndpointToProject(String endpointid, String projectid) {
//		AddEndpointToProjectCommand command = new AddEndpointToProjectCommand(endpointFilterDriver, endpointid, projectid);
//		command.execute();
//	}
//
//	@Override
//	public void checkEndpointInProject(String endpointid, String projectid) {
//		CheckEndpointInProjectCommand command = new CheckEndpointInProjectCommand(endpointFilterDriver, endpointid,
//				projectid);
//		command.execute();
//	}
//
//	@Override
//	public void removeEndpointFromProject(String endpointid, String projectid) {
//		RemoveEndpointFromProjectCommand command = new RemoveEndpointFromProjectCommand(endpointFilterDriver, endpointid,
//				projectid);
//		command.execute();
//	}
//
//	@Override
//	public List<Endpoint> listEndpointsForProject(String projectid) {
//		ListEndpointsForProjectCommand command = new ListEndpointsForProjectCommand(endpointFilterDriver, projectid);
//		return command.execute();
//	}
//
//	@Override
//	public List<Project> listProjectsForEndpoint(String endpointid) {
//		ListProjectsForEndpointCommand command = new ListProjectsForEndpointCommand(endpointFilterDriver, endpointid);
//		return command.execute();
//	}
//
// }
