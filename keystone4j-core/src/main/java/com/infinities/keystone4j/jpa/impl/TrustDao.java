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
package com.infinities.keystone4j.jpa.impl;

import java.util.Calendar;
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
import com.infinities.keystone4j.model.trust.Trust;

public class TrustDao extends AbstractDao<Trust> {

	public TrustDao() {
		super(Trust.class);
	}

	// @Override
	// public Trust findById(Object id) {
	// Trust trust = this.findById(id);
	// if (trust.getDeletedAt() == null) {
	// return null;
	// }
	// if (trust.getExpiresAt() != null) {
	// if (new Date().after(trust.getExpiresAt())) {
	// return null;
	// }
	// }
	// return trust;
	// }

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
		Predicate trusteePredicate = cb.equal(root.get("trusteeUserId"), trusteeid);
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
		Predicate trusteePredicate = cb.equal(root.get("trustorUserId"), trustorid);
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
		trust.setDeletedAt(Calendar.getInstance());
		this.merge(trust);
	}

	public Trust findByIdWithNonDeleted(String trustid) {
		EntityManager em = getEntityManager();
		// EntityTransaction tx = null;
		// try {
		// tx = em.getTransaction();
		// tx.begin();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Trust> cq = cb.createQuery(getEntityType());
		Root<Trust> root = cq.from(getEntityType());
		List<Predicate> predicates = Lists.newArrayList();
		Predicate deletedPredicate = cb.isNull(root.get("deletedAt"));
		predicates.add(deletedPredicate);
		Predicate idPredicate = cb.equal(root.get("id"), trustid);
		predicates.add(idPredicate);
		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);

		TypedQuery<Trust> q = em.createQuery(cq);
		return q.getSingleResult();
	}

	public List<Trust> findByIdWithRemainingUses(String trustid, Integer remainingUses) {
		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Trust> cq = cb.createQuery(getEntityType());
		Root<Trust> root = cq.from(getEntityType());
		List<Predicate> predicates = Lists.newArrayList();
		Predicate deletedPredicate = cb.isNull(root.get("deletedAt"));
		predicates.add(deletedPredicate);
		Predicate idPredicate = cb.equal(root.get("id"), trustid);
		predicates.add(idPredicate);
		Predicate remainingUsesPredicate = cb.equal(root.get("remainingUses"), remainingUses);
		predicates.add(remainingUsesPredicate);
		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);

		TypedQuery<Trust> q = em.createQuery(cq);
		return q.getResultList();
	}

	public Trust findById(String trustid, boolean deleted) {
		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Trust> cq = cb.createQuery(getEntityType());
		Root<Trust> root = cq.from(getEntityType());
		List<Predicate> predicates = Lists.newArrayList();
		if (!deleted) {
			Predicate deletedPredicate = cb.isNull(root.get("deletedAt"));
			predicates.add(deletedPredicate);
		}
		Predicate idPredicate = cb.equal(root.get("id"), trustid);
		predicates.add(idPredicate);
		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);

		TypedQuery<Trust> q = em.createQuery(cq);
		return q.getSingleResult();
	}

	public List<Trust> listUndeleted() {
		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Trust> cq = cb.createQuery(getEntityType());
		Root<Trust> root = cq.from(getEntityType());
		List<Predicate> predicates = Lists.newArrayList();
		Predicate deletedPredicate = cb.isNull(root.get("deletedAt"));
		predicates.add(deletedPredicate);
		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);

		TypedQuery<Trust> q = em.createQuery(cq);
		return q.getResultList();
	}
}
