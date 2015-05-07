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

import javax.persistence.EntityManager;

public class EntityManagerHelper {

	private static final ThreadLocal<EntityManager> threadLocal;

	private static ThreadLocal<Boolean> lock;

	static {
		threadLocal = new ThreadLocal<EntityManager>();
		lock = new ThreadLocal<Boolean>();
		lock.set(true);
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
		setLock(true);
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
		setLock(false);
	}

	public static void rollback() {
		getEntityManager().getTransaction().rollback();
	}

	public static void commit() {
		getEntityManager().getTransaction().commit();
	}

	public static void setLock(boolean value) {
		lock.set(value);
	}

	public static boolean getLock() {
		try {
			return lock.get();
		} catch (Exception e) {
			return true;
		}
	}
}
