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
//import com.infinities.keystone4j.auth.driver.PasswordAuthDriver;
//import com.infinities.keystone4j.auth.model.AuthContext;
//import com.infinities.keystone4j.auth.model.AuthInfo;
//import com.infinities.keystone4j.identity.IdentityApi;
//import com.infinities.keystone4j.model.assignment.Domain;
//import com.infinities.keystone4j.model.auth.AuthData;
//import com.infinities.keystone4j.model.auth.AuthV3;
//import com.infinities.keystone4j.model.auth.Identity;
//import com.infinities.keystone4j.model.auth.Password;
//import com.infinities.keystone4j.model.identity.User;
//import com.infinities.keystone4j.token.provider.TokenProviderApi;
//import com.infinities.keystone4j.trust.TrustApi;
//
//public class PasswordAuthDriverTest extends AbstractDbUnitJpaTest {
//
//	private Mockery context;
//	private PasswordAuthDriver driver;
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
//	private final static String METHOD = "password";
//	private Identity identity;
//	private Password password;
//	private User user;
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
//		driver = new PasswordAuthDriver();
//		identityApi = context.mock(IdentityApi.class);
//		assignmentApi = context.mock(AssignmentApi.class);
//		tokenProviderApi = context.mock(TokenProviderApi.class);
//		// authPayload = context.mock(AuthData.class);
//		trustApi = context.mock(TrustApi.class);
//		keystoneContext = new KeystoneContext();
//		userContext = new AuthContext();
//		auth = new AuthV3();
//		identity = new Identity();
//		password = new Password();
//		Domain domain = new Domain();
//		domain.setId("default");
//		user = new User();
//		user.setDomain(domain);
//		user.setId("0f3328f8-a7e7-41b4-830d-be8fdd5186c7");
//		user.setPassword("admin");
//		password.setUser(user);
//		identity.setPassword(password);
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
//				exactly(1).of(identityApi).getUser(user.getId(), null);
//				will(returnValue(user));
//
//				exactly(1).of(identityApi).authenticate(user.getId(), user.getPassword(), user.getDomainid());
//				will(returnValue(user));
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
//				exactly(1).of(identityApi).getUser(user.getId(), null);
//				will(returnValue(user));
//
//				exactly(1).of(identityApi).authenticate(user.getId(), user.getPassword(), user.getDomainid());
//				will(returnValue(user));
//			}
//		});
//		driver.authenticate(keystoneContext, authInfo, userContext, tokenProviderApi, identityApi, assignmentApi);
//	}
//
// }
