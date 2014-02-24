package com.infinities.keystone4j.jpa.impl;

import java.util.List;

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
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<GroupDomainGrant> cq = cb.createQuery(GroupDomainGrant.class);
		Root<GroupDomainGrant> root = cq.from(GroupDomainGrant.class);
		Predicate predicate = cb.equal(root.get("group").get("id"), groupid);
		cq.where(predicate);
		cq.select(root);

		TypedQuery<GroupDomainGrant> q = getEntityManager().createQuery(cq);
		List<GroupDomainGrant> grants = q.getResultList();
		return grants;
	}

	public GroupDomainGrant findByGroupidAndDomainid(String groupid, String domainid) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<GroupDomainGrant> cq = cb.createQuery(GroupDomainGrant.class);
		Root<GroupDomainGrant> root = cq.from(GroupDomainGrant.class);
		List<Predicate> predicates = Lists.newArrayList();
		Predicate groupPredicate = cb.equal(root.get("group").get("id"), groupid);
		predicates.add(groupPredicate);
		Predicate projectPredicate = cb.equal(root.get("domain").get("id"), domainid);
		predicates.add(projectPredicate);
		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);

		TypedQuery<GroupDomainGrant> q = getEntityManager().createQuery(cq);
		GroupDomainGrant grant = q.getSingleResult();
		return grant;
	}

}
