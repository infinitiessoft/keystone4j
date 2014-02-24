package com.infinities.keystone4j.jpa.impl;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.google.common.collect.Lists;
import com.infinities.keystone4j.assignment.model.UserDomainGrant;
import com.infinities.keystone4j.jpa.AbstractDao;

public class UserDomainGrantDao extends AbstractDao<UserDomainGrant> {

	public UserDomainGrantDao() {
		super(UserDomainGrant.class);
	}

	public List<UserDomainGrant> listUserDomainGrant(String userid) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<UserDomainGrant> cq = cb.createQuery(UserDomainGrant.class);
		Root<UserDomainGrant> root = cq.from(UserDomainGrant.class);
		Predicate predicate = cb.equal(root.get("user").get("id"), userid);
		cq.where(predicate);
		cq.select(root);

		TypedQuery<UserDomainGrant> q = getEntityManager().createQuery(cq);
		List<UserDomainGrant> grants = q.getResultList();
		return grants;
	}

	public UserDomainGrant findByUseridAndDomainid(String userid, String domainid) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<UserDomainGrant> cq = cb.createQuery(UserDomainGrant.class);
		Root<UserDomainGrant> root = cq.from(UserDomainGrant.class);
		List<Predicate> predicates = Lists.newArrayList();
		Predicate groupPredicate = cb.equal(root.get("user").get("id"), userid);
		predicates.add(groupPredicate);
		Predicate projectPredicate = cb.equal(root.get("domain").get("id"), domainid);
		predicates.add(projectPredicate);
		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);

		TypedQuery<UserDomainGrant> q = getEntityManager().createQuery(cq);
		UserDomainGrant grant = q.getSingleResult();
		return grant;
	}

}
