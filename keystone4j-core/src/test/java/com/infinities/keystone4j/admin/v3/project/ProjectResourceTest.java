package com.infinities.keystone4j.admin.v3.project;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.client.ClientProtocolException;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.Invocation;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.action.CustomAction;
import org.jmock.lib.concurrent.Synchroniser;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

import com.infinities.keystone4j.JsonUtils;
import com.infinities.keystone4j.ObjectMapperResolver;
import com.infinities.keystone4j.PatchClient;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.assignment.model.GroupDomainGrant;
import com.infinities.keystone4j.assignment.model.GroupDomainGrantMetadata;
import com.infinities.keystone4j.assignment.model.GroupProjectGrant;
import com.infinities.keystone4j.assignment.model.GroupProjectGrantMetadata;
import com.infinities.keystone4j.assignment.model.Project;
import com.infinities.keystone4j.assignment.model.ProjectWrapper;
import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.assignment.model.UserDomainGrant;
import com.infinities.keystone4j.assignment.model.UserDomainGrantMetadata;
import com.infinities.keystone4j.assignment.model.UserProjectGrant;
import com.infinities.keystone4j.assignment.model.UserProjectGrantMetadata;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.model.Group;
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.TrustApi;

public class ProjectResourceTest extends JerseyTest {

	private Mockery context;
	private TokenApi tokenApi;
	private TokenProviderApi tokenProviderApi;
	private AssignmentApi assignmentApi;
	private IdentityApi identityApi;
	private PolicyApi policyApi;
	private TrustApi trustApi;
	private CatalogApi catalogApi;
	private Domain domain, domain2, defaultDomain;
	private User user;
	private Group group;
	private Project project;
	private Role role1, role2, role3;
	private UserDomainGrant userDomainGrant, userDomainGrant2;
	private UserDomainGrantMetadata userDomainGrantMetadata1, userDomainGrantMetadata2;
	private UserProjectGrant userProjectGrant;
	private UserProjectGrantMetadata userProjectGrantMetadata;
	private GroupDomainGrant groupDomainGrant, groupDomainGrant2;
	private GroupDomainGrantMetadata groupDomainGrantMetadata1, groupDomainGrantMetadata2;
	private GroupProjectGrant groupProjectGrant;
	private GroupProjectGrantMetadata groupProjectGrantMetadata;


