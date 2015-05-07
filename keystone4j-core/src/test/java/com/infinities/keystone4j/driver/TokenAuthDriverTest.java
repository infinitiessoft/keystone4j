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
//package com.infinities.keystone4j.driver;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
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
//import com.infinities.keystone4j.KeystoneContext;
//import com.infinities.keystone4j.assignment.AssignmentApi;
//import com.infinities.keystone4j.auth.AuthDriver;
//import com.infinities.keystone4j.auth.driver.TokenAuthDriver;
//import com.infinities.keystone4j.auth.model.AuthContext;
//import com.infinities.keystone4j.auth.model.AuthInfo;
//import com.infinities.keystone4j.identity.IdentityApi;
//import com.infinities.keystone4j.model.assignment.Domain;
//import com.infinities.keystone4j.model.auth.AuthData;
//import com.infinities.keystone4j.model.auth.AuthV3;
//import com.infinities.keystone4j.model.auth.Identity;
//import com.infinities.keystone4j.model.identity.User;
//import com.infinities.keystone4j.model.token.Token;
//import com.infinities.keystone4j.model.token.TokenData;
//import com.infinities.keystone4j.model.token.TokenDataWrapper;
//import com.infinities.keystone4j.token.provider.TokenProviderApi;
//import com.infinities.keystone4j.trust.TrustApi;
//
//public class TokenAuthDriverTest extends AbstractDbUnitJpaTest {
//
//	private Mockery context;
//	private TokenAuthDriver driver;
//	private TokenProviderApi tokenProviderApi;
//	private IdentityApi identityApi;
//	private AssignmentApi assignmentApi;
//	private TrustApi trustApi;
//	private KeystoneContext keystoneContext;
//	// private AuthData authPayload;
//	private AuthContext userContext;
//	private AuthInfo authInfo;
//	private AuthV3 auth;
//	private final Map<String, AuthDriver> authMethods = new HashMap<String, AuthDriver>();
//	private final static String METHOD = "token";
//	private Identity identity;
//	private Token token;
//	private User user;
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
//		driver = new TokenAuthDriver();
//		identityApi = context.mock(IdentityApi.class);
//		assignmentApi = context.mock(AssignmentApi.class);
//		tokenProviderApi = context.mock(TokenProviderApi.class);
//		// authPayload = context.mock(AuthData.class);
//		trustApi = context.mock(TrustApi.class);
//		keystoneContext = new KeystoneContext();
//		userContext = new AuthContext();
//		auth = new AuthV3();
//		identity = new Identity();
//		token = new Token();
//		token.setId("newtoken");
//		Domain domain = new Domain();
//		domain.setId("default");
//		user = new User();
//		user.setDomain(domain);
//		user.setId("0f3328f8-a7e7-41b4-830d-be8fdd5186c7");
//		user.setPassword("admin");
//		token.setUser(user);
//		tokenDataWrapper = new TokenDataWrapper();
//		tokenData = new TokenData();
//		tokenData.setToken(token);
//		tokenData.setUser(user);
//		tokenDataWrapper.setToken(tokenData);
//		identity.setToken(token);
//		List<String> methods = new ArrayList<String>();
//		methods.add(METHOD);
//		identity.setMethods(methods);
//		auth.setIdentity(identity);
//		authMethods.put(METHOD, driver);
//		authInfo = new AuthInfo(keystoneContext, auth, authMethods, assignmentApi, trustApi);
//
//	}
//
//	@After
//	public void tearDown() throws Exception {
//		context.assertIsSatisfied();
//	}
//
//	@Test
//	public void testAuthenticateKeystoneContextAuthDataAuthContextTokenProviderApiIdentityApiAssignmentApi() {
//		AuthData methodData = authInfo.getMethodData(METHOD);
//		context.checking(new Expectations() {
//
//			{
//				exactly(1).of(tokenProviderApi).validateV3Token(token.getId());
//				will(returnValue(tokenDataWrapper));
//
//			}
//		});
//		driver.authenticate(keystoneContext, methodData, userContext, tokenProviderApi, identityApi, assignmentApi);
//	}
//
//	@Test
//	public void testAuthenticateKeystoneContextAuthInfoAuthContextTokenProviderApiIdentityApiAssignmentApi() {
//		context.checking(new Expectations() {
//
//			{
//				exactly(1).of(tokenProviderApi).validateV3Token(token.getId());
//				will(returnValue(tokenDataWrapper));
//
//			}
//		});
//		driver.authenticate(keystoneContext, authInfo, userContext, tokenProviderApi, identityApi, assignmentApi);
//	}
//
// }
