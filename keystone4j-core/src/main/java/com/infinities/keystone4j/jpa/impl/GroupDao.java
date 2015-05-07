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

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.jpa.AbstractDao;
import com.infinities.keystone4j.model.identity.Group;

public class GroupDao extends AbstractDao<Group> {

	public GroupDao() {
		super(Group.class);
	}

	public Group findByName(String name, String domainid) {
		EntityManager em = getEntityManager();
		// EntityTransaction tx = null;
		// try {
		// tx = em.getTransaction();
		// tx.begin();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Group> cq = cb.createQuery(getEntityType());
		Root<Group> root = cq.from(getEntityType());
		Path<String> path = root.get("name");
		List<Predicate> predicates = Lists.newArrayList();
		Predicate namePredicate = cb.equal(path, name);
		predicates.add(namePredicate);
		if (!Strings.isNullOrEmpty(domainid)) {
			Predicate domainidPredicate = cb.equal(root.get("domain").get("id"), domainid);
			predicates.add(domainidPredicate);
		}
		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);
		TypedQuery<Group> q = em.createQuery(cq);
		Group group = q.getSingleResult();
		// tx.commit();
		return group;
		// } catch (RuntimeException e) {
		// if (tx != null && tx.isActive()) {
		// tx.rollback();
		// }
		// throw e;
		// } finally {
		// em.close();
		// }
	}

	public List<Group> listGroup(String domainid) {
		EntityManager em = getEntityManager();
		// EntityTransaction tx = null;
		// try {
		// tx = em.getTransaction();
		// tx.begin();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Group> cq = cb.createQuery(getEntityType());
		Root<Group> root = cq.from(getEntityType());
		List<Predicate> predicates = Lists.newArrayList();
		if (!Strings.isNullOrEmpty(domainid)) {
			Predicate domainidPredicate = cb.equal(root.get("domain").get("id"), domainid);
			predicates.add(domainidPredicate);
		}
		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);

		TypedQuery<Group> q = em.createQuery(cq);
		List<Group> groups = q.getResultList();
		// tx.commit();
		return groups;
		// } catch (RuntimeException e) {
		// if (tx != null && tx.isActive()) {
		// tx.rollback();
		// }
		// throw e;
		// } finally {
		// em.close();
		// }

	}

	public List<Group> listGroups(Hints hints) throws SecurityException, IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		return filterLimitQuery(Group.class, hints);
	}
}
