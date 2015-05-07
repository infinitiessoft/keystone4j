/*******************************************************************************
 * # Copyright 2015 InfinitiesSoft Solutions Inc.
 * #
 * # Licensed under the Apache License, Version 2.0 (the "License"); you may
 * # not use this file except in compliance with the License. You may obtain
 * # a copy of the License at
 * #
 * #      http://www.apache.org/licenses/LICENSE-2.0
 * #
 * # Unless required by applicable law or agreed to in writing, software
 * # distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * # WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * # License for the specific language governing permissions and limitations
 * # under the License.
 *******************************************************************************/
package com.infinities.keystone4j.jpa.impl;

import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.google.common.collect.Lists;
import com.infinities.keystone4j.jpa.AbstractDao;
import com.infinities.keystone4j.model.contrib.revoke.RevocationEvent;

public class RevocationEventDao extends AbstractDao<RevocationEvent> {

	public RevocationEventDao() {
		super(RevocationEvent.class);
	}

	public List<RevocationEvent> findAll(Calendar lastFetch) {
		EntityManager em = getEntityManager();

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<RevocationEvent> cq = cb.createQuery(getEntityType());
		Root<RevocationEvent> root = cq.from(getEntityType());
		if (lastFetch != null) {
			List<Predicate> predicates = Lists.newArrayList();
			Path<Calendar> path = root.get("revokedAt");
			Predicate revokedAtPredicate = cb.greaterThan(path, lastFetch);
			predicates.add(revokedAtPredicate);
			cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		}
		cq.select(root);
		cq.orderBy(cb.asc(root.get("revokedAt")));
		TypedQuery<RevocationEvent> q = em.createQuery(cq);
		List<RevocationEvent> events = q.getResultList();
		return events;
		// return new ArrayList<RevocationEvent>();
	}

	public int removeByCutoffTime(Calendar oldest) {
		EntityManager em = getEntityManager();
		Query query = em.createQuery("DELETE FROM RevocationEvent e WHERE e.revokedAt < :oldest");
		query.setParameter("oldest", oldest);
		int deleted = query.executeUpdate();
		return deleted;
		// return 0;
	}
}
