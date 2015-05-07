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
package com.infinities.keystone4j.admin.v3.project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.client.ClientProtocolException;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.infinities.keystone4j.PatchClient;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.intergrated.v3.AbstractIntegratedTest;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.assignment.wrapper.ProjectWrapper;
import com.infinities.keystone4j.model.identity.Group;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.utils.JsonUtils;
import com.infinities.keystone4j.utils.jackson.JacksonFeature;
import com.infinities.keystone4j.utils.jackson.ObjectMapperResolver;

public class ProjectResourceTest extends AbstractIntegratedTest {

	private Domain defaultDomain;
	private User user;
	private Group group;
	private Project project;
	private Role role1, role2;


	@Override
	protected Application configure() {
		enable(TestProperties.LOG_TRAFFIC);
		enable(TestProperties.DUMP_ENTITY);

		defaultDomain = new Domain();
		defaultDomain.setId("default");
		defaultDomain.setDescription("desc of default Domain");
		defaultDomain.setName("my default domain");

		project = new Project();
		project.setId("88e550a135bb4e6da68e79e5b7c4b2f2");
		project.setDomain(defaultDomain);
		project.setName("admin");
		project.setDescription("description");

		user = new User();
		user.setId("e7912c2225e84ac5905d8cf0b5040a6d");

		group = new Group();
		group.setId("88e550a135bb4e6da68e79e5b7c4b2f3");
		group.setName("demo");
		group.setDomain(defaultDomain);

		role1 = new Role();
		role1.setId("9fe2ff9ee4384b1894a90878d3e92bab");
		role1.setName("_member_");
		role1.setDescription("Default role for project membership");

		role2 = new Role();
		role2.setId("d903936e7bbd4183b8cd35816d2cf88b");

		return new ProjectResourceTestApplication();
	}

