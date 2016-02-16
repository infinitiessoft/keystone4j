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
package com.infinities.keystone4j.intergrated.v3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

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
import com.google.common.base.Strings;
import com.infinities.keystone4j.KeystoneApplication;
import com.infinities.keystone4j.PatchClient;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.assignment.wrapper.ProjectWrapper;
import com.infinities.keystone4j.model.assignment.wrapper.RoleWrapper;
import com.infinities.keystone4j.model.auth.AuthV3;
import com.infinities.keystone4j.model.auth.AuthV3Wrapper;
import com.infinities.keystone4j.model.auth.Identity;
import com.infinities.keystone4j.model.auth.Password;
import com.infinities.keystone4j.model.identity.CreateUserParam;
import com.infinities.keystone4j.model.identity.Group;
import com.infinities.keystone4j.model.identity.UpdateUserParam;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.model.identity.wrapper.CreateUserParamWrapper;
import com.infinities.keystone4j.model.identity.wrapper.GroupWrapper;
import com.infinities.keystone4j.model.identity.wrapper.UpdateUserParamWrapper;
import com.infinities.keystone4j.model.utils.Views;
import com.infinities.keystone4j.utils.JsonUtils;
import com.infinities.keystone4j.utils.jackson.JacksonFeature;
import com.infinities.keystone4j.utils.jackson.ObjectMapperResolver;

public class Keystone4jV3IT extends AbstractIntegratedTest {

	private Domain defaultDomain;
	private String adminToken;


	@Override
	protected Application configure() {
		enable(TestProperties.LOG_TRAFFIC);
		enable(TestProperties.DUMP_ENTITY);
		defaultDomain = new Domain();
		defaultDomain.setId("default");

		return new KeystoneApplication();
	}

	@Test
	public void testCreateAdminUserIT() throws JsonProcessingException, IOException {
		// create project
		String newProjectId = createProject(defaultDomain);
		// create admin role
		String newRoleId = createAdminRole();
		// create user
		String newUserId = createUser(newProjectId);
		// grant user
		grantUser(newUserId, newProjectId, newRoleId);
		// authenticate user
		String tokenid = authenticate("default", "default", "default");
		// list project by admin user
		listProject(newProjectId, tokenid);
	}

	@Test
	public void testCreateAdminGroupIT() throws JsonProcessingException, IOException {
		// create project
		String newProjectId = createProject(defaultDomain);
		// create admin role
		String newRoleId = createAdminRole();
		// create group
		String newGroupId = createGroup();
		// grant group
		grantGroup(newGroupId, newProjectId, newRoleId);
		// create user
		String newUserId = createUser(newProjectId);
		// add user to group
		addUserToGroup(newUserId, newGroupId);
		// authenticate user
		String tokenid = authenticate("default", "default", "default");
		// list project by admin user
		listProject(newProjectId, tokenid);
	}

	@Test
	public void testIT() throws JsonGenerationException, JsonMappingException, IOException {
		// create project
		String newProjectId = createProject(defaultDomain);
		// get project
		getProject(newProjectId);
		// list project
		listProject(newProjectId);
		// update project
		updateProject(newProjectId);
		// create role
		String newRoleId = createRole();
		// get role
		getRole(newRoleId);
		// list role
		listRole(newRoleId);
		// update role
		updateRole(newRoleId);
		// create group
		String newGroupId = createGroup();
		// get group
		getGroup(newGroupId);
		// list group
		listGroup(newGroupId);
		// update group
		updateGroup(newGroupId);
		// grant group
		grantGroup(newGroupId, newProjectId, newRoleId);
		// list grant by group
		listGrantByGroup(newGroupId, newProjectId, newRoleId);
		// create user
		String newUserId = createUser(newProjectId);
		// get user
		getUser(newUserId);
		// list user
		listUser(newUserId);
		// grant user
		grantUser(newUserId, newProjectId, newRoleId);
		// list grant by user
		listGrantByUser(newUserId, newProjectId, newRoleId);
		// add user to group
		addUserToGroup(newUserId, newGroupId);
		// check user in group
		checkUserInGroup(newUserId, newGroupId);
		// list users in group
		listUserInGroup(newUserId, newGroupId);
		// authenticate user
		String tokenid = authenticate("default", "default", "default");
		// list user project
		listUserProject(newUserId, newProjectId, tokenid);
		// list service failed
		listService(tokenid);
		// revoke token
		revokeToken(tokenid);
		// list user project failed;
		listUserProjectFail(newUserId, tokenid);
		// update user
		updateUser(newUserId, "updated", false);
		// remove user from group
		removeUserFromGroup(newUserId, newGroupId);
		// revoke grant by user
		revokeGrantByUser(newUserId, newProjectId, newRoleId);
		// delete user
		deleteUser(newUserId);
		// revoke grant by user
		revokeGrantByGroup(newGroupId, newProjectId, newRoleId);
		// delete group
		deleteGroup(newGroupId);
		// delete role
		deleteRole(newRoleId);
		// delete project
		deleteProject(newProjectId);
	}

