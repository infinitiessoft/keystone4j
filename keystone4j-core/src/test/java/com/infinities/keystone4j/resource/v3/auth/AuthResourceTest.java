package com.infinities.keystone4j.resource.v3.auth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.auth.model.AuthContext;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.auth.AuthV3;
import com.infinities.keystone4j.model.auth.AuthV3Wrapper;
import com.infinities.keystone4j.model.auth.Identity;
import com.infinities.keystone4j.model.auth.Password;
import com.infinities.keystone4j.model.auth.TokenMetadata;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.model.token.Token;
import com.infinities.keystone4j.model.token.TokenData;
import com.infinities.keystone4j.model.token.TokenDataWrapper;
import com.infinities.keystone4j.model.trust.Trust;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.TrustApi;
import com.infinities.keystone4j.utils.jackson.JacksonFeature;
import com.infinities.keystone4j.utils.jackson.JsonUtils;
import com.infinities.keystone4j.utils.jackson.ObjectMapperResolver;

public class AuthResourceTest extends JerseyTest {

	private User user, returnUser;
	private Domain domain;
	private Identity identity;
	private Password password;
	private TokenMetadata tokenMetadata;
	private String tokenid = "newtokenid";
	private TokenData tokenData;
	private Date date;
	private Token token;
	private Mockery context;
	private TokenApi tokenApi;
	private TokenProviderApi tokenProviderApi;
	private AssignmentApi assignmentApi;
	private IdentityApi identityApi;
	private PolicyApi policyApi;
	private TrustApi trustApi;


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
		user = new User();
		user.setId("0ca8f6");
		user.setPassword("secrete");
		user.setDomain(domain);
		domain = new Domain();
		domain.setId("domainid");
		identity = new Identity();
		identity.getMethods().add("password");
		password = new Password();
		password.setUser(user);
		identity.getAuthMethods().put("password", password);
		date = new Date();
		tokenid = "newtokenid";
		tokenData = new TokenData();
		tokenData.setExpireAt(date);
		tokenData.setIssuedAt(date);
		tokenData.setMethods(identity.getMethods());
		token = new Token();
		token.setExpires(date);
		token.setIssueAt(date);
		token.setId(tokenid);
		token.setUser(user);
		tokenData.setToken(token);
		returnUser = new User();
		returnUser.setId("0ca8f6");
		returnUser.setPassword("secrete");
		returnUser.setDomain(domain);
		tokenData.setUser(returnUser);
		tokenMetadata = new TokenMetadata(tokenid, new TokenDataWrapper(tokenData));

		return new AuthResourceTestApplication(tokenApi, tokenProviderApi, assignmentApi, identityApi, policyApi, trustApi);

	}

	@Test
	public void testAuthenticateForToken() throws JsonGenerationException, JsonMappingException, IOException {
		context.checking(new Expectations() {

			{
				exactly(2).of(identityApi).getUser("0ca8f6", null);
				will(returnValue(returnUser));

				exactly(1).of(identityApi).authenticate(user.getId(), user.getPassword(), domain.getId());

				exactly(1).of(tokenProviderApi).issueV3Token(with(any(String.class)), with(equal(identity.getMethods())),
						with(aNull(Date.class)), with(aNull(String.class)), with(aNull(String.class)),
						with(any(AuthContext.class)), with(aNull(Trust.class)), with(any(Token.class)), with(equal(true)));
				will(returnValue(tokenMetadata));

			}
		});

		SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		iso8601Format.setTimeZone(TimeZone.getTimeZone("GMT"));
		String iso8601Date = iso8601Format.format(date);

		AuthV3 auth = new AuthV3();
		auth.setIdentity(identity);
		AuthV3Wrapper wrapper = new AuthV3Wrapper(auth);
		String json = JsonUtils.toJson(wrapper);
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
		assertEquals(201, response.getStatus());
		assertEquals(token.getId(), response.getHeaderString("X-Subject-Token"));

		node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		JsonNode tokenJ = node.get("token");
		assertNotNull(tokenJ);
		assertEquals(iso8601Date, tokenJ.get("expires_at").asText());
		assertEquals(iso8601Date, tokenJ.get("issued_at").asText());
		assertEquals("password", tokenJ.get("methods").get(0).asText());

		JsonNode userJ = tokenJ.get("user");
		assertEquals(user.getId(), userJ.get("id").asText());
		assertEquals(domain.getId(), userJ.get("domain").get("id").asText());
	}

	@Test
	public void testCheckToken() {
		final String subjectToken = "subject-token";

		context.checking(new Expectations() {

			{
				exactly(1).of(tokenProviderApi).checkV3Token(subjectToken);
			}
		});
		Response response = target("/v3/auth/tokens").register(JacksonFeature.class).register(ObjectMapperResolver.class)
				.request().header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText())
				.header("X-Subject-Token", subjectToken).head();
		assertEquals(204, response.getStatus());
	}

	@Test
	public void testRevokeToken() {
		final String subjectToken = "subject-token";

		context.checking(new Expectations() {

			{
				exactly(1).of(tokenProviderApi).revokeToken(subjectToken);
			}
		});
		Response response = target("/v3/auth/tokens").register(JacksonFeature.class).register(ObjectMapperResolver.class)
				.request().header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText())
				.header("X-Subject-Token", subjectToken).delete();
		assertEquals(204, response.getStatus());
	}

	@Test
	public void testValidateToken() throws JsonProcessingException, IOException {
		SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		iso8601Format.setTimeZone(TimeZone.getTimeZone("GMT"));
		String iso8601Date = iso8601Format.format(date);
		final String subjectToken = "subject-token";

		context.checking(new Expectations() {

			{
				exactly(1).of(tokenProviderApi).validateV3Token(subjectToken);
				will(returnValue(new TokenDataWrapper(tokenData)));
			}
		});
		Response response = target("/v3/auth/tokens").register(JacksonFeature.class).register(ObjectMapperResolver.class)
				.request().header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText())
				.header("X-Subject-Token", subjectToken).get();
		assertEquals(200, response.getStatus());

		assertEquals(subjectToken, response.getHeaderString("X-Subject-Token"));

		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		JsonNode tokenJ = node.get("token");
		assertNotNull(tokenJ);
		assertEquals(iso8601Date, tokenJ.get("expires_at").asText());
		assertEquals(iso8601Date, tokenJ.get("issued_at").asText());
		assertEquals("password", tokenJ.get("methods").get(0).asText());

		JsonNode userJ = tokenJ.get("user");
		assertEquals(user.getId(), userJ.get("id").asText());
		assertEquals(domain.getId(), userJ.get("domain").get("id").asText());
	}

	@Test
	public void testGetRevocationList() {
		// TODO no example in openstack api.
		// fail("Not yet implemented");
	}
}
