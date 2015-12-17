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

import javax.ws.rs.ProcessingException;

import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ApplicationHandler;
import org.glassfish.jersey.test.spi.TestContainer;
import org.glassfish.jersey.test.spi.TestContainerException;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JettyTestContainerFactory implements TestContainerFactory {

	private static class JettyTestContainer implements TestContainer {

		private final URI uri;
		private final ApplicationHandler appHandler;
		private Server server;
		private static final Logger LOGGER = LoggerFactory.getLogger(JettyTestContainer.class);


		private JettyTestContainer(URI uri, ApplicationHandler appHandler) {
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

			LOGGER.info("Starting JettyTestContainer...");

			try {
				this.server = JettyHttpContainerFactory.createServer(uri, appHandler);
				org.eclipse.jetty.server.ServerConnector connector = (ServerConnector) server.getConnectors()[0];
				org.eclipse.jetty.server.HttpConnectionFactory factory = (HttpConnectionFactory) connector
						.getDefaultConnectionFactory();
				factory.getHttpConfiguration().setRequestHeaderSize(10000);
			} catch (ProcessingException e) {
				throw new TestContainerException(e);
			}
		}

		@Override
		public void stop() {

			LOGGER.info("Stopping JettyTestContainer...");

			try {
				this.server.stop();
			} catch (Exception ex) {
				LOGGER.info("Error Stopping JettyTestContainer...", ex);
			}
		}
	}


	@Override
	public TestContainer create(URI uri, ApplicationHandler appHandler) throws IllegalArgumentException {
		return new JettyTestContainer(uri, appHandler);
	}
}
