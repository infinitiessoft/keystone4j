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
package com.infinities.keystone4j.admin.v3.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
import com.infinities.keystone4j.model.identity.CreateUserParam;
import com.infinities.keystone4j.model.identity.Group;
import com.infinities.keystone4j.model.identity.UpdateUserParam;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.model.identity.UserParam;
import com.infinities.keystone4j.model.identity.wrapper.CreateUserParamWrapper;
import com.infinities.keystone4j.model.identity.wrapper.UpdateUserParamWrapper;
import com.infinities.keystone4j.model.identity.wrapper.UserParamWrapper;
import com.infinities.keystone4j.model.utils.Views;
import com.infinities.keystone4j.utils.JsonUtils;
import com.infinities.keystone4j.utils.jackson.JacksonFeature;
import com.infinities.keystone4j.utils.jackson.ObjectMapperResolver;

public class UserV3ResourceTest extends AbstractIntegratedTest {

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

		defaultDomain = new Domain();
		defaultDomain.setId("default");
		defaultDomain.setDescription("desc of default Domain");
		defaultDomain.setName("my default domain");

		project = new Project();
		project.setId("88e550a135bb4e6da68e79e5b7c4b2f2");
		project.setDomain(defaultDomain);
		project.setName("admin");

		user = new User();
		user.setId("e7912c2225e84ac5905d8cf0b5040a6d");
		user.setName("admin");
		user.setDescription("admin user");
		user.setDomain(defaultDomain);
		user.setDefaultProject(project);

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

