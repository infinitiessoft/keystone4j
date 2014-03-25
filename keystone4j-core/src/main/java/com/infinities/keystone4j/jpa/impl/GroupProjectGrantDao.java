package com.infinities.keystone4j.jpa.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.google.common.collect.Lists;
import com.infinities.keystone4j.assignment.model.GroupProjectGrant;
import com.infinities.keystone4j.jpa.AbstractDao;

public class GroupProjectGrantDao extends AbstractDao<GroupProjectGrant> {

	public GroupProjectGrantDao() {
		super(GroupProjectGrant.class);
	}

	public List<GroupProjectGrant> listGroupProjectGrant(String groupid) {

		EntityManager em = getEntityManager();
		// EntityTransaction tx = null;
		// try {
		// tx = em.getTransaction();
		// tx.begin();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<GroupProjectGrant> cq = cb.createQuery(GroupProjectGrant.class);
		Root<GroupProjectGrant> root = cq.from(GroupProjectGrant.class);
		Predicate predicate = cb.equal(root.get("group").get("id"), groupid);
		cq.where(predicate);
		cq.select(root);

		TypedQuery<GroupProjectGrant> q = em.createQuery(cq);
		List<GroupProjectGrant> grants = q.getResultList();

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

	public GroupProjectGrant findByGroupidAndProjectid(String groupid, String projectid) {

		EntityManager em = getEntityManager();
		// EntityTransaction tx = null;
		// try {
		// tx = em.getTransaction();
		// tx.begin();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<GroupProjectGrant> cq = cb.createQuery(GroupProjectGrant.class);
		Root<GroupProjectGrant> root = cq.from(GroupProjectGrant.class);
		List<Predicate> predicates = Lists.newArrayList();
		Predicate groupPredicate = cb.equal(root.get("group").get("id"), groupid);
		predicates.add(groupPredicate);
		Predicate projectPredicate = cb.equal(root.get("project").get("id"), projectid);
		predicates.add(projectPredicate);
		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);

		TypedQuery<GroupProjectGrant> q = em.createQuery(cq);
		GroupProjectGrant grant = q.getSingleResult();
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
