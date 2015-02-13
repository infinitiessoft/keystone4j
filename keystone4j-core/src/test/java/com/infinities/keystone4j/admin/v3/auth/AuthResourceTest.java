package com.infinities.keystone4j.admin.v3.auth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.auth.AuthV3;
import com.infinities.keystone4j.model.auth.AuthV3Wrapper;
import com.infinities.keystone4j.model.auth.Identity;
import com.infinities.keystone4j.model.auth.Password;
import com.infinities.keystone4j.model.auth.Scope;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.model.token.Token;
import com.infinities.keystone4j.utils.jackson.JacksonFeature;
import com.infinities.keystone4j.utils.jackson.JsonUtils;
import com.infinities.keystone4j.utils.jackson.ObjectMapperResolver;

public class AuthResourceTest extends JerseyTest {

	private User user, returnUser;
	private Domain domain;
	private Identity identity;
	private Password password;


	@Override
	protected Application configure() {
		enable(TestProperties.LOG_TRAFFIC);
		enable(TestProperties.DUMP_ENTITY);
		user = new User();
		user.setId("e7912c2225e84ac5905d8cf0b5040a6d");
		user.setPassword("f00@bar");
		user.setDomain(domain);
		domain = new Domain();
		domain.setId("default");
		identity = new Identity();
		identity.getMethods().add("password");
		password = new Password();
		password.setUser(user);
		identity.getAuthMethods().put("password", password);
		returnUser = new User();
		returnUser.setId("0ca8f6");
		returnUser.setPassword("secrete");
		returnUser.setDomain(domain);
		return new AuthResourceTestApplication();
	}

	@Test
	public void testAuthenticateForTokenAndCheckAndValidateAndRevokeAndCheck() throws JsonGenerationException,
			JsonMappingException, IOException {
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
		assertEquals("e7912c2225e84ac5905d8cf0b5040a6d", userJson.get("id").asText());
		assertEquals("f00@bar", userJson.get("password").asText());

		// authenticate
		Response response = target("/v3").path("auth").path("tokens").register(JacksonFeature.class).request()
				.post(Entity.entity(wrapper, MediaType.APPLICATION_JSON_TYPE));

		assertEquals(201, response.getStatus());
		String tokenid = response.getHeaderString("X-Subject-Token");

		assertNotNull(tokenid);

		node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		System.err.println(node);
		JsonNode tokenJ = node.get("token");
		assertNotNull(tokenJ);
		assertNotNull(tokenJ.get("expires_at").asText());
		assertNotNull(tokenJ.get("issued_at").asText());
		assertEquals("password", tokenJ.get("methods").get(0).asText());

		JsonNode userJ = tokenJ.get("user");
		assertEquals(user.getId(), userJ.get("id").asText());
		assertEquals(domain.getId(), userJ.get("domain").get("id").asText());

		// check
		response = target("/v3").path("auth").path("tokens").register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText())
				.header("X-Subject-Token", tokenid).head();
		assertEquals(200, response.getStatus());