	private String getAdminToken() {
		if (Strings.isNullOrEmpty(adminToken)) {
			adminToken = Config.getOpt(Config.Type.DEFAULT, "admin_token").asText();
		}
		return adminToken;
	}

	private void listGrantByGroup(String newGroupId, String newProjectId, String newRoleId) throws JsonProcessingException,
			IOException {
		Response response = target("/v3/projects").path(newProjectId).path("groups").path(newGroupId).path("roles")
				.register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", getAdminToken()).get();
		assertEquals(200, response.getStatus());
		String ret = response.readEntity(String.class);
		JsonNode node = JsonUtils.convertToJsonNode(ret);
		JsonNode roleList = node.get("roles");
		assertEquals(1, roleList.size());
		JsonNode roleJ = roleList.get(0);
		assertEquals(newRoleId, roleJ.get("id").asText());
	}

	private void listGrantByUser(String newUserId, String newProjectId, String newRoleId) throws JsonProcessingException,
			IOException {
		Response response = target("/v3/projects").path(newProjectId).path("users").path(newUserId).path("roles")
				.register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", getAdminToken()).get();
		assertEquals(200, response.getStatus());
		String ret = response.readEntity(String.class);
		JsonNode node = JsonUtils.convertToJsonNode(ret);
		JsonNode roleList = node.get("roles");
		assertEquals(1, roleList.size());
		JsonNode roleJ = roleList.get(0);
		assertEquals(newRoleId, roleJ.get("id").asText());
	}

	private void revokeGrantByGroup(String newGroupId, String newProjectId, String newRoleId) {
		Response response = target("/v3/projects").path(newProjectId).path("groups").path(newGroupId).path("roles")
				.path(newRoleId).register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", getAdminToken()).delete();
		assertEquals(204, response.getStatus());
	}

	private void revokeGrantByUser(String newUserId, String newProjectId, String newRoleId) {
		Response response = target("/v3/projects").path(newProjectId).path("users").path(newUserId).path("roles")
				.path(newRoleId).register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", getAdminToken()).delete();
		assertEquals(204, response.getStatus());
	}

	private void grantGroup(String newGroupId, String newProjectId, String newRoleId) {
		Response response = target("/v3/projects").path(newProjectId).path("groups").path(newGroupId).path("roles")
				.path(newRoleId).register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", getAdminToken()).put(Entity.json(""));
		assertEquals(204, response.getStatus());
	}

	private void listUserInGroup(String newUserId, String newGroupId) throws JsonProcessingException, IOException {
		Response response = target("/v3/groups").path(newGroupId).path("users").register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request().header("X-Auth-Token", getAdminToken()).get();
		assertEquals(200, response.getStatus());
		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		JsonNode usersJ = node.get("users");
		assertEquals(1, usersJ.size());
		JsonNode userJ = usersJ.get(0);
		assertEquals(newUserId, userJ.get("id").asText());
	}

	private void removeUserFromGroup(String newUserId, String newGroupId) {
		Response response = target("/v3/groups").path(newGroupId).path("users").path(newUserId)
				.register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", getAdminToken()).delete();
		assertEquals(204, response.getStatus());
	}

	private void checkUserInGroup(String newUserId, String newGroupId) {
		Response response = target("/v3/groups").path(newGroupId).path("users").path(newUserId)
				.register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", getAdminToken()).head();
		assertEquals(204, response.getStatus());
	}

	private void addUserToGroup(String newUserId, String newGroupId) {
		Response response = target("/v3/groups").path(newGroupId).path("users").path(newUserId)
				.register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", getAdminToken()).put(Entity.json(""));
		assertEquals(204, response.getStatus());
	}

	private void deleteGroup(String newGroupId) {
		Response response = target("/v3/groups").path(newGroupId).register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request().header("X-Auth-Token", getAdminToken()).delete();
		assertEquals(204, response.getStatus());
	}

