package com.infinities.keystone4j;

import javax.persistence.EntityManager;

public abstract class SqlFunction<T> implements Command<T> {

	private EntityManager entityManager;

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

}
