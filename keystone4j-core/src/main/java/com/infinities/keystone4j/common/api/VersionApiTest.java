package com.infinities.keystone4j.common.api;

import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.net.URL;

import javax.ws.rs.container.ContainerRequestContext;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class VersionApiTest {

	private Mockery context;
	private VersionApi versionApi;
	private ContainerRequestContext requestContext;


	@Before
	public void setUp() throws Exception {
		context = new JUnit4Mockery() {

			{
				setImposteriser(ClassImposteriser.INSTANCE);
				setThreadingPolicy(new Synchroniser());
			}
		};
		versionApi = new VersionApi("admin");
		requestContext = context.mock(ContainerRequestContext.class);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetIdentityURL() throws MalformedURLException {
		URL url = versionApi.getIdentityURL(requestContext, "v2");
		assertEquals("http://localhost:35357/v2/", url.toString());
	}
}