	private void updateGroup(String newGroupId) throws JsonGenerationException, JsonMappingException, IOException {
		Group group = new Group();
		group.setName("updatedName");
		GroupWrapper wrapper = new GroupWrapper(group);
		String json = JsonUtils.toJson(wrapper, Views.Advance.class);
		System.err.println(json);

		PatchClient client = new PatchClient("http://localhost:9998/v3/groups/" + newGroupId);
		JsonNode node = client.connect(wrapper);
		JsonNode groupJ = node.get("group");
		assertEquals(newGroupId, groupJ.get("id").asText());
		assertEquals(group.getName(), groupJ.get("name").asText());
	}

	private void listGroup(String newGroupId) throws JsonProcessingException, IOException {
		Response response = target("/v3/groups").register(JacksonFeature.class).register(ObjectMapperResolver.class)
				.request().header("X-Auth-Token", getAdminToken()).get();
		assertEquals(200, response.getStatus());
		String ret = response.readEntity(String.class);
		JsonNode node = JsonUtils.convertToJsonNode(ret);
		JsonNode groupsJ = node.get("groups");
		boolean exist = false;
		for (JsonNode p : groupsJ) {
			if (newGroupId.equals(p.get("id").asText())) {
				exist = true;
				break;
			}
		}
		assertTrue(exist);
	}

	private void getGroup(String newGroupId) throws JsonProcessingException, IOException {
		Response response = target("/v3/groups").path(newGroupId).register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request().header("X-Auth-Token", getAdminToken()).get();
		assertEquals(200, response.getStatus());
		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		JsonNode groupJ = node.get("group");
		assertEquals(newGroupId, groupJ.get("id").asText());
	}

	private String createGroup() throws JsonGenerationException, JsonMappingException, IOException {
		Group group = new Group();
		group.setName("test");
		group.setDescription("description");
		group.setDomainId("default");
		GroupWrapper wrapper = new GroupWrapper(group);
		Response response = target("/v3/groups").register(JacksonFeature.class).register(ObjectMapperResolver.class)
				.request().header("X-Auth-Token", getAdminToken())
				.post(Entity.entity(wrapper, MediaType.APPLICATION_JSON_TYPE));
		assertEquals(201, response.getStatus());
		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		JsonNode groupJ = node.get("group");
		assertNotNull(groupJ.get("id").asText());
		return groupJ.get("id").asText();
	}

	private void revokeToken(String tokenid) {
		// revoke
		Response response = target("/v3/auth/tokens").register(JacksonFeature.class).register(ObjectMapperResolver.class)
				.request().header("X-Auth-Token", getAdminToken()).header("X-Subject-Token", tokenid).delete();
		assertEquals(204, response.getStatus());
	}

	private void listService(String tokenid) {
		Response response = target("/v3/services").register(JacksonFeature.class).register(ObjectMapperResolver.class)
				.request().header("X-Auth-Token", tokenid).get();
		assertEquals(403, response.getStatus());
	}

	private void listUserProject(String newUserId, String newProjectId, String tokenid) throws JsonProcessingException,
			IOException {
		Response response = target("/v3/users").path(newUserId).path("projects").register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request().header("X-Auth-Token", tokenid).get();
		assertEquals(200, response.getStatus());
		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		JsonNode projectsJ = node.get("projects");
		assertEquals(1, projectsJ.size());
		JsonNode projectJ = projectsJ.get(0);
		assertEquals(newProjectId, projectJ.get("id").asText());
	}

	private void listUserProjectFail(String newUserId, String tokenid) throws JsonProcessingException, IOException {
		Response response = target("/v3/users").path(newUserId).path("projects").register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request().header("X-Auth-Token", tokenid).get();
		assertEquals(401, response.getStatus());
	}

	private void grantUser(String newUserId, String newProjectId, String newRoleId) {
		Response response = target("/v3/projects").path(newProjectId).path("users").path(newUserId).path("roles")
				.path(newRoleId).register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", getAdminToken()).put(Entity.json(""));
		assertEquals(204, response.getStatus());
	}

	private void deleteRole(String newRoleId) {
		Response response = target("/v3/roles").path(newRoleId).register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request().header("X-Auth-Token", getAdminToken()).delete();
		assertEquals(204, response.getStatus());
	}

	private void updateRole(String newRoleId) throws ClientProtocolException, IOException {
		Role role = new Role();
		role.setName("name");
		role.setDescription("description");
		RoleWrapper wrapper = new RoleWrapper(role);
		PatchClient client = new PatchClient("http://localhost:9998/v3/roles/" + newRoleId);
		JsonNode node = client.connect(wrapper);
		JsonNode roleJ = node.get("role");
		assertEquals(newRoleId, roleJ.get("id").asText());
		assertEquals(role.getName(), roleJ.get("name").asText());
		assertEquals(role.getDescription(), roleJ.get("description").asText());
	}

