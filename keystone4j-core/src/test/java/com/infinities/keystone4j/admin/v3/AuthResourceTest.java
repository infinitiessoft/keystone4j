package com.infinities.keystone4j.admin.v3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonMappingException;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

import com.infinities.keystone4j.JsonUtils;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.auth.model.AuthV3;
import com.infinities.keystone4j.auth.model.AuthV3Wrapper;
import com.infinities.keystone4j.auth.model.Identity;
import com.infinities.keystone4j.auth.model.Password;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.TrustApi;

public class AuthResourceTest extends JerseyTest {

	@Override
	protected Application configure() {
		Mockery context = new JUnit4Mockery() {

			{
				setImposteriser(ClassImposteriser.INSTANCE);
				setThreadingPolicy(new Synchroniser());
			}
		};

		enable(TestProperties.LOG_TRAFFIC);
		enable(TestProperties.DUMP_ENTITY);
		final TokenApi tokenApi = context.mock(TokenApi.class);
		final TokenProviderApi tokenProviderApi = context.mock(TokenProviderApi.class);
		final AssignmentApi assignmentApi = context.mock(AssignmentApi.class);
		final IdentityApi identityApi = context.mock(IdentityApi.class);
		final PolicyApi policyApi = context.mock(PolicyApi.class);
		final TrustApi trustApi = context.mock(TrustApi.class);
		context.checking(new Expectations() {

			{
				// exactly(1).of(tokenApi).getToken(INVALID_TOKEN);
				// will(returnValue(null));

			}
		});

		return new AuthResourceTestApplication(tokenApi, tokenProviderApi, assignmentApi, identityApi, policyApi, trustApi);

	}

	@Test
	public void testAuthenticateForToken() throws JsonGenerationException, JsonMappingException, IOException {
		AuthV3 auth = new AuthV3();
		Identity identity = new Identity();
		identity.getMethods().add("password");
		Password password = new Password();
		User user = new User();
		user.setId("0ca8f6");
		user.setPassword("secrete");
		password.setUser(user);
		identity.getAuthMethods().put("password", password);
		auth.setIdentity(identity);
		AuthV3Wrapper wrapper = new AuthV3Wrapper(auth);
		String json = JsonUtils.toJson(wrapper);
		System.out.println(json);
		JsonNode node = JsonUtils.convertToJsonNode(json);
		JsonNode authJson = node.get("auth");
		assertNotNull(authJson);
		JsonNode identityJson = authJson.get("identity");
		assertNotNull(identityJson);
		JsonNode methodsJson = identityJson.get("methods");
		assertEquals(1, methodsJson.size());
		assertEquals("password", methodsJson.get(0).asText());
		JsonNode passwordJson = identityJson.get("password");
		assertNotNull(passwordJson);
		JsonNode userJson = passwordJson.get("user");
		assertNotNull(userJson);
		assertEquals("0ca8f6", userJson.get("id").asText());
		assertEquals("secrete", userJson.get("password").asText());

		Response response = target("/v3/auth/tokens").register(JacksonFeature.class).request()
				.post(Entity.entity(wrapper, MediaType.APPLICATION_JSON_TYPE));
		assertEquals(200, response.getStatus());
	}

	// @Test
	// public void testCheckToken() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testRevokeToken() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testValidateToken() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testGetRevocationList() {
	// fail("Not yet implemented");
	// }
}
