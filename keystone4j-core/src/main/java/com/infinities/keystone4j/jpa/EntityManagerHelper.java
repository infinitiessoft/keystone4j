package com.infinities.keystone4j.jpa;

import javax.persistence.EntityManager;

public class EntityManagerHelper {

	private static final ThreadLocal<EntityManager> threadLocal;

	static {
		threadLocal = new ThreadLocal<EntityManager>();
	}


	public static EntityManager getEntityManager() {
		EntityManager em = threadLocal.get();

		if (em == null) {
			em = EntityManagerListener.getEntityManagerFactory().createEntityManager();
			threadLocal.set(em);
		}
		return em;
	}

	public static void closeEntityManager() {
		EntityManager em = threadLocal.get();
		if (em != null) {
			em.close();
			threadLocal.set(null);
		}
	}

	// public static void closeEntityManagerFactory() {
	// emf.close();
	// }

	public static void beginTransaction() {
		getEntityManager().getTransaction().begin();
	}

	public static void rollback() {
		getEntityManager().getTransaction().rollback();
	}

	public static void commit() {
		getEntityManager().getTransaction().commit();
	}
}
