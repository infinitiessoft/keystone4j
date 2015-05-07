/*******************************************************************************
 * # Copyright 2015 InfinitiesSoft Solutions Inc.
 * #
 * # Licensed under the Apache License, Version 2.0 (the "License"); you may
 * # not use this file except in compliance with the License. You may obtain
 * # a copy of the License at
 * #
 * #      http://www.apache.org/licenses/LICENSE-2.0
 * #
 * # Unless required by applicable law or agreed to in writing, software
 * # distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * # WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * # License for the specific language governing permissions and limitations
 * # under the License.
 *******************************************************************************/
//package com.infinities.keystone4j.api;
//
//import static org.junit.Assert.assertEquals;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//
//import javax.ws.rs.WebApplicationException;
//
//import org.jmock.Expectations;
//import org.jmock.Mockery;
//import org.jmock.integration.junit4.JUnit4Mockery;
//import org.jmock.lib.concurrent.Synchroniser;
//import org.jmock.lib.legacy.ClassImposteriser;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import com.infinities.keystone4j.auth.model.AuthContext;
//import com.infinities.keystone4j.model.assignment.Domain;
//import com.infinities.keystone4j.model.assignment.Project;
//import com.infinities.keystone4j.model.assignment.Role;
//import com.infinities.keystone4j.model.auth.TokenIdAndData;
//import com.infinities.keystone4j.model.identity.Group;
//import com.infinities.keystone4j.model.identity.User;
//import com.infinities.keystone4j.model.token.Token;
//import com.infinities.keystone4j.model.token.TokenData;
//import com.infinities.keystone4j.model.token.TokenDataWrapper;
//import com.infinities.keystone4j.model.trust.Trust;
//import com.infinities.keystone4j.token.provider.TokenProviderApi;
//import com.infinities.keystone4j.token.provider.TokenProviderDriver;
//import com.infinities.keystone4j.token.provider.api.TokenProviderApiImpl;
//
//public class TokenProviderApiImplTest {
//
//	private Mockery context;
//	private TokenProviderApi tokenProviderApi;
//	// private TokenApi tokenApi;
//	private TokenProviderDriver driver;
//	private Domain domain;
//	private Project project;
//	private User user, trustee, trustor;
//	private Token token;
//	private Group group;
//	private Role role;
//	private Trust trust1, trust2;
//	private TokenDataWrapper tokenDataWrapper;
//	private TokenData tokenData;
//
//
//	@Before
//	public void setUp() throws Exception {
//		context = new JUnit4Mockery() {
//
//			{
//				setImposteriser(ClassImposteriser.INSTANCE);
//				setThreadingPolicy(new Synchroniser());
//			}
//		};
//
//		tokenProviderApi = context.mock(TokenProviderApi.class);
//		driver = context.mock(TokenProviderDriver.class);
//
//		domain = new Domain();
//		domain.setDescription("desc of Domain");
//		domain.setName("my domain");
//
//		project = new Project();
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
//
//		trustee = new User();
//		trustee.setId("newtrustee");
//		trustee.setDescription("my trustee");
//		trustee.setName("example trustee");
//		trustee.setDefault_project(project);
//		trustee.setDomain(domain);
//
//		trustor = new User();
//		trustor.setId("newtrustor");
//		trustor.setDescription("my trustor");
//		trustor.setName("example trustor");
//		trustor.setDefault_project(project);
//		trustor.setDomain(domain);
//
//		token = new Token();
//		token.setProject(project);
//		token.setExpires(new Date());
//		token.setId("newtoken");
//		token.setIssueAt(new Date());
//		token.setUser(user);
//		user.getTokens().add(token);
//
//		group = new Group();
//		group.setDescription("my group");
//		group.setDomain(domain);
//		group.setName("newgroup");
//
//		role = new Role();
//		role.setDescription("my role1");
//		role.setName("example role1");
//
//		trust1 = new Trust();
//		trust1.setId("newtrust1");
//		trust1.setDescription("my trust1");
//		trust1.setProject(project);
//		trust1.setTrustee(trustee);
//		trust1.setTrustor(user);
//
//		trust2 = new Trust();
//		trust2.setId("newtrust2");
//		trust2.setDescription("my trust2");
//		trust2.setProject(project);
//		trust2.setTrustee(user);
//		trust2.setTrustor(trustor);
//
//		tokenDataWrapper = new TokenDataWrapper();
//		tokenData = new TokenData();
//		tokenData.setDomain(domain);
//		tokenData.setExpireAt(token.getExpires());
//		tokenData.setIssuedAt(token.getIssueAt());
//		tokenData.setProject(project);
//		tokenData.setUser(user);
//		tokenData.getRoles().add(role);
//		tokenData.setToken(token);
//		tokenDataWrapper.setToken(tokenData);
//
//		tokenProviderApi = new TokenProviderApiImpl(driver);
//	}
//
//	@After
//	public void tearDown() throws Exception {
//		context.assertIsSatisfied();
//	}
//
//	@Test
//	public void testIssueV3Token() {
//		Calendar calendar = Calendar.getInstance();
//		calendar.add(Calendar.MINUTE, 5);
//		final Date date = calendar.getTime();
//		token.setExpires(date);
//		tokenData.setExpireAt(date);
//		user.setId("newuser");
//		domain.setId("newdomain");
//		group.setId("newgroup");
//		project.setId("newproject");
//		// final String uniqueid = Cms.Instance.hashToken(token.getId());
//		final List<String> methodNames = new ArrayList<String>();
//		methodNames.add("password");
//		final AuthContext authContext = new AuthContext();
//		authContext.getRoles().add(role);
//		authContext.setDomainid(domain.getId());
//		authContext.setExpiresAt(date);
//		authContext.setMethodNames(methodNames);
//		authContext.setProjectid(project.getId());
//		authContext.setUserid(user.getId());
//		final TokenIdAndData metadata = new TokenIdAndData(token.getId(), tokenDataWrapper);
//
//		context.checking(new Expectations() {
//
//			{
//				exactly(1).of(driver).issueV3Token(user.getId(), methodNames, date, project.getId(), domain.getId(),
//						authContext, trust1, token, false);
//				will(returnValue(metadata));
//			}
//		});
//		TokenIdAndData retMetadata = tokenProviderApi.issueV3Token(user.getId(), methodNames, date, project.getId(),
//				domain.getId(), authContext, trust1, token, false);
//		TokenDataWrapper ret = retMetadata.getTokenData();
//		assertEquals(tokenDataWrapper.getToken().getDomain(), ret.getToken().getDomain());
//		assertEquals(tokenDataWrapper.getToken().getExpireAt(), ret.getToken().getExpireAt());
//		assertEquals(tokenDataWrapper.getToken().getIssuedAt(), ret.getToken().getIssuedAt());
//		assertEquals(tokenDataWrapper.getToken().getProject(), ret.getToken().getProject());
//		assertEquals(tokenDataWrapper.getToken().getUser(), ret.getToken().getUser());
//		assertEquals(tokenDataWrapper.getToken().getRoles(), ret.getToken().getRoles());
//		assertEquals(tokenDataWrapper.getToken().getToken(), ret.getToken().getToken());
//	}
//
//	@Test(expected = WebApplicationException.class)
//	public void testCheckV3TokenWithExpireDate() {
//		user.setId("newuser");
//		domain.setId("newdomain");
//		group.setId("newgroup");
//
//		context.checking(new Expectations() {
//
//			{
//				exactly(1).of(driver).validateV3Token(token.getId());
//				will(returnValue(tokenDataWrapper));
//			}
//		});
//
//		tokenProviderApi.checkV3Token(token.getId());
//	}
//
//	@Test
//	public void testCheckV3Token() {
//		Calendar calendar = Calendar.getInstance();
//		calendar.add(Calendar.MINUTE, 5);
//		Date date = calendar.getTime();
//		token.setExpires(date);
//		tokenData.setExpireAt(date);
//		user.setId("newuser");
//		domain.setId("newdomain");
//		group.setId("newgroup");
//		// String uniqueid = null;
//
//		// uniqueid = Cms.Instance.hashToken(tokenid);
//
//		// final String uniqueid = Cms.Instance.hashToken(token.getId());
//		context.checking(new Expectations() {
//
//			{
//				exactly(1).of(driver).validateV3Token(token.getId());
//				will(returnValue(tokenDataWrapper));
//			}
//		});
//
//		tokenProviderApi.checkV3Token(token.getId());
//	}
//
//	@Test
//	public void testRevokeToken() {
//		Calendar calendar = Calendar.getInstance();
//		calendar.add(Calendar.MINUTE, 5);
//		Date date = calendar.getTime();
//		token.setExpires(date);
//		tokenData.setExpireAt(date);
//		user.setId("newuser");
//		domain.setId("newdomain");
//		group.setId("newgroup");
//		context.checking(new Expectations() {
//
//			{
//				exactly(1).of(driver).revokeToken(token.getId());
//			}
//		});
//		tokenProviderApi.revokeToken(token.getId());
//	}
//
//	@Test
//	public void testValidateV3Token() {
//		Calendar calendar = Calendar.getInstance();
//		calendar.add(Calendar.MINUTE, 5);
//		Date date = calendar.getTime();
//		token.setExpires(date);
//		tokenData.setExpireAt(date);
//		user.setId("newuser");
//		domain.setId("newdomain");
//		group.setId("newgroup");
//
//		context.checking(new Expectations() {
//
//			{
//				exactly(1).of(driver).validateV3Token(token.getId());
//				will(returnValue(tokenDataWrapper));
//			}
//		});
//
//		TokenDataWrapper ret = tokenProviderApi.validateV3Token(token.getId());
//		assertEquals(tokenDataWrapper.getToken().getDomain(), ret.getToken().getDomain());
//		assertEquals(tokenDataWrapper.getToken().getExpireAt(), ret.getToken().getExpireAt());
//		assertEquals(tokenDataWrapper.getToken().getIssuedAt(), ret.getToken().getIssuedAt());
//		assertEquals(tokenDataWrapper.getToken().getProject(), ret.getToken().getProject());
//		assertEquals(tokenDataWrapper.getToken().getUser(), ret.getToken().getUser());
//		assertEquals(tokenDataWrapper.getToken().getRoles(), ret.getToken().getRoles());
//		// assertEquals(tokenDataWrapper.getToken().getToken(),
//		// ret.getToken().getToken());
//	}
//
// }
