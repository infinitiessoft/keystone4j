package com.infinities.keystone4j.jpa.impl;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.google.common.collect.Lists;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.jpa.AbstractDao;

public class DomainDao extends AbstractDao<Domain> {

	public DomainDao() {
		super(Domain.class);
	}

	public Domain findByName(String name) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Domain> cq = cb.createQuery(getEntityType());
		Root<Domain> root = cq.from(getEntityType());
		Path<String> path = root.get("name");
		List<Predicate> predicates = Lists.newArrayList();
		Predicate namePredicate = cb.equal(path, name);
		predicates.add(namePredicate);
		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);

		TypedQuery<Domain> q = getEntityManager().createQuery(cq);
		Domain domain = q.getSingleResult();
		return domain;
	}
}
