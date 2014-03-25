package com.infinities.keystone4j.jpa;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.annotation.WebListener;

import org.eclipse.jetty.util.component.LifeCycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebListener
public class EntityManagerListener implements LifeCycle.Listener {

	private static EntityManagerFactory emf;
	private final static Logger logger = LoggerFactory.getLogger(EntityManagerListener.class);


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
		// TODO Auto-generated method stub
		logger.debug("Application  was starting.");

	}

	@Override
	public void lifeCycleStarted(LifeCycle event) {
		logger.debug("Application  was initialized.");
		emf = Persistence.createEntityManagerFactory("com.infinities.keystone4j.jpa");
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
		if (emf != null) {
			emf.close();
		}
	}

	public static void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
		emf = entityManagerFactory;
	}

	public static EntityManagerFactory getEntityManagerFactory() {
		return emf;
	}
}
