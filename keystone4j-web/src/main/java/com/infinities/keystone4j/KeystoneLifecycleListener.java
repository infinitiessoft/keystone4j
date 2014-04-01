package com.infinities.keystone4j;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.infinities.keystone4j.jpa.EntityManagerListener;

public class KeystoneLifecycleListener implements ServletContextListener {

	// @Override
	public void contextInitialized(ServletContextEvent sce) {
		EntityManagerListener.setupEntityManagerFactory();
	}

	// @Override
	public void contextDestroyed(ServletContextEvent sce) {
		EntityManagerListener.shutdownEntityManagerFactory();
	}

}
