package com.infinities.keystone4j.jpa.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.google.common.collect.Lists;
import com.infinities.keystone4j.identity.model.UserGroupMembership;
import com.infinities.keystone4j.jpa.AbstractDao;

public class UserGroupMembershipDao extends AbstractDao<UserGroupMembership> {

	public UserGroupMembershipDao() {
		super(UserGroupMembership.class);
	}

	public List<UserGroupMembership> listByGroup(String groupid) {
		EntityManager em = getEntityManager();
		// EntityTransaction tx = null;
		// try {
		// tx = em.getTransaction();
		// tx.begin();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<UserGroupMembership> cq = cb.createQuery(UserGroupMembership.class);
		Root<UserGroupMembership> root = cq.from(UserGroupMembership.class);
		Predicate predicate = cb.equal(root.get("group").get("id"), groupid);
		cq.where(predicate);
		cq.select(root);

		TypedQuery<UserGroupMembership> q = em.createQuery(cq);
		List<UserGroupMembership> ret = q.getResultList();

		// tx.commit();
		return ret;
		// } catch (RuntimeException e) {
		// if (tx != null && tx.isActive()) {
		// tx.rollback();
		// }
		// throw e;
		// } finally {
		// em.close();
		// }
	}

	public List<UserGroupMembership> listByUser(String userid) {
		EntityManager em = getEntityManager();
		// EntityTransaction tx = null;
		// try {
		// tx = em.getTransaction();
		// tx.begin();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<UserGroupMembership> cq = cb.createQuery(UserGroupMembership.class);
		Root<UserGroupMembership> root = cq.from(UserGroupMembership.class);
		Predicate predicate = cb.equal(root.get("user").get("id"), userid);
		cq.where(predicate);
		cq.select(root);

		TypedQuery<UserGroupMembership> q = em.createQuery(cq);
		List<UserGroupMembership> ret = q.getResultList();
		// tx.commit();
		return ret;
		// } catch (RuntimeException e) {
		// if (tx != null && tx.isActive()) {
		// tx.rollback();
		// }
		// throw e;
		// } finally {
		// em.close();
		// }

	}

	public UserGroupMembership findByUserGroup(String userid, String groupid) {

		EntityManager em = getEntityManager();
		// EntityTransaction tx = null;
		// try {
		// tx = em.getTransaction();
		// tx.begin();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<UserGroupMembership> cq = cb.createQuery(UserGroupMembership.class);
		Root<UserGroupMembership> root = cq.from(UserGroupMembership.class);
		List<Predicate> predicates = Lists.newArrayList();
		Predicate userPredicate = cb.equal(root.get("user").get("id"), userid);
		predicates.add(userPredicate);
		Predicate groupPredicate = cb.equal(root.get("group").get("id"), groupid);
		predicates.add(groupPredicate);
		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);
		TypedQuery<UserGroupMembership> q = em.createQuery(cq);
		UserGroupMembership ret = q.getSingleResult();

		// tx.commit();
		return ret;
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
