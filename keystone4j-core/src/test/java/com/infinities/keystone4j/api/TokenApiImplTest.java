package com.infinities.keystone4j.api;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ws.rs.WebApplicationException;

import org.apache.commons.codec.DecoderException;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.Invocation;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.action.CustomAction;
import org.jmock.lib.concurrent.Synchroniser;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.identity.Group;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.model.token.Token;
import com.infinities.keystone4j.model.trust.Trust;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.TokenDriver;
import com.infinities.keystone4j.token.api.TokenApiImpl;
import com.infinities.keystone4j.trust.TrustApi;
import com.infinities.keystone4j.utils.Cms;

public class TokenApiImplTest {

	private Mockery context;
	// private AssignmentApi assignmentApi;
	// private IdentityApi identityApi;
	// private TokenProviderApi tokenProviderApi;
	private TrustApi trustApi;
	private TokenApi tokenApi;
	private TokenDriver driver;
	private Domain domain;
	private Project project;
	private User user, trustee, trustor;
	private Token token;
	private Group group;
	private Role role;
	private Trust trust1, trust2;


	@Before
	public void setUp() throws Exception {
		context = new JUnit4Mockery() {

			{
				setImposteriser(ClassImposteriser.INSTANCE);
				setThreadingPolicy(new Synchroniser());
			}
		};

		// assignmentApi = context.mock(AssignmentApi.class);
		// identityApi = context.mock(IdentityApi.class);
		// tokenProviderApi = context.mock(TokenProviderApi.class);
		trustApi = context.mock(TrustApi.class);
		driver = context.mock(TokenDriver.class);

		domain = new Domain();
		domain.setDescription("desc of Domain");
		domain.setName("my domain");

		project = new Project();
		project.setDescription("desc of Project");
		project.setDomain(domain);
		project.setName("my project");

		user = new User();
		user.setId("newuser");
		user.setDescription("my user");
		user.setName("example user");
		user.setDefault_project(project);
		user.setDomain(domain);

		trustee = new User();
		trustee.setId("newtrustee");
		trustee.setDescription("my trustee");
		trustee.setName("example trustee");
		trustee.setDefault_project(project);
		trustee.setDomain(domain);

		trustor = new User();
		trustor.setId("newtrustor");
		trustor.setDescription("my trustor");
		trustor.setName("example trustor");
		trustor.setDefault_project(project);
		trustor.setDomain(domain);

		token = new Token();
		token.setProject(project);
		token.setExpires(new Date());
		token.setId("newtoken");
		token.setIssueAt(new Date());
		token.setUser(user);
		user.getTokens().add(token);

		group = new Group();
		group.setDescription("my group");
		group.setDomain(domain);
		group.setName("newgroup");

		role = new Role();
		role.setDescription("my role1");
		role.setName("example role1");

		trust1 = new Trust();
		trust1.setId("newtrust1");
		trust1.setDescription("my trust1");
		trust1.setProject(project);
		trust1.setTrustee(trustee);
		trust1.setTrustor(user);

		trust2 = new Trust();
		trust2.setId("newtrust2");
		trust2.setDescription("my trust2");
		trust2.setProject(project);
		trust2.setTrustee(user);
		trust2.setTrustor(trustor);

		tokenApi = new TokenApiImpl(trustApi, driver);
	}

	@After
	public void tearDown() throws Exception {
		context.assertIsSatisfied();
	}

