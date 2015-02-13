package com.infinities.keystone4j.jpa.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.google.common.collect.Lists;
import com.infinities.keystone4j.jpa.AbstractDao;
import com.infinities.keystone4j.model.trust.TrustRole;

public class TrustRoleDao extends AbstractDao<TrustRole> {

	public TrustRoleDao() {
		super(TrustRole.class);
	}

	public List<TrustRole> findByTrustId(String trustid) {
		EntityManager em = getEntityManager();
		// EntityTransaction tx = null;
		// try {
		// tx = em.getTransaction();
		// tx.begin();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<TrustRole> cq = cb.createQuery(getEntityType());
		Root<TrustRole> root = cq.from(getEntityType());
		List<Predicate> predicates = Lists.newArrayList();
		Predicate trustidPredicate = cb.equal(root.get("trustId"), trustid);
		predicates.add(trustidPredicate);
		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);

		TypedQuery<TrustRole> q = em.createQuery(cq);
		List<TrustRole> refs = q.getResultList();
		// tx.commit();
		return refs;
	}
}
