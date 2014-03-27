package com.infinities.keystone4j.driver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.assignment.model.Project;
import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.token.TokenDriver;
import com.infinities.keystone4j.token.driver.TokenJpaDriver;
import com.infinities.keystone4j.token.model.Token;

public class TokenJpaDriverTest extends AbstractDbUnitJpaTest {

	private Mockery context;
	private TokenDriver driver;
	private Token token;
	private User user;
	private Role roleAdmin, roleDemo;
	private Project project;
	private Date expiresAt;


	@Before
	public void setUp() throws Exception {
		context = new JUnit4Mockery() {

			{
				setImposteriser(ClassImposteriser.INSTANCE);
				setThreadingPolicy(new Synchroniser());
			}
		};
		driver = new TokenJpaDriver();
		user = new User();
		Domain domain = new Domain();
		domain.setId("default");
		expiresAt = new Date();
		project = new Project();
		project.setId("79ea2c65-4679-441f-a596-8aec16752a2f");
		user.setDomain(domain);
		user.setId("0f3328f8-a7e7-41b4-830d-be8fdd5186c7");
		user.setPassword("admin");
		token = new Token();
		token.setId("newtoken");
		token.setUser(user);
		token.setDomain(domain);
		token.setExpires(expiresAt);
		token.setIssueAt(expiresAt);
		roleAdmin = new Role();
		roleAdmin.setId("708bb4f9-9d3c-46af-b18c-7033dc022ffb");
		roleAdmin.setName("admin");
		roleAdmin.setDescription("admin role");
		roleDemo = new Role();
		roleDemo.setId("708bb4f9-9d3c-46af-b18c-7033dc022fff");
		roleDemo.setName("demo");
		roleDemo.setDescription("demo role");
		// TokenRole tokenRole1 = new TokenRole();
		// tokenRole1.setId("newtokenrole1");
		// tokenRole1.setToken(token);
		// tokenRole1.setRole(roleAdmin);
		// TokenRole tokenRole2 = new TokenRole();
		// tokenRole2.setId("newtokenrole2");
		// tokenRole2.setToken(token);
		// tokenRole2.setRole(roleDemo);
		// token.getTokenRoles().add(tokenRole1);
		// token.getTokenRoles().add(tokenRole2);
		token.setProject(project);

	}

	@After
	public void tearDown() throws Exception {
		context.assertIsSatisfied();
	}

	@Test
	public void testGetToken() {
		Token ret = driver.getToken("708bb4f9-9d3c-46af-b18c-7033dc022f11");
		assertEquals("708bb4f9-9d3c-46af-b18c-7033dc022f11", ret.getId());
		assertEquals("0f3328f8-a7e7-41b4-830d-be8fdd5186c7", ret.getUser().getId());
	}

	@Test
	public void testDeleteToken() {
		Token ret = driver.getToken("708bb4f9-9d3c-46af-b18c-7033dc022f11");
		assertTrue(ret.getValid());
		driver.deleteToken("708bb4f9-9d3c-46af-b18c-7033dc022f11");
		ret = driver.getToken("708bb4f9-9d3c-46af-b18c-7033dc022f11");
		assertFalse(ret.getValid());
	}

	@Test
	public void testListRevokeTokens() {
		List<Token> rets = driver.listRevokeTokens();
		assertEquals(1, rets.size());
	}

	@Test
	public void testFlushExpiredTokens() {
		Token ret = driver.getToken("708bb4f9-9d3c-46af-b18c-7033dc022f31");
		assertNotNull(ret);
		driver.flushExpiredTokens();
		// ret = driver.getToken("708bb4f9-9d3c-46af-b18c-7033dc022f31");
		// assertNull(ret);
	}

	@Test
	public void testDeleteTokensForTrust() {
		Token ret = driver.getToken("708bb4f9-9d3c-46af-b18c-7033dc012f11");
		assertTrue(ret.getValid());
		driver.deleteTokensForTrust(user.getId(), "0f3328f8-a7e7-41b4-830d-be8fdd5086c8");
		ret = driver.getToken("708bb4f9-9d3c-46af-b18c-7033dc012f11");
		assertFalse(ret.getValid());
	}

	@Test
	public void testDeleteTokensForUser() {
		Token ret = driver.getToken("708bb4f9-9d3c-46af-b18c-7033dc022f11");
		assertTrue(ret.getValid());
		driver.deleteTokensForUser(user.getId(), project.getId());
		ret = driver.getToken("708bb4f9-9d3c-46af-b18c-7033dc022f11");
		assertFalse(ret.getValid());
	}

	@Test
	public void testCreateToken() {
		Token ret = driver.createToken(token);
		assertEquals(token.getId(), ret.getId());
		assertEquals(token.getIssueAt(), ret.getIssueAt());
		assertEquals(token.getExpires(), ret.getExpires());
		assertEquals(token.getDomain(), ret.getDomain());
		assertEquals(token.getUser(), ret.getUser());
		assertEquals(token.getProject(), ret.getProject());
	}
}