		// validate
		response = target("/v3/auth/tokens").register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText())
				.header("X-Subject-Token", tokenid).get();
		assertEquals(200, response.getStatus());
		assertEquals(tokenid, response.getHeaderString("X-Subject-Token"));

		node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		tokenJ = node.get("token");
		assertNotNull(tokenJ);
		assertNotNull(tokenJ.get("expires_at").asText());
		assertNotNull(tokenJ.get("issued_at").asText());
		assertEquals("password", tokenJ.get("methods").get(0).asText());

		userJ = tokenJ.get("user");
		assertEquals(user.getId(), userJ.get("id").asText());
		assertEquals(domain.getId(), userJ.get("domain").get("id").asText());
		System.err.println(node.toString());

		// revoke
		response = target("/v3/auth/tokens").register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText())
				.header("X-Subject-Token", tokenid).delete();
		assertEquals(204, response.getStatus());

		response = target("/v3").path("auth").path("tokens").register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText())
				.header("X-Subject-Token", tokenid).head();
		assertEquals(404, response.getStatus());
	}

	@Test
	public void testAuthenticateForTokenByName() throws JsonGenerationException, JsonMappingException, IOException {
		AuthV3 auth = new AuthV3();
		user.setId(null);
		user.setName("admin");
		domain = new Domain();
		domain.setId("default");
		user.setDomain(domain);
		auth.setIdentity(identity);
		AuthV3Wrapper wrapper = new AuthV3Wrapper(auth);
		// authenticate
		Response response = target("/v3").path("auth").path("tokens").register(JacksonFeature.class).request()
				.post(Entity.entity(wrapper, MediaType.APPLICATION_JSON_TYPE));

		assertEquals(201, response.getStatus());
		System.err.println(response.readEntity(String.class));
	}

	@Test
	public void testAuthenticateForTokenByDomainName() throws JsonGenerationException, JsonMappingException, IOException {
		AuthV3 auth = new AuthV3();
		user.setId(null);
		user.setName("admin");
		domain = new Domain();
		domain.setName("Default");
		user.setDomain(domain);
		auth.setIdentity(identity);
		AuthV3Wrapper wrapper = new AuthV3Wrapper(auth);
		// authenticate
		Response response = target("/v3").path("auth").path("tokens").register(JacksonFeature.class).request()
				.post(Entity.entity(wrapper, MediaType.APPLICATION_JSON_TYPE));

		assertEquals(201, response.getStatus());
		System.err.println(response.readEntity(String.class));
	}

	@Test
	public void testAuthenticateForTokenById() throws JsonGenerationException, JsonMappingException, IOException {
		user = new User();
		user.setId("e7912c2225e84ac5905d8cf0b5040a6d");
		user.setPassword("f00@bar");
		// user.setDomain(domain);
		// domain = new Domain();
		// domain.setId("default");
		identity = new Identity();
		identity.getMethods().add("password");
		password = new Password();
		password.setUser(user);
		identity.getAuthMethods().put("password", password);
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
		assertEquals("e7912c2225e84ac5905d8cf0b5040a6d", userJson.get("id").asText());
		assertEquals("f00@bar", userJson.get("password").asText());

		// authenticate
		Response response = target("/v3").path("auth").path("tokens").register(JacksonFeature.class).request()
				.post(Entity.entity(wrapper, MediaType.APPLICATION_JSON_TYPE));

		assertEquals(201, response.getStatus());
		System.err.println(response.readEntity(String.class));
	}

	@Test
	public void testAuthenticateForTokenByToken() throws JsonGenerationException, JsonMappingException, IOException {
		AuthV3 auth = new AuthV3();
		user.setId(null);
		user.setName("admin");
		domain = new Domain();
		domain.setName("Default");
		user.setDomain(domain);
		auth.setIdentity(identity);
		AuthV3Wrapper wrapper = new AuthV3Wrapper(auth);
		// authenticate
		Response response = target("/v3").path("auth").path("tokens").register(JacksonFeature.class).request()
				.post(Entity.entity(wrapper, MediaType.APPLICATION_JSON_TYPE));

		assertEquals(201, response.getStatus());
		String tokenid = response.getHeaderString("X-Subject-Token");

		auth = new AuthV3();
		identity = new Identity();
		identity.getMethods().add("token");
		auth.setIdentity(identity);
		Token token = new Token();
		token.setId(tokenid);
		identity.setToken(token);
		wrapper = new AuthV3Wrapper(auth);
		// authenticate
		response = target("/v3").path("auth").path("tokens").register(JacksonFeature.class).request()
				.post(Entity.entity(wrapper, MediaType.APPLICATION_JSON_TYPE));

		assertEquals(201, response.getStatus());
		System.err.println(response.readEntity(String.class));
	}

	@Test
	public void testAuthenticateForTokenByIdAnsProjectScope() throws JsonGenerationException, JsonMappingException,
			IOException {
		user = new User();
		user.setId("e7912c2225e84ac5905d8cf0b5040a6d");
		user.setPassword("f00@bar");
		identity = new Identity();
		identity.getMethods().add("password");
		password = new Password();
		password.setUser(user);
		identity.getAuthMethods().put("password", password);
		Scope scope = new Scope();
		AuthV3 auth = new AuthV3();
		auth.setScope(scope);
		Project project = new Project();
		project.setId("88e550a135bb4e6da68e79e5b7c4b2f2");
		scope.setProject(project);
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
		assertEquals("e7912c2225e84ac5905d8cf0b5040a6d", userJson.get("id").asText());
		assertEquals("f00@bar", userJson.get("password").asText());

		// authenticate
		Response response = target("/v3").path("auth").path("tokens").register(JacksonFeature.class).request()
				.post(Entity.entity(wrapper, MediaType.APPLICATION_JSON_TYPE));

		assertEquals(201, response.getStatus());
		System.err.println(response.readEntity(String.class));
	}

	@Test
	public void testAuthenticateForTokenByIdAnsDomainScope() throws JsonGenerationException, JsonMappingException,
			IOException {
		user = new User();
		user.setId("e7912c2225e84ac5905d8cf0b5040a6d");
		user.setPassword("f00@bar");
		identity = new Identity();
		identity.getMethods().add("password");
		password = new Password();
		password.setUser(user);
		identity.getAuthMethods().put("password", password);
		Scope scope = new Scope();
		AuthV3 auth = new AuthV3();
		auth.setScope(scope);
		Domain d = new Domain();
		d.setId("default");
		scope.setDomain(d);
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
		assertEquals("e7912c2225e84ac5905d8cf0b5040a6d", userJson.get("id").asText());
		assertEquals("f00@bar", userJson.get("password").asText());

		// authenticate
		Response response = target("/v3").path("auth").path("tokens").register(JacksonFeature.class).request()
				.post(Entity.entity(wrapper, MediaType.APPLICATION_JSON_TYPE));

		assertEquals(201, response.getStatus());
		String ret = response.readEntity(String.class);
		node = JsonUtils.convertToJsonNode(ret);
		assertNotNull(node.get("token").get("domain"));
		System.err.println(ret);
	}

	@Test
	public void testAuthenticateForTokenByIdAnsProjectNameScope() throws JsonGenerationException, JsonMappingException,
			IOException {
		user = new User();
		user.setId("e7912c2225e84ac5905d8cf0b5040a6d");
		user.setPassword("f00@bar");
		identity = new Identity();
		identity.getMethods().add("password");
		password = new Password();
		password.setUser(user);
		identity.getAuthMethods().put("password", password);
		Scope scope = new Scope();
		AuthV3 auth = new AuthV3();
		auth.setScope(scope);
		Project project = new Project();
		project.setName("admin");
		Domain d = new Domain();
		d.setId("default");
		project.setDomain(d);
		scope.setProject(project);
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
		assertEquals("e7912c2225e84ac5905d8cf0b5040a6d", userJson.get("id").asText());
		assertEquals("f00@bar", userJson.get("password").asText());

		// authenticate
		Response response = target("/v3").path("auth").path("tokens").register(JacksonFeature.class).request()
				.post(Entity.entity(wrapper, MediaType.APPLICATION_JSON_TYPE));

		assertEquals(201, response.getStatus());
		System.err.println(response.readEntity(String.class));
	}

	@Test
	public void testAuthenticateForTokenByIdAnsProjectNameDomainNameScope() throws JsonGenerationException,
			JsonMappingException, IOException {
		user = new User();
		user.setId("e7912c2225e84ac5905d8cf0b5040a6d");
		user.setPassword("f00@bar");
		identity = new Identity();
		identity.getMethods().add("password");
		password = new Password();
		password.setUser(user);
		identity.getAuthMethods().put("password", password);
		Scope scope = new Scope();
		AuthV3 auth = new AuthV3();
		auth.setScope(scope);
		Project project = new Project();
		project.setName("admin");
		Domain d = new Domain();
		d.setName("Default");
		project.setDomain(d);
		scope.setProject(project);
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
		assertEquals("e7912c2225e84ac5905d8cf0b5040a6d", userJson.get("id").asText());
		assertEquals("f00@bar", userJson.get("password").asText());

		// authenticate
		Response response = target("/v3").path("auth").path("tokens").register(JacksonFeature.class).request()
				.post(Entity.entity(wrapper, MediaType.APPLICATION_JSON_TYPE));

		assertEquals(201, response.getStatus());
		System.err.println(response.readEntity(String.class));
	}

	@Test
	public void testCheckToken() {
		final String subjectToken = "subject-token";
		Response response = target("/v3").path("auth").path("tokens").register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText())
				.header("X-Subject-Token", subjectToken).head();
		assertEquals(404, response.getStatus());
	}

	@Test
	public void testRevokeToken() {
		final String subjectToken = "subject-token";
		Response response = target("/v3/auth/tokens").register(JacksonFeature.class).register(ObjectMapperResolver.class)
				.request().header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText())
				.header("X-Subject-Token", subjectToken).delete();
		assertEquals(404, response.getStatus());
	}

	@Test
	public void testValidateToken() throws JsonProcessingException, IOException {
		Response response = target("/v3/auth/tokens").register(JacksonFeature.class).register(ObjectMapperResolver.class)
				.request().header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText())
				.header("X-Subject-Token", "subjectToken").get();
		assertEquals(404, response.getStatus());
	}

	@Test
	public void testGetRevocationList() {
		// TODO no example in openstack api.
		// fail("Not yet implemented");
	}
}
