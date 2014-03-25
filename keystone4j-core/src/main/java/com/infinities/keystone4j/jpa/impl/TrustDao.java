package com.infinities.keystone4j.jpa.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.google.common.collect.Lists;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.jpa.AbstractDao;
import com.infinities.keystone4j.trust.model.Trust;

public class TrustDao extends AbstractDao<Trust> {

	public TrustDao() {
		super(Trust.class);
	}

	@Override
	public Trust findById(Object id) {
		Trust trust = this.findById(id);
		if (trust.getDeletedAt() == null) {
			return null;
		}
		if (trust.getExpiresAt() != null) {
			if (new Date().after(trust.getExpiresAt())) {
				return null;
			}
		}
		return trust;
	}

	@Override
	public List<Trust> findAll() {
		EntityManager em = getEntityManager();
		// EntityTransaction tx = null;
		// try {
		// tx = em.getTransaction();
		// tx.begin();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Trust> cq = cb.createQuery(getEntityType());
		Root<Trust> root = cq.from(getEntityType());
		List<Predicate> predicates = Lists.newArrayList();
		Predicate domainidPredicate = cb.isNull(root.get("deletedAt"));
		predicates.add(domainidPredicate);
		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);

		TypedQuery<Trust> q = em.createQuery(cq);
		List<Trust> trusts = q.getResultList();
		// tx.commit();
		return trusts;
		// } catch (RuntimeException e) {
		// if (tx != null && tx.isActive()) {
		// tx.rollback();
		// }
		// throw e;
		// } finally {
		// em.close();
		// }

	}

	public List<Trust> listTrustsForTrustee(Object trusteeid) {

		EntityManager em = getEntityManager();
		// EntityTransaction tx = null;
		// try {
		// tx = em.getTransaction();
		// tx.begin();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Trust> cq = cb.createQuery(getEntityType());
		Root<Trust> root = cq.from(getEntityType());
		List<Predicate> predicates = Lists.newArrayList();
		Predicate domainidPredicate = cb.isNull(root.get("deletedAt"));
		predicates.add(domainidPredicate);
		Predicate trusteePredicate = cb.equal(root.get("trustee").get("id"), trusteeid);
		predicates.add(trusteePredicate);
		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);

		TypedQuery<Trust> q = em.createQuery(cq);
		List<Trust> trusts = q.getResultList();
		// tx.commit();
		return trusts;

		// } catch (RuntimeException e) {
		// if (tx != null && tx.isActive()) {
		// tx.rollback();
		// }
		// throw e;
		// } finally {
		// em.close();
		// }

	}

	public List<Trust> listTrustsForTrustor(String trustorid) {

		EntityManager em = getEntityManager();
		// EntityTransaction tx = null;
		// try {
		// tx = em.getTransaction();
		// tx.begin();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Trust> cq = cb.createQuery(getEntityType());
		Root<Trust> root = cq.from(getEntityType());
		List<Predicate> predicates = Lists.newArrayList();
		Predicate domainidPredicate = cb.isNull(root.get("deletedAt"));
		predicates.add(domainidPredicate);
		Predicate trusteePredicate = cb.equal(root.get("trustor").get("id"), trustorid);
		predicates.add(trusteePredicate);
		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);

		TypedQuery<Trust> q = em.createQuery(cq);
		List<Trust> trusts = q.getResultList();
		// tx.commit();
		return trusts;

		// } catch (RuntimeException e) {
		// if (tx != null && tx.isActive()) {
		// tx.rollback();
		// }
		// throw e;
		// } finally {
		// em.close();
		// }

	}

	public void remove(String trustid) {
		Trust trust = this.findById(trustid);
		if (trust == null) {
			throw Exceptions.TrustNotFoundException.getInstance(null, trustid);
		}
		trust.setDeletedAt(new Date());
		this.merge(trust);
	}
}
