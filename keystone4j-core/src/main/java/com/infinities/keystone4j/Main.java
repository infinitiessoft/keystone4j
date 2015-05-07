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

import javax.ws.rs.core.UriBuilder;

import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;

import com.infinities.keystone4j.jpa.EntityManagerListener;

public class Main {

	public static void main(String args[]) {
		URI baseUri = UriBuilder.fromUri("http://localhost/").port(9999).build();
		KeystoneApplication application = new KeystoneApplication();
		Server server = JettyHttpContainerFactory.createServer(baseUri, application, false);
		server.addLifeCycleListener(new EntityManagerListener());

		try {
			server.start();
			server.join();
		} catch (Exception e) {
			try {
				server.stop();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
}
