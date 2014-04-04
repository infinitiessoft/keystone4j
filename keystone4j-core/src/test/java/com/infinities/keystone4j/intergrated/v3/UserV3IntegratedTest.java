package com.infinities.keystone4j.intergrated.v3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
import com.infinities.keystone4j.KeystoneApplication;
import com.infinities.keystone4j.PatchClient;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.assignment.GroupDomainGrant;
import com.infinities.keystone4j.model.assignment.GroupProjectGrant;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.assignment.UserDomainGrant;
import com.infinities.keystone4j.model.assignment.UserProjectGrant;
import com.infinities.keystone4j.model.identity.Group;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.model.identity.UserGroupMembership;
import com.infinities.keystone4j.model.identity.UserParam;
import com.infinities.keystone4j.model.identity.UserParamWrapper;
import com.infinities.keystone4j.model.identity.UserWrapper;
import com.infinities.keystone4j.model.utils.Views;
import com.infinities.keystone4j.utils.jackson.JacksonFeature;
import com.infinities.keystone4j.utils.jackson.JsonUtils;
import com.infinities.keystone4j.utils.jackson.ObjectMapperResolver;

public class UserV3IntegratedTest extends AbstractIntegratedTest {

	// private Mockery context;
	// private TokenApi tokenApi;
	// private TokenProviderApi tokenProviderApi;
	// private AssignmentApi assignmentApi;
	// private IdentityApi identityApi;
	// private PolicyApi policyApi;
	// private TrustApi trustApi;
	// private CatalogApi catalogApi;
	private Domain domain, domain2, defaultDomain;
	private User user;
	private Group group;
	private Project project;
	private Role role1, role2, role3;
	private UserDomainGrant userDomainGrant, userDomainGrant2;
	// private UserDomainGrantMetadata userDomainGrantMetadata1,
	// userDomainGrantMetadata2;
	private UserProjectGrant userProjectGrant;
	// private UserProjectGrantMetadata userProjectGrantMetadata;
	private GroupDomainGrant groupDomainGrant, groupDomainGrant2;
	// private GroupDomainGrantMetadata groupDomainGrantMetadata1,
	// groupDomainGrantMetadata2;
	private GroupProjectGrant groupProjectGrant;
	// private GroupProjectGrantMetadata groupProjectGrantMetadata;
	private UserGroupMembership userGroupMembership;
	private final String baseUrl = "http://localhost:8080/v3/users";


