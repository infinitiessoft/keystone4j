package com.infinities.keystone4j;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;

public class Main {

	public static void main(String args[]) {
		URI baseUri = UriBuilder.fromUri("http://localhost/").port(9999).build();
		KeystoneApplication application = new KeystoneApplication();
		Server server = JettyHttpContainerFactory.createServer(baseUri, application);
		try {
			server.start();
			server.join();
		} catch (Exception e) {
			try {
				server.stop();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
