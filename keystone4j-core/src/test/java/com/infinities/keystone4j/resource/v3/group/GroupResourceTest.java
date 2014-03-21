package com.infinities.keystone4j.resource.v3.group;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.client.ClientProtocolException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
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
import com.infinities.keystone4j.Views;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.assignment.model.GroupDomainGrant;
import com.infinities.keystone4j.assignment.model.GroupDomainGrantMetadata;
import com.infinities.keystone4j.assignment.model.GroupProjectGrant;
import com.infinities.keystone4j.assignment.model.GroupProjectGrantMetadata;
import com.infinities.keystone4j.assignment.model.Project;
import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.assignment.model.UserDomainGrant;
import com.infinities.keystone4j.assignment.model.UserDomainGrantMetadata;
import com.infinities.keystone4j.assignment.model.UserProjectGrant;
import com.infinities.keystone4j.assignment.model.UserProjectGrantMetadata;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.model.Group;
import com.infinities.keystone4j.identity.model.GroupWrapper;
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.identity.model.UserGroupMembership;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.TrustApi;

public class GroupResourceTest extends JerseyTest {

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
	private UserGroupMembership userGroupMembership;


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
		// user.setId("newuser");
		user.setDescription("my user");
		user.setName("example user");
		user.setDefault_project(project);
		user.setDomain(domain);
		user.setEmail("user@com.tw");
		user.setPassword("password");

		group = new Group();
		// group.setId("newgroup");
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
		// user.getUserProjectGrants().add(userProjectGrant);
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

		// user.getUserDomainGrants().add(userDomainGrant);
		// user.getUserDomainGrants().add(userDomainGrant2);

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
		// group.getGroupProjectGrants().add(groupProjectGrant);
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

		// group.getGroupDomainGrants().add(groupDomainGrant);
		// group.getGroupDomainGrants().add(groupDomainGrant2);
		userGroupMembership = new UserGroupMembership();
		userGroupMembership.setDescription("my usergroupmembership");
		userGroupMembership.setId("newgroupmembership");
		userGroupMembership.setUser(user);
		userGroupMembership.setGroup(group);

		// group.getUserGroupMemberships().add(userGroupMembership);

