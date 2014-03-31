package com.infinities.keystone4j.resource.v3.filter;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.filter.Middleware;
import com.infinities.keystone4j.token.TokenApi;

public class AuthContextMiddlewareTest extends JerseyTest {

	// private final static String INVALID_TOKEN = "invalid_token";

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
		context.checking(new Expectations() {

			{
				exactly(1).of(tokenApi).getToken("invalid");
				will(returnValue(null));

			}
		});
		return new AuthContextMiddlewareTestApplication(tokenApi);
	}

	@Test
	public void testWithNoToken() {
		Response response = target("/").request().get();
		assertEquals(300, response.getStatus());
	}

	@Test
	public void testWithAdminToken() {
		Response response = target("/").request()
				.header(Middleware.AUTH_TOKEN_HEADER, Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText())
				.get();
		assertEquals(300, response.getStatus());
	}

	@Test
	public void testWithInvalidToken() {
		Response response = target("/").request().header(Middleware.AUTH_TOKEN_HEADER, "invalid").get();
		assertEquals(401, response.getStatus());
	}

}
