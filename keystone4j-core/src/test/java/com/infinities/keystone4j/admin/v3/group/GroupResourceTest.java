package com.infinities.keystone4j.admin.v3.group;

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
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.infinities.keystone4j.PatchClient;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.identity.Group;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.model.identity.wrapper.GroupWrapper;
import com.infinities.keystone4j.model.utils.Views;
import com.infinities.keystone4j.utils.jackson.JacksonFeature;
import com.infinities.keystone4j.utils.jackson.JsonUtils;
import com.infinities.keystone4j.utils.jackson.ObjectMapperResolver;

public class GroupResourceTest extends JerseyTest {

	private Domain defaultDomain;
	private User user, user2;
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

		user2 = new User();
		user2.setId("e7912c2225e84ac5905d8cf0b5040a6f");
		user2.setName("demo");
		user2.setDescription("demo user");
		user2.setDomain(defaultDomain);
		user2.setDefaultProject(project);

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

		return new GroupResourceTestApplication();

	}

	@Test
	public void testCreateGroup() throws JsonProcessingException, IOException {
		Group group = new Group();
		group.setName("test");
		group.setDescription("description");
		group.setDomainId("default");
		GroupWrapper wrapper = new GroupWrapper(group);
		String json = JsonUtils.toJson(wrapper, Views.Advance.class);
		JsonNode node = JsonUtils.convertToJsonNode(json);
		JsonNode groupJ = node.get("group");
		assertEquals(group.getName(), groupJ.get("name").asText());
		assertEquals(group.getDescription(), groupJ.get("description").asText());
		assertEquals(group.getDomain().getId(), groupJ.get("domain_id").asText());
		Response response = target("/v3/groups").register(JacksonFeature.class).register(ObjectMapperResolver.class)
				.request().header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText())
				.post(Entity.entity(wrapper, MediaType.APPLICATION_JSON_TYPE));
		assertEquals(201, response.getStatus());
		node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		System.err.println(node.toString());
		groupJ = node.get("group");
		assertNotNull(groupJ.get("id").asText());
		assertEquals(group.getName(), groupJ.get("name").asText());
		assertEquals(group.getDescription(), groupJ.get("description").asText());
		assertEquals(group.getDomain().getId(), groupJ.get("domain_id").asText());
	}

	@Test
	public void testListGroups() throws JsonProcessingException, IOException {
		final List<Group> groups = new ArrayList<Group>();
		groups.add(group);

		Response response = target("/v3/groups").register(JacksonFeature.class).register(ObjectMapperResolver.class)
				.request().header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());
		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		JsonNode groupsJ = node.get("groups");
		assertEquals(1, groupsJ.size());
		JsonNode groupJ = groupsJ.get(0);
		assertEquals(group.getId(), groupJ.get("id").asText());
		assertEquals(group.getName(), groupJ.get("name").asText());
		assertEquals(group.getDomain().getId(), groupJ.get("domain_id").asText());
	}

	@Test
	public void testGetGroup() throws JsonProcessingException, IOException {
		Response response = target("/v3/groups").path(group.getId()).register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());
		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		JsonNode groupJ = node.get("group");
		assertEquals(group.getId(), groupJ.get("id").asText());
		assertEquals(group.getName(), groupJ.get("name").asText());
		assertEquals(group.getDomain().getId(), groupJ.get("domain_id").asText());
	}

	@Test
	public void testUpdateGroup() throws ClientProtocolException, IOException {
		group.setDescription("description");
		GroupWrapper wrapper = new GroupWrapper(group);
		PatchClient client = new PatchClient("http://localhost:9998/v3/groups/" + group.getId());
		JsonNode node = client.connect(wrapper);
		JsonNode groupJ = node.get("group");
		assertEquals(group.getId(), groupJ.get("id").asText());
		assertEquals(group.getName(), groupJ.get("name").asText());
		assertEquals(group.getDescription(), groupJ.get("description").asText());
		assertEquals(group.getDomain().getId(), groupJ.get("domain_id").asText());
	}

	@Test
	public void testDeleteGroup() {
		Response response = target("/v3/groups").path(group.getId()).register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).delete();
		assertEquals(204, response.getStatus());
	}

	@Test
	public void testListUsersInGroup() throws JsonProcessingException, IOException {
		Response response = target("/v3/groups").path(group.getId()).path("users").register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());
		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		JsonNode usersJ = node.get("users");
		assertEquals(1, usersJ.size());
		JsonNode userJ = usersJ.get(0);
		assertEquals(user2.getId(), userJ.get("id").asText());
		assertEquals(user2.getName(), userJ.get("name").asText());
		assertEquals(user2.getDescription(), userJ.get("description").asText());
		assertEquals(user2.getDomain().getId(), userJ.get("domain_id").asText());
		assertEquals(user2.getDefaultProjectId(), userJ.get("default_project_id").asText());
		assertNull(userJ.get("password"));
	}

	@Test
	public void testAddUserToGroup() {
		Response response = target("/v3/groups").path(group.getId()).path("users").path(user.getId())
				.register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText())
				.put(Entity.json(""));
		assertEquals(204, response.getStatus());
	}

	@Test
	public void testCheckUserInGroup() {
		Response response = target("/v3/groups").path(group.getId()).path("users").path(user2.getId())
				.register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).head();
		assertEquals(204, response.getStatus());
	}

	@Test
	public void testRemoveUserFromGroup() {
		Response response = target("/v3/groups").path(group.getId()).path("users").path(user2.getId())
				.register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).delete();
		assertEquals(204, response.getStatus());
	}

}
