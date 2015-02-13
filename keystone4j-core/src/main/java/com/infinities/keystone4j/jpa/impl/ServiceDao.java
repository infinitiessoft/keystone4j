package com.infinities.keystone4j.jpa.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.google.common.collect.Lists;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.jpa.AbstractDao;
import com.infinities.keystone4j.model.catalog.Service;

public class ServiceDao extends AbstractDao<Service> {

	public ServiceDao() {
		super(Service.class);
	}

	public Service findByName(String name) {

		EntityManager em = getEntityManager();
		// EntityTransaction tx = null;
		// try {
		// tx = em.getTransaction();
		// tx.begin();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Service> cq = cb.createQuery(getEntityType());
		Root<Service> root = cq.from(getEntityType());
		Path<String> path = root.get("name");
		List<Predicate> predicates = Lists.newArrayList();
		Predicate namePredicate = cb.equal(path, name);
		predicates.add(namePredicate);
		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);

		TypedQuery<Service> q = em.createQuery(cq);
		Service domain = q.getSingleResult();
		// tx.commit();
		return domain;

		// } catch (RuntimeException e) {
		// if (tx != null && tx.isActive()) {
		// tx.rollback();
		// }
		// throw e;
		// } finally {
		// em.close();
		// }

	}

	public List<Service> listService(Hints hints) throws SecurityException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		return filterLimitQuery(Service.class, hints);
	}

	public List<Service> listAllEnabled() {
		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Service> cq = cb.createQuery(getEntityType());
		Root<Service> root = cq.from(getEntityType());
		root.join("endpoints");
		cq.where(cb.isTrue(root.<Boolean> get("enabled")));
		cq.select(root);
		TypedQuery<Service> q = em.createQuery(cq);
		List<Service> result = q.getResultList();
		return result;
	}
}