	@Override
	protected Application configure() {
		context = new JUnit4Mockery() {

			{
				setImposteriser(ClassImposteriser.INSTANCE);
				setThreadingPolicy(new Synchroniser());
			}
		};

		enable(TestProperties.LOG_TRAFFIC);
		enable(TestProperties.DUMP_ENTITY);
		tokenApi = context.mock(TokenApi.class);
		tokenProviderApi = context.mock(TokenProviderApi.class);
		assignmentApi = context.mock(AssignmentApi.class);
		identityApi = context.mock(IdentityApi.class);
		policyApi = context.mock(PolicyApi.class);
		trustApi = context.mock(TrustApi.class);
		catalogApi = context.mock(CatalogApi.class);

		domain = new Domain();
		domain.setId("domain1");
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
		project.setId("project");
		project.setDescription("desc of Project");
		project.setDomain(domain);
		project.setName("my project");

		user = new User();
		user.setId("newuser");
		user.setDescription("my user");
		user.setName("example user");
		user.setDefault_project(project);
		user.setDomain(domain);
		user.setEmail("user@com.tw");

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
		userDomainGrantMetadata1 = new UserDomainGrantMetadata();
		userDomainGrantMetadata1.setId("userDomainGrantMetadata1");
		userDomainGrantMetadata1.setGrant(userDomainGrant);
		userDomainGrantMetadata1.setRole(role1);
		role1.getUserDomainMetadatas().add(userDomainGrantMetadata1);
		userDomainGrant.getMetadatas().add(userDomainGrantMetadata1);

		userProjectGrant = new UserProjectGrant();
		userProjectGrant.setId("userProjectGrant");
		userProjectGrant.setDescription("my userProjectGrant");
		userProjectGrant.setProject(project);
		userProjectGrant.setUser(user);
		userProjectGrantMetadata = new UserProjectGrantMetadata();
		userProjectGrantMetadata.setId("userProjectGrantMetadata");
		userProjectGrantMetadata.setGrant(userProjectGrant);
		userProjectGrantMetadata.setRole(role2);
		role2.getUserProjectMetadatas().add(userProjectGrantMetadata);
		userProjectGrant.getMetadatas().add(userProjectGrantMetadata);
		user.getUserProjectGrants().add(userProjectGrant);
		// project.getUserProjectGrants().add(userProjectGrant);

		userDomainGrant2 = new UserDomainGrant();
		userDomainGrant2.setId("userdomaingrant2");
		userDomainGrant2.setDescription("my userdomaingrant2");
		userDomainGrant2.setDomain(domain2);
		userDomainGrant2.setUser(user);
		userDomainGrantMetadata2 = new UserDomainGrantMetadata();
		userDomainGrantMetadata2.setId("userDomainGrantMetadata2");
		userDomainGrantMetadata2.setGrant(userDomainGrant);
		userDomainGrantMetadata2.setRole(role3);
		role3.getUserDomainMetadatas().add(userDomainGrantMetadata2);
		userDomainGrant2.getMetadatas().add(userDomainGrantMetadata2);

		user.getUserDomainGrants().add(userDomainGrant);
		user.getUserDomainGrants().add(userDomainGrant2);

		groupDomainGrant = new GroupDomainGrant();
		groupDomainGrant.setId("groupdomaingrant1");
		groupDomainGrant.setDescription("my groupdomaingrant1");
		groupDomainGrant.setDomain(domain);
		groupDomainGrant.setGroup(group);
		groupDomainGrantMetadata1 = new GroupDomainGrantMetadata();
		groupDomainGrantMetadata1.setId("groupDomainGrantMetadata1");
		groupDomainGrantMetadata1.setGrant(groupDomainGrant);
		groupDomainGrantMetadata1.setRole(role1);
		role1.getGroupDomainMetadatas().add(groupDomainGrantMetadata1);
		groupDomainGrant.getMetadatas().add(groupDomainGrantMetadata1);

		groupProjectGrant = new GroupProjectGrant();
		groupProjectGrant.setId("groupProjectGrant");
		groupProjectGrant.setDescription("my groupProjectGrant");
		groupProjectGrant.setProject(project);
		groupProjectGrant.setGroup(group);
		groupProjectGrantMetadata = new GroupProjectGrantMetadata();
		groupProjectGrantMetadata.setId("groupProjectGrantMetadata");
		groupProjectGrantMetadata.setGrant(groupProjectGrant);
		groupProjectGrantMetadata.setRole(role2);
		role2.getGroupProjectMetadatas().add(groupProjectGrantMetadata);
		groupProjectGrant.getMetadatas().add(groupProjectGrantMetadata);
		group.getGroupProjectGrants().add(groupProjectGrant);
		// project.getGroupProjectGrants().add(groupProjectGrant);

		groupDomainGrant2 = new GroupDomainGrant();
		groupDomainGrant2.setId("groupdomaingrant2");
		groupDomainGrant2.setDescription("my groupdomaingrant2");
		groupDomainGrant2.setDomain(domain2);
		groupDomainGrant2.setGroup(group);
		groupDomainGrantMetadata2 = new GroupDomainGrantMetadata();
		groupDomainGrantMetadata2.setId("groupDomainGrantMetadata2");
		groupDomainGrantMetadata2.setGrant(groupDomainGrant);
		groupDomainGrantMetadata2.setRole(role3);
		role3.getGroupDomainMetadatas().add(groupDomainGrantMetadata2);
		groupDomainGrant2.getMetadatas().add(groupDomainGrantMetadata2);

		group.getGroupDomainGrants().add(groupDomainGrant);
		group.getGroupDomainGrants().add(groupDomainGrant2);

		return new ProjectResourceTestApplication(catalogApi, tokenApi, tokenProviderApi, assignmentApi, identityApi,
				policyApi, trustApi);

	}

	// @Test
	public void testCreateProject() throws JsonGenerationException, JsonMappingException, IOException {
		final String id = "newproject";
		context.checking(new Expectations() {

			{
				exactly(1).of(assignmentApi).getDomain(defaultDomain.getId());
				will(returnValue(defaultDomain));

				exactly(1).of(assignmentApi).createProject(project);
				will(new CustomAction("add id to project") {

					@Override
					public Object invoke(Invocation invocation) throws Throwable {
						Project project = (Project) invocation.getParameter(0);
						project.setId(id);
						return project;
					}

				});
			}
		});

		ProjectWrapper wrapper = new ProjectWrapper(project);
		String json = JsonUtils.toJson(wrapper);
		JsonNode node = JsonUtils.convertToJsonNode(json);
		JsonNode projectJ = node.get("project");
		assertEquals(project.getName(), projectJ.get("name").asText());
		assertEquals(project.getDescription(), projectJ.get("description").asText());
		assertEquals(project.getDomain().getId(), projectJ.get("domain_id").asText());
		System.out.println(json);
		Response response = target("/v3/projects").register(JacksonFeature.class).register(ObjectMapperResolver.class)
				.request().header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText())
				.post(Entity.entity(wrapper, MediaType.APPLICATION_JSON_TYPE));
		assertEquals(201, response.getStatus());

