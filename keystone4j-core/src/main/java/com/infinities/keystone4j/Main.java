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

import java.io.File;
import java.net.URI;
import java.net.URL;

import javax.ws.rs.core.UriBuilder;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.glassfish.jersey.jetty.JettyHttpContainer;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ContainerFactory;

import com.infinities.keystone4j.jpa.EntityManagerListener;
import com.infinities.keystone4j.utils.KeystoneUtils;

public class Main {

	public static void main(String args[]) {
		URI baseUri = UriBuilder.fromUri("https://localhost/").port(5000).build();
		KeystoneApplication application = new KeystoneApplication();

		JettyHttpContainer container = ContainerFactory.createContainer(JettyHttpContainer.class, application);

		SslContextFactory sslContextFactory = new SslContextFactory();
		URL url = KeystoneUtils.getURL(KeystoneApplication.CONF_DIR + "ssl" + File.separator + "keystone4j.p12");

		sslContextFactory.setKeyStorePath(url.toExternalForm());
		sslContextFactory.setKeyStorePassword("changeit");
		sslContextFactory.setKeyManagerPassword("changeit");
		sslContextFactory.setKeyStoreType("PKCS12");
		sslContextFactory.setProtocol("TLS");
		Server server = JettyHttpContainerFactory.createServer(baseUri, sslContextFactory, container, false);

		server.addLifeCycleListener(new EntityManagerListener());

		URI accountBaseUri = UriBuilder.fromUri("https://localhost/").port(5001).build();
		AccountApplication accountApplication = new AccountApplication();
		JettyHttpContainer accountContainer = ContainerFactory.createContainer(JettyHttpContainer.class, accountApplication);
		Server accountServer = JettyHttpContainerFactory.createServer(accountBaseUri, sslContextFactory, accountContainer,
				false);

		URI adminBaseUri = UriBuilder.fromUri("https://localhost/").port(5002).build();
		AdminApplication adminApplication = new AdminApplication();
		JettyHttpContainer adminContainer = ContainerFactory.createContainer(JettyHttpContainer.class, adminApplication);
		Server adminServer = JettyHttpContainerFactory.createServer(adminBaseUri, sslContextFactory, adminContainer, false);

		try {
			server.start();
			accountServer.start();
			adminServer.start();
			server.join();
		} catch (Exception e) {
			try {
				server.stop();
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			try {
				accountServer.stop();
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			try {
				adminServer.stop();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		// URI adminBaseUri =
		// UriBuilder.fromUri("http://localhost/").port(5001).build();
		// AdminApplication adminApplication = new AdminApplication();
		// Server adminServer =
		// JettyHttpContainerFactory.createServer(adminBaseUri,
		// adminApplication, false);
		// //
		// adminServer.getAttributeNames().setAttribute("org.eclipse.jetty.server.Request.maxFormContentSize",
		// // 800000000);
		// adminServer.addLifeCycleListener(new EntityManagerListener());
		// org.eclipse.jetty.server.ServerConnector adminConnector =
		// (ServerConnector) adminServer.getConnectors()[0];
		// org.eclipse.jetty.server.HttpConnectionFactory adminFactory =
		// (HttpConnectionFactory) adminConnector
		// .getDefaultConnectionFactory();
		// adminFactory.getHttpConfiguration().setRequestHeaderSize(10000);
		// try {
		// adminServer.start();
		// adminServer.join();
		// } catch (Exception e) {
		// try {
		// adminServer.stop();
		// } catch (Exception e1) {
		// e1.printStackTrace();
		// }
		// }
		//
		// URI accountBaseUri =
		// UriBuilder.fromUri("http://localhost/").port(5002).build();
		// AccountApplication accountApplication = new AccountApplication();
		// Server accountServer =
		// JettyHttpContainerFactory.createServer(accountBaseUri,
		// accountApplication, false);
		// //
		// server.getAttributeNames().setAttribute("org.eclipse.jetty.server.Request.maxFormContentSize",
		// // 800000000);
		// // server.addLifeCycleListener(new EntityManagerListener());
		// org.eclipse.jetty.server.ServerConnector accountConnector =
		// (ServerConnector) accountServer.getConnectors()[0];
		// org.eclipse.jetty.server.HttpConnectionFactory accountFactory =
		// (HttpConnectionFactory) accountConnector
		// .getDefaultConnectionFactory();
		// accountFactory.getHttpConfiguration().setRequestHeaderSize(10000);
		// try {
		// accountServer.start();
		// accountServer.join();
		// } catch (Exception e) {
		// try {
		// accountServer.stop();
		// } catch (Exception e1) {
		// e1.printStackTrace();
		// }
		// }

	}
}
