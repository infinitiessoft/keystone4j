package com.infinities.keystone4j.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractDao<T> implements GenericDao<T> {

	private final static Logger logger = LoggerFactory.getLogger(AbstractDao.class);
	// private EntityManager entityManager;
	private final Class<T> entityType;


	public AbstractDao(Class<T> entityType) {
		this.entityType = entityType;
	}

	// public void setEntityManager(EntityManager entityManager) {
	// this.entityManager = entityManager;
	// }

	// protected EntityManager getEntityManager() {
	// return entityManager;
	// }

	protected EntityManager getEntityManager() {
		return EntityManagerListener.createEntityManager();
	}

	@Override
	public void persist(T transientInstance) {
		getEntityManager().persist(transientInstance);
		logger.debug("persisting {} instance", getEntityType().getSimpleName());
	}

	@Override
	public void remove(T persistentInstance) {
		getEntityManager().remove(persistentInstance);
		logger.debug("removing {} instance", getEntityType().getSimpleName());
	}

	@Override
	public T merge(T detachedInstance) {
		T result = getEntityManager().merge(detachedInstance);
		logger.debug("merging {} instance", getEntityType().getSimpleName());
		return result;
	}

	@Override
	public T findById(Object id) {
		T instance = getEntityManager().find(getEntityType(), id);
		logger.debug("getting {} instance with id: {}", new Object[] { getEntityType().getSimpleName(), id });
		return instance;
	}

	@Override
	public List<T> findAll() {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(getEntityType());
		Root<T> taskEventRoot = cq.from(getEntityType());
		cq.select(taskEventRoot);

		TypedQuery<T> q = getEntityManager().createQuery(cq);
		List<T> list = q.getResultList();
		return list;
	}

	protected Class<T> getEntityType() {
		return entityType;
	}
}
