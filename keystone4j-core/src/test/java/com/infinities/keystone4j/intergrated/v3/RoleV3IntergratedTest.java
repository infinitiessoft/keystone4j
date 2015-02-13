//package com.infinities.keystone4j.intergrated.v3;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
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
//import com.fasterxml.jackson.core.JsonGenerationException;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonMappingException;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.infinities.keystone4j.KeystoneApplication;
//import com.infinities.keystone4j.PatchClient;
//import com.infinities.keystone4j.common.Config;
//import com.infinities.keystone4j.model.assignment.Domain;
//import com.infinities.keystone4j.model.assignment.Project;
//import com.infinities.keystone4j.model.assignment.Role;
//import com.infinities.keystone4j.model.assignment.RoleWrapper;
//import com.infinities.keystone4j.model.identity.Group;
//import com.infinities.keystone4j.model.identity.User;
//import com.infinities.keystone4j.model.utils.Views;
//import com.infinities.keystone4j.utils.jackson.JacksonFeature;
//import com.infinities.keystone4j.utils.jackson.JsonUtils;
//import com.infinities.keystone4j.utils.jackson.ObjectMapperResolver;
//
//public class RoleV3IntergratedTest extends AbstractIntegratedTest {
//
//	private Domain domain;
//	private User user;
//	private Group group;
//	private Project project;
//	private Role role1;
//	private final String baseUrl = "http://localhost:8080/v3/roles";
//
//
//	@Override
//	protected Application configure() {
//		enable(TestProperties.LOG_TRAFFIC);
//		enable(TestProperties.DUMP_ENTITY);
//
//		domain = new Domain();
//		domain.setId("domain1");
//		domain.setDescription("desc of Domain");
//		domain.setName("my domain");
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
//		group.setId("newgroup");
//		group.setDescription("my group");
//		group.setName("example group");
//		group.setDomain(domain);
//
//		role1 = new Role();
//		role1.setDescription("my role1");
//		role1.setName("example role1");
//
//		return new KeystoneApplication();
//	}
//
//	@Test
//	public void testCreateRole() throws JsonGenerationException, JsonMappingException, IOException {
//
//		RoleWrapper wrapper = new RoleWrapper(role1, baseUrl);
//		String json = JsonUtils.toJson(wrapper, Views.Advance.class);
//		JsonNode node = JsonUtils.convertToJsonNode(json);
//		JsonNode roleJ = node.get("role");
//		assertEquals(role1.getName(), roleJ.get("name").asText());
//		assertEquals(role1.getDescription(), roleJ.get("description").asText());
//		Response response = target("/v3/roles").register(JacksonFeature.class).register(ObjectMapperResolver.class)
//				.request().header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText())
//				.post(Entity.entity(wrapper, MediaType.APPLICATION_JSON_TYPE));
//		assertEquals(201, response.getStatus());
//
//		node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
//		roleJ = node.get("role");
//		assertNotNull(roleJ.get("id").asText());
//		assertEquals(role1.getName(), roleJ.get("name").asText());
//		assertEquals(role1.getDescription(), roleJ.get("description").asText());
//	}
//
//	@Test
//	public void testListRoles() throws JsonProcessingException, IOException {
//		Response response = target("/v3/roles").register(JacksonFeature.class).register(ObjectMapperResolver.class)
//				.request().header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
//		assertEquals(200, response.getStatus());
//		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
//		JsonNode rolesJ = node.get("roles");
//		assertEquals(2, rolesJ.size());
//	}
//
//	@Test
//	public void testGetRole() throws JsonProcessingException, IOException {
//		final String id = "708bb4f9-9d3c-46af-b18c-7033dc022ffb";
//		role1.setId(id);
//
//		Response response = target("/v3/roles").path(role1.getId()).register(JacksonFeature.class)
//				.register(ObjectMapperResolver.class).request()
//				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
//		assertEquals(200, response.getStatus());
//
//		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
//		JsonNode roleJ = node.get("role");
//		assertEquals("708bb4f9-9d3c-46af-b18c-7033dc022ffb", roleJ.get("id").asText());
//		assertEquals("admin", roleJ.get("name").asText());
//		assertEquals("admin role", roleJ.get("description").asText());
//	}
//
//	@Test
//	public void testUpdateRole() throws ClientProtocolException, IOException {
//		final String id = "708bb4f9-9d3c-46af-b18c-7033dc022ffb";
//		role1.setId(id);
//		RoleWrapper wrapper = new RoleWrapper(role1, baseUrl);
//		PatchClient client = new PatchClient("http://localhost:9998/v3/roles/" + role1.getId());
//		JsonNode node = client.connect(wrapper);
//
//		JsonNode roleJ = node.get("role");
//		assertEquals(id, roleJ.get("id").asText());
//		assertEquals(role1.getName(), roleJ.get("name").asText());
//		assertEquals(role1.getDescription(), roleJ.get("description").asText());
//	}
//
//	@Test
//	public void testDeleteRole() {
//		final String id = "708bb4f9-9d3c-46af-b18c-7033dc022ffb";
//		role1.setId(id);
//		Response response = target("/v3/roles").path(role1.getId()).register(JacksonFeature.class)
//				.register(ObjectMapperResolver.class).request()
//				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).delete();
//		assertEquals(204, response.getStatus());
//	}
//
// }
