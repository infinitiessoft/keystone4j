package com.infinities.keystone4j.jpa.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.google.common.collect.Lists;
import com.infinities.keystone4j.assignment.model.GroupDomainGrant;
import com.infinities.keystone4j.jpa.AbstractDao;

public class GroupDomainGrantDao extends AbstractDao<GroupDomainGrant> {

	public GroupDomainGrantDao() {
		super(GroupDomainGrant.class);
	}

	public List<GroupDomainGrant> listGroupDomainGrant(String groupid) {

		EntityManager em = getEntityManager();
		// EntityTransaction tx = null;
		// try {
		// tx = em.getTransaction();
		// tx.begin();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<GroupDomainGrant> cq = cb.createQuery(GroupDomainGrant.class);
		Root<GroupDomainGrant> root = cq.from(GroupDomainGrant.class);
		Predicate predicate = cb.equal(root.get("group").get("id"), groupid);
		cq.where(predicate);
		cq.select(root);

		TypedQuery<GroupDomainGrant> q = em.createQuery(cq);
		List<GroupDomainGrant> grants = q.getResultList();
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

	public GroupDomainGrant findByGroupidAndDomainid(String groupid, String domainid) {

		EntityManager em = getEntityManager();
		// EntityTransaction tx = null;
		// try {
		// tx = em.getTransaction();
		// tx.begin();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<GroupDomainGrant> cq = cb.createQuery(GroupDomainGrant.class);
		Root<GroupDomainGrant> root = cq.from(GroupDomainGrant.class);
		List<Predicate> predicates = Lists.newArrayList();
		Predicate groupPredicate = cb.equal(root.get("group").get("id"), groupid);
		predicates.add(groupPredicate);
		Predicate projectPredicate = cb.equal(root.get("domain").get("id"), domainid);
		predicates.add(projectPredicate);
		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);

		TypedQuery<GroupDomainGrant> q = em.createQuery(cq);
		GroupDomainGrant grant = q.getSingleResult();
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