	@Test
	public void testCreateProject() throws JsonGenerationException, JsonMappingException, IOException {
		Project project = new Project();
		project.setDescription("desc of Project");
		project.setDomain(defaultDomain);
		project.setName("my project");
		ProjectWrapper wrapper = new ProjectWrapper(project);
		String json = JsonUtils.toJson(wrapper);
		JsonNode node = JsonUtils.convertToJsonNode(json);
		JsonNode projectJ = node.get("project");
		assertEquals(project.getName(), projectJ.get("name").asText());
		assertEquals(project.getDescription(), projectJ.get("description").asText());
		assertEquals(project.getDomain().getId(), projectJ.get("domain_id").asText());
		System.err.println(json);
		Response response = target("/v3/projects").register(JacksonFeature.class).register(ObjectMapperResolver.class)
				.request().header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText())
				.post(Entity.entity(wrapper, MediaType.APPLICATION_JSON_TYPE));
		assertEquals(201, response.getStatus());
		String ret = response.readEntity(String.class);
		System.err.println(ret);
		node = JsonUtils.convertToJsonNode(ret);
		projectJ = node.get("project");
		assertNotNull(projectJ.get("id").asText());
		assertEquals(project.getName(), projectJ.get("name").asText());
		assertEquals(project.getDescription(), projectJ.get("description").asText());
		assertEquals(project.getDomain().getId(), projectJ.get("domain_id").asText());
		assertNotNull(projectJ.get("enabled").asText());
		assertNotNull(projectJ.get("links"));
		assertNotNull(projectJ.get("links").get("self").asText());
	}

	@Test
	public void testListProject() throws JsonProcessingException, IOException {
		final List<Project> projects = new ArrayList<Project>();
		projects.add(project);

		Response response = target("/v3/projects").register(JacksonFeature.class).register(ObjectMapperResolver.class)
				.request().header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());
		String ret = response.readEntity(String.class);
		System.err.println(ret);
		JsonNode node = JsonUtils.convertToJsonNode(ret);
		JsonNode projectsJ = node.get("projects");
		assertEquals(1, projectsJ.size());
		JsonNode projectJ = projectsJ.get(0);
		assertEquals(project.getId(), projectJ.get("id").asText());
		assertEquals(project.getName(), projectJ.get("name").asText());
		assertEquals(project.getDescription(), projectJ.get("description").asText());
		assertEquals(project.getDomainId(), projectJ.get("domain_id").asText());
		assertNotNull(projectJ.get("enabled").asText());
		assertNotNull(projectJ.get("links"));
		assertNotNull(projectJ.get("links").get("self").asText());
	}

	@Test
	public void testGetProject() throws JsonProcessingException, IOException {
		Response response = target("/v3/projects").path(project.getId()).register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());
		String ret = response.readEntity(String.class);
		System.err.println(ret);
		JsonNode node = JsonUtils.convertToJsonNode(ret);
		JsonNode projectJ = node.get("project");
		assertEquals(project.getId(), projectJ.get("id").asText());
		assertEquals(project.getName(), projectJ.get("name").asText());
		assertEquals(project.getDomainId(), projectJ.get("domain_id").asText());
		assertNotNull(projectJ.get("enabled").asText());
		assertNotNull(projectJ.get("links"));
		assertNotNull(projectJ.get("links").get("self").asText());
	}

	@Test
	public void testUpdateProject() throws ClientProtocolException, IOException {
		project.setName("project1");
		project.setDescription("desc");
		project.setEnabled(false);

		ProjectWrapper wrapper = new ProjectWrapper(project);
		PatchClient client = new PatchClient("http://localhost:9998/v3/projects/" + project.getId());
		JsonNode node = client.connect(wrapper);
		System.err.println(node.toString());
		JsonNode projectJ = node.get("project");
		assertEquals(project.getId(), projectJ.get("id").asText());
		assertEquals(project.getName(), projectJ.get("name").asText());
		assertEquals(project.getDescription(), projectJ.get("description").asText());
		assertEquals(project.getDomainId(), projectJ.get("domain_id").asText());
		assertFalse(projectJ.get("enabled").asBoolean());
		assertNotNull(projectJ.get("links"));
		assertNotNull(projectJ.get("links").get("self").asText());
	}

	@Test
	public void testDeleteProject() {
		Response response = target("/v3").path("projects").path(project.getId()).register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).delete();
		assertEquals(204, response.getStatus());
	}

	@Test
	public void testListGrantByUser() throws JsonProcessingException, IOException {
		final List<Role> roles = new ArrayList<Role>();
		roles.add(role1);

		Response response = target("/v3/projects").path(project.getId()).path("users").path(user.getId()).path("roles")
				.register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());
		String ret = response.readEntity(String.class);
		System.err.println(ret);
		JsonNode node = JsonUtils.convertToJsonNode(ret);
		JsonNode roleList = node.get("roles");
		assertEquals(1, roleList.size());
		JsonNode roleJ = roleList.get(0);
		assertEquals(role1.getId(), roleJ.get("id").asText());
		assertEquals(role1.getName(), roleJ.get("name").asText());
		assertEquals(role1.getDescription(), roleJ.get("description").asText());
	}

	@Test
	public void testCreateGrantByUser() {
		Response response = target("/v3/projects").path(project.getId()).path("users").path(user.getId()).path("roles")
				.path(role2.getId()).register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText())
				.put(Entity.json(""));
		assertEquals(204, response.getStatus());
	}

	@Test
	public void testCheckGrantByUser() {
		Response response = target("/v3/projects").path(project.getId()).path("users").path(user.getId()).path("roles")
				.path(role1.getId()).register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).head();
		assertEquals(204, response.getStatus());
	}

	@Test
	public void testRevokeGrantByUser() {
		Response response = target("/v3/projects").path(project.getId()).path("users").path(user.getId()).path("roles")
				.path(role1.getId()).register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).delete();
		assertEquals(204, response.getStatus());
	}

	@Test
	public void testListGrantByGroup() throws JsonProcessingException, IOException {
		final List<Role> roles = new ArrayList<Role>();
		roles.add(role1);

		Response response = target("/v3/projects").path(project.getId()).path("groups").path(group.getId()).path("roles")
				.register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());
		String ret = response.readEntity(String.class);
		System.err.println(ret);
		JsonNode node = JsonUtils.convertToJsonNode(ret);
		JsonNode roleList = node.get("roles");
		assertEquals(1, roleList.size());
		JsonNode roleJ = roleList.get(0);
		assertEquals(role1.getId(), roleJ.get("id").asText());
		assertEquals(role1.getName(), roleJ.get("name").asText());
		assertEquals(role1.getDescription(), roleJ.get("description").asText());
	}

	@Test
	public void testCreateGrantByGroup() {
		Response response = target("/v3/projects").path(project.getId()).path("groups").path(group.getId()).path("roles")
				.path(role2.getId()).register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText())
				.put(Entity.json(""));
		assertEquals(204, response.getStatus());
	}

	@Test
	public void testCheckGrantByGroup() {
		Response response = target("/v3/projects").path(project.getId()).path("groups").path(group.getId()).path("roles")
				.path(role1.getId()).register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).head();
		assertEquals(204, response.getStatus());
	}

	@Test
	public void testRevokeGrantByGroup() {
		Response response = target("/v3/projects").path(project.getId()).path("groups").path(group.getId()).path("roles")
				.path(role1.getId()).register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).delete();
		assertEquals(204, response.getStatus());
	}

}
