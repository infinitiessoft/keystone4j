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
package com.infinities.keystone4j;

import java.net.URI;

import javax.servlet.ServletRegistration;
import javax.ws.rs.ProcessingException;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.servlet.WebappContext;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ApplicationHandler;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.test.spi.TestContainer;
import org.glassfish.jersey.test.spi.TestContainerException;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GrizzlyWebContainerFactory implements TestContainerFactory {

	private static class GrizzlyTestContainer implements TestContainer {

		private final URI uri;
		private final ApplicationHandler appHandler;
		private HttpServer server;
		private static final Logger LOGGER = LoggerFactory.getLogger(GrizzlyTestContainer.class);


		private GrizzlyTestContainer(URI uri, ApplicationHandler appHandler) {
			this.appHandler = appHandler;
			this.uri = uri;
		}

		@Override
		public ClientConfig getClientConfig() {
			return null;
		}

		@Override
		public URI getBaseUri() {
			return uri;
		}

		@Override
		public void start() {
			LOGGER.debug("Starting GrizzlyTestContainer...");

			try {
				this.server = GrizzlyHttpServerFactory.createHttpServer(uri, appHandler);
				// Initialize and register Jersey Servlet
				WebappContext context = new WebappContext("WebappContext", "");
				ServletRegistration registration = context.addServlet("ServletContainer", ServletContainer.class);
				registration.setInitParameter("javax.ws.rs.Application", appHandler.getConfiguration().getApplication()
						.getClass().getName());
				// Add an init parameter - this could be loaded from a parameter
				// in the constructor
				registration.setInitParameter("myparam", "myvalue");
				registration.addMapping("/*");
				context.deploy(server);

			} catch (ProcessingException e) {
				throw new TestContainerException(e);
			}
		}

		@Override
		public void stop() {
			LOGGER.info("Stopping GrizzlyTestContainer...");
			this.server.shutdownNow();
		}
	}


	@Override
	public TestContainer create(URI baseUri, ApplicationHandler application) throws IllegalArgumentException {
		return new GrizzlyTestContainer(baseUri, application);
	}
}
