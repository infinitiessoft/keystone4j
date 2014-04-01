package com.infinities.keystone4j.resource.v3.domain;

import static org.junit.Assert.assertEquals;

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
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.Invocation;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.action.CustomAction;
import org.jmock.lib.concurrent.Synchroniser;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.infinities.keystone4j.PatchClient;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.assignment.model.DomainWrapper;
import com.infinities.keystone4j.assignment.model.GroupDomainGrant;
import com.infinities.keystone4j.assignment.model.GroupProjectGrant;
import com.infinities.keystone4j.assignment.model.Project;
import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.assignment.model.UserDomainGrant;
import com.infinities.keystone4j.assignment.model.UserProjectGrant;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.model.Group;
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.TrustApi;
import com.infinities.keystone4j.utils.jackson.JacksonFeature;
import com.infinities.keystone4j.utils.jackson.JsonUtils;
import com.infinities.keystone4j.utils.jackson.ObjectMapperResolver;

public class DomainResourceTest extends JerseyTest {

	private Mockery context;
	private TokenApi tokenApi;
	private TokenProviderApi tokenProviderApi;
	private AssignmentApi assignmentApi;
	private IdentityApi identityApi;
	private PolicyApi policyApi;
	private TrustApi trustApi;
	private CatalogApi catalogApi;
	private Domain domain, domain2;
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
		role1.getUserDomainGrants().add(userDomainGrant);
		// domain.getUserDomainGrants().add(userDomainGrant);
		user.getUserDomainGrants().add(userDomainGrant);
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
		role2.getUserProjectGrants().add(userProjectGrant);
		// userProjectGrant.getMetadatas().add(userProjectGrantMetadata);
		user.getUserProjectGrants().add(userProjectGrant);
		project.getUserProjectGrants().add(userProjectGrant);

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
		user.getUserDomainGrants().add(userDomainGrant2);
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
		role1.getGroupDomainGrants().add(groupDomainGrant);
		group.getGroupDomainGrants().add(groupDomainGrant);
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
		role2.getGroupProjectGrants().add(groupProjectGrant);
		// groupProjectGrant.getMetadatas().add(groupProjectGrantMetadata);
		group.getGroupProjectGrants().add(groupProjectGrant);
		project.getGroupProjectGrants().add(groupProjectGrant);

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
		role3.getGroupDomainGrants().add(groupDomainGrant2);
		// groupDomainGrant2.getGroupDomainGrantMetadatas().add(groupDomainGrantMetadata2);
		group.getGroupDomainGrants().add(groupDomainGrant2);

