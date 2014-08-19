package com.infinities.keystone4j.resource.v2.token;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertPathBuilderException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bouncycastle.cms.CMSException;
import org.bouncycastle.operator.OperatorCreationException;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.common.collect.Maps;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.assignment.GroupDomainGrant;
import com.infinities.keystone4j.model.assignment.GroupProjectGrant;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.assignment.UserDomainGrant;
import com.infinities.keystone4j.model.assignment.UserProjectGrant;
import com.infinities.keystone4j.model.catalog.Catalog;
import com.infinities.keystone4j.model.catalog.Endpoint;
import com.infinities.keystone4j.model.catalog.Service;
import com.infinities.keystone4j.model.common.Link;
import com.infinities.keystone4j.model.identity.Group;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.model.token.Auth;
import com.infinities.keystone4j.model.token.AuthWrapper;
import com.infinities.keystone4j.model.token.PasswordCredentials;
import com.infinities.keystone4j.model.token.Token;
import com.infinities.keystone4j.model.token.v2.Access;
import com.infinities.keystone4j.model.token.v2.Access.Metadata;
import com.infinities.keystone4j.model.token.v2.TokenV2;
import com.infinities.keystone4j.model.token.v2.TokenV2DataWrapper;
import com.infinities.keystone4j.model.trust.SignedWrapper;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.TrustApi;
import com.infinities.keystone4j.utils.Cms;
import com.infinities.keystone4j.utils.jackson.JacksonFeature;
import com.infinities.keystone4j.utils.jackson.ObjectMapperResolver;

public class TokenResourceTest extends JerseyTest {

	private User user;
	private Domain domain;
	private Mockery context;
	private TokenApi tokenApi;
	private TokenProviderApi tokenProviderApi;
	private AssignmentApi assignmentApi;
	private IdentityApi identityApi;
	private PolicyApi policyApi;
	private TrustApi trustApi;
	private CatalogApi catalogApi;
	private Domain domain2, defaultDomain;
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
	private Catalog catalog;
	private Endpoint endpoint;
	private Service service;
	private TokenV2DataWrapper wrapper;


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
		user = new User();
		user.setId("0ca8f6");
		user.setPassword("secrete");
		user.setDomain(domain);
		domain = new Domain();
		domain.setId("domainid");

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
		userDomainGrant.setRole(role1);
		// userDomainGrantMetadata1 = new UserDomainGrantMetadata();
		// userDomainGrantMetadata1.setId("userDomainGrantMetadata1");
		// userDomainGrantMetadata1.setGrant(userDomainGrant);
		// userDomainGrantMetadata1.setRole(role1);
		role1.getUserDomainGrants().add(userDomainGrant);
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
		role2.getUserProjectGrants().add(userProjectGrant);
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
		role1.getGroupDomainGrants().add(groupDomainGrant);
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
		role2.getGroupProjectGrants().add(groupProjectGrant);
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
		role3.getGroupDomainGrants().add(groupDomainGrant2);

		catalog = new Catalog();
		service = new Service();
		service.setDescription("Keystone Identity Service");
		service.setName("keystone");
		service.setType("identity");
		service.setId("newserviceid");

		endpoint = new Endpoint();
		endpoint.setInterfaceType("internal");
		endpoint.setName("the internal volume endpoint");
		endpoint.setUrl("http://identity:35357/v3/endpoints/");
		endpoint.setService(service);
		catalog.getEndpoints().add(endpoint);
		catalog.getServices().add(service);

		wrapper = new TokenV2DataWrapper();
		Access access = new Access();
		access.setRegionName("region");
		Access.Service service = new Access.Service();
		service.setName("service");
		service.setType("type");
		Access.Service.Endpoint endpoint = new Access.Service.Endpoint();
		endpoint.setAdminURL("admin url");
		endpoint.setInternalURL("internal url");
		endpoint.setPublicURL("public url");
		endpoint.setRegion("region");
		service.getEndpoints().add(endpoint);
		Link link = new Link();
		link.setHref("href");
		link.setRel("rel");
		link.setType("type");
		service.getEndpointsLinks().add(link);
		access.getServiceCatalog().add(service);
		access.setTenantId("tenantId");
		Access.User u = new Access.User();
		u.setId("userid");
		u.setName("name");
		u.setTenantId("tenantid");
		u.setUsername("username");
		Access.User.Role role = new Access.User.Role();
		role.setId("id");
		role.setName("name");
		u.getRoles().add(role);
		access.setUser(u);
		TokenV2 token = new TokenV2();
		token.setExpires(Calendar.getInstance());
		token.setId("tokenid");
		token.setIssued_at(Calendar.getInstance());
		token.setTenant(project);
		access.setToken(token);
		Metadata metadata = new Metadata();
		metadata.setIsAdmin(0);
		metadata.getRoles().add("role1");
		access.setMetadata(metadata);
		wrapper.setAccess(access);

