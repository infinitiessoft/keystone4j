/*******************************************************************************
 * Copyright 2015 InfinitiesSoft Solutions Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/

package com.infinities.keystone4j.middleware;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.Invocation;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.action.CustomAction;
import org.jmock.lib.concurrent.Synchroniser;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.infinities.keystone4j.client.Config;
import com.infinities.keystone4j.middleware.AuthProtocol.TokenEditor;
import com.infinities.keystone4j.middleware.model.RevokedToken;
import com.infinities.keystone4j.middleware.model.TokenWrapper;
import com.infinities.keystone4j.middleware.model.wrapper.AccessWrapper;
import com.infinities.keystone4j.middleware.model.wrapper.AuthWrapper;
import com.infinities.keystone4j.utils.JsonUtils;

public class AuthProtocolTest {

	protected Mockery context = new JUnit4Mockery() {

		{
			setThreadingPolicy(new Synchroniser());
			setImposteriser(ClassImposteriser.INSTANCE);
		}
	};
	private AuthProtocol auth;
	private ContainerRequestContext requestContext;
	private MultivaluedMap<String, String> headers;
	private String tokenid;
	private Client client;
	private WebTarget target;
	private Builder builder;
	private Response response;
	private AccessWrapper accessWrapper;
	private RevokedToken revokedWrapper;


	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		URL tokenUrl = Thread.currentThread().getContextClassLoader().getResource("token.txt");
		tokenid = Resources.toString(tokenUrl, Charsets.UTF_8);
		auth = new AuthProtocol();
		requestContext = context.mock(ContainerRequestContext.class);
		headers = context.mock(MultivaluedMap.class);
		target = context.mock(WebTarget.class);
		builder = context.mock(Builder.class);
		response = context.mock(Response.class);

		context.checking(new Expectations() {

			{
				allowing(requestContext).getHeaders();
				will(returnValue(headers));
			}
		});
		client = context.mock(Client.class);
		auth.identityServer.client = client;
		URL authUrl = Thread.currentThread().getContextClassLoader().getResource("authwrapper.txt");
		accessWrapper = JsonUtils.readJson(new File(authUrl.getPath()), AccessWrapper.class);
		URL revokedUrl = Thread.currentThread().getContextClassLoader().getResource("revokedwrapper.txt");
		revokedWrapper = JsonUtils.readJson(new File(revokedUrl.getPath()), RevokedToken.class);

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCall401() throws IOException {
		// remove headers
		context.checking(new Expectations() {

			{
				oneOf(headers).remove("X-Identity-Status");
				will(returnValue(null));
				oneOf(headers).remove("X-Domain-Id");
				will(returnValue(null));
				oneOf(headers).remove("X-Domain-Name");
				will(returnValue(null));
				oneOf(headers).remove("X-Project-Id");
				will(returnValue(null));
				oneOf(headers).remove("X-Project-Name");
				will(returnValue(null));
				oneOf(headers).remove("X-Project-Domain-Id");
				will(returnValue(null));
				oneOf(headers).remove("X-Project-Domain-Name");
				will(returnValue(null));
				oneOf(headers).remove("X-User-Id");
				will(returnValue(null));
				oneOf(headers).remove("X-User-Name");
				will(returnValue(null));
				oneOf(headers).remove("X-User-Domain-Id");
				will(returnValue(null));
				oneOf(headers).remove("X-User-Domain-Name");
				will(returnValue(null));
				oneOf(headers).remove("X-Roles");
				will(returnValue(null));
				oneOf(headers).remove("X-Service-Catalog");
				will(returnValue(null));
				oneOf(headers).remove("X-User");
				will(returnValue(null));
				oneOf(headers).remove("X-Tenant-Id");
				will(returnValue(null));
				oneOf(headers).remove("X-Tenant-Name");
				will(returnValue(null));
				oneOf(headers).remove("X-Tenant");
				will(returnValue(null));
				oneOf(headers).remove("X-Role");
				will(returnValue(null));
			}
		});
		// get token
		context.checking(new Expectations() {

			{
				oneOf(requestContext).getHeaderString("X-Storage-Token");
				will(returnValue(null));
				oneOf(requestContext).getHeaderString("X-Auth-Token");
				will(returnValue(tokenid));
			}
		});

		String auth_host = Config.Instance.getOpt(Config.Type.keystone_authtoken, "auth_host").asText();
		int auth_port = Config.Instance.getOpt(Config.Type.keystone_authtoken, "auth_port").asInteger();
		String auth_protocol = Config.Instance.getOpt(Config.Type.keystone_authtoken, "auth_protocol").asText();

		final String identityUri = String.format("%s://%s:%s", auth_protocol, auth_host, auth_port);

		// requestAdminToken
		context.checking(new Expectations() {

			{
				exactly(2).of(client).target(identityUri);
				will(returnValue(target));
				exactly(2).of(target).path("/v2.0/tokens");
				will(returnValue(target));
				exactly(2).of(target).request(MediaType.APPLICATION_JSON_TYPE);
				will(returnValue(builder));
				exactly(2).of(builder).accept(MediaType.APPLICATION_JSON_TYPE);
				will(returnValue(builder));
				exactly(2).of(builder).method(with(any(String.class)), with(any(Entity.class)));
				will(new BuilderAction1("request admin token"));
				exactly(2).of(response).readEntity(AccessWrapper.class);
				will(returnValue(accessWrapper));

				oneOf(requestContext).abortWith(with(any(Response.class)));
			}
		});

		// requestAdminToken
		context.checking(new Expectations() {

			{
				exactly(2).of(client).target(identityUri);
				will(returnValue(target));
				exactly(2).of(target).path("/v2.0/tokens/revoked");
				will(returnValue(target));
				exactly(2).of(target).request(MediaType.APPLICATION_JSON_TYPE);
				will(returnValue(builder));
				exactly(2).of(builder).accept(MediaType.APPLICATION_JSON_TYPE);
				will(returnValue(builder));
				exactly(2).of(builder).header("X-Auth-Token", accessWrapper.getAccess().getToken().getId());
				will(returnValue(builder));
				exactly(2).of(builder).method("GET");
				will(returnValue(response));
				exactly(3).of(response).getStatus();
				will(returnValue(401));
				oneOf(headers).remove("X-Identity-Status");
				will(returnValue(null));
			}
		});

		auth.call(requestContext);
	}

	@Test
	public void testCall() throws IOException {
		// remove headers
		context.checking(new Expectations() {

			{
				oneOf(headers).remove("X-Identity-Status");
				will(returnValue(null));
				oneOf(headers).remove("X-Domain-Id");
				will(returnValue(null));
				oneOf(headers).remove("X-Domain-Name");
				will(returnValue(null));
				oneOf(headers).remove("X-Project-Id");
				will(returnValue(null));
				oneOf(headers).remove("X-Project-Name");
				will(returnValue(null));
				oneOf(headers).remove("X-Project-Domain-Id");
				will(returnValue(null));
				oneOf(headers).remove("X-Project-Domain-Name");
				will(returnValue(null));
				oneOf(headers).remove("X-User-Id");
				will(returnValue(null));
				oneOf(headers).remove("X-User-Name");
				will(returnValue(null));
				oneOf(headers).remove("X-User-Domain-Id");
				will(returnValue(null));
				oneOf(headers).remove("X-User-Domain-Name");
				will(returnValue(null));
				oneOf(headers).remove("X-Roles");
				will(returnValue(null));
				oneOf(headers).remove("X-Service-Catalog");
				will(returnValue(null));
				oneOf(headers).remove("X-User");
				will(returnValue(null));
				oneOf(headers).remove("X-Tenant-Id");
				will(returnValue(null));
				oneOf(headers).remove("X-Tenant-Name");
				will(returnValue(null));
				oneOf(headers).remove("X-Tenant");
				will(returnValue(null));
				oneOf(headers).remove("X-Role");
				will(returnValue(null));
			}
		});
		// get token
		context.checking(new Expectations() {

			{
				oneOf(requestContext).getHeaderString("X-Storage-Token");
				will(returnValue(null));
				oneOf(requestContext).getHeaderString("X-Auth-Token");
				will(returnValue(tokenid));
			}
		});

		String auth_host = Config.Instance.getOpt(Config.Type.keystone_authtoken, "auth_host").asText();
		int auth_port = Config.Instance.getOpt(Config.Type.keystone_authtoken, "auth_port").asInteger();
		String auth_protocol = Config.Instance.getOpt(Config.Type.keystone_authtoken, "auth_protocol").asText();

		final String identityUri = String.format("%s://%s:%s", auth_protocol, auth_host, auth_port);

		// requestAdminToken
		context.checking(new Expectations() {

			{
				oneOf(client).target(identityUri);
				will(returnValue(target));
				oneOf(target).path("/v2.0/tokens");
				will(returnValue(target));
				oneOf(target).request(MediaType.APPLICATION_JSON_TYPE);
				will(returnValue(builder));
				oneOf(builder).accept(MediaType.APPLICATION_JSON_TYPE);
				will(returnValue(builder));
				oneOf(builder).method(with(any(String.class)), with(any(Entity.class)));
				will(new BuilderAction1("request admin token"));
				oneOf(response).readEntity(AccessWrapper.class);
				will(returnValue(accessWrapper));
			}
		});

		// fetch revoked token list
		context.checking(new Expectations() {

			{
				exactly(2).of(client).target(identityUri);
				will(returnValue(target));
				exactly(1).of(target).path("");
				will(returnValue(target));
				exactly(1).of(target).path("/v2.0/tokens/revoked");
				will(returnValue(target));
				exactly(2).of(target).request(MediaType.APPLICATION_JSON_TYPE);
				will(returnValue(builder));
				exactly(2).of(builder).accept(MediaType.APPLICATION_JSON_TYPE);
				will(returnValue(builder));
				exactly(1).of(builder).header("X-Auth-Token", accessWrapper.getAccess().getToken().getId());
				will(returnValue(builder));
				exactly(2).of(builder).method("GET");
				will(returnValue(response));
				exactly(3).of(response).getStatus();
				will(returnValue(200));
				exactly(1).of(response).readEntity(RevokedToken.class);
				will(returnValue(revokedWrapper));
				exactly(1).of(requestContext).setProperty(with(any(String.class)), with(any(AccessWrapper.class)));

			}
		});

		final String catalog = JsonUtils.toString(accessWrapper.getAccess().getServiceCatalog());

		// add headers
		context.checking(new Expectations() {

			{
				oneOf(headers).add("X-Project-Id", accessWrapper.getAccess().getToken().getTenant().getId());
				oneOf(headers).add("X-Tenant", accessWrapper.getAccess().getToken().getTenant().getName());
				oneOf(headers).add("X-Identity-Status", "Confirmed");
				oneOf(headers).add("X-Domain-Id", null);
				oneOf(headers).add("X-Domain-Name", null);
				oneOf(headers).add("X-Project-Name", accessWrapper.getAccess().getToken().getTenant().getName());
				oneOf(headers).add("X-Project-Domain-Id", "default");
				oneOf(headers).add("X-Project-Domain-Name", "Default");
				oneOf(headers).add("X-User-Id", accessWrapper.getAccess().getUser().getId());
				oneOf(headers).add("X-User-Name", accessWrapper.getAccess().getUser().getName());
				oneOf(headers).add("X-User-Domain-Id", "default");
				oneOf(headers).add("X-User-Domain-Name", "Default");
				oneOf(headers).add("X-Roles", accessWrapper.getAccess().getUser().getRoles().get(0).getName());
				oneOf(headers).add("X-User", accessWrapper.getAccess().getUser().getName());
				oneOf(headers).add("X-Tenant-Id", accessWrapper.getAccess().getToken().getTenant().getId());
				oneOf(headers).add("X-Tenant-Name", accessWrapper.getAccess().getToken().getTenant().getName());
				oneOf(headers).add("X-Role", accessWrapper.getAccess().getUser().getRoles().get(0).getName());
				oneOf(headers).add("X-Service-Catalog", catalog);
			}
		});

		final TokenEditor tokenEditor = context.mock(TokenEditor.class);
		auth.tokenEditor = tokenEditor;

		// update expire date
		context.checking(new Expectations() {

			{
				oneOf(tokenEditor).update(with(any(TokenWrapper.class)));
				will(new CustomAction("update expire date") {

					@Override
					public Object invoke(Invocation invocation) throws Throwable {
						TokenWrapper token = (TokenWrapper) invocation.getParameter(0);
						token.getExpire().setTime(new Date());
						return null;
					}

				});

			}
		});

		auth.call(requestContext);
	}


	private class BuilderAction1 extends CustomAction {

		public BuilderAction1(String description) {
			super(description);
		}

		@SuppressWarnings("unchecked")
		@Override
		public Object invoke(Invocation invocation) throws Throwable {
			String method = (String) invocation.getParameter(0);
			Entity<AuthWrapper> entity = (Entity<AuthWrapper>) invocation.getParameter(1);
			AuthWrapper wrapper = entity.getEntity();
			assertEquals("POST", method);
			String username = Config.Instance.getOpt(Config.Type.keystone_authtoken, "admin_user").asText();
			String password = Config.Instance.getOpt(Config.Type.keystone_authtoken, "admin_password").asText();
			String tenant = Config.Instance.getOpt(Config.Type.keystone_authtoken, "admin_tenant_name").asText();
			assertEquals(tenant, wrapper.getAuth().getTenantName());
			assertEquals(username, wrapper.getAuth().getPasswordCredentials().getUsername());
			assertEquals(password, wrapper.getAuth().getPasswordCredentials().getPassword());

			return response;
		}

	}
}