		return new UserResourceTestApplication();

	}

	@Test
	public void testCreateUser() throws JsonGenerationException, JsonMappingException, IOException {
		CreateUserParam user = new CreateUserParam();
		user.setDefaultProjectId(project.getId());
		user.setDescription("description");
		user.setDomainId("default");
		user.setName("testing");
		user.setPassword("password");
		CreateUserParamWrapper wrapper = new CreateUserParamWrapper(user);
		String json = JsonUtils.toJson(wrapper, Views.Advance.class);
		JsonNode node = JsonUtils.convertToJsonNode(json);
		JsonNode userJ = node.get("user");
		assertEquals(user.getName(), userJ.get("name").asText());
		assertEquals(user.getDescription(), userJ.get("description").asText());
		assertEquals(user.getDomainId(), userJ.get("domain_id").asText());
		assertEquals(user.getDefaultProjectId(), userJ.get("default_project_id").asText());
		assertEquals(user.getPassword(), userJ.get("password").asText());
		Response response = target("/v3/users").register(JacksonFeature.class).register(ObjectMapperResolver.class)
				.request().header("X-Auth-Token", Config.getOpt(Config.Type.DEFAULT, "admin_token").asText())
				.post(Entity.entity(wrapper, MediaType.APPLICATION_JSON_TYPE));
		assertEquals(201, response.getStatus());

		node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		System.err.println(node);
		userJ = node.get("user");
		assertNotNull(userJ.get("id").asText());
		assertEquals(user.getName(), userJ.get("name").asText());
		assertEquals(user.getDescription(), userJ.get("description").asText());
		assertEquals(user.getDomainId(), userJ.get("domain_id").asText());
		assertEquals(user.getDefaultProjectId(), userJ.get("default_project_id").asText());
		assertEquals(user.getEnabled().booleanValue(), userJ.get("enabled").asBoolean());
		assertNull(userJ.get("password"));

	}

	@Test
	public void testListUsers() throws JsonProcessingException, IOException {
		final List<User> users = new ArrayList<User>();
		users.add(user);

		Response response = target("/v3/users").register(JacksonFeature.class).register(ObjectMapperResolver.class)
				.request().header("X-Auth-Token", Config.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());
		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		System.err.println(node.toString());
		JsonNode usersJ = node.get("users");
		assertEquals(2, usersJ.size());
		JsonNode userJ = usersJ.get(0);
		assertNotNull(userJ.get("id").asText());
		assertNotNull(userJ.get("name").asText());
		assertNotNull(userJ.get("description").asText());
		assertNotNull(userJ.get("domain_id").asText());
		assertNotNull(userJ.get("default_project_id").asText());
		assertNotNull(userJ.get("enabled").asText());
		assertNotNull(userJ.get("links"));
		assertNotNull(userJ.get("links").get("self").asText());
		assertNull(userJ.get("password"));
	}

	@Test
	public void testGetUser() throws JsonGenerationException, JsonMappingException, IOException {
		Response response = target("/v3/users").path(user.getId()).register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());

		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		System.err.println(node.toString());
		JsonNode userJ = node.get("user");
		assertEquals(user.getId(), userJ.get("id").asText());
		assertEquals(user.getName(), userJ.get("name").asText());
		assertEquals(user.getDescription(), userJ.get("description").asText());
		assertEquals(user.getDomain().getId(), userJ.get("domain_id").asText());
		assertEquals(user.getDefaultProjectId(), userJ.get("default_project_id").asText());
		assertEquals(user.getEnabled().booleanValue(), userJ.get("enabled").asBoolean());
		assertNull(userJ.get("password"));

	}

	@Test
	public void testUpdateUser() throws ClientProtocolException, IOException {
		String userid = this.user.getId();
		UpdateUserParam user = new UpdateUserParam();
		user.setDefaultProjectId(project.getId());
		user.setDescription("description");
		user.setName("testing");
		user.setPassword("password");
		user.setEnabled(false);
		UpdateUserParamWrapper wrapper = new UpdateUserParamWrapper(user);
		String json = JsonUtils.toJson(wrapper, Views.Advance.class);
		System.err.println(json);

		PatchClient client = new PatchClient("http://localhost:9998/v3/users/" + userid);
		JsonNode node = client.connect(wrapper);
		System.err.println(node.toString());
		JsonNode userJ = node.get("user");
		assertEquals(userid, userJ.get("id").asText());
		assertEquals(user.getName(), userJ.get("name").asText());
		assertEquals(user.getDescription(), userJ.get("description").asText());
		assertEquals(this.user.getDomainId(), userJ.get("domain_id").asText());
		assertEquals(user.getDefaultProjectId(), userJ.get("default_project_id").asText());
		assertEquals(user.getEnabled().booleanValue(), userJ.get("enabled").asBoolean());
		assertNull(userJ.get("password"));
	}

	@Test
	public void testDeleteUser() {
		Response response = target("/v3/users").path(user.getId()).register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.getOpt(Config.Type.DEFAULT, "admin_token").asText()).delete();
		assertEquals(204, response.getStatus());
	}

	@Test
	public void testChangePassword() throws JsonGenerationException, JsonMappingException, IOException {
		final UserParam param = new UserParam();
		param.setPassword("secret2");
		param.setOriginalPassword("f00@bar");

		UserParamWrapper wrapper = new UserParamWrapper(param);
		String json = JsonUtils.toJson(wrapper, Views.Advance.class);
		JsonNode node = JsonUtils.convertToJsonNode(json);
		JsonNode userJ = node.get("user");
		assertEquals(param.getPassword(), userJ.get("password").asText());
		assertEquals(param.getOriginalPassword(), userJ.get("original_password").asText());
		Response response = target("/v3/users").path(user.getId()).path("password").register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.getOpt(Config.Type.DEFAULT, "admin_token").asText())
				.post(Entity.entity(wrapper, MediaType.APPLICATION_JSON_TYPE));
		assertEquals(204, response.getStatus());
	}

	@Test
	public void testListGroupsForUser() throws JsonProcessingException, IOException {
		final List<Group> groups = new ArrayList<Group>();
		groups.add(group);

		Response response = target("/v3/users").path("e7912c2225e84ac5905d8cf0b5040a6f").path("groups")
				.register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());
		String ret = response.readEntity(String.class);
		System.err.println(ret);
		JsonNode node = JsonUtils.convertToJsonNode(ret);
		JsonNode groupsJ = node.get("groups");
		assertEquals(1, groupsJ.size());
		JsonNode groupJ = groupsJ.get(0);
		assertEquals(group.getId(), groupJ.get("id").asText());
		assertEquals(group.getName(), groupJ.get("name").asText());
		assertEquals(group.getDomain().getId(), groupJ.get("domain_id").asText());
	}

	@Test
	public void testListUserProjects() throws JsonProcessingException, IOException {
		final List<Project> projects = new ArrayList<Project>();
		projects.add(project);

		Response response = target("/v3/users").path(user.getId()).path("projects").register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());
		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		System.err.println(node.toString());
		JsonNode projectsJ = node.get("projects");
		assertEquals(1, projectsJ.size());
		JsonNode projectJ = projectsJ.get(0);
		assertEquals(project.getId(), projectJ.get("id").asText());
		assertEquals(project.getName(), projectJ.get("name").asText());
		assertEquals(project.getDomain().getId(), projectJ.get("domain_id").asText());
	}

}
