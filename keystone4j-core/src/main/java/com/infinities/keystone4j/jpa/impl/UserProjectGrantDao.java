package com.infinities.keystone4j.jpa.impl;

import java.util.List;

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
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<UserProjectGrant> cq = cb.createQuery(UserProjectGrant.class);
		Root<UserProjectGrant> root = cq.from(UserProjectGrant.class);
		Predicate predicate = cb.equal(root.get("user").get("id"), userid);
		cq.where(predicate);
		cq.select(root);

		TypedQuery<UserProjectGrant> q = getEntityManager().createQuery(cq);
		List<UserProjectGrant> grants = q.getResultList();
		return grants;
	}

	public UserProjectGrant findByUseridAndProjectid(String userid, String projectid) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<UserProjectGrant> cq = cb.createQuery(UserProjectGrant.class);
		Root<UserProjectGrant> root = cq.from(UserProjectGrant.class);
		List<Predicate> predicates = Lists.newArrayList();
		Predicate groupPredicate = cb.equal(root.get("user").get("id"), userid);
		predicates.add(groupPredicate);
		Predicate projectPredicate = cb.equal(root.get("project").get("id"), projectid);
		predicates.add(projectPredicate);
		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);

		TypedQuery<UserProjectGrant> q = getEntityManager().createQuery(cq);
		UserProjectGrant grant = q.getSingleResult();
		return grant;
	}

}