	private void listRole(String newRoleId) throws JsonProcessingException, IOException {
		Response response = target("/v3/roles").register(JacksonFeature.class).register(ObjectMapperResolver.class)
				.request().header("X-Auth-Token", getAdminToken()).get();
		assertEquals(200, response.getStatus());
		String ret = response.readEntity(String.class);
		JsonNode node = JsonUtils.convertToJsonNode(ret);
		JsonNode projectsJ = node.get("roles");
		boolean exist = false;
		for (JsonNode p : projectsJ) {
			if (newRoleId.equals(p.get("id").asText())) {
				exist = true;
				break;
			}
		}
		assertTrue(exist);
	}

	private void getRole(String newRoleId) throws JsonProcessingException, IOException {
		Response response = target("/v3/roles").path(newRoleId).register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request().header("X-Auth-Token", getAdminToken()).get();
		assertEquals(200, response.getStatus());
		String ret = response.readEntity(String.class);
		JsonNode node = JsonUtils.convertToJsonNode(ret);
		JsonNode roleJ = node.get("role");
		assertEquals(newRoleId, roleJ.get("id").asText());
	}

	private String createAdminRole() throws JsonProcessingException, IOException {
		Role role = new Role();
		role.setName("admin");
		role.setDescription("admin");
		RoleWrapper wrapper = new RoleWrapper(role);
		Response response = target("/v3/roles").register(JacksonFeature.class).register(ObjectMapperResolver.class)
				.request().header("X-Auth-Token", getAdminToken())
				.post(Entity.entity(wrapper, MediaType.APPLICATION_JSON_TYPE));
		assertEquals(201, response.getStatus());
		String ret = response.readEntity(String.class);
		JsonNode node = JsonUtils.convertToJsonNode(ret);
		JsonNode roleJ = node.get("role");
		assertNotNull(roleJ.get("id").asText());
		assertEquals(role.getName(), roleJ.get("name").asText());
		assertEquals(role.getDescription(), roleJ.get("description").asText());
		return roleJ.get("id").asText();
	}

	private String createRole() throws JsonProcessingException, IOException {
		Role role = new Role();
		role.setName("test");
		role.setDescription("test");
		RoleWrapper wrapper = new RoleWrapper(role);
		Response response = target("/v3/roles").register(JacksonFeature.class).register(ObjectMapperResolver.class)
				.request().header("X-Auth-Token", getAdminToken())
				.post(Entity.entity(wrapper, MediaType.APPLICATION_JSON_TYPE));
		assertEquals(201, response.getStatus());
		String ret = response.readEntity(String.class);
		JsonNode node = JsonUtils.convertToJsonNode(ret);
		JsonNode roleJ = node.get("role");
		assertNotNull(roleJ.get("id").asText());
		assertEquals(role.getName(), roleJ.get("name").asText());
		assertEquals(role.getDescription(), roleJ.get("description").asText());
		return roleJ.get("id").asText();
	}

	private void deleteProject(String newProjectId) {
		Response response = target("/v3").path("projects").path(newProjectId).register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request().header("X-Auth-Token", getAdminToken()).delete();
		assertEquals(204, response.getStatus());
	}

	private void updateProject(String newProjectId) throws ClientProtocolException, IOException {
		Project project = new Project();
		project.setName("project1");
		ProjectWrapper wrapper = new ProjectWrapper(project);
		PatchClient client = new PatchClient("http://localhost:9998/v3/projects/" + newProjectId);
		JsonNode node = client.connect(wrapper);
		JsonNode projectJ = node.get("project");
		assertEquals(newProjectId, projectJ.get("id").asText());
		assertEquals(project.getName(), projectJ.get("name").asText());
	}

	private void listProject(String newProjectId) throws JsonProcessingException, IOException {
		listProject(newProjectId, getAdminToken());
	}

	private void listProject(String newProjectId, String tokenid) throws JsonProcessingException, IOException {
		Response response = target("/v3/projects").register(JacksonFeature.class).register(ObjectMapperResolver.class)
				.request().header("X-Auth-Token", tokenid).get();
		assertEquals(200, response.getStatus());
		String ret = response.readEntity(String.class);
		JsonNode node = JsonUtils.convertToJsonNode(ret);
		JsonNode projectsJ = node.get("projects");
		boolean exist = false;
		for (JsonNode p : projectsJ) {
			if (newProjectId.equals(p.get("id").asText())) {
				exist = true;
				break;
			}
		}
		assertTrue(exist);
	}

