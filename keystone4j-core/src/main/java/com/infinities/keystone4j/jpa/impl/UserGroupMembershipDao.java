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

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.google.common.collect.Lists;
import com.infinities.keystone4j.jpa.AbstractDao;
import com.infinities.keystone4j.model.identity.Group;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.model.identity.UserGroupMembership;

public class UserGroupMembershipDao extends AbstractDao<UserGroupMembership> {

	public UserGroupMembershipDao() {
		super(UserGroupMembership.class);
	}

	public List<User> listUserByGroup(String groupid) {
		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<User> cq = cb.createQuery(User.class);
		Root<User> root = cq.from(User.class);
		Join<User, UserGroupMembership> join = root.join("userGroupMemberships");
		cq.select(root).where(cb.equal(join.get("group").get("id"), groupid));
		TypedQuery<User> q = em.createQuery(cq);
		List<User> users = q.getResultList();

		return users;

	}

	public List<UserGroupMembership> listByGroup(String groupid) {
		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<UserGroupMembership> cq = cb.createQuery(UserGroupMembership.class);
		Root<UserGroupMembership> root = cq.from(UserGroupMembership.class);
		Predicate predicate = cb.equal(root.get("group").get("id"), groupid);
		cq.where(predicate);
		cq.select(root);

		TypedQuery<UserGroupMembership> q = em.createQuery(cq);
		List<UserGroupMembership> ret = q.getResultList();

		return ret;
	}

	public List<UserGroupMembership> listByUser(String userid) {
		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<UserGroupMembership> cq = cb.createQuery(UserGroupMembership.class);
		Root<UserGroupMembership> root = cq.from(UserGroupMembership.class);
		Predicate predicate = cb.equal(root.get("user").get("id"), userid);
		cq.where(predicate);
		cq.select(root);

		TypedQuery<UserGroupMembership> q = em.createQuery(cq);
		List<UserGroupMembership> ret = q.getResultList();

		return ret;
	}

	public List<Group> listGroupsByUser(String userid) {
		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Group> cq = cb.createQuery(Group.class);
		Root<Group> root = cq.from(Group.class);
		Join<Group, UserGroupMembership> join = root.join("userGroupMemberships");
		cq.select(root).where(cb.equal(join.get("user").get("id"), userid));
		TypedQuery<Group> q = em.createQuery(cq);
		List<Group> groups = q.getResultList();

		return groups;
	}

	public UserGroupMembership findByUserGroup(String userid, String groupid) {

		EntityManager em = getEntityManager();
		// EntityTransaction tx = null;
		// try {
		// tx = em.getTransaction();
		// tx.begin();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<UserGroupMembership> cq = cb.createQuery(UserGroupMembership.class);
		Root<UserGroupMembership> root = cq.from(UserGroupMembership.class);
		List<Predicate> predicates = Lists.newArrayList();
		Predicate userPredicate = cb.equal(root.get("user").get("id"), userid);
		predicates.add(userPredicate);
		Predicate groupPredicate = cb.equal(root.get("group").get("id"), groupid);
		predicates.add(groupPredicate);
		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.select(root);
		TypedQuery<UserGroupMembership> q = em.createQuery(cq);
		UserGroupMembership ret = q.getSingleResult();

		// tx.commit();
		return ret;
		// } catch (RuntimeException e) {
		// if (tx != null && tx.isActive()) {
		// tx.rollback();
		// }
		// throw e;
		// } finally {
		// em.close();
		// }
	}
}
