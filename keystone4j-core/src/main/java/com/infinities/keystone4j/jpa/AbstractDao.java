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
		return EntityManagerHelper.getEntityManager();
	}

	@Override
	public void persist(T transientInstance) {
		EntityManager em = getEntityManager();
		// EntityTransaction tx = null;
		// try {
		// tx = em.getTransaction();
		// tx.begin();
		em.persist(transientInstance);
		// tx.commit();
		logger.debug("persisting {} instance", getEntityType().getSimpleName());
		// } catch (RuntimeException e) {
		// if (tx != null && tx.isActive()) {
		// tx.rollback();
		// }
		// throw e;
		// } finally {
		// em.close();
		// }
	}

	@Override
	public void remove(T persistentInstance) {
		EntityManager em = getEntityManager();
		// EntityTransaction tx = null;
		// try {
		// tx = em.getTransaction();
		// tx.begin();
		em.remove(em.merge(persistentInstance));
		// tx.commit();
		// logger.debug("removing {} instance",
		// getEntityType().getSimpleName());
		// } catch (RuntimeException e) {
		// if (tx != null && tx.isActive()) {
		// tx.rollback();
		// }
		// throw e;
		// } finally {
		// em.close();
		// }
	}

	@Override
	public T merge(T detachedInstance) {
		EntityManager em = getEntityManager();
		// EntityTransaction tx = null;
		// try {
		// tx = em.getTransaction();
		// tx.begin();
		T result = em.merge(detachedInstance);
		// tx.commit();
		logger.debug("merging {} instance", getEntityType().getSimpleName());
		return result;
		// } catch (RuntimeException e) {
		// if (tx != null && tx.isActive()) {
		// tx.rollback();
		// }
		// throw e;
		// } finally {
		// em.close();
		// }

	}

	@Override
	public T findById(Object id) {
		EntityManager em = getEntityManager();
		// EntityTransaction tx = null;
		// try {
		// tx = em.getTransaction();
		// tx.begin();
		T result = em.find(getEntityType(), id);
		// tx.commit();
		logger.debug("getting {} instance with id: {}", new Object[] { getEntityType().getSimpleName(), id });
		return result;
		// } catch (RuntimeException e) {
		// if (tx != null && tx.isActive()) {
		// tx.rollback();
		// }
		// throw e;
		// } finally {
		// em.close();
		// }
	}

	@Override
	public List<T> findAll() {

		EntityManager em = getEntityManager();
		// EntityTransaction tx = null;
		// try {
		// tx = em.getTransaction();
		// tx.begin();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(getEntityType());
		Root<T> taskEventRoot = cq.from(getEntityType());
		cq.select(taskEventRoot);
		TypedQuery<T> q = em.createQuery(cq);
		List<T> result = q.getResultList();
		// tx.commit();
		return result;
		// } catch (RuntimeException e) {
		// if (tx != null && tx.isActive()) {
		// tx.rollback();
		// }
		// throw e;
		// } finally {
		// em.close();
		// }
	}

	protected Class<T> getEntityType() {
		return entityType;
	}
}