	@Override
	protected Application configure() {

		enable(TestProperties.LOG_TRAFFIC);
		enable(TestProperties.DUMP_ENTITY);

		domain = new Domain();
		domain.setId("default");
		domain.setDescription("desc of Domain");
		domain.setName("my domain");

		domain2 = new Domain();
		domain2.setId("domain2");
		domain2.setDescription("desc of Domain2");
		domain2.setName("my domain2");

		defaultDomain = new Domain();
		defaultDomain.setId("default");
		defaultDomain.setDescription("desc of default Domain");
		defaultDomain.setName("my default domain");

		project = new Project();
		project.setId("79ea2c65-4679-441f-a596-8aec16752a2f");
		project.setDescription("desc of Project");
		project.setDomain(domain);
		project.setName("my project");

		user = new User();
		// user.setId("newuser");
		user.setDescription("my user");
		user.setName("example user");
		user.setDefault_project(project);
		user.setDomain(domain);
		user.setEmail("user@com.tw");
		user.setPassword("password");

		group = new Group();
		group.setId("newgroup");
		group.setDescription("my group");
		group.setName("example group");
		group.setDomain(domain);

		role1 = new Role();
		role1.setId("role1");
		role1.setDescription("my role1");
		role1.setName("example role1");

		role2 = new Role();
		role2.setId("role2");
		role2.setDescription("my role2");
		role2.setName("example role2");

		role3 = new Role();
		role3.setId("role1");
		role3.setDescription("my role3");
		role3.setName("example role3");

		userDomainGrant = new UserDomainGrant();
		userDomainGrant.setId("userdomaingrant1");
		userDomainGrant.setDescription("my userdomaingrant1");
		userDomainGrant.setDomain(domain);
		userDomainGrant.setUser(user);
		userDomainGrant.setRole(role1);
		// userDomainGrantMetadata1 = new UserDomainGrantMetadata();
		// userDomainGrantMetadata1.setId("userDomainGrantMetadata1");
		// userDomainGrantMetadata1.setGrant(userDomainGrant);
		// userDomainGrantMetadata1.setRole(role1);
		// role1.getUserDomainGrants().add(userDomainGrant);
		// domain.getUserDomainGrants().add(userDomainGrant);
		// user.getUserDomainGrants().add(userDomainGrant);
		// userDomainGrant.getMetadatas().add(userDomainGrantMetadata1);

		userProjectGrant = new UserProjectGrant();
		userProjectGrant.setId("userProjectGrant");
		userProjectGrant.setDescription("my userProjectGrant");
		userProjectGrant.setProject(project);
		userProjectGrant.setUser(user);
		userProjectGrant.setRole(role2);
		// userProjectGrantMetadata = new UserProjectGrantMetadata();
		// userProjectGrantMetadata.setId("userProjectGrantMetadata");
		// userProjectGrantMetadata.setGrant(userProjectGrant);
		// userProjectGrantMetadata.setRole(role2);
		// role2.getUserProjectGrants().add(userProjectGrant);
		// userProjectGrant.getMetadatas().add(userProjectGrantMetadata);
		// user.getUserProjectGrants().add(userProjectGrant);
		// project.getUserProjectGrants().add(userProjectGrant);

		userDomainGrant2 = new UserDomainGrant();
		userDomainGrant2.setId("userdomaingrant2");
		userDomainGrant2.setDescription("my userdomaingrant2");
		userDomainGrant2.setDomain(domain2);
		userDomainGrant2.setUser(user);
		userDomainGrant2.setRole(role3);
		// userDomainGrantMetadata2 = new UserDomainGrantMetadata();
		// userDomainGrantMetadata2.setId("userDomainGrantMetadata2");
		// userDomainGrantMetadata2.setGrant(userDomainGrant);
		// userDomainGrantMetadata2.setRole(role3);
		role3.getUserDomainGrants().add(userDomainGrant2);
		// user.getUserDomainGrants().add(userDomainGrant2);
		// domain2.getUserDomainGrants().add(userDomainGrant2);
		// userDomainGrant2.getMetadatas().add(userDomainGrantMetadata2);

		groupDomainGrant = new GroupDomainGrant();
		groupDomainGrant.setId("groupdomaingrant1");
		groupDomainGrant.setDescription("my groupdomaingrant1");
		groupDomainGrant.setDomain(domain);
		groupDomainGrant.setGroup(group);
		groupDomainGrant.setRole(role1);
		// groupDomainGrantMetadata1 = new GroupDomainGrantMetadata();
		// groupDomainGrantMetadata1.setId("groupDomainGrantMetadata1");
		// groupDomainGrantMetadata1.setGrant(groupDomainGrant);
		// groupDomainGrantMetadata1.setRole(role1);
		// role1.getGroupDomainGrants().add(groupDomainGrant);
		// group.getGroupDomainGrants().add(groupDomainGrant);
		// domain.getGroupDomainGrants().add(groupDomainGrant);
		// groupDomainGrant.getGroupDomainGrantMetadatas().add(groupDomainGrantMetadata1);

		groupProjectGrant = new GroupProjectGrant();
		groupProjectGrant.setId("groupProjectGrant");
		groupProjectGrant.setDescription("my groupProjectGrant");
		groupProjectGrant.setProject(project);
		groupProjectGrant.setGroup(group);
		groupProjectGrant.setRole(role2);
		// groupProjectGrantMetadata = new GroupProjectGrantMetadata();
		// groupProjectGrantMetadata.setId("groupProjectGrantMetadata");
		// groupProjectGrantMetadata.setGrant(groupProjectGrant);
		// groupProjectGrantMetadata.setRole(role2);
		// role2.getGroupProjectGrants().add(groupProjectGrant);
		// groupProjectGrant.getMetadatas().add(groupProjectGrantMetadata);
		// group.getGroupProjectGrants().add(groupProjectGrant);
		// project.getGroupProjectGrants().add(groupProjectGrant);

		groupDomainGrant2 = new GroupDomainGrant();
		groupDomainGrant2.setId("groupdomaingrant2");
		groupDomainGrant2.setDescription("my groupdomaingrant2");
		groupDomainGrant2.setDomain(domain2);
		groupDomainGrant2.setGroup(group);
		groupDomainGrant2.setRole(role3);
		// groupDomainGrantMetadata2 = new GroupDomainGrantMetadata();
		// groupDomainGrantMetadata2.setId("groupDomainGrantMetadata2");
		// groupDomainGrantMetadata2.setGrant(groupDomainGrant);
		// groupDomainGrantMetadata2.setRole(role3);
		// role3.getGroupDomainGrants().add(groupDomainGrant2);
		// groupDomainGrant2.getGroupDomainGrantMetadatas().add(groupDomainGrantMetadata2);
		// group.getGroupDomainGrants().add(groupDomainGrant2);

		userGroupMembership = new UserGroupMembership();
		userGroupMembership.setDescription("my usergroupmembership");
		userGroupMembership.setId("newgroupmembership");
		userGroupMembership.setUser(user);
		userGroupMembership.setGroup(group);

		group.getUserGroupMemberships().add(userGroupMembership);

		return new KeystoneApplication();

	}

