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