		node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		projectJ = node.get("project");
		assertEquals(id, projectJ.get("id").asText());
		assertEquals(project.getName(), projectJ.get("name").asText());
		assertEquals(project.getDescription(), projectJ.get("description").asText());
		assertEquals(project.getDomain().getId(), projectJ.get("domain_id").asText());

	}

	@Test
	public void testListProject() throws JsonProcessingException, IOException {
		final List<Project> projects = new ArrayList<Project>();
		project.setId("project1");
		projects.add(project);

		context.checking(new Expectations() {

			{
				exactly(1).of(assignmentApi).listProjects();
				will(returnValue(projects));
			}
		});
		Response response = target("/v3/projects").register(JacksonFeature.class).register(ObjectMapperResolver.class)
				.request().header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());
		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		JsonNode projectsJ = node.get("projects");
		assertEquals(1, projectsJ.size());
		JsonNode projectJ = projectsJ.get(0);
		assertEquals(project.getId(), projectJ.get("id").asText());
		assertEquals(project.getName(), projectJ.get("name").asText());
		assertEquals(project.getDescription(), projectJ.get("description").asText());
		assertEquals(project.getDomainid(), projectJ.get("domain_id").asText());
	}

	@Test
	public void testGetProject() throws JsonProcessingException, IOException {
		project.setId("project1");

		context.checking(new Expectations() {

			{
				exactly(1).of(assignmentApi).getProject(project.getId());
				will(returnValue(project));
			}
		});
		Response response = target("/v3/projects").path(project.getId()).register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());
		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		JsonNode projectJ = node.get("project");
		assertEquals(project.getId(), projectJ.get("id").asText());
		assertEquals(project.getName(), projectJ.get("name").asText());
		assertEquals(project.getDescription(), projectJ.get("description").asText());
		assertEquals(project.getDomainid(), projectJ.get("domain_id").asText());
	}

	@Test
	public void testUpdateProject() throws ClientProtocolException, IOException {
		project.setId("project1");
		context.checking(new Expectations() {

			{
				exactly(1).of(assignmentApi).updateProject(project.getId(), project);
				will(returnValue(project));
			}
		});
		ProjectWrapper wrapper = new ProjectWrapper(project);
		PatchClient client = new PatchClient("http://localhost:9998/v3/projects/" + project.getId());
		JsonNode node = client.connect(wrapper);

		JsonNode projectJ = node.get("project");
		assertEquals(project.getId(), projectJ.get("id").asText());
		assertEquals(project.getName(), projectJ.get("name").asText());
		assertEquals(project.getDescription(), projectJ.get("description").asText());
		assertEquals(project.getDomainid(), projectJ.get("domain_id").asText());
	}

	@Test
	public void testDeleteProject() {
		project.setId("project1");
		context.checking(new Expectations() {

			{
				exactly(1).of(assignmentApi).deleteProject(project.getId());
				will(returnValue(project));
			}
		});
		Response response = target("/v3").path("projects").path(project.getId()).register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).delete();
		assertEquals(204, response.getStatus());
	}

	@Test
	public void testGetProjectUsers() throws JsonProcessingException, IOException {
		project.setId("project1");
		final List<User> users = new ArrayList<User>();
		users.add(user);

		context.checking(new Expectations() {

			{
				exactly(1).of(assignmentApi).listUsersForProject(project.getId());
				will(returnValue(users));
			}
		});
		Response response = target("/v3/projects").path(project.getId()).path("users").register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());
		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		assertEquals(1, node.size());
		JsonNode userJ = node.get(0);
		assertEquals(user.getId(), userJ.get("id").asText());
		assertEquals(user.getDefaultProjectId(), userJ.get("default_project_id").asText());
		assertEquals(user.getName(), userJ.get("name").asText());
		assertEquals(user.getEmail(), userJ.get("email").asText());
		assertEquals(user.getDescription(), userJ.get("description").asText());
		assertEquals(user.getDomainid(), userJ.get("domain_id").asText());
	}

	@Test
	public void testCreateGrantByUser() {
		context.checking(new Expectations() {

			{
				exactly(1).of(identityApi).getUser(user.getId(), null);
				will(returnValue(user));
				exactly(1).of(assignmentApi).createGrantByUserProject(role1.getId(), user.getId(), project.getId());
			}
		});
		Response response = target("/v3/projects").path(project.getId()).path("users").path(user.getId()).path("roles")
				.path(role1.getId()).register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText())
				.put(Entity.json(""));
		assertEquals(204, response.getStatus());
	}

	@Test
	public void testCreateGrantByGroup() {
		context.checking(new Expectations() {

			{
				exactly(1).of(identityApi).getGroup(group.getId(), null);
				will(returnValue(group));
				exactly(1).of(assignmentApi).createGrantByGroupProject(role1.getId(), group.getId(), project.getId());
			}
		});
		Response response = target("/v3/projects").path(project.getId()).path("groups").path(group.getId()).path("roles")
				.path(role1.getId()).register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText())
				.put(Entity.json(""));
		assertEquals(204, response.getStatus());
	}

	@Test
	public void testCheckGrantByUser() {
		context.checking(new Expectations() {

			{
				exactly(1).of(identityApi).getUser(user.getId(), null);
				will(returnValue(user));
				exactly(1).of(assignmentApi).getGrantByUserProject(role1.getId(), user.getId(), project.getId(),
						Config.Instance.getOpt(Config.Type.os_inherit, "enabled").asBoolean());
			}
		});
		Response response = target("/v3/projects").path(project.getId()).path("users").path(user.getId()).path("roles")
				.path(role1.getId()).register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).head();
		assertEquals(204, response.getStatus());
	}

	@Test
	public void testCheckGrantByGroup() {
		context.checking(new Expectations() {

			{
				exactly(1).of(identityApi).getGroup(group.getId(), null);
				will(returnValue(group));
				exactly(1).of(assignmentApi).getGrantByGroupProject(role1.getId(), group.getId(), project.getId(),
						Config.Instance.getOpt(Config.Type.os_inherit, "enabled").asBoolean());
			}
		});
		Response response = target("/v3/projects").path(project.getId()).path("groups").path(group.getId()).path("roles")
				.path(role1.getId()).register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).head();
		assertEquals(204, response.getStatus());
	}

	@Test
	public void testListGrantByUser() throws JsonProcessingException, IOException {
		project.setId("project1");
		final List<Role> roles = new ArrayList<Role>();
		roles.add(role1);

		context.checking(new Expectations() {

			{
				exactly(1).of(assignmentApi).listGrantsByUserProject(user.getId(), project.getId(),
						Config.Instance.getOpt(Config.Type.os_inherit, "enabled").asBoolean());
				will(returnValue(roles));
			}
		});
		Response response = target("/v3/projects").path(project.getId()).path("users").path(user.getId()).path("roles")
				.register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());
		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		assertEquals(1, node.size());
		JsonNode roleJ = node.get(0);
		assertEquals(role1.getId(), roleJ.get("id").asText());
		assertEquals(role1.getName(), roleJ.get("name").asText());
		assertEquals(role1.getDescription(), roleJ.get("description").asText());
	}

	@Test
	public void testListGrantByGroup() throws JsonProcessingException, IOException {
		project.setId("project1");
		final List<Role> roles = new ArrayList<Role>();
		roles.add(role1);

		context.checking(new Expectations() {

			{
				exactly(1).of(assignmentApi).listGrantsByGroupProject(group.getId(), project.getId(),
						Config.Instance.getOpt(Config.Type.os_inherit, "enabled").asBoolean());
				will(returnValue(roles));
			}
		});
		Response response = target("/v3/projects").path(project.getId()).path("groups").path(group.getId()).path("roles")
				.register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());
		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		assertEquals(1, node.size());
		JsonNode roleJ = node.get(0);
		assertEquals(role1.getId(), roleJ.get("id").asText());
		assertEquals(role1.getName(), roleJ.get("name").asText());
		assertEquals(role1.getDescription(), roleJ.get("description").asText());
	}

	@Test
	public void testRevokeGrantByUser() {
		context.checking(new Expectations() {

			{
				exactly(1).of(identityApi).getUser(user.getId(), null);
				will(returnValue(user));
				exactly(1).of(assignmentApi).deleteGrantByUserProject(role1.getId(), user.getId(), project.getId(),
						Config.Instance.getOpt(Config.Type.os_inherit, "enabled").asBoolean());
			}
		});
		Response response = target("/v3/projects").path(project.getId()).path("users").path(user.getId()).path("roles")
				.path(role1.getId()).register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).delete();
		assertEquals(204, response.getStatus());
	}

	@Test
	public void testRevokeGrantByGroup() {
		context.checking(new Expectations() {

			{
				exactly(1).of(identityApi).getGroup(group.getId(), null);
				will(returnValue(group));
				exactly(1).of(assignmentApi).deleteGrantByGroupProject(role1.getId(), group.getId(), project.getId(),
						Config.Instance.getOpt(Config.Type.os_inherit, "enabled").asBoolean());
			}
		});
		Response response = target("/v3/projects").path(project.getId()).path("groups").path(group.getId()).path("roles")
				.path(role1.getId()).register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).delete();
		assertEquals(204, response.getStatus());
	}

}