	private void getProject(String newProjectId) throws JsonProcessingException, IOException {
		Response response = target("/v3/projects").path(newProjectId).register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request().header("X-Auth-Token", getAdminToken()).get();
		assertEquals(200, response.getStatus());
		String ret = response.readEntity(String.class);
		JsonNode node = JsonUtils.convertToJsonNode(ret);
		JsonNode projectJ = node.get("project");
		assertEquals(newProjectId, projectJ.get("id").asText());
	}

	private String createProject(Domain domain) throws JsonProcessingException, IOException {
		Project project = new Project();
		project.setDescription("desc of Project");
		project.setDomain(domain);
		project.setName("new project");
		ProjectWrapper wrapper = new ProjectWrapper(project);
		Response response = target("/v3/projects").register(JacksonFeature.class).register(ObjectMapperResolver.class)
				.request().header("X-Auth-Token", getAdminToken())
				.post(Entity.entity(wrapper, MediaType.APPLICATION_JSON_TYPE));
		assertEquals(201, response.getStatus());
		String ret = response.readEntity(String.class);
		JsonNode node = JsonUtils.convertToJsonNode(ret);
		JsonNode projectJ = node.get("project");
		String projectId = projectJ.get("id").asText();
		return projectId;
	}

	private void deleteUser(String newUserId) {
		Response response = target("/v3/users").path(newUserId).register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request().header("X-Auth-Token", getAdminToken()).delete();
		assertEquals(204, response.getStatus());
	}

	private void updateUser(String userid, String name, boolean enabled) throws ClientProtocolException, IOException {
		UpdateUserParam updateUserParam = new UpdateUserParam();
		updateUserParam.setName(name);
		updateUserParam.setEnabled(enabled);
		UpdateUserParamWrapper updateUserParamWrapper = new UpdateUserParamWrapper(updateUserParam);
		PatchClient client = new PatchClient("http://localhost:9998/v3/users/" + userid);
		JsonNode node = client.connect(updateUserParamWrapper);
		JsonNode userJ = node.get("user");
		assertEquals(userid, userJ.get("id").asText());
		assertEquals(name, userJ.get("name").asText());
	}

	private String authenticate(String domainId, String name, String pass) {
		Identity identity = new Identity();
		identity = new Identity();
		identity.getMethods().add("password");
		Domain passDomain = new Domain();
		passDomain.setId(domainId);
		User passUser = new User();
		passUser.setName(name);
		passUser.setPassword(pass);
		passUser.setDomain(passDomain);
		Password password = new Password();
		password.setUser(passUser);
		identity.getAuthMethods().put("password", password);
		AuthV3 auth = new AuthV3();
		auth.setIdentity(identity);
		AuthV3Wrapper authV3Wrapper = new AuthV3Wrapper(auth);
		Response response = target("/v3").path("auth").path("tokens").register(JacksonFeature.class).request()
				.post(Entity.entity(authV3Wrapper, MediaType.APPLICATION_JSON_TYPE));

		assertEquals(201, response.getStatus());
		String tokenid = response.getHeaderString("X-Subject-Token");
		assertNotNull(tokenid);
		return tokenid;
	}

	private void listUser(String newUserId) throws JsonProcessingException, IOException {
		Response response = target("/v3/users").register(JacksonFeature.class).register(ObjectMapperResolver.class)
				.request().header("X-Auth-Token", getAdminToken()).get();
		assertEquals(200, response.getStatus());
		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		JsonNode usersJ = node.get("users");
		boolean exist = false;
		for (JsonNode u : usersJ) {
			if (newUserId.equals(u.get("id").asText())) {
				exist = true;
				break;
			}
		}
		assertTrue(exist);
	}

	private void getUser(String newUserId) throws JsonProcessingException, IOException {
		Response response = target("/v3/users").path(newUserId).register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request().header("X-Auth-Token", getAdminToken()).get();
		assertEquals(200, response.getStatus());
		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		JsonNode userJ = node.get("user");
		assertEquals(newUserId, userJ.get("id").asText());
	}

	private String createUser(String projectId) throws JsonProcessingException, IOException {
		// create
		CreateUserParam user = new CreateUserParam();
		user.setDefaultProjectId(projectId);
		user.setDescription("description");
		user.setDomainId("default");
		user.setName("default");
		user.setPassword("default");
		CreateUserParamWrapper wrapper = new CreateUserParamWrapper(user);
		Response response = target("/v3/users").register(JacksonFeature.class).register(ObjectMapperResolver.class)
				.request().header("X-Auth-Token", getAdminToken())
				.post(Entity.entity(wrapper, MediaType.APPLICATION_JSON_TYPE));
		assertEquals(201, response.getStatus());
		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		JsonNode userJ = node.get("user");
		String newUserId = userJ.get("id").asText();
		return newUserId;
	}
}
