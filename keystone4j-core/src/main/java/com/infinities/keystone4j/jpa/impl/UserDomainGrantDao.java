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
//package com.infinities.keystone4j.jpa.impl;
//
//import java.util.List;
//
//import javax.persistence.EntityManager;
//import javax.persistence.TypedQuery;
//import javax.persistence.criteria.CriteriaBuilder;
//import javax.persistence.criteria.CriteriaQuery;
//import javax.persistence.criteria.Predicate;
//import javax.persistence.criteria.Root;
//
//import com.google.common.collect.Lists;
//import com.infinities.keystone4j.jpa.AbstractDao;
//import com.infinities.keystone4j.model.assignment.UserDomainGrant;
//
//public class UserDomainGrantDao extends AbstractDao<UserDomainGrant> {
//
//	public UserDomainGrantDao() {
//		super(UserDomainGrant.class);
//	}
//
//	public List<UserDomainGrant> listUserDomainGrant(String userid) {
//
//		EntityManager em = getEntityManager();
//		// EntityTransaction tx = null;
//		// try {
//		// tx = em.getTransaction();
//		// tx.begin();
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//		CriteriaQuery<UserDomainGrant> cq = cb.createQuery(UserDomainGrant.class);
//		Root<UserDomainGrant> root = cq.from(UserDomainGrant.class);
//		Predicate predicate = cb.equal(root.get("user").get("id"), userid);
//		cq.where(predicate);
//		cq.select(root);
//
//		TypedQuery<UserDomainGrant> q = em.createQuery(cq);
//		List<UserDomainGrant> grants = q.getResultList();
//		// tx.commit();
//		return grants;
//		// } catch (RuntimeException e) {
//		// if (tx != null && tx.isActive()) {
//		// tx.rollback();
//		// }
//		// throw e;
//		// } finally {
//		// em.close();
//		// }
//
//	}
//
//	public List<UserDomainGrant> findByUseridAndDomainid(String userid, String domainid) {
//
//		EntityManager em = getEntityManager();
//		// EntityTransaction tx = null;
//		// try {
//		// tx = em.getTransaction();
//		// tx.begin();
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//		CriteriaQuery<UserDomainGrant> cq = cb.createQuery(UserDomainGrant.class);
//		Root<UserDomainGrant> root = cq.from(UserDomainGrant.class);
//		List<Predicate> predicates = Lists.newArrayList();
//		Predicate groupPredicate = cb.equal(root.get("user").get("id"), userid);
//		predicates.add(groupPredicate);
//		Predicate projectPredicate = cb.equal(root.get("domain").get("id"), domainid);
//		predicates.add(projectPredicate);
//		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
//		cq.select(root);
//
//		TypedQuery<UserDomainGrant> q = em.createQuery(cq);
//		List<UserDomainGrant> grants = q.getResultList();
//		// tx.commit();
//		return grants;
//		// } catch (RuntimeException e) {
//		// if (tx != null && tx.isActive()) {
//		// tx.rollback();
//		// }
//		// throw e;
//		// } finally {
//		// em.close();
//		// }
//
//	}
//
//	public UserDomainGrant getGrant(String userid, String domainid, String roleid) {
//
//		EntityManager em = getEntityManager();
//		// EntityTransaction tx = null;
//		// try {
//		// tx = em.getTransaction();
//		// tx.begin();
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//		CriteriaQuery<UserDomainGrant> cq = cb.createQuery(UserDomainGrant.class);
//		Root<UserDomainGrant> root = cq.from(UserDomainGrant.class);
//		List<Predicate> predicates = Lists.newArrayList();
//		Predicate groupPredicate = cb.equal(root.get("user").get("id"), userid);
//		predicates.add(groupPredicate);
//		Predicate projectPredicate = cb.equal(root.get("domain").get("id"), domainid);
//		predicates.add(projectPredicate);
//		Predicate rolePredicate = cb.equal(root.get("role").get("id"), roleid);
//		predicates.add(rolePredicate);
//		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
//		cq.select(root);
//
//		TypedQuery<UserDomainGrant> q = em.createQuery(cq);
//		UserDomainGrant grant = q.getSingleResult();
//		// tx.commit();
//		return grant;
//		// } catch (RuntimeException e) {
//		// if (tx != null && tx.isActive()) {
//		// tx.rollback();
//		// }
//		// throw e;
//		// } finally {
//		// em.close();
//		// }
//
//	}
//
// }
