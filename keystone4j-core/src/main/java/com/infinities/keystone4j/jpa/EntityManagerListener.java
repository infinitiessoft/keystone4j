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
package com.infinities.keystone4j.jpa;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.annotation.WebListener;

import org.eclipse.jetty.util.component.LifeCycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.infinities.keystone4j.KeystoneApplication;

@WebListener
public class EntityManagerListener implements LifeCycle.Listener {

	private static EntityManagerFactory emf;
	private final static Logger logger = LoggerFactory.getLogger(EntityManagerListener.class);
	private static final String PERSISTENCE_UNIT_NAME = JpaProperties.PERSISTENCE_UNIT_NAME;
	private static final String JPA_PROPERTIES_FILE = JpaProperties.JPA_PROPERTIES_FILE;


	// @Override
	// public void contextInitialized(ServletContextEvent sce) {
	// System.out.println("......................contextInitialized");
	// // emf =
	// //
	// Persistence.createEntityManagerFactory("com.infinities.keystone4j.jpa");
	// }
	//
	// //
	// @Override
	// public void contextDestroyed(ServletContextEvent sce) {
	// System.out.println("......................contextDestroyed");
	// // emf.close();
	// }

	// public static EntityManager createEntityManager() {
	// if (emf == null) {
	// throw new IllegalStateException("Context is not initialized yet.");
	// }
	// return emf.createEntityManager();
	// // return null;
	// }

	// @Override
	// public void onEvent(RequestEvent event) {
	// switch (event.getType()) {
	// case START:
	// System.out.println("Application  was initialized.");
	// break;
	// case FINISHED:
	// System.out.println("Application was destroy.");
	// break;
	// default:
	// System.out.println("Application was " + event.getType().name());
	// break;
	// }
	// }

	@Override
	public void lifeCycleStarting(LifeCycle event) {
		logger.debug("Application  was starting.");

	}

	@Override
	public void lifeCycleStarted(LifeCycle event) {
		logger.debug("Application  was initialized.");
		setupEntityManagerFactory();
	}

	@Override
	public void lifeCycleFailure(LifeCycle event, Throwable cause) {
		logger.debug("Application  was failure.");
	}

	@Override
	public void lifeCycleStopping(LifeCycle event) {
		logger.debug("Application was destroy.");
	}

	@Override
	public void lifeCycleStopped(LifeCycle event) {
		logger.debug("Application  was stopped.");
		// EntityManagerHelper.closeEntityManagerFactory();
		shutdownEntityManagerFactory();
	}

	public static void setupEntityManagerFactory() {
		String configFileLocation = KeystoneApplication.CONF_DIR + JPA_PROPERTIES_FILE;
		Properties prop = new Properties();
		if (!Strings.isNullOrEmpty(JPA_PROPERTIES_FILE)) {
			try {
				logger.debug("Load ConfigFile: {}", configFileLocation);
				prop.load(new FileInputStream(configFileLocation));
			} catch (FileNotFoundException e) {
				logger.warn("File " + configFileLocation + " not found", e);
			} catch (IOException e) {
				logger.warn("Loading File " + configFileLocation + " fail", e);
			}
		}

		if (prop.isEmpty()) {
			logger.debug("Properites is empty, Create EntityManagerFactory {}", PERSISTENCE_UNIT_NAME);
			emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		} else {
			for (Object key : prop.keySet()) {
				logger.debug("jpa property key: {}, value: {}", new Object[] { key, prop.get(key) });
			}
			logger.debug("Properites is found, Create EntityManagerFactory {}", PERSISTENCE_UNIT_NAME);
			emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, prop);
		}
	}

	public static void shutdownEntityManagerFactory() {
		if (emf != null) {
			emf.close();
		}
	}

	public static void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
		emf = entityManagerFactory;
	}

	public static EntityManagerFactory getEntityManagerFactory() {
		if (emf == null) {
			setupEntityManagerFactory();
		}
		return emf;
	}
}