	@Test
	public void testListUserProjects() throws JsonProcessingException, IOException {
		user.setId("0f3328f8-a7e7-41b4-830d-be8fdd5186c7");

		Response response = target("/v3/users").path(user.getId()).path("projects").register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());
		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		JsonNode projectsJ = node.get("projects");
		assertEquals(1, projectsJ.size());
		JsonNode projectJ = projectsJ.get(0);
		assertEquals("79ea2c65-4679-441f-a596-8aec16752a2f", projectJ.get("id").asText());
		assertEquals("admin", projectJ.get("name").asText());
		assertEquals("admin project", projectJ.get("description").asText());
		assertEquals("default", projectJ.get("domain_id").asText());
	}

	@Test
	public void testCreateUser() throws JsonGenerationException, JsonMappingException, IOException {
		// final String id = "newuser";
		// user.setId("");

		UserWrapper wrapper = new UserWrapper(user, baseUrl);
		String json = JsonUtils.toJson(wrapper, Views.Advance.class);
		JsonNode node = JsonUtils.convertToJsonNode(json);
		JsonNode userJ = node.get("user");
		assertEquals(user.getName(), userJ.get("name").asText());
		assertEquals(user.getDescription(), userJ.get("description").asText());
		assertEquals(user.getDomain().getId(), userJ.get("domain_id").asText());
		// assertEquals(user.getDefaultProjectId(),
		// userJ.get("default_project_id").asText());
		assertEquals(user.getEmail(), userJ.get("email").asText());
		assertEquals(user.getPassword(), userJ.get("password").asText());
		Response response = target("/v3/users").register(JacksonFeature.class).register(ObjectMapperResolver.class)
				.request().header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText())
				.post(Entity.entity(wrapper, MediaType.APPLICATION_JSON_TYPE));
		assertEquals(201, response.getStatus());

		node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		userJ = node.get("user");
		assertNotNull(userJ.get("id").asText());
		assertEquals(user.getName(), userJ.get("name").asText());
		assertEquals(user.getDescription(), userJ.get("description").asText());
		assertEquals(user.getDomain().getId(), userJ.get("domain_id").asText());
		assertEquals(user.getDefaultProjectId(), userJ.get("default_project_id").asText());
		assertEquals(user.getEmail(), userJ.get("email").asText());
		assertNull(userJ.get("password"));

	}

	@Test
	public void testListUsers() throws JsonProcessingException, IOException {
		Response response = target("/v3/users").register(JacksonFeature.class).register(ObjectMapperResolver.class)
				.request().header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());
		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		JsonNode usersJ = node.get("users");
		assertEquals(2, usersJ.size());
	}

	@Test
	public void testGetUser() throws JsonGenerationException, JsonMappingException, IOException {
		final String id = "0f3328f8-a7e7-41b4-830d-be8fdd5186c7";
		user.setId(id);

		Response response = target("/v3/users").path(user.getId()).register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());

		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		JsonNode userJ = node.get("user");
		assertEquals(id, userJ.get("id").asText());
		assertEquals("admin", userJ.get("name").asText());
		assertEquals("admin user", userJ.get("description").asText());
		assertEquals("default", userJ.get("domain_id").asText());
		assertEquals("79ea2c65-4679-441f-a596-8aec16752a2f", userJ.get("default_project_id").asText());
		assertEquals("admin@keystone4j.com", userJ.get("email").asText());
		assertNull(userJ.get("password"));

	}

	@Test
	public void testUpdateUser() throws ClientProtocolException, IOException {
		final String id = "0f3328f8-a7e7-41b4-830d-be8fdd5186c7";
		user.setId(id);

		UserWrapper wrapper = new UserWrapper(user, baseUrl);
		PatchClient client = new PatchClient("http://localhost:9998/v3/users/" + user.getId());
		JsonNode node = client.connect(wrapper);

		JsonNode userJ = node.get("user");
		assertEquals(id, userJ.get("id").asText());
		assertEquals(user.getName(), userJ.get("name").asText());
		assertEquals(user.getDescription(), userJ.get("description").asText());
		assertEquals(user.getDomain().getId(), userJ.get("domain_id").asText());
		assertEquals(user.getDefaultProjectId(), userJ.get("default_project_id").asText());
		assertEquals(user.getEmail(), userJ.get("email").asText());
		assertNull(userJ.get("password"));
	}

	@Test
	public void testDeleteUser() {
		final String id = "0f3328f8-a7e7-41b4-830d-be8fdd5186c7";
		user.setId(id);

		Response response = target("/v3/users").path(user.getId()).register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).delete();
		assertEquals(204, response.getStatus());
	}

	@Test
	public void testChangePassword() throws JsonGenerationException, JsonMappingException, IOException {
		final String id = "0f3328f8-a7e7-41b4-830d-be8fdd5186c7";
		user.setId(id);
		final UserParam param = new UserParam();
		param.setPassword("admin2");
		param.setOriginalPassword("admin");

		UserParamWrapper wrapper = new UserParamWrapper(param);
		String json = JsonUtils.toJson(wrapper, Views.Advance.class);
		JsonNode node = JsonUtils.convertToJsonNode(json);
		JsonNode userJ = node.get("user");
		assertEquals(param.getPassword(), userJ.get("password").asText());
		assertEquals(param.getOriginalPassword(), userJ.get("original_password").asText());
		Response response = target("/v3/users").path(user.getId()).path("password").register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText())
				.post(Entity.entity(wrapper, MediaType.APPLICATION_JSON_TYPE));
		assertEquals(204, response.getStatus());
	}

	@Test
	public void testListGroupsForUser() throws JsonProcessingException, IOException {
		user.setId("0f3328f8-a7e7-41b4-830d-be8fdd5186c8");

		Response response = target("/v3/users").path(user.getId()).path("groups").register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());
		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		JsonNode groupsJ = node.get("groups");
		assertEquals(1, groupsJ.size());
		JsonNode groupJ = groupsJ.get(0);
		assertEquals("708bb4f9-9d3c-46af-b18c-7033dc022ffd", groupJ.get("id").asText());
		assertEquals("demo_group", groupJ.get("name").asText());
		assertEquals("demo group", groupJ.get("description").asText());
		assertEquals("default", groupJ.get("domain_id").asText());
	}
}
