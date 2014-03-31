package com.infinities.keystone4j.jpa.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.google.common.collect.Lists;
import com.infinities.keystone4j.assignment.model.UserProjectGrant;
import com.infinities.keystone4j.jpa.AbstractDao;

public class UserProjectGrantDao extends AbstractDao<UserProjectGrant> {

	public UserProjectGrantDao() {
		super(UserProjectGrant.class);
	}

	public List<UserProjectGrant> listUserProjectGrant(String userid) {
		EntityManager em = getEntityManager();
		// EntityTransaction tx = null;
		// try {
		// tx = em.getTransaction();
		// tx.begin();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<UserProjectGrant> cq = cb.createQuery(UserProjectGrant.class);
		Root<UserProjectGrant> root = cq.from(UserProjectGrant.class);
		Predicate predicate = cb.equal(root.get("user").get("id"), userid);
		cq.where(predicate);
		cq.select(root);

		TypedQuery<UserProjectGrant> q = em.createQuery(cq);
		List<UserProjectGrant> grants = q.getResultList();
		// tx.commit();
		return grants;
		// } catch (RuntimeException e) {
		// if (tx != null && tx.isActive()) {
		// tx.rollback();
		// }
		// throw e;
		// } finally {
		// em.close();
		// }

	}

	public List<UserProjectGrant> findByUseridAndProjectid(String userid, String projectid) {
		EntityManager em = getEntityManager();
		// EntityTransaction tx = null;
		// try {
		// tx = em.getTransaction();
		// tx.begin();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<UserProjectGrant> cq = cb.createQuery(UserProjectGrant.class);
		Root<UserProjectGrant> root = cq.from(UserProjectGrant.class);
		List<Predicate> predicates = Lists.newArrayList();
		Predicate groupPredicate = cb.equal(root.get("user").get("id"), userid);
		predicates.add(groupPredicate);
		Predicate projectPredicate = cb.equal(root.get("project").get("id"), projectid);
		predicates.add(projectPredicate);
		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);
		TypedQuery<UserProjectGrant> q = em.createQuery(cq);
		List<UserProjectGrant> grants = q.getResultList();
		// tx.commit();
		return grants;
		// } catch (RuntimeException e) {
		// if (tx != null && tx.isActive()) {
		// tx.rollback();
		// }
		// throw e;
		// } finally {
		// em.close();
		// }

	}

	public UserProjectGrant getGrant(String userid, String projectid, String roleid) {
		EntityManager em = getEntityManager();
		// EntityTransaction tx = null;
		// try {
		// tx = em.getTransaction();
		// tx.begin();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<UserProjectGrant> cq = cb.createQuery(UserProjectGrant.class);
		Root<UserProjectGrant> root = cq.from(UserProjectGrant.class);
		List<Predicate> predicates = Lists.newArrayList();
		Predicate groupPredicate = cb.equal(root.get("user").get("id"), userid);
		predicates.add(groupPredicate);
		Predicate projectPredicate = cb.equal(root.get("project").get("id"), projectid);
		predicates.add(projectPredicate);
		Predicate rolePredicate = cb.equal(root.get("role").get("id"), roleid);
		predicates.add(rolePredicate);
		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);
		TypedQuery<UserProjectGrant> q = em.createQuery(cq);
		UserProjectGrant grant = q.getSingleResult();
		// tx.commit();
		return grant;
		// } catch (RuntimeException e) {
		// if (tx != null && tx.isActive()) {
		// tx.rollback();
		// }
		// throw e;
		// } finally {
		// em.close();
		// }

	}

}
