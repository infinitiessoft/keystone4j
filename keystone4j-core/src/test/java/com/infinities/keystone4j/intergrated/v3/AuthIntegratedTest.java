package com.infinities.keystone4j.intergrated.v3;

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

import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.infinities.keystone4j.KeystoneApplication;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.auth.model.AuthV3;
import com.infinities.keystone4j.auth.model.AuthV3Wrapper;
import com.infinities.keystone4j.auth.model.Identity;
import com.infinities.keystone4j.auth.model.Password;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.token.model.Token;
import com.infinities.keystone4j.token.model.TokenData;
import com.infinities.keystone4j.utils.jackson.JacksonFeature;
import com.infinities.keystone4j.utils.jackson.JsonUtils;
import com.infinities.keystone4j.utils.jackson.ObjectMapperResolver;

public class AuthIntegratedTest extends AbstractIntegratedTest {

	private User user, returnUser;
	private Domain domain;
	private Identity identity;
	private Password password;
	private String tokenid = "newtokenid";
	private TokenData tokenData;
	private Date date;
	private Token token;


	// private final static Logger logger =
	// LoggerFactory.getLogger(AuthIntegratedTest.class);

	@Override
	protected Application configure() {
		enable(TestProperties.LOG_TRAFFIC);
		enable(TestProperties.DUMP_ENTITY);
		setting();
		return new KeystoneApplication();
	}

	// @Before
	public void setting() {
		user = new User();
		user.setId("0f3328f8-a7e7-41b4-830d-be8fdd5186c7");
		user.setPassword("admin");
		// user.setDomain(domain);
		domain = new Domain();
		domain.setId("default");
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
	}

	@Test
	public void testAuthenticateForToken() throws JsonGenerationException, JsonMappingException, IOException {
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
		assertEquals("0f3328f8-a7e7-41b4-830d-be8fdd5186c7", userJson.get("id").asText());
		assertEquals("admin", userJson.get("password").asText());
		// logger.debug("----------------start test");
		Response response = target("/v3/auth/tokens").register(JacksonFeature.class).register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request()
				// .header("X-Auth-Token",
				// Config.Instance.getOpt(Config.Type.DEFAULT,
				// "admin_token").asText())
				.post(Entity.entity(wrapper, MediaType.APPLICATION_JSON_TYPE));
		// logger.debug("----------------end test");
		assertEquals(201, response.getStatus());
		assertNotNull(response.getHeaderString("X-Subject-Token"));

		node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		JsonNode tokenJ = node.get("token");
		assertNotNull(tokenJ);
		assertNotNull(tokenJ.get("expires_at").asText());
		assertNotNull(tokenJ.get("issued_at").asText());
		assertEquals("password", tokenJ.get("methods").get(0).asText());

		JsonNode userJ = tokenJ.get("user");
		assertEquals(user.getId(), userJ.get("id").asText());
		assertEquals(domain.getId(), userJ.get("domain").get("id").asText());
	}

	@Test
	public void testCheckToken() {
		final String subjectToken = "708bb4f9-9d3c-46af-b18c-7033dc012f11";

		Response response = target("/v3/auth/tokens").register(JacksonFeature.class).register(ObjectMapperResolver.class)
				.request().header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText())
				.header("X-Subject-Token", subjectToken).head();
		assertEquals(204, response.getStatus());
	}

	@Test
	public void testRevokeToken() {
		final String subjectToken = "708bb4f9-9d3c-46af-b18c-7033dc022f11";
		Response response = target("/v3/auth/tokens").register(JacksonFeature.class).register(ObjectMapperResolver.class)
				.request().header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText())
				.header("X-Subject-Token", subjectToken).delete();
		assertEquals(204, response.getStatus());
	}

	@Test
	public void testValidateToken() throws JsonProcessingException, IOException {
		SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		iso8601Format.setTimeZone(TimeZone.getTimeZone("GMT"));
		// String iso8601Date = iso8601Format.format(date);
		final String subjectToken = "708bb4f9-9d3c-46af-b18c-7033dc022f11";

		Response response = target("/v3/auth/tokens").register(JacksonFeature.class).register(ObjectMapperResolver.class)
				.request().header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText())
				.header("X-Subject-Token", subjectToken).get();
		assertEquals(200, response.getStatus());

		assertEquals(subjectToken, response.getHeaderString("X-Subject-Token"));

		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		JsonNode tokenJ = node.get("token");
		assertNotNull(tokenJ);
		assertNotNull(tokenJ.get("expires_at").asText());
		assertNotNull(tokenJ.get("issued_at").asText());
		assertEquals("password", tokenJ.get("methods").get(0).asText());

		JsonNode userJ = tokenJ.get("user");
		assertEquals(user.getId(), userJ.get("id").asText());
		assertEquals(domain.getId(), userJ.get("domain_id").asText());
	}

	// @Test
	public void testGetRevocationList() {
		// TODO no example in openstack api.
		// fail("Not yet implemented");
	}
}