		return new DomainResourceTestApplication(catalogApi, tokenApi, tokenProviderApi, assignmentApi, identityApi,
				policyApi, trustApi);

	}

	@Test
	public void testCreateDomain() throws JsonProcessingException, IOException {
		final String id = "newdomain";
		context.checking(new Expectations() {

			{
				exactly(1).of(assignmentApi).createDomain(domain);
				will(new CustomAction("add id to domain") {

					@Override
					public Object invoke(Invocation invocation) throws Throwable {
						Domain domain = (Domain) invocation.getParameter(0);
						domain.setId(id);
						return domain;
					}

				});
			}
		});

		DomainWrapper wrapper = new DomainWrapper(domain);
		String json = JsonUtils.toJson(wrapper);
		JsonNode node = JsonUtils.convertToJsonNode(json);
		JsonNode domainJ = node.get("domain");
		assertEquals(domain.getName(), domainJ.get("name").asText());
		assertEquals(domain.getDescription(), domainJ.get("description").asText());

		Response response = target("/v3/domains").register(JacksonFeature.class).register(ObjectMapperResolver.class)
				.request().header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText())
				.post(Entity.entity(wrapper, MediaType.APPLICATION_JSON_TYPE));
		assertEquals(201, response.getStatus());

		node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		domainJ = node.get("domain");
		assertEquals(id, domainJ.get("id").asText());
		assertEquals(domain.getName(), domainJ.get("name").asText());
		assertEquals(domain.getDescription(), domainJ.get("description").asText());
	}

	@Test
	public void testListDomain() throws JsonProcessingException, IOException {
		final List<Domain> domains = new ArrayList<Domain>();
		domain.setId("domain1");
		domains.add(domain);

		context.checking(new Expectations() {

			{
				exactly(1).of(assignmentApi).listDomains();
				will(returnValue(domains));
			}
		});
		Response response = target("/v3/domains").register(JacksonFeature.class).register(ObjectMapperResolver.class)
				.request().header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());
		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		JsonNode domainsJ = node.get("domains");
		assertEquals(1, domainsJ.size());
		JsonNode domainJ = domainsJ.get(0);
		assertEquals(domain.getId(), domainJ.get("id").asText());
		assertEquals(domain.getName(), domainJ.get("name").asText());
		assertEquals(domain.getDescription(), domainJ.get("description").asText());
	}

	@Test
	public void testGetDomain() throws JsonProcessingException, IOException {
		domain.setId("domain1");
		context.checking(new Expectations() {

			{
				exactly(1).of(assignmentApi).getDomain(domain.getId());
				will(returnValue(domain));
			}
		});
		Response response = target("/v3").path("domains").path(domain.getId()).register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());
		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		JsonNode domainJ = node.get("domain");
		assertEquals(domain.getId(), domainJ.get("id").asText());
		assertEquals(domain.getName(), domainJ.get("name").asText());
		assertEquals(domain.getDescription(), domainJ.get("description").asText());
	}

	@Test
	public void testUpdateDomain() throws ClientProtocolException, IOException {
		domain.setId("domain1");
		context.checking(new Expectations() {

			{
				exactly(1).of(assignmentApi).updateDomain(domain.getId(), domain);
				will(returnValue(domain));
			}
		});
		DomainWrapper wrapper = new DomainWrapper(domain);
		PatchClient client = new PatchClient("http://localhost:9998/v3/domains/" + domain.getId());
		JsonNode node = client.connect(wrapper);

		JsonNode domainJ = node.get("domain");
		assertEquals(domain.getId(), domainJ.get("id").asText());
		assertEquals(domain.getName(), domainJ.get("name").asText());
		assertEquals(domain.getDescription(), domainJ.get("description").asText());
	}

	@Test
	public void testDeleteDomain() {
		domain.setId("domain1");
		context.checking(new Expectations() {

			{
				exactly(1).of(assignmentApi).deleteDomain(domain.getId());
				will(returnValue(domain));
			}
		});
		Response response = target("/v3").path("domains").path(domain.getId()).register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).delete();
		assertEquals(204, response.getStatus());
	}

	@Test
	public void testCreateGrantByUser() {
		context.checking(new Expectations() {

			{
				exactly(1).of(identityApi).getUser(user.getId(), null);
				will(returnValue(user));
				exactly(1).of(assignmentApi).createGrantByUserDomain(role1.getId(), user.getId(), domain.getId(),
						Config.Instance.getOpt(Config.Type.os_inherit, "enabled").asBoolean());
			}
		});
		Response response = target("/v3/domains").path(domain.getId()).path("users").path(user.getId()).path("roles")
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
				exactly(1).of(assignmentApi).createGrantByGroupDomain(role1.getId(), group.getId(), domain.getId(),
						Config.Instance.getOpt(Config.Type.os_inherit, "enabled").asBoolean());
			}
		});
		Response response = target("/v3/domains").path(domain.getId()).path("groups").path(group.getId()).path("roles")
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
				exactly(1).of(assignmentApi).getGrantByUserDomain(role1.getId(), user.getId(), domain.getId(),
						Config.Instance.getOpt(Config.Type.os_inherit, "enabled").asBoolean());
				will(returnValue(role1));
			}
		});
		Response response = target("/v3/domains").path(domain.getId()).path("users").path(user.getId()).path("roles")
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
				exactly(1).of(assignmentApi).getGrantByGroupDomain(role1.getId(), group.getId(), domain.getId(),
						Config.Instance.getOpt(Config.Type.os_inherit, "enabled").asBoolean());
				will(returnValue(role1));
			}
		});
		Response response = target("/v3/domains").path(domain.getId()).path("groups").path(group.getId()).path("roles")
				.path(role1.getId()).register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).head();
		assertEquals(204, response.getStatus());
	}

	@Test
	public void testListGrantByUser() throws JsonProcessingException, IOException {
		final List<Role> roles = new ArrayList<Role>();
		roles.add(role1);

		context.checking(new Expectations() {

			{
				exactly(1).of(assignmentApi).listGrantsByUserDomain(user.getId(), domain.getId(),
						Config.Instance.getOpt(Config.Type.os_inherit, "enabled").asBoolean());
				will(returnValue(roles));
			}
		});
		Response response = target("/v3/domains").path(domain.getId()).path("users").path(user.getId()).path("roles")
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
		final List<Role> roles = new ArrayList<Role>();
		roles.add(role1);

		context.checking(new Expectations() {

			{
				exactly(1).of(assignmentApi).listGrantsByGroupDomain(group.getId(), domain.getId(),
						Config.Instance.getOpt(Config.Type.os_inherit, "enabled").asBoolean());
				will(returnValue(roles));
			}
		});
		Response response = target("/v3/domains").path(domain.getId()).path("groups").path(group.getId()).path("roles")
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
				exactly(1).of(assignmentApi).deleteGrantByUserDomain(role1.getId(), user.getId(), domain.getId(),
						Config.Instance.getOpt(Config.Type.os_inherit, "enabled").asBoolean());
			}
		});
		Response response = target("/v3/domains").path(domain.getId()).path("users").path(user.getId()).path("roles")
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
				exactly(1).of(assignmentApi).deleteGrantByGroupDomain(role1.getId(), group.getId(), domain.getId(),
						Config.Instance.getOpt(Config.Type.os_inherit, "enabled").asBoolean());
			}
		});
		Response response = target("/v3/domains").path(domain.getId()).path("groups").path(group.getId()).path("roles")
				.path(role1.getId()).register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).delete();
		assertEquals(204, response.getStatus());
	}

}
