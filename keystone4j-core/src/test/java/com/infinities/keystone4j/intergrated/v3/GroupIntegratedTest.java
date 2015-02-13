//package com.infinities.keystone4j.intergrated.v3;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertNull;
//
//import java.io.IOException;
//
//import javax.ws.rs.client.Entity;
//import javax.ws.rs.core.Application;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//
//import org.apache.http.client.ClientProtocolException;
//import org.glassfish.jersey.test.TestProperties;
//import org.junit.Test;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.infinities.keystone4j.KeystoneApplication;
//import com.infinities.keystone4j.PatchClient;
//import com.infinities.keystone4j.common.Config;
//import com.infinities.keystone4j.model.assignment.Domain;
//import com.infinities.keystone4j.model.assignment.Project;
//import com.infinities.keystone4j.model.assignment.Role;
//import com.infinities.keystone4j.model.identity.Group;
//import com.infinities.keystone4j.model.identity.GroupWrapper;
//import com.infinities.keystone4j.model.identity.User;
//import com.infinities.keystone4j.model.utils.Views;
//import com.infinities.keystone4j.utils.jackson.JacksonFeature;
//import com.infinities.keystone4j.utils.jackson.JsonUtils;
//import com.infinities.keystone4j.utils.jackson.ObjectMapperResolver;
//
//public class GroupIntegratedTest extends AbstractIntegratedTest {
//
//	private Domain domain, domain2, defaultDomain;
//	private User user;
//	private Group group;
//	private Project project;
//	private Role role1;
//	private final String baseUrl = "http://localhost:8080/v3/groups";
//
//
//	@Override
//	protected Application configure() {
//		enable(TestProperties.LOG_TRAFFIC);
//		enable(TestProperties.DUMP_ENTITY);
//
//		domain = new Domain();
//		domain.setId("default");
//
//		domain2 = new Domain();
//		domain2.setId("domain2");
//		domain2.setDescription("desc of Domain2");
//		domain2.setName("my domain2");
//
//		defaultDomain = new Domain();
//		defaultDomain.setId("default");
//		defaultDomain.setDescription("desc of default Domain");
//		defaultDomain.setName("my default domain");
//
//		project = new Project();
//		project.setId("project");
//		project.setDescription("desc of Project");
//		project.setDomain(domain);
//		project.setName("my project");
//
//		user = new User();
//		// user.setId("newuser");
//		user.setDescription("my user");
//		user.setName("example user");
//		user.setDefault_project(project);
//		user.setDomain(domain);
//		user.setEmail("user@com.tw");
//		user.setPassword("password");
//
//		group = new Group();
//		// group.setId("newgroup");
//		group.setDescription("my group");
//		group.setName("example group");
//		group.setDomain(domain);
//
//		role1 = new Role();
//		role1.setId("role1");
//		role1.setDescription("my role1");
//		role1.setName("example role1");
//		return new KeystoneApplication();
//	}
//
//	@Test
//	public void testCreateGroup() throws JsonProcessingException, IOException {
//		GroupWrapper wrapper = new GroupWrapper(group, baseUrl);
//		String json = JsonUtils.toJson(wrapper, Views.Advance.class);
//		JsonNode node = JsonUtils.convertToJsonNode(json);
//		JsonNode groupJ = node.get("group");
//		assertEquals(group.getName(), groupJ.get("name").asText());
//		assertEquals(group.getDescription(), groupJ.get("description").asText());
//		assertEquals(group.getDomain().getId(), groupJ.get("domain_id").asText());
//		Response response = target("/v3/groups").register(JacksonFeature.class).register(ObjectMapperResolver.class)
//				.request().header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText())
//				.post(Entity.entity(wrapper, MediaType.APPLICATION_JSON_TYPE));
//		assertEquals(201, response.getStatus());
//		node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
//		groupJ = node.get("group");
//		assertNotNull(groupJ.get("id").asText());
//		assertEquals(group.getName(), groupJ.get("name").asText());
//		assertEquals(group.getDescription(), groupJ.get("description").asText());
//		assertEquals(group.getDomain().getId(), groupJ.get("domain_id").asText());
//	}
//
//	@Test
//	public void testListGroups() throws JsonProcessingException, IOException {
//		Response response = target("/v3/groups").register(JacksonFeature.class).register(ObjectMapperResolver.class)
//				.request().header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
//		assertEquals(200, response.getStatus());
//		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
//		JsonNode groupsJ = node.get("groups");
//		assertEquals(2, groupsJ.size());
//	}
//
//	@Test
//	public void testGetGroup() throws JsonProcessingException, IOException {
//		group.setId("708bb4f9-9d3c-46af-b18c-7033dc022ffc");
//
//		Response response = target("/v3/groups").path(group.getId()).register(JacksonFeature.class)
//				.register(ObjectMapperResolver.class).request()
//				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
//		assertEquals(200, response.getStatus());
//		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
//		JsonNode groupJ = node.get("group");
//		assertEquals("708bb4f9-9d3c-46af-b18c-7033dc022ffc", groupJ.get("id").asText());
//		assertEquals("admin_group", groupJ.get("name").asText());
//		assertEquals("admin group", groupJ.get("description").asText());
//		assertEquals("default", groupJ.get("domain_id").asText());
//	}
//
//	@Test
//	public void testUpdateGroup() throws ClientProtocolException, IOException {
//		group.setId("708bb4f9-9d3c-46af-b18c-7033dc022ffc");
//
//		GroupWrapper wrapper = new GroupWrapper(group, baseUrl);
//		PatchClient client = new PatchClient("http://localhost:9998/v3/groups/" + group.getId());
//		JsonNode node = client.connect(wrapper);
//		JsonNode groupJ = node.get("group");
//		assertEquals(group.getId(), groupJ.get("id").asText());
//		assertEquals(group.getName(), groupJ.get("name").asText());
//		assertEquals(group.getDescription(), groupJ.get("description").asText());
//		assertEquals(group.getDomain().getId(), groupJ.get("domain_id").asText());
//	}
//
//	@Test
//	public void testDeleteGroup() {
//		group.setId("708bb4f9-9d3c-46af-b18c-7033dc022ffc");
//		Response response = target("/v3/groups").path(group.getId()).register(JacksonFeature.class)
//				.register(ObjectMapperResolver.class).request()
//				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).delete();
//		assertEquals(204, response.getStatus());
//	}
//
//	@Test
//	public void testListUsersInGroup() throws JsonProcessingException, IOException {
//		group.setId("708bb4f9-9d3c-46af-b18c-7033dc022ffd");
//
//		Response response = target("/v3/groups").path(group.getId()).path("users").register(JacksonFeature.class)
//				.register(ObjectMapperResolver.class).request()
//				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
//		assertEquals(200, response.getStatus());
//		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
//		JsonNode usersJ = node.get("users");
//		assertEquals(1, usersJ.size());
//		JsonNode userJ = usersJ.get(0);
//		assertEquals("0f3328f8-a7e7-41b4-830d-be8fdd5186c8", userJ.get("id").asText());
//		assertEquals("demo", userJ.get("name").asText());
//		assertEquals("demo user", userJ.get("description").asText());
//		assertEquals("default", userJ.get("domain_id").asText());
//		assertEquals("79ea2c65-4679-441f-a596-8aec16752a20", userJ.get("default_project_id").asText());
//		assertEquals("demo@keystone4j.com", userJ.get("email").asText());
//		assertNull(userJ.get("password"));
//	}
//
//	@Test
//	public void testAddUserToGroup() {
//		group.setId("708bb4f9-9d3c-46af-b18c-7033dc022ffd");
//		user.setId("0f3328f8-a7e7-41b4-830d-be8fdd5186c7");
//
//		Response response = target("/v3/groups").path(group.getId()).path("users").path(user.getId())
//				.register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
//				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText())
//				.put(Entity.json(""));
//		assertEquals(204, response.getStatus());
//	}
//
//	@Test
//	public void testCheckUserInGroup() {
//		group.setId("708bb4f9-9d3c-46af-b18c-7033dc022ffd");
//		user.setId("0f3328f8-a7e7-41b4-830d-be8fdd5186c8");
//
//		Response response = target("/v3/groups").path(group.getId()).path("users").path(user.getId())
//				.register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
//				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).head();
//		assertEquals(204, response.getStatus());
//	}
//
//	@Test
//	public void testRemoveUserFromGroup() {
//		group.setId("708bb4f9-9d3c-46af-b18c-7033dc022ffd");
//		user.setId("0f3328f8-a7e7-41b4-830d-be8fdd5186c8");
//		Response response = target("/v3/groups").path(group.getId()).path("users").path(user.getId())
//				.register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
//				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).delete();
//		assertEquals(204, response.getStatus());
//	}
//
// }
