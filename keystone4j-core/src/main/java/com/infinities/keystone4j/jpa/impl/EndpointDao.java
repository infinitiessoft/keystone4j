package com.infinities.keystone4j.jpa.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.jpa.AbstractDao;
import com.infinities.keystone4j.model.catalog.Endpoint;

public class EndpointDao extends AbstractDao<Endpoint> {

	public EndpointDao() {
		super(Endpoint.class);
	}

	public List<Endpoint> listEndpoint(Hints hints) throws SecurityException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		return filterLimitQuery(Endpoint.class, hints);
	}

	public List<Endpoint> listAllEnabled() {
		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Endpoint> cq = cb.createQuery(Endpoint.class);
		Root<Endpoint> root = cq.from(Endpoint.class);
		root.join("service");
		cq.select(root).where(cb.isTrue(root.<Boolean> get("enabled")));
		TypedQuery<Endpoint> q = em.createQuery(cq);
		List<Endpoint> endpoints = q.getResultList();
		return endpoints;
	}
}