		return new TokenResourceTestApplication(catalogApi, tokenApi, tokenProviderApi, assignmentApi, identityApi,
				policyApi, trustApi);

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAuthenticate() throws JsonGenerationException, JsonMappingException, IOException {
		final Entry<String, TokenV2DataWrapper> entry = Maps.immutableEntry("tokenid", wrapper);
		final List<Role> roles = new ArrayList<Role>();
		roles.add(role1);
		roles.add(role2);
		roles.add(role3);
		context.checking(new Expectations() {

			{
				exactly(1).of(identityApi).getUserByName("admin", "default");
				will(returnValue(user));
				exactly(1).of(identityApi).authenticate(user.getId(), user.getPassword(), null);
				will(returnValue(user));
				exactly(1).of(assignmentApi).getProjectByName("admin", "default");
				will(returnValue(project));
				exactly(1).of(assignmentApi).getProject(project.getId());
				will(returnValue(project));
				exactly(1).of(assignmentApi).getRolesForUserAndProject(user.getId(), project.getId());
				will(returnValue(roles));
				exactly(1).of(catalogApi).getV3Catalog(user.getId(), project.getId());
				will(returnValue(catalog));

				exactly(1).of(assignmentApi).getRole(role1.getId());
				will(returnValue(role1));
				exactly(1).of(assignmentApi).getRole(role2.getId());
				will(returnValue(role2));
				exactly(1).of(assignmentApi).getRole(role3.getId());
				will(returnValue(role3));

				exactly(1).of(tokenProviderApi).issueV2Token(with(any(Token.class)), with(any(List.class)),
						with(any(Catalog.class)));
				will(returnValue(entry));

			}
		});

		PasswordCredentials credentials = new PasswordCredentials();
		credentials.setUsername("admin");
		credentials.setPassword("secrete");
		Auth auth = new Auth();
		auth.setTenantName("admin");
		auth.setPasswordCredentials(credentials);
		AuthWrapper authWrapper = new AuthWrapper();
		authWrapper.setAuth(auth);
		// String json = JsonUtils.toJson(authWrapper);
		Response response = target("/v2.0/tokens").register(JacksonFeature.class).request()
				.post(Entity.entity(authWrapper, MediaType.APPLICATION_JSON_TYPE));
		assertEquals(200, response.getStatus());
	}

	@Test
	public void testGetRevocationList() throws CertificateException, OperatorCreationException, NoSuchAlgorithmException,
			NoSuchProviderException, CertPathBuilderException, InvalidAlgorithmParameterException, CMSException, IOException {
		Token token = new Token();
		token.setDomain(domain);
		token.setExpires(new Date());
		token.setId("tokenid");
		token.setIssueAt(new Date());
		token.setProject(project);
		token.setUser(user);
		final List<Token> tokens = new ArrayList<Token>();
		tokens.add(token);
		context.checking(new Expectations() {

			{
				exactly(1).of(tokenApi).listRevokedTokens();
				will(returnValue(tokens));
			}
		});
		Response response = target("/v2.0/tokens/revoked").register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());
		SignedWrapper signedWrapper = response.readEntity(SignedWrapper.class);
		String formatted = signedWrapper.getSigned().replace("-----BEGIN CMS-----", "").replace("-----END CMS-----", "")
				.trim();
		String result = Cms.Instance.verifySignature(formatted.getBytes(),
				Config.Instance.getOpt(Config.Type.signing, "certfile").asText(),
				Config.Instance.getOpt(Config.Type.signing, "ca_certs").asText());
		System.out.println(result);
	}

	@Test
	public void testValidateToken() {
		fail("Not yet implemented");
	}

	@Test
	public void testValidateTokenHead() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteToken() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetEndpoints() {
		fail("Not yet implemented");
	}

}
