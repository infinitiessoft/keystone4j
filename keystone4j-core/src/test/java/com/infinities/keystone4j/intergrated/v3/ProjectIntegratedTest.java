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
//import com.infinities.keystone4j.model.assignment.GroupDomainGrant;
//import com.infinities.keystone4j.model.assignment.GroupProjectGrant;
//import com.infinities.keystone4j.model.assignment.Project;
//import com.infinities.keystone4j.model.assignment.ProjectWrapper;
//import com.infinities.keystone4j.model.assignment.Role;
//import com.infinities.keystone4j.model.assignment.UserDomainGrant;
//import com.infinities.keystone4j.model.assignment.UserProjectGrant;
//import com.infinities.keystone4j.model.identity.Group;
//import com.infinities.keystone4j.model.identity.User;
//import com.infinities.keystone4j.utils.jackson.JacksonFeature;
//import com.infinities.keystone4j.utils.jackson.JsonUtils;
//import com.infinities.keystone4j.utils.jackson.ObjectMapperResolver;
//
//public class ProjectIntegratedTest extends AbstractIntegratedTest {
//
//	private Domain domain, domain2, defaultDomain;
//	private User user;
//	private Group group;
//	private Project project;
//	private Role role1, role2, role3;
//	private UserDomainGrant userDomainGrant, userDomainGrant2;
//	// private UserDomainGrantMetadata userDomainGrantMetadata1,
//	// userDomainGrantMetadata2;
//	private UserProjectGrant userProjectGrant;
//	// private UserProjectGrantMetadata userProjectGrantMetadata;
//	private GroupDomainGrant groupDomainGrant, groupDomainGrant2;
//	// private GroupDomainGrantMetadata groupDomainGrantMetadata1,
//	// groupDomainGrantMetadata2;
//	private GroupProjectGrant groupProjectGrant;
//	private final String baseUrl = "http://localhost:8080/v3/projects";
//
//
//	// private GroupProjectGrantMetadata groupProjectGrantMetadata;
//
//	@Override
//	protected Application configure() {
//		enable(TestProperties.LOG_TRAFFIC);
//		enable(TestProperties.DUMP_ENTITY);
//
//		domain = new Domain();
//		domain.setId("default");
//		// domain.setDescription("desc of Domain");
//		// domain.setName("my domain");
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
//		// project.setId("project");
//		project.setDescription("desc of Project");
//		project.setDomain(domain);
//		project.setName("my project");
//
//		user = new User();
//		user.setId("newuser");
//		user.setDescription("my user");
//		user.setName("example user");
//		user.setDefault_project(project);
//		user.setDomain(domain);
//		user.setEmail("user@com.tw");
//
//		group = new Group();
//		group.setId("newgroup");
//		group.setDescription("my group");
//		group.setName("example group");
//		group.setDomain(domain);
//
//		role1 = new Role();
//		role1.setId("role1");
//		role1.setDescription("my role1");
//		role1.setName("example role1");
//
//		role2 = new Role();
//		role2.setId("role2");
//		role2.setDescription("my role2");
//		role2.setName("example role2");
//
//		role3 = new Role();
//		role3.setId("role1");
//		role3.setDescription("my role3");
//		role3.setName("example role3");
//
//		userDomainGrant = new UserDomainGrant();
//		userDomainGrant.setId("userdomaingrant1");
//		userDomainGrant.setDescription("my userdomaingrant1");
//		userDomainGrant.setDomain(domain);
//		userDomainGrant.setUser(user);
//		userDomainGrant.setRole(role1);
//		// userDomainGrantMetadata1 = new UserDomainGrantMetadata();
//		// userDomainGrantMetadata1.setId("userDomainGrantMetadata1");
//		// userDomainGrantMetadata1.setGrant(userDomainGrant);
//		// userDomainGrantMetadata1.setRole(role1);
//		role1.getUserDomainGrants().add(userDomainGrant);
//		// domain.getUserDomainGrants().add(userDomainGrant);
//		// user.getUserDomainGrants().add(userDomainGrant);
//		// userDomainGrant.getMetadatas().add(userDomainGrantMetadata1);
//
//		userProjectGrant = new UserProjectGrant();
//		userProjectGrant.setId("userProjectGrant");
//		userProjectGrant.setDescription("my userProjectGrant");
//		userProjectGrant.setProject(project);
//		userProjectGrant.setUser(user);
//		userProjectGrant.setRole(role2);
//		// userProjectGrantMetadata = new UserProjectGrantMetadata();
//		// userProjectGrantMetadata.setId("userProjectGrantMetadata");
//		// userProjectGrantMetadata.setGrant(userProjectGrant);
//		// userProjectGrantMetadata.setRole(role2);
//		role2.getUserProjectGrants().add(userProjectGrant);
//		// userProjectGrant.getMetadatas().add(userProjectGrantMetadata);
//		// user.getUserProjectGrants().add(userProjectGrant);
//		// project.getUserProjectGrants().add(userProjectGrant);
//
//		userDomainGrant2 = new UserDomainGrant();
//		userDomainGrant2.setId("userdomaingrant2");
//		userDomainGrant2.setDescription("my userdomaingrant2");
//		userDomainGrant2.setDomain(domain2);
//		userDomainGrant2.setUser(user);
//		userDomainGrant2.setRole(role3);
//		// userDomainGrantMetadata2 = new UserDomainGrantMetadata();
//		// userDomainGrantMetadata2.setId("userDomainGrantMetadata2");
//		// userDomainGrantMetadata2.setGrant(userDomainGrant);
//		// userDomainGrantMetadata2.setRole(role3);
//		role3.getUserDomainGrants().add(userDomainGrant2);
//		// user.getUserDomainGrants().add(userDomainGrant2);
//		// domain2.getUserDomainGrants().add(userDomainGrant2);
//		// userDomainGrant2.getMetadatas().add(userDomainGrantMetadata2);
//
//		groupDomainGrant = new GroupDomainGrant();
//		groupDomainGrant.setId("groupdomaingrant1");
//		groupDomainGrant.setDescription("my groupdomaingrant1");
//		groupDomainGrant.setDomain(domain);
//		groupDomainGrant.setGroup(group);
//		groupDomainGrant.setRole(role1);
//		// groupDomainGrantMetadata1 = new GroupDomainGrantMetadata();
//		// groupDomainGrantMetadata1.setId("groupDomainGrantMetadata1");
//		// groupDomainGrantMetadata1.setGrant(groupDomainGrant);
//		// groupDomainGrantMetadata1.setRole(role1);
//		role1.getGroupDomainGrants().add(groupDomainGrant);
//		// group.getGroupDomainGrants().add(groupDomainGrant);
//		// domain.getGroupDomainGrants().add(groupDomainGrant);
//		// groupDomainGrant.getGroupDomainGrantMetadatas().add(groupDomainGrantMetadata1);
//
//		groupProjectGrant = new GroupProjectGrant();
//		groupProjectGrant.setId("groupProjectGrant");
//		groupProjectGrant.setDescription("my groupProjectGrant");
//		groupProjectGrant.setProject(project);
//		groupProjectGrant.setGroup(group);
//		groupProjectGrant.setRole(role2);
//		// groupProjectGrantMetadata = new GroupProjectGrantMetadata();
//		// groupProjectGrantMetadata.setId("groupProjectGrantMetadata");
//		// groupProjectGrantMetadata.setGrant(groupProjectGrant);
//		// groupProjectGrantMetadata.setRole(role2);
//		role2.getGroupProjectGrants().add(groupProjectGrant);
//		// groupProjectGrant.getMetadatas().add(groupProjectGrantMetadata);
//		// group.getGroupProjectGrants().add(groupProjectGrant);
//		// project.getGroupProjectGrants().add(groupProjectGrant);
//
//		groupDomainGrant2 = new GroupDomainGrant();
//		groupDomainGrant2.setId("groupdomaingrant2");
//		groupDomainGrant2.setDescription("my groupdomaingrant2");
//		groupDomainGrant2.setDomain(domain2);
//		groupDomainGrant2.setGroup(group);
//		groupDomainGrant2.setRole(role3);
//		// groupDomainGrantMetadata2 = new GroupDomainGrantMetadata();
//		// groupDomainGrantMetadata2.setId("groupDomainGrantMetadata2");
//		// groupDomainGrantMetadata2.setGrant(groupDomainGrant);
//		// groupDomainGrantMetadata2.setRole(role3);
//		role3.getGroupDomainGrants().add(groupDomainGrant2);
//		// groupDomainGrant2.getGroupDomainGrantMetadatas().add(groupDomainGrantMetadata2);
//		// group.getGroupDomainGrants().add(groupDomainGrant2);
//
//		return new KeystoneApplication();
//	}
//
//	@Test
//	public void testCreateProject() throws JsonGenerationException, JsonMappingException, IOException {
//		ProjectWrapper wrapper = new ProjectWrapper(project, baseUrl);
//		String json = JsonUtils.toJson(wrapper);
//		JsonNode node = JsonUtils.convertToJsonNode(json);
//		JsonNode projectJ = node.get("project");
//		assertEquals(project.getName(), projectJ.get("name").asText());
//		assertEquals(project.getDescription(), projectJ.get("description").asText());
//		assertEquals(project.getDomain().getId(), projectJ.get("domain_id").asText());
//		Response response = target("/v3/projects").register(JacksonFeature.class).register(ObjectMapperResolver.class)
//				.request().header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText())
//				.post(Entity.entity(wrapper, MediaType.APPLICATION_JSON_TYPE));
//		assertEquals(201, response.getStatus());
//
//		node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
//		projectJ = node.get("project");
//		assertNotNull(projectJ.get("id").asText());
//		assertEquals(project.getName(), projectJ.get("name").asText());
//		assertEquals(project.getDescription(), projectJ.get("description").asText());
//		assertEquals(project.getDomain().getId(), projectJ.get("domain_id").asText());
//	}
//
//	@Test
//	public void testListProject() throws JsonProcessingException, IOException {
//		Response response = target("/v3/projects").register(JacksonFeature.class).register(ObjectMapperResolver.class)
//				.request().header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
//		assertEquals(200, response.getStatus());
//		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
//		JsonNode projectsJ = node.get("projects");
//		assertEquals(2, projectsJ.size());
//	}
//
//	@Test
//	public void testGetProject() throws JsonProcessingException, IOException {
//		project.setId("79ea2c65-4679-441f-a596-8aec16752a2f");
//
//		Response response = target("/v3/projects").path(project.getId()).register(JacksonFeature.class)
//				.register(ObjectMapperResolver.class).request()
//				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
//		assertEquals(200, response.getStatus());
//		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
//		JsonNode projectJ = node.get("project");
//		assertEquals("79ea2c65-4679-441f-a596-8aec16752a2f", projectJ.get("id").asText());
//		assertEquals("admin", projectJ.get("name").asText());
//		assertEquals("admin project", projectJ.get("description").asText());
//		assertEquals("default", projectJ.get("domain_id").asText());
//	}
//
//	@Test
//	public void testUpdateProject() throws ClientProtocolException, IOException {
//		project.setId("79ea2c65-4679-441f-a596-8aec16752a2f");
//
//		ProjectWrapper wrapper = new ProjectWrapper(project, baseUrl);
//		PatchClient client = new PatchClient("http://localhost:9998/v3/projects/" + project.getId());
//		JsonNode node = client.connect(wrapper);
//
//		JsonNode projectJ = node.get("project");
//		assertEquals(project.getId(), projectJ.get("id").asText());
//		assertEquals(project.getName(), projectJ.get("name").asText());
//		assertEquals(project.getDescription(), projectJ.get("description").asText());
//		assertEquals(project.getDomainid(), projectJ.get("domain_id").asText());
//	}
//
//	@Test
//	public void testDeleteProject() {
//		project.setId("79ea2c65-4679-441f-a596-8aec16752a2f");
//
//		Response response = target("/v3").path("projects").path(project.getId()).register(JacksonFeature.class)
//				.register(ObjectMapperResolver.class).request()
//				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).delete();
//		assertEquals(204, response.getStatus());
//	}
//
//	@Test
//	public void testGetProjectUsers() throws JsonProcessingException, IOException {
//		project.setId("79ea2c65-4679-441f-a596-8aec16752a2f");
//
//		Response response = target("/v3/projects").path(project.getId()).path("users").register(JacksonFeature.class)
//				.register(ObjectMapperResolver.class).request()
//				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
//		assertEquals(200, response.getStatus());
//		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
//		assertEquals(1, node.size());
//		JsonNode userJ = node.get(0);
//		assertEquals("0f3328f8-a7e7-41b4-830d-be8fdd5186c7", userJ.get("id").asText());
//		assertEquals("79ea2c65-4679-441f-a596-8aec16752a2f", userJ.get("default_project_id").asText());
//		assertEquals("admin", userJ.get("name").asText());
//		assertEquals("admin@keystone4j.com", userJ.get("email").asText());
//		assertEquals("admin user", userJ.get("description").asText());
//		assertEquals("default", userJ.get("domain_id").asText());
//	}
//
//	@Test
//	public void testCreateGrantByUser() {
//		project.setId("79ea2c65-4679-441f-a596-8aec16752a2f");
//		user.setId("0f3328f8-a7e7-41b4-830d-be8fdd5186c8");
//		role1.setId("708bb4f9-9d3c-46af-b18c-7033dc022ffb");
//		Response response = target("/v3/projects").path(project.getId()).path("users").path(user.getId()).path("roles")
//				.path(role1.getId()).register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
//				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText())
//				.put(Entity.json(""));
//		assertEquals(204, response.getStatus());
//	}
//
//	@Test
//	public void testCreateGrantByGroup() {
//		project.setId("79ea2c65-4679-441f-a596-8aec16752a2f");
//		group.setId("708bb4f9-9d3c-46af-b18c-7033dc022ffd");
//		role1.setId("708bb4f9-9d3c-46af-b18c-7033dc022ffb");
//		Response response = target("/v3/projects").path(project.getId()).path("groups").path(group.getId()).path("roles")
//				.path(role1.getId()).register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
//				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText())
//				.put(Entity.json(""));
//		assertEquals(204, response.getStatus());
//	}
//
//	@Test
//	public void testCheckGrantByUser() {
//		project.setId("79ea2c65-4679-441f-a596-8aec16752a2f");
//		user.setId("0f3328f8-a7e7-41b4-830d-be8fdd5186c7");
//		role1.setId("708bb4f9-9d3c-46af-b18c-7033dc022ffb");
//		Response response = target("/v3/projects").path(project.getId()).path("users").path(user.getId()).path("roles")
//				.path(role1.getId()).register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
//				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).head();
//		assertEquals(204, response.getStatus());
//	}
//
//	@Test
//	public void testCheckGrantByGroup() {
//		project.setId("79ea2c65-4679-441f-a596-8aec16752a2f");
//		group.setId("708bb4f9-9d3c-46af-b18c-7033dc022ffd");
//		role1.setId("708bb4f9-9d3c-46af-b18c-7033dc022ffb");
//		Response response = target("/v3/projects").path(project.getId()).path("groups").path(group.getId()).path("roles")
//				.path(role1.getId()).register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
//				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).head();
//		assertEquals(204, response.getStatus());
//	}
//
//	@Test
//	public void testListGrantByUser() throws JsonProcessingException, IOException {
//		project.setId("79ea2c65-4679-441f-a596-8aec16752a2f");
//		user.setId("0f3328f8-a7e7-41b4-830d-be8fdd5186c7");
//
//		Response response = target("/v3/projects").path(project.getId()).path("users").path(user.getId()).path("roles")
//				.register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
//				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
//		assertEquals(200, response.getStatus());
//		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
//		assertEquals(1, node.size());
//		JsonNode roleJ = node.get(0);
//		assertEquals("708bb4f9-9d3c-46af-b18c-7033dc022ffb", roleJ.get("id").asText());
//		assertEquals("admin", roleJ.get("name").asText());
//		assertEquals("admin role", roleJ.get("description").asText());
//	}
//
//	@Test
//	public void testListGrantByGroup() throws JsonProcessingException, IOException {
//		project.setId("79ea2c65-4679-441f-a596-8aec16752a2f");
//		group.setId("708bb4f9-9d3c-46af-b18c-7033dc022ffd");
//		Response response = target("/v3/projects").path(project.getId()).path("groups").path(group.getId()).path("roles")
//				.register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
//				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
//		assertEquals(200, response.getStatus());
//		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
//		assertEquals(1, node.size());
//		JsonNode roleJ = node.get(0);
//		assertEquals("708bb4f9-9d3c-46af-b18c-7033dc022ffb", roleJ.get("id").asText());
//		assertEquals("admin", roleJ.get("name").asText());
//		assertEquals("admin role", roleJ.get("description").asText());
//	}
//
//	@Test
//	public void testRevokeGrantByUser() {
//		project.setId("79ea2c65-4679-441f-a596-8aec16752a2f");
//		user.setId("0f3328f8-a7e7-41b4-830d-be8fdd5186c7");
//		role1.setId("708bb4f9-9d3c-46af-b18c-7033dc022ffb");
//		Response response = target("/v3/projects").path(project.getId()).path("users").path(user.getId()).path("roles")
//				.path(role1.getId()).register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
//				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).delete();
//		assertEquals(204, response.getStatus());
//	}
//
//	@Test
//	public void testRevokeGrantByGroup() {
//		project.setId("79ea2c65-4679-441f-a596-8aec16752a2f");
//		group.setId("708bb4f9-9d3c-46af-b18c-7033dc022ffd");
//		role1.setId("708bb4f9-9d3c-46af-b18c-7033dc022ffb");
//		Response response = target("/v3/projects").path(project.getId()).path("groups").path(group.getId()).path("roles")
//				.path(role1.getId()).register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
//				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).delete();
//		assertEquals(204, response.getStatus());
//	}
//
// }
