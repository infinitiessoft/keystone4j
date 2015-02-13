package com.infinities.keystone4j.jpa.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.google.common.collect.Lists;
import com.infinities.keystone4j.jpa.AbstractDao;
import com.infinities.keystone4j.model.identity.mapping.EntityType;
import com.infinities.keystone4j.model.identity.mapping.IdMapping;

public class IdMappingDao extends AbstractDao<IdMapping> {

	public IdMappingDao() {
		super(IdMapping.class);
	}

	public IdMapping find(String domainId, String localId, EntityType entityType) {
		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<IdMapping> cq = cb.createQuery(getEntityType());
		Root<IdMapping> root = cq.from(getEntityType());
		Path<String> path = root.get("domainId");
		List<Predicate> predicates = Lists.newArrayList();
		Predicate domainIdPredicate = cb.equal(path, domainId);
		predicates.add(domainIdPredicate);
		Predicate localIdPredicate = cb.equal(root.get("localId"), localId);
		predicates.add(localIdPredicate);
		Predicate typePredicate = cb.equal(root.get("entityType"), entityType);
		predicates.add(typePredicate);
		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);
		TypedQuery<IdMapping> q = em.createQuery(cq);
		IdMapping idMapping = q.getSingleResult();
		return idMapping;
	}

}
