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

import com.google.common.collect.Lists;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.jpa.AbstractDao;
import com.infinities.keystone4j.model.identity.User;

public class UserDao extends AbstractDao<User> {

	public UserDao() {
		super(User.class);
	}

	// public List<User> listUsersForProject(String projectid) {
	// EntityManager em = getEntityManager();
	// CriteriaBuilder cb = em.getCriteriaBuilder();
	// CriteriaQuery<User> cq = cb.createQuery(User.class);
	// Root<User> root = cq.from(User.class);
	// Join<User, UserProjectGrant> grant = root.join("userProjectGrants");
	// cq.select(root).where(cb.equal(grant.get("project").get("id"),
	// projectid));
	// TypedQuery<User> q = em.createQuery(cq);
	// List<User> users = q.getResultList();
	//
	// return users;
	//
	// }

	public User findByName(String name, String domainid) {

		EntityManager em = getEntityManager();
		// EntityTransaction tx = null;
		// try {
		// tx = em.getTransaction();
		// tx.begin();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<User> cq = cb.createQuery(getEntityType());
		Root<User> root = cq.from(getEntityType());
		Path<String> path = root.get("name");
		List<Predicate> predicates = Lists.newArrayList();
		Predicate namePredicate = cb.equal(path, name);
		predicates.add(namePredicate);
		Predicate domainPredicate = cb.equal(root.get("domain").get("id"), domainid);
		predicates.add(domainPredicate);
		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);

		TypedQuery<User> q = em.createQuery(cq);
		User user = q.getSingleResult();
		// tx.commit();
		return user;

		// } catch (RuntimeException e) {
		// if (tx != null && tx.isActive()) {
		// tx.rollback();
		// }
		// throw e;
		// } finally {
		// em.close();
		// }

	}

	public List<User> listUsers(Hints hints) throws SecurityException, IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		return filterLimitQuery(User.class, hints);
	}
}
