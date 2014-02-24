package com.infinities.keystone4j.jpa.impl;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.google.common.collect.Lists;
import com.infinities.keystone4j.catalog.model.Service;
import com.infinities.keystone4j.jpa.AbstractDao;

public class ServiceDao extends AbstractDao<Service> {

	public ServiceDao() {
		super(Service.class);
	}

	public Service findByName(String name) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Service> cq = cb.createQuery(getEntityType());
		Root<Service> root = cq.from(getEntityType());
		Path<String> path = root.get("name");
		List<Predicate> predicates = Lists.newArrayList();
		Predicate namePredicate = cb.equal(path, name);
		predicates.add(namePredicate);
		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);

		TypedQuery<Service> q = getEntityManager().createQuery(cq);
		Service domain = q.getSingleResult();
		return domain;
	}
}
