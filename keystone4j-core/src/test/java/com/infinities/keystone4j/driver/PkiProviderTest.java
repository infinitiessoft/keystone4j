package com.infinities.keystone4j.driver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.auth.model.AuthContext;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.auth.TokenIdAndData;
import com.infinities.keystone4j.model.catalog.Catalog;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.model.token.Token;
import com.infinities.keystone4j.model.token.TokenData;
import com.infinities.keystone4j.model.token.TokenDataWrapper;
import com.infinities.keystone4j.model.token.TokenRole;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.provider.TokenProviderDriver;
import com.infinities.keystone4j.token.provider.driver.PkiProvider;
import com.infinities.keystone4j.trust.TrustApi;

public class PkiProviderTest extends AbstractDbUnitJpaTest {

	private Mockery context;
	private TokenProviderDriver driver;
	private Token token;
	private User user;
	private TokenDataWrapper tokenDataWrapper;
	private TokenData tokenData;
	private final static String METHOD = "password";
	private List<String> methodNames;
	private Date expiresAt;
	private Project project;
	private Domain domain;
	private AuthContext userContext;
	private IdentityApi identityApi;
	private AssignmentApi assignmentApi;
	private TokenApi tokenApi;
	private CatalogApi catalogApi;
	private Role roleAdmin, roleDemo;
	private Catalog catalog;
	private TrustApi trustApi;


	@Before
	public void setUp() throws Exception {
		context = new JUnit4Mockery() {

			{
				setImposteriser(ClassImposteriser.INSTANCE);
				setThreadingPolicy(new Synchroniser());
			}
		};
		identityApi = context.mock(IdentityApi.class);
		assignmentApi = context.mock(AssignmentApi.class);
		tokenApi = context.mock(TokenApi.class);
		catalogApi = context.mock(CatalogApi.class);
		trustApi = context.mock(TrustApi.class);
		driver = new PkiProvider(identityApi, assignmentApi, catalogApi, tokenApi, trustApi);
		token = new Token();
		token.setId("newtoken");
		domain = new Domain();
		domain.setId("default");
		user = new User();
		user.setDomain(domain);
		user.setId("0f3328f8-a7e7-41b4-830d-be8fdd5186c7");
		user.setPassword("admin");
		roleAdmin = new Role();
		roleAdmin.setId("708bb4f9-9d3c-46af-b18c-7033dc022ffb");
		roleAdmin.setName("admin");
		roleAdmin.setDescription("admin role");
		roleDemo = new Role();
		roleDemo.setId("708bb4f9-9d3c-46af-b18c-7033dc022fff");
		roleDemo.setName("demo");
		roleDemo.setDescription("demo role");
		token.setUser(user);
		token.setDomain(domain);
		tokenDataWrapper = new TokenDataWrapper();
		tokenData = new TokenData();
		tokenData.setToken(token);
		tokenData.setUser(user);
		tokenDataWrapper.setToken(tokenData);
		methodNames = new ArrayList<String>();
		methodNames.add(METHOD);
		expiresAt = new Date();
		project = new Project();
		project.setId("79ea2c65-4679-441f-a596-8aec16752a2f");
		userContext = new AuthContext();
		token.setExpires(expiresAt);
		token.setIssueAt(expiresAt);
		TokenRole tokenRole1 = new TokenRole();
		tokenRole1.setId("newtokenrole1");
		tokenRole1.setToken(token);
		tokenRole1.setRole(roleAdmin);
		TokenRole tokenRole2 = new TokenRole();
		tokenRole2.setId("newtokenrole2");
		tokenRole2.setToken(token);
		tokenRole2.setRole(roleDemo);
		token.getTokenRoles().add(tokenRole1);
		token.getTokenRoles().add(tokenRole2);
		token.setProject(project);
		catalog = new Catalog();
		catalog.setId("newcatalog");
	}

	@After
	public void tearDown() throws Exception {
		context.assertIsSatisfied();
	}

	@Test
	public void testIssueV3Token() {
		final List<Role> rolesDomain = new ArrayList<Role>();
		rolesDomain.add(roleAdmin);

		final List<Role> rolesProject = new ArrayList<Role>();
		rolesProject.add(roleDemo);
		context.checking(new Expectations() {

			{
				exactly(1).of(assignmentApi).getDomain(domain.getId());
				will(returnValue(domain));

				exactly(1).of(assignmentApi).getProject(project.getId());
				will(returnValue(project));

				exactly(1).of(assignmentApi).getRolesForUserAndDomain(user.getId(), domain.getId());
				will(returnValue(rolesDomain));

				exactly(1).of(assignmentApi).getRolesForUserAndProject(user.getId(), project.getId());
				will(returnValue(rolesProject));

				exactly(1).of(tokenApi).createToken(with(any(Token.class)));
				will(returnValue(token));
			}
		});
		TokenIdAndData ret = driver.issueV3Token(user.getId(), methodNames, expiresAt, project.getId(), domain.getId(),
				userContext, null, token, false);

		assertNotNull(ret.getTokenid());
		assertEquals(domain, ret.getTokenData().getToken().getDomain());
		assertEquals(expiresAt, ret.getTokenData().getToken().getExpireAt());
		assertEquals(user, ret.getTokenData().getToken().getUser());
		assertEquals(1, ret.getTokenData().getToken().getRoles().size());
	}

	@Test
	public void testRevokeToken() {
		context.checking(new Expectations() {

			{
				exactly(1).of(tokenApi).deleteToken(token.getId());
			}
		});
		driver.revokeToken(token.getId());
	}

	@Test
	public void testValidateV3Token() {
		final List<Role> rolesProject = new ArrayList<Role>();
		rolesProject.add(roleDemo);
		context.checking(new Expectations() {

			{

				exactly(1).of(identityApi).getUser(user.getId(), null);
				will(returnValue(user));

				exactly(1).of(assignmentApi).getProject(project.getId());
				will(returnValue(project));

				exactly(1).of(tokenApi).getToken(token.getId());
				will(returnValue(token));

				exactly(1).of(assignmentApi).getRolesForUserAndProject(user.getId(), project.getId());
				will(returnValue(rolesProject));

				exactly(1).of(catalogApi).getV3Catalog(user.getId(), project.getId());
				will(returnValue(catalog));
			}
		});
		TokenDataWrapper ret = driver.validateV3Token(token.getId());
		// assertEquals(domain, ret.getToken().getDomain());
		assertEquals(expiresAt, ret.getToken().getExpireAt());
		assertEquals(user, ret.getToken().getUser());
		assertEquals(1, ret.getToken().getRoles().size());
	}
}
