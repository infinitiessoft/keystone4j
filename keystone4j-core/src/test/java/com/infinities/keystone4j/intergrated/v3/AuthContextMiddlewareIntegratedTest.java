package com.infinities.keystone4j.intergrated.v3;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;

import com.infinities.keystone4j.KeystoneApplication;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.filter.Middleware;

public class AuthContextMiddlewareIntegratedTest extends AbstractIntegratedTest {

	// private final static String INVALID_TOKEN = "invalid_token";

	@Override
	protected Application configure() {
		enable(TestProperties.LOG_TRAFFIC);
		enable(TestProperties.DUMP_ENTITY);

		return new KeystoneApplication();
	}

	@Test
	public void testWithNoToken() {
		Response response = target("/v3/users").request().get();
		assertEquals(401, response.getStatus());
	}

	@Test
	public void testWithAdminToken() {
		Response response = target("/v3/users").request()
				.header(Middleware.AUTH_TOKEN_HEADER, Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText())
				.get();
		assertEquals(200, response.getStatus());
	}

	@Test
	public void testWithInvalidToken() {
		Response response = target("/v3/users").request().header(Middleware.AUTH_TOKEN_HEADER, "invalid").get();
		assertEquals(401, response.getStatus());
	}

}