		return new GroupResourceTestApplication(catalogApi, tokenApi, tokenProviderApi, assignmentApi, identityApi,
				policyApi, trustApi);
	}

	@Test
	public void testCreateGroup() throws JsonProcessingException, IOException {
		final String id = "newgroup";
		context.checking(new Expectations() {

			{
				exactly(1).of(assignmentApi).getDomain(defaultDomain.getId());
				will(returnValue(defaultDomain));

				exactly(1).of(identityApi).createGroup(group);
				will(new CustomAction("add id to group") {

					@Override
					public Object invoke(Invocation invocation) throws Throwable {
						Group group = (Group) invocation.getParameter(0);
						group.setId(id);
						return group;
					}

				});
			}
		});

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
		groupJ = node.get("group");
		assertEquals(id, groupJ.get("id").asText());
		assertEquals(group.getName(), groupJ.get("name").asText());
		assertEquals(group.getDescription(), groupJ.get("description").asText());
		assertEquals(group.getDomain().getId(), groupJ.get("domain_id").asText());
	}

	@Test
	public void testListGroups() throws JsonProcessingException, IOException {
		final List<Group> groups = new ArrayList<Group>();
		group.setDomain(defaultDomain);
		group.setId("group1");
		groups.add(group);

		context.checking(new Expectations() {

			{
				exactly(1).of(assignmentApi).getDomain(
						Config.Instance.getOpt(Config.Type.identity, "default_domain_id").asText());
				will(returnValue(defaultDomain));
				exactly(1).of(identityApi).listGroups(defaultDomain.getId());
				will(returnValue(groups));
			}
		});
		Response response = target("/v3/groups").register(JacksonFeature.class).register(ObjectMapperResolver.class)
				.request().header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());
		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		JsonNode groupsJ = node.get("groups");
		assertEquals(1, groupsJ.size());
		JsonNode groupJ = groupsJ.get(0);
		assertEquals(group.getId(), groupJ.get("id").asText());
		assertEquals(group.getName(), groupJ.get("name").asText());
		assertEquals(group.getDescription(), groupJ.get("description").asText());
		assertEquals(group.getDomain().getId(), groupJ.get("domain_id").asText());
	}

	@Test
	public void testGetGroup() throws JsonProcessingException, IOException {
		group.setDomain(defaultDomain);
		group.setId("group1");

		context.checking(new Expectations() {

			{
				exactly(1).of(assignmentApi).getDomain(
						Config.Instance.getOpt(Config.Type.identity, "default_domain_id").asText());
				will(returnValue(defaultDomain));
				exactly(1).of(identityApi).getGroup(group.getId(), defaultDomain.getId());
				will(returnValue(group));
			}
		});
		Response response = target("/v3/groups").path(group.getId()).register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());
		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		JsonNode groupJ = node.get("group");
		assertEquals(group.getId(), groupJ.get("id").asText());
		assertEquals(group.getName(), groupJ.get("name").asText());
		assertEquals(group.getDescription(), groupJ.get("description").asText());
		assertEquals(group.getDomain().getId(), groupJ.get("domain_id").asText());
	}

	@Test
	public void testUpdateGroup() throws ClientProtocolException, IOException {
		group.setDomain(defaultDomain);
		group.setId("group1");

		context.checking(new Expectations() {

			{
				exactly(1).of(assignmentApi).getDomain(
						Config.Instance.getOpt(Config.Type.identity, "default_domain_id").asText());
				will(returnValue(defaultDomain));
				exactly(1).of(identityApi).updateGroup(group.getId(), group, defaultDomain.getId());
				will(returnValue(group));
			}
		});
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
		group.setDomain(defaultDomain);
		group.setId("group1");
		context.checking(new Expectations() {

			{
				exactly(1).of(assignmentApi).getDomain(defaultDomain.getId());
				will(returnValue(defaultDomain));

				exactly(1).of(identityApi).deleteGroup(group.getId(), defaultDomain.getId());
				will(returnValue(group));
			}
		});

		Response response = target("/v3/groups").path(group.getId()).register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).delete();
		assertEquals(204, response.getStatus());
	}

	@Test
	public void testListUsersInGroup() throws JsonProcessingException, IOException {
		group.setDomain(defaultDomain);
		group.setId("group1");
		user.setId("newuser");
		final List<User> users = new ArrayList<User>();
		users.add(user);
		context.checking(new Expectations() {

			{
				exactly(1).of(assignmentApi).getDomain(defaultDomain.getId());
				will(returnValue(defaultDomain));

				exactly(1).of(identityApi).listUsersInGroup(group.getId(), defaultDomain.getId());
				will(returnValue(users));
			}
		});

		Response response = target("/v3/groups").path(group.getId()).path("users").register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());
		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		JsonNode usersJ = node.get("users");
		assertEquals(1, usersJ.size());
		JsonNode userJ = usersJ.get(0);
		assertEquals(user.getId(), userJ.get("id").asText());
		assertEquals(user.getName(), userJ.get("name").asText());
		assertEquals(user.getDescription(), userJ.get("description").asText());
		assertEquals(user.getDomain().getId(), userJ.get("domain_id").asText());
		assertEquals(user.getDefaultProjectId(), userJ.get("default_project_id").asText());
		assertEquals(user.getEmail(), userJ.get("email").asText());
		assertNull(userJ.get("password"));
	}

	@Test
	public void testAddUserToGroup() {
		group.setDomain(defaultDomain);
		group.setId("group1");
		user.setId("newuser");
		context.checking(new Expectations() {

			{
				exactly(1).of(assignmentApi).getDomain(defaultDomain.getId());
				will(returnValue(defaultDomain));

				exactly(1).of(identityApi).addUserToGroup(user.getId(), group.getId(), defaultDomain.getId());
				will(returnValue(user));
			}
		});
		Response response = target("/v3/groups").path(group.getId()).path("users").path(user.getId())
				.register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText())
				.put(Entity.json(""));
		assertEquals(204, response.getStatus());
	}

	@Test
	public void testCheckUserInGroup() {
		group.setDomain(defaultDomain);
		group.setId("group1");
		user.setId("newuser");
		context.checking(new Expectations() {

			{
				exactly(1).of(assignmentApi).getDomain(defaultDomain.getId());
				will(returnValue(defaultDomain));

				exactly(1).of(identityApi).checkUserInGroup(user.getId(), group.getId(), defaultDomain.getId());
				will(returnValue(user));
			}
		});
		Response response = target("/v3/groups").path(group.getId()).path("users").path(user.getId())
				.register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).head();
		assertEquals(204, response.getStatus());
	}

	@Test
	public void testRemoveUserFromGroup() {
		group.setDomain(defaultDomain);
		group.setId("group1");
		user.setId("newuser");
		context.checking(new Expectations() {

			{
				exactly(1).of(assignmentApi).getDomain(defaultDomain.getId());
				will(returnValue(defaultDomain));

				exactly(1).of(identityApi).removeUserFromGroup(user.getId(), group.getId(), defaultDomain.getId());
				will(returnValue(user));
			}
		});
		Response response = target("/v3/groups").path(group.getId()).path("users").path(user.getId())
				.register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).delete();
		assertEquals(204, response.getStatus());
	}

}