	@Test(expected = WebApplicationException.class)
	public void testGetTokenWithExpireDate() throws UnsupportedEncodingException, NoSuchAlgorithmException, DecoderException {
		token.setId("newtoken");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, -5);
		Date date = calendar.getTime();
		token.setExpires(date);
		token.setIssueAt(date);
		final String id = Cms.Instance.hashToken(token.getId(), null);
		context.checking(new Expectations() {

			{
				exactly(1).of(driver).getToken(id);
				will(returnValue(token));
			}
		});
		Token ret = tokenApi.getToken(token.getId());
		assertEquals(token.getId(), ret.getId());
		assertEquals(token.getProject(), ret.getProject());
		assertEquals(token.getExpires(), ret.getExpires());
		assertEquals(token.getIssueAt(), ret.getIssueAt());
		assertEquals(token.getValid(), ret.getValid());
	}

	@Test
	public void testGetToken() throws UnsupportedEncodingException, NoSuchAlgorithmException, DecoderException {
		token.setId("newtoken");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, 5);
		Date date = calendar.getTime();
		token.setExpires(date);
		token.setIssueAt(date);
		final String id = Cms.Instance.hashToken(token.getId(), null);
		context.checking(new Expectations() {

			{
				exactly(1).of(driver).getToken(id);
				will(returnValue(token));
			}
		});
		Token ret = tokenApi.getToken(token.getId());
		assertEquals(token.getId(), ret.getId());
		assertEquals(token.getProject(), ret.getProject());
		assertEquals(token.getExpires(), ret.getExpires());
		assertEquals(token.getIssueAt(), ret.getIssueAt());
		assertEquals(token.getValid(), ret.getValid());
	}

	@Test
	public void testListRevokedTokens() {
		token.setId("newtoken");
		final List<Token> tokens = new ArrayList<Token>();
		tokens.add(token);

		context.checking(new Expectations() {

			{
				exactly(1).of(driver).listRevokeTokens();
				will(returnValue(tokens));
			}
		});
		List<Token> rets = tokenApi.listRevokedTokens();
		assertEquals(1, rets.size());
		Token ret = rets.get(0);
		assertEquals(token.getId(), ret.getId());
		assertEquals(token.getProject(), ret.getProject());
		assertEquals(token.getExpires(), ret.getExpires());
		assertEquals(token.getIssueAt(), ret.getIssueAt());
		assertEquals(token.getValid(), ret.getValid());
	}

	@Test
	public void testDeleteTokensForTrust() {
		user.setId("newuser");
		project.setId("newproject");
		final List<Trust> trusts1 = new ArrayList<Trust>();
		trusts1.add(trust1);

		final List<Trust> trusts2 = new ArrayList<Trust>();
		trusts2.add(trust2);

		context.checking(new Expectations() {

			{
				exactly(1).of(driver).deleteTokensForTrust(user.getId(), trust1.getId());
			}
		});
		tokenApi.deleteTokensForTrust(user.getId(), trust1.getId());
	}

	@Test
	public void testDeleteTokensForUser() {
		user.setId("newuser");
		project.setId("newproject");
		final List<Trust> trusts1 = new ArrayList<Trust>();
		trusts1.add(trust1);

		final List<Trust> trusts2 = new ArrayList<Trust>();
		trusts2.add(trust2);

		context.checking(new Expectations() {

			{
				exactly(1).of(trustApi).listTrustsForTrustee(user.getId());
				will(returnValue(trusts1));
				exactly(1).of(driver).deleteTokensForTrust(user.getId(), trust1.getId());
				exactly(1).of(trustApi).listTrustsForTrustor(user.getId());
				will(returnValue(trusts2));
				exactly(1).of(driver).deleteTokensForTrust(user.getId(), trust2.getId());
				exactly(1).of(driver).deleteTokensForUser(user.getId(), project.getId());
			}
		});
		tokenApi.deleteTokensForUser(user.getId(), project.getId());
	}

	// @Test
	// public void testDeleteTokensForDomain() {
	// domain.setId("domainid");
	// user.setId("newuser");
	// project.setId("newproject");
	// final List<Project> projects = new ArrayList<Project>();
	// projects.add(project);
	// final List<User> users = new ArrayList<User>();
	// users.add(user);
	//
	// final List<Trust> trusts1 = new ArrayList<Trust>();
	// trusts1.add(trust1);
	//
	// final List<Trust> trusts2 = new ArrayList<Trust>();
	// trusts2.add(trust2);
	//
	// context.checking(new Expectations() {
	//
	// {
	// exactly(1).of(assignmentApi).listProjects();
	// will(returnValue(projects));
	// exactly(1).of(assignmentApi).listUsersForProject(project.getId());
	// will(returnValue(users));
	// exactly(1).of(driver).deleteTokensForUser(user.getId(), project.getId());
	//
	// exactly(1).of(trustApi).listTrustsForTrustee(user.getId());
	// will(returnValue(trusts1));
	// exactly(1).of(driver).deleteTokensForTrust(user.getId(), trust1.getId());
	// exactly(1).of(trustApi).listTrustsForTrustor(user.getId());
	// will(returnValue(trusts2));
	// exactly(1).of(driver).deleteTokensForTrust(user.getId(), trust2.getId());
	// }
	// });
	// tokenApi.deleteTokensForDomain(domain.getId());
	// }

	@Test
	public void testCreateToken() {
		final String id = "newtoken";
		context.checking(new Expectations() {

			{
				exactly(1).of(driver).createToken(token);
				will(new CustomAction("add id to token") {

					@Override
					public Object invoke(Invocation invocation) throws Throwable {
						Token token = (Token) invocation.getParameter(0);
						token.setId(id);
						return token;
					}

				});
			}
		});

		Token ret = tokenApi.createToken(token);
		assertEquals(id, ret.getId());
		assertEquals(token.getProject(), ret.getProject());
		assertEquals(token.getExpires(), ret.getExpires());
		assertEquals(token.getIssueAt(), ret.getIssueAt());
		assertEquals(token.getValid(), ret.getValid());
	}

	@Test
	public void testDeleteToken() throws UnsupportedEncodingException, NoSuchAlgorithmException, DecoderException {
		token.setId("newtoken");
		user.setId("newuser");
		project.setId("newproject");
		final String id = Cms.Instance.hashToken(token.getId(), null);
		context.checking(new Expectations() {

			{
				exactly(1).of(driver).deleteToken(id);
			}
		});
		tokenApi.deleteToken(token.getId());
	}

}
