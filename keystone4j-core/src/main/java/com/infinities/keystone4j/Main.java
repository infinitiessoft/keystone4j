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
