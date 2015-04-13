package com.infinities.keystone4j.resource.v2.token;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertPathBuilderException;
import java.security.cert.CertificateException;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bouncycastle.cms.CMSException;
import org.bouncycastle.operator.OperatorCreationException;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.model.token.Auth;
import com.infinities.keystone4j.model.token.PasswordCredentials;
import com.infinities.keystone4j.model.token.wrapper.AuthWrapper;
import com.infinities.keystone4j.model.trust.wrapper.SignedWrapper;
import com.infinities.keystone4j.utils.Cms;
import com.infinities.keystone4j.utils.JsonUtils;
import com.infinities.keystone4j.utils.jackson.JacksonFeature;
import com.infinities.keystone4j.utils.jackson.ObjectMapperResolver;

public class TokenResourceTest extends JerseyTest {

	@Override
	protected Application configure() {
		enable(TestProperties.LOG_TRAFFIC);
		enable(TestProperties.DUMP_ENTITY);
		return new TokenResourceTestApplication();

	}

	@Test
	public void testAuthenticate() throws JsonGenerationException, JsonMappingException, IOException {
		PasswordCredentials credentials = new PasswordCredentials();
		credentials.setUsername("admin");
		credentials.setPassword("f00@bar");
		Auth auth = new Auth();
		auth.setTenantName("admin");
		auth.setPasswordCredentials(credentials);
		AuthWrapper authWrapper = new AuthWrapper();
		authWrapper.setAuth(auth);
		Response response = target("/v2.0/tokens").register(JacksonFeature.class).request()
				.post(Entity.entity(authWrapper, MediaType.APPLICATION_JSON_TYPE));
		assertEquals(200, response.getStatus());
		String ret = response.readEntity(String.class);
		System.err.println(ret);
	}

	@Test
	public void testGetRevocationList() throws CertificateException, OperatorCreationException, NoSuchAlgorithmException,
			NoSuchProviderException, CertPathBuilderException, InvalidAlgorithmParameterException, CMSException, IOException {
		Response response = target("/v2.0/tokens/revoked").register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());
		SignedWrapper signedWrapper = response.readEntity(SignedWrapper.class);
		String formatted = signedWrapper.getSigned().replace("-----BEGIN CMS-----", "").replace("-----END CMS-----", "")
				.trim();
		String result = Cms.Instance.verifySignature(formatted.getBytes(),
				Config.Instance.getOpt(Config.Type.signing, "certfile").asText(),
				Config.Instance.getOpt(Config.Type.signing, "ca_certs").asText());
		System.err.println(result);
	}

	@Test
	public void testValidateToken() throws JsonProcessingException, IOException {
		PasswordCredentials credentials = new PasswordCredentials();
		credentials.setUsername("admin");
		credentials.setPassword("f00@bar");
		Auth auth = new Auth();
		auth.setTenantName("admin");
		auth.setPasswordCredentials(credentials);
		AuthWrapper authWrapper = new AuthWrapper();
		authWrapper.setAuth(auth);
		Response response = target("/v2.0/tokens").register(JacksonFeature.class).request()
				.post(Entity.entity(authWrapper, MediaType.APPLICATION_JSON_TYPE));
		assertEquals(200, response.getStatus());
		String ret = response.readEntity(String.class);
		JsonNode node = JsonUtils.convertToJsonNode(ret);
		System.err.println(ret);
		String tokenid = node.get("access").get("token").get("id").asText();
		response.close();
		Response response2 = target("/v2.0/tokens/" + tokenid).register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response2.getStatus());
		ret = response2.readEntity(String.class);
		System.err.println(ret);
		response2.close();
		node = JsonUtils.convertToJsonNode(ret);

	}

	@Test
	public void testValidateTokenHead() throws JsonProcessingException, IOException {
		PasswordCredentials credentials = new PasswordCredentials();
		credentials.setUsername("admin");
		credentials.setPassword("f00@bar");
		Auth auth = new Auth();
		auth.setTenantName("admin");
		auth.setPasswordCredentials(credentials);
		AuthWrapper authWrapper = new AuthWrapper();
		authWrapper.setAuth(auth);
		Response response = target("/v2.0/tokens").register(JacksonFeature.class).request()
				.post(Entity.entity(authWrapper, MediaType.APPLICATION_JSON_TYPE));
		assertEquals(200, response.getStatus());
		String ret = response.readEntity(String.class);
		JsonNode node = JsonUtils.convertToJsonNode(ret);
		System.err.println(ret);
		String tokenid = node.get("access").get("token").get("id").asText();
		System.err.println(tokenid);
		response = target("/v2.0/tokens/" + tokenid).register(JacksonFeature.class).register(ObjectMapperResolver.class)
				.request().header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText())
				.head();
		// response = target("/v2.0/tokens/" +
		// tokenid).register(JacksonFeature.class).request()
		// .post(Entity.entity(authWrapper, MediaType.APPLICATION_JSON_TYPE));
		assertEquals(204, response.getStatus());
		ret = response.readEntity(String.class);
		node = JsonUtils.convertToJsonNode(ret);
		System.err.println(ret);
	}

	@Test
	public void testDeleteToken() throws JsonProcessingException, IOException {
		PasswordCredentials credentials = new PasswordCredentials();
		credentials.setUsername("admin");
		credentials.setPassword("f00@bar");
		Auth auth = new Auth();
		auth.setTenantName("admin");
		auth.setPasswordCredentials(credentials);
		AuthWrapper authWrapper = new AuthWrapper();
		authWrapper.setAuth(auth);
		Response response = target("/v2.0/tokens").register(JacksonFeature.class).request()
				.post(Entity.entity(authWrapper, MediaType.APPLICATION_JSON_TYPE));
		assertEquals(200, response.getStatus());
		String ret = response.readEntity(String.class);
		JsonNode node = JsonUtils.convertToJsonNode(ret);
		System.err.println(ret);
		String tokenid = node.get("access").get("token").get("id").asText();
		System.err.println(tokenid);
		response = target("/v2.0/tokens/" + tokenid).register(JacksonFeature.class).register(ObjectMapperResolver.class)
				.request().header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText())
				.delete();
		// response = target("/v2.0/tokens/" +
		// tokenid).register(JacksonFeature.class).request()
		// .post(Entity.entity(authWrapper, MediaType.APPLICATION_JSON_TYPE));
		assertEquals(204, response.getStatus());
		ret = response.readEntity(String.class);
		node = JsonUtils.convertToJsonNode(ret);
		System.err.println(ret);
	}

	@Test
	public void testGetEndpoints() throws JsonProcessingException, IOException {
		PasswordCredentials credentials = new PasswordCredentials();
		credentials.setUsername("admin");
		credentials.setPassword("f00@bar");
		Auth auth = new Auth();
		auth.setTenantName("admin");
		auth.setPasswordCredentials(credentials);
		AuthWrapper authWrapper = new AuthWrapper();
		authWrapper.setAuth(auth);

		Response response = target("/v2.0/tokens").register(JacksonFeature.class).request()
				.post(Entity.entity(authWrapper, MediaType.APPLICATION_JSON_TYPE));
		assertEquals(200, response.getStatus());
		String ret = response.readEntity(String.class);
		response.close();
		JsonNode node = JsonUtils.convertToJsonNode(ret);
		System.err.println(ret);
		String tokenid = node.get("access").get("token").get("id").asText();
		System.err.println(tokenid);
		response = target("/v2.0/tokens/" + tokenid + "/endpoints").register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());
		ret = response.readEntity(String.class);
		node = JsonUtils.convertToJsonNode(ret);
		assertEquals(1, node.get("endpoints").size());
		response.close();
	}

}
